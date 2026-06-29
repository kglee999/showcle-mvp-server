package com.showcle.global.config;

import com.showcle.api.auth.service.MemberService;
import com.showcle.global.config.security.*;
import com.showcle.global.config.security.filter.RestAuthenticationFilter;
import com.showcle.global.config.security.provider.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.*;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig {

    @Value("${server.cors.allowed-origins}")
    private List<String> allowedOrigins;

    @Value("${server.cors.allowed-methods}")
    private List<String> allowedMethods;

    @Value("${server.cors.allowed-header}")
    private List<String> allowedHeader;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;

    // 로그인 URL
    public static final String SECURITY_LOGIN_URL = "/api/member/v1/signin";
    // 로그아웃 URL
    public static final String SECURITY_LOGOUT_URL = "/api/member/v1/signout";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // CORS 설정
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // CSRF 설정
            .csrf(AbstractHttpConfigurer::disable)
            // 기본 제공 로그인폼 사용하지 않음
            .formLogin(AbstractHttpConfigurer::disable)
            // 시큐리티 기본 로그인 모듈 사용하지 않음
            .httpBasic(AbstractHttpConfigurer::disable)
            // 세션 설정
            .sessionManagement(configurer -> configurer
                    .maximumSessions(1)                  // 같은 아이디로 1명만 로그인 할 수 있음
                    .maxSessionsPreventsLogin(false)     // 신규 로그인 사용자의 로그인이 허용되고, 기존 사용자는 세션아웃 됨
                    .expiredSessionStrategy(securitySessionExpiredStrategy()))
            // URL 설정
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(
                        SECURITY_LOGIN_URL,
                            SECURITY_LOGOUT_URL,
                            "/api/v1/auth/member",
                            "/api/v1/auth/email/available",
                            "/api/v1/auth/email/code/send",
                            "/api/v1/auth/email/code/verify",
                            "/api/v1/auth/email/find",
                            "/api/v1/auth/passwd/find"
                    ).permitAll()
                    .anyRequest().authenticated())
            // 에러 핸들링
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(customAuthenticationEntryPoint()).accessDeniedHandler(customAccessDeniedHandler()))

            .logout(configurer -> configurer
                    .logoutUrl(SECURITY_LOGOUT_URL)
                    .logoutSuccessHandler(customLogoutSuccessHandler()))

            // 헤더 옵션 ( header )
            .headers(configurer -> configurer
                    .frameOptions(option -> option.sameOrigin()))

            .addFilterBefore(restAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(allowedMethods);
        configuration.setAllowedHeaders(allowedHeader);
        // 세션 쿠키 허용
        configuration.setAllowCredentials(true);

        // 노출할 헤더 설정 (클라이언트에서 접근 가능한 헤더)
        configuration.setExposedHeaders(Arrays.asList(
            "Content-Type",
            "Authorization",
            "refreshToken",
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials",
            "Access-Control-Allow-Headers",
            "Access-Control-Allow-Methods",
            "Access-Control-Max-Age",
            "Access-Control-Expose-Headers"
        ));

        // 프리플라이트 요청 결과를 캐시하는 시간(초) - 1시간 설정
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    // 시큐리티 예외 처리
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) ->web.ignoring()
                .requestMatchers(
                "/img/**",
                "/css/**",
                "/js/**"
            );
    }

    // 인증 성공 후 세션을 어떻게 처리할지 결정하는 전략
    // SessionFixationProtectionStrategy 로그인 시 새 세션 ID 발급 (기본값, 세션 고정 공격 방어)
    // ChangeSessionIdAuthenticationStrategy 세션 ID만 변경 (세션 데이터 유지
    // ConcurrentSessionControlAuthenticationStrategy 동시 세션 수 제한
    // CompositeSessionAuthenticationStrategy 여러 전략 조합
    @Bean
    public CompositeSessionAuthenticationStrategy sessionAuthenticationStrategy() {
        // 동시 세션수 제한 옵션
        ConcurrentSessionControlAuthenticationStrategy concurrentSessionControlAuthenticationStrategy
                = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        // 신규 로그인 허용, 기존 세션 만료 처리 옵션
        concurrentSessionControlAuthenticationStrategy.setExceptionIfMaximumExceeded(false);

        // 새 세션 생성 + 기존 세션 속성 복사 (세션 고정 공격 방어)
        SessionFixationProtectionStrategy sessionFixationProtectionStrategy = new SessionFixationProtectionStrategy();
        // 세션 ID만 변경 (세션 객체 재사용) (세션 고정 공격 방어)
        ChangeSessionIdAuthenticationStrategy changeSessionIdAuthenticationStrategy = new ChangeSessionIdAuthenticationStrategy();

        // 인증 성공 후에 새 세션을 sessionRegistry 에 등록하는 역할
        RegisterSessionAuthenticationStrategy registerSessionStrategy = new RegisterSessionAuthenticationStrategy(sessionRegistry());

        CompositeSessionAuthenticationStrategy sessionAuthenticationStrategy
                = new CompositeSessionAuthenticationStrategy(
                    List.of(
                            concurrentSessionControlAuthenticationStrategy,     // 1. 동시 세션 수 체크 (먼저 세션 수 체크해서 초과면 기존 세션 만료)
                            changeSessionIdAuthenticationStrategy,              // 2. 세션 ID 변경
                            sessionFixationProtectionStrategy,                  // 3. 세션 ID 생성
                            registerSessionStrategy                             // 4. SessionRegistry 등록 ( 변경된 새 세션이 sessionRegistry 에 등록되어야 함 )
                    ));

        return sessionAuthenticationStrategy;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        CustomAuthenticationProvider provider = new CustomAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setMemberService(memberService);
        return new ProviderManager(provider);
    }

    // 현재 애플리케이션에 활성화된 세션과 사용자 목록을 메모리에서 관리하는 레지스트리
    // 로그인시마다 세션이 저장됨
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    // 로그인 성공시 실행되는 핸들러
    @Bean
    public RestAuthenticationSuccessHandler restAuthenticationSuccessHandler() {
        return new RestAuthenticationSuccessHandler();
    }

    // 로그인 실패시 실행되는 핸들러
    @Bean
    public RestAuthenticationFailuerHandler restAuthenticationFailuerHandler() {
        return new RestAuthenticationFailuerHandler();
    }

    // 세션 만료 처리 ( JSON 응답 반환 )
    @Bean
    public SecuritySessionExpiredStrategy securitySessionExpiredStrategy() {
        return new SecuritySessionExpiredStrategy();
    }

    // 로그아웃 ( JSON 반환 )
    @Bean
    public CustomLogoutSuccessHandler customLogoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    // 인증되지 않은 상태에서 접근시 예외 처리 ( JSON 반환 )
    // 401 Unauthorized 로그인 이전
    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    // AbstractAuthenticationProcessingFilter의 SecurityContextRepository 기본값이 NullSecurityContextRepository로 변경
    // 로그인 성공 후 세션에 SecurityContext가 저장되지 않아서 다음 요청에서 인증이 풀립니다.
    @Bean
    public HttpSessionSecurityContextRepository httpSessionSecurityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    // 권한 없음 에러 처리
    // 403 Forbidden 로그인했지만 권한 없음
    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    // 인증 방식 REST API 로 구현되도록 REST FILTER 추가
    public RestAuthenticationFilter restAuthenticationFilter() throws Exception {

        RequestMatcher loginMatcher = request -> request
                .getRequestURI().equals(SECURITY_LOGIN_URL) && HttpMethod.POST.name().equals(request.getMethod());

        RestAuthenticationFilter filter = new RestAuthenticationFilter(loginMatcher);
        filter.setAuthenticationManager(authenticationManager());
        filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy());
        filter.setSecurityContextRepository(httpSessionSecurityContextRepository());
        filter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(restAuthenticationFailuerHandler());
        return filter;
    }
}

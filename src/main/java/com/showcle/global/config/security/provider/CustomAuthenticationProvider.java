package com.showcle.global.config.security.provider;

import com.showcle.api.auth.dto.Member;
import com.showcle.api.auth.service.MemberService;
import com.showcle.global.config.security.principal.MemberPrincipal;
import com.showcle.global.model.LoginForm;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Setter
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private PasswordEncoder passwordEncoder;
    private MemberService memberService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        LoginForm form = (LoginForm) authentication.getPrincipal();
        log.info("email : {}", form.email());

        Member member = memberService.findByEmail(form.email());

        if (member == null) {
            throw new UsernameNotFoundException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        if (!passwordEncoder.matches(form.password(), member.getPasswd())) {
            throw new BadCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        // 최종 로그인일자 업데이트
        memberService.updateLastLoginDt(member.getIdx());

        MemberPrincipal principal = new MemberPrincipal(member);
        UsernamePasswordAuthenticationToken result = UsernamePasswordAuthenticationToken.authenticated(principal, member.getEmail(), principal.getAuthorities());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

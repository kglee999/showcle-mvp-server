package com.showcle.global.config.security;

import com.showcle.global.enums.ServiceResult;
import com.showcle.global.model.JsonResponse;
import com.showcle.global.util.CommonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

// 인증되지 않은 사용자가 접근하려고 할 때 처리하는 클래스
// 미 로그인 ( anonymous )
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        log.error(authException.getMessage());

        if(InsufficientAuthenticationException.class == authException.getClass()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8);

        String result = objectMapper.writeValueAsString(new JsonResponse<>(false, ServiceResult.UNAUTHORIZED.name(), null));
        response.getWriter().println(result);
    }
}

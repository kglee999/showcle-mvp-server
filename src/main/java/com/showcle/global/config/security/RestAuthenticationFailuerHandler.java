package com.showcle.global.config.security;

import com.showcle.global.enums.ServiceResult;
import com.showcle.global.model.JsonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

// 로그인 실패시 실행되는 핸들러
@Slf4j
public class RestAuthenticationFailuerHandler implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        log.debug(exception.getMessage());

        ServiceResult state = ServiceResult.BAD_REQUEST;

        if (UsernameNotFoundException.class == exception.getClass()) {
            state = ServiceResult.NOT_FOUND;
        } else if(BadCredentialsException.class == exception.getClass()) {
            state = ServiceResult.NOT_MATCH;
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8);

        String result = objectMapper.writeValueAsString(new JsonResponse<>(false, state.name(), exception.getMessage()));
        response.getWriter().println(result);
    }
}

package com.showcle.global.config.security;

import com.showcle.global.config.security.principal.MemberPrincipal;
import com.showcle.global.enums.ServiceResult;
import com.showcle.global.model.JsonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

// 로그인 성공시 실행되는 핸들러
@Slf4j
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        log.debug(authentication.getName());

        MemberPrincipal principal = (MemberPrincipal) authentication.getPrincipal();

        // 세션 정보 조회 후 리턴


        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8);

        String result = objectMapper.writeValueAsString(new JsonResponse<>(true, ServiceResult.SUCCESS.name(), principal.getIdx()));
        response.getWriter().println(result);
    }
}

package com.showcle.global.config.security;

import com.showcle.global.enums.ServiceResult;
import com.showcle.global.model.JsonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

// 인가되지 않은 사용자가 접근하려고 할 때 처리하는 클래스
// 권한 없음 오류 JSON 리턴
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8);

        String result = objectMapper.writeValueAsString(new JsonResponse<>(false, ServiceResult.FAIL.name(), null));
        response.getWriter().println(result);
    }
}

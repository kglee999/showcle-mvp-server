package com.showcle.global.config.security;

import com.showcle.global.config.security.principal.MemberPrincipal;
import com.showcle.global.enums.ServiceResult;
import com.showcle.global.model.JsonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

// 로그아웃 성공시 JSON 응답
@Slf4j
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, @Nullable Authentication authentication)
            throws IOException, ServletException {

        if(authentication != null && authentication.getPrincipal() instanceof MemberPrincipal) {
            MemberPrincipal member = (MemberPrincipal) authentication.getPrincipal();
            log.debug("### LOGOUT SUCCESS : {}", member.getUsername());
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8);

        String result = objectMapper.writeValueAsString(new JsonResponse<>(true, ServiceResult.SUCCESS.name(), null));
        response.getWriter().println(result);
    }
}

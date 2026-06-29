package com.showcle.global.config.security;

import com.showcle.global.enums.ServiceResult;
import com.showcle.global.model.JsonResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

// 세션 만료시 JSON 응답 반환
@Slf4j
public class SecuritySessionExpiredStrategy implements SessionInformationExpiredStrategy {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
        log.debug("### SESSION EXPIRED");
        HttpServletResponse response = event.getResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8);

        String result = objectMapper.writeValueAsString(new JsonResponse<>(false, ServiceResult.UNAUTHORIZED.name(), null));
        response.getWriter().println(result);
    }
}

package com.showcle.global.config.security.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

// SNS 로그인 성공시 해당 페이지 호출
// 세션 SecurityContext 생성 필요
// 리액트|앱 로그인 성공 페이지로 리턴
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        super.onAuthenticationSuccess(request, response, authentication);

        // 로그인 처리

        /*
        String state = request.getParameter("state");

        if ("app".equals(state)) {
            // 앱 딥링크로 리다이렉트 (커스텀 URL 스킴)
            response.sendRedirect("myapp://oauth2/success?memberId=" + principal.getIdx());

        } else {
            // 웹(React) 페이지로 리다이렉트 (세션 쿠키 자동 전달)
            response.sendRedirect("http://localhost:3000/oauth2/callback");
        }
        */

        // 로그인 성공 후에 앱, 프론트 페이지로 REDIRECT 해야 함
    }
}

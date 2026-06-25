package com.showcle.global.service;

import com.showcle.global.enums.Auth;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.sender}")
    private String sender;

    public void sendEmail(Auth.MailType type, Map<String, String> param, String to) {
        Resource resource = new ClassPathResource(type.getHtmlPath());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String content = sb.toString();

            // 파라미터 REPLACE
            for( String key : param.keySet() ){
                content = content.replace("#{" + key + "}", param.get(key));
            }

            // 메일 발송
            send(new String[]{to}, type.getSubject(), content);

        } catch(Exception e) {
            log.error(e.getMessage());
        }
    }

    @Async
    public void send(String[] to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(sender);
            helper.setSubject(subject);
            helper.setTo(to);
            helper.setText(content, true);  // true HTML
            mailSender.send(message);

        } catch(Exception e) {
            log.error(e.getMessage());
        }
    }
}

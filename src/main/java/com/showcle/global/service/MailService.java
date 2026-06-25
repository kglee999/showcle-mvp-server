package com.showcle.global.service;

import com.showcle.global.enums.MailType;
import com.showcle.global.util.CommonUtil;
import jakarta.mail.MessagingException;
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

    @Async
    public void sendEmail(MailType type, Map<String, String> param, String[] to) {
        Resource resource = new ClassPathResource(type.getHtmlPath());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String content = sb.toString();

            // 파라미터 REPLACE
            for (Map.Entry<String, String> entry : param.entrySet()) {
                content = content.replace("#{" + entry.getKey() + "}", entry.getValue());
            }

            // 메일 발송
            send(to, type.getSubject(), content);

        } catch(Exception e) {
            log.error(CommonUtil.printException(e));
        }
    }

    public void send(String[] to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
        helper.setFrom(sender);
        helper.setSubject(subject);
        helper.setTo(to);
        helper.setText(content, true);  // true HTML
        mailSender.send(message);
    }
}

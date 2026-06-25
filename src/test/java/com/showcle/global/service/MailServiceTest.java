package com.showcle.global.service;

import com.showcle.global.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
@Slf4j
class MailServiceTest {

    @Test
    public void sendMailTest() {
        String code = CommonUtil.generateAuthCode();
        log.info(code);
    }

}
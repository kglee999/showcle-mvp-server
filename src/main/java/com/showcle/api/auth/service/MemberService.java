package com.showcle.api.auth.service;

import com.showcle.api.auth.dto.Member;
import com.showcle.api.auth.dto.MemberAuth;
import com.showcle.api.auth.mapper.MemberMapper;
import com.showcle.global.enums.Auth;
import com.showcle.global.service.MailService;
import com.showcle.global.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MailService mailService;
    private final MemberMapper memberMapper;

    // 이메일 중복 체크
    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        Member member = memberMapper.findByEmail(email);
        return member == null;
    }

    // 이메일 인증번호 발송
    @Transactional
    public boolean sendEmailCode(String email) {
        String code = CommonUtil.generateAuthCode();

        // 메일 발송
        Map<String, String> params = new HashMap<>();
        params.put("subject", "이메일 인증번호 발송");
        params.put("code", code);
        mailService.sendEmail(Auth.MailType.EMAIL_VERIFY, params, email);

        // 디비 저장 (인증 만료시간 5분 )
        int result = memberMapper.insertMemberMailAuth(email, code);
        return result > 0;
    }

    // 이메일 인증번호 조회
    @Transactional(readOnly = true)
    public MemberAuth findLatestMemberMailCode(String email) {
        return memberMapper.findLatestMemberMailCode(email);
    }

    // 이메일 인증번호 인증 완료 업데이트
    @Transactional
    public boolean updateMemberMailAuth(int idx) {
        return memberMapper.updateMemberMailAuth(idx) > 0;
    }

    // 이메일 인증 완료 여부 확인
    @Transactional(readOnly = true)
    public boolean isEmailVerified(String email) {
        return memberMapper.isEmailVerified(email) > 0;
    }
}

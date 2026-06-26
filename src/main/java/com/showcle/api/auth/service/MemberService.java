package com.showcle.api.auth.service;

import com.showcle.api.auth.dto.Member;
import com.showcle.api.auth.dto.MemberAuth;
import com.showcle.api.auth.mapper.MemberMapper;
import com.showcle.global.enums.ErrorCode;
import com.showcle.global.enums.FileType;
import com.showcle.global.enums.MailType;
import com.showcle.global.exception.BusinessException;
import com.showcle.global.interfaces.FileUploader;
import com.showcle.global.model.FileModel;
import com.showcle.global.service.MailService;
import com.showcle.global.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final FileUploader localFileUploadService;
    private final PasswordEncoder passwordEncoder;

    // 이메일 중복 체크
    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        Member member = memberMapper.findByEmail(email);
        return member == null;
    }

    // 이메일 인증번호 발송
    @Transactional
    public void sendEmailCode(String email) {
        String code = CommonUtil.generateAuthCode();

        // 디비 저장 (인증 만료시간 5분 )
        int result = memberMapper.insertMemberMailAuth(email, code);

        if(result <= 0) {
            throw new BusinessException(ErrorCode.SERVER_DB_UPDATE_ERROR);
        }

        // 메일 발송 (디비 저장실패시 메일이 발송되면 안됨)
        Map<String, String> params = new HashMap<>();
        params.put("subject", MailType.EMAIL_VERIFY.getSubject());
        params.put("code", code);
        mailService.sendEmail(MailType.EMAIL_VERIFY, params, new String[]{ email });
    }

    // 이메일 인증번호 인증
    @Transactional
    public void verifyEmail(String email, String code) {
        MemberAuth auth = memberMapper.findLatestMemberMailCode(email);

        if (auth == null) {
            throw new BusinessException(ErrorCode.EMAIL_CODE_EXPRIED);
        }
        if (!auth.getCode().equals(code)) {
            throw new BusinessException(ErrorCode.EMAIL_CODE_NOT_MATCH);
        }

        int result = memberMapper.updateMemberMailAuth(auth.getIdx());

        if(result <= 0) {
            throw new BusinessException(ErrorCode.SERVER_DB_UPDATE_ERROR);
        }
    }

    // 이메일 인증 완료 여부 확인
    @Transactional(readOnly = true)
    public boolean isEmailVerified(String email) {
        return memberMapper.isEmailVerified(email) > 0;
    }

    // 회원 정보 저장
    @Transactional
    public void saveMember(Member param) {
        // 이메일 인증 여부 확인
        boolean isVerified = isEmailVerified(param.getEmail());
        if(!isVerified) {
            throw new BusinessException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        // 이메일 중복 여부 확인
        boolean isAvailable = isEmailAvailable(param.getEmail());
        if(!isAvailable) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 프로필 이미지 업로드
        FileModel model = localFileUploadService.upload(FileType.MEMBER_PROFILE, param.getProfileImgFile());
        // 이미지 파일 idx 저장
        param.setProfileImg(model.getIdx());
        // 패스워드 암호화
        param.setPasswd(passwordEncoder.encode(param.getPasswd()));
        // 전화번호 숫자빼고 삭제 후 저장
        param.setPhone(CommonUtil.removeNonNumber(param.getPhone()));

        // 회원정보 저장
        int result = memberMapper.insert(param);
        if(result <= 0) {
            throw new BusinessException(ErrorCode.SERVER_DB_UPDATE_ERROR);
        }
    }
}

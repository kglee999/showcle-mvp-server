package com.showcle.api.auth.controller;

import com.showcle.api.auth.dto.Member;
import com.showcle.api.auth.dto.MemberAuth;
import com.showcle.api.auth.dto.RequestCode;
import com.showcle.api.auth.dto.RequestEmail;
import com.showcle.api.auth.service.MemberService;
import com.showcle.global.controller.CommonController;
import com.showcle.global.enums.ErrorCode;
import com.showcle.global.exception.BusinessException;
import com.showcle.global.exception.CustomValidationException;
import com.showcle.global.model.JsonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class MemberController extends CommonController {

    private final MemberService memberService;

    // 회원 가입
    @PostMapping(value = "/member", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResponse<Object>> register(@Validated @ModelAttribute Member member, Errors errors) {
        if(errors.hasErrors()) {
            throw new CustomValidationException(validateHandling(errors));
        }

        // 이메일 인증 여부 확인
        boolean isVerified = memberService.isEmailVerified(member.getEmail());
        if(!isVerified) {
            throw new BusinessException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        // 이메일 중복 여부 확인
        boolean isAvailable = memberService.isEmailAvailable(member.getEmail());
        if(!isAvailable) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 프로필 이미지 업로드


        // 맴버 정보 저장
        // member.getProfileFile()
        return ResponseEntity.ok(new JsonResponse<>(true));
    }

    // 이메일 사용 가능 여부 확인
    @GetMapping("/email/available")
    public ResponseEntity<JsonResponse<Object>> isEmailAvailable(@Validated @RequestParam("email") String email, Errors errors) {
        if(errors.hasErrors()) {
            throw new CustomValidationException(validateHandling(errors));
        }

        boolean result = memberService.isEmailAvailable(email);
        return ResponseEntity.ok(new JsonResponse<>(result));
    }

    // 이메일 인증번호 발송
    @PostMapping("/email/code/send")
    public ResponseEntity<JsonResponse<Object>> sendEmail(@Validated @RequestBody RequestEmail body, Errors errors) {
        if(errors.hasErrors()) {
            throw new CustomValidationException(validateHandling(errors));
        }

        // 인증코드 메일 발송
        boolean result = memberService.sendEmailCode(body.getEmail());
        return ResponseEntity.ok(new JsonResponse<>(result));
    }

    // 이메일 인증번호 인증
    @PostMapping("/email/code/verify")
    public ResponseEntity<JsonResponse<Object>> verifyEmail(@Validated @RequestBody RequestCode body, Errors errors) {
        if(errors.hasErrors()) {
            throw new CustomValidationException(validateHandling(errors));
        }

        MemberAuth auth = memberService.findLatestMemberMailCode(body.getEmail());

        if (auth == null) {
            throw new BusinessException(ErrorCode.EMAIL_CODE_EXPRIED);
        }
        if (!auth.getCode().equals(body.getCode())) {
            throw new BusinessException(ErrorCode.EMAIL_CODE_NOT_MATCH);
        }

        boolean result = memberService.updateMemberMailAuth(auth.getIdx());
        return ResponseEntity.ok(new JsonResponse<>(result));
    }
}

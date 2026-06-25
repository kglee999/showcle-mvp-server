package com.showcle.api.auth.controller;

import com.showcle.api.auth.dto.Member;
import com.showcle.api.auth.dto.CodeRequest;
import com.showcle.api.auth.dto.EmailRequest;
import com.showcle.api.auth.service.MemberService;
import com.showcle.global.controller.CommonController;
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
    public ResponseEntity<JsonResponse<Object>> saveMember(@Validated @ModelAttribute Member member, Errors errors) {
        checkValidation(errors);

        memberService.saveMember(member);
        return ResponseEntity.ok(new JsonResponse<>(null));
    }

    // 이메일 사용 가능 여부 확인
    @GetMapping("/email/available")
    public ResponseEntity<JsonResponse<Object>> emailAvailable(@RequestParam("email") String email) {
        boolean result = memberService.isEmailAvailable(email);
        return ResponseEntity.ok(new JsonResponse<>(result));
    }

    // 이메일 인증번호 발송
    @PostMapping("/email/code/send")
    public ResponseEntity<JsonResponse<Object>> sendEmail(@Validated @RequestBody EmailRequest body, Errors errors) {
        checkValidation(errors);

        memberService.sendEmailCode(body.getEmail());
        return ResponseEntity.ok(new JsonResponse<>(null));
    }

    // 이메일 인증번호 인증
    @PostMapping("/email/code/verify")
    public ResponseEntity<JsonResponse<Object>> verifyEmail(@Validated @RequestBody CodeRequest body, Errors errors) {
        checkValidation(errors);

        memberService.verifyEmail(body.getEmail(), body.getCode());
        return ResponseEntity.ok(new JsonResponse<>(null));
    }
}

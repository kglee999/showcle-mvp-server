package com.showcle.api.auth.controller;

import com.showcle.api.auth.dto.*;
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
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class MemberController extends CommonController {

    private final MemberService memberService;

    // 회원 가입
    @PostMapping(value = "/member", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResponse<Object>> saveMember(@Validated MemberRequest param, Errors errors) {
        checkValidation(errors);

        memberService.saveMember(param);
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

        memberService.sendEmailCode(body.email());
        return ResponseEntity.ok(new JsonResponse<>(null));
    }

    // 이메일 인증번호 인증
    @PostMapping("/email/code/verify")
    public ResponseEntity<JsonResponse<Object>> verifyEmail(@Validated @RequestBody EmailVerifyRequest body, Errors errors) {
        checkValidation(errors);

        memberService.verifyEmail(body.email(), body.code());
        return ResponseEntity.ok(new JsonResponse<>(null));
    }

    // 이메일 찾기
    @PostMapping("/email/find")
    public ResponseEntity<JsonResponse<Object>> findEmail(@Validated @RequestBody EmailFindRequest body, Errors errors) {
        checkValidation(errors);

        List<Member.EmailFindResponse> result = memberService.findEmail(body);
        return ResponseEntity.ok(new JsonResponse<>(result));
    }

    // 비밀번호 찾기
    @PostMapping("/passwd/find")
    public ResponseEntity<JsonResponse<Object>> findPasswd(@Validated @RequestBody PasswdFindRequest body, Errors errors) {
        checkValidation(errors);

        memberService.findPasswd(body);
        return ResponseEntity.ok(new JsonResponse<>(null));
    }
}

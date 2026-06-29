package com.showcle.api.proj.controller;

import com.showcle.global.controller.CommonController;
import com.showcle.global.model.JsonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/proj")
@RequiredArgsConstructor
public class ProjectController extends CommonController {

    // 프로젝트 목록 테스트
    @GetMapping("/list")
    public ResponseEntity<JsonResponse<Object>> list() {
        return ResponseEntity.ok(new JsonResponse<>(null));
    }
}

package com.showcle.api.com.controller;

import com.showcle.api.com.dto.CountryCode;
import com.showcle.api.com.service.ComService;
import com.showcle.global.controller.CommonController;
import com.showcle.global.model.JsonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/com")
@RequiredArgsConstructor
public class ComController extends CommonController {

    private final ComService comService;

    // 국가 코드 조회
    @GetMapping("/country-code")
    public ResponseEntity<JsonResponse<Object>> getCountryCodeList() {
        List<CountryCode> countryCodeList = comService.findCountryCodeList();
        return ResponseEntity.ok(new JsonResponse<>(countryCodeList));
    }
}

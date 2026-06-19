package com.showcle.domain.sample.controller;

import com.showcle.domain.sample.dto.Sample;
import com.showcle.domain.sample.service.SampleService;
import com.showcle.global.controller.CommonController;
import com.showcle.global.model.JsonResponse;
import com.showcle.global.enums.ServiceResult;
import com.showcle.global.exception.CustomValidationException;
import com.showcle.global.util.ExcelDownloadHelper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/sample")
@RequiredArgsConstructor
public class SampleController extends CommonController {

    private final SampleService sampleService;

    private final ExcelDownloadHelper excelHelper;

    @GetMapping("")
    public ResponseEntity<JsonResponse<List<Sample>>> sampleIndex() {
        List<Sample> sampleList = sampleService.findAll();
        return ResponseEntity.ok(new JsonResponse<>(true, ServiceResult.SUCCESS.name(), sampleList));
    }

    @PostMapping("")
    public ResponseEntity<JsonResponse<Object>> samplePost(@Validated @RequestBody Sample sample, Errors errors) {
        /*
        @ValidEnum(enumClass = Code.PURCHASE_TYPE.class, ignoreCase = true) @NotEmpty String purchaseType,
        @ValidEnum(enumClass = Code.PRODUCT_TYPE.class, ignoreCase = true) @NotEmpty String productType
        */

        if(errors.hasErrors()) {
            throw new CustomValidationException(validateHandling(errors));
        }

        sampleService.insert(sample);
        return ResponseEntity.ok(new JsonResponse<>(sample));
    }

    @PutMapping("/{idx}")
    public ResponseEntity<JsonResponse<Sample>> samplePut(@PathVariable("idx") Integer idx, @RequestBody Sample sample) {
        sample.setIdx(idx);
        sampleService.update(sample);
        return ResponseEntity.ok(new JsonResponse<>(sample));
    }

    @DeleteMapping
    public ResponseEntity<JsonResponse<Void>> sampleDelete() {
        sampleService.deleteAll();
        return ResponseEntity.ok(new JsonResponse<>(null));
    }

    @GetMapping("/excel")
    public void excel(HttpServletResponse response) {

        List<Sample> sampleList = sampleService.findAll();
        excelHelper.export(response, sampleList);
    }

}

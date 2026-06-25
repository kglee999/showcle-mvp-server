package com.showcle.infra.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class HolidayServiceTest {

    @Test
    public void test() {
        /*MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("serviceKey", "IfoaRCupG7VK+0u8DONLdOdVyxCXmx1sI22LQGaORawgOmyvxpO4fUpVM73WglRw48XstC+9MvV9wsG/flQTwg==");
        params.add("solYear", "2026");
        params.add("numOfRows", "10000");
        params.add("pageNo", "1");
        params.add("_type", "json");

        RestClient rest = RestClient.builder()
                .baseUrl("https://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getHoliDeInfo")
                *//*.requestInterceptor((request, body, execution) -> {
                    log.debug("[REQ] {} {}", request.getMethod(), request.getURI());
                    log.debug("[REQ] Headers: {}", request.getHeaders());
                    log.debug("[REQ] Body: {}", new String(body, StandardCharsets.UTF_8));

                    ClientHttpResponse response = execution.execute(request, body);

                    byte[] responseBody = response.getBody().readAllBytes();
                    log.debug("[RES] Status: {}", response.getStatusCode());
                    log.debug("[RES] Body: {}", new String(responseBody, StandardCharsets.UTF_8));

                    // 읽은 바디를 다시 감싸서 반환
                    // return new BufferedClientHttpResponse(response, responseBody);
                    return response;
                })*//*
                .build();

        String aaa = rest.get().uri(builder -> builder.queryParams(params).build()).retrieve().body(String.class);
        log.info(aaa);*/
    }
}
package com.showcle.api.com.service;

import com.showcle.api.com.dto.CountryCode;
import com.showcle.api.com.mapper.ComMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ComService {

    private final ComMapper comMapper;

    // 국가 코드 조회
    @Cacheable(value = "countryCode")
    @Transactional(readOnly = true)
    public List<CountryCode> findCountryCodeList() {
        return comMapper.findCountryCodeList();
    }
}

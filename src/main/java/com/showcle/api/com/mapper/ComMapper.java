package com.showcle.api.com.mapper;

import com.showcle.api.com.dto.CountryCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ComMapper {

    // 국가 코드 조회
    List<CountryCode> findCountryCodeList();
}
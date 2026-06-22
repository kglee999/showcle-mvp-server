package com.showcle.api.sample.mapper;

import com.showcle.api.sample.dto.Sample;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SampleMapper {

    List<Sample> findAll();

    int insert(Sample sample);

    int update(Sample sample);

    int deleteAll();
}

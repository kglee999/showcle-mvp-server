package com.showcle.domain.sample.service;

import com.showcle.domain.sample.dto.Sample;
import com.showcle.domain.sample.mapper.SampleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleMapper sampleMapper;

//  @Cacheable(value = "sample")
    @Transactional(readOnly = true)
    public List<Sample> findAll() {
        return sampleMapper.findAll();
    }

//  @CacheEvict(value = "sample", allEntries = true)
    @Transactional
    public int insert(Sample sample) {
        return sampleMapper.insert(sample);
    }

//  @CacheEvict(value = "sample", allEntries = true)
    @Transactional
    public int update(Sample sample) {
        return sampleMapper.update(sample);
    }

//  @CacheEvict(value = "sample", allEntries = true)
    @Transactional
    public int deleteAll() {
        return sampleMapper.deleteAll();
    }
}

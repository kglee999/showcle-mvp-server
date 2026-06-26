package com.showcle.global.mapper;

import com.showcle.global.model.FileModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {

    // 파일 저장
    int insert(FileModel fileModel);
}

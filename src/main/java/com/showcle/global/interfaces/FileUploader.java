package com.showcle.global.interfaces;

import com.showcle.global.enums.FileType;
import com.showcle.global.model.FileModel;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {
    FileModel upload(FileType type, MultipartFile fileList);
}

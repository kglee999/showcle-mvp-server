package com.showcle.global.service;

import com.showcle.global.enums.FileType;
import com.showcle.global.exception.BusinessException;
import com.showcle.global.interfaces.FileUploader;
import com.showcle.global.model.FileModel;
import com.showcle.global.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.showcle.global.enums.ErrorCode.FILE_UPLOAD_BAD_EXTENSION;
import static com.showcle.global.enums.ErrorCode.FILE_UPLOAD_ERROR;

@Service
@Slf4j
public class LocalFileUploadService implements FileUploader {

    @Value("${server.file.storage-path}")
    private String storagePath;
    @Value("${server.file.web-path}")
    private String webPath;

    // 실행 파일 및 특정 확장자는 업로드되지 않도록 처리
    private static final String[] BAD_EXTENSION = { "jsp", "php", "asp", "html", "perl", "exe", "class", "js", "lnk", "pif", "msi", "vbs", "inf", "reg"};
    // 디렉토리 생성 날짜 형식
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public FileModel upload(FileType type, MultipartFile mpf) {

        if(type == null || mpf.isEmpty()) {
            throw new BusinessException(FILE_UPLOAD_ERROR);
        }

        try {
            LocalDateTime today = LocalDateTime.now();
            String dateDir = today.format(DATE_FORMATTER);

            Path saveDir = Paths.get(storagePath, type.getPath(), dateDir).toAbsolutePath().normalize();
            Path webDir = Paths.get(webPath, type.getPath(), dateDir).toAbsolutePath().normalize();

            String contentType = mpf.getContentType();
            String realFileName = mpf.getOriginalFilename();
            long fileSize = mpf.getSize();

            String uuid = UUID.randomUUID().toString().replace("-", "");
            String ext = realFileName.substring(realFileName.lastIndexOf(".") + 1);
            String saveFileName = uuid + "." + ext;

            // 불량 확장자가 존재할때..
            for (String badExt : BAD_EXTENSION) {
                if (ext.equalsIgnoreCase(badExt)) {
                    throw new BusinessException(FILE_UPLOAD_BAD_EXTENSION);
                }
            }

            // 디렉토리 생성
            Files.createDirectories(saveDir);

            try (InputStream input = mpf.getInputStream();
                 BufferedOutputStream output = new BufferedOutputStream(Files.newOutputStream(saveDir.resolve(saveFileName)))
            ) {
                byte[] buffer = new byte[8192];
                int length;

                while((length = input.read(buffer)) != -1) {
                    output.write(buffer, 0, length);
                }
            }

            FileModel model = FileModel.builder()
                    .ownerIdx(0)
                    .type(type)
                    .fileType(contentType)
                    .fileSize(fileSize)
                    .saveFileName(saveFileName)
                    .realFileName(realFileName)
                    .savePath(saveDir.toString())
                    .webPath(webDir.toString())
                    .fileKey(uuid)
                    .build();

            // FILE 테이블 저장
            return model;

        } catch(BusinessException e) {
            throw e;
        } catch(Exception e) {
            log.error(CommonUtil.printException(e));
            throw new BusinessException(FILE_UPLOAD_ERROR);
        }
    }
}

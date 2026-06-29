package com.showcle.global.service;

import com.showcle.global.constants.Constant;
import com.showcle.global.enums.FileType;
import com.showcle.global.exception.BusinessException;
import com.showcle.global.interfaces.FileUploader;
import com.showcle.global.mapper.FileMapper;
import com.showcle.global.model.FileModel;
import com.showcle.global.util.CommonUtil;
import lombok.RequiredArgsConstructor;
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
import java.util.UUID;
import static com.showcle.global.enums.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocalFileUploadService implements FileUploader {

    @Value("${server.file.storage-path}")
    private String storagePath;
    @Value("${server.file.web-path}")
    private String webPath;

    private final FileMapper fileMapper;

    @Override
    public FileModel upload(FileType type, MultipartFile mpf) {

        if(type == null || mpf.isEmpty()) return null;

        try {
            LocalDateTime today = LocalDateTime.now();
            String dateDir = today.format(Constant.DATE_FORMATTER1);

            Path saveDir = Paths.get(storagePath, type.getPath(), dateDir).toAbsolutePath().normalize();
            Path webDir = Paths.get(webPath, type.getPath(), dateDir).toAbsolutePath().normalize();

            String contentType = mpf.getContentType();
            String realFileName = mpf.getOriginalFilename();
            long fileSize = mpf.getSize();

            String uuid = UUID.randomUUID().toString().replace("-", "");
            String ext = realFileName.substring(realFileName.lastIndexOf(".") + 1);
            String saveFileName = uuid + "." + ext;

            // 불량 확장자가 존재할때..
            for (String badExt : Constant.BAD_EXTENSION) {
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
                    .type(type.name())
                    .fileType(contentType)
                    .fileSize(fileSize)
                    .saveFileName(saveFileName)
                    .realFileName(realFileName)
                    .savePath(saveDir.toString())
                    .webPath(webDir.toString())
                    .fileKey(uuid)
                    .build();

            int result = fileMapper.insert(model);

            if(result <= 0) {
                throw new BusinessException(SERVER_DB_UPDATE_ERROR);
            }
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

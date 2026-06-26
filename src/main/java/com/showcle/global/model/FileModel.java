package com.showcle.global.model;

import com.showcle.global.enums.FileType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class FileModel {

    private int idx;
    private int ownerIdx;
    private String type;
    private String fileType;
    private long fileSize;
    private String saveFileName;
    private String realFileName;
    private String savePath;
    private String webPath;
    private String fileKey;
    private int discarded;
    private String createdBy;
}

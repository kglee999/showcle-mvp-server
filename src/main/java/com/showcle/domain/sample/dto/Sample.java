package com.showcle.domain.sample.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.showcle.global.annotation.ExcelColumn;
import com.showcle.global.annotation.ExcelExport;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@ExcelExport(sheetName = "샘플", title = "샘플")
public class Sample {

    @ExcelColumn(order = 1, title = "일련번호")
    private int idx;
    @ExcelColumn(order = 2, title = "이름")
    @NotEmpty
    private String name;
    @ExcelColumn(order = 3, title = "불")
    private int bool;
    @ExcelColumn(order = 4, title = "날짜")
    private LocalDate date;

    @ExcelColumn(order = 5, title = "생성일")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @ExcelColumn(order = 6, title = "종료일")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private OffsetDateTime updatedAt;
}

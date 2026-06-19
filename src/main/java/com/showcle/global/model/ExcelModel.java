package com.showcle.global.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.poi.ss.usermodel.CellType;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ExcelModel {
    private int order;
    private String title;
    private String fieldName;
    private CellType cellType;

    public String getMethod() {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}

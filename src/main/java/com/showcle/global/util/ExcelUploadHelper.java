package com.showcle.global.util;

import com.showcle.global.annotation.ExcelRead;
import com.showcle.global.exception.ExcelUploadException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelUploadHelper {

    public static final int DEFAULT_START_ROW = 1;
    public static final int MAX_EXCEL_READ_ROWS = 2000;

    public <T> List<T> read(MultipartFile mpf, T t) {
        return read(mpf, DEFAULT_START_ROW, t);
    }

    public <T> List<T> read(MultipartFile mpf, int startRow, T t) {
        if(mpf.isEmpty() || t == null) throw new ExcelUploadException("EXCEL FILE ERROR");

        try (Workbook workbook = WorkbookFactory.create(mpf.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Field[] fieldList = t.getClass().getDeclaredFields();

            // EXCEL SHEET VALIDATION
            if(sheet == null) throw new ExcelUploadException("EXCEL SHEET ERROR");
            if(startRow < 0) throw new ExcelUploadException("INVALID START ROW");
            if(sheet.getPhysicalNumberOfRows() - startRow > MAX_EXCEL_READ_ROWS) throw new ExcelUploadException("EXCEL ROW LIMIT EXCEEDED");

            List<T> list = new ArrayList<>();
            T data = null;

            for(int i = startRow ; i < sheet.getPhysicalNumberOfRows() ; i++) {
                Row row = sheet.getRow(i);
                Cell cell;
                data = (T) t.getClass().getDeclaredConstructor().newInstance();

                // 시트 첫 셀값이 없을 경우 다음 ROW 로 진행
                if (row != null && row.getCell(0) != null) {
                    for (Field field : fieldList) {
                        ExcelRead read = field.getAnnotation(ExcelRead.class);

                        if (read != null) {
                            cell = row.getCell(read.order());

                            if (cell != null && cell.getCellType() != CellType.BLANK) {
                                cell.setCellType(CellType.STRING);
                                field.setAccessible(true);
                                field.set(data, cell.getStringCellValue());
                            }
                        }
                    }
                    list.add(data);
                }
            }
            return list;

        } catch (Exception e) {
            log.error(CommonUtil.printException(e));
            throw new ExcelUploadException(e.getMessage());
        }
    }
}

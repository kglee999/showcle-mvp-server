package com.showcle.global.util;

import com.showcle.global.annotation.ExcelColumn;
import com.showcle.global.annotation.ExcelExport;
import com.showcle.global.exception.ExcelDownloadException;
import com.showcle.global.model.ExcelModel;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class ExcelDownloadHelper {

    public <T> void export(HttpServletResponse response, List<T> list) {

		int rownum = 0;
		int colnum = 0;

		try (SXSSFWorkbook workbook = new SXSSFWorkbook();
			 ServletOutputStream out = response.getOutputStream()) {

			if (CollectionUtils.isEmpty(list)) throw new ExcelDownloadException("export file is empty");

			ExcelExport excel = list.get(0).getClass().getAnnotation(ExcelExport.class);
			List<ExcelModel> modelList = getModelList(list);

			// 임시 파일 생성시 압축해서 생성
			workbook.setCompressTempFiles(true);
			// 시트 생성
			SXSSFSheet sheet = workbook.createSheet(excel.sheetName());
			// 메모리 행 1000개씩 flush 로 처리 ( Out of memory 방지 )
			sheet.setRandomAccessWindowSize(1000);

			// 셀 스타일 생성
			CellStyle titleStyle = titleStyle(workbook);
			CellStyle headerStyle = headerStyle(workbook);
			CellStyle bodyStyle = bodyStyle(workbook);

			// 타이틀 영역
			SXSSFRow row = sheet.createRow(rownum++);
			SXSSFCell cell = row.createCell(colnum, CellType.STRING);
			cell.setCellStyle(titleStyle);
			cell.setCellValue(excel.title());

			rownum++;

			// 헤더 영역
			row = sheet.createRow(rownum++);

			for(ExcelModel model : modelList) {
				cell = row.createCell(colnum++, CellType.STRING);
				cell.setCellStyle(headerStyle);
				cell.setCellValue(model.getTitle());
			}

			// 본문 영역
			for(T t : list) {
				row = sheet.createRow(rownum++);
				colnum = 0;

				for(ExcelModel model : modelList) {
					cell = row.createCell(colnum++, model.getCellType());
					cell.setCellStyle(bodyStyle);
					Object value = getValue(t, model);

					if (value != null) {
						if (value instanceof String str) {
							cell.setCellValue(str);
						} else if (value instanceof Double de) {
							cell.setCellValue(de);
						} else if (value instanceof Date dt) {
							cell.setCellValue(dt);
						} else {
							cell.setCellValue(value.toString());
						}
					}
				}
			}

			StringBuilder sb = new StringBuilder();
			sb.append(excel.sheetName()).append("_").append(new Date().getTime()).append(".xlsx");
			// 공백이 + 로 다운로드 되는 부분 수정
			String fileName = UriUtils.encode(sb.toString(), StandardCharsets.UTF_8).replace("\\+", "%20");

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="+ fileName);
			workbook.write(out);

		} catch (Exception e) {
			log.error(CommonUtil.printException(e));
			throw new ExcelDownloadException(e.getMessage());
		}
	}

	public <T> List<ExcelModel> getModelList(List<T> list) {
		Field[] fields = list.get(0).getClass().getDeclaredFields();

		return Arrays.stream(fields)
				.filter(field -> field.isAnnotationPresent(ExcelColumn.class))
				.map(field -> {
					ExcelColumn column = field.getAnnotation(ExcelColumn.class);
					return new ExcelModel(column.order(), column.title(), field.getName(), column.cellType());
				}).sorted(Comparator.comparingInt(ExcelModel::getOrder)).toList();
	}

	public <T> Object getValue(T t, ExcelModel model) {
        try {
			Method method = t.getClass().getDeclaredMethod(model.getMethod());
			return method.invoke(t);

        } catch (Exception e) {
			log.error(e.getMessage());
            return "";
        }
    }

	// 상단 타이틀 스타일
	private CellStyle titleStyle(SXSSFWorkbook workbook) {
		CellStyle style = workbook.createCellStyle();

		Font font = workbook.createFont();
		font.setColor(IndexedColors.BROWN.getIndex());
		font.setFontHeightInPoints((short) 20);
		font.setBold(true);
		style.setFont(font);
		return style;
	}

	// 헤더 영역 스타일
	private CellStyle headerStyle(SXSSFWorkbook workbook) {
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);

		Font font = workbook.createFont();
		font.setBold(true);
		style.setFont(font);
		return style;
	}

	// 바디 영역 스타일
	private CellStyle bodyStyle(SXSSFWorkbook workbook) {
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		return style;
	}
}

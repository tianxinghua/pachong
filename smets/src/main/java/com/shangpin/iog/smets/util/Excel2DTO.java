package com.shangpin.iog.smets.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author houkun
 * @version 20160203
 */
public class Excel2DTO {
	/**
	 * 将excel文件转换成dto
	 * @param filePath excel路径
	 * @param sheetNum 要转换excel的第几个sheet
	 * @param needColsNo 需要excel的哪几列
	 * @param clazz 要转换成的dto
	 * @return List<DTO>
	 */
	@SuppressWarnings("resource")
	public <T> List<T> toDTO(String filePath, Integer sheetNum,
			Short[] needColsNo, Class<T> clazz) {
		List<T> returnList = new ArrayList<T>();
		XSSFWorkbook wb = null;
		
		XSSFSheet sheet = null;
		try {
			wb = new XSSFWorkbook(filePath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		XSSFRow row = null;
		sheet = wb.getSheetAt(sheetNum);
		for (int j = 1; j < sheet.getPhysicalNumberOfRows(); j++) {
			row = sheet.getRow(j);
			try {
				T t = fillDTO(clazz.newInstance(), row, needColsNo);
				returnList.add(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return returnList;
	}

	private <T> T fillDTO(T t, XSSFRow row, Short[] needColsNo) {
		try {
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < needColsNo.length; i++) {
				fields[i].setAccessible(true);
				fields[i].set(t, getStrinCellValue(row.getCell(needColsNo[i])));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	private String getStrinCellValue(XSSFCell xssfCell) {

		if (xssfCell == null) {
			return "";
		}
		String strCell = "";
		switch (xssfCell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			strCell = xssfCell.toString();
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			strCell = String.valueOf((int)xssfCell.getNumericCellValue());
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(xssfCell.getStringCellValue());
		case XSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		return strCell;
	}
}

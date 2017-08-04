package com.shangpin.asynchronous.task.consumer.util.excel;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;

import com.shangpin.ephub.client.data.mysql.enumeration.ErrorReason;

public class ExcelDropdown {
	private static String EXCEL_HIDE_SHEET_NAME = "poihide";
	private static String HIDE_SHEET_NAME_YN = "errorList";
	// 设置下拉列表的内容
	private static String[] errorList = {ErrorReason.UNPROFITABLE_BRAND.getDesCh(),ErrorReason.NO_MATERIAL_INFO.getDesCh(),
			ErrorReason.NO_ORIGIN_INFO.getDesCh(),ErrorReason.WRONG_MATERIAL_COMPOSITION.getDesCh(),
			ErrorReason.WRONG_MATERIAL_PERCENTAGE.getDesCh(),ErrorReason.NO_COLOR_CODE.getDesCh(),
			ErrorReason.NO_MATERIAL_CODE.getDesCh(),ErrorReason.WRONG_CODE_RULE.getDesCh(),
			ErrorReason.CHILD_ADULT_INVERSION.getDesCh(),ErrorReason.MAN_WOMAN_INVERSION.getDesCh(),ErrorReason.WRONG_MAPPING_OF_CODE.getDesCh(),
			ErrorReason.DIFFERENT_SPU_TO_SAME_ITEM_CODE.getDesCh(),ErrorReason.WRONG_SIZE.getDesCh()};

//	public static void main(String[] args) {
//		Workbook wb = new HSSFWorkbook();
//		Sheet sheet = wb.createSheet("测试");
//		sheet.setColumnWidth(0, 20*300);
//		Row row = sheet.createRow(0);
//		Cell cell = row.createCell(0);
//		cell.setCellValue("无法处理原因");
//		creatExcelHidePage(wb);
//		
//		setDataValidation(sheet.createRow(1),0);
//		FileOutputStream fileOut;
//		try {
//			fileOut = new FileOutputStream("d://excel_danborusu1_1.xls");
//			wb.write(fileOut);
//			fileOut.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void creatExcelHidePage(Workbook workbook) {
		Sheet hideInfoSheet = workbook.createSheet(EXCEL_HIDE_SHEET_NAME);// 隐藏一些信息
		// 在隐藏页设置选择信息
		Row errorReasonRow = hideInfoSheet.createRow(0);
		creatRow(errorReasonRow, errorList);
		creatExcelNameList(workbook, HIDE_SHEET_NAME_YN, 1, errorList.length, false);
		// 设置隐藏页标志
		workbook.setSheetHidden(workbook.getSheetIndex(EXCEL_HIDE_SHEET_NAME), true);
	}
	
	public static void setDataValidation(Row row, int columnIndex) {
		Sheet sheet = row.getSheet();
		DataValidation data_validation_list = getDataValidationByFormula(HIDE_SHEET_NAME_YN, row.getRowNum(),
				columnIndex);
		sheet.addValidationData(data_validation_list);
	}

	private static void creatExcelNameList(Workbook workbook, String nameCode, int order, int size,
			boolean cascadeFlag) {
		Name name = workbook.createName();
		name.setNameName(nameCode);
		String formula = EXCEL_HIDE_SHEET_NAME + "!" + creatExcelNameList(order, size, cascadeFlag);
		name.setRefersToFormula(formula);
	}

	private static String creatExcelNameList(int order, int size, boolean cascadeFlag) {
		char start = 'A';
		if (cascadeFlag) {
			if (size <= 25) {
				char end = (char) (start + size - 1);
				return "$" + start + "$" + order + ":$" + end + "$" + order;
			} else {
				char endPrefix = 'A';
				char endSuffix = 'A';
				if ((size - 25) / 26 == 0 || size == 51) {// 26-51之间，包括边界（仅两次字母表计算）
					if ((size - 25) % 26 == 0) {// 边界值
						endSuffix = (char) ('A' + 25);
					} else {
						endSuffix = (char) ('A' + (size - 25) % 26 - 1);
					}
				} else {// 51以上
					if ((size - 25) % 26 == 0) {
						endSuffix = (char) ('A' + 25);
						endPrefix = (char) (endPrefix + (size - 25) / 26 - 1);
					} else {
						endSuffix = (char) ('A' + (size - 25) % 26 - 1);
						endPrefix = (char) (endPrefix + (size - 25) / 26);
					}
				}
				return "$" + start + "$" + order + ":$" + endPrefix + endSuffix + "$" + order;
			}
		} else {
			if (size <= 26) {
				char end = (char) (start + size - 1);
				return "$" + start + "$" + order + ":$" + end + "$" + order;
			} else {
				char endPrefix = 'A';
				char endSuffix = 'A';
				if (size % 26 == 0) {
					endSuffix = (char) ('A' + 25);
					if (size > 52 && size / 26 > 0) {
						endPrefix = (char) (endPrefix + size / 26 - 2);
					}
				} else {
					endSuffix = (char) ('A' + size % 26 - 1);
					if (size > 52 && size / 26 > 0) {
						endPrefix = (char) (endPrefix + size / 26 - 1);
					}
				}
				return "$" + start + "$" + order + ":$" + endPrefix + endSuffix + "$" + order;
			}
		}
	}

	private static void creatRow(Row currentRow, String[] textList) {
		if (textList != null && textList.length > 0) {
			int i = 0;
			for (String cellValue : textList) {
				Cell userNameLableCell = currentRow.createCell(i++);
				userNameLableCell.setCellValue(cellValue);
			}
		}
	}

	private static DataValidation getDataValidationByFormula(String formulaString, int rowIndex,
			int columnIndex) {
		System.out.println("formulaString  " + formulaString);
		// 加载下拉列表内容
		DVConstraint constraint = DVConstraint.createFormulaListConstraint(formulaString);
		CellRangeAddressList regions = new CellRangeAddressList(rowIndex, rowIndex, columnIndex, columnIndex);
		// 数据有效性对象
		DataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
		data_validation_list.setEmptyCellAllowed(false);
		if (data_validation_list instanceof XSSFDataValidation) {
			data_validation_list.setSuppressDropDownArrow(true);
			data_validation_list.setShowErrorBox(true);
		} else {
			data_validation_list.setSuppressDropDownArrow(false);
		}
		// 设置输入信息提示信息
		//data_validation_list.createPromptBox("下拉选择提示", "请使用下拉方式选择合适的值！");
		// 设置输入错误提示信息
		data_validation_list.createErrorBox("选择错误提示", "你输入的值未在备选列表中，请下拉选择合适的值！");
		return data_validation_list;
	}

}
package com.shangpin.iog.biondini.util.excel;

/**
 * <p>Title: Common</p>
 * <p>Description: 解析Excel时用到的一些常量 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月29日 下午2:08:24
 *
 */
public class Common {

	public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
	public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

	public static final String EMPTY = "";
	public static final String POINT = ".";
	public static final String LIB_PATH = "lib";
	public static final String STUDENT_INFO_XLS_PATH = LIB_PATH
			+ "/student_info" + POINT + OFFICE_EXCEL_2003_POSTFIX;
	public static final String STUDENT_INFO_XLSX_PATH = LIB_PATH
			+ "/student_info" + POINT + OFFICE_EXCEL_2010_POSTFIX;
	public static final String NOT_EXCEL_FILE = " : Not the Excel file!";
	public static final String PROCESSING = "Processing...";
}
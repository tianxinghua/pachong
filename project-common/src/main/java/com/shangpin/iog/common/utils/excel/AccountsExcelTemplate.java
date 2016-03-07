package com.shangpin.iog.common.utils.excel;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 功能描述: Excel操纵类
 * @author: andy.li
 * @date: 2011-4-22
 */
public class AccountsExcelTemplate {
	private static Log logger = LogFactory.getLog(AccountsExcelTemplate.class);
	private static final String DATAS = "datas";
	
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	private HSSFRow currentRow;
	private Map styles = new HashMap(); //数据行的默认样式配置
	private Map confStyles = new HashMap(); //通过设置"#STYLE_XXX"来标识的样式配置
	private int initrow; //数据输出起始行
	private int initcol; //数据输出起始列
	private int num; //index number
	private int currentcol; //当前列
	private int currentRowIndex; //当前行index
	private int rowheight = 22; //行高
	private int lastLowNum = 0;
	private String cellStyle = null;
	
	private AccountsExcelTemplate() {
	}
	
	/**
	 * 使用默认模板创建ExcelTemplate对象
	 * @return 根据模板已初始化完成的ExcelTemplate对象
	 */
	public  AccountsExcelTemplate newInstance(){
		return newInstance("templates/default.xls");
	}

	/**
	 * 指定模板创建ExcelTemplate对象
	 * @param templates 模板名称
	 * @return 根据模板已初始化完成的ExcelTemplate对象
	 */
	public static  AccountsExcelTemplate newInstance(String templates){
		try {
			AccountsExcelTemplate excel = new AccountsExcelTemplate();
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(new File(templates)));
			excel.workbook = new HSSFWorkbook(fs);
			excel.sheet = excel.workbook.getSheetAt(0);
			//查找配置
			excel.initConfig();
			//查找其它样式配置
			excel.readCellStyles();
			//删除配置行
			excel.sheet.removeRow( excel.sheet.getRow(excel.initrow) );
			return excel;
		} catch (Exception e) {
			e.printStackTrace();
			logger.trace("创建Excel对象出现异常",e);
			throw new RuntimeException("创建Excel对象出现异常");
		}
	}
	
	
	/**
	 * 设置特定的单元格样式，此样式可以通过在模板文件中定义"#STYLE_XX"来得到，如：
	 * #STYLE_1，传入的参数就是"STYLE_1"
	 * @param style 
	 */
	public void setCellStyle(String style){
		cellStyle = style;
	}
	
	/**
	 * 取消特定的单元格格式，恢复默认的配置值，即DATAS所在行的值
	 */
	public void setCellDefaultStyle(){
		cellStyle = null;
	}
	
	/**
	 * 创建新行
	 * @param index 从0开始计数
	 */
	public void createRow(int index){
		//如果在当前插入数据的区域有后续行，则将其后面的行往后移动
		if(lastLowNum > initrow && index > 0){
			sheet.shiftRows(index + initrow ,lastLowNum + index,1,true,true);
		}
		currentRow = sheet.createRow(index + initrow);
		currentRow.setHeight((short)rowheight);
		currentRowIndex = index;
		currentcol = initcol;
	}
	
	/**
	 * 根据传入的字符串值，在当前行上创建新列
	 * @param value 列的值（字符串）
	 */
	public void createCell(String value){
		HSSFCell cell = createCell();
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(value);
	}
	public void createCell(float value){
		HSSFCell cell = createCell();
		HSSFCellStyle cstyle = (HSSFCellStyle)confStyles.get(cellStyle);
		if(null==cstyle){
			cstyle=workbook.createCellStyle();
		}
		cstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
		cell.setCellStyle(cstyle);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        DecimalFormat df=new DecimalFormat("#0.00");
		cell.setCellValue(Double.parseDouble(df.format(value)));
	}
	public void createCell(double value){
		HSSFCell cell = createCell();
		HSSFCellStyle cstyle = (HSSFCellStyle)confStyles.get(cellStyle);
		if(null==cstyle){
			cstyle=workbook.createCellStyle();
		}
		cstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
		cell.setCellStyle(cstyle);
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        DecimalFormat df=new DecimalFormat("#0.00");
		cell.setCellValue(Double.parseDouble(df.format(value)));
	}
	public void createNumberCell(Integer value){
		HSSFCell cell = createCell();
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
	}
	/**
	 * 根据传入的日期值，在当前行上创建新列
	 * 在这种情况下（传入日期），你可以在模板中定义对应列
	 * 的日期格式，这样可以灵活通过模板来控制输出的日期格式
	 * @param value 日期
	 */
	public void createCell(Date value){
		HSSFCell cell = createCell();
		cell.setCellValue(value);
	}
	
	/**
	 * 创建当前行的序列号列，通常在一行的开头便会创建
	 * 注意要使用这个方法，你必需在创建行之前调用initPageNumber方法
	 */
	public void createSerialNumCell(){
		HSSFCell cell = createCell();
		cell.setCellValue(currentRowIndex + num);
	}
	
	@SuppressWarnings("deprecation")
	private HSSFCell createCell(){
		HSSFCell cell = currentRow.createCell((short)currentcol++);
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		HSSFCellStyle style = (HSSFCellStyle)styles.get(new Integer(cell.getCellNum()));
		if(style != null){
			cell.setCellStyle(style);
		}
		
		//设置了特定格式
		if(cellStyle != null){
			HSSFCellStyle ts = (HSSFCellStyle)confStyles.get(cellStyle);
			if(ts != null){
				cell.setCellStyle(ts);
			}
		}
		return cell;
	}
	
	/**
	 * 获取当前HSSFWorkbook的实例
	 * @return
	 */
	public HSSFWorkbook getWorkbook(){
		return workbook;
	}
	
	/**
	 * 获取模板中定义的单元格样式，如果没有定义，则返回空
	 * @param style 模板定义的样式名称
	 * @return 模板定义的单元格的样式，如果没有定义则返回空
	 */
	public HSSFCellStyle getTemplateStyle(String style){
		return (HSSFCellStyle)confStyles.get(style);
	}
	
	/**
	 * 替换模板中的文本参数
	 * 参数以“#”开始
	 * @param props
	 */
	public void replaceParameters(Properties props){
		if(props == null || props.size() == 0){
			return;
		}
		Set propsets = props.entrySet();
		Iterator rowit = sheet.rowIterator();
		while(rowit.hasNext()){
			HSSFRow row = (HSSFRow)rowit.next();
			if(row == null)	continue;
			int cellLength = row.getLastCellNum();
			for(int i=0; i<cellLength; i++){
				HSSFCell cell = (HSSFCell)row.getCell((short)i);
				if(cell == null) continue;
				String value = cell.getStringCellValue();
				if(value != null && value.indexOf("#") != -1){
					for (Iterator iter = propsets.iterator(); iter.hasNext();) {
						Map.Entry entry = (Map.Entry) iter.next();
						value = value.replaceAll("#"+entry.getKey(),(String)entry.getValue());
					}
				}
				cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell.setCellValue(value);
			}
		}
	}
	
	/**
	 * 初始化Excel配置
	 */
	private void initConfig(){
		lastLowNum = sheet.getLastRowNum();
		Iterator rowit = sheet.rowIterator();
		boolean configFinish = false;
		while(rowit.hasNext()){
			if(configFinish){
				break;
			}
			HSSFRow row = (HSSFRow)rowit.next();
			if(row == null)	continue;
			int cellLength = row.getLastCellNum();
			int rownum = row.getRowNum();
			for(int i=0; i<cellLength; i++){
				HSSFCell cell = (HSSFCell)row.getCell((short)i);
				if(cell == null) continue;
				String config = cell.getStringCellValue();
				if(DATAS.equalsIgnoreCase(config)){
					//本行是数据开始行和样式配置行，需要读取相应的配置信息
					initrow = row.getRowNum();
					rowheight = row.getHeight();
					initcol = cell.getCellNum();
					configFinish = true;
				}
				if(configFinish){
					readCellStyle(cell);
				}				
			}
		}
	}
	
	/**
	 * 读取cell的样式
	 * @param cell
	 */
	@SuppressWarnings("unchecked")
	private void readCellStyle(HSSFCell cell){
		HSSFCellStyle style = cell.getCellStyle();
		if(style == null) return;
		styles.put(new Integer(cell.getCellNum()),style);
	}
	
	/**
	 * 读取模板中其它单元格的样式配置
	 */
	private void readCellStyles(){
		Iterator rowit = sheet.rowIterator();
		while(rowit.hasNext()){
			HSSFRow row = (HSSFRow)rowit.next();
			if(row == null)	continue;
			int cellLength = row.getLastCellNum();
			for(int i=0; i<cellLength; i++){
				HSSFCell cell = (HSSFCell)row.getCell((short)i);
				if(cell == null) continue;
				String value = cell.getStringCellValue();
				if(value != null && value.indexOf("#STYLE_") != -1){
					HSSFCellStyle style = cell.getCellStyle();
					if(style == null) continue;
					confStyles.put(value.substring(1),style);
					//remove it
					row.removeCell(cell);
				}
			}
		}
	}
	public void setValue(int rowNum,int colNum,String value){
		  HSSFRow row=this.sheet.getRow(rowNum);
		  HSSFCell cell=row.getCell((short)colNum);
		  if (cell==null){
		    cell = row.createCell((short) colNum);
		  }
		  cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		  cell.setCellValue(value);
	}
	public void mergedRegion(int rowNum,int colNum,int cols,int rows){
		sheet.addMergedRegion(new Region(rowNum,(short)colNum,cols,(short)rows));
	}
}

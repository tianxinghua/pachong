package com.shangpin.ephub.product.business.common.util;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
//import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;  
/** 

 * 导出EXCEL工具类，适用于单行表头的表格 

 * @author zhaogenchun

 * @since 2016-12-22

 */  
@Slf4j
public class ExportExcelUtils { 
	
	/**
	 * 插入图片
	 * @param url 图片链接
	 * @param row Excel的一行
	 * @param startColumn 图片插入开始列
	 */
	public static void insertImageToExcel(String url,HSSFRow row,short startColumn){
		BufferedImage bufferImg = null;
		try {
			if(!StringUtils.isEmpty(url)){
				bufferImg = ImageIO.read(new URL(url));
				ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();							
				ImageIO.write(bufferImg, "jpg", byteArrayOut);
				HSSFPatriarch patriarch = row.getSheet().createDrawingPatriarch();
				HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 500, 255,startColumn, row.getRowNum(), startColumn, row.getRowNum());
//				anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);
				patriarch.createPicture(anchor, row.getSheet().getWorkbook().addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
			}else{
				row.createCell(startColumn).setCellValue("无图片");
			}
		} catch (Exception e) {
			log.error("插入图片异常："+e.getMessage()); 
			bufferImg = null;
			row.createCell(startColumn).setCellValue("图片错误");
		}
	}

	  public static void exportExcelWithStyle(String title, String[] headers, String[] columns, List<Map<String, String>> result, OutputStream out) throws Exception{  

	        // 声明一个工作薄  
	        HSSFWorkbook workbook = new HSSFWorkbook();  
	        // 生成一个表格  
	        HSSFSheet sheet = workbook.createSheet(title);  
	        // 设置表格默认列宽度为20个字节  
	        sheet.setDefaultColumnWidth(20);  
	        // 生成一个样式  
	        HSSFCellStyle style = workbook.createCellStyle();  
	        // 设置这些样式  
	        style.setFillForegroundColor(HSSFColor.GOLD.index);  
	        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
	        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
	        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
	        style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
	        style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
//	        style.setAlignment(HorizontalAlignment.CENTER);  
	        // 生成一个字体  
	        HSSFFont font = workbook.createFont();  
	        font.setColor(HSSFColor.VIOLET.index);  
	        font.setFontHeightInPoints((short) 12);  
//	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
	        // 把字体应用到当前的样式  
	        style.setFont(font);  
	        // 指定当单元格内容显示不下时自动换行  
	        style.setWrapText(true);  
	        // 产生表格标题行  
	        HSSFRow row = sheet.createRow(0);  

	        for (int i = 0; i < headers.length; i++) {  

	            HSSFCell cell = row.createCell(i);  

	            cell.setCellStyle(style);  

	            HSSFRichTextString text = new HSSFRichTextString(headers[i]);  

	            cell.setCellValue(text);  

	        }  
	        // 遍历集合数据，产生数据行  
	        if(result != null){  
	            int index = 1;  
	            for(Map<String, String> m:result){  
	                row = sheet.createRow(index);  
	                int cellIndex = 0;  
	                for(String s:columns){  
	                    HSSFCell cell = row.createCell(cellIndex);  
	                    HSSFRichTextString richString = new HSSFRichTextString(m.get(s) == null ? "" : m.get(s).toString());  
	                    cell.setCellValue(richString);  
	                    cellIndex++;  
	                }  
	                index++;  
	            }     
	        }  
	        workbook.write(out);  
	    }  

    /** 
     * 导出Excel的方法 
     * @param title excel中的sheet名称 
     * @param headers 表头 
     * @param columns 表头对应的数据库中的列名 
     * @param result 结果集 
     * @param out 输出流 
     * @param pattern 时间格式 
     * @throws Exception 
     */  
    public static void exportExcel(String title, String[] headers, String[] columns, List<Map<String, String>> result, OutputStream out) throws Exception{  
        // 声明一个工作薄  
        HSSFWorkbook workbook = new HSSFWorkbook();  
        // 生成一个表格  
        HSSFSheet sheet = workbook.createSheet(title);  
        
        // 设置表格默认列宽度为20个字节  
        sheet.setDefaultColumnWidth(20);  
        // 生成一个样式  
        HSSFCellStyle style = workbook.createCellStyle();  
//        HSSFFont font = workbook.createFont();  
//        style.setFont(font);  
//        style.setWrapText(true);  
        // 产生表格标题行  
        HSSFRow row = sheet.createRow(0);  
        for (int i = 0; i < headers.length; i++) {  
            HSSFCell cell = row.createCell(i);  
            cell.setCellStyle(style);  
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);  
            cell.setCellValue(text);  
        }  
        // 遍历集合数据，产生数据行  
        if(result != null){  
            int index = 1;  
            for(Map<String, String> m:result){  
                row = sheet.createRow(index);  
                int cellIndex = 0;  
                for(String s:columns){  
                    HSSFCell cell = row.createCell(cellIndex);  
                    HSSFRichTextString richString = new HSSFRichTextString(m.get(s) == null ? "" : m.get(s).toString());  
                    cell.setCellValue(richString);  
                    cellIndex++;  
                }  
                index++;  
            }     
        }  
        workbook.write(out);  
    }  
    
}  


package com.shangpin.iog.catalogue.excel;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {
    
    /**
     * read the Excel file
     * @param path the path of the Excel file
     * @return
     * @throws IOException
     */
    public List<Item> readExcel(String path) throws IOException {
        if (path == null || Common.EMPTY.equals(path)) {
            return null;
        } else {
            String postfix = Util.getPostfix(path);
            if (!Common.EMPTY.equals(postfix)) {
                if (Common.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
                    return readXls(path);
                } else if (Common.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
                    return readXlsx(path);
                }
            } else {
                System.out.println(path + Common.NOT_EXCEL_FILE);
            }
        }
        return null;
    }

    /**
     * Read the Excel 2010
     * @param path the path of the excel file
     * @return
     * @throws IOException
     */
    public List<Item> readXlsx(String path) throws IOException {
        System.out.println(Common.PROCESSING + path);
        InputStream is = new FileInputStream(path);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        List<Item> list = new ArrayList<Item>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    Item item = new Item();
                    item.setId(getValue(xssfRow.getCell(0)));
                    item.setDesigner(getValue(xssfRow.getCell(1)));
                    item.setDescription2(getValue(xssfRow.getCell(2)));
                    item.setSaison(getValue(xssfRow.getCell(3)));
                    item.setCategory(getValue(xssfRow.getCell(4)));
                    item.setImageURL(getValue(xssfRow.getCell(5)));
                    item.setImage_link1(getValue(xssfRow.getCell(6)));
                    item.setLink_to_product(getValue(xssfRow.getCell(7)));
                    item.setTitre(getValue(xssfRow.getCell(8)));
                    item.setSize(getValue(xssfRow.getCell(9)));
                    item.setQuatity(getValue(xssfRow.getCell(10)));
                    item.setPrix_HT(getValue(xssfRow.getCell(11)));
                    item.setPrix_TTC(getValue(xssfRow.getCell(12)));                    
                    list.add(item);
                }
            }
        }
        return list;
    }

    /**
     * Read the Excel 2003-2007
     * @param path the path of the Excel
     * @return
     * @throws IOException
     */
    public List<Item> readXls(String path) throws IOException {
        System.out.println(Common.PROCESSING + path);
        InputStream is = new FileInputStream(path);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        List<Item> list = new ArrayList<Item>();
        // Read the Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // Read the Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    Item item = new Item();
                    item.setId(getValue(hssfRow.getCell(0)));
                    item.setDesigner(getValue(hssfRow.getCell(1)));
                    item.setDescription2(getValue(hssfRow.getCell(2)));
                    item.setSaison(getValue(hssfRow.getCell(3)));
                    item.setCategory(getValue(hssfRow.getCell(4)));
                    item.setImageURL(getValue(hssfRow.getCell(5)));
                    item.setImage_link1(getValue(hssfRow.getCell(6)));
                    item.setLink_to_product(getValue(hssfRow.getCell(7)));
                    item.setTitre(getValue(hssfRow.getCell(8)));
                    item.setSize(getValue(hssfRow.getCell(9)));
                    item.setQuatity(getValue(hssfRow.getCell(10)));
                    item.setPrix_HT(getValue(hssfRow.getCell(11)));
                    item.setPrix_TTC(getValue(hssfRow.getCell(12)));                    
                    list.add(item);
                }
            }
        }
        return list;
    }

    @SuppressWarnings("static-access")
    private String getValue(XSSFCell xssfRow) {
    	if(null != xssfRow){
    		if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
                return String.valueOf(xssfRow.getBooleanCellValue());
            } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
                return String.valueOf(xssfRow.getNumericCellValue());
            } else {
                return String.valueOf(xssfRow.getStringCellValue());
            }
    	}else{
    		return "";
    	}
        
    }

    @SuppressWarnings("static-access")
    private String getValue(HSSFCell hssfCell) {
    	if(null != hssfCell){
    		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
                return String.valueOf(hssfCell.getBooleanCellValue());
            } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
                return String.valueOf(hssfCell.getNumericCellValue());
            } else {
                return String.valueOf(hssfCell.getStringCellValue());
            }
    	}else{
    		return "";
    	}
        
    }
}
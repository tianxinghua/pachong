package com.shangpin.iog.notenom.excel;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.notenom.dto.Item;

public class ReadExcel {
    
	/**
	 * 
	 * @param uri 文件网址
	 * @param path 本地路径
	 * @throws Exception
	 */
    public static void downLoadFile(String uri,String path) throws Exception{
    	
//    	HttpClient httpClient = new HttpClient();
//		PostMethod postMethod = new PostMethod(uri);
//		postMethod.setRequestHeader("Content-Type", "application/msexcel;charset=UTF-8");
//		int returnCode = httpClient.executeMethod(postMethod);
//		BufferedInputStream bufferedInputStream = 
//        		new BufferedInputStream(postMethod.getResponseBodyAsStream());
		
		
		URL url = new URL(uri);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为5分钟
		conn.setConnectTimeout(5 * 60 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		// 得到输入流
		InputStream bufferedInputStream = conn.getInputStream();	
		
		
		
		byte[] ims=new byte[1024*1024*100];
		File file = new File(path);
		if(!file.exists()){
        	file.createNewFile();
        }
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file) );
        int i = -1;
        while((i=bufferedInputStream.read(ims)) != -1){        	
        	bufferedOutputStream.write(ims,0,i);
        }
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        bufferedInputStream.close();
        System.out.println("***************下载完成********************");
        
    }
	
    /**
     * read the Excel file
     * @param path the path of the Excel file
     * @return
     * @throws IOException
     */
    public static <T> List<T> readExcel(Class<T> clazz,String path) throws Exception {
        if (path == null || Common.EMPTY.equals(path)) {
            return null;
        } else {
            String postfix = Util.getPostfix(path);
            if (!Common.EMPTY.equals(postfix)) {
                if (Common.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
                    return readXls(clazz,path);
                } else if (Common.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
                    return readXlsx(clazz,path);
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
    public static <T> List<T> readXlsx(Class<T> clazz,String path) throws Exception {
        System.out.println(Common.PROCESSING + path);
        InputStream is = new FileInputStream(path);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        List<T> list = new ArrayList<T>();
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
	                List<String> colValueList = new ArrayList<String>();
	                for(int cellNo=0;cellNo<xssfRow.getLastCellNum();cellNo++){
	                	colValueList.add(getValue(xssfRow.getCell(cellNo)));
	                }
	                T t = clazz.newInstance();
	                Field[] fields = t.getClass().getDeclaredFields();
	                for(int i=0;i<fields.length;i++){
	                	fields[i].setAccessible(true);
	                	fields[i].set(t, colValueList.get(i));
	                }
	                list.add(t);
                }
//                if (xssfRow != null) {
//                    Item item = new Item();
//                    item.setCategories(getValue(xssfRow.getCell(0)));
//                    item.setBrandName(getValue(xssfRow.getCell(1)));
//                    item.setProductName(getValue(xssfRow.getCell(2)));
//                    item.setProductNo(getValue(xssfRow.getCell(3)));
//                    item.setBlank(getValue(xssfRow.getCell(4)));
//                    item.setColor(getValue(xssfRow.getCell(5)));
//                    item.setSize(getValue(xssfRow.getCell(6)));
//                    item.setDanwei(getValue(xssfRow.getCell(7)));
//                    item.setSaleprice(getValue(xssfRow.getCell(8)));
//                    item.setSkuNo(getValue(xssfRow.getCell(9)));
//                    item.setBarcode(getValue(xssfRow.getCell(10)));
//                    item.setStock(getValue(xssfRow.getCell(11)));
//                    item.setMaterial(getValue(xssfRow.getCell(12)));   
//                    item.setSeason(getValue(xssfRow.getCell(13)));
//                    item.setMadein(getValue(xssfRow.getCell(14)));
//                    item.setGender(getValue(xssfRow.getCell(15)));
//                    item.setLength(getValue(xssfRow.getCell(16)));
//                    item.setWidth(getValue(xssfRow.getCell(17)));
//                    item.setHeight(getValue(xssfRow.getCell(18)));
//                    list.add(item);
//                }
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
    public static <T> List<T> readXls(Class<T> clazz,String path) throws Exception {
        System.out.println(Common.PROCESSING + path);
        InputStream is = new FileInputStream(path);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        List<T> list = new ArrayList<T>();
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
                	List<String> colValueList = new ArrayList<String>();
                	for(int cellNo=0;cellNo<hssfRow.getLastCellNum();cellNo++){
                		colValueList.add(getValue(hssfRow.getCell(cellNo)));
                	}
                	T t = clazz.newInstance();
	                Field[] fields = t.getClass().getDeclaredFields();
	                for(int i=0;i<fields.length;i++){
	                	fields[i].setAccessible(true);
	                	fields[i].set(t, colValueList.get(i));
	                }
	                list.add(t);
//                    Item item = new Item();
//                    item.setCategories(getValue(hssfRow.getCell(0)));
//                    item.setBrandName(getValue(hssfRow.getCell(1)));
//                    item.setProductName(getValue(hssfRow.getCell(2)));
//                    item.setProductNo(getValue(hssfRow.getCell(3)));
//                    item.setBlank(getValue(hssfRow.getCell(4)));
//                    item.setColor(getValue(hssfRow.getCell(5)));
//                    item.setSize(getValue(hssfRow.getCell(6)));
//                    item.setDanwei(getValue(hssfRow.getCell(7)));
//                    item.setSaleprice(getValue(hssfRow.getCell(8)));
//                    item.setSkuNo(getValue(hssfRow.getCell(9)));
//                    item.setBarcode(getValue(hssfRow.getCell(10)));
//                    item.setStock(getValue(hssfRow.getCell(11)));
//                    item.setMaterial(getValue(hssfRow.getCell(12)));   
//                    item.setSeason(getValue(hssfRow.getCell(13)));
//                    item.setMadein(getValue(hssfRow.getCell(14)));
//                    item.setGender(getValue(hssfRow.getCell(15)));
//                    item.setLength(getValue(hssfRow.getCell(16)));
//                    item.setWidth(getValue(hssfRow.getCell(17)));
//                    item.setHeight(getValue(hssfRow.getCell(18)));
//                    list.add(item);
                }
            }
        }
        return list;
    }

    @SuppressWarnings("static-access")
    private static String getValue(XSSFCell xssfRow) {
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
    private static String getValue(HSSFCell hssfCell) {
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
package com.shangpin.iog.biondini.util.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: ReadExcel</p>
 * <p>Description: 解析Excel工具类 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月29日 下午1:59:44
 *
 */
@Slf4j
public class ReadExcel {
    
	/**
	 * 
	 * @param uri 文件网址
	 * @param path 本地路径
	 * @throws Exception
	 */
    public static void downLoadFile(String uri, String path) throws Exception {
    	InputStream bufferedInputStream = null;
    	BufferedOutputStream bufferedOutputStream = null;
    	try {
    		URL url = new URL(uri);
    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    		// 设置超时间为5分钟
    		conn.setConnectTimeout(5 * 60 * 1000);
    		// 防止屏蔽程序抓取而返回403错误
    		conn.setRequestProperty("User-Agent",
    				"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
    		// 得到输入流
    		bufferedInputStream = conn.getInputStream();	

    		byte[] ims=new byte[1024*1024*100];
    		File file = new File(path);
    		if(!file.exists()){
            	file.createNewFile();
            }
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file) );
            int i = -1;
            while((i=bufferedInputStream.read(ims)) != -1){        	
            	bufferedOutputStream.write(ims,0,i);
            }
		} catch (Exception e) {
			throw e;
		}finally {
			try {
				if(null != bufferedOutputStream){
					bufferedOutputStream.flush();
		            bufferedOutputStream.close();
				}
				if(null != bufferedInputStream){
					bufferedInputStream.close();
				}
			} catch (Exception e2) {
				log.error("关闭流时出错："+e2.getMessage(),e2); 
			}
		}
		
    }
    
    /**
     * read the Excel file
     * @param clazz 要转换成的实体类
     * @param input 输入流
     * @param fileName 文件名称，主要用于判断后缀
     * @return
     * @throws Exception
     */
    public static <T> List<T> readExcel(Class<T> clazz, InputStream input, String fileName) throws Exception {
    	if (StringUtils.isEmpty(fileName)) {
            return null;
        } else {
            String postfix = getPostfix(fileName);
            if (!Common.EMPTY.equals(postfix)) {
                if (Common.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
                    return readXls(clazz,input);
                } else if (Common.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
                    return readXlsx(clazz,input);
                }
            } else {
                log.error(fileName + Common.NOT_EXCEL_FILE);
            }
        }
        return null;
    }
	
    /**
     * read the Excel file
     * @param clazz 
     * @param path the path of the Excel file
     * @return
     * @throws IOException
     */
    public static <T> List<T> readExcel(Class<T> clazz, String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return null;
        } else {
            String postfix = getPostfix(path);
            if (!Common.EMPTY.equals(postfix)) {
                if (Common.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
                    return readXls(clazz,path);
                } else if (Common.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
                    return readXlsx(clazz,path);
                }
            } else {
            	log.error(path + Common.NOT_EXCEL_FILE);
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
    public static <T> List<T> readXlsx(Class<T> clazz, String path) throws Exception {
        System.out.println(Common.PROCESSING + path);
        InputStream is = new FileInputStream(path);
        return readXlsx(clazz, is);
    }

    /**
     * Read the Excel 2010 of stream
     * @param clazz
     * @param is
     * @return
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
	public static <T> List<T> readXlsx(Class<T> clazz, InputStream is)
			throws IOException, InstantiationException, IllegalAccessException {
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        List<T> list = new ArrayList<T>();
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
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
    public static <T> List<T> readXls(Class<T> clazz, String path) throws Exception {
        System.out.println(Common.PROCESSING + path);
        InputStream is = new FileInputStream(path);
        return readXls(clazz, is);
    }

    /**
     * Read the Excel 2003-2007 of stream
     * @param clazz
     * @param is
     * @return
     * @throws IOException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
	public static <T> List<T> readXls(Class<T> clazz, InputStream is)
			throws IOException, InstantiationException, IllegalAccessException {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        List<T> list = new ArrayList<T>();
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
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
    /**
     * 获取文件的后缀
     * @param path
     * @return
     */
    private static String getPostfix(String path) {
        if (path == null || Common.EMPTY.equals(path.trim())) {
            return Common.EMPTY;
        }
        if (path.contains(Common.POINT)) {
            return path.substring(path.lastIndexOf(Common.POINT) + 1, path.length());
        }
        return Common.EMPTY;
    }
}
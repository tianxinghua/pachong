package com.shangpin.iog.bagheera.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.shangpin.iog.bagheera.dto.BagheeraDTO;

/**
 * Created by sunny on 2015/9/8.
 */
public class DownloadAndReadExcel {
    public static final String PROPERTIES_FILE_NAME = "conf";
    static ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_FILE_NAME) ;
    private static String path = bundle.getString("path");
    private static String httpurl = bundle.getString("url");
    private static Logger logger = Logger.getLogger("info");

    /**
     * http下载excel文件到本地路径
     * @throws MalformedURLException
     */
    public static String downloadNet() throws MalformedURLException {
        int bytesum = 0;
        int byteread = 0;

        FileOutputStream fs = null;
        String realPath="";
        try {
        	URL url = new URL(httpurl);
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            realPath = getPath(path);
            fs = new FileOutputStream(realPath);

            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
        	logger.info("下载失败");
        	throw new RuntimeException(e);
        }finally {
			if(null != fs){
				try {
					fs.close();
				} catch (Exception e2) {
					e2.printStackTrace(); 
				}
			}
		}
        return  realPath;
    }
    public static List<BagheeraDTO> readLocalExcel() throws IOException {
        String realPath=downloadNet();
        HSSFWorkbook wb=null;
        HSSFSheet sheet=null;
        FileInputStream fw=null;
        /*要想把excel中的每一行数据转换成javabean对象则用反射技术
        * javabean中的属性个数要与excel中的列数一样，不然可能报错,属性顺序与列的顺序也要一样
        * */
        try{
            fw=new FileInputStream(realPath);
            POIFSFileSystem fs=new POIFSFileSystem(fw);
            wb=new HSSFWorkbook(fs);
            fw.close();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(fw!=null){
                fw.close();
            }
        }
        HSSFRow row=null;
        List<BagheeraDTO> dataList=new ArrayList<BagheeraDTO>();
        for(int i=0;i<1;i++){
            sheet=wb.getSheetAt(i);
            for(int j=1;j<sheet.getPhysicalNumberOfRows();j++){
                row=sheet.getRow(j);
                BagheeraDTO bagheeraDTO=new BagheeraDTO();
                Field[] beanFiled=bagheeraDTO.getClass().getDeclaredFields();
                for(int z=0;z<24;z++){
                    try {
                        beanFiled[z].setAccessible(true);
                        short a = (short)z;
                        HSSFCell cell = row.getCell(a);
                        if(null != cell){
                        	cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        }
						beanFiled[z].set(bagheeraDTO, getStrinCellValue(cell));
                    } catch (Exception e) {
                    	System.out.println(beanFiled[z]);
                        e.printStackTrace();
                    }
                }
                dataList.add(bagheeraDTO);
            }
        }
        return dataList;
    }
    public static String getStrinCellValue(HSSFCell cell){

        if(cell==null){
            return "";
        }
        String strCell ="";
        switch (cell.getCellType()){
            case HSSFCell.CELL_TYPE_STRING:
                strCell=cell.toString();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell=String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell=String.valueOf(cell.getStringCellValue());
            case HSSFCell.CELL_TYPE_BLANK:
                strCell="";
                break;
            default:
                strCell="";
        }

        if(strCell.equals("") || strCell==null){
            return "";
        }
        return strCell;
    }
    public static String getPath(String realpath){
        //Date dt=new Date();
        //SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-ddHH");
        //String date=matter1.format(dt).replaceAll("-","").trim();
        //realpath = realpath+"_"+date+".xls";
        realpath = realpath+".xls";
        return realpath;
    }
  /*  public static void main(String[] args) {
        try {
            readLocalExcel();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}

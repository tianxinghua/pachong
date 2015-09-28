package com.shangpin.iog.fashionesta.utils;

import com.csvreader.CsvReader;
import com.shangpin.iog.fashionesta.dto.FashionestaDTO;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

/**
 * Created by monkey on 2015/9/28.
 */
public class DownloadAndReadCSV {
    public static final String PROPERTIES_FILE_NAME = "param";
    static ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_FILE_NAME) ;
    private static String path = bundle.getString("path");
    private static String httpurl = bundle.getString("url");

    /**
     * http下载csv文件到本地路径
     * @throws MalformedURLException
     */
    public static String downloadNet() throws MalformedURLException {
        int bytesum = 0;
        int byteread = 0;

        URL url = new URL(httpurl);
        String realPath="";
        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            realPath = getPath(path);
            FileOutputStream fs = new FileOutputStream(realPath);

            byte[] buffer = new byte[1204];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  realPath;
    }
    public static List<FashionestaDTO> readLocalCSV() throws Exception {
        
    	String realPath=downloadNet();
        String rowString = null;
        List<FashionestaDTO> dtoList = new ArrayList<FashionestaDTO>();
    	String[] split = null;
    	List<String> colValueList = null;
    	StringBuffer sb = null;
        //解析csv文件
        CsvReader cr = new CsvReader(new FileReader(realPath));
        System.out.println("创建cr对象成功");
        //得到列名集合
        cr.readRecord();
        rowString = cr.getRawRecord();
        split = rowString.split(";");
		List<String> colNameList = Arrays.asList(split);
		//读取每一行，把对应列名的数据填充到FashionestaDTO对象中，放入list集合返回
		//TODO 读取每一行信息解析，根据type判断是否是product(configable,simple),分别存入product和item,最后返回List<Product>
		//TODO 两次判断
		while(cr.readRecord()){
			rowString = cr.getRawRecord();
			split = rowString.split(";");
			colValueList = Arrays.asList(split);
			if (StringUtils.isNotBlank(colValueList.get(colNameList.indexOf("\"qty\"")))&& StringUtils.isNotBlank(colValueList.get(colNameList.indexOf("\"material1\"")))) {
				FashionestaDTO fashionestaDTO = new FashionestaDTO();
				fashionestaDTO.setBRAND(colValueList.get(colNameList.indexOf("\"brand\"")));
				fashionestaDTO.setCATEGORY(colValueList.get(colNameList.indexOf("\"Im_type\"")));
				fashionestaDTO.setCOLOR(colValueList.get(colNameList.indexOf("\"color\"")));
				fashionestaDTO.setCURRENCY("Euro");
				fashionestaDTO.setDESCRIPTION(colValueList.get(colNameList.indexOf("\"description\"")));
				fashionestaDTO.setGENDER(colValueList.get(colNameList.indexOf("\"gender\"")));
				fashionestaDTO.setIMAGE_URL(colValueList.get(colNameList.indexOf("\"image\"")));
				fashionestaDTO.setSIMAGE_URL(colValueList.get(colNameList.indexOf("\"small_image\"")));
				fashionestaDTO.setMADE(colValueList.get(colNameList.indexOf("\"country_of_origin\"")));
				
				fashionestaDTO.setMATERIAL1(colValueList.get(colNameList.indexOf("\"material1\"")));
				fashionestaDTO.setMATERIAL2(colValueList.get(colNameList.indexOf("\"material2\"")));
				fashionestaDTO.setMATERIAL3(colValueList.get(colNameList.indexOf("\"material3\"")));
				fashionestaDTO.setMATERIAL4(colValueList.get(colNameList.indexOf("\"material4\"")));
				fashionestaDTO.setPRICE(colValueList.get(colNameList.indexOf("\"price\"")));
				fashionestaDTO.setSPECIAL_PRICE(colValueList.get(colNameList.indexOf("\"special_price\"")));
				fashionestaDTO.setPRODUCT_NAME(colValueList.get(colNameList.indexOf("\"name\"")));
				fashionestaDTO.setSIZE(colValueList.get(colNameList.indexOf("\"size\"")));
				fashionestaDTO.setSTOCK(colValueList.get(colNameList.indexOf("\"qty\"")));
				fashionestaDTO.setSUPPLIER_CODE(colValueList.get(colNameList.indexOf("\"sku\"")));
				dtoList.add(fashionestaDTO);
			}
		}
        return dtoList;
    }
  
    public static String getPath(String realpath){
        Date dt=new Date();
        SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-ddHH");
        String date=matter1.format(dt).replaceAll("-","").trim();
        realpath = realpath+"_"+date+".csv";
        return realpath;
    }
   public static void main(String[] args) {
        try {
        	System.out.println("下载中");
            readLocalCSV();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

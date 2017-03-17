package com.shangpin.iog.monnierfreres.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.csvreader.CsvReader;

/**
 * Created by monkey on 2015/9/28.
 */
public class DownloadAndReadCSV {
    public static final String PROPERTIES_FILE_NAME = "sop";
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
        FileOutputStream fs = null;
        try {
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
        	if(null != fs){
        		try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
        return  realPath;
    }
    public static Map<String,String> readLocalCSV() throws Exception {
        
    	String realPath=downloadNet();
        String rowString = null;
    	String[] split = null;
    	List<String> colValueList = null;
        //解析csv文件
        CsvReader cr = new CsvReader(new FileReader(realPath));
        System.out.println("创建cr对象成功");
        //得到列名集合
        cr.readRecord();
        rowString = cr.getRawRecord();
        split = rowString.split("\"\\|\"");
		List<String> colNameList = Arrays.asList(split);
		Map<String,String> map = new HashMap<String,String>();
		while(cr.readRecord()){
			rowString = cr.getRawRecord();
			split = rowString.split("\"\\|\"");
			colValueList = Arrays.asList(split);
			if(colNameList.size()==colValueList.size()){
				String skuId = colValueList.get(colNameList.indexOf("\"sku")).replace("\"", "");
				String qty = colValueList.get(colNameList.indexOf("qty"));
				map.put(skuId,qty);
				

			}
		}
		cr.close();
		File flie = new File(realPath);
		boolean falg = flie.delete();
		if(falg){
			System.out.println("文件删除success");
		}else{
			System.out.println("文件删除fail");
		}
		return map;
    }
    public static String getPath(String realpath){
		   Date dt=new Date();
	        SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-ddHH");
	        String date=matter1.format(dt).replaceAll("-","").trim();
     realpath = realpath+"_"+date+".csv";
     return realpath;
 }
}

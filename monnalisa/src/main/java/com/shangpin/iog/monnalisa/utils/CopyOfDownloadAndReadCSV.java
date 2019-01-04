package com.shangpin.iog.monnalisa.utils;

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
import java.util.List;
import java.util.ResourceBundle;

import com.csvreader.CsvReader;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CopyOfDownloadAndReadCSV {
    public static final String PROPERTIES_FILE_NAME = "conf";
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
            File file = new File(realPath);
            fs = new FileOutputStream(file);
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
        }finally {
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
    public static JSONArray readLocalCSV() throws Exception {
        
    	String realPath=downloadNet();
//    	String realPath= "F://forzieri_2017061315.csv";
        String rowString = null;
    	String[] split = null;
    	List<String> colValueList = null;
        //解析csv文件
        CsvReader cr = new CsvReader(new FileReader(realPath));
        //得到列名集合
        cr.readRecord();
        rowString = cr.getRawRecord();
        split = rowString.split("\t");
		List<String> colNameList = Arrays.asList(split);
		JSONArray arr= new JSONArray();
		while(cr.readRecord()){
			rowString = cr.getRawRecord();
			split = rowString.split("\t");
			colValueList = Arrays.asList(split);
//			if(colValueList.size()==colNameList.size()){
				JSONObject obj = new JSONObject();	
				int i=0;
				for(String header:colNameList){
					String name = header.replace("\"","");
					if(i>=colValueList.size()){
						obj.put(name,null);
					}else{
						String value = colValueList.get(i).replace("\"","");
						obj.put(name,value);
					}
					i++;
				}
				arr.add(obj);
//			}
		}
		cr.close();
		File flie = new File(realPath);
		boolean falg = flie.delete();
		if(falg){
			System.out.println("文件删除success");
		}else{
			System.out.println("文件删除fail");
		}
        return arr;
    }
	public static String getPath(String realpath){
		   Date dt=new Date();
	        SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-ddHH");
	        String date=matter1.format(dt).replaceAll("-","").trim();
        realpath = realpath+"_"+date+".csv";
        return realpath;
    }
	
}

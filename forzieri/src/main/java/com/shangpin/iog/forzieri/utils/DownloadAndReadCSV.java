package com.shangpin.iog.forzieri.utils;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import com.csvreader.CsvReader;

/**
 * Created by monkey on 2015/10/29.
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
    public static List<String> downloadNet() throws MalformedURLException {
        int bytesum = 0;
        int byteread = 0;
        int num = 0;
        List<String> returnList = new ArrayList<String>();
        String[] urls = httpurl.split(",");
        try {
	        for (String hurl : urls) {
	        	num++;
	        	URL url = new URL(hurl);
	        	String realPath="";
	        	URLConnection conn = url.openConnection();
	        	InputStream inStream = conn.getInputStream();
	        	realPath = getPath(path+num);
	        	FileOutputStream fs = new FileOutputStream(realPath);
	        	
	        	byte[] buffer = new byte[1204];
	        	int length;
	        	while ((byteread = inStream.read(buffer)) != -1) {
	        		bytesum += byteread;
	        		System.out.println(bytesum);
	        		fs.write(buffer, 0, byteread);
	        	}
	        	returnList.add(realPath);
			}
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  returnList;
    }
    public static <T> T fillDTO(T t,List<String> data){
    	try {
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < data.size(); i++) {
				fields[i].setAccessible(true);
				fields[i].set(t, data.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
    }
    /**
     * 填充csvDTO
     * @param clazz 要填充的类类型 
     * @param sep csv分隔符
     * @return 填充好的集合
     * @throws Exception
     */
    public static <T> List<T> readLocalCSV(Class<T> clazz,String sep) throws Exception {
        
    	//--List<String> realPaths=downloadNet();
        String rowString = null;
        List<T> dtoList = new ArrayList<T>();
        //Set<T> dtoSet = new HashSet<T>();
    	String[] split = null;
    	List<String> colValueList = null;
    	CsvReader cr = null;
    	//--for (String realPath : realPaths) {
    		//解析csv文件
//--    		cr = new CsvReader(new FileReader(realPath));
    		cr = new CsvReader(new FileReader("F:/forzieri1.csv"));//----
    		System.out.println("创建cr对象成功");
    		//得到列名集合
    		cr.readRecord();
    		int a = 0;
    		while(cr.readRecord()){
    			a++;
    			rowString = cr.getRawRecord();
    			if (StringUtils.isNotBlank(rowString)) {
    				split = rowString.split(sep);
    				colValueList = Arrays.asList(split);
    				T t = fillDTO(clazz.newInstance(),colValueList);
    				//过滤重复的dto。。。sku,
    				//dtoSet.add(t);
    				dtoList.add(t);
				}
    			System.out.println(a);
    		}
		//--}
        return dtoList;
    }
  
    public static String getPath(String realpath){
        //Date dt=new Date();
        SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-ddHH");
        //String date=matter1.format(dt).replaceAll("-","").trim();
        //realpath = realpath+"_"+date+".csv";
        realpath = realpath+".csv";
        return realpath;
    }
  /* public static void main(String[] args) {
        try {
        	System.out.println("下载中");
			List<CsvDTO> lists = readLocalCSV(CsvDTO.class,"\\|");
            System.out.println("====");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}

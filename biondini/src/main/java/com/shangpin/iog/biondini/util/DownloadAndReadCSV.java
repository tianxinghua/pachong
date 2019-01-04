package com.shangpin.iog.biondini.util;


import com.csvreader.CsvReader;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by monkey on 2015/10/29.
 */
public class DownloadAndReadCSV {
    public static final String PROPERTIES_FILE_NAME = "conf";
    static ResourceBundle bundle = null ;
	private static String filePath ="";
    static {
    	if(null==bundle){
			bundle = ResourceBundle.getBundle(PROPERTIES_FILE_NAME) ;
		}
		filePath = bundle.getString("filePath");
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
        
    	List<String> realPaths=new ArrayList<String>();
    	realPaths.add(filePath);
    	File file = new File(filePath);
		List<T> dtoList = new ArrayList<T>();
    	if(!file.exists()){
    		return dtoList;
		}
        String rowString = null;

        //Set<T> dtoSet = new HashSet<T>();
    	String[] split = null;
    	List<String> colValueList = null;
    	CsvReader cr = null;
    	for (String realPath : realPaths) {
    		//解析csv文件
        	cr = new CsvReader(new FileReader(realPath));

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
		}
        return dtoList;
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

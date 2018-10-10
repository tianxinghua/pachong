package com.shangpin.com.mcw.service;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.csvreader.CsvReader;
import org.apache.commons.lang.StringUtils;



/**
 * Created by monkey on 2015/10/29.
 */
public class DownloadAndReadCSV {

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
        

   		String realPaths="D://mcw.csv";
   		//String realPaths="G:/test1.csv";
        String rowString = null;

        List<T> dtoList = new ArrayList<T>();
    	String[] split = null;
    	List<String> colValueList = null;
    	CsvReader cr = null;
		//解析csv文件
		InputStream is=new FileInputStream(realPaths);
		cr=new CsvReader(is,Charset.forName("GBK"));
		//得到列名集合
		cr.readRecord();
		int a = 0;
		while(cr.readRecord()){
			a++;
			rowString = cr.getRawRecord();
			System.out.println(rowString);
			if (StringUtils.isNotBlank(rowString)) {
				split = rowString.split(",");
				colValueList = Arrays.asList(split);
				T t = fillDTO(clazz.newInstance(),colValueList);
				dtoList.add(t);
			}
			System.out.println(a);
		}
        return dtoList;
    }
}

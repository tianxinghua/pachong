package com.shangpin.iog.utils;


import com.csvreader.CsvReader;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

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
	 * 默认跳过第一行的 title 数据读取（while 中 的 a++;）
	 * @param filePath 文件存放位置
	 * @param clazz 每行对应实体 DTO class
	 * @param sep 数据分隔符
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
    public static <T> List<T> readLocalCSV(String filePath, Class<T> clazz,String sep) throws Exception {
        
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
    			if (!StringUtils.isEmpty(rowString)) {
    				split = rowString.split(sep);
    				colValueList = Arrays.asList(split);
    				T t = fillDTO(clazz.newInstance(),colValueList);
    				//过滤重复的dto。。。sku,
    				//dtoSet.add(t);
    				dtoList.add(t);
				}
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

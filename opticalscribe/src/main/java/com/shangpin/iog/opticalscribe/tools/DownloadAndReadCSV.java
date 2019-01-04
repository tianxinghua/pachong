package com.shangpin.iog.opticalscribe.tools;


import com.csvreader.CsvReader;
import com.shangpin.iog.opticalscribe.dto.TemplateProductDTO;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	 * @param charset 文件编码读取格式 utf-8 gbk
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
    public static <T> List<T> readLocalCSV(String filePath, Class<T> clazz,String sep,String charset) throws Exception {
    	File file = new File(filePath);
		List<T> dtoList = new ArrayList<T>();
    	if(!file.exists()){
    		return dtoList;
		}
        String rowString = null;
    	String[] split = null;
    	List<String> colValueList = null;
		FileInputStream fin = new FileInputStream(filePath);
		Charset charsetObj = Charset.forName(charset);
		CsvReader cr = new CsvReader(fin, charsetObj);
		System.out.println("创建cr对象成功");
		//得到列名集合（默认跳过第一行）
		cr.readRecord();
		while(cr.readRecord()){
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
        return dtoList;
    }
  

  	public static void main(String[] args) {
        try {
        	System.out.println("下载中");
			List<TemplateProductDTO> lists = readLocalCSV("D://GUCCI/gucci-it.csv",TemplateProductDTO.class,",","gbk");
            System.out.println("====");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

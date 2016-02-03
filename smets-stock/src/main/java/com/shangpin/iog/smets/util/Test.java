package com.shangpin.iog.smets.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.csvreader.CsvReader;
import com.shangpin.iog.smets.dto.TxtDTO;

public class Test {
	public static void main(String[] args) {
		List<TxtDTO> dataList = new ArrayList<TxtDTO>();
		List<TxtDTO> logList = new ArrayList<TxtDTO>();
		Map<String,String> logmap = new HashMap<String, String>();
		String rowString = null;
		String[] split = null;
		List<String> colValueList = null;
		try {
			int a = 0;
			CsvReader cr = new CsvReader("D:/smetssssssssss.txt");
			while (cr.readRecord()) {
				rowString = cr.getRawRecord();   
				if (StringUtils.isNotBlank(rowString)) {
					a++;
					split = rowString.split(";");
					colValueList = Arrays.asList(split);
					TxtDTO t = new Test().fillDTO(new TxtDTO(), colValueList);
					dataList.add(t);
				}
			}
			System.out.println("a="+a);
			CsvReader cr1 = new CsvReader("D:/smets-stock-info1.log");
			int b = 0;
			while (cr.readRecord()) {
				rowString = cr.getRawRecord();   
				if (StringUtils.isNotBlank(rowString)) {
					//3000003328498
					Pattern p = Pattern.compile("suppliersku: (\\d*)"); 
					Matcher matcher = p.matcher(rowString);
					if (matcher.find()) {
						b++;
						logmap.put(matcher.group().substring(13), "1");
					}
				}
			}
			System.out.println("b="+b);
			int num = 0;
			for (TxtDTO dto : dataList) {
				if (logmap.containsKey(dto.getSkuId())) {
					num++;
				}
			}
			System.out.println("num="+num);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public <T> T fillDTO(T t,List<String> data){
    	try {
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < data.size(); i++) {
				fields[i].setAccessible(true);
				fields[i].set(t,data.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
    }
}
	

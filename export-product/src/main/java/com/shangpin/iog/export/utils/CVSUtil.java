package com.shangpin.iog.export.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.csvreader.CsvReader;

public class CVSUtil {

	/**
	 * 生成csv文件
	 * @param buffer 
	 * @param filePath 要写入的文件  xxx.csv
	 */
	public static void writeCSV(StringBuffer buffer,String filePath){
		
		String messageText  = buffer.toString();
		if(StringUtils.isNotBlank(messageText)){
			BufferedInputStream in = null;
			File file = null;
			FileOutputStream out = null;
			try{
				in = new BufferedInputStream(new ByteArrayInputStream(messageText.getBytes("gb2312")));
				file = new File(filePath);
				out = new FileOutputStream(file);
				byte[] data = new byte[1024];
	            int len = 0;
	            while (-1 != (len=in.read(data, 0, data.length))) {
	                out.write(data, 0, len);
	            }
			}catch(Exception e){
				e.printStackTrace();
			}finally {
				try{
					if (in != null) {
		                in.close();
		            }
		            if (out != null) { 
		                out.close();
		            }
				}catch(Exception ex){
					ex.printStackTrace(); 
				}
	            
	        }
			
		}
	
	}
	
	/**
	 * 
	 * @param file csv文件
	 * @param clazz 映射的类
	 * @param sep 分隔符
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> readCSV(File file,Class<T> clazz, char sep)
			throws Exception {
		
		String rowString = null;
		List<T> dtoList = new ArrayList<T>();
		List<String> colValueList = null;
		CsvReader cr = null;
		// 解析csv文件
		cr = new CsvReader(new FileReader(file));
		System.out.println("创建cr对象成功");
		// 得到列名集合
		cr.readRecord();
		int a = 0;
		while (cr.readRecord()) {
			a++;
			rowString = cr.getRawRecord();
			if (StringUtils.isNotBlank(rowString)) {
				colValueList = fromCSVLinetoArray(rowString,sep);
				T t = fillDTO(clazz.newInstance(), colValueList);
				// 过滤重复的dto。。。sku,
				// dtoSet.add(t);
				dtoList.add(t);
			}

		}

		return dtoList;
	}

	/**
	 * 把CSV文件的一行转换成字符串数组。不指定数组长度。
	 */
	public static ArrayList fromCSVLinetoArray(String source,char sep) {
		if (source == null || source.length() == 0) {
			return new ArrayList();
		}
		int currentPosition = 0;
		int maxPosition = source.length();
		int nextComma = 0;
		ArrayList rtnArray = new ArrayList();
		while (currentPosition < maxPosition) {
			nextComma = nextComma(source, currentPosition,sep);
			rtnArray.add(nextToken(source, currentPosition, nextComma));
			currentPosition = nextComma + 1;
			if (currentPosition == maxPosition) {
				rtnArray.add("");
			}
		}
		return rtnArray;
	}

	/**
	 * 查询下一个逗号的位置。
	 * 
	 * @param source
	 *            文字列
	 * @param st
	 *            检索开始位置
	 * @return 下一个逗号的位置。
	 */
	private static int nextComma(String source, int st,char sep) {
		int maxPosition = source.length();
		boolean inquote = false;
		while (st < maxPosition) {
			char ch = source.charAt(st);
			if (!inquote && ch == sep) {
				break;
			} else if ('"' == ch) {
				inquote = !inquote;
			}
			st++;
		}
		return st;
	}

	/**
	 * 取得下一个字符串
	 */
	private static String nextToken(String source, int st, int nextComma) {
		StringBuffer strb = new StringBuffer();
		int next = st;
		while (next < nextComma) {
			char ch = source.charAt(next++);
			if (ch == '"') {
				if ((st + 1 < next && next < nextComma)
						&& (source.charAt(next) == '"')) {
					strb.append(ch);
					next++;
				}
			} else {
				strb.append(ch);
			}
		}
		return strb.toString();
	}

	public static <T> T fillDTO(T t, List<String> data) {
		try {
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				fields[i].set(t, data.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	
}

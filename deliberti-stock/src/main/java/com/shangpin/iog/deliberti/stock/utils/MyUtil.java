package com.shangpin.iog.deliberti.stock.utils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.csvreader.CsvReader;

public class MyUtil {

	/**
	 * 
	 * @param uri 请求地址
	 * @param clazz 对应实体类
	 * @param sep 分隔符
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> readLocalCSV(String uri,Class<T> clazz)
			throws Exception {
		URL url = new URL(uri);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为60分钟
		conn.setConnectTimeout(60 * 60 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		// 得到输入流
		InputStream in = conn.getInputStream();
		if (in == null) {
			System.out.println("下载失败！！！！！！！！！！");
			System.exit(0);
		}
		
		String rowString = null;
		List<T> dtoList = new ArrayList<T>();
		List<String> colValueList = new ArrayList<>();
		CsvReader cr = null;
		
		String[] split = null;
		// 解析csv文件
		cr = new CsvReader(in, Charset.forName("UTF-8"));
		System.out.println("创建cr对象成功");
		// 得到列名集合
		cr.readRecord();
		int a = 0;
		while (cr.readRecord()) {
			a++;
			System.out.println("a="+a);
			rowString = cr.getRawRecord();
			if (StringUtils.isNotBlank(rowString)) {
				split = rowString.split("[|]");
				colValueList = Arrays.asList(split);
				System.out.println("第"+a+"行长度为:"+colValueList.size());
				T t = fillDTO(clazz.newInstance(),colValueList);
				//过滤重复的dto。。。sku,
				//dtoSet.add(t);
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

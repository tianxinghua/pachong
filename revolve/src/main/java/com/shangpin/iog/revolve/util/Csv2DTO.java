package com.shangpin.iog.revolve.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.csvreader.CsvReader;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.revolve.sepStrategy.ISepStrategy;

public class Csv2DTO {
	/**
	 * 将csv文件转换成dto
	 * 
	 * @param filePath
	 *            csv路径
	 * @param needColsNo
	 *            需要csv的哪几列
	 * @param clazz
	 *            要转换成的dto
	 * @return List<DTO>
	 */
	@SuppressWarnings("resource")
	public  <T> List<T> toDTO(String url,String filePath, Class<T> clazz) {
//		filePath = "F://code//products";
		txtDownload(url,filePath);
		List<T> dtoList = new ArrayList<T>();
		CsvReader cr = null;
		String[] split = null;
		List<String> colValueList = null;
		String rowString = "";
		try {
//			InputStream in = new FileInputStream("E:\\products.txt");
			InputStream in = new FileInputStream(filePath);
			cr = new CsvReader(in, Charset.forName("utf-8"));
			cr.readRecord();
			while(cr.readRecord()){
				rowString = cr.getRawRecord();
				if (StringUtils.isNotBlank(rowString)) {
//					colValueList = fromCSVLinetoArray(rowString,sep.charAt(0));
//					split = rowString.split(sep);
//					colValueList = Arrays.asList(split);
//					T t = fillDTO(clazz.newInstance(),needColsNo,iSepStrategies, colValueList);
//					dtoList.add(t);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dtoList;
	}

	private <T> T fillDTO(T t,String[] needColsNo,ISepStrategy[] iSepStrategies,List<String> data) {
		try {
			Field[] fields = t.getClass().getDeclaredFields();
			String[] split = null;
			String str = "";
			for (int i = 0; i < needColsNo.length; i++) {
				fields[i].setAccessible(true);
				if (needColsNo[i].equals("")) {
					fields[i].set(t,"");
					continue;
				}
				str = "";
				split = needColsNo[i].split(",");
				List<String> dataList = new ArrayList<String>();
				
				for (int j = 0; j < split.length; j++) {
					dataList.add(data.get(Integer.valueOf(split[j])));
//					str+=data.get(Integer.valueOf(split[j]))+colsSep[i];
				}
				
//				str+=data.get(Integer.valueOf(split[split.length-1]));
				fields[i].set(t,iSepStrategies[i].merge(dataList));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	// http异常
	private String getHttpStr(String url) {
		
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();
//		HttpPost httpPost = new HttpPost("https://api.forzieri.com/test/orders");
		HttpGet httpPost = new HttpGet(url);
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        CloseableHttpResponse response = null;
        String str = "";
		try {
			response = httpClient.execute(httpPost);
			str = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
//		String str = HttpUtil45.get(url, new OutTimeConfig(1000 * 60 * 10,1000 * 60 * 20, 1000 * 60 * 20), null);
		return str;
	}

	/**
	 * http下载txtcsv文件到本地路径
	 * 
	 * @throws MalformedURLException
	 */
	private void txtDownload(String url,String filepath){
		
//		String json = HttpUtil45.get(url, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10),null);
//		System.out.println(json);
		String csvFile = getHttpStr(url);
		System.out.println(csvFile.length());
		if (csvFile.contains("发生错误异常")) {
			try {
				Thread.currentThread().sleep(5000l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			csvFile = getHttpStr(url);
		}
		FileWriter fwriter = null;
		try {
			File file = new File(filepath);
			if (!file.exists()) {
				file.createNewFile();
			}
			fwriter = new FileWriter(filepath);
			fwriter.write(csvFile);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				fwriter.flush();
				fwriter.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 把CSV文件的一行转换成字符串数组。不指定数组长度。
	 */
	public ArrayList<String> fromCSVLinetoArray(String source,char sep) {
		if (source == null || source.length() == 0) {
			return new ArrayList<String>();
		}
		int currentPosition = 0;
		int maxPosition = source.length();
		int nextComma = 0;
		ArrayList<String> rtnArray = new ArrayList<String>();
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
	private int nextComma(String source, int st,char sep) {
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
	private String nextToken(String source, int st, int nextComma) {
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
}

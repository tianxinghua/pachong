package com.shangpin.iog.theclutcher.utils;

import java.io.*;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class DownloadFileFromNet {

	/**
	 * 
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @return
	 * @throws IOException
	 */
	public static File downLoad(String urlStr, String fileName, String savePath)
			throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为5分钟
		conn.setConnectTimeout(5*60*1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		// 得到输入流
		InputStream inputStream = conn.getInputStream();
		// 获取自己数组
		byte[] getData = readInputStream(inputStream);

		// 文件保存位置
		File saveDir = new File(savePath);
		if (!saveDir.exists()) {
			saveDir.mkdir();
		}
		File file = new File(saveDir + File.separator + fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		fos.flush(); 
		if (fos != null) { 
			fos.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}
		System.out.println("info:" + url + " download success");
		
		return file;
	}

	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}
	
	/**
	 * 文件转字符串
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws JDOMException 
	 */
	public static String file2Striing(File file) throws IOException{
		BufferedReader bufferedReader = new BufferedReader(new UnicodeReader(new FileInputStream(file),"utf-8"));
		StringBuilder stringBuilder = new StringBuilder();
		String content = "";
		while ((content = bufferedReader.readLine()) != null) {
			stringBuilder.append(content); 
		}
		String result = stringBuilder.toString();
		
		return result;
	}

}

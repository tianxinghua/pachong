package com.shangpin.iog.papini.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBException;

import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.papini.dto.Product;
import com.shangpin.iog.papini.dto.Products;

public class ZipUtil {
	private static ResourceBundle bdl = null;
	public static String url;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		url = bdl.getString("url");
	}
	public static void main(String[] args) {
		List<Product> allProduct = new ZipUtil().getAllProduct();
		for (Product product : allProduct) {
			System.out.println(product.getProductId());
		}
	}
	public List<Product> getAllProduct(){
		Products products = null;
		List<Product> productList = new ArrayList<Product>();
		String xml = downLoadAndReadXml(url);
		System.out.println(url);
			ByteArrayInputStream is = null;
			try {
				is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e2) {
				e2.printStackTrace();
			}
		
		try {
			products = ObjectXMLUtil.xml2Obj(Products.class,is);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		if (products.getProducts()!=null&&products.getProducts().size()>0) {
			productList.addAll(products.getProducts());
		}
		return productList;
	}
	
	private String readZipFile(String file) throws Exception {
		String content = "";
		ZipFile zf = new ZipFile(file);
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry ze;
		while ((ze = zin.getNextEntry()) != null) {
			if (!ze.isDirectory()) {
				System.out.println("file - " + ze.getName() + " : "+ ze.getSize() + " bytes");
				long size = ze.getSize();
				if (size > 0) {
					BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
					String line;
					while ((line = br.readLine()) != null) {
						content += line;
					}
					br.close();
				}
			}
		}
		zin.close();
		zf.close();
		return content;
	}
	
	private  String downLoadAndReadXml(String zipUrl) {
		int byteSum = 0;
		int byteRead = 0;
		String content = "";
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
		String folder = System.getProperty("java.io.tmpdir");
		String localFilePath = folder + "/papini_"+ sf1.format(new Date()) + ".zip";
		File zipFile = new File(localFilePath);
		if (zipFile.exists()) {
			zipFile.delete();
		}
		InputStream inStream = null;
		FileOutputStream fs = null;
		try {
			URL url = new URL(zipUrl);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(1000*60*60);
			conn.setReadTimeout(1000*60*60);
			inStream = conn.getInputStream();
			fs = new FileOutputStream(zipFile);
			byte[] buffer = new byte[4096];
			while ((byteRead = inStream.read(buffer)) != -1) {
				byteSum += byteRead;
				fs.write(buffer, 0, byteRead);
			}
			fs.flush();
			content = readZipFile(localFilePath);
			// XML内容读取完毕应删除文件
			zipFile.delete();
			System.out.println("文件下载成功.....size=" + byteSum);
		} catch (Exception e) {
			System.out.println("下载异常" + e);
			return null;
		} finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				inStream = null;
			}
			try {
				if (fs != null) {
					fs.close();
				}
			} catch (IOException e) {
				fs = null;
			}
		}
		return content;
	}

}

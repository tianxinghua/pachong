package com.shangpin.iog.stefaniamode;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.stefaniamode.service.FetchProduct;

public class Startup {
	private static Logger log = Logger.getLogger("info");

	private static ApplicationContext factory;

	private static ResourceBundle bdl = null;

	public static String supplierId;

	public static String zipUrl;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		zipUrl = bdl.getString("zipUrl");
	}

	private static void loadSpringContext() {
		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	public static String readZipFile(String file) throws Exception {
		String content = "";
		ZipFile zf = new ZipFile(file);
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry ze;
		while ((ze = zin.getNextEntry()) != null) {
			if (!ze.isDirectory()) {
				System.out.println("file - " + ze.getName() + " : "
						+ ze.getSize() + " bytes");
				long size = ze.getSize();
				if (size > 0) {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(zf.getInputStream(ze)));
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

	public static String downLoadAndReadXml(String zipUrl) {
		int byteSum = 0;
		int byteRead = 0;
		String content = "";
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
		String folder = System.getProperty("java.io.tmpdir");
		String localFilePath = folder + "/stefaniamode_"
				+ sf1.format(new Date()) + ".zip";
		File zipFile = new File(localFilePath);
		if (zipFile.exists()) {
			// 测试时下载成功一次可以重复使用文件内容；
			// try {
			// return readZipFile(localFilePath);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			zipFile.delete();
		}
		InputStream inStream = null;
		FileOutputStream fs = null;
		try {
			URL url = new URL(zipUrl);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(3600000);
			conn.setReadTimeout(3600000);
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

	public static void main(String[] args) {

		// 加载spring
		log.info("----拉取stefaniamode数据开始----");
		loadSpringContext();
		log.info("----初始SPRING成功----");
		// 拉取数据
		FetchProduct fetchProduct = (FetchProduct) factory.getBean("stefaniamode");
		String[] split = zipUrl.split(",");
		for (String url : split) {
			String xmlContent = downLoadAndReadXml(url);
			fetchProduct.fetchProductAndSave(xmlContent, supplierId);
		}

		log.info("----拉取stefaniamode数据完成----");

		System.out.println("-------fetch end---------");

	}

}

package com.shangpin.iog.theclutcher.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;

public class UNZIPFile {
	
	private static Logger logger = Logger.getLogger("info");

	/**
	 * 解压zip文件
	 * @param zipFile 要解压的zip
	 * @param descDir 解压后文件保存路径
	 * @throws IOException
	 */
	public static File unZipFile(File zipFile, String descDir)
			throws IOException {
//		logger.info("====================解压文件开始=================");
		File retFile = null;
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		ZipFile zip = new ZipFile(zipFile);
		for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = zip.getInputStream(entry);
			String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
			;
			// 判断路径是否存在,不存在则创建文件路径
			File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			if (!file.exists()) {
				file.mkdirs();
			}
			// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if ((retFile = new File(outPath)).isDirectory()) {
				continue;
			}
			// 输出文件路径信息
			System.out.println(outPath);

			OutputStream out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			out.flush();
			in.close();
			out.close();
		}
		System.out.println("******************解压完毕********************");
//		logger.info("====================解压文件结束=================");
		return retFile;
	}
}

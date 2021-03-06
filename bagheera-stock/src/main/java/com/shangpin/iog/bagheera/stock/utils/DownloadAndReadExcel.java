package com.shangpin.iog.bagheera.stock.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.shangpin.iog.bagheera.stock.dto.BagheeraDTO;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

/**
 * Created by sunny on 2015/9/8.
 */
public class DownloadAndReadExcel {
	public static final String PROPERTIES_FILE_NAME = "conf";
	static ResourceBundle bundle = ResourceBundle
			.getBundle(PROPERTIES_FILE_NAME);
	private static String path = bundle.getString("path");
	private static Logger logger = Logger.getLogger("info");
	private static String httpurl = bundle.getString("url");

	/**
	 * http下载excel文件到本地路径
	 * 
	 * @throws MalformedURLException
	 */
	public static String downloadNet() {
		int bytesum = 0;
		int byteread = 0;
		String realPath = "";
		try {
			URL url = new URL(httpurl);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(1000 * 60 * 30);
			conn.setConnectTimeout(1000 * 60 * 60);
			InputStream inStream = conn.getInputStream();
			realPath = getPath(path);
			FileOutputStream fs = new FileOutputStream(realPath);

			byte[] buffer = new byte[1204];
			int length;
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				System.out.println(bytesum);
				fs.write(buffer, 0, byteread);
			}
		} catch (Exception e) {
			logger.info("下载失败");
			return "";
		}
		return realPath;
	}

	public static List<BagheeraDTO> readLocalExcel() throws IOException {
		String realPath = downloadNet();
		
		//若下载失败，重复10遍
		int k = 0;
		while(StringUtils.isBlank(realPath) && k<10){
			try {
				Thread.sleep(1000*3);
				realPath = downloadNet();
			} catch (Exception e) {				
				e.printStackTrace();
			}finally{
				k++;
			}
		}
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		FileInputStream fw = null;
		/*
		 * 要想把excel中的每一行数据转换成javabean对象则用反射技术
		 * javabean中的属性个数要与excel中的列数一样，不然可能报错,属性顺序与列的顺序也要一样
		 */
		try {
			fw = new FileInputStream(realPath);
			POIFSFileSystem fs = new POIFSFileSystem(fw);
			wb = new HSSFWorkbook(fs);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				fw.close();
			}
		}
		HSSFRow row = null;
		List<BagheeraDTO> dataList = new ArrayList<BagheeraDTO>();
		for (int i = 0; i < 1; i++) {
			// 循环excel中所有的 sheet
			sheet = wb.getSheetAt(i);
			for (int j = 1; j < sheet.getPhysicalNumberOfRows(); j++) {
				// 循环每一个sheet中的每一行
				row = sheet.getRow(j);
				BagheeraDTO bagheeraDTO = new BagheeraDTO();
				Field[] beanFiled = bagheeraDTO.getClass().getDeclaredFields();
				for (int z = 0; z < 21; z++) {
					// 循环每一行中的所有列,就是得到单元格中的数据
					try {
						// 强制反射,让private 的属性也可以访问
						beanFiled[z].setAccessible(true);
						// 把得到的属性进行赋值，就是把读取到的单元格中的数据赋给对应的属性
						short a = (short) z;
						beanFiled[z].set(bagheeraDTO,
								getStrinCellValue(row.getCell(a)));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				dataList.add(bagheeraDTO);
			}
		}
		return dataList;
	}

	public static String getStrinCellValue(HSSFCell cell) {

		if (cell == null) {
			return "";
		}
		String strCell = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			strCell = cell.toString();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			strCell = String.valueOf(cell.getNumericCellValue());
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getStringCellValue());
		case HSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
		}

		if (strCell.equals("") || strCell == null) {
			return "";
		}
		return strCell;
	}

	public static String getPath(String realpath) {
		// Date dt=new Date();
		// SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-ddHH");
		// String date=matter1.format(dt).replaceAll("-","").trim();
		realpath = realpath + ".xls";
		return realpath;
	}

	public static void main(String[] args) {
		FileOutputStream fileOutputStream = null;
		HttpGet get = new HttpGet("http://www.bagheeraboutique.com/en-US/home/shangpin");
		CloseableHttpResponse resp=null;
		File file = new File("F://test//abc.xls");
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		OutTimeConfig outTimeConf = new OutTimeConfig(1000 * 60 * 10,
				1000 * 60 * 120, 1000 * 60 * 120);
		HttpClientContext localContext = HttpClientContext.create();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {
			localContext.setRequestConfig(defaultRequestConfig(outTimeConf));
			fileOutputStream = new FileOutputStream(file);
			resp=httpClient.execute(get,localContext);

			HttpEntity entity=resp.getEntity();
			entity.writeTo(fileOutputStream);
			fileOutputStream.flush();
		}catch(Exception e){
			logger.error("--------------httpError:"+e.getMessage());
		}finally{
			try {
				if(resp!=null)
					resp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		

	}
	private static RequestConfig defaultRequestConfig(OutTimeConfig outTimeConf){
		OutTimeConfig outCnf=outTimeConf;
		if(outTimeConf==null) outCnf=OutTimeConfig.defaultOutTimeConfig();
		return RequestConfig.custom()
				.setConnectionRequestTimeout(outCnf.getConnectOutTime())
				.setConnectTimeout(outCnf.getConnectOutTime())
				.setSocketTimeout(outCnf.getSocketOutTime())
				.build();
	}
}

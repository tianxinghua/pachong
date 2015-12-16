/**
 * Created by huxia on 2015/6/10.
 */
package com.shangpin.iog.stefaniamode.stock;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBException;

import com.shangpin.iog.app.AppContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.stefaniamode.stock.dto.Item;
import com.shangpin.iog.stefaniamode.stock.dto.Items;
import com.shangpin.iog.stefaniamode.stock.dto.Product;
import com.shangpin.iog.stefaniamode.stock.dto.Products;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component("stefaniamodeStock")
public class GrabStockImp extends AbsUpdateProductStock {
	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	private static ApplicationContext factory;
	private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	private static ResourceBundle bdl = null;
	private static String supplierId;
	public static String zipUrl;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		zipUrl = bdl.getString("zipUrl");
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
			try {
				return readZipFile(localFilePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
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
	public List<Product> getProductList(String[] urls) throws ServiceMessageException{
		String xml = "";
		Products products = null;
		List<Product> productList = new ArrayList<Product>();
		try {
			for (String url : urls) {
				xml = downLoadAndReadXml(url);
				products = ObjectXMLUtil.xml2Obj(Products.class, xml);
				productList.addAll(products.getProducts());
			}
		} catch (Exception e) {
			e.printStackTrace();
			loggerError.error("拉取stefaniamode数据失败---" + e.getMessage());
			throw new ServiceMessageException("拉取stefaniamode数据失败");
		}
		return productList;
	}
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException {
		Map<String, String> skustock = new HashMap<>(skuNo.size());
		Map<String, String> stockMap = new HashMap<>();
		String[] urls = zipUrl.split(",");
		List<Product> productList = null;
//		Products products = null;
		try {
			logger.info("拉取stefaniamode数据开始");

			Map<String, String> mongMap = new HashMap<>();
//			String xmlContent = downLoadAndReadXml(zipUrl);

			mongMap.put("supplierId", supplierId);
			mongMap.put("supplierName", "stefaniamode");
			logger.info(mongMap);
			productList = getProductList(urls);
//			products = ObjectXMLUtil.xml2Obj(Products.class, xmlContent);
			logger.info("拉取stefaniamode数据成功");
		} catch (Exception e) {
			e.printStackTrace();
			loggerError.error("拉取stefaniamode数据失败---" + e.getMessage());
			throw new ServiceMessageException("拉取stefaniamode数据失败");

		} finally {
			HttpUtil45.closePool();
		}
		String skuId = "";
		for (Product product : productList) {

			Items items = product.getItems();
			if (null == items) {
				continue;
			}
			List<Item> itemList = items.getItems();
			if (null == itemList)
				continue;
			for (Item item : items.getItems()) {
				if (StringUtils.isNotBlank(item.getStock())) {
					skuId = item.getItem_id();
					if (skuId.indexOf("½") > 0) {
						skuId = skuId.replace("½", "+");
					}
					stockMap.put(skuId, item.getStock());
				}
			} 
		}

		for (String skuno : skuNo) {

			if (stockMap.containsKey(skuno)) {
				skustock.put(skuno, stockMap.get(skuno));
			} else {
				skustock.put(skuno, "0");
			}
		}
		logger.info("stefaniamode赋值库存数据成功");
		return skustock;
	}



	public static void main(String[] args) throws Exception {

		//加载spring
        loadSpringContext();
        GrabStockImp grabStockImp = (GrabStockImp)factory.getBean("stefaniamodeStock");
		//AbsUpdateProductStock grabStockImp = new GrabStockImp();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logger.info("stefaniamode更新数据库开始");
		try {
			grabStockImp.updateProductStock(supplierId, "2015-01-01 00:00",
                    format.format(new Date()));
			logger.info("stefaniamode更新数据库成功结束");
		} catch (Exception e) {
			loggerError.error("stefaniamode更新库存失败");
		}

		System.exit(0);

	}

}

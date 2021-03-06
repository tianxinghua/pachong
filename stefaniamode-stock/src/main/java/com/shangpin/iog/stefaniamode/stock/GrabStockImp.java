/**
 * Created by huxia on 2015/6/10.
 */
package com.shangpin.iog.stefaniamode.stock;

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
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
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

	private static void loadSpringContext() {
		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	private static ResourceBundle bdl = null;
	private static String supplierId;
	public static String url;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
	}


	public List<Product> getProductList(String url)
			throws ServiceMessageException {
		String xml = null;
		xml = HttpUtil45.get(url, new OutTimeConfig(1000 * 60 * 5,
				1000 * 60 * 5, 1000 * 60 * 5), null);
		System.out.println(url);
		ByteArrayInputStream is = null;

		try {
			is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Products products = null;
		List<Product> productList = new ArrayList<Product>();
		try {
			products = ObjectXMLUtil.xml2Obj(Products.class, is);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		if (products.getProducts() != null && products.getProducts().size() > 0) {
			productList.addAll(products.getProducts());
		}
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return productList;
	}

	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException {
		Map<String, String> skustock = new HashMap<>(skuNo.size());
		Map<String, String> stockMap = new HashMap<>();
		List<Product> productList = null;
		// Products products = null;
		try {
			logger.info("拉取stefaniamode数据开始");

			Map<String, String> mongMap = new HashMap<>();
			productList = getProductList(url);
			logger.info("拉取stefaniamode数据成功");
		} catch (Exception e) {
			e.printStackTrace();
			loggerError.error("拉取stefaniamode数据失败---" + e.getMessage());
			throw new ServiceMessageException("拉取stefaniamode数据失败");

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

//	public static void main(String[] args) throws Exception {
//
//		// 加载spring
//		loadSpringContext();
//		GrabStockImp grabStockImp = (GrabStockImp) factory
//				.getBean("stefaniamodeStock");
//		// AbsUpdateProductStock grabStockImp = new GrabStockImp();
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		logger.info("stefaniamode更新数据库开始");
//		try {
//			grabStockImp.updateProductStock(supplierId, "2015-01-01 00:00",
//					format.format(new Date()));
//			logger.info("stefaniamode更新数据库成功结束");
//		} catch (Exception e) {
//			loggerError.error("stefaniamode更新库存失败");
//		}
//
//		System.exit(0);
//
//	}

}

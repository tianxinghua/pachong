package com.shangpin.iog.service;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.Item;

/**
 * Created by Administrator on 2015/7/8.
 */
@Component("coccole")
public class CoccoleStockImp extends AbsUpdateProductStock {

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String uri;
	private static String productUrl;
	public static int day;
	public static Gson gson = null;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		uri = bdl.getString("uri");
		productUrl = bdl.getString("productUrl");
		gson = new Gson();
	}
	private static ApplicationContext factory;

	private static void loadSpringContext() {

		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	private Map<String, String> skuStockMap = new HashMap<>();

	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		Map<String, String> stock_map = new HashMap<String, String>();
		String json = HttpUtil45.get(uri + productUrl, new OutTimeConfig(
				1000 * 60 * 20, 1000 * 60 * 20, 1000 * 60 * 20), null);
		try {
			List<Item> list = gson.fromJson(json, new TypeToken<List<Item>>() {
			}.getType());
			if (list != null) {
				for (Item item : list) {
					// 使用GSON，直接转成Bean对象
					skuStockMap.put(item.getKEY(), item.getDISPO());
				}
			}

			for (String skuno : skuNo) {
				if (skuStockMap.containsKey(skuno)) {
					stock_map.put(skuno, skuStockMap.get(skuno));
				} else {
					stock_map.put(skuno, "0");
				}

			}

		} catch (Exception e) {
			logger.info("拉取发生异常："+e);
		}

		logger.info("返回的map大小  stock_map.size======" + stock_map.size());
		return stock_map;
	}

	public static void main(String[] args) throws Exception {
		// 加载spring
		loadSpringContext();
		// 拉取数据
		CoccoleStockImp stockImp = (CoccoleStockImp) factory
				.getBean("coccole");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logger.info("coccole更新数据库开始");
		try {
			stockImp.updateProductStock(supplierId, "2015-01-01 00:00",
					format.format(new Date()));
		} catch (Exception e) {
			loggerError.error("coccole更新库存失败." + e.getMessage());
			e.printStackTrace();
		}
		logger.info("coccole更新数据库结束");
		System.exit(0);

	}

}

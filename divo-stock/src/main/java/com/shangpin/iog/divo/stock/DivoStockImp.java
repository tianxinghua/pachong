package com.shangpin.iog.divo.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.sop.AbsUpdateProductStock;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Component("divoStock")
public class DivoStockImp extends AbsUpdateProductStock {
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;

	private static void loadSpringContext() {
		factory = new AnnotationConfigApplicationContext(
				new Class[] { AppContext.class });
	}

	static {
		if (null == bdl) {
			bdl = ResourceBundle.getBundle("sop");
		}
		supplierId = bdl.getString("supplierId");
		host = bdl.getString("HOST");
		app_key = bdl.getString("APP_KEY");
		app_secret = bdl.getString("APP_SECRET");
	}

	private static String url = bdl.getString("url");
	private static String host;
	private static String app_key;
	private static String app_secret;
	private static ApplicationContext factory;

	public Map<String, Integer> grabStock(Collection<String> skuNo)throws ServiceException, Exception {
		Map<String, String> skuMap = new HashMap();
		int num = 0;
		String data = "";
		String skuData = HttpUtil45.post(url + "GetAllAvailabilityMarketplace",new OutTimeConfig(7200000, 7200000, 7200000));

		save("skuData.txt", skuData);
		String[] skuStrings = skuData.split("\\r\\n");
		for (int i = 1; i < skuStrings.length; i++) {
			if (StringUtils.isNotBlank(skuStrings[i])) {
				if (i == 1) {
					data = skuStrings[i].split("\\n")[1];
				} else {
					data = skuStrings[i];
				}
				String[] skuArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;", "").split(";");
				String stock = skuArr[2];
				String barCode = skuArr[5];
				skuMap.put(skuArr[0] + "-" + barCode, stock);
				if (!stock.equals("0")) {
					num++;
				}
			}
		}
		logger.info("新数据库存不为0的有：：：" + num);
		Map<String, Integer> returnMap = new HashMap();
		Iterator<String> iterator = skuNo.iterator();

		logger.info("循环赋值");
		String skuId = "";
		String stock = "0";
		while (iterator.hasNext()) {
			skuId = (String) iterator.next();
			if (StringUtils.isNotBlank(skuId)) {
				if (skuMap.containsKey(skuId)) {
					stock = (String) skuMap.get(skuId);
					try {
						returnMap.put(skuId, Integer.valueOf(stock));
					} catch (NumberFormatException e) {
						returnMap.put(skuId, Integer.valueOf(0));
					}
				} else {
					returnMap.put(skuId, Integer.valueOf(0));
				}
			}
		}
		return returnMap;
	}

	public void save(String name, String data) {
		File file = new File("/usr/local/appstock/divo/" + name);
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fwriter = null;
		try {
			fwriter = new FileWriter("/usr/local/appstock/divo/" + name);
			fwriter.write(data);
			return;
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

	public static void main(String[] args) throws Exception {
		loadSpringContext();
		DivoStockImp stockImp = (DivoStockImp) factory.getBean("divoStock");
		logger.info(stockImp.toString());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logger.info("divo更新数据库开始");
		try {
			stockImp.updateProductStock(host, app_key, app_secret,"2015-01-01 00:00", format.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("divo更新库存数据库出错" + e.toString());
		}
		logger.info("divo更新数据库结束");
		System.exit(0);
	}
}

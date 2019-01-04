package com.shangpin.iog.pozzilei.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.pozzilei.stock.dto.Quantity;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2015/7/8.
 */
@Component("pozzilei")
public class PozzileiStockImp extends AbsUpdateProductStock {

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");

	@Autowired
	private ProductFetchService pfs;

	@Autowired
	ProductSearchService productSearchService;
	private static ApplicationContext factory;

	private static void loadSpringContext() {

		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	private static ResourceBundle bdl = null;
	private static String supplierId;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
	}

	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		System.out.println(skuNo.size());
		Map<String, String> stock_map = new HashMap<String, String>();
		Gson gson = new Gson();
		String url = "", json = "";
		OutTimeConfig outTimeConfig = new OutTimeConfig(1000 * 60, 1000 * 60,
				1000 * 60);
		for (String skuno : skuNo) {
			String barCodeArray = pfs.findBarCodeBySupplierIdAndSkuId(supplierId,
					skuno);
			String barCode = null;
			String database = null;
			if (barCodeArray != null && barCodeArray.split("\\|").length > 1) {
				barCode = barCodeArray.split("\\|")[0];
				database = barCodeArray.split("\\|")[1];
			} else {
				stock_map.put(skuno, "0");
				continue;
			}
			// String itemId = skuno;
			// 根据供应商skuno获取库存，并更新我方sop库存
			url = "http://net13serverpo.net/pozziapi/Myapi/Productslist/GetQuantityByBarcode?DBContext="+database+"&barcode=[[barcode]]&key=5jq3vkBd7d";
			url = url.replaceAll("\\[\\[barcode\\]\\]", barCode);
			json = null;
			try {
				json = HttpUtil45.get(url, outTimeConfig, null);
			} catch (Exception e) {
				stock_map.put(skuno, "0"); // 读取失败的时候赋值为0
				loggerError.error("拉取失败 " + e.getMessage());
				e.printStackTrace();
				continue;
			}
			if (json != null && !json.isEmpty()) {
				if (json.equals("{\"Result\":\"No Record Found\"}")) { // 未找到
					stock_map.put(skuno, "0");
				} else {// 找到赋值

					try {
						Quantity result = gson.fromJson(json,
								new TypeToken<Quantity>() {
								}.getType());
						stock_map.put(skuno, result.getResult());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		return stock_map;
	}

	public static void main(String[] args) throws Exception {
		// 加载spring
		loadSpringContext();
		// //拉取数据
		 PozzileiStockImp stockImp
		 =(PozzileiStockImp)factory.getBean("pozzilei");
		// AbsUpdateProductStock grabStockImp = new SpinnakerStockImp();
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		 logger.info("pozzilei更新数据库开始");
		 try {
		 stockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new
		 Date()));
		 } catch (Exception e) {
		 loggerError.error("pozzilei更新库存失败."+e.getMessage());
		 e.printStackTrace();
		 }
		 logger.info("pozzilei更新数据库结束");
		 System.exit(0);

	}

}

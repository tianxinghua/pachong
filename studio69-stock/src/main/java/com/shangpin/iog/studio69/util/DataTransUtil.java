
package com.shangpin.iog.studio69.util;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.studio69.dto.Good;
import com.shangpin.iog.studio69.dto.GoodsStock;
import com.shangpin.iog.studio69.dto.Stock;
import com.shangpin.iog.studio69.dto.Stocks;

public class DataTransUtil {
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	private static String username;
	private static String password;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
		username = bdl.getString("username");
		password = bdl.getString("password");
	}

	public static Map<String,String> getGoodsStockList(){
		OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*10, 1000*60*10);
		Map<String,String> returnMap = new HashMap<String, String>();
		logger.info("获取所有库存");
		String data = HttpUtil45.postAuth(url+"GetGoodsStockList", null, outTimeConf, username, password);
		GoodsStock goodsStock = null;
		try {
			goodsStock = ObjectXMLUtil.xml2Obj(GoodsStock.class, data);
		} catch (JAXBException e) {
			logger.info("数据转换错误");
			System.exit(0);
		}
		String size = "";
		List<Good> goodList = goodsStock.getGood();
		for (Good good : goodList) {
			List<Stock> stockList = good.getStocks().getStock();
			for (Stock stock : stockList) {
				size = stock.getSize();
				if(size.indexOf("½")>0){
					size=size.replace("½","+");
				}
				returnMap.put(good.getID()+"-"+size, stock.getQty());
			}
		}
		return returnMap;
	}
	
	
	public static void main(String[] args) {
		DataTransUtil.getGoodsStockList();
	}
}

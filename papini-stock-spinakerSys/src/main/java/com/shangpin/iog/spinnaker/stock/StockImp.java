package com.shangpin.iog.spinnaker.stock;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.spinnaker.stock.dto.ResponseDTO;
import com.shangpin.iog.spinnaker.stock.dto.Seasoncode;
import com.shangpin.iog.spinnaker.stock.dto.SeasoncodeList;
import com.shangpin.iog.spinnaker.stock.dto.Stock;
import com.shangpin.iog.spinnaker.stock.schedule.AppContext;

@Component("monti")
public class StockImp extends AbsUpdateProductStock {
	
	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");

	private static Gson gson = new Gson();

	private static ResourceBundle bdl = null;
	private static String api_url = "";
	private static String api_key = "";

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		api_url = bdl.getString("api_url");
		api_key = bdl.getString("api_key");
	}
	
	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		Map<String, String> stock_map = new HashMap<String, String>();
		Map<String, String> map = getAllStock();
		for (String skuno : skuNo) {
			if (map.containsKey(skuno))
				stock_map.put(skuno, map.get(skuno));
			else {
				stock_map.put(skuno, "0");
			}
		}
		return stock_map;
	}
	/**
	 * 抓取供应商的库存
	 * @return 供应商sku编号和供应商库存对应关系map集合
	 * @throws UnsupportedEncodingException
	 */
	private Map<String, String> getAllStock() throws UnsupportedEncodingException{
		Map<String, String> map = new HashMap<String, String>();
		OutTimeConfig outTimeConfig = new OutTimeConfig(120000, 120000, 120000);
		String season_url = api_url+"/Myapi/Productslist/GetAllSeasonCode?DBContext=Default&key="+api_key;
		logger.info("获取季节码url："+season_url); 
		String season_json = HttpUtil45.get(season_url,outTimeConfig, null);
		logger.info("获取的季节信息："+season_json); 
		int i = 0;
		while(HttpUtil45.errorResult.equals(season_json) && i<100){
			season_json = HttpUtil45.get(season_url,outTimeConfig, null);
			i++;
		}
		if ((HttpUtil45.errorResult.equals(season_json))) {
			logger.info("获取季节码失败，抓取库存失败。已经尝试了"+i+"遍"); 
			loggerError.error("获取季节码失败，抓取库存失败。已经尝试了"+i+"遍"); 
			return null;
		}
		SeasoncodeList season_list = gson.fromJson(season_json, SeasoncodeList.class);
		for (Seasoncode obj : season_list.getSeasonCode()) {
			String code = URLEncoder.encode(obj.getSeasonCode(), "UTF-8");
			String url = api_url+"/Myapi/Productslist/GetAllStockForSync?DBContext=Default&SeasonCode="
					+ code + "&typeSync=web&key="+api_key;
			logger.info("获取库存url："+url); 
			String stockData = HttpUtil45.get(url,outTimeConfig, null);
			if (HttpUtil45.errorResult.equals(stockData)) {
				logger.info("季节码："+obj.getSeasonCode()+"获取库存失败。"); 
				loggerError.error("季节码："+obj.getSeasonCode()+"获取库存失败。"); 
				continue;
			}
			ResponseDTO res = gson.fromJson(stockData,ResponseDTO.class);
			if (res != null) {
				List<Stock> list = res.getListStockData();
				for (Stock stock : list) {
					if ((stock != null) && (stock.getBarcode() != null)) {
						map.put(stock.getBarcode(), stock.getQty());
					}
				}
			}
		}
		logger.info("抓取的供应商的库存map集合大小是："+map.size()); 
		return map;
	}
    
    @SuppressWarnings("unused")
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    public static void main(String[] args) throws Exception {
        loadSpringContext();      
    }
//    @Override
//    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
//        Map<String, String> stock_map = new HashMap<String, String>();
//        Gson gson = new Gson();
//        String barcode="" ,url="",json="";
//        OutTimeConfig outTimeConfig = new OutTimeConfig(1000*60,1000*60,1000*60);
//        for (String skuno : skuNo) {
////            if (barcode_map.containsKey(skuno)) {
////                continue;
////            } else {
////                barcode_map.put(skuno, null);
////            }
//
//             barcode = skuno.trim();
//            //根据供应商skuno获取库存，并更新我方sop库存
//             url = "http://80.88.89.209/papiniapi/Myapi/Productslist/GetQuantityByBarcode?DBContext=Default&barcode=[[barcode]]&key=doV3IeZ6bz";
//            url = url.replaceAll("\\[\\[barcode\\]\\]", barcode);
//             json = null;
//            try {
//                json = HttpUtil45.get(url, outTimeConfig, null);
//            } catch (Exception e) {
//                stock_map.put(skuno, "0");  //读取失败的时候赋值为0
//                logError.error("拉取失败 "+e.getMessage());
//                e.printStackTrace();
//                continue;
//            }
//            if (json != null && !json.isEmpty()) {
//
//                if(json.equals("{\"Result\":\"No Record Found\"}")) {    //未找到
//
//                            stock_map.put(skuno, "0");
//
//
//                }else{//找到赋值
//
//                    try {
//                        Quantity result = gson.fromJson(json, new TypeToken<Quantity>() {
//                        }.getType());
//                        stock_map.put(skuno, result.getResult());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//
//            }
//        }
//
//        return stock_map;
//    }
}
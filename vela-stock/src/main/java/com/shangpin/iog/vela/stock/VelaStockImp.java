package com.shangpin.iog.vela.stock;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.vela.stock.dto.ResponseDTO;
import com.shangpin.iog.vela.stock.dto.Seasoncode;
import com.shangpin.iog.vela.stock.dto.SeasoncodeList;
import com.shangpin.iog.vela.stock.dto.Stock;

@Component("velaStock")
public class VelaStockImp extends AbsUpdateProductStock {

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
		SeasoncodeList season_list = gson.fromJson(season_json, SeasoncodeList.class);
		if (season_list == null || StringUtils.isNotEmpty(season_list.getResult())) {
			logger.info("获取季节码失败，抓取库存失败。"); 
			loggerError.error("获取季节码失败，抓取库存失败。"); 
			return null;
		}
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
//    @Override
//    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
//        Map<String, String> stock_map = new HashMap<>();
//        Gson gson = new Gson();
//        int i=0;
//        Map<String,String> mongMap = new HashMap<>();
//        mongMap.put("supplierId",supplierId);
//        mongMap.put("supplierName","vela");
//        StringBuffer buffer = new StringBuffer();
//        OutTimeConfig outTimeConfig = new OutTimeConfig(1000*60,1000*60,1000*60);
//        String url="",priceUrl="",priceJson="",itemId="",sopPrice="";
//        for (String skuno : skuNo) {
//             itemId = skuno.trim();
//            //根据供应商skuno获取库存，并更新我方sop库存
//             if(itemId.length()<10){//item id
//                 url = "http://185.58.119.177/velashopapi/Myapi/Productslist/GetQuantityByItemID?DBContext=Default&ItemID=[[itemId]]&key=MPm32XJp7M";
//             }else{
//                 url = "http://185.58.119.177/velashopapi/Myapi/Productslist/GetQuantityByBarcode?DBContext=Default&barcode=[[itemId]]&key=MPm32XJp7M";
//             }
////
//
//            url = url.replaceAll("\\[\\[itemId\\]\\]", itemId);
//
////            sopPrice="";
////            priceUrl = "http://185.58.119.177/velashopapi/Myapi/Productslist/GetPriceByItemID?DBContext=Default&ItemID="+itemId+"&key=MPm32XJp7M";
//
//            String json = null;
//            try {
//                json = HttpUtil45.get(url, outTimeConfig, null);
//                //存入mongodb
////                buffer.append(json).append("|||");
//
//            } catch (Exception e) {
//                //如果 httpUtil45 发生错误 返回  {"error":"发生异常错误"}
//            	logError.error("拉取数据失败---" + e.getMessage());
//                e.printStackTrace();
//            }
//            if (json != null && !json.isEmpty()) {
//                if(json.equals("{\"error\":\"发生异常错误\"}")){
//                    //重复调用5次
//                     while(i<5){
//                         json = HttpUtil45.get(url, outTimeConfig, null);
//                         if(json.equals("{\"error\":\"发生异常错误\"}")){
//                             i++;
//                         }else{
//                             i=0;
//                             break;
//                         }
//
//                     }
//                    if(json.equals("{\"error\":\"发生异常错误\"}")){
//                        stock_map.put(skuno, "0");
//                        i=0;
//                        continue;
//                    }
//                }
//                try {
//                    Quantity result = gson.fromJson(json, new TypeToken<Quantity>() {
//                    }.getType());
//
//
//                    if("No Record Found".equals(result.getResult())){
//                        stock_map.put(skuno, "0");
//
//                    }else{
//                        stock_map.put(skuno, result.getResult()+sopPrice);
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        mongMap.put("result",buffer.toString());
//        return stock_map;
//    }
}

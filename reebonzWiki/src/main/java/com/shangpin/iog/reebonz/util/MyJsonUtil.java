package com.shangpin.iog.reebonz.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.reebonz.dto.Item;
import com.shangpin.iog.reebonz.dto.Items;
import com.shangpin.iog.reebonz.dto.ResponseObject;

/**
 * Created by Administrator on 2015/9/21.
 */
public class MyJsonUtil {
	
	private static Logger logger = Logger.getLogger("info");
    private static ResourceBundle bdl=null;
    private static String eventUrl=null;
    private static String productUrl=null;
    private static String stockUrl=null;
    static {
        if(null==bdl)
        	bdl=ResourceBundle.getBundle("conf");
        eventUrl = bdl.getString("eventUrl");
        productUrl = bdl.getString("productUrl");
        stockUrl = bdl.getString("stockUrl");
    }
    
	/**
	 * 第一步：获取活动信息
	 * */
	public static List<Item> getReebonzEventJson() {
		String json = null;
		try {
			json = HttpUtil45
					.get(eventUrl,
							new OutTimeConfig(1000 * 20, 1000 * 20, 1000 * 20),
							null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("抓取的活动列表："+json);
		return getProductList(json);
	}

	/**
	 * 第二步：根据活动获取商品信息
	 * */
	public static List<Item> getReebonzSpuJsonByEventId(String eventId,int start,int rows) {
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("event_id", eventId);
		map.put("start",String.valueOf(start));
		map.put("rows", String.valueOf(rows));
		String json = null;
		try {
			json = HttpUtil45
					.get(productUrl,
							new OutTimeConfig(1000 * 60, 1000 * 120, 1000 * 60),
							map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("抓取的活动"+eventId+"的商品列表："+json);
	
		return getProductList(json);
	}

	/**
	 * 第三步：根据活动Id和skuId获取库存以及尺码
	 * */
	public static List<Item> getSkuScokeJson(String eventId, String skuId) {

		Map<String,String> map = new HashMap<String,String>();
		map.put("event_id", eventId);
		map.put("sku", skuId);
		String json = null;
		try {
			json = HttpUtil45
					.get(stockUrl,
							new OutTimeConfig(1000 * 120, 1000 * 120, 1000 * 120),
							map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("--抓取的活动:"+eventId+"以及skuId:"+skuId+"的商品库存："+json);
		return getProductList(json);
	}

	/**
	 *  获取参加某一活动的商品总数
	 * */
	public static String getProductNum(String eventId) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("event_id", eventId);
		map.put("start","0");
		map.put("rows", "0");
		String json = null;
		try {
			json = HttpUtil45
					.get(productUrl,
							new OutTimeConfig(1000 * 600, 1000 * 60, 1000 * 600),
							map);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("抓取的活动"+eventId+"的商品总数："+json);
		return json;
	}
	
	private static List<Item> getProductList(String json) {
			
			if("{\"error\":\"发生异常错误\"}".equals(json)){
				logger.info("网络连接："+json);
				return null;
			}else{
				ResponseObject obj = new Gson().fromJson(json, ResponseObject.class);
				if("1".equals(obj.getReturn_code())){
					Object o = obj.getResponse();
					JSONObject jsonObject = JSONObject.fromObject(o); 
					Items list = new Gson().fromJson(jsonObject.toString(), Items.class);
					return list.getDocs();
				}else{
					logger.info("发生异常："+obj.getError_msg());
					return null;
				}
			}
		}
	
}

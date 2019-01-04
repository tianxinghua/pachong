package com.shangpin.iog.coccoleBimbi.service;

/**
 * Created by wang on 2015/9/21.
 */

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.iog.coccoleBimbi.dto.Item;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.product.AbsSaveProduct;

/**
 * Created by 赵根春 on 2015/9/25.
 */
@Component("coccoleBimbi")
public class FetchProduct extends AbsSaveProduct{

	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String uri;
	private static String productUrl;
	public static Gson gson = null;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		
		uri = bdl.getString("uri");
		productUrl = bdl.getString("productUrl");
		gson = new Gson();
	}
	static int i=0;
	
	public  void fetchProduct(){
		
		try{
			String json = HttpUtil45.get(uri+productUrl,new OutTimeConfig(1000 * 60 *50, 1000 * 60*50, 1000 * 60*50),null);
			if("{\"error\":\"发生异常错误\"}".equals(json)){
				System.out.println(json);
				return ;
			}
			List<Item> list = gson.fromJson(json,new TypeToken<List<Item>>() {
			}.getType());
			if(list!=null&&!list.isEmpty()){
				 for (Item item : list) {
				    	try{
					        supp.setData(gson.toJson(item));
							pushMessage(gson.toJson(item));
				    	}catch(Exception e){
				    		i++;
				    	}
				    }
			}
		    System.out.println("错误的个数："+i);
		}catch(Exception e){
			logger.info("拉去异常"+e.getMessage());
			System.out.println(e.getMessage());
		}
	}

	@Override
	public Map<String, Object> fetchProductAndSave() {
		return null;
	}
	
}

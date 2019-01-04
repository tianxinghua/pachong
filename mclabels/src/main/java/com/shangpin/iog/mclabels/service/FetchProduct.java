package com.shangpin.iog.mclabels.service;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.mclabels.dao.Item;
import com.shangpin.iog.mclabels.dao.Items;
import com.shangpin.product.AbsSaveProduct;

/**
 * Created by 赵根春 on 2015/9/25.
 */
@Component("mclabels")
public class FetchProduct extends AbsSaveProduct{

	private static Logger logger = Logger.getLogger("info");
	private static String supplierId = "2016111101957";
	private static String url = null;
	public static int day = 90;
	public static int max;
	public static Gson gson = null;
	private static ResourceBundle bdl = null;
	static {
		gson = new Gson();
		if (null == bdl){
			bdl = ResourceBundle.getBundle("conf");
		}
		url = bdl.getString("url");
	}
	static int i=0;
	public void save(){
		handleData("spu", supplierId, day, null);
	}
	public  List<Item>   getProductList(){
		String json = HttpUtil45
				.get(url,new OutTimeConfig(1000 * 60 *20, 1000 * 60*20, 1000 * 60*20),null);
		Items obj = null;
		try{
			obj = gson.fromJson(json,new TypeToken<Items>(){}.getType());
		}catch(Exception e){
			System.out.println(e.getMessage());
			i++;
			if(i!=5){
				getProductList();
			}

		}
		return obj.getResults();
	}
	public  Map<String, Object> fetchProductAndSave() {
		List<Item> list = getProductList();
		logger.info("总的商品数量：" + list.size());
		if(list!=null&&list.size()>0){
			for (Item item : list) {
				supp.setData(gson.toJson(item));
				pushMessage(null);
			}
		}
		return null;
	}
}

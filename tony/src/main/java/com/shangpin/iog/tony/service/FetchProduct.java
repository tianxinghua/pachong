package com.shangpin.iog.tony.service;

/**
 * Created by wang on 2015/9/21.
 */

import java.util.ArrayList;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.tony.common.Constant;
import com.shangpin.iog.tony.common.MyJsonClient;
import com.shangpin.iog.tony.common.StringUtil;
import com.shangpin.iog.tony.dto.Items;
import com.shangpin.iog.tony.dto.ReturnObject;
import com.shangpin.iog.tony.dto.Spu;
import com.shangpin.product.AbsSaveProduct;

/**
 * Created by wangyuzhi on 2015/9/10.
 */
@Component("tony")
public class FetchProduct extends AbsSaveProduct{

    private static Logger log = Logger.getLogger("info");
    @Autowired
    ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;
    private MyJsonClient jsonClient = new MyJsonClient();
    private String itemsJson;
    private String categoriesJson;

    private static Gson gson = new Gson();
    
    public void sendAndSaveProduct(){
		handleData("spu", Constant.SUPPLIER_ID, Constant.day, null);
	}
	
	/**
	 * message mapping and save into DB
	 */
	public Map<String,Object> fetchProductAndSave() {
	
        //获取数据
        itemsJson =       jsonClient.httpPostOfJson(Constant.ITEMS_INPUT, Constant.ITEMS_URL);
        System.out.println(itemsJson);
        categoriesJson = jsonClient.httpPostOfJson(Constant.CATEGORIES_INPUT, Constant.CATEGORIES_URL);
        this.getStock(itemsJson);
		return null;
    }

    private  void getStock(String itemsJson){
    	 Map<String,String> categoryMap = StringUtil.getCategoryMap(categoriesJson);
    	 System.out.println(itemsJson);
        JsonObject jsonObject = (JsonObject) new JsonParser().parse(itemsJson);
        System.out.println(jsonObject);
        ReturnObject obj = new Gson().fromJson(itemsJson, ReturnObject.class);
        if(obj!=null){
            String next = obj.getNext_step();
            Spu data = obj.getData();
            if(data!=null){
                Items[] array = data.getInventory();
                if(array!=null){
                    for(Items item:array){
                    	item.setDesc(item.getDesc().replace("<br>",""));
                    	item.setDesc_en(item.getDesc_en().replace("<br>",""));
                         String name = StringUtil.getCategoryNameByID((ArrayList)item.getCat_ids(), categoryMap);
                         item.setCat_ids(name);
                         if(item.getSku_parent().contains("MWL010E00139_100")){
                        	 System.out.println("s");
                         }
                       //TODO 发送消息队列
 						supp.setData(gson.toJson(item));
 						String json = gson.toJson(supp);
 						log.info(json); 
						pushMessage(json);
                    }
                }
                if(next!=null){
                    getStock(jsonClient.httpPostOfJson("{\"merchantId\":\"55f707f6b49dbbe14ec6354d\",\"token\":\"d355cd8701b2ebc54d6c8811e03a3229\",\"step\":\""+next+"\"}", Constant.ITEMS_URL));
                }
            }
        }
    }

}

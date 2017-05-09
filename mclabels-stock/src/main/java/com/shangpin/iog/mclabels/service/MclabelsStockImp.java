package com.shangpin.iog.mclabels.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.mclabels.dao.DistributionCenterInfoSubmit;
import com.shangpin.iog.mclabels.dao.Item;

/**
 * Created by huxia on 2015/7/17.
 */
@Component("velaStock")
public class MclabelsStockImp extends AbsUpdateProductStock {


    
    public  Map<String,String> getProductList(){
    	Map<String,String> map = new HashMap<>();
		String url = "http://nodo.azurewebsites.net/api/channel/export/?channelName=Shangpin&token=bearer%20eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InNlcnZpY2VzQHNoYW5ncGluLmNvbSIsInBhc3N3b3JkIjoiJDJhJDA4JEdrcDZENmdYVkQxVmtOYWdGandrak9yRGRQekcvdjBuN3ZTUzFScEhKYnNyNHZXY0g2OTNPIiwiaWQiOjEzLCJpYXQiOjE0ODA2OTgxMTF9.7kRekC3N7xe0TjuFGbbpK93Kv4ry7eyrS897qmPvBIc";
		String json = HttpUtil45
				.get(url,new OutTimeConfig(1000 * 60 *2, 1000 * 60*2, 1000 * 60*2),null);
		Gson gson = new Gson();
		try{
			List<Item> obj = gson.fromJson(json,new TypeToken<List<Item>>(){}.getType());
			if(obj!=null&&obj.size()>0){
				for (Item item : obj) {
					if("false".equals(item.getVariationInfo().getIsParent())){
						DistributionCenterInfoSubmit distributionCenterInfoSubmit = item.getDistributionCenterList().getDistributionCenterInfoSubmit();
						String stock = distributionCenterInfoSubmit.getQuantity();
						if(Integer.parseInt(stock)<0){
							map.put(item.getSku(), "0");
						}else{
							map.put(item.getSku(), stock);
						}
					}
				}
				
			}
		}catch(Exception e){
			
		}
		return map;
	}

    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {

        Map<String, String> stock_map = new HashMap<>();
        Map<String,String> mongMap = getProductList();
        for (String skuno : skuNo) {
        	if(mongMap.containsKey(skuno)){
        		stock_map.put(skuno, mongMap.get(skuno));
        	}else{
        		stock_map.put(skuno, "0");
        	}
        }
        return stock_map;
    }

}
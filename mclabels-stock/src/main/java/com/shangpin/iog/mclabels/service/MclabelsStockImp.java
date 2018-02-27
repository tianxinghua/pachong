package com.shangpin.iog.mclabels.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





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
import com.shangpin.iog.mclabels.dao.Items;

/**
 * Created by huxia on 2015/7/17.
 */
@Component
public class MclabelsStockImp extends AbsUpdateProductStock {

	private static Logger logger = Logger.getLogger("info");

    public  Map<String,String> getProductList(){
    	logger.info("开始执行");
    	Map<String,String> map = new HashMap<>();
//		String url = "http://nodo.azurewebsites.net/api/channel/export/?channelName=Shangpin&token=bearer%20eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InNlcnZpY2VzQHNoYW5ncGluLmNvbSIsInBhc3N3b3JkIjoiJDJhJDA4JEdrcDZENmdYVkQxVmtOYWdGandrak9yRGRQekcvdjBuN3ZTUzFScEhKYnNyNHZXY0g2OTNPIiwiaWQiOjEzLCJpYXQiOjE0ODA2OTgxMTF9.7kRekC3N7xe0TjuFGbbpK93Kv4ry7eyrS897qmPvBIc";
		//String url = "http://nodo-stage.azurewebsites.net/api/channel/export/?channelName=Shangpin&token=bearer%20eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InNlcnZpY2VzQHNoYW5ncGluLmNvbSIsInBhc3N3b3JkIjoiJDJhJDA4JEdrcDZENmdYVkQxVmtOYWdGandrak9yRGRQekcvdjBuN3ZTUzFScEhKYnNyNHZXY0g2OTNPIiwiaWQiOjEzLCJpYXQiOjE0ODA2OTgxMTF9.7kRekC3N7xe0TjuFGbbpK93Kv4ry7eyrS897qmPvBIc";
        String url = "http://nodo.azurewebsites.net/api/channel/export/?channelName=Shangpin&token=bearer%20eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InNlcnZpY2VzQHNoYW5ncGluLmNvbSIsInBhc3N3b3JkIjoiJDJhJDA4JEdrcDZENmdYVkQxVmtOYWdGandrak9yRGRQekcvdjBuN3ZTUzFScEhKYnNyNHZXY0g2OTNPIiwiaWQiOjEzLCJpYXQiOjE0ODA2OTgxMTF9.7kRekC3N7xe0TjuFGbbpK93Kv4ry7eyrS897qmPvBIc";
		String json =  null;
		try{
			json = HttpUtil45
					.get(url,new OutTimeConfig(1000 * 60 *2, 1000 * 60*60, 1000 * 60*60),null);	
			logger.info("执行结束");
		}catch(Exception e){
			logger.info("拉取保存："+e.getMessage(),e);
		}
		
		Gson gson = new Gson();
		try{
			Items items = gson.fromJson(json,new TypeToken<Items>(){}.getType());
			List<Item> obj  = items.getResults();
			if(items!=null&&obj!=null&&obj.size()>0){
				logger.info("size:"+obj.size());
				for (Item item : obj) {
					
					try{
						if("false".equals(item.getVariationInfo().getIsParent())){
							DistributionCenterInfoSubmit distributionCenterInfoSubmit = item.getDistributionCenterList().getDistributionCenterInfoSubmit();
							String stock = distributionCenterInfoSubmit.getQuantity();
							if(Integer.parseInt(stock)<0){
								map.put(item.getSku(), "0");
							}else{
								map.put(item.getSku(), stock);
							}
						}	
					}catch(Exception e){
						logger.info("sku:"+item.getSku()+"解析出错");
						map.put(item.getSku(), "0");
					}
					
				}
				
			}
		}catch(Exception e){
			logger.info(e.getMessage());
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
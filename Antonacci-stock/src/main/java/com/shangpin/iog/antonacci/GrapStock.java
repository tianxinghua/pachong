package com.shangpin.iog.antonacci;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.antonacci.schedule.AppContext;
import com.shangpin.iog.antonacci.schedule.Product;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

@Component("antonacci")
public class GrapStock extends AbsUpdateProductStock{

	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000 * 60*20,
			1000 * 60 * 30, 1000 * 60 * 30);

	private static ResourceBundle bdl = null;
	private static String uri;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		uri = bdl.getString("uri");
	}
	
	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {

		Map<String, Integer> supplierStock = new HashMap<>();
		Map<String, String> skustock = new HashMap<>();
		Gson gson = new Gson();
		String result = HttpUtil45.get(uri,outTimeConf, null);
		List<Product> productList = gson.fromJson(result,
				new TypeToken<List<Product>>() {
				}.getType());
		
		if(productList!=null&&productList.size()>0){
			for(Product product:productList){
				supplierStock.put(product.getSkuId(),product.getStock());
			}
			
			for (String skuno : skuNo) {
				
				if(supplierStock.containsKey(skuno.replace(".5", "½"))){
					int stock = supplierStock.get(skuno.replace(".5", "½"));
					if(stock<0){
						skustock.put(skuno,"0");	
					}else{
						skustock.put(skuno, stock+"");
					}
						
				}else{
					skustock.put(skuno, "0");
				}
	        }
		}
		
		return skustock;
	}
	
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		loadSpringContext();
	}
}

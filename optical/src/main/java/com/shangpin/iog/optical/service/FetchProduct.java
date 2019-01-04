package com.shangpin.iog.optical.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.product.AbsSaveProduct;

/**
 * Created by usr on 2015/9/14.
 */

@Component("optical")
public class FetchProduct extends AbsSaveProduct{

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	private static  Gson gson = new Gson();

	public Map<String, Object> fetchProductAndSave() {

		try{
			String xml = HttpUtil45.get("http://www.opticalscribe.com/spfiles/spfilexls.xml",new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30), null);
			List<List<String>> list = Utils.getListProduct(xml);
			logger.info("抓取供应商的产品list========"+list.size()); 
			for(List<String> item : list){
				try {
					supp.setData(gson.toJson(item));
					pushMessage(null);
				} catch (Exception e) {
					e.printStackTrace();
					loggerError.error(e);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			loggerError.error(ex);
		}
		return null;
	}

}


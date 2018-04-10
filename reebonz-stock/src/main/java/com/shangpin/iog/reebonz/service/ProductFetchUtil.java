package com.shangpin.iog.reebonz.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

import com.shangpin.iog.reebonz.dto.StockQuery;
import com.shangpin.iog.reebonz.dto.StockResult;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by wangchao on 2017/10/26.
 */
@Component
public class ProductFetchUtil {
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String stockUrl = "",supplierId="";
	static {
		if (null == bdl){
			bdl = ResourceBundle.getBundle("conf");
		}

		stockUrl = bdl.getString("stockUrl");
		supplierId = bdl.getString("supplierId");
	}
	ObjectMapper mapper = new ObjectMapper();
	OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);



	public  Map<String,String> getProductStock(Map<String,String> stockMap) {

		String result = null;
		int page=0,pageSize = 100;
		boolean loop= true;
		String request ="";
		List<StockResult> stockList = null;
		while(loop){
			page++;
			StockQuery query = new StockQuery();
			query.setPageIndex(page);
			query.setPageSize(pageSize);
			query.setSupplierId(supplierId);
			try {
				request = mapper.writeValueAsString(query);
				result = HttpUtil45.operateData("post","json",stockUrl,timeConfig,null,request,"","");
				stockList = 	mapper.readValue(result, new TypeReference<List<StockResult>>(){} );
				if(null==stockList||stockList.size()<100){
					loop = false;
				}
				if(null!=stockList&&stockList.size()>0){
                     for(StockResult stockResult:stockList){
                     	stockMap.put(stockResult.getSupplierSkuNo(),String.valueOf(stockResult.getStock()));
					 }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}




		return stockMap;
	}


}

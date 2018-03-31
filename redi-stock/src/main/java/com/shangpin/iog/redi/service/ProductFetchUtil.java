package com.shangpin.iog.redi.service;


import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.iog.redi.dto.Sku;
import com.shangpin.iog.redi.dto.Spu;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.util.StringUtils;

/**
 * Created by wangchao on 2017/10/26.
 */
@Component
public class ProductFetchUtil {
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String stockUrl = "";
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		stockUrl = bdl.getString("stockUrl");
	}
	ObjectMapper mapper = new ObjectMapper();

	public boolean  setProductStock(String spuNo,Map<String, String> supplierSkuStcokMap) throws Exception {
		boolean handle = true;
		List<Spu> spuList = this.getProduct(spuNo);
		if(null!=spuList&&spuList.size()>0){
			for(Spu spu:spuList){
				List<Sku> skuList = spu.getItem_sizes();
				if(null!=skuList&&skuList.size()>0){
					for(Sku sku:skuList){
						supplierSkuStcokMap.put(sku.getItem_size_id(),sku.getItem_stock());
					}
				}
			}
		}else{
             handle  = false;
		}
		return  handle;
	}


	private List<Spu> getProduct(String spuNo) {
		OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);
		String result = HttpUtil45.get(stockUrl + spuNo, timeConfig, null);
		List<Spu> spuLis = null;
		if(!StringUtils.isEmpty(result)){
			if(HttpUtil45.errorResult.equals(result)){
				//链接错误
			}else{
				try {
					spuLis = 	mapper.readValue(result, new TypeReference<List<Spu>>(){} );

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


		return spuLis;
	}

	public static void main(String[] args) {


	}
}

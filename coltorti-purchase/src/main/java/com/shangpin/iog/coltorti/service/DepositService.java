package com.shangpin.iog.coltorti.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.coltorti.conf.ApiURL;
import com.shangpin.iog.coltorti.dto.ColtortiStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

public class DepositService {
	static Logger logger =LoggerFactory.getLogger(DepositService.class);
	public static String getDespositId(String skuId) throws ServiceException{
		Map<String,String> param=ColtortiUtil.getCommonParam(0,0);
		if(skuId!=null) param.put("id", skuId);
		String body=HttpUtil45.get(ColtortiUtil.paramGetUrl(ApiURL.STOCK,param),new OutTimeConfig(10000,10000,10000),null);
		ColtortiUtil.check(body);
		Gson gson = new Gson();
		Map<String,List<ColtortiStock>> mp=null;
		try{
			mp=gson.fromJson(body, new TypeToken<Map<String,List<ColtortiStock>>>(){}.getType());
		}catch(Exception e){
			logger.error("http请求结果转换库存失败，skuId:{}返回数据{}",skuId,body);
		}
		
		return mp.toString();
	}
	public static void main(String[] args) {
		try {
			String despositId = new DepositService().getDespositId("151431DAB000003-303");
			System.out.println(despositId);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
}

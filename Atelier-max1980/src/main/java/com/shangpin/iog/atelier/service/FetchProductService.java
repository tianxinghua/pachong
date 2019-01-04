package com.shangpin.iog.atelier.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.atelier.dto.AtelierDTO;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.product.AbsSaveProduct;

@Component("Atelier-MAX1980")
public  class FetchProductService extends AbsSaveProduct{
	
	@Autowired
	SendProductService sendProductService;
	@Autowired
	SaveProductService saveProductService;

	final Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String api_url = "";
	private static String spu_interface = "";
	private static String sku_interface = "";
	private static String image_interface = "";
	private static String price_interface = "";
	private static String userName = "";
	private static String password = "";
	private static String supplierId = "";
	private static int day;
	private static Gson gson;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		api_url = bdl.getString("api_url");
		spu_interface = bdl.getString("spu_interface");
		sku_interface = bdl.getString("sku_interface");
		image_interface = bdl.getString("image_interface");
		price_interface = bdl.getString("price_interface");
		userName = bdl.getString("userName");
		password = bdl.getString("password");
		if(StringUtils.isNotBlank(bdl.getString("flag"))){
			flag = Boolean.parseBoolean(bdl.getString("flag"));
		}
		gson = new Gson();
	}
	public void sendAndSaveProduct(){
		handleData("spu", supplierId, day, null);
	}
	
	@Override
	public Map<String, Object>  fetchProductAndSave(){
	
		
		String skuData = fetchSkuData();
		String spuData = fetchSpuData();
		String imageData = fetchImagData();
		String priceData = fetchPriceData();
		pushMessage(spuData,skuData,imageData,priceData);
		Map<String, Object> returnMap = null;
		if(flag){
			returnMap = saveProductService.fetchProductAndSave(spuData, skuData, imageData, priceData);
		}
		return returnMap;
	}
	public void pushMessage(String spu,String sku,String imag,String price) {
		
		try{
			//具体的业务
			
			Map<String,List<String>> skuMap =  sendProductService.handleSkuData(sku);
			Map<String,List<String>> imgMap = sendProductService.handleImagData(imag);
			Map<String,String> spuMap =  sendProductService.handleSpuData(spu);
			Map<String,String> priceMap = sendProductService.handlePriceData(price);
			
			for (Map.Entry<String, String> entry : spuMap.entrySet()) {
				
				AtelierDTO dto = new AtelierDTO();
				dto.setSpu(entry.getValue());
				String spuId = entry.getKey();
				dto.setSpuId(spuId);
				dto.setSku(skuMap.get(spuId));
				if(imgMap.get(spuId)==null){
					dto.setImage(new ArrayList<String>());	
				}else{
					dto.setImage(imgMap.get(spuId));
				}
				dto.setPrice(priceMap.get(spuId));
				supp.setData(gson.toJson(dto));
				pushMessage(gson.toJson(supp));
			}
			
		}catch(Exception ex){
			
		}
	}
	
	/**
	 * 下载接口文件并返回数据
	 * 
	 * @param api_interface
	 * @param outTimeConfig
	 * @return
	 */
	public String getInterfaceData(String api_interface,
			OutTimeConfig outTimeConfig) {
		logger.info("开始拉取" + api_interface + "的数据......");
		String data = HttpUtil45.postAuth(api_url + api_interface, null,
				outTimeConfig, userName, password);
		int i = 0;
		while ((StringUtils.isBlank(data) || HttpUtil45.errorResult
				.equals(data)) && i < 10) {
			try {
				Thread.sleep(1000 * 3);
				data = HttpUtil45.postAuth(api_url + api_interface, null,
						outTimeConfig, userName, password);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				i++;
			}
		}
		logger.info("拉取" + api_interface + "用了==" + i + "次");
		return data;
	}

	public String fetchSpuData() {
		String spuData = getInterfaceData(spu_interface, new OutTimeConfig(
				1000 * 60 * 90, 1000 * 60 * 60, 1000 * 60 * 90));
		if (HttpUtil45.errorResult.equals(spuData)) {
			return null;
		}
		return spuData;
	}

	public String fetchSkuData() {
		String skuData = getInterfaceData(sku_interface, new OutTimeConfig(
				1000 * 60 * 60, 1000 * 60 * 5, 1000 * 60 * 60));
		if (HttpUtil45.errorResult.equals(skuData)) {
			return null;
		}
		return skuData;
	}

	public String fetchImagData() {
		String imageData = getInterfaceData(image_interface, new OutTimeConfig(
				1000 * 60 * 60, 1000 * 60 * 5, 1000 * 60 * 60));
		if (HttpUtil45.errorResult.equals(imageData)) {
			return null;
		}
		return imageData;
	}

	public String fetchPriceData() {
		String priceData = getInterfaceData(price_interface, new OutTimeConfig(
				1000 * 60 * 60, 1000 * 60 * 5, 1000 * 60 * 60));
		if (HttpUtil45.errorResult.equals(priceData)) {
			return null;
		}
		return priceData;
	}

}

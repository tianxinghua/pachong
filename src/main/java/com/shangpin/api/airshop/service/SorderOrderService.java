package com.shangpin.api.airshop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.dto.Product;
import com.shangpin.api.airshop.dto.PurAccountContent;
import com.shangpin.api.airshop.dto.PurAccountRQ;
import com.shangpin.api.airshop.dto.PurAccountRS;
import com.shangpin.api.airshop.dto.base.ApiContent;
import com.shangpin.api.airshop.dto.base.ApiContentOne;
import com.shangpin.api.airshop.service.base.BaseService;
import com.shangpin.api.airshop.supplier.service.ProductService;
import com.shangpin.api.airshop.supplier.service.SupplierService;
import com.shangpin.api.airshop.util.Constants;
import com.shangpin.api.airshop.util.DateFormat;
import com.shangpin.common.utils.FastJsonUtil;

/**
 * Date: 2016年1月12日 <br/>
 * 
 * @author wanghua
 * @since JDK 7
 */
@Service
public class SorderOrderService extends BaseService {
	private static Logger logger = LoggerFactory.getLogger(SorderOrderService.class);
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private ProductService productService;
	@Autowired
	private  RestTemplate restTemplate;
	 
	public JSONObject statementList(String sopUserNo,String orderCode,String startTime,String endTime,String pageIndex,String pageSize){
		if (startTime==null||endTime==null) {
			startTime="";
			endTime="";
		}
		if (startTime.equals("")||endTime.equals("")) {
			startTime="";
			endTime="";
		}else {
			startTime=DateFormat.TimeFormatChangeToString(startTime, "dd-MM-yyyy", "yyyy-MM-dd");
			endTime=DateFormat.TimeFormatChangeToString(endTime, "dd-MM-yyyy", "yyyy-MM-dd");
		}
		JSONObject param = new JSONObject();
		param.put("sopUserNo", sopUserNo);
		param.put("pageIndex", pageIndex);
		param.put("pageSize", pageSize);
		param.put("sopPurchaseOrderNo", orderCode);
		param.put("updateTimeBegin", startTime);
		param.put("updateTimeEnd", endTime);
		
		String result= restTemplate.postForObject(ApiServiceUrlConfig.getSorderOrderListUri(),getHttpPostData(param.toJSONString()), String.class);
		JSONObject jsonResult= JSONObject.parseObject(result);
		if (jsonResult.getBoolean("isSuccess")&&jsonResult.getJSONObject("messageRes")!=null) {
			//组合数据
			JSONArray resultList = new JSONArray();
			JSONArray tempList =jsonResult.getJSONObject("messageRes").getJSONArray("purchaseOrderDetails");
			
			if (tempList.size()>0) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < tempList.size(); i++) {
					sb.append( tempList.getJSONObject(i).getString("supplierSkuNo")   +",");
					JSONObject item = new JSONObject();
					item.put("brandName", tempList.getJSONObject(i).getString("brandName") );
					item.put("sopPurchaseOrderNo", tempList.getJSONObject(i).getString("sopPurchaseOrderNo") );
					item.put("supplierSkuNo", tempList.getJSONObject(i).getString("supplierSkuNo") );
					item.put("productName", "" );
					item.put("color", "");
					item.put("size", "" );
					item.put("qty", tempList.getJSONObject(i).getString("qty") );
					item.put("skuPriceCurrency", tempList.getJSONObject(i).getString("currencyName") );
					item.put("skuPrice", tempList.getJSONObject(i).getString("skuPrice") );
					resultList.add(item);
				}
				String productParamResult= getProductProperty(sopUserNo,sb.substring(0,sb.length()-1));
				
				//组合数据
				getSkuStockJoin(resultList,JSONObject.parseArray(productParamResult));
			}
			
			return JSONObject.parseObject("{code:\"0\",msg:\"\",content:{total:\""+ jsonResult.getJSONObject("messageRes").getString("total")
					+"\", purchaseOrderDetails:"+resultList.toJSONString()+"}}");
			
		}else {
			return JSONObject.parseObject("{code:\"0\",msg:\"no data\",content:{total:\"0\",purchaseOrderDetails:[]}}");
		}
		
	} 
	
	/**两个结果集数据组合
	 * @param skuResult1
	 * @param skuResult2
	 */
	private void getSkuStockJoin(JSONArray skuResult1,JSONArray skuResult2){
		if (skuResult2.size()>0&&skuResult2.size()>0) {
			for (int i = 0; i < skuResult1.size(); i++) {
				for (int j = 0; j < skuResult2.size(); j++) {
					if (skuResult1.getJSONObject(i).getString("supplierSkuNo")!=null&&skuResult1.getJSONObject(i).getString("supplierSkuNo").equals(skuResult2.getJSONObject(j).getString("skuId")) ) {
						if (skuResult2.getJSONObject(j).getString("color")==null) {
							skuResult1.getJSONObject(i).put("color", "");
						}else {
							skuResult1.getJSONObject(i).put("color", skuResult2.getJSONObject(j).getString("color"));
						}
						if (skuResult2.getJSONObject(j).getString("size")==null) {
							skuResult1.getJSONObject(i).put("size", "");
						}else {
							skuResult1.getJSONObject(i).put("size", skuResult2.getJSONObject(j).getString("size"));
						}
						if (skuResult2.getJSONObject(j).getString("productName")==null) {
							skuResult1.getJSONObject(i).put("productName", "");
						}else {
							skuResult1.getJSONObject(i).put("productName", skuResult2.getJSONObject(j).getString("productName"));
						}
						
					}
				}
			}
		}
	}
	//包装Http请求参数
	private HttpEntity<String> getHttpPostData(String param){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<String> paramEntity  = new HttpEntity<String>("=" +param,headers);
		return paramEntity;
	}
	//获取商品 颜色尺码
	private String getProductProperty(String supplierNo, String supplierSkuNo) {
		return productService.product(supplierNo, supplierSkuNo);
		/*String url = ApiServiceUrlConfig.getProductUri() + "/" + supplierNo + "/" + supplierSkuNo;
		return restTemplate.getForObject(url, String.class);*/
	}
	
	
	/***
     * 结算单列表
     * @param purAccountRQ
     * @return
     */
	public PurAccountRS sorderOrderList(PurAccountRQ purAccountRQ) {
		purAccountRQ.converDate();
		String json = supplierService.sorderOrderList(purAccountRQ);
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		JSONObject jsonObject = JSON.parseObject(json);
		String code = String.valueOf(jsonObject.get("ResCode"));
		if (!Constants.SUCCESS.equals(code)) {
			return null;
		}
		ApiContentOne<PurAccountRS> requestContent = FastJsonUtil.fromJson(json,
				new TypeReference<ApiContentOne<PurAccountRS>>() {
				});
		if (Constants.SUCCESS.equals(requestContent.getResCode())) {
			PurAccountRS pRs = requestContent.getMessageRes();
			List<PurAccountContent> pList = pRs.getPurchaseOrderDetails();
			if (null == pList || pList.size() == 0) {
				return null;
			}
			String skus = "";
			for (PurAccountContent pContent : pList) {
				String sku = pContent.getSupplierSkuNo();
				skus += sku + ",";
			}
			List<Product> products = productService.list(purAccountRQ.getSopUserNo(), skus);
			for (PurAccountContent detail : pList) {
				if (null == products) {
					detail.setProductName("");
					detail.setSize("");
					detail.setColor("");
				} else {
					for (Product product : products) {
						if (detail.getSupplierSkuNo().equals(product.getSkuId())) {
							detail.setProductName(product.getProductName());
							detail.setSize(product.getSize());
							detail.setColor(product.getColor());
						}
					}
				}
				detail.setSkuPriceCurrency(PurAccountContent.converSkuPriceCurrency(detail.getSkuPriceCurrency()));
			}
			return pRs;
		}
		return null;
	}

	/***
	 * 供应商已完结采购单导出
	 * 
	 * @param purAccountRQ
	 * @return
	 */
	public List<HashMap<String, Object>> findSorderExport(PurAccountRQ purAccountRQ) {
		purAccountRQ.converDate();
		List<HashMap<String, Object>> result2 = new ArrayList<HashMap<String, Object>>();
		String json = supplierService.findSorderExport(purAccountRQ);
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		ApiContent<PurAccountContent> requestContent = FastJsonUtil.fromJson(json,
				new TypeReference<ApiContent<PurAccountContent>>() {
				});
		if (Constants.SUCCESS.equals(requestContent.getResCode())&& requestContent.getMessageRes().size()>0) {
			ApiContent<PurAccountContent> rContent = DataLogic(purAccountRQ.getSopUserNo(), requestContent);
			List<PurAccountContent> pList = rContent.getMessageRes();
			for (int i = 0; i < pList.size(); i++) {
				HashMap<String, Object> result = new HashMap<String, Object>();
				result.put("SopPurchaseOrderNo", pList.get(i).getSopPurchaseOrderNo());
				result.put("BrandName", pList.get(i).getBrandName());
				result.put("SupplierSkuNo", pList.get(i).getSupplierSkuNo());
				result.put("itemName",
						pList.get(i).getProductName() + pList.get(i).getColor() + pList.get(i).getSize());
				result.put("qty", pList.get(i).getQty());
				result.put("currency", pList.get(i).getSkuPriceCurrency());
				result.put("amount", pList.get(i).getSkuPriceCurrency());
				result2.add(result);
			}
			return result2;
		}
		return null;
	}
	
	/**
	 * 处理Api返回的数据
	 * 
	 * @param json
	 * @param purAccountRQ
	 * @return
	 */
	public ApiContent<PurAccountContent> DataLogic(String sopUserNo, ApiContent<PurAccountContent> pList) {
		String skus = "";
		for (PurAccountContent pContent : pList.getMessageRes()) {
			String sku = pContent.getSupplierSkuNo();
			skus += sku + ",";
		}
		List<Product> products = productService.list(sopUserNo, skus);
		for (PurAccountContent detail : pList.getMessageRes()) {
			if (null == products) {
				detail.setProductName("");
				detail.setSize("");
				detail.setColor("");
			} else {
				for (Product product : products) {
					if (detail.getSupplierSkuNo().equals(product.getSkuId())) {
						detail.setProductName(product.getProductName());
						detail.setSize(product.getSize());
						detail.setColor(product.getColor());
					}
				}
			}
		}
		return pList;
	}

}

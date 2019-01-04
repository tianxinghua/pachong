package com.shangpin.api.airshop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.supplier.service.ProductService;

/**SKU库存管理
 * @author tianxiuquan
 *
 */
@Service
public class StockService {

	private static Logger logger = LoggerFactory.getLogger(StockService.class);
	@Autowired
	private  RestTemplate restTemplate;
	@Autowired
	ProductService prodSrv;
	/**供应商库存分页查询
	 * @param sopUserNo
	 * @param supplierSkuNo
	 * @param productModel
	 * @param skuNo
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public JSONObject getSkuStockPageByCondition(String sopUserNo,String supplierSkuNo,String productModel,String skuNo,String brandName,int pageIndex,int pageSize){
		JSONObject param = new JSONObject();
		param.put("SopUserNo", sopUserNo);
		param.put("SupplierSkuNo", supplierSkuNo);
		param.put("ProductModel", productModel);
		param.put("SkuNo", skuNo);
		param.put("BrandName", brandName);
		param.put("PageIndex", pageIndex);
		param.put("PageSize", pageSize);
		logger.info("库存查询参数："+param.toJSONString());
		String skuStockResult= restTemplate.postForObject(ApiServiceUrlConfig.getFindStockPage(),getHttpPostData(param.toJSONString()), String.class);
		logger.info("库存返回结果："+skuStockResult);
		JSONObject jsonResult= JSONObject.parseObject(skuStockResult);
		if (jsonResult.getBoolean("IsSuccess")&&jsonResult.getJSONObject("MessageRes").getJSONArray("SkuStockList")!=null) {
			//组合数据
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < jsonResult.getJSONObject("MessageRes").getJSONArray("SkuStockList").size(); i++) {
				sb.append( jsonResult.getJSONObject("MessageRes").getJSONArray("SkuStockList").getJSONObject(i).getString("SupplierSkuNo")   +",");
				jsonResult.getJSONObject("MessageRes").getJSONArray("SkuStockList").getJSONObject(i).put("color","");
				jsonResult.getJSONObject("MessageRes").getJSONArray("SkuStockList").getJSONObject(i).put("size","");
				jsonResult.getJSONObject("MessageRes").getJSONArray("SkuStockList").getJSONObject(i).put("productName","");
			}
			//获取另一个结果集
			String productParamResult= getProductProperty(sopUserNo,sb.substring(0,sb.length()-1));//"[{skuId:\"vionnet001\",color:\"aaa\",size:\"cc\"}]";
			JSONArray productParamJSONResult=JSONObject.parseArray(productParamResult);
			//组合数据
			getSkuStockJoin(jsonResult.getJSONObject("MessageRes").getJSONArray("SkuStockList"),productParamJSONResult);
			//CategoryNameEn
			JSONObject a= JSONObject.parseObject("{code:\"0\",msg:\"\",content:{total:\""+ jsonResult.getJSONObject("MessageRes").getString("Total")+"\", list:"+jsonResult.getJSONObject("MessageRes").getJSONArray("SkuStockList").toJSONString()+"}}");
			return a;
		}else{
			jsonResult=JSONObject.parseObject("{code:\"1\",msg:\"no data\",content:{list:[]}}");
			return jsonResult;
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
					if (skuResult1.getJSONObject(i).getString("SupplierSkuNo")!=null&&skuResult1.getJSONObject(i).getString("SupplierSkuNo").equals(skuResult2.getJSONObject(j).getString("skuId")) ) {
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
	
	
	
	/**导出
	 * @param sopUserNo
	 * @param supplierSkuNo
	 * @param productModel
	 * @param skuNo
	 * @return
	 */
	public JSONObject getSkuStockPageByConditionExport(String sopUserNo,String supplierSkuNo,String productModel,String skuNo,String brandName){
		JSONObject param = new JSONObject();
		param.put("SopUserNo", sopUserNo);
		param.put("SupplierSkuNo", supplierSkuNo);
		param.put("ProductModel", productModel);
		param.put("SkuNo", skuNo);
		param.put("BrandName", brandName);
		String result= restTemplate.postForObject(ApiServiceUrlConfig.getFindStockPageExport(),getHttpPostData(param.toJSONString()), String.class);
		JSONObject jsonResult= JSONObject.parseObject(result);
		if (jsonResult.getBoolean("IsSuccess")&&jsonResult.getJSONObject("MessageRes").getJSONArray("SkuStockList")!=null) {
			//组合数据
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < jsonResult.getJSONObject("MessageRes").getJSONArray("SkuStockList").size(); i++) {
				if (i==(jsonResult.getJSONObject("MessageRes").getJSONArray("SkuStockList").size()-1)) {
					sb.append( jsonResult.getJSONObject("MessageRes").getJSONArray("SkuStockList").getJSONObject(i).getString("SupplierSkuNo"));
				}else {
					if (i%10==0&&i!=0) {
						sb.append( jsonResult.getJSONObject("MessageRes").getJSONArray("SkuStockList").getJSONObject(i).getString("SupplierSkuNo")   +"#");
					}else {
						sb.append( jsonResult.getJSONObject("MessageRes").getJSONArray("SkuStockList").getJSONObject(i).getString("SupplierSkuNo")   +",");
					}
				}
				
				jsonResult.getJSONObject("MessageRes").getJSONArray("SkuStockList").getJSONObject(i).put("color","");
				jsonResult.getJSONObject("MessageRes").getJSONArray("SkuStockList").getJSONObject(i).put("size","");
				jsonResult.getJSONObject("MessageRes").getJSONArray("SkuStockList").getJSONObject(i).put("productName","");
			}
			
			//获取另一个结果集
			String[] skuList =sb.toString().split("#");
			JSONArray productParamJSONResult=new JSONArray();
			for (int i = 0; i < skuList.length; i++) {
				String productParamResult= getProductProperty(sopUserNo,skuList[i]);
				productParamJSONResult.addAll(JSONObject.parseArray(productParamResult));
			}
			//String productParamResult= getProductProperty(sopUserNo,sb.substring(0,sb.length()-1));//"[{skuId:\"vionnet001\",color:\"aaa\",size:\"cc\"}]";
			//JSONArray productParamJSONResult=JSONObject.parseArray(productParamResult);
			
			//组合数据
			getSkuStockJoin(jsonResult.getJSONObject("MessageRes").getJSONArray("SkuStockList"),productParamJSONResult);
			
			JSONObject a= JSONObject.parseObject("{code:\"0\",msg:\"no data\",content:{list:"+jsonResult.getJSONObject("MessageRes").getJSONArray("SkuStockList").toJSONString()+"}}");
			return a;
		}else{
			jsonResult=JSONObject.parseObject("{code:\"1\",msg:\"no data\",content:{list:[]}}");
			return jsonResult;
		}
		
	}
	
	/**修改SKU库存
	 * @param sopUserNo
	 * @param sopSkuNo
	 * @param qty
	 * @return
	 */
	public JSONObject updateSkuStock(String sopUserNo,String sopSkuNo,String qty){
		JSONObject param = new JSONObject();
		param.put("SopUserNo", sopUserNo);
		param.put("SopSkuNo", sopSkuNo);
		param.put("Qty", qty);
		String result= restTemplate.postForObject(ApiServiceUrlConfig.getUpdateSkuStock(),getHttpPostData(param.toJSONString()), String.class);
		JSONObject jsonResult= JSONObject.parseObject(result);
		if (jsonResult.getBooleanValue("IsSuccess")) {
			return JSONObject.parseObject("{code:\"0\",msg:\"\",content:{}}");
		}else {
			return JSONObject.parseObject("{code:\"1\",msg:\""+jsonResult.getString("MessageRes")+"\",content:{}}");
		}
	}
	
	/**批量导入修改库存数据
	 * @param sopUserNo
	 * @param sopSkuNo
	 * @param qty
	 * @return
	 */
	public JSONObject updateSkuStockImport(JSONArray dataList){
		String result= restTemplate.postForObject(ApiServiceUrlConfig.getFindSkuStockImport(),getHttpPostData(dataList.toJSONString()), String.class);
		//JSONObject jsonResult= JSONObject.parseObject(result);
		return JSONObject.parseObject(result);
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
		return prodSrv.product(supplierNo, supplierSkuNo);
		/*String url=ApiServiceUrlConfig.getProductUri();
		Map<String,String> request = new HashMap<>();
		request.put("supplierId",supplierNo);
		request.put("skuId",supplierSkuNo);
		return restTemplate.postForObject(url, request, String.class);*/
	}
	/*
	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplate();
		String url="http://192.168.20.100:8088/iog/product/postsearch";
		Map<String,String> request = new HashMap<>();
		request.put("supplierId","222");
		request.put("skuId","333");
		String result=restTemplate.postForEntity(url, request, String.class).getBody();
		System.out.println(result);
	}*/
}

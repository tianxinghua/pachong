package com.shangpin.iog.EMonti.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.EMonti.axisclient.MagentoService;
import com.shangpin.iog.EMonti.axisclient.MagentoServiceLocator;
import com.shangpin.iog.EMonti.axisclient.PortType;
import com.shangpin.iog.EMonti.util.SoapXmlUtil;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
//import com.shangpin.iog.E-Monti.util.E-MontiUtil;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("E-Monti")
public class FetchProduct {

	private static Logger logger = Logger.getLogger("info");
	private static Logger error = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	public static int day;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
	}
	@Autowired
	private ProductFetchService productFetchService;
	@Autowired
	private ProductSearchService productSearchService;
	public void fetchProductAndSave() {
		Date startDate,endDate= new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
		//获取原有的SKU 仅仅包含价格和库存
		Map<String,SkuDTO> skuDTOMap = new HashedMap();
		try {
			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		try {
			
			MagentoService magentoService = new MagentoServiceLocator();		
			PortType portType =magentoService.getPort();
			String login = portType.login("soapws", "soap22WS!@#");
			
			//获取颜色对照表
			logger.info("===========开始获取颜色对照表=================");
			Map cilorAttribut = new HashMap();
			cilorAttribut.put("attributeId","92");			
			HashMap[] colorArrs = (HashMap[])portType.call(login,"catalog_product_attribute.options",cilorAttribut);
			
			//获取材质对照表
			logger.info("===========开始获取材质对照表================");
			Map materialAttribut = new HashMap();
			materialAttribut.put("attributeId","150");			
			HashMap[] materialArrs = (HashMap[])portType.call(login,"catalog_product_attribute.options",materialAttribut);
			
			//品牌对照表
			logger.info("===========开始获取品牌对照表================");
			Map brandAttribut = new HashMap();
			brandAttribut.put("attributeId","132");			
			HashMap[] brandArrs = (HashMap[])portType.call(login,"catalog_product_attribute.options",brandAttribut);
			
			//产地对照表
			logger.info("===========开始获取产地对照表================");
			Map originAttribut = new HashMap();
			originAttribut.put("attributeId","81");			
			HashMap[] originArrs = (HashMap[])portType.call(login,"catalog_product_attribute.options",originAttribut);
			
			//开始获取产品信息
			logger.info("===========开始获取产品信息================");
			Map<String,Object> paramatt = new HashMap<String,Object>();
			paramatt.put("setId", "4");				
			HashMap[] product_attribute = (HashMap[])portType.call(login, "product_attribute.list",paramatt);
			List filters = new ArrayList<>();
			for(HashMap map : product_attribute){
				filters.add(map.get("attribute_id"));
			}							
			@SuppressWarnings("rawtypes")
			HashMap[] objects = (HashMap[])portType.call(login, "catalog_product.list",null);
			System.out.println(objects.length); 
			for(Map map : objects){	
				try {
					System.out.println("product_id==="+map.get("product_id").toString()); 
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("product",map.get("product_id").toString());
					param.put("storeView", "");
					param.put("attributes", filters.toArray());
					param.put("productIdentifierType", "ID");
					HashMap product = (HashMap)portType.call(login, "catalog_product.info", param);
					
					//获取价格和库存
					String skuStock = "";
					Map stockMap = new HashMap();
					String[] aaa = new String[1];
					aaa[0] = map.get("sku").toString();
					stockMap.put("productIds", aaa);
					HashMap[] oo = (HashMap[])portType.call(login, "cataloginventory_stock_item.list", stockMap);
					for(HashMap stock : oo){
						skuStock  = stock.get("qty").toString();
					}
					
					//获取品类
					String gender = "";
					String[] category_ids = (String[])product.get("category_ids");
					String category = "";
					if(null != category_ids && category_ids.length>0){
						for(String catid :category_ids){
							Map categoryMap = new HashMap();
							categoryMap.put("categoryId", catid);
							HashMap categoryObj = (HashMap)portType.call(login,"catalog_category.info",categoryMap);
							if(!category.equals(categoryObj.get("name").toString())){
								category = category + " "+categoryObj.get("name").toString();
							}
							if("men".equals(categoryObj.get("name").toString().toLowerCase()) || "women".equals(categoryObj.get("name").toString().toLowerCase())){
								gender = categoryObj.get("name").toString().toLowerCase();
							}
						}
						
					}
					
					SkuDTO sku = new SkuDTO();
					sku.setId(UUIDGenerator.getUUID());
					sku.setSupplierId(supplierId);
					sku.setSkuId(product.get("sku").toString());
					sku.setSpuId(product.get("product_id").toString());
					sku.setProductName(product.get("name").toString());
					sku.setSalePrice(product.get("price").toString());
					sku.setProductCode(product.get("product_id").toString());
					for(HashMap colorMap :colorArrs){
						if(product.get("color").toString().equals(colorMap.get("value").toString())){
							sku.setColor(colorMap.get("label").toString());
							break;
						}
					}
					
					sku.setProductDescription(product.get("description").toString());
					try {
						sku.setProductSize(product.get("dimension").toString());
					} catch (Exception e) {						
					}
					
					sku.setStock(skuStock);
					if (skuDTOMap.containsKey(sku.getSkuId())) {
						skuDTOMap.remove(sku.getSkuId());
					}
					try {
						productFetchService.saveSKU(sku);
					} catch (Exception e) {
						try { 
							// 更新价格和库存
							productFetchService.updatePriceAndStock(sku);
							e.printStackTrace();
						} catch (ServiceException e1) {
							e1.printStackTrace();
						}
					}
					
					//保存图片
					try {
						List<String> picList = new ArrayList<String>();
						Map picMap = new HashMap();
						picMap.put("storeView","");
						picMap.put("identifierType", "sku");
						picMap.put("product",sku.getSkuId());
						HashMap[] picArrs = (HashMap[])portType.call(login,"catalog_product_attribute_media.list",picMap);
						for(Map picM :picArrs){
							picList.add(picM.get("url").toString());
						}
						productFetchService.savePicture(supplierId, null, sku.getSkuId(), picList);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					SpuDTO spu = new SpuDTO();
					spu.setId(UUIDGenerator.getUUID());
					spu.setSupplierId(supplierId);
					spu.setSpuId(sku.getSpuId());
					spu.setCategoryGender(gender);
					spu.setCategoryName(category);
					for(Map brandMap :brandArrs){
						if(product.get("aw_shopbybrand_brand").toString().equals(brandMap.get("value").toString())){
							spu.setBrandName(brandMap.get("label").toString());
							break;
						}
					}					
//					spu.setSeasonName(seasonName);
					for(Map materralMap : materialArrs){
						if(product.get("materiale").toString().equals(materralMap.get("value").toString())){
							spu.setMaterial(materralMap.get("label").toString());
							break;
						}
					}
					for(Map originMap :originArrs){
						if(product.get("manufacturer").toString().equals(originMap.get("value").toString())){
							spu.setProductOrigin(originMap.get("label").toString());
							break;
						}
					}					
					
					try {
						productFetchService.saveSPU(spu);
					} catch (ServiceException e) {
					   try {
							productFetchService.updateMaterial(spu);
						} catch (ServiceException e1) {
							e1.printStackTrace();
						}
					} 
					
				} catch (Exception e) {
					e.printStackTrace(); 
					error.error(e); 
				}
			} 
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error.error(e.getMessage());
		}
		
		
		
		
		//更新网站不再给信息的老数据
		for(Iterator<Map.Entry<String,SkuDTO>> itor = skuDTOMap.entrySet().iterator();itor.hasNext(); ){
			 Map.Entry<String,SkuDTO> entry =  itor.next();
			if(!"0".equals(entry.getValue().getStock())){//更新不为0的数据 使其库存为0
				entry.getValue().setStock("0");
				try {
					productFetchService.updatePriceAndStock(entry.getValue());
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		}
		
		logger.info("抓取结束");
	}
	public static void main(String[] args){
		FetchProduct f = new FetchProduct();
		f.fetchProductAndSave();
//		String sopAction = "";
//		SoapXmlUtil.downloadSoapXmlAsFile("http://wbs.servicesmp.eu:8082/pritelli/servlet/SQLDataProviderServer/stock", "", "", soapRequestData, localUrl)
	}
}


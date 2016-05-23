package com.shangpin.iog.grouppo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.axis2.client.ServiceClient;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.grouppo.axis.ProductWSServiceStub;
import com.shangpin.iog.grouppo.axis.ProductWSServiceStub.Product_TabularQueryResponse;
import com.shangpin.iog.grouppo.axis.ProductWSServiceStub.Product_TabularQueryResponseStructure;
import com.shangpin.product.AbsSaveProduct;

/**
 * Created by lubaijiang on 2015/9/14.
 */

@Component("grouppo")
public class FetchProduct extends AbsSaveProduct{

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");		
	}
	
	public Map<String, Object> fetchProductAndSave() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
		
		try{
			//具体的业务			
			//skuList.add(sku);
			//spuList.add(spu);
			/**
			List<String> list = new ArrayList<String>();
			imageMap.put(sku.getSkuId()+";"+sku.getProductCode()+" "+sku.getColor(), list);
			**/
			ProductWSServiceStub productWSServiceStub = new ProductWSServiceStub();
			productWSServiceStub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.SO_TIMEOUT,new Integer(1000*60*60));
			productWSServiceStub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CONNECTION_TIMEOUT,new Integer(1000*60*60));
        	
			ProductWSServiceStub.Product_TabularQuery product_TabularQuery2 = new ProductWSServiceStub.Product_TabularQuery();
			product_TabularQuery2.setM_UserName("shangpin");
			product_TabularQuery2.setM_Password("getDataWs16");
			product_TabularQuery2.setM_Company("PRITE");
			product_TabularQuery2.setSkuId(""); 
			Product_TabularQueryResponse response = null;
			try {
				response = productWSServiceStub.product_TabularQuery(product_TabularQuery2);
			} catch (Exception e) {
				loggerError.error("第1次异常==="+e);
				try {
					response = productWSServiceStub.product_TabularQuery(product_TabularQuery2);
				} catch (Exception e2) {
					loggerError.error("第2次异常==="+e2);
					try {
						response = productWSServiceStub.product_TabularQuery(product_TabularQuery2);
					} catch (Exception e3) {
						loggerError.error("第3次异常==="+e3);
					}
				}
			}			
			if(null != response){
				Product_TabularQueryResponseStructure[] localItems = response.getRecords().getItem();
				System.out.println(localItems.length); 
				for(Product_TabularQueryResponseStructure item : localItems){
					try {
						SkuDTO sku = new SkuDTO();
						sku.setId(UUIDGenerator.getUUID());
		                sku.setSupplierId(supplierId);
		                sku.setSkuId(item.getSkuId());
		                sku.setSpuId(item.getProductCode());
		                sku.setProductName(item.getCategoryName()+" "+item.getBrandName());
		                sku.setMarketPrice(item.getMarketPrice());
		                sku.setSalePrice("");
		                sku.setSupplierPrice(item.getSupplierPrice());
		                sku.setBarcode(item.getSkuId());
		                sku.setProductCode(item.getProductCode());
		                sku.setColor(item.getColor());
		                sku.setSaleCurrency(item.getSaleCurrency());
		                sku.setProductSize(item.getSize());
		                sku.setStock(""+item.getStock());
		                skuList.add(sku);	                
		                try {
		                	imageMap.put(sku.getSkuId()+";"+sku.getProductCode()+" "+sku.getColor(), Arrays.asList(item.getImages().split("\\|\\|")));
						} catch (Exception e) {
							e.printStackTrace();
						}               
		                
		                SpuDTO spu = new SpuDTO();
		                spu.setId(UUIDGenerator.getUUID());
		                spu.setSupplierId(supplierId);
		                spu.setSpuId(sku.getSpuId());
		                spu.setCategoryGender(item.getCategoryGender());
		                spu.setCategoryName(item.getCategoryName());
		                spu.setBrandName(item.getBrandName());
		                spu.setSeasonName(item.getSeasonName());
		                spu.setMaterial(item.getMaterial());
		                spu.setProductOrigin(item.getProductOrigin());
		                spuList.add(spu);
		                
					} catch (Exception e) {
						e.printStackTrace();
					}		
	                
				}
			}			
			System.out.println("======orver========="); 
			
		}catch(Exception ex){
			loggerError.error(ex);
			ex.printStackTrace();
		}
		System.out.println("imageMap======"+imageMap.size()); 
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;		
	}	
	
	public static void main(String[] args) {
		FetchProduct f = new FetchProduct();
		f.fetchProductAndSave();
	}
	
}


package com.shangpin.iog.grouppo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.grouppo.axis.ProductWSServiceStub;
import com.shangpin.iog.grouppo.axis.ProductWSServiceStub.Product_TabularQueryResponse;
import com.shangpin.iog.grouppo.axis.ProductWSServiceStub.Product_TabularQueryResponseStructure;
import com.shangpin.iog.grouppo.dto.PritelliSkuDto;
import com.shangpin.iog.grouppo.dto.PritelliSpuDto;
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
	
	private static Gson gson = new Gson();
	
	public Map<String, Object> fetchProductAndSave() {
		
		try{
			ProductWSServiceStub productWSServiceStub = new ProductWSServiceStub();
			productWSServiceStub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.SO_TIMEOUT,new Integer(1000*60*120));
			productWSServiceStub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CONNECTION_TIMEOUT,new Integer(1000*60*120));

			ProductWSServiceStub.Product_TabularQuery product_TabularQuery2 = new ProductWSServiceStub.Product_TabularQuery();
			product_TabularQuery2.setM_UserName("shangpin");
			product_TabularQuery2.setM_Password("getDataWs16");
			product_TabularQuery2.setM_Company("PRITE");
			product_TabularQuery2.setSkuId(""); 
			Product_TabularQueryResponse response = null;
			try {
				response = productWSServiceStub.product_TabularQuery(product_TabularQuery2);
			} catch (Exception e) {
				loggerError.error("第1次异常==="+e.getMessage(),e);
				try {
					response = productWSServiceStub.product_TabularQuery(product_TabularQuery2);
				} catch (Exception e2) {
					loggerError.error("第2次异常==="+e2.getMessage(),e2);
					try {
						response = productWSServiceStub.product_TabularQuery(product_TabularQuery2);
					} catch (Exception e3) {
						loggerError.error("第3次异常==="+e3.getMessage(),e3);
					}
				}
			}			
			if(null != response){

				Product_TabularQueryResponseStructure[] localItems = response.getRecords().getItem();

				if(null!=localItems) {
					System.out.println(localItems.length);
					for (Product_TabularQueryResponseStructure item : localItems) {
						try {
							PritelliSkuDto sku = new PritelliSkuDto();
							sku.setId(UUIDGenerator.getUUID());
							sku.setSupplierId(supplierId);
							sku.setSkuId(item.getSkuId());
							sku.setSpuId(item.getProductCode());
							sku.setProductName(item.getCategoryName() + " " + item.getBrandName());
							sku.setMarketPrice(item.getMarketPrice());
							sku.setSalePrice("");
							sku.setSupplierPrice(item.getSupplierPrice());
							sku.setBarcode(item.getSkuId());
							sku.setSaleCurrency(item.getSaleCurrency());
							sku.setProductSize(item.getSize());
							sku.setStock("" + item.getStock());

							PritelliSpuDto spu = new PritelliSpuDto();
							spu.setId(UUIDGenerator.getUUID());
							spu.setSupplierId(supplierId);
							spu.setSpuId(sku.getSpuId());
							spu.setCategoryGender(item.getCategoryGender());
							spu.setCategoryName(item.getCategoryName());
							spu.setBrandName(item.getBrandName());
							spu.setSeasonName(item.getSeasonName());
							spu.setMaterial(item.getMaterial());
							spu.setProductOrigin(item.getProductOrigin());
							spu.setColor(item.getColor());
							spu.setProductModel(item.getProductCode());
							spu.setPictures(Arrays.asList(item.getImages().split("\\|\\|")));
							List<PritelliSkuDto> skus = new ArrayList<>();
							skus.add(sku);
							spu.setSkus(skus);
							//发送
							supp.setData(gson.toJson(spu));
							pushMessage(null);

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}else{
					loggerError.error("未获取到产品信息");
				}
			}			
			System.out.println("======orver========="); 
			
		}catch(Exception ex){
			loggerError.error(ex.getMessage(),ex);
			ex.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args){
		FetchProduct fetchProduct = new FetchProduct();
		fetchProduct.fetchProductAndSave();
	}
	
}


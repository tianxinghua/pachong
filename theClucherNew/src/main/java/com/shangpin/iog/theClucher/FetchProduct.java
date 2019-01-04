package com.shangpin.iog.theClucher;

/**
 * Created by wang on 2015/9/21.
 */

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.product.AbsSaveProduct;

/**
 * Created by 赵根春 on 2015/9/25.
 */
@Component("theClucher")
public class FetchProduct  extends AbsSaveProduct{

	@Autowired
	ProductFetchService productFetchService;
	@Autowired
	private ProductSearchService productSearchService;
	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static Gson gson = null;
	public static int day;
	public static int max;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		gson = new Gson();
	}

	public void sendAndSaveProduct(){
		handleData("spu", supplierId, day, null);
	}
	
	@Override
	public Map<String, Object> fetchProductAndSave() {
		getProductList();
		if(flag){
//			messMappingAndSave(obj);
		}
		return null;
	}
	  private static String getJson() {

			String fullFileName = "F://code//rail.json";

			File file = new File(fullFileName);
			Scanner scanner = null;
			StringBuilder buffer = new StringBuilder();
			try {
				scanner = new Scanner(file, "utf-8");
//				while (scanner.hasNextLine()) {
					buffer.append(scanner.nextLine());
//				}
			} catch (Exception e) {

			} finally {
				if (scanner != null) {
					scanner.close();
				}
			}
			System.out.println(buffer.toString());
			return buffer.toString();
		}

	public  void getProductList(){
//		String json =Test.getJson();
		String json =getJson();
		List<Product> obj = new Gson().fromJson(json,new TypeToken<List<Product>>()
		        {}.getType());
		
		if(obj!=null&&obj.size()>0){
			
			for(Product product:obj){ 
				supp.setData(gson.toJson(product));
				pushMessage(null);
			}
			
		}
	}
	
	/**
	 * message mapping and save into DB
	 */
	private  void messMappingAndSave(List<Product> item1) {
		if(item1!=null){
					
					for (Product item : item1) {
						SpuDTO spu = new SpuDTO();
						try {
							spu.setId(UUIDGenerator.getUUID());
							spu.setSupplierId(supplierId);
							spu.setSpuId(item.getSpuID());
							spu.setCategoryName(item.getCategory());
							spu.setSubCategoryName(item.getSubCategory());
							spu.setBrandName(item.getBrand());
							spu.setSpuName(item.getName());
//							spu.setProductOrigin(item.getMade_in());
							spu.setSeasonName(item.getSeason());
							spu.setCategoryGender(item.getGender());
							productFetchService.saveSPU(spu);
						} catch (Exception e) {
							try {
								productFetchService.updateMaterial(spu);
							} catch (ServiceException e1) {
								e1.printStackTrace();
							}
						}
						
						String skuId = null;
						SkuDTO sku = new SkuDTO();
						try {
							sku.setId(UUIDGenerator.getUUID());
							sku.setSupplierId(supplierId);
							sku.setSpuId(item.getSpuID());
							String size = null;
							size = item.getSize();
							if(size==null){
								size = "A";
							}
							skuId = item.getSkuID();
							sku.setSkuId(skuId);
							sku.setProductSize(size);
							sku.setStock(item.getStock());
							sku.setProductCode(item.getCode());
							sku.setMarketPrice(item.getMarketPrice());
							sku.setSupplierPrice(item.getSupplierPrice());
							sku.setColor(item.getColor());
							sku.setProductName(item.getName());
							sku.setProductDescription(item.getDescription());
//							sku.setSaleCurrency(item.getCurrency());
							productFetchService.saveSKU(sku);
							
						} catch (ServiceException e) {
							if (e.getMessage().equals("数据插入失败键重复")) {
								try {
									productFetchService.updatePriceAndStock(sku);
								} catch (ServiceException e1) {
									e1.printStackTrace();
								}
							} else {
								e.printStackTrace();
							}
		
						}
						
						String pics = item.getImages();
						String [] picArray = pics.split("\\|\\|");
						productFetchService.savePicture(supplierId, item.getSpuID(), null,Arrays.asList(picArray));
				
				}
				System.out.println("--保存end.....--");
				logger.info("--保存end.....--");
			}
	}

}
package com.shangpin.iog.deliberti.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.deliberti.dto.DelibertiSkuDto;
import com.shangpin.iog.deliberti.dto.DelibertiSpuDto;
import com.shangpin.iog.deliberti.dto.Product;
import com.shangpin.iog.deliberti.util.MyUtil;
import com.shangpin.product.AbsSaveProduct;

@Component("deliberti")
public class FetchProduct extends AbsSaveProduct{

	private static Logger logger = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	public static int day;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("productUrl");
		day = Integer.valueOf(bdl.getString("day"));
	}
	
	private static Gson gson = new Gson();

	
	public Map<String, Object> fetchProductAndSave() {
		
		try {
			List<Product> allProducts = MyUtil.readLocalCSV(url,Product.class);
			System.out.println("List<Product>的大小是=============="+allProducts.size());
			logger.info("List<Product>的大小是=============="+allProducts.size()); 
			for (Product pro : allProducts) {
				System.out.println(pro.getSpuId());
				//保存spu
				String sizes [] = {pro.getSize35(),pro.getSize35x(),pro.getSize36(),pro.getSize36x(),pro.getSize37(),pro.getSize37x(),
						pro.getSize38(),pro.getSize38x(),pro.getSize39(),pro.getSize39x(),pro.getSize40(),pro.getSize40x(),
						pro.getSize41(),pro.getSize41x(),pro.getSize42(),pro.getSize42x(),pro.getSize43(),pro.getSize43x(),
						pro.getSize44(),pro.getSize44x(),pro.getSize45(),pro.getSize45x(),pro.getSize46(),pro.getSize46x(),
						pro.getSize47(),pro.getSize47x(),pro.getSize48(),pro.getSize48x(),pro.getSize49(),pro.getSize49x()};

				List<DelibertiSkuDto> skus = new ArrayList<>();
				for (int i = 0; i < sizes.length; i++) {
					//保存sku
					DelibertiSkuDto sku = new DelibertiSkuDto();
					if(sizes[i]!=null){
						String si[] = sizes[i].split("~");
						if (si.length<1) {
							continue;
						}
						if(si[0].equals("")){
							continue;
						}else{
							if(si[0].indexOf("x")!=-1){
								String size = si[0].replace("x", ".5");
								sku.setProductSize(size);
							}else{
								sku.setProductSize(si[0]);
							}
						}
						try {
							if(si[1].equals("0")){
								continue;
							}else{
								sku.setStock(si[1]);
							}
						} catch (Exception e2) {
							e2.printStackTrace();
							continue;
						}
						sku.setId(UUIDGenerator.getUUID());
						sku.setSupplierId(supplierId);
						sku.setSpuId(pro.getSpuId());
						sku.setSkuId(pro.getSpuId()+"-"+sku.getProductSize());
						sku.setBarcode(pro.getBrandCode());
						sku.setMarketPrice(pro.getRetailPrice());
						sku.setSalePrice(pro.getCurrentPrice());
						sku.setSupplierPrice(pro.getCurrentPrice());
						sku.setProductName(pro.getSubCategoryDesc());
						sku.setSaleCurrency("Euro");
						sku.setProductDescription(pro.getProductType());
						skus.add(sku);
					}
				}
				DelibertiSpuDto spu = new DelibertiSpuDto();
				spu.setId(UUIDGenerator.getUUID());
				spu.setSupplierId(supplierId);
				spu.setSpuId(pro.getSpuId());
				spu.setBrandName(pro.getBrandDesc());
				spu.setCategoryGender(pro.getGenderDesc());
				spu.setCategoryName(pro.getMainCategoryDesc());
				spu.setMaterial(pro.getMaterial());
				spu.setSeasonName(pro.getSeasonDesc());
				spu.setProductOrigin("");
				spu.setColor(pro.getColor());
				spu.setProductModel(pro.getArticleCode());
				spu.setDescription(pro.getDescription()); 
				spu.setSkus(skus);
				List<String> pics = new ArrayList<String>();
				if(pro.getImage()!=null){
					String picArrays[] = pro.getImage().split("~");
					for (int i = 0; i < picArrays.length; i++) {
						pics.add("http://deliberti.it/productsimage/"+picArrays[i]);
					}
					spu.setPictures(pics);
				}
				supp.setData(gson.toJson(spu));  
				pushMessage(null);
			}
			logger.info("抓取结束");
		} catch (Exception e2) {
			e2.printStackTrace();
			logError.error(e2);
		}
		return null;
	}
}


package com.shangpin.iog.theStyleSide.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.theStyleSide.dto.CsvDTO;
import com.shangpin.iog.theStyleSide.utils.DownloadAndReadCSV;
import com.shangpin.product.AbsSaveProduct;

@Component("theStyleSide")
public class FetchProduct extends AbsSaveProduct{
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	public static int day;
	private Gson gson = new Gson();
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		if(bdl.getString("flag")!=null){
			flag = Boolean.parseBoolean(bdl.getString("flag"));
		}
	}
	@Override
	public Map<String,Object> fetchProductAndSave() {
		logger.info("开始抓取");
		List<CsvDTO> csvLists = null;
		try {
			List<String> strings = DownloadAndReadCSV.downloadNet();

			csvLists = DownloadAndReadCSV.readLocalCSV(CsvDTO.class, "\",\"",strings);
			logger.info("抓取结束");
			if(csvLists!=null){
				for (CsvDTO csvDTO : csvLists) {
					if(csvDTO==null||(csvDTO.getSize()==null||csvDTO.getSize().equals(""))||csvDTO.getSku().equals("SKU")){
						continue;
					}
					System.out.println(gson.toJson(csvDTO));
					supp.setData(gson.toJson(csvDTO));
		 			pushMessage(null);
		 			System.out.println("111111111111");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(flag){
			return getProductMap(csvLists);
		}
		return null;
	}
	
	public  Map<String,Object> getProductMap(List<CsvDTO> csvLists){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String, List<String>> imageMap = new HashMap<String, List<String>>();
		for (CsvDTO csvDTO : csvLists) {
			if(csvDTO.getSize()==null||csvDTO.getSize().equals("")||csvDTO.getSku().equals("SKU")) {
				continue;
			}
			String[] array = csvDTO.getSize().split(";");
			for(String skuSizeAndStock : array) {
				try {
					String stock = null;
					String size = null;
					if(!skuSizeAndStock.contains("(")){
						System.out.println("库存信息数据格式异常");
						logger.info("商品barCode:"+csvDTO.getProduct_code()+" 库存信息数据格式异常跳过，skuSizeAndStock："+skuSizeAndStock);
                        continue;
					}else{
						size = skuSizeAndStock.substring(0, skuSizeAndStock.indexOf("("));
						stock = skuSizeAndStock.substring(skuSizeAndStock.indexOf("(")+1,skuSizeAndStock.indexOf(")"));
					}
					SkuDTO skuDTO = new SkuDTO();
					skuDTO.setId(UUIDGenerator.getUUID());
					skuDTO.setSkuId(csvDTO.getSku()+"-"+size);
					skuDTO.setSpuId(csvDTO.getSku());
					skuDTO.setSupplierId(supplierId);
					skuDTO.setProductName(csvDTO.getModel_number());
					skuDTO.setSalePrice(csvDTO.getItalian_retail_price());
					skuDTO.setMarketPrice(csvDTO.getItalian_retail_price());
					skuDTO.setSupplierPrice(csvDTO.getItalian_retail_price());
					skuDTO.setColor(csvDTO.getColor());
					skuDTO.setProductCode(csvDTO.getSku()+size);
					skuDTO.setSaleCurrency("EURO");
					skuDTO.setProductSize(csvDTO.getSize());
					skuDTO.setStock(stock);
					skuDTO.setProductDescription(csvDTO.getDescription());
					skuDTO.setBarcode(csvDTO.getProduct_code());
					skuList.add(skuDTO);

					//保存图片
					ProductPictureDTO pictureDTO = new ProductPictureDTO();
					pictureDTO.setSupplierId(supplierId);
					pictureDTO.setSkuId(csvDTO.getSku()+"-"+size);
					String[] picUrl = new String[]{csvDTO.getPics()};
					imageMap.put(skuDTO.getSkuId() + ";" + skuDTO.getProductCode(), Arrays.asList(picUrl));

					//保存spu
					SpuDTO spu = new SpuDTO();
					spu.setId(UUIDGenerator.getUUID());
					spu.setSpuId(csvDTO.getSku());
					spu.setSupplierId(supplierId);
					spu.setBrandName(csvDTO.getBrand());
					spu.setCategoryName(csvDTO.getCategory());
					spu.setCategoryGender(csvDTO.getSesso());
					spu.setMaterial(csvDTO.getMaterial());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}
}


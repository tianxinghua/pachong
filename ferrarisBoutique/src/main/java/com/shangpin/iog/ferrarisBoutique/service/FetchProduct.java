package com.shangpin.iog.ferrarisBoutique.service;
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
import com.shangpin.iog.ferrarisBoutique.dto.CsvDTO;
import com.shangpin.iog.ferrarisBoutique.utils.DownloadAndReadCSV;
import com.shangpin.product.AbsSaveProduct;

@Component("ferrarisBoutique")
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
			csvLists = DownloadAndReadCSV.readLocalCSV(CsvDTO.class, "\\|");
			logger.info("抓取结束");
			int i = 0;
			if(csvLists!=null){
				for (CsvDTO csvDTO : csvLists) {
					if(i==100) {
						break;
					}
					if(csvDTO.getSpuno().equals("795")) {
						System.out.println(1111);
					}
					if(csvDTO==null){
						continue;
					}
					System.out.println(gson.toJson(csvDTO));
					supp.setData(gson.toJson(csvDTO));
		 			pushMessage(null);
		 			i++;
		 			System.out.println(i);
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
			SkuDTO skuDTO = new SkuDTO();
			skuDTO.setId(UUIDGenerator.getUUID());
			skuDTO.setSkuId(csvDTO.getSkuno());
			skuDTO.setSpuId(csvDTO.getSpuno());
			skuDTO.setSupplierId(supplierId);
			skuDTO.setProductName(csvDTO.getSpuname());
			skuDTO.setSalePrice(csvDTO.getMarketPrice());
			skuDTO.setMarketPrice(csvDTO.getMarketPrice());
			skuDTO.setSupplierPrice(csvDTO.getSupplyPrice());
			skuDTO.setColor(csvDTO.getColor());
			skuDTO.setProductCode(csvDTO.getProductCode());
			skuDTO.setSaleCurrency(csvDTO.getCurrency());
			skuDTO.setProductSize(csvDTO.getSize());
			skuDTO.setStock(csvDTO.getStock());
			skuDTO.setProductDescription(csvDTO.getProductDescription());
			skuDTO.setBarcode(csvDTO.getBarcode());
			skuList.add(skuDTO);
			
			//保存图片
			ProductPictureDTO pictureDTO = new ProductPictureDTO();
			pictureDTO.setSupplierId(supplierId);
			pictureDTO.setSkuId(csvDTO.getSkuno());
			String[] picUrl = new String[]{csvDTO.getPic(),csvDTO.getImages1(),csvDTO.getImages2(),csvDTO.getImages3(),csvDTO.getImages4()};
			imageMap.put(skuDTO.getSkuId() + ";" + skuDTO.getProductCode(), Arrays.asList(picUrl));
			
			//保存spu
			SpuDTO spu = new SpuDTO();
			spu.setId(UUIDGenerator.getUUID());
			spu.setSpuId(csvDTO.getSpuno());
			spu.setSupplierId(supplierId);
			spu.setBrandName(csvDTO.getBrand());
			spu.setCategoryName(csvDTO.getCategory());
//			spu.setCategoryGender(csvDTO);
			spu.setMaterial(csvDTO.getMaterial());
		}
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}
}


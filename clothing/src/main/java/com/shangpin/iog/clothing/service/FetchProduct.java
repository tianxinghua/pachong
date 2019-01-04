package com.shangpin.iog.clothing.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.clothing.dto.CsvDTO;
import com.shangpin.iog.clothing.utils.DownloadAndReadCSV;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.product.AbsSaveProduct;

@Component("clothing")
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
			csvLists = DownloadAndReadCSV.readLocalCSV(CsvDTO.class, "\t");
			logger.info("抓取结束");
			if(csvLists!=null){
				for (CsvDTO csvDTO : csvLists) {
					if(csvDTO==null||csvDTO.getAge()==null||csvDTO.getAge().equals("")||csvDTO.getAge().equals(" ")){
						continue;
					}
					csvDTO.setPrice(csvDTO.getPrice().substring(0,csvDTO.getPrice().indexOf(" GBP")));
					if(csvDTO.getSale_price()==null||csvDTO.getSale_price().equals(" ")||csvDTO.getSale_price().equals("")) {
						csvDTO.setSale_price(csvDTO.getPrice());
					}else {
						csvDTO.setSale_price(csvDTO.getSale_price().substring(0,csvDTO.getSale_price().indexOf(" GBP")));
					}
					System.out.println(gson.toJson(csvDTO));
					supp.setData(gson.toJson(csvDTO));
		 			pushMessage(null);
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
			if(csvDTO.getId().equals("")||csvDTO==null){
				continue;
			}
			SkuDTO skuDTO = new SkuDTO();
			skuDTO.setId(UUIDGenerator.getUUID());
			skuDTO.setSkuId(csvDTO.getMpn());
			skuDTO.setSpuId(csvDTO.getLinked_products());
			skuDTO.setSupplierId(supplierId);
			skuDTO.setProductName(csvDTO.getTitle());
			skuDTO.setSalePrice(csvDTO.getPrice());
			skuDTO.setMarketPrice(csvDTO.getPrice());
			skuDTO.setSupplierPrice(csvDTO.getPrice());
			skuDTO.setColor(csvDTO.getColour());
			skuDTO.setProductCode(csvDTO.getLinked_products());
			skuDTO.setSaleCurrency("GBP");
			skuDTO.setProductSize(csvDTO.getShoe_size());
			skuDTO.setStock(csvDTO.getStock_quantity());
			skuDTO.setProductDescription(csvDTO.getDescription());
			skuDTO.setBarcode(csvDTO.getId());
			skuList.add(skuDTO);
			
			//保存图片
			ProductPictureDTO pictureDTO = new ProductPictureDTO();
			pictureDTO.setSupplierId(supplierId);
			pictureDTO.setSkuId(csvDTO.getMpn());
			String[] picUrl = new String[]{csvDTO.getImage_link()};
			imageMap.put(skuDTO.getSkuId() + ";" + skuDTO.getProductCode(), Arrays.asList(picUrl));
			
			//保存spu
			SpuDTO spu = new SpuDTO();
			spu.setId(UUIDGenerator.getUUID());
			spu.setSpuId(csvDTO.getId());
			spu.setSupplierId(supplierId);
			spu.setBrandName(csvDTO.getBrand());
			spu.setCategoryName(csvDTO.getDepartment());
			spu.setCategoryGender("");
			spu.setMaterial(csvDTO.getDescription());
		}
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}
}


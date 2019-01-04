package com.shangpin.iog.srl.service;
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
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.srl.dto.CsvDTO;
import com.shangpin.product.AbsSaveProduct;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component("srl")
public class FetchProduct extends AbsSaveProduct{
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	public static int day;
	private static String httpurl;
	private Gson gson = new Gson();
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		if(bdl.getString("flag")!=null){
			flag = Boolean.parseBoolean(bdl.getString("flag"));
		}
		httpurl = bdl.getString("url");
	}
	@Override
	public Map<String,Object> fetchProductAndSave() {
		logger.info("开始抓取");
		List<CsvDTO> csvLists = null;
		try {
			csvLists = getAllProduct();
			logger.info("抓取结束");
			if(csvLists!=null){
				for (CsvDTO csvDTO : csvLists) {
					if(csvDTO==null){
						continue;
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
			if(csvDTO==null){
				continue;
			}
			SkuDTO skuDTO = new SkuDTO();
			skuDTO.setId(UUIDGenerator.getUUID());
			skuDTO.setSkuId(csvDTO.getSku());
			skuDTO.setSpuId(csvDTO.getSpu());
			skuDTO.setSupplierId(supplierId);
			skuDTO.setProductName(csvDTO.getName());
			skuDTO.setSalePrice(csvDTO.getMarketPrice());
			skuDTO.setMarketPrice(csvDTO.getMarketPrice());
			skuDTO.setSupplierPrice(csvDTO.getSupplyPrice());
			skuDTO.setColor(csvDTO.getColour());
			skuDTO.setProductCode(csvDTO.getSpu());
			skuDTO.setSaleCurrency("EURO");
			skuDTO.setProductSize(csvDTO.getSize());
			skuDTO.setStock(csvDTO.getQty());
			skuDTO.setProductDescription("");
			skuDTO.setBarcode(csvDTO.getBarcode());
			skuList.add(skuDTO);
			
			//保存图片
			ProductPictureDTO pictureDTO = new ProductPictureDTO();
			pictureDTO.setSupplierId(supplierId);
			pictureDTO.setSkuId(csvDTO.getSku());
			String[] picUrl = new String[]{csvDTO.getUrlImage()};
			imageMap.put(skuDTO.getSkuId() + ";" + skuDTO.getProductCode(), Arrays.asList(picUrl));
			
			//保存spu
			SpuDTO spu = new SpuDTO();
			spu.setId(UUIDGenerator.getUUID());
			spu.setSpuId(csvDTO.getSpu());
			spu.setSupplierId(supplierId);
			spu.setBrandName(csvDTO.getBrand());
			spu.setCategoryName(csvDTO.getCategory());
			spu.setCategoryGender(csvDTO.getGender());
			spu.setMaterial(csvDTO.getMaterial());
		}
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}
	
	private List<CsvDTO> getAllProduct() {
		OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);
		String result = HttpUtil45.get(httpurl, timeConfig, null);
		JSONObject resultJson = JSONObject.fromObject(result);
		JSONArray array = (JSONArray) resultJson.get("catalog");
		List<CsvDTO> listDTO = new ArrayList<>();
		for(int i=0;i<array.size();i++) {
			JSONObject json = array.getJSONObject(i);
			CsvDTO dto = new CsvDTO();
			dto.setSku(json.get("sku") == null ? "" : json.get("sku").toString());
			dto.setSpu(json.get("spu") == null ? "" : json.get("spu").toString());
			dto.setBrand(json.get("brand") == null ? "" : json.get("brand").toString());
			dto.setName(json.get("name") == null ? "" : json.get("name").toString());
			if(json.get("colour") != null) {
				if(json.get("colour").toString().length()>50) {
					dto.setColour(json.get("colour").toString().substring(0, 49));
				}else {
					dto.setColour(json.get("colour").toString());
				}
			}else {
				dto.setColour("");
			}
			dto.setMaterial(json.get("material") == null ? "" : json.get("material").toString());
			dto.setGender(json.get("gender") == null ? "" : json.get("gender").toString());
			dto.setShape(json.get("shape") == null ? "" : json.get("shape").toString());
			dto.setType(json.get("type") == null ? "" : json.get("type").toString());
			dto.setBarcode(json.get("barcode") == null ? "" : json.get("barcode").toString());
			dto.setMarketPrice(json.get("marketPrice") == null ? "" : json.get("marketPrice").toString());
			dto.setSupplyPrice(json.get("supplyPrice") == null ? "" : json.get("supplyPrice").toString());
			dto.setCategory(json.get("category") == null ? "" : json.get("category").toString());
			dto.setUrlImage(json.get("urlImage") == null ? "" : json.get("urlImage").toString());
			dto.setQty(json.get("qty") == null ? "" : json.get("qty").toString());
			dto.setSeason(json.get("season") == null ? "" : json.get("season").toString());
			if(json.get("size")!=null) {
				String[] arraySize = json.get("size").toString().split("/");
				dto.setSize(arraySize[0]);
			}else {
				dto.setSize("");
			}
			listDTO.add(dto);
		}
		return listDTO;
	}
}


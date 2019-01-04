package com.shangpin.iog.monnalisa.service;
import java.rmi.RemoteException;
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
import com.shangpin.iog.monnalisa.dto.CsvDTO;
import com.shangpin.iog.monnalisa.utils.DownloadAndReadCSV;
import com.shangpin.product.AbsSaveProduct;

import eu.monnalisa.pf.MonnalisaWSProxy;

@Component("monnalisa")
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
			csvLists = DownloadAndReadCSV.readLocalCSV(CsvDTO.class, ";");
			logger.info("抓取结束");
			if(csvLists!=null){
				for (CsvDTO csvDTO : csvLists) {
					String data = "";
					if(csvDTO==null){
						continue;
					}
					String[] array = csvDTO.getGoogle_product_category().split(">");
					for(int j=0;j<array.length;j++){
						if(!array[j].contains("years")&&!array[j].contains("months")){
							if(j==0)
								data = array[j];
							else
								data = data +">"+array[j];
						}
					}
					csvDTO.setGoogle_product_category(data);
					MonnalisaWSProxy proxy = new MonnalisaWSProxy();
	            	String[] arraySku = csvDTO.getId().split("-");
	            	eu.monnalisa.pf.GenericResult result = null;
	            	try {
	            		result= proxy.getDisponibilitaMagazzini(arraySku[0], arraySku[1], arraySku[2], arraySku[3], arraySku[4], null, null, "ecommerce", null);
	            		csvDTO.setStock(result.getQuantity());
	            	} catch (Exception e) {
						logger.error(e.getMessage());
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
			if(csvDTO.getId().equals("")||csvDTO==null){
				continue;
			}
			csvDTO.getId().lastIndexOf("-");
			SkuDTO skuDTO = new SkuDTO();
			skuDTO.setId(UUIDGenerator.getUUID());
			skuDTO.setSkuId(csvDTO.getId().substring(0, csvDTO.getId().lastIndexOf("-")));
			skuDTO.setSpuId(csvDTO.getId().substring(0, csvDTO.getId().lastIndexOf("-")));
			skuDTO.setSupplierId(supplierId);
			skuDTO.setProductName(csvDTO.getTitle());
			skuDTO.setSalePrice(csvDTO.getSale_price());
			skuDTO.setMarketPrice(csvDTO.getPrice());
			skuDTO.setSupplierPrice(csvDTO.getPrice());
			skuDTO.setColor(csvDTO.getColor());
			skuDTO.setProductCode(csvDTO.getId());
			skuDTO.setSaleCurrency("EURO");
			skuDTO.setProductSize(csvDTO.getSize());
			skuDTO.setStock("10");
			skuDTO.setProductDescription(csvDTO.getDescription());
			skuDTO.setBarcode(csvDTO.getBarcode());
			skuList.add(skuDTO);
			
			//保存图片
			ProductPictureDTO pictureDTO = new ProductPictureDTO();
			pictureDTO.setSupplierId(supplierId);
			pictureDTO.setSkuId(csvDTO.getId().substring(0, csvDTO.getId().lastIndexOf("-")));
			String[] picUrl = new String[]{csvDTO.getImage_link()};
			imageMap.put(skuDTO.getSkuId() + ";" + skuDTO.getProductCode(), Arrays.asList(picUrl));
			
			//保存spu
			SpuDTO spu = new SpuDTO();
			spu.setId(UUIDGenerator.getUUID());
			spu.setSpuId(csvDTO.getId());
			spu.setSupplierId(supplierId);
			spu.setBrandName(csvDTO.getBrand());
			spu.setCategoryName(csvDTO.getGoogle_product_category());
			spu.setCategoryGender(csvDTO.getGender());
			spu.setMaterial(csvDTO.getMaterial());
		}
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}
}


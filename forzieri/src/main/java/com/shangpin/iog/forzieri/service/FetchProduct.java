package com.shangpin.iog.forzieri.service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.forzieri.dto.CategoryMap;
import com.shangpin.iog.forzieri.dto.CsvDTO;
import com.shangpin.iog.forzieri.utils.DownloadAndReadCSV;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.service.SkuPriceService;
import com.shangpin.product.AbsSaveProduct;

@Component("forzieri")
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
	@Autowired
	private ProductFetchService productFetchService;
	@Autowired
	private SkuPriceService skuPriceService;
	@Autowired
	private ProductSearchService productSearchService;
	@Override
	public Map<String,Object> fetchProductAndSave() {
		logger.info("开始抓取");
		List<CsvDTO> csvLists = null;
		try {
			csvLists = DownloadAndReadCSV.readLocalCSV(CsvDTO.class, "\t");
			logger.info("抓取结束");
			if(csvLists!=null){
				for (CsvDTO csvDTO : csvLists) {
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
		Map<String, String> categoryMap = new CategoryMap(new HashMap<String,String>()).getCategoryMap();
		for (CsvDTO csvDTO : csvLists) {
			SkuDTO skuDTO = new SkuDTO();
			skuDTO.setId(UUIDGenerator.getUUID());
			skuDTO.setSkuId(csvDTO.getSku());
			skuDTO.setSpuId(csvDTO.getProduct_id());
			skuDTO.setSupplierId(supplierId);
			skuDTO.setProductName(csvDTO.getProduct_name());
			skuDTO.setSalePrice(csvDTO.getSelling_price());
			skuDTO.setMarketPrice(csvDTO.getList_price());
			skuDTO.setSupplierPrice(csvDTO.getCost_price());
			skuDTO.setColor(csvDTO.getColor());
			if (StringUtils.isNotBlank(csvDTO.getManufacturer_id())) {
				skuDTO.setProductCode(csvDTO.getManufacturer_id());
			}else{
				skuDTO.setProductCode(csvDTO.getSku());
			}
			skuDTO.setSaleCurrency("EURO");
			skuDTO.setProductSize(csvDTO.getSize());
			skuDTO.setStock(csvDTO.getQuantity());
			skuDTO.setProductDescription(csvDTO.getDescription());
			skuDTO.setBarcode(csvDTO.getPreorder()+" | "+csvDTO.getShips_in_days());
			skuList.add(skuDTO);
			
			//保存图片
			ProductPictureDTO pictureDTO = new ProductPictureDTO();
			pictureDTO.setSupplierId(supplierId);
			pictureDTO.setSkuId(csvDTO.getSku());
			String[] picUrl = new String[]{csvDTO.getVistaImagel0(),csvDTO.getVistaImagel1()
					,csvDTO.getVistaImagel2(),csvDTO.getVistaImagel3()
					,csvDTO.getVistaImagel4(),csvDTO.getVistaImagel5()};
			imageMap.put(skuDTO.getSkuId() + ";" + skuDTO.getProductCode(), Arrays.asList(picUrl));
			
			//保存spu
			SpuDTO spu = new SpuDTO();
			spu.setId(UUIDGenerator.getUUID());
			spu.setSpuId(csvDTO.getProduct_id());
			spu.setSupplierId(supplierId);
			spu.setBrandName(csvDTO.getBrand_name());
			String categoryid = csvDTO.getCategory_ids().split("\\|")[0];
			spu.setCategoryName(categoryMap.get(categoryid));
			spu.setCategoryGender(csvDTO.getGender());
			spu.setMaterial(csvDTO.getMaterial());
		}
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}
}


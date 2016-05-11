package com.shangpin.iog.revolve.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.revolve.dto.ProductDTO;
import com.shangpin.iog.revolve.sepStrategy.ISepStrategy;
import com.shangpin.iog.revolve.sepStrategy.SepStrategyContext;
import com.shangpin.iog.revolve.util.Csv2DTO;
import com.shangpin.product.AbsSaveProduct;

@Component("framerevolve")
public class RevolveFrameFetchProduct extends AbsSaveProduct{
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	final Logger logger = Logger.getLogger("info");

	private static ResourceBundle bdl = null;

	public static String supplierId;

	public static String url;
	public static String picpath;
	public static int day;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		url = bdl.getString("url");
		picpath = bdl.getString("picpath");
	}
	//sku:List(skuDTO) spu:List(spuDTO) image: Map(id;picName,List) 
	@Override
	public Map<String, Object> fetchProductAndSave() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
		Map<String,ProductDTO> spuMap = new HashMap<String, ProductDTO>();
		String sep = "\t";
		Csv2DTO csv2 = new Csv2DTO();
		//第一个为size and stock
		String[] needColsNo = new String[]{"","0","2","14","22","3","","9,10,11,12,8","","16","4","","23","","","27","23","19","20","","5"};
		//策略组
		String[] strategys = new String[]{"","","","","","","","more% %0%;","","","","","sin% %0%\"\"","","","","sin% %1%\"\"","","","",""};
		ISepStrategy[] iSepStrategies = new SepStrategyContext().operate(strategys);
		List<ProductDTO> dto = csv2.toDTO(url, "F:\\head-revolveChinaMarketplace.txt", sep, needColsNo, iSepStrategies, ProductDTO.class);
		
		for (ProductDTO productDTO : dto) {
			if (!spuMap.containsKey(productDTO.getSpuId())) {
				spuMap.put(productDTO.getSpuId(), productDTO);
			}
			SkuDTO sku = new SkuDTO();
			sku.setId(UUIDGenerator.getUUID());
			sku.setSupplierId(supplierId);

			sku.setSpuId(productDTO.getSpuId());
			sku.setSkuId(productDTO.getSkuId());
			sku.setProductSize(productDTO.getSize());
			sku.setMarketPrice(productDTO.getMarketPrice());
			sku.setSalePrice(productDTO.getSalePrice());
			sku.setSupplierPrice(productDTO.getSupplierPrice());
			sku.setColor(productDTO.getColor());
			sku.setProductDescription(productDTO.getDescription());
			sku.setStock(productDTO.getStock());
			sku.setProductCode(productDTO.getProductCode());
			sku.setSaleCurrency(productDTO.getCurrency());
			sku.setBarcode(productDTO.getBarcode());
			skuList.add(sku);
		}
		ProductDTO productDTO = null;
		String[] picArray = null;
		for (Entry<String, ProductDTO> en : spuMap.entrySet()) {
			productDTO = en.getValue();
			SpuDTO spu = new SpuDTO();
			spu.setId(UUIDGenerator.getUUID());
			spu.setSupplierId(supplierId);
			spu.setSpuId(productDTO.getSpuId());
			spu.setBrandName(productDTO.getBrand());
			spu.setCategoryName(productDTO.getCategory());
			spu.setMaterial(productDTO.getMaterial());
			spu.setSeasonName(productDTO.getSeason());
			spu.setProductOrigin(productDTO.getOrigin());
			// 商品所属性别字段；
			spu.setCategoryGender(productDTO.getGender());
			spuList.add(spu);
			
			if (StringUtils.isNotBlank(productDTO.getPicurl())) {
				picArray = productDTO.getPicurl().split(";");
				imageMap.put(productDTO.getSpuId()+";"+productDTO.getSpuId()+" "+productDTO.getColor(), Arrays.asList(picArray));
			}
			
		}
		
		
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}
	public static void main(String[] args){
//		new RevolveFrameFetchProduct().fetchProductAndSave();
//	  	加载spring
        loadSpringContext();
        RevolveFrameFetchProduct stockImp =(RevolveFrameFetchProduct)factory.getBean("framerevolve");
		stockImp.handleData("spu", supplierId, day, picpath);
	}
}

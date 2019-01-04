package com.shangpin.iog.revolve.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
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
	public static String filepath;
    @Autowired
    private ProductFetchService pfs;
    @Autowired
	ProductSearchService productSearchService;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		url = bdl.getString("url");
		picpath = bdl.getString("picpath");
		filepath = bdl.getString("filepath");
	}
	//sku:List(skuDTO) spu:List(spuDTO) image: Map(id;picName,List) 
	@Override
	public Map<String, Object> fetchProductAndSave() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
		Map<String,ProductDTO> spuMap = new HashMap<String, ProductDTO>();
		char sep = '\t';
		Csv2DTO csv2 = new Csv2DTO();
		//第一个为size and stock
	
		try {
//			List<ProductDTO> dto = csv2.toDTO(url, filepath,  ProductDTO.class);
			List<ProductDTO> dto = CVSUtil.readCSV(url,filepath, ProductDTO.class, sep);
		System.out.println(dto.size());
		for (ProductDTO productDTO : dto) {
			if (!spuMap.containsKey(productDTO.getItem_group_id())) {
				spuMap.put(productDTO.getItem_group_id(), productDTO);
			}
			SkuDTO sku = new SkuDTO();
			sku.setId(UUIDGenerator.getUUID());
			sku.setSupplierId(supplierId);
			sku.setProductName(productDTO.getTitle());
			sku.setSpuId(productDTO.getItem_group_id());
			sku.setSkuId(productDTO.getId());
			sku.setProductSize(productDTO.getSize());
			sku.setSalePrice(productDTO.getPrice().substring(3));
			sku.setSupplierPrice("");
			sku.setMarketPrice("");
			sku.setColor(productDTO.getColor());
			sku.setProductDescription(productDTO.getDescription());
			sku.setStock(productDTO.getSellableqty());
			sku.setProductCode(productDTO.getMpn());
			sku.setSaleCurrency(productDTO.getPrice().substring(0,3));
//			sku.setBarcode(productDTO.getBarcode());
			skuList.add(sku);
		}
		ProductDTO productDTO = null;
		for (Entry<String, ProductDTO> en : spuMap.entrySet()) {
			productDTO = en.getValue();
			SpuDTO spu = new SpuDTO();
			spu.setId(UUIDGenerator.getUUID());
			spu.setSupplierId(supplierId);
			spu.setSpuId(productDTO.getItem_group_id());
			spu.setBrandName(productDTO.getBrand());
			spu.setSpuName(productDTO.getTitle());
			spu.setCategoryName(productDTO.getProductType());
			String [] mate = productDTO.getDescription().split("\\.");
			spu.setMaterial(mate[0]);
			spu.setSeasonName(productDTO.getYear());
			spu.setProductOrigin(productDTO.getOrigin());
			// 商品所属性别字段；
			spu.setCategoryGender(productDTO.getGender());
			spuList.add(spu);
			List<String> list = new ArrayList();
			if (StringUtils.isNotBlank(productDTO.getImage_max_link_1())) {
				list.add(productDTO.getImage_max_link_1());
			}
			if (StringUtils.isNotBlank(productDTO.getImage_max_link_2())) {
				list.add(productDTO.getImage_max_link_2());
			}
			if (StringUtils.isNotBlank(productDTO.getImage_max_link_3())) {
				list.add(productDTO.getImage_max_link_3());
			}
			if (StringUtils.isNotBlank(productDTO.getImage_max_link_4())) {
				list.add(productDTO.getImage_max_link_4());
			}
			if (StringUtils.isNotBlank(productDTO.getImage_max_link_5())) {
				list.add(productDTO.getImage_max_link_5());
			}
			imageMap.put(productDTO.getItem_group_id()+";"+productDTO.getItem_group_id()+" "+productDTO.getColor(), list);
			
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("skuList"+skuList.size());
		System.out.println("imageMap"+imageMap.size());
		System.out.println("spuList"+spuList.size());
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

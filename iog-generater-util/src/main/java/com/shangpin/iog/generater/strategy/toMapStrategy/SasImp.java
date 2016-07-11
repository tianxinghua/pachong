package com.shangpin.iog.generater.strategy.toMapStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.generater.dto.ProductDTO;

/**
 * 处理特殊csv的类
 * 该类适用于sizeandstock，即尺码和库存在一列
 * @author sunny
 *
 */
public class SasImp implements TransStrategy{

	public Map<String, Object> toMap(List<ProductDTO> productList,String supplierId, String[] strategys) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();

		String osep = strategys[0].split("%")[0];
		String nsep = strategys[0].split("%")[1];
		
		String[] picArray = null;
		String sasstrs = "";
		for (ProductDTO productDTO : productList) {
			
			try {
				
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
				sasstrs = productDTO.getSizeandstock();
				for (String sas : sasstrs.split(nsep)) {
					try {
						
						SkuDTO sku = new SkuDTO();
						sku.setId(UUIDGenerator.getUUID());
						sku.setSupplierId(supplierId);
						sku.setSpuId(productDTO.getSpuId());
						sku.setSkuId(productDTO.getSkuId());
						
						sku.setProductSize(sas.split(osep)[0]);
						
						sku.setMarketPrice(productDTO.getMarketPrice());
						sku.setSalePrice(productDTO.getSalePrice());
						sku.setSupplierPrice(productDTO.getSupplierPrice());
						sku.setColor(productDTO.getColor());
						sku.setProductDescription(productDTO.getDescription());
						
						sku.setStock(sas.split(osep)[1]);
						
						sku.setProductCode(productDTO.getProductCode());
						sku.setSaleCurrency(productDTO.getCurrency());
						sku.setBarcode(productDTO.getBarcode());
						skuList.add(sku);
						
					} catch (Exception e) {
						e.printStackTrace();
					}					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}	
			
		}
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}
}

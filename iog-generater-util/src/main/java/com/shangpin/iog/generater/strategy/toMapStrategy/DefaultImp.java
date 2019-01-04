package com.shangpin.iog.generater.strategy.toMapStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.generater.dto.ProductDTO;
/**
 * 默认每个product为一个sku,每个sku都包含spu信息
 * @author Administrator
 *
 */
public class DefaultImp implements TransStrategy{

	public Map<String, Object> toMap(List<ProductDTO> productList,String supplierId,String[] strategys) {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
		Map<String,ProductDTO> spuMap = new HashMap<String, ProductDTO>();
		
		for (ProductDTO productDTO : productList) {
			try {
				
				if (!spuMap.containsKey(productDTO.getSpuId())) {
					spuMap.put(productDTO.getSpuId(), productDTO);
				}
				SkuDTO sku = new SkuDTO();
				sku.setId(UUIDGenerator.getUUID());
				sku.setSupplierId(supplierId);

				sku.setSpuId(productDTO.getSpuId());
				sku.setSkuId(productDTO.getSkuId());
				sku.setProductName(productDTO.getProductName()); 
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
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		ProductDTO productDTO = null;
		String[] picArray = null;
		for (Entry<String, ProductDTO> en : spuMap.entrySet()) {
			try {
				
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
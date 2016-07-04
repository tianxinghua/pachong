package com.shangpin.iog.generater.strategy.toMapStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.generater.dto.ProductDTO;

/**
 * 处理特殊的xml的类
 * 该类适用于一个spu下边包含多个sku，即ProductDTO类的productList不为空
 * @author sunny
 *
 */
public class SpuWithSkuImp implements TransStrategy{

	@Override
	public Map<String, Object> toMap(List<ProductDTO> productList,
			String supplierId, String[] strategys) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
		for (ProductDTO productDTO : productList) {
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
			for(ProductDTO skuDTO : productDTO.getProductList()){
				SkuDTO sku = new SkuDTO();
				sku.setId(UUIDGenerator.getUUID());
				sku.setSupplierId(supplierId);
				sku.setSpuId(skuDTO.getSpuId());
				sku.setSkuId(skuDTO.getSkuId());
				sku.setProductSize(skuDTO.getSize());
				sku.setMarketPrice(skuDTO.getMarketPrice());
				sku.setSalePrice(skuDTO.getSalePrice().substring(3));
				sku.setSupplierPrice(skuDTO.getSupplierPrice());
				sku.setColor(skuDTO.getColor());
				sku.setProductDescription(skuDTO.getDescription());
				sku.setStock(skuDTO.getStock());
				sku.setProductCode(skuDTO.getProductCode());
				sku.setSaleCurrency(skuDTO.getSalePrice().substring(0,3));
				sku.setBarcode(skuDTO.getBarcode());
				skuList.add(sku);
				//TODO 处理图片
			}
			//TODO 或者在这里处理图片
		}
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}

}

/**
 * 
 */
package com.shangpin.iog.coltorti.convert;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.shangpin.iog.coltorti.dto.ColtortiProduct;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月5日
 */
public class ColtortiProductConvert {
	static String supplier="Coltorti";
	public static SkuDTO product2sku(ColtortiProduct p){
		SkuDTO dto = new SkuDTO();
		dto.setId(UUIDGenerator.getUUID());
		dto.setSupplierId(supplier);
		dto.setProductName(p.getName());
		dto.setProductCode(p.getSkuId());
		dto.setColor(p.getColor());
		dto.setCreateTime(new Date());
		dto.setLastTime(p.getUpdatedAt());
		dto.setSkuId(p.getSkuId());
		dto.setSupplierPrice(p.getPrice()==null?"0":""+p.getPrice());
		dto.setSpuId(p.getProductId());
		dto.setSaleCurrency("EUR");
		dto.setProductDescription(p.getDescription());
		dto.setStock(p.getStock().toString());
		if(p.getScalars()!=null && p.getScalars().size()>0)
			dto.setProductSize(p.getScalars().entrySet().iterator().next().getValue());
		else if(p.getSkuId().lastIndexOf("#")>0){//用于经过尺码拆分后的新产品
			int idx=p.getSkuId().lastIndexOf("#");
			dto.setProductSize(p.getSkuId().substring(idx+1));
		}
		return dto;
	}
	
	public static SpuDTO product2spu(ColtortiProduct p){
		SpuDTO dto = new SpuDTO();
		dto.setId(UUIDGenerator.getUUID());
		dto.setSpuId(p.getProductId());
		if(p.getBrand()!=null){
			Entry<String, String> entry=p.getBrand().entrySet().iterator().next();
			String brand=entry.getValue();
			dto.setBrandName(brand);
			dto.setBrandId(entry.getKey());
		}
		if(p.getFamily()!=null){
			Entry<String, String> entry=p.getFamily().entrySet().iterator().next();
			dto.setCategoryGender(entry.getValue());
		}
		dto.setCategoryName(p.getCategory());
		dto.setSubCategoryName(p.getSubCategory());
		dto.setLastTime(p.getUpdatedAt());
		dto.setSpuName(p.getName());
		if(p.getSeason()!=null && p.getSeason().size()>0){
			Entry<String,String> entry=p.getSeason().entrySet().iterator().next();
			dto.setSeasonName(entry.getValue());
			dto.setSeasonId(entry.getKey());
		}
		dto.setSupplierId(supplier);
		dto.setProductOrigin(p.getMadIn());
		dto.setMaterial(p.getMaterial());
		return dto;
	}
	
	public static Set<ProductPictureDTO> productPic(ColtortiProduct p){
		List<List<String>> imgurls=p.getImages();
		Set<ProductPictureDTO> ppc = new HashSet<>(imgurls.size());
		for (List<String> list : imgurls) {
			for (String string : list) {
				ProductPictureDTO pc= new ProductPictureDTO();
				pc.setId(UUIDGenerator.getUUID());
				pc.setSkuId(p.getSkuId());pc.setSupplierId(supplier);
				pc.setPicUrl(string);
				ppc.add(pc);
			}
		}
		return ppc;
	}
}

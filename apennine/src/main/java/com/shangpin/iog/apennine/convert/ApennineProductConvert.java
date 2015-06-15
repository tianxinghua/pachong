package com.shangpin.iog.apennine.convert;

import java.util.ArrayList;
import java.util.List;

import com.shangpin.iog.apennine.domain.ApennineProductDTO;
import com.shangpin.iog.apennine.domain.ApennineProductPictureDTO;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;

public class ApennineProductConvert {
	public static SkuDTO product2sku(ApennineProductDTO dto){
		 SkuDTO skuDTO=new SkuDTO();
         skuDTO.setId(UUIDGenerator.getUUID());
         skuDTO.setProductCode(dto.getScode());
         skuDTO.setProductSize(dto.getSize());
         skuDTO.setColor(dto.getColor());
         skuDTO.setProductName(dto.getCdescript());
         skuDTO.setSupplierPrice(dto.getPricec());
         skuDTO.setSkuId(dto.getScode());
         skuDTO.setSpuId(dto.getScode());
         skuDTO.setSupplierId("00000003");
         return skuDTO;
	}
	public static SpuDTO product2spu(ApennineProductDTO dto){
		 SpuDTO spuDTO=new SpuDTO();
         spuDTO.setId(UUIDGenerator.getUUID());
         spuDTO.setSeasonId(dto.getCat1());
         spuDTO.setBrandName(dto.getCat());
         spuDTO.setSpuId(dto.getScode());
         spuDTO.setSupplierId("00000003");
         spuDTO.setCategoryName(dto.getCat2());
         return spuDTO;
	}
}

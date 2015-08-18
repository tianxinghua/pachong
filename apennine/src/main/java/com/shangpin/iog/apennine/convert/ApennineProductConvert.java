package com.shangpin.iog.apennine.convert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.shangpin.iog.apennine.domain.ApennineProductDTO;
import com.shangpin.iog.apennine.domain.ApennineProductPictureDTO;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;

public class ApennineProductConvert {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SkuDTO product2sku(ApennineProductDTO dto) throws ParseException {
		 SkuDTO skuDTO=new SkuDTO();
         Date time = sdf.parse(dto.getCreatedate().replace("T"," "));
         Timestamp createTime = new Timestamp(time.getTime());
         skuDTO.setId(UUIDGenerator.getUUID());
         skuDTO.setProductCode(dto.getScode());
         skuDTO.setProductSize(dto.getSize());
         skuDTO.setColor(dto.getColor());
         skuDTO.setProductName(dto.getCdescript());
         skuDTO.setSupplierPrice(dto.getPricec());
         skuDTO.setSkuId(dto.getScode()+"-"+dto.getSize()+"-"+dto.getColor());
         skuDTO.setSpuId(dto.getScode());
         skuDTO.setCreateTime(createTime);
         skuDTO.setSupplierId("2015070701319");
         return skuDTO;
	}
	public static SpuDTO product2spu(ApennineProductDTO dto) throws ParseException {
		 SpuDTO spuDTO=new SpuDTO();
         Date time = sdf.parse(dto.getCreatedate().replace("T", " "));
         Timestamp createTime = new Timestamp(time.getTime());
         spuDTO.setId(UUIDGenerator.getUUID());
         spuDTO.setSeasonId(dto.getCat1());
         spuDTO.setBrandName(dto.getCat());
         spuDTO.setSpuId(dto.getScode());
         spuDTO.setCreateTime(createTime);
         spuDTO.setSupplierId("2015070701319");
         spuDTO.setCategoryName(dto.getCat2());
         return spuDTO;
	}
}

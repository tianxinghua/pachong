package com.shangpin.supplier.product.consumer.supplier.coltorti.convert;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.supplier.product.consumer.supplier.coltorti.dto.ColtortiProduct;

/**
 * <p>Title:ColtortiProductConvert </p>
 * <p>Description: 供应商coltorti原始数据到hub数据的转换 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2017年1月9日 下午12:00:35
 *
 */
public class ColtortiProductConvert {
	
	/**
	 * 将原始数据转化成HubSupplierSkuDto对象
	 * @param supplierId 供应商门户编号
	 * @param p 供应商原始数据反序列化之后对应的对象
	 * @return
	 */
	public static HubSupplierSkuDto product2sku(String supplierId,ColtortiProduct p){
		HubSupplierSkuDto dto = new HubSupplierSkuDto();
		dto.setSupplierId(supplierId);
		dto.setCreateTime(new Date());
		dto.setSupplierSkuNo(p.getSkuId());
		dto.setMarketPrice(p.getPrice()==null?new BigDecimal(0): new BigDecimal(p.getPrice()));
		if(null!=p.getDiscountRate()){
			dto.setSupplyPrice(dto.getMarketPrice().multiply(new BigDecimal(100-p.getDiscountRate()))
					.divide(new BigDecimal(122),5).setScale(0,BigDecimal.ROUND_HALF_UP));
		}else {
			dto.setSupplyPrice(dto.getMarketPrice().multiply(new BigDecimal(100))
					.divide(new BigDecimal(122),5).setScale(0,BigDecimal.ROUND_HALF_UP));
		}
		dto.setMarketPriceCurrencyorg("EUR");
		dto.setStock(p.getStock()==null ? 0 : p.getStock());
		if(p.getScalars()!=null && p.getScalars().size()>0)
			dto.setSupplierSkuSize(p.getScalars().entrySet().iterator().next().getValue());
		else if(StringUtils.isNotEmpty(p.getSizeKeyValue())){//用于经过尺码拆分后的新产品
			int idx=p.getSizeKeyValue().lastIndexOf("#");
			dto.setSupplierSkuSize(p.getSizeKeyValue().substring(idx+1));
		}
		return dto;
	}
	/**
	 * 将原始数据转化成HubSupplierSpuDto对象
	 * @param supplierId 供应商门户编号
	 * @param p 供应商原始数据反序列化之后对应的对象
	 * @return
	 */
	public static HubSupplierSpuDto product2spu(String supplierId,ColtortiProduct p,String data){
		HubSupplierSpuDto dto = new HubSupplierSpuDto();
		dto.setSupplierSpuNo(p.getProductId());
		if(p.getBrand()!=null){
			Entry<String, String> entry=p.getBrand().entrySet().iterator().next();
			String brand=entry.getValue();
			dto.setSupplierBrandname(brand);
			dto.setSupplierBrandno(entry.getKey());
		}
		if(p.getFamily()!=null){
			Entry<String, String> entry=p.getFamily().entrySet().iterator().next();
			dto.setSupplierGender(entry.getValue());
		}
		dto.setSupplierCategoryname(p.getCategory());
		dto.setSupplierSpuName(p.getName());
		if(p.getSeason()!=null && p.getSeason().size()>0){
			Entry<String,String> entry=p.getSeason().entrySet().iterator().next();
			dto.setSupplierSeasonname(entry.getValue());
			dto.setSupplierSeasonno(entry.getKey());
		}
		dto.setSupplierId(supplierId);
		dto.setSupplierOrigin(p.getMadIn());
		dto.setSupplierMaterial(p.getMaterial());
		dto.setSupplierSpuModel(p.getProductCode());
		dto.setSupplierSpuColor(p.getColor()); 
		dto.setSupplierSpuDesc(p.getDescription());
		dto.setMemo(data);
		return dto;
	}
	/**
	 * 转化图片
	 * @param p
	 * @return
	 */
	public static List<Image> productPic(ColtortiProduct p){
		List<String> imgurls=p.getImages();
		if(null==imgurls) return null;
		List<Image> ppc = new ArrayList<Image>(imgurls.size());
		for (String picUrl : imgurls) {
			Image pc= new Image();
			pc.setUrl(picUrl);
			ppc.add(pc);
		}
		return ppc;
	}
}

package com.shangpin.supplier.product.consumer.supplier.coltortiv2.convert;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.supplier.product.consumer.supplier.coltortiv2.dto.Attribute;
import com.shangpin.supplier.product.consumer.supplier.coltortiv2.dto.ColtortiProduct;
import com.shangpin.supplier.product.consumer.supplier.coltortiv2.dto.ColtortiSkuDto;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;

/**
 * <p>Title:ColtortiProductConvert </p>
 * <p>Description: 供应商coltorti原始数据到hub数据的转换 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2017年1月9日 下午12:00:35
 *
 */
public class ColtortiProductConvert {
	
	private static Gson gson = new Gson();
	
	/**
	 * 将原始数据转化成HubSupplierSkuDto对象
	 * @param supplierId 供应商门户编号
	 * @param p 供应商原始数据反序列化之后对应的对象
	 * @return
	 */
	public static List<HubSupplierSkuDto> product2sku(String supplierId,ColtortiProduct p){
		List<ColtortiSkuDto> lists = p.getColtortiSkus();
		if(CollectionUtils.isNotEmpty(lists)){
			List<HubSupplierSkuDto> returnList = Lists.newArrayList();
			for(ColtortiSkuDto supplierDto : lists){
				HubSupplierSkuDto dto = new HubSupplierSkuDto();
				dto.setSupplierId(supplierId);
				dto.setCreateTime(new Date());
				dto.setMarketPrice(p.getRetail_price()==null?new BigDecimal(0): new BigDecimal(p.getRetail_price()));
				dto.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(p.getDiscount().getYour_price())));
				dto.setMarketPriceCurrencyorg("EUR");
				dto.setStock(supplierDto.getStock());
				dto.setSupplierSkuSize(supplierDto.getSize());
				dto.setSupplierSkuNo(p.getSupplierSpuNo()+"#"+supplierDto.getSizeId());
				returnList.add(dto);
			}
			return returnList;
		}
		return null;
	}
	/**
	 * 将原始数据转化成HubSupplierSpuDto对象
	 * @param supplierId 供应商门户编号
	 * @param p 供应商原始数据反序列化之后对应的对象
	 * @return
	 */
	public static HubSupplierSpuDto product2spu(String supplierId,ColtortiProduct p){
		HubSupplierSpuDto dto = new HubSupplierSpuDto();
		dto.setSupplierSpuNo(p.getSupplierSpuNo());
		dto.setSupplierBrandname(p.getBrandName());
		dto.setSupplierBrandno(p.getBrand_id());
		dto.setSupplierGender(p.getGenderName());
		dto.setSupplierCategoryname(p.getCategoryName());
		dto.setSupplierSpuName(p.getName().getEn());
		dto.setSupplierSeasonname(p.getSeasonName());
		dto.setSupplierSeasonno(p.getSeason_id());
		dto.setSupplierId(supplierId);
		dto.setSupplierSpuColor(p.getVariation_name()); 
		dto.setSupplierSpuDesc(p.getDescription().getEn());
		setOtherAttributes(p, dto);
		List<String> others = p.getOther_ids();
		if(CollectionUtils.isNotEmpty(others)){
			for(String id : others){
				if(!id.equals(p.getProduct_id())){
					dto.setSupplierSpuModel(id + " "+p.getVariation_id());
					break;
				}
			}
		}
		
		return dto;
	}
	/**
	 * 转化图片
	 * @param p
	 * @return
	 */
	public static List<Image> productPic(ColtortiProduct p){
		List<String> imgurls = p.getImages();
		if (null == imgurls)
			return null;
		List<Image> ppc = new ArrayList<Image>(imgurls.size());
		Map<Integer, String> map = new HashMap<Integer, String>();
		int[] a = new int[imgurls.size()];
		for (int i=0; i<imgurls.size();i++) {
			String picUrl = imgurls.get(i);
			if (picUrl != null) {
				String s = picUrl.substring(picUrl.lastIndexOf("-") + 1);
				int num = Integer.parseInt(s.split("\\.")[0]);
				a[i] = num;
				map.put(num, picUrl);
				System.out.println(s);
			}
		}
		Arrays.sort(a); // 进行排序
		for (int num : a) {
			Image pc = new Image();
			pc.setUrl(map.get(num));
			ppc.add(pc);
		}
		return ppc;
	}
	/**
	 * 设置其他属性
	 * @param p
	 * @param dto
	 */
	private static void setOtherAttributes(ColtortiProduct p, HubSupplierSpuDto dto){
		Map<String, Map<String, Object>> attributes = p.getAttributes();
		if(attributes != null && attributes.size() > 0){
			String supplierOrigin = getValues(attributes, "000004");
			dto.setSupplierOrigin(null != supplierOrigin ? supplierOrigin : "" );
			String material = null != getValues(attributes, "000005") ? getValues(attributes, "000005") : getValues(attributes, "000003");
			dto.setSupplierMaterial(null != material ? material : "");
		} 
	}
	
	private static String getValues(Map<String,Map<String,Object>> attributes, String attrKey){
		Map<String, Object> attr=attributes.get(attrKey);
		if(attr==null) return null;
		String values= gson.toJson(attr.get("values"));
		if(!StringUtils.isEmpty(values)){
			Map<String,Attribute> map = gson.fromJson(values, new TypeToken<Map<String,Attribute>>(){}.getType());
			for(Attribute att : map.values()){
				return att.getName().getIt();
			}
		}
		return null;
	}
}

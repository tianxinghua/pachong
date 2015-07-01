/**
 * 
 */
package com.shangpin.iog.ebay.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ebay.soap.eBLBaseComponents.AmountType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.NameValueListArrayType;
import com.ebay.soap.eBLBaseComponents.NameValueListType;
import com.ebay.soap.eBLBaseComponents.PictureDetailsType;
import com.ebay.soap.eBLBaseComponents.VariationProductListingDetailsType;
import com.ebay.soap.eBLBaseComponents.VariationType;
import com.ebay.soap.eBLBaseComponents.VariationsType;
import com.shangpin.ebay.shoping.SimpleItemType;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.ebay.conf.EbayConf;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月29日
 */
public class TradeItemConvert {


	/**
	 * @param tps
	 * @param userType 
	 * @return
	 */
	public static Map<String, ? extends Collection<? extends Object>> convert2SKuAndSpu(ItemType[] tps, String userType) {
		Map<String,List<?>> sksp=new HashMap<>();
		List<SkuDTO> skus = new ArrayList<>();
		List<SpuDTO> spus = new ArrayList<>();
		List<ProductPictureDTO> pictures = new ArrayList<>();
		for (int i = 0; i < tps.length; i++) {
			List<SkuDTO> skuts=toSku(tps[i],userType);
			skus.addAll(skuts);
			spus.add(toSpu(tps[i]));
			List<ProductPictureDTO> pics=toPic(tps[i],userType);
			pictures.addAll(pics);
		}
		sksp.put("pic", pictures);
		sksp.put("sku", skus);
		sksp.put("spu", spus);
		return sksp;
	}

	/**
	 * @param itemType
	 * @param userType 
	 * @return
	 */
	public static List<SkuDTO> toSku(ItemType itemType, String userType) {
		List<SkuDTO> skus=new ArrayList<>();
		VariationsType vst=itemType.getVariations();
		if( vst!= null && vst.getVariation()!=null && vst.getVariation().length>0){
			SkuDTO sku = new SkuDTO();
			setCommon(sku,itemType,userType);
			VariationType[] vr=vst.getVariation();
			for (int i = 0; i < vr.length; i++) {
				String skstr=getSkuIdStr(vr[i],itemType);//以#号分隔itemId,skuId 默认itemId
				sku.setSkuId(skstr);
				setVariationPrice(sku, vr[i]);//设置金额
				setStock(sku,vr[i]);//设置库存
				setSKUAttr(sku,vr[i].getVariationSpecifics());//设置一些属性,条码等
				if(sku.getProductCode()==null){//设置产品条码
					VariationProductListingDetailsType vpd=vr[i].getVariationProductListingDetails();
					if(vpd!=null){
						String upc=vpd.getUPC();
						String ean=vpd.getEAN();String isbn=vpd.getISBN();
						String code=(upc==null?(ean==null?(isbn==null?null:isbn):ean):upc);
						sku.setProductCode(code);sku.setBarcode(code);
					}			
				}
			}
			skus.add(sku);
		}else{
			SkuDTO sku = new SkuDTO();
			setCommon(sku,itemType,userType);
			setSKUAttr(sku, itemType.getItemSpecifics());
			skus.add(sku);
		}
		return skus;
	}

	/**
	 * @param itemType
	 * @param userType
	 * @return 
	 */
	private static List<ProductPictureDTO> toPic(ItemType itemType, String userType) {
		PictureDetailsType pdt=itemType.getPictureDetails();
		List<ProductPictureDTO> pics = new ArrayList<>();
		if(pdt!=null){
			String[] urls=pdt.getPictureURL();
			for (String string : urls) {
				ProductPictureDTO pic = new ProductPictureDTO();
				pic.setPicUrl(string);
				pic.setSupplierId(EbayConf.EBAY+userType);
			}
		}
		return pics;
	}

	/**
	 * @param itemType
	 * @return 
	 */
	private static SpuDTO toSpu(ItemType itemType) {
		SpuDTO spu = new SpuDTO();
		setCategory(spu,itemType);
		setSpuAttr(spu,itemType.getItemSpecifics());
		return spu;
	}

	/**
	 * TODO spu属性，需要获取item的ItemSpecifics
	 * @param spu
	 * @param itemType
	 */
	private static void setSpuAttr(SpuDTO spu, NameValueListArrayType itemSpecifics) {
		if(itemSpecifics==null)
			return ;
		NameValueListType[] nvat=itemSpecifics.getNameValueList();
		for (NameValueListType nv : nvat) {
			String nvName=nv.getName().toLowerCase();
			if(nvName.contains("brand")){
				spu.setBrandName(nvName);
				continue;
			}
			if(nvName.contains("manufacturer")){
				spu.setProductOrigin(nvName);
				continue;
			}
			if(nvName.contains("material")){
				spu.setMaterial(nvName);
				continue;
			}
		}
	}

	/**
	 * TODO 设置sku属性，这些怕是需要再次拉取数据
	 * @param sku
	 * @param vr
	 */
	private static void setSKUAttr(SkuDTO sku, NameValueListArrayType variationSpecifics) {
		NameValueListType[] nvList=variationSpecifics.getNameValueList();
		for (NameValueListType nv : nvList) {
			String vname=nv.getName().toUpperCase();
			if(vname.equalsIgnoreCase("UPC")||vname.equalsIgnoreCase("EAN")||vname.equalsIgnoreCase("ISBN")){
				sku.setProductCode(nv.getValue(0));
				sku.setBarcode(nv.getValue(0));
				continue;
			}
			if(vname.contains("SIZE")){
				sku.setProductSize(nv.getValue(0));
				continue;
			}
			if(vname.contains("COLOR")){
				sku.setColor(nv.getValue(0));
			}
		}
	}

	/**
	 * @param spu
	 * @param itemType
	 */
	private static void setCategory(SpuDTO spu, ItemType itemType) {
		String title=itemType.getTitle().toLowerCase();
		if(title.contains("women")||title.contains("femal")){
			spu.setCategoryGender("F");
		}else if(title.contains("man")||title.contains("male")){
			spu.setCategoryGender("M");
		}
		spu.setCategoryId(itemType.getPrimaryCategory().getCategoryID());
		spu.setCategoryName(itemType.getPrimaryCategory().getCategoryName());
		if(itemType.getSecondaryCategory()!=null){
			spu.setSubCategoryName(itemType.getSecondaryCategory().getCategoryName());
			spu.setSubCategoryId(itemType.getSecondaryCategory().getCategoryID());
		}
	}

	/**
	 * @param sku
	 * @param itemType
	 * @param userId 
	 */
	private static void setCommon(SkuDTO sku, ItemType itemType, String userId) {
		sku.setProductName(itemType.getTitle());
		sku.setSupplierId(EbayConf.EBAY+userId);
		sku.setSupplierPrice(""+itemType.getSellingStatus().getCurrentPrice().getValue());
		sku.setSalePrice(""+itemType.getSellingStatus().getCurrentPrice().getValue());
		sku.setSaleCurrency(itemType.getSellingStatus().getCurrentPrice().getCurrencyID().value());
		sku.setStock(""+(itemType.getQuantity()-itemType.getSellingStatus().getQuantitySold()));
		sku.setCreateTime(new Date());
		sku.setLastTime(itemType.getListingDetails().getEndTime().getTime());
		//TODO 
		sku.setSpuId(itemType.getItemID());
	}

	/**
	 * @param vt nullable
	 * @param itemType 
	 * @return
	 */
	private static String getSkuIdStr(VariationType vt, ItemType itemType) {
		if(vt==null)
			return itemType.getItemID();
		String skst =vt.getSKU();
		//itemType 还有一个sku
		if(StringUtils.isNotBlank(skst))
			return itemType.getItemID()+"#"+skst;
		return itemType.getItemID();
	}

	/**
	 * 设置变种库存
	 * @param sku
	 * @param vt
	 */
	private static void setStock(SkuDTO sku, VariationType vt) {
		sku.setStock(""+(vt.getQuantity()-vt.getSellingStatus().getQuantitySold()));
	}

	/** 设置变种价格
	 * @param sku
	 * @param vt
	 */
	private static void setVariationPrice(SkuDTO sku,
			VariationType vt) {
		AmountType amt=null;
		if(vt.getSellingStatus()!=null)
			amt=vt.getSellingStatus().getCurrentPrice();
		if(amt==null)
			amt=vt.getStartPrice();
		sku.setSalePrice(amt.getValue()+"");
		sku.setSaleCurrency(amt.getCurrencyID().value());
	}



}

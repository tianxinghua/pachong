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
import com.ebay.soap.eBLBaseComponents.DiscountPriceInfoType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.NameValueListArrayType;
import com.ebay.soap.eBLBaseComponents.NameValueListType;
import com.ebay.soap.eBLBaseComponents.PictureDetailsType;
import com.ebay.soap.eBLBaseComponents.VariationProductListingDetailsType;
import com.ebay.soap.eBLBaseComponents.VariationType;
import com.ebay.soap.eBLBaseComponents.VariationsType;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.ebay.conf.EbayInit;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月29日
 */
public class TradeItemConvert {


	/**
	 * @param tps
	 * @param supplierId 
	 * @return
	 */
	public static Map<String, ? extends Collection<? extends Object>> convert2SKuAndSpu(ItemType[] tps, String supplierId) {
		Map<String,List<?>> sksp=new HashMap<>();
		List<SkuDTO> skus = new ArrayList<>();
		List<SpuDTO> spus = new ArrayList<>();
		List<ProductPictureDTO> pictures = new ArrayList<>();
		for (int i = 0; i < tps.length; i++) {
			List<SkuDTO> skuts=toSku(tps[i],supplierId);
			skus.addAll(skuts);
			spus.add(toSpu(tps[i],supplierId));
			List<ProductPictureDTO> pics=toPic(tps[i],supplierId);
			pictures.addAll(pics);
		}
		sksp.put("pic", pictures);
		sksp.put("sku", skus);
		sksp.put("spu", spus);
		return sksp;
	}

	/**
	 * @param itemType
	 * @param supplierId 
	 * @return
	 */
	public static List<SkuDTO> toSku(ItemType itemType, String supplierId) {
		List<SkuDTO> skus=new ArrayList<>();
		VariationsType vst=itemType.getVariations();
		if( vst!= null && vst.getVariation()!=null && vst.getVariation().length>0){
			SkuDTO sku = new SkuDTO();
			setSKUCommon(sku,itemType,supplierId);
			VariationType[] vr=vst.getVariation();
			for (int i = 0; i < vr.length; i++) {
				String skstr=getSkuIdStr(vr[i],itemType);//以#号分隔itemId,skuId 默认itemId
				sku.setSkuId(skstr);
				setVariationPrice(sku, vr[i]);//设置金额
				setStock(sku,vr[i]);//设置库存
				if(vr[i].getVariationSpecifics()!=null)
					setSKUAttr(sku,vr[i].getVariationSpecifics());//设置一些属性,条码等
				if(itemType.getItemSpecifics()!=null){
					//TODO 补救属性
					setItemAttr(sku,itemType.getItemSpecifics().getNameValueList());
				}
				if(sku.getProductCode()==null){//设置产品条码
					VariationProductListingDetailsType vpd=vr[i].getVariationProductListingDetails();
					if(vpd!=null){
						String upc=vpd.getUPC();
						String ean=vpd.getEAN();String isbn=vpd.getISBN();
						String code=(upc==null?(ean==null?(isbn==null?null:isbn):ean):upc);
						sku.setProductCode(code);sku.setBarcode(code);
					}			
				}
				setMarketPrice(vr[i].getDiscountPriceInfo(), sku);
			}
			skus.add(sku);
		}else{
			SkuDTO sku = new SkuDTO();
			setSKUCommon(sku,itemType,supplierId);
			if(itemType.getItemSpecifics()!=null)
				setSKUAttr(sku, itemType.getItemSpecifics());
			setMarketPrice(itemType.getDiscountPriceInfo(), sku);
			skus.add(sku);
		}
		return skus;
	}

	/**
	 * @param sku
	 * @param nvs
	 */
	private static void setItemAttr(SkuDTO sku,
			NameValueListType[] nvs) {
		if(StringUtils.isBlank(sku.getColor())){
			sku.setColor(getNVAttrValue("color",nvs));
		}
		if(StringUtils.isBlank(sku.getProductSize())){
			sku.setProductSize(getNVAttrValue("size",nvs));
		}
		if(StringUtils.isBlank(sku.getBarcode())){
			sku.setBarcode(getNVAttrValue("ean",nvs));
			if(sku.getBarcode()==null){
				sku.setBarcode(getNVAttrValue("upc", nvs));
			}
		}
		if(StringUtils.isBlank(sku.getProductCode())){
			sku.setProductCode(getNVAttrValue("mpn", nvs));
		}
	}
	/**
	 * 获取包含指定属性名的 值<br/>
	 * "Manufacturer Part Number" 也是mpn
	 * @param name 小写的属性名字符串
	 * @param nvs 属性键值对
	 * @return
	 */
	private static String getNVAttrValue(String name, NameValueListType[] nvs) {
		String nName=null;
		for (NameValueListType nv : nvs) {
			nName = nv.getName().toLowerCase();
			if (name.equals("mpn")
					&& (nName.contains(name) || (nName.contains("manufacturer") && nName
							.contains("number"))))
				return nv.getValue(0);			
			if(nName.contains(name))
				return nv.getValue(0);
		}
		return null;
	}
	

	/**
	 * 设置折扣前价格
	 * @param discountPriceInfo
	 * @param sku
	 */
	private static void setMarketPrice(DiscountPriceInfoType discountPriceInfo,
			SkuDTO sku) {
		if(discountPriceInfo!=null){//折扣前的价格
			AmountType amt=discountPriceInfo.getOriginalRetailPrice();
			if(amt!=null){
				sku.setMarketPrice(amt.getValue()+"");
			}
		}
	}

	/**
	 * @param itemType
	 * @param supplierId
	 * @return 
	 */
	private static List<ProductPictureDTO> toPic(ItemType itemType, String supplierId) {
		PictureDetailsType pdt=itemType.getPictureDetails();
		List<ProductPictureDTO> pics = new ArrayList<>();
		if(pdt!=null){
			String[] urls=pdt.getPictureURL();
			for (String string : urls) {
				ProductPictureDTO pic = new ProductPictureDTO();
				pic.setPicUrl(string);
				pic.setSupplierId(EbayInit.EBAY);
			}
		}
		return pics;
	}

	/**
	 * @param itemType
	 * @param supplierId 
	 * @return 
	 */
	private static SpuDTO toSpu(ItemType itemType, String supplierId) {
		SpuDTO spu = new SpuDTO();
		spu.setId(UUIDGenerator.getUUID());
		spu.setSpuId(itemType.getItemID());
		spu.setSupplierId(EbayInit.EBAY);
		spu.setSpuName(itemType.getTitle());
		setCategory(spu,itemType);
		if(itemType.getItemSpecifics()!=null)
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
		String nvName;
		for (NameValueListType nv : nvat) {
			nvName=nv.getName().toLowerCase();
			if(nvName.contains("brand")){
				spu.setBrandName(nvName);
				continue;
			}
			if(nvName.contains("manufacturer") && !nvName.contains("number")){
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
			String vname=nv.getName().toLowerCase();
			String value = nv.getValue(0);			
			if(vname.equals("mpn")||(vname.contains("manufacturer") && vname
					.contains("number"))||
					vname.equalsIgnoreCase("isbn")){
				sku.setProductCode(value);
				continue;
			}
			if(vname.equalsIgnoreCase("ean")||vname.equalsIgnoreCase("upc")){
				sku.setBarcode(value);
				continue;
			}
			if(vname.contains("size") && sku.getProductSize()==null){
				sku.setProductSize(value);
				continue;
			}
			if(vname.contains("color") && sku.getColor()==null){
				sku.setColor(value);
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
		if(itemType.getPrimaryCategory()!=null){
			spu.setCategoryId(itemType.getPrimaryCategory().getCategoryID());
			spu.setCategoryName(itemType.getPrimaryCategory().getCategoryName());			
		}
		if(itemType.getSecondaryCategory()!=null){
			spu.setSubCategoryName(itemType.getSecondaryCategory().getCategoryName());
			spu.setSubCategoryId(itemType.getSecondaryCategory().getCategoryID());
		}
	}

	/**
	 * 设置，产品名称、价格，库存，endTime
	 * @param sku
	 * @param itemType
	 * @param userId 
	 */
	private static void setSKUCommon(SkuDTO sku, ItemType itemType, String userId) {
		sku.setProductName(itemType.getTitle());
		sku.setSupplierId(EbayInit.EBAY);
		sku.setMemo("storeName:"+userId);
		sku.setSupplierPrice(""+itemType.getSellingStatus().getCurrentPrice().getValue());
		sku.setSalePrice(""+itemType.getSellingStatus().getCurrentPrice().getValue());
		sku.setSaleCurrency(itemType.getSellingStatus().getCurrentPrice().getCurrencyID().value());
		sku.setStock(""+(itemType.getQuantity()-itemType.getSellingStatus().getQuantitySold()));
		sku.setCreateTime(new Date());
		if(itemType.getListingDetails()!=null)
			sku.setLastTime(itemType.getListingDetails().getEndTime().getTime());
		//TODO 
		sku.setSpuId(itemType.getItemID());
	}

	/**
	 * 设置skuId，无变种的就是sit的itemId<br/>
	 * 有变种的则是:itemId#变种sku;<br/>
	 * 若是没有变种sku,则是变种属性键值对如下：<br/>
	 * itemId#color:blue@size:m@width:medium<br/>
	 * 注意：如果变种sku没有，那么就是：itemId#（会导致重复，覆盖）<br/>
	 * @param itemType trading接口拉取的的item
	 * @param vt item变种数据  nullable
	 * @return 变换的skuId
	 */
	public static String getSkuIdStr(VariationType vt, ItemType itemType) {
		if(vt!=null){
			if(vt.getSKU()==null){
				StringBuffer skuId=new StringBuffer();
				NameValueListType[] skuNvs=vt.getVariationSpecifics().getNameValueList();
				NameValueListType[] itemNvs=itemType.getVariations().getVariationSpecificsSet().getNameValueList();
				for (NameValueListType itemNv : itemNvs) {//item所有的可选项
					for (NameValueListType skuNv : skuNvs) {
						if(itemNv.getName().equals(skuNv.getName())){//变种的值
							skuId.append(itemNv.getName()).append(":").append(skuNv.getValue(0)).append("@");//键值
							break;
						}
					}
				}
				return itemType.getItemID()+"#"+skuId.toString();
			}
			return itemType.getItemID()+"#"+vt.getSKU();
		}
		else
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

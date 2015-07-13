/**
 * 
 */
package com.shangpin.iog.ebay.convert;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.shangpin.ebay.shoping.NameValueListArrayType;
import com.shangpin.ebay.shoping.NameValueListType;
import com.shangpin.ebay.shoping.PicturesType;
import com.shangpin.ebay.shoping.SimpleItemType;
import com.shangpin.ebay.shoping.VariationSpecificPictureSetType;
import com.shangpin.ebay.shoping.VariationType;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.ebay.conf.EbayConf;

/**
 * 根据item的id，调用shopping的GetMultipleItems返回来的结果组装sku,spu,pic
 * @description 
 * @author 陈小峰
 * <br/>2015年7月1日
 */
public class ShopingItemConvert {

	/**
	 * 转换sku，spu,pic
	 * @param itemTypes
	 * @param supplerKey
	 * @return
	 */
	/*sit.getItemSpecifics();//for spu || sku attr
	sit.getVariations();//for sku 
	sit.getQuantity();//for sku item quantity
	sit.getQuantitySold();//for sku item quantity
	sit.getPictureURLArray();//for pic
	sit.getVariations().getPicturesArray();//for pic
	sit.getVariations().getVariationArray(0).getVariationSpecifics();//for sku attr
	sit.getVariations().getVariationArray(0).getQuantity();//for sku quantity
	sit.getVariations().getVariationSpecificsSet().getNameValueListArray();//for sku varia
	 */	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String,  Collection> convert2kpp(
			SimpleItemType[] itemTypes, String supplerKey) {
		Map<String,  Collection> map=new HashMap<String, Collection>();
		Set<ProductPictureDTO> rtnPic=new HashSet<>(itemTypes.length*2);
		Set<SkuDTO> rtnSku=new HashSet<>(itemTypes.length);
		Set<SpuDTO> rtnSpu=new HashSet<>(itemTypes.length);
		for (SimpleItemType sit : itemTypes) {
			Object[] obj=convertSku(supplerKey,sit);
			rtnSku.addAll((Set<SkuDTO>)obj[0]);
			rtnPic.addAll((Set<ProductPictureDTO>)obj[1]);
			SpuDTO spu = convertSpu(sit,supplerKey);
			rtnSpu.add(spu);
		}
		map.put("sku",rtnSku);
		map.put("spu",rtnSpu);
		map.put("pic",rtnPic);
		return map;
	}

	/**
	 * 转换spu
	 * @param sit
	 * @param supplierKey
	 * @return 
	 */
	private static SpuDTO convertSpu(SimpleItemType sit, String supplierKey) {
		SpuDTO spu = new SpuDTO();
		setSpuCategory(sit, spu);
		spu.setId(UUIDGenerator.getUUID());
		spu.setSpuId(sit.getItemID());
		NameValueListArrayType nv=sit.getItemSpecifics();
		setSpuAttr(spu,nv.getNameValueListArray());
		spu.setSupplierId(EbayConf.EBAY+supplierKey);
		spu.setSpuName(sit.getTitle());
		return spu;
	}

	/**
	 * 设置1，2级分类，性别分类
	 * @param sit
	 * @param spu
	 */
	private static void setSpuCategory(SimpleItemType sit, SpuDTO spu) {
		String title=sit.getTitle();
		spu.setCategoryId(sit.getPrimaryCategoryID());
		spu.setCategoryName(sit.getPrimaryCategoryName());
		spu.setSubCategoryId(sit.getSecondaryCategoryID());
		spu.setSubCategoryName(sit.getSecondaryCategoryName());
		if(title.contains("women")||title.contains("femal")){
			spu.setCategoryGender("F");
		}else if(title.contains("man")||title.contains("male")){
			spu.setCategoryGender("M");
		}
	}

	/**
	 * 设置品牌，材质，制造地，季节
	 * @param spu
	 * @param nameValueListArray
	 */
	private static void setSpuAttr(SpuDTO spu,
			NameValueListType[] nameValueListArray) {
		for (NameValueListType nv : nameValueListArray) {
			String name=nv.getName().toLowerCase();
			String value=nv.getValueArray(0);
			if(name.contains("brand")){
				spu.setBrandName(value);continue;
			}
			if(name.contains("material")){
				spu.setMaterial(value);continue;
			}
			if(name.contains("manufacturer")){
				spu.setProductOrigin(value);continue;
			}
			if(name.contains("season")){
				spu.setSeasonName(value); continue;
			}
		}
		/*spu.setBrandId(brandId);spu.setBrandName(brandName);
		spu.setMaterial(material);spu.setPicUrl(picUrl);
		spu.setProductOrigin(productOrigin);*/
	}

	/**
	 * 转换sku
	 * @param userId
	 * @param createDate
	 * @param sit
	 * @return 第一个是sku的set,第二个是pic的set
	 */
	private static Object[] convertSku(String userId,SimpleItemType sit) {
		 Date createDate = new Date();
		Set<ProductPictureDTO> rtnPic=new HashSet<>();
		Set<SkuDTO> rtnSku=new HashSet<>();
		Object[] picAndSku=new Object[]{rtnSku,rtnPic};
		if(sit.getVariations()!=null && sit.getVariations().getVariationArray()!=null){
			VariationType[] vrts=sit.getVariations().getVariationArray();
			for (VariationType vt : vrts) {
				SkuDTO sku = new SkuDTO();
				setSkuCommon(userId, createDate, sit, sku);
				sku.setStock(""+(vt.getQuantity()-vt.getSellingStatus().getQuantitySold()));
				sku.setSaleCurrency(vt.getStartPrice().getCurrencyID().toString());
				sku.setSalePrice(""+vt.getStartPrice().getDoubleValue());
				sku.setSupplierPrice(sku.getSalePrice());
				setSkuAtt(sku,sit.getItemSpecifics().getNameValueListArray());
				String skuId=getSkuId(sit,vt);
				sku.setSkuId(skuId);
				//sit.getVariations().getPicturesArray();//for pic
				getVariationPic(sit.getVariations().getPicturesArray(),rtnPic,skuId,sku.getSupplierId());
				rtnSku.add(sku);
			}
		}else{
			SkuDTO sku = new SkuDTO();
			setSkuCommon(userId, createDate, sit, sku);
			sku.setStock(""+(sit.getQuantity()-sit.getQuantitySold()));
			sku.setSalePrice(""+sit.getCurrentPrice().getDoubleValue());
			sku.setSaleCurrency(sit.getCurrentPrice().getCurrencyID().toString());
			sku.setSupplierPrice(sku.getSalePrice());
			sku.setSkuId(getSkuId(sit,null));
			//sit.getPictureURLArray();
			url2Pic(sit.getItemID(), sku.getSupplierId(), rtnPic, sit.getPictureURLArray());
			rtnSku.add(sku);
		}
		return picAndSku;
	}

	/**
	 * 设置skuId，无变种的就是sit的itemId<br/>
	 * 有变种的则是:itemId#变种sku;<br/>
	 * 注意：如果变种sku没有，那么就是：itemId#（会导致重复，覆盖）<br/>
	 * @param sit shopping接口拉取的的item
	 * @param vt item变种数据  nullable
	 * @return 变换的skuId
	 */
	public static String getSkuId(SimpleItemType sit, VariationType vt) {
		if(vt!=null)
			return sit.getItemID()+"#"+(vt.getSKU()==null?"":vt.getSKU());
		else 
			return sit.getItemID();
	}

	/**
	 * 
	 * @param picturesTypes
	 * @param rtnPic
	 * @param skuId
	 * @param supperlierId
	 */
	private static void getVariationPic(PicturesType[] picturesTypes,Set<ProductPictureDTO> rtnPic, String skuId,String supperlierId) {
		if(picturesTypes!=null && picturesTypes.length>0){
			for (PicturesType picturesType : picturesTypes) {//颜色图片、尺码图片等
				VariationSpecificPictureSetType[] vsps=picturesType.getVariationSpecificPictureSetArray();
				for (VariationSpecificPictureSetType vsp : vsps) {//不同颜色值，尺码值的图片
					String[] urls=vsp.getPictureURLArray();
					url2Pic(skuId, supperlierId, rtnPic, urls);
				}			
			}
		}
	}

	/**
	 * @param skuId
	 * @param supperlierId
	 * @param set
	 * @param urls
	 */
	private static void url2Pic(String skuId, String supperlierId,
			Set<ProductPictureDTO> set, String[] urls) {
		for (String url : urls) {//每个图片url
			ProductPictureDTO pic=new ProductPictureDTO();
			pic.setId(UUIDGenerator.getUUID());
			pic.setPicUrl(url);
			pic.setSkuId(skuId);
			pic.setSupplierId(supperlierId);
			set.add(pic);
		}
	}

	/**
	 * 设置 size，color,货号，条形码
	 * @param sku
	 * @param nameValueListArray
	 */
	private static void setSkuAtt(SkuDTO sku,
			NameValueListType[] nameValueListArray) {
		for (NameValueListType nv : nameValueListArray) {
			String name=nv.getName().toUpperCase();
			String value = nv.getValueArray(0);
			if(name.contains("COLOR") && sku.getColor()!=null){
				sku.setColor(value);
				continue;
			}
			if(name.equalsIgnoreCase("UPC")||name.equalsIgnoreCase("EAN")
					||name.equalsIgnoreCase("ISBN")||name.equals("MPN")||name.equals("Manufacturer Part Number")){
				sku.setProductCode(value);
				sku.setBarcode(value);
				continue;
			}
			if(name.contains("SIZE")){
				sku.setProductSize(value);
				continue;
			}
		}
		//sku.setColor(color);
		//sku.setBarcode(barcode);
		//sku.setProductCode(productCode);
	}

	/**
	 * 设置sku的spuid,productName,supplierid,id,createTime,
	 * @param userId
	 * @param createDate
	 * @param sit
	 * @param sku
	 */
	private static void setSkuCommon(String userId, Date createDate,
			SimpleItemType sit, SkuDTO sku) {
		sku.setProductName(sit.getTitle());
		sku.setSpuId(sit.getItemID());
		sku.setId(UUIDGenerator.getUUID());
		sku.setCreateTime(createDate);
		sku.setSupplierId(EbayConf.EBAY+userId);
		sku.setLastTime(sit.getEndTime().getTime());
	}
}

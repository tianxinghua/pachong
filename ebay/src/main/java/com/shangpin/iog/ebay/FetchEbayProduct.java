package com.shangpin.iog.ebay;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.shangpin.ebay.shoping.NameValueListType;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.shangpin.ebay.finding.FindItemsIneBayStoresResponse;
import com.shangpin.ebay.finding.FindItemsIneBayStoresResponseDocument;
import com.shangpin.ebay.finding.SearchItem;
import com.shangpin.ebay.shoping.GetMultipleItemsResponseType;
import com.shangpin.ebay.shoping.NameValueListType;
import com.shangpin.ebay.shoping.SimpleItemType;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.ebay.service.GrabEbayApiService;
import com.shangpin.iog.service.ProductFetchService;

/**
 * Created by huxia on 2015/6/30.
 */
@Component("ebay")
public class FetchEbayProduct {
    final Logger logger = Logger.getLogger(this.getClass());

    public Set<SkuDTO> getSkuDTO() {
        return skuDTO;
    }

    public void setSkuDTO(Set<SkuDTO> skuDTO) {
        this.skuDTO = skuDTO;
    }

    private Set<SkuDTO> skuDTO = new HashSet<SkuDTO>();

    public Set<SpuDTO> getSpuDTO() {
        return spuDTO;
    }

    public void setSpuDTO(Set<SpuDTO> spuDTO) {
        this.spuDTO = spuDTO;
    }

    private Set<SpuDTO> spuDTO = new HashSet<SpuDTO>();

    @Autowired
    ProductFetchService productFetchService;

    public void fetchSkuAndSave(SimpleItemType it, StringBuffer pics) throws Exception {

        ProductPictureDTO productPicture = null;
        SkuDTO sku = null;
        if (it.getEndTime().getTime().after(Calendar.getInstance().getTime())) {
            if (it.getVariations() != null) {
                com.shangpin.ebay.shoping.VariationType[] variationType = it.getVariations().getVariationArray();
                if (variationType != null) {
                    for (com.shangpin.ebay.shoping.VariationType variationtype : variationType) {
                        sku = new SkuDTO();
                        productPicture = new ProductPictureDTO();
                        productPicture.setId(UUIDGenerator.getUUID());
                        productPicture.setSkuId(it.getItemID() + "#" + variationtype.getSKU());
                        productPicture.setSupplierId("2015-071301325");
                        productPicture.setPicUrl(pics.toString());
                        sku.setId(UUIDGenerator.getUUID());
                        sku.setSkuId(it.getItemID() + "#" + variationtype.getSKU());
                        sku.setSupplierId("2015-071301325");
                        sku.setStock(String.valueOf(variationtype.getQuantity() - variationtype.getSellingStatus().getQuantitySold()));
                        sku.setProductName(it.getTitle());
                        com.shangpin.ebay.shoping.AmountType amountType = variationtype.getStartPrice();
                        sku.setSaleCurrency(amountType.getCurrencyID().toString());
                        if (!amountType.getStringValue().equals("")) {
                            sku.setSalePrice(amountType.getStringValue());
                            sku.setSupplierPrice(amountType.getStringValue());
                        } else {
                            sku.setSalePrice(it.getConvertedCurrentPrice().toString());
                            sku.setSupplierPrice(it.getCurrentPrice().toString());
                        }
                        NameValueListType[] nameValueListTypes = variationtype.getVariationSpecifics().getNameValueListArray();
                        for (NameValueListType nameValueListType : nameValueListTypes) {
                            if (nameValueListType.getName().toLowerCase().contains("color")) {
                                sku.setColor(nameValueListType.getValueArray(0));
                            }
                            if (nameValueListType.getName().toLowerCase().contains("size(")
                                    || nameValueListType.getName().trim().toLowerCase().equals("size")
                                    || nameValueListType.getName().toLowerCase().contains("size type")
                                    ||nameValueListType.getName().toLowerCase().contains("length")) {
                                sku.setProductSize(nameValueListType.getValueArray(0));
                            }
                            if (nameValueListType.getName().toLowerCase().contains("upc")
                                    || nameValueListType.getName().toLowerCase().contains("ean")
                                    || nameValueListType.getName().toLowerCase().contains("isbn")) {
                                sku.setBarcode(nameValueListType.getValueArray(0));
                                sku.setProductCode(nameValueListType.getValueArray(0));
                            }
                        }
                        sku.setSpuId(it.getItemID());
                        try {
                            productFetchService.saveSKU(sku);
                            productFetchService.savePictureForMongo(productPicture);
                        }catch(Exception e){
                            if(e instanceof DuplicateKeyException){

                            }else{
                                logger.error("save error",e);
                            }
                        }
                    }
                    skuDTO.add(sku);
                }
            } else {
                sku = new SkuDTO();
                sku.setId(UUIDGenerator.getUUID());
                sku.setSkuId(it.getItemID());
                sku.setSpuId(it.getItemID());
                sku.setSupplierId("2015-071301325");
                sku.setProductName(it.getTitle());
                sku.setStock(String.valueOf(it.getQuantity() - it.getQuantitySold()));
                if (it.getItemSpecifics() != null) {
                    NameValueListType[] nameValueListType = it.getItemSpecifics().getNameValueListArray();
                    if (nameValueListType != null) {
                        for (NameValueListType nameValueList : nameValueListType) {
                            if (nameValueList.getName().toLowerCase().contains("color")) {
                                sku.setColor(nameValueList.getValueArray(0));
                            }
                            if (nameValueList.getName().toLowerCase().contains("size(")
                                    || nameValueList.getName().trim().toLowerCase().equals("size")
                                    || nameValueList.getName().toLowerCase().contains("size type")
                                    ||nameValueList.getName().toLowerCase().contains("length")) {
                                sku.setProductSize(nameValueList.getValueArray(0));
                            }
                            if (nameValueList.getName().toLowerCase().contains("upc")
                                    || nameValueList.getName().toLowerCase().contains("ean")
                                    || nameValueList.getName().toLowerCase().contains("isbn")) {
                                sku.setBarcode(nameValueList.getValueArray(0));
                                sku.setProductCode(nameValueList.getValueArray(0));
                            }
                        }
                    }
                    sku.setSaleCurrency(it.getConvertedCurrentPrice().getCurrencyID().toString());
                    System.out.println("价格:"+it.getConvertedCurrentPrice().getStringValue());
                    sku.setSalePrice(it.getConvertedCurrentPrice().getStringValue());
                    sku.setSupplierPrice(it.getConvertedCurrentPrice().getStringValue());
                    sku.setCreateTime(it.getStartTime().getTime());
                    sku.setLastTime(it.getEndTime().getTime());
                    skuDTO.add(sku);
                    try {
                        productFetchService.saveSKU(sku);
                        productFetchService.savePictureForMongo(productPicture);
                    }catch(Exception e){
                        if(e instanceof DuplicateKeyException){

                        }else{
                            logger.error("save error",e);
                        }
                    }
                }
            }
        }
    }

    public void saveSpu( SearchItem[] type,SimpleItemType[] itemTypes) throws Exception {

        SpuDTO spu = null;
        for (SearchItem t : type) {
            spu = new SpuDTO();
            for (SimpleItemType item : itemTypes) {
                spu.setId(UUIDGenerator.getUUID());
                spu.setSpuId(t.getItemId());
                spu.setSupplierId("2015-071301325");
                spu.setSpuName(t.getTitle());
                if (t.getTitle().toLowerCase().contains("women")
                        || t.getTitle().toLowerCase().contains("female")
                        || t.getTitle().toLowerCase().contains("lady")) {
                    spu.setCategoryGender("Woman");
                } else if (t.getTitle().toLowerCase().contains("men") || t.getTitle().toLowerCase().contains("male")) {
                    spu.setCategoryGender("Man");
                }
                if(StringUtils.isBlank(t.getPrimaryCategory().getCategoryId())){ break;}
                else {
                    spu.setCategoryId(t.getPrimaryCategory().getCategoryId());
                    spu.setCategoryName(t.getPrimaryCategory().getCategoryName());
                }
                //获取二级category
                if (item.getSecondaryCategoryID() != null) {
                    spu.setSubCategoryId(item.getSecondaryCategoryID());
                    spu.setSubCategoryName(item.getSecondaryCategoryName());
                }
                
                //spu.setPicUrl(pics.toString());
                spu.setCreateTime(t.getListingInfo().getStartTime().getTime());
                spu.setLastTime(t.getListingInfo().getEndTime().getTime());
                //判断和获取品牌、材质、产地

                if (item.getItemSpecifics() != null) {
                    com.shangpin.ebay.shoping.NameValueListType[] nameValueListType = item.getItemSpecifics().getNameValueListArray();
                    if (nameValueListType != null) {
                        for (com.shangpin.ebay.shoping.NameValueListType nameValueList : nameValueListType) {
                            if (nameValueList.getName().toLowerCase().contains("brand")) {
                                    spu.setBrandName(nameValueList.getValueArray(0));
                            }
                            if (nameValueList.getName().toLowerCase().contains("material")) {
                                spu.setMaterial(nameValueList.getValueArray(0));
                            }
                            if (nameValueList.getName().toLowerCase().contains("manufacture")) {
                                spu.setProductOrigin(nameValueList.getValueArray(0));
                            }
                        }
                    }
                }
                //获得图片地址
                String[] picUrl = item.getPictureURLArray();
                StringBuffer pics=new StringBuffer();
                for(String pic:picUrl){
                    pics.append(pic);
                }
               try {
                       if(StringUtils.isNotBlank(spu.getBrandName())&&StringUtils.isNotBlank(spu.getCategoryName())) {
                           spuDTO.add(spu);
                           productFetchService.saveSPU(spu);
                           fetchSkuAndSave(item, pics);
                       }
                     // System.out.println("品牌："+spu.getBrandName()+"     "+spu.getCategoryName());
                } catch (DuplicateKeyException e) {
                   // e.printStackTrace();
                }
            }
        }

    }

    public void fetchSpuAndSave(String storeName, String keywords) throws Exception {

        Collection<String> itemIds = new HashSet<>();
        int  pageSize=1;
        FindItemsIneBayStoresResponse rt=null;
        do{
            try {
                rt = GrabEbayApiService.findItemsIneBayStores(storeName,keywords,100,pageSize);
                if (rt.getAck().equals("Failure")) {
                    return;
                } else {
                    if (rt.getSearchResult() != null) {
                        SearchItem[] type = rt.getSearchResult().getItemArray();
                        if (type != null) {
                            GetMultipleItemsResponseType result = null;
                            SimpleItemType[] itemTypes = new SimpleItemType[type.length];
                            SimpleItemType[] itemType = new SimpleItemType[20];
                            int i = 0, j = 0, k = type.length / 20 * 20;
                            //System.out.println("type的值："+type.length);
                            if (type.length < 20) {
                                for (SearchItem t : type) {
                                    itemIds.add(t.getItemId());
                                }
                                result = GrabEbayApiService.shoppingGetMultipleItems(itemIds);
                                itemTypes = result.getItemArray();
                            }
                            for (SearchItem t : type) {
                                itemIds.add(t.getItemId());
                                ++j;
                                if (itemIds.size() == 20) {
                                    result = GrabEbayApiService.shoppingGetMultipleItems(itemIds);
                                    itemType = result.getItemArray();
                                    System.arraycopy(itemType, 0, itemTypes, 20 * i, 20);
                                    itemIds.clear();
                                    i++;
                                    continue;
                                }
                                if (j == k) {
                                    itemIds.clear();
                                    j = 0;
                                    continue;
                                }
                            }
                            if (type.length % 20 != 0) {
                                result = GrabEbayApiService.shoppingGetMultipleItems(itemIds);
                                itemType = result.getItemArray();
                               // System.out.println("k的值："+k);
                                System.arraycopy(itemType, 0, itemTypes, k, type.length % 20);
                            }
                            saveSpu(type,itemTypes);
                        }
                    }
                }
            } catch (XmlException e) {
                e.printStackTrace();
            }
            pageSize++;
        }while(pageSize<=rt.getPaginationOutput().getTotalPages());
    }

}

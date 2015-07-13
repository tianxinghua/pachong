package com.shangpin.iog.ebay;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.ebay.sdk.ApiAccount;
import com.ebay.sdk.ApiCall;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.SdkException;
import com.ebay.sdk.SdkSoapException;
import com.ebay.soap.eBLBaseComponents.AmountType;
import com.ebay.soap.eBLBaseComponents.DetailLevelCodeType;
import com.ebay.soap.eBLBaseComponents.GetItemRequestType;
import com.ebay.soap.eBLBaseComponents.GetItemResponseType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.VariationSpecificPictureSetType;
import com.ebay.soap.eBLBaseComponents.VariationType;
import com.shangpin.ebay.finding.FindItemsIneBayStoresResponse;
import com.shangpin.ebay.finding.FindItemsIneBayStoresResponseDocument;
import com.shangpin.ebay.finding.SearchItem;
import com.shangpin.ebay.shoping.GetMultipleItemsResponseType;
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
    //TODO 如果没实际用途，没有其他扩展用途，移除
    public void fetchSkuAndSave(String itemID) throws Exception {

        ProductPictureDTO productPicture = null;
        SkuDTO sku = null;
        String picUrl = getPicUrl(itemID);
        ItemType it = testGetItem(itemID);
        if (it.getListingDetails().getEndTime().getTime().after(Calendar.getInstance().getTime())) {
            if (it.getVariations() != null) {
                VariationType[] variationType = it.getVariations().getVariation();
                if (variationType != null) {
                    for (VariationType variationtype : variationType) {
                        sku = new SkuDTO();
                        productPicture = new ProductPictureDTO();
                        productPicture.setId(UUIDGenerator.getUUID());
                        productPicture.setSkuId(itemID + "#" + variationtype.getSKU());
                        productPicture.setSupplierId("ebay#" + it.getSeller().getUserID());
                        productPicture.setPicUrl(picUrl);
                        sku.setId(UUIDGenerator.getUUID());
                        sku.setSkuId(itemID + "#" + variationtype.getSKU());
                        sku.setSupplierId("ebay#" + it.getSeller().getUserID());
                        sku.setStock(String.valueOf(variationtype.getQuantity() - variationtype.getSellingStatus().getQuantitySold()));
                        sku.setProductName(it.getTitle());
                        AmountType amountType = variationtype.getStartPrice();
                        sku.setSaleCurrency(amountType.getCurrencyID().toString());
                        if (!String.valueOf(amountType.getValue()).equals("")) {
                            sku.setSalePrice(String.valueOf(amountType.getValue()));
                            sku.setSupplierPrice(String.valueOf(amountType.getValue()));
                        } else {
                            sku.setSalePrice(it.getListingDetails().getConvertedStartPrice().toString());
                            sku.setSupplierPrice(it.getListingDetails().getConvertedStartPrice().toString());
                        }
                        com.ebay.soap.eBLBaseComponents.NameValueListType[] nameValueListTypes = variationtype.getVariationSpecifics().getNameValueList();
                        for (com.ebay.soap.eBLBaseComponents.NameValueListType nameValueListType : nameValueListTypes) {
                            if (nameValueListType.getName().toLowerCase().contains("color")) {
                                sku.setColor(nameValueListType.getValue(0));
                            }
                            if (nameValueListType.getName().toLowerCase().contains("size")) {
                                sku.setProductSize(nameValueListType.getValue(0));
                            }
                            if (nameValueListType.getName().toLowerCase().contains("upc")
                                    || nameValueListType.getName().toLowerCase().contains("ean")
                                    || nameValueListType.getName().toLowerCase().contains("isbn")) {
                                sku.setBarcode(nameValueListType.getValue(0));
                                sku.setProductCode(nameValueListType.getValue(0));
                            }
                        }
                        sku.setSpuId(itemID);
                        productFetchService.saveSKU(sku);
                        productFetchService.savePictureForMongo(productPicture);
                    }
                    skuDTO.add(sku);
                }
            } else {
                sku = new SkuDTO();
                sku.setId(UUIDGenerator.getUUID());
                sku.setSkuId(itemID);
                sku.setSpuId(itemID);
                sku.setSupplierId("ebay#" + it.getSeller().getUserID());
                sku.setProductName(it.getTitle());
                sku.setStock(String.valueOf(it.getQuantity() - it.getSellingStatus().getQuantitySold()));
                if (it.getItemSpecifics() != null) {
                    com.ebay.soap.eBLBaseComponents.NameValueListType[] nameValueListType = it.getItemSpecifics().getNameValueList();
                    if (nameValueListType != null) {
                        for (com.ebay.soap.eBLBaseComponents.NameValueListType nameValueList : nameValueListType) {
                            if (nameValueList.getName().toLowerCase().contains("color")) {
                                sku.setColor(nameValueList.getValue(0));
                            }
                            if (nameValueList.getName().toLowerCase().contains("size(")
                                    || nameValueList.getName().toLowerCase().equals("size")
                                    || nameValueList.getName().toLowerCase().contains("size type")) {
                                sku.setProductSize(nameValueList.getValue(0));
                            }
                            if (nameValueList.getName().toLowerCase().contains("upc")
                                    || nameValueList.getName().toLowerCase().contains("ean")
                                    || nameValueList.getName().toLowerCase().contains("isbn")) {
                                sku.setBarcode(nameValueList.getValue(0));
                                sku.setProductCode(nameValueList.getValue(0));
                            }
                        }
                    }
                    sku.setSaleCurrency(it.getListingDetails().getConvertedStartPrice().getCurrencyID().toString());
                    sku.setSalePrice(it.getListingDetails().getConvertedStartPrice().toString());
                    sku.setSupplierPrice(it.getListingDetails().getConvertedStartPrice().toString());
                    sku.setCreateTime(it.getListingDetails().getStartTime().getTime());
                    sku.setLastTime(it.getListingDetails().getEndTime().getTime());
                    skuDTO.add(sku);
                    productFetchService.saveSKU(sku);
                    productFetchService.savePictureForMongo(productPicture);
                }
                //productFetchService.savePictureForMongo(productPicture);
            }
        }
    }
    //TODO 移除，统一从配置类中获取
    private static ApiContext getProApiContext() {
        ApiContext api = new ApiContext();
        String apiUrl = "https://api.ebay.com/wsapi";
        api.setApiServerUrl(apiUrl);
        ApiCredential apiCred = new ApiCredential();
        ApiAccount ac = new ApiAccount();
        ac.setApplication("shangpin-6405-4c99-8a0b-95cb1bc38662");
        ac.setDeveloper("3812f2db-96ee-48c0-9c2e-8844a4cc1d85");
        ac.setCertificate("61eaf940-f7c7-464a-94a4-ce64c2ac075d");
        apiCred.seteBayToken("AgAAAA**AQAAAA**aAAAAA**KHyKVQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AHmIakDpaLow+dj6x9nY+seQ**uuYCAA**AAMAAA**/Od2vx5Xr3j+VUbRWjes8d8ja4b4lbq3oBaPrR6loaOwQSQ6JwXPkbvXhQoGsUN1I9a36qxjxI9tkKevURpeG76Bl+wVrNso5QAAuDtzKUyRb5li1hd2FDiMRwMlgVaFHpcU9eAW/Hs9QzcktnJiT1VG9SGtG9Jw/FucG1vPbtgtJmluiWpSltAM/IlWZzWRIEYFmmzG5eBmohwRFByzf4KkZoPMSmZsrdRhxM/zDm10qcVI8m3qsSJKPKui0KE045v4znX+lQJJCnTDJ085YQHBgTPbOEMYYqz7tZqcJBtlXHd/R2qyokgRZNiwFtNqb54ivib4yQV6TRlRcrcLCVyznkatsINFTyiMZGfOf+l2duKEZhYChVzuNd4rWMuMWj3ef6Porn4Ag5DB8N3AQ1HdJcZbpRh6AKlf1vy8mfnPOmDmCdhB4VMSH1jIcg8JN9jX9ED6/LoCgccfkqsAU+nbqDAyKQERF23EBjXmOJVZVHfpwgHJ871/PMQcYbIQDuBKp3bcU8jRyI0VH94ExPEXMO+/rkNe6aYTHisQ1WOwYAs+7PyIppipnqzMAhfSJs/AAybe2VC8X5DcXJXjQKSMwW34XnOffbAq3XJ3Hwh3a7pn1iZqlaxrOhvGKDYYO/kKFgX1kRtREud12nSHO8fNSBUaMJycdD4UlA9CtBFSgOWPwLFkKp1hpOjAMQXloPcSMi+2C4JXLLJSunCKJV5C4AcQdiS7cRx6D3l6ITgffYNgPKGioklNJYU5qETW");
        apiCred.setApiAccount(ac);
        api.setApiCredential(apiCred);
        api.setRuName("shangpin-shangpin-8ce3-4-xpmdteex");
        return api;
    }
    //TODO 有用吗？
    public GetMultipleItemsResponseType getItems(SearchItem[] type) throws Exception {

        Collection<String> itemIds = new HashSet<>();
        for (SearchItem t : type) {
            itemIds.add(t.getItemId());
            break;
            //System.out.println(t.getItemId());
        }
        return GrabEbayApiService.shoppingGetMultipleItems(itemIds);
    }
    //TODO 这里不只是spu，方法需要顾名思义；方法体太长考虑拆分成小功能；没有考虑到下架的情况
    public void saveSpu(FindItemsIneBayStoresResponse rt) throws Exception {

        Collection<String> itemIds = new HashSet<>();
        SpuDTO spu = null;
        if (rt.getSearchResult() != null) {
            SearchItem[] type = rt.getSearchResult().getItemArray();
            if (type != null) {
                GetMultipleItemsResponseType result = null;
                SimpleItemType[] itemTypes = new SimpleItemType[type.length];
                SimpleItemType[] itemType = new SimpleItemType[20];
                int i = 0, j = 0, k = type.length / 20 * 20;
                if (type.length < 20) {
                    for (SearchItem t : type) {
                        itemIds.add(t.getItemId());
                    }
                    result = GrabEbayApiService.shoppingGetMultipleItems(itemIds);
                    itemTypes = result.getItemArray();
                }

                int o = 0;
                for (SearchItem t : type) {
                    itemIds.add(t.getItemId());
                    ++j;
                    // System.out.println(itemIds.size()+"geshu"+j+"jshiduoshao");
                    if (itemIds.size() == 20) {

                        System.out.println(itemIds.size() + "数量");
                        result = GrabEbayApiService.shoppingGetMultipleItems(itemIds);
                        itemType = result.getItemArray();
                        System.arraycopy(itemType, 0, itemTypes, 20 * i, 20);

                        //System.out.println(itemTypes.length + "dashuzu");
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
                //TODO 请移除测试
                for (SimpleItemType l : itemTypes) {
                    System.out.println(++o + "dddd");
                }
                if (type.length % 20 != 0) {
                    int p = 0;
                    result = GrabEbayApiService.shoppingGetMultipleItems(itemIds);
                    itemType = result.getItemArray();
                    System.out.println("haha" + i);
                    System.arraycopy(itemType, 0, itemTypes, k, type.length % 20);
                    for (SimpleItemType l : itemTypes) {
                        System.out.println("nihao" + l.getItemID());
                        System.out.println(++p + "dddd");
                    }
                }
                for (SearchItem t : type) {
                    spu = new SpuDTO();
                    for (SimpleItemType item : itemTypes) {

                        spu.setId(UUIDGenerator.getUUID());
                        spu.setSpuId(t.getItemId());
                        // System.out.println(t.getItemId()+"zjk。。。。"+ item.getSeller().getUserID()+"huaxiahuaxiahuaxia");
                        if (item.getSeller() != null) {
                            if (item.getSeller().getUserID() != null) {
                                spu.setSupplierId("ebay#" + item.getSeller().getUserID());
                            }
                        }
                        spu.setSpuName(t.getTitle());
                        if (t.getTitle().toLowerCase().contains("women")
                                || t.getTitle().toLowerCase().contains("female")
                                || t.getTitle().toLowerCase().contains("lady")) {
                            spu.setCategoryGender("Woman");
                        } else if (t.getTitle().toLowerCase().contains("men") || t.getTitle().toLowerCase().contains("male")) {
                            spu.setCategoryGender("Man");
                        }
                        spu.setCategoryId(t.getPrimaryCategory().getCategoryId());
                        spu.setCategoryName(t.getPrimaryCategory().getCategoryName());
                        //获取二级category
                        if (item.getSecondaryCategoryID() != null) {
                            spu.setSubCategoryId(item.getSecondaryCategoryID());
                            spu.setSubCategoryName(item.getSecondaryCategoryName());
                        }
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
                        //调用getPicUrl方法来获得图片地址
                        //TODO 239行中的调用是否能取到？这里不能为了一个url调用一次api，考虑从已有的图片拿一张就行
                        spu.setPicUrl(getPicUrl(t.getItemId()));
                        spu.setCreateTime(t.getListingInfo().getStartTime().getTime());
                        spu.setLastTime(t.getListingInfo().getEndTime().getTime());
                        spuDTO.add(spu);
                        try {
                            productFetchService.saveSPU(spu);
                        } catch (DuplicateKeyException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    public void fetchSpuAndSave(String storeName, String keywords) throws Exception {

        try {
            for (int i = 1; i <= 100; i++) {
                String xml = HttpUtil45.get(getUrl(storeName, keywords, i),null,null);
                //System.out.println(xml);
                FindItemsIneBayStoresResponseDocument doc = FindItemsIneBayStoresResponseDocument.Factory.parse(xml);
                FindItemsIneBayStoresResponse rt = doc.getFindItemsIneBayStoresResponse();
                if (rt.getAck().equals("Failure")) {
                    continue;
                } else {
                    //System.out.println(storeName+rt.getPaginationOutput().getTotalPages());
                    try {
                        if (i > rt.getPaginationOutput().getTotalPages()) {
                            break;
                        }
                    } catch (NullPointerException e) {

                    }
                    saveSpu(rt);
                    //getItems(rt);
                }
            }
        } catch (XmlException e) {
            e.printStackTrace();
        }
    }
    //TODO 移除
    private String findCommonUrl(String operName) {
        String url = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=%s&"
                + "SECURITY-APPNAME=%s&RESPONSE-DATA-FORMAT=XML&REST-PAYLOAD&";
        String appid = "shangpin-8ce3-4e36-8082-464c90ad53bc";
        return url = String.format(url, operName, appid);
    }
    //TODO 命名！！
    public ItemType testGetItem(String itemId) throws com.ebay.sdk.ApiException, SdkSoapException, SdkException {

        ApiContext api = getProApiContext();
        ApiCall call = new ApiCall(api);
        GetItemRequestType req = new GetItemRequestType();
        req.setIncludeItemSpecifics(true);
        req.setItemID(itemId);
        req.setDetailLevel(new DetailLevelCodeType[]{DetailLevelCodeType.ITEM_RETURN_ATTRIBUTES});
        GetItemResponseType resp = (GetItemResponseType) call.execute(req);
        ItemType item = resp.getItem();
        return item;
    }
    //TODO 使用配置类！
    public String getUrl(String storeName, String keywords, int i) {
        String url = findCommonUrl("findItemsIneBayStores");

        url += "storeName=%s&paginationInput.entriesPerPage=100&paginationInput.pageNumber=" + i + "&keywords=%s";
        url = String.format(url, storeName, keywords);

        return url;
    }

    public String getPicUrl(String itemId) throws Exception {

        StringBuilder picUrl = new StringBuilder();
        ItemType item = testGetItem(itemId);
        //将两处的图片加在一个String中，获取所有图片
        if (item.getPictureDetails().getPictureURL() != null) {
            for (int m = 0; m < item.getPictureDetails().getPictureURL().length; m++) {
                picUrl.append(item.getPictureDetails().getPictureURL()[m]).append(";");
            }
        }
        if (item.getVariations() != null) {
            if (item.getVariations().getPictures() != null) {
                if (item.getVariations().getPictures().length > 0) {
                    VariationSpecificPictureSetType[] variationSpecificPictureSetType = item.getVariations().getPictures()[0].getVariationSpecificPictureSet();
                    if (variationSpecificPictureSetType != null)
                        for (VariationSpecificPictureSetType var : variationSpecificPictureSetType) {
                            picUrl.append(var.getPictureURL()).append(";");
                        }
                }
            }
        }
        return picUrl.toString();
    }
}

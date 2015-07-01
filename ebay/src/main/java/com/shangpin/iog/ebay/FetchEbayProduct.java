package com.shangpin.iog.ebay;

import ShangPin.SOP.Api.ApiException;
import com.ebay.sdk.*;
import com.ebay.soap.eBLBaseComponents.*;
import com.shangpin.ebay.finding.FindItemsIneBayStoresResponse;
import com.shangpin.ebay.finding.FindItemsIneBayStoresResponseDocument;
import com.shangpin.ebay.finding.SearchItem;
import com.shangpin.ebay.finding.SearchResult;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by huxia on 2015/6/30.
 */
@Component("ebay")
public class FetchEbayProduct {
    final Logger logger = Logger.getLogger(this.getClass());


    public Set getSkuDTO() {
        return skuDTO;
    }

    public void setSkuDTO(Set skuDTO) {
        this.skuDTO = skuDTO;
    }

    private Set skuDTO = new HashSet();

    public Set getSpuDTO() {
        return spuDTO;
    }

    public void setSpuDTO(Set spuDTO) {
        this.spuDTO = spuDTO;
    }

    private Set spuDTO = new HashSet();

    @Autowired
    ProductFetchService productFetchService;

    public void FetchSkuAndSave(String itemID) throws ApiException, SdkException, ServiceException, XmlException {

        SkuDTO sku = null;
        ApiContext api = getProApiContext();
        ApiCall call = new ApiCall(api);
        GetItemRequestType req = new GetItemRequestType();
        req.setIncludeItemSpecifics(true);
        req.setItemID(itemID);
        req.setDetailLevel(new DetailLevelCodeType[]{DetailLevelCodeType.ITEM_RETURN_ATTRIBUTES});
        GetItemResponseType resp = (GetItemResponseType) call.execute(req);
        ItemType it = resp.getItem();
        VariationType[] variationType = it.getVariations().getVariation();
        SearchResult searchResult = testFindItemInEbayStore();
        SearchItem[] type = null;
        if (searchResult != null) {
            type = searchResult.getItemArray();
        }
        if (type != null) {
            for (SearchItem t : type) {
                if (it.getListingDetails().getEndTime().getTime().before(Calendar.getInstance().getTime())) {
                    for (VariationType variationtype : variationType) {
                        sku = new SkuDTO();
                        sku.setId(UUIDGenerator.getUUID());
                        sku.setSkuId(variationtype.getSKU());
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
                            // System.out.println(nameValueListType.getName()+"dha");
                            if (nameValueListType.getName().contains("Color")) {
                                sku.setColor(nameValueListType.getValue(0));
                            }
                            if (nameValueListType.getName().contains("Size")) {
                                sku.setProductSize(nameValueListType.getValue(0));
                            }
                        }
                        sku.setCreateTime(it.getListingDetails().getStartTime().getTime());
                        sku.setLastTime(it.getListingDetails().getEndTime().getTime());
                        if (t.getItemId().equals(itemID)) {
                            if (t.getProductId() != null) {
                                sku.setSpuId(t.getProductId().getStringValue());
                                break;
                            } else {
                                sku.setSpuId(t.getItemId());
                                break;
                            }
                        }
                        productFetchService.saveSKU(sku);
                        skuDTO.add(sku);
                    }
                }
            }
        }
    }

    private ApiContext getProApiContext() {
        ApiContext api = new ApiContext();
        String apiUrl = "https://api.ebay.com/wsapi";
        api.setApiServerUrl(apiUrl);
        ApiCredential apiCred = new ApiCredential();
        ApiAccount ac = new ApiAccount();
        ac.setApplication("vanskydba-8e2b-46af-adc1-58cae63bf2e");
        ac.setDeveloper("6ff7eee5-cfdf-49f9-a8ba-be9dbffe0574");
        ac.setCertificate("a2b6fd62-4912-4467-bf86-8d8acf6e155d");
        apiCred.seteBayToken("AgAAAA**AQAAAA**aAAAAA**4yWKVQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6AHmIakCZGKpg6dj6x9nY+seQ**cOYCAA**AAMAAA**Q+CApXtvYnRBEIwhMDD6R5Bo0q1liNZZ8uJ1aMgCFFjakHgDH4MnIVe0MrQELtauhyNV4s/D818O4yQRu2rRN3UWkB2wHmSeWIcT/J0fYvlFxM3FgjzvKJ6syyeWUF/zQ4bg92RREYLZLaF19O1GS7FgnOJJjMOfh0Q7eMGnkNK/gHYmzRlgaYPKNMZwBaDeJN32BmpRtSedc0kgMDuzAehbBMAPcQ+QT8FzkfHPuRY4khKozZYG0Y3hUARlRWsEiLG7WUH25eVRDyA0mw1PERwRvFIT9SyiyhQKlF7x8NfMWbsRAsYukwdIJitg4sQol2T7urgHJSyTm6qbrq1NBjyNjFergMfNJKXqMLQ1ZB7Xhqloe7pG59MYDtpw4Rl68o4GjuaKN7OU5unje2U2kz2sugYke9YUqgBJ7q+lDABPaDw99omwnXEswwYa6G/WRUm5zaiivE83f0h+blnyLILZoPGMgh0ex2riVg2pgiDT/Zy4U55YW8BAQS9EizFAsJ27Kodx2r9MGfS+tJTTZTSGc16SkCxjGCg/clvPPkyNPimwV2e+wNDMLud55oQLHQaYgJ3BUpx204lpeR3sAoq9Ue/RfOL5Z+WQJMTYTPlDh86xbND4MHMsIOyZhuuPiwcsf/zaTupxvbrV7tueZ6jsGTOEy4s50zdAqs++EckbkPSrANud8UhalcOiyf85u8QwBxr4+/7V2k74CIBtKTz7HJFKEGMmjX/uLllTzoDizKu80C0pvaF8ghpeX7wK");
        apiCred.setApiAccount(ac);
        api.setApiCredential(apiCred);
        api.setRuName("vansky-vansky891-1d37--qzptrxv");
        return api;
    }

    public void FetchSpuAndSave(String storeName) throws Exception {

        SpuDTO spu = null;
        String url = findCommonUrl("findItemsIneBayStores");
        url += "storeName=%s&paginationInput.entriesPerPage=300&paginationInput.pageNumber=1";
        url = String.format(url, storeName);
        System.out.println(url);
        String xml = HttpUtils.get(url);
        System.out.println(xml);
        try {
            FindItemsIneBayStoresResponseDocument doc = FindItemsIneBayStoresResponseDocument.Factory.parse(xml);
            FindItemsIneBayStoresResponse rt = doc.getFindItemsIneBayStoresResponse();
            StringBuilder picUrl = new StringBuilder();
            if (rt.getSearchResult() != null) {
                SearchItem[] type = rt.getSearchResult().getItemArray();
                if (type != null) {
                    for (SearchItem t : type) {
                        spu = new SpuDTO();
                        ItemType item = testGetItem(t.getItemId());

                        spu.setId(UUIDGenerator.getUUID());
                        if (t.getProductId() != null) {
                            spu.setSpuId(t.getProductId().getStringValue());
                        }else {
                            spu.setSpuId(t.getItemId());
                        }
                        if(item.getSeller().getUserID()!=null) {
                            spu.setSupplierId("ebay#" + item.getSeller().getUserID());
                        }
                        spu.setSpuName(t.getTitle());
                        spu.setCategoryId(t.getPrimaryCategory().getCategoryId());
                        spu.setCategoryName(t.getPrimaryCategory().getCategoryName());
                        //获取二级category
                        if (item.getSecondaryCategory() != null) {
                            spu.setSubCategoryId(item.getSecondaryCategory().getCategoryID());
                            spu.setSubCategoryName(item.getSecondaryCategory().getCategoryName());
                        }
                        //判断和获取品牌、材质、产地
                        if (item.getItemSpecifics() != null) {
                            com.ebay.soap.eBLBaseComponents.NameValueListType[] nameValueListType = item.getItemSpecifics().getNameValueList();
                            if (nameValueListType != null) {
                                for (com.ebay.soap.eBLBaseComponents.NameValueListType nameValueList : nameValueListType) {
                                    if (nameValueList.getName().contains("brand")) {
                                        spu.setBrandName(nameValueList.getValue(0));
                                    }
                                    if (nameValueList.getName().contains("Material")) {
                                        spu.setMaterial(nameValueList.getValue(0));
                                    }
                                    if (nameValueList.getName().contains("Manufacture")) {
                                        spu.setProductOrigin(nameValueList.getValue(0));
                                    }
                                }
                            }
                        }
                        //将两处的图片加在一个String中，获取所有图片
                        if (item.getPictureDetails().getPictureURL() != null) {
                            for (int m = 0; m < item.getPictureDetails().getPictureURL().length; m++) {
                                picUrl.append(item.getPictureDetails().getPictureURL()[m]).append(";");
                            }
                        }
                        if (item.getVariations() != null) {
                            if (item.getVariations().getPictures() != null) {
                                VariationSpecificPictureSetType[] variationSpecificPictureSetType = item.getVariations().getPictures()[0].getVariationSpecificPictureSet();
                                if (variationSpecificPictureSetType != null)
                                    for (VariationSpecificPictureSetType var : variationSpecificPictureSetType) {
                                        picUrl.append(var.getPictureURL()).append(";");
                                    }
                            }
                        }
                        spu.setPicUrl(picUrl.toString());
                        spu.setCreateTime(t.getListingInfo().getStartTime().getTime());
                        spu.setLastTime(t.getListingInfo().getEndTime().getTime());
                        spuDTO.add(spu);
                        productFetchService.saveSPU(spu);
                    }
                }
            }
        } catch (XmlException e) {
            e.printStackTrace();
        }
    }

    private String findCommonUrl(String operName) {
        String url = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=%s&"
                + "SECURITY-APPNAME=%s&RESPONSE-DATA-FORMAT=XML&REST-PAYLOAD&";
        String appid = "vanskydba-8e2b-46af-adc1-58cae63bf2e";
        return url = String.format(url, operName, appid);
    }

    public ItemType testGetItem(String itemId) throws com.ebay.sdk.ApiException, SdkException, Exception {

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

    public SearchResult testFindItemInEbayStore() throws XmlException {
        String url = findCommonUrl("findItemsIneBayStores");
        url += "storeName=%s&paginationInput.entriesPerPage=300&paginationInput.pageNumber=1";
        String storeName = "inzara.store";
        url = String.format(url, storeName);
        System.out.println(url);
        String xml = HttpUtils.get(url);
        System.out.println(xml);
        FindItemsIneBayStoresResponseDocument doc = FindItemsIneBayStoresResponseDocument.Factory.parse(xml);
        FindItemsIneBayStoresResponse rt = doc.getFindItemsIneBayStoresResponse();
        SearchResult searchResult = rt.getSearchResult();
        return searchResult;
    }

}

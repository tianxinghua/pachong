package com.shangpin.iog.ebay;

import com.ebay.sdk.*;
import com.ebay.soap.eBLBaseComponents.*;
import com.shangpin.ebay.finding.FindItemsIneBayStoresResponse;
import com.shangpin.ebay.finding.FindItemsIneBayStoresResponseDocument;
import com.shangpin.ebay.finding.SearchItem;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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

    public Set<SkuDTO> getSkuDTO() {
        return skuDTO;
    }

    public void setSkuDTO(Set<SkuDTO> skuDTO) {
        this.skuDTO = skuDTO;
    }

    private Set<SkuDTO> skuDTO=new HashSet<SkuDTO>();

    public Set<SpuDTO> getSpuDTO() {
        return spuDTO;
    }

    public void setSpuDTO(Set<SpuDTO> spuDTO) {
        this.spuDTO = spuDTO;
    }

    private Set<SpuDTO> spuDTO = new HashSet<SpuDTO> ();

    @Autowired
    ProductFetchService productFetchService;

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
                            if (nameValueListType.getName().contains("Color")) {
                                sku.setColor(nameValueListType.getValue(0));
                            }
                            if (nameValueListType.getName().contains("Size")) {
                                sku.setProductSize(nameValueListType.getValue(0));
                            }
                        }
                        sku.setSpuId(itemID);
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
                            if (nameValueList.getName().contains("Color")) {
                                sku.setColor(nameValueList.getValue(0));
                            }
                            if (nameValueList.getName().contains("Size")) {
                                sku.setProductSize(nameValueList.getValue(0));
                            }
                        }
                    }
                }
                sku.setSaleCurrency(it.getListingDetails().getConvertedStartPrice().getCurrencyID().toString());
                sku.setSalePrice(it.getListingDetails().getConvertedStartPrice().toString());
                sku.setSupplierPrice(it.getListingDetails().getConvertedStartPrice().toString());
                sku.setCreateTime(it.getListingDetails().getStartTime().getTime());
                sku.setLastTime(it.getListingDetails().getEndTime().getTime());
                skuDTO.add(sku);
            }
            try {
                productFetchService.saveSKU(sku);
                productFetchService.savePictureForMongo(productPicture);
            }catch (DuplicateKeyException e){
                e.printStackTrace();
            }
        }
    }

    private static ApiContext getProApiContext() {
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
        api.setRuName("shangpin-shangpin-8ce3-4-xpmdteex");
        return api;
    }

    public void fetchSpuAndSave(String storeName) throws Exception {

        SpuDTO spu = null;
        String xml = HttpUtils.get(getUrl(storeName));
        try {
            FindItemsIneBayStoresResponseDocument doc = FindItemsIneBayStoresResponseDocument.Factory.parse(xml);
            FindItemsIneBayStoresResponse rt = doc.getFindItemsIneBayStoresResponse();
            if (rt.getSearchResult() != null) {
                SearchItem[] type = rt.getSearchResult().getItemArray();
                if (type != null) {
                    for (SearchItem t : type) {
                        spu = new SpuDTO();
                        ItemType item = testGetItem(t.getItemId());
                        spu.setId(UUIDGenerator.getUUID());
                        spu.setSpuId(t.getItemId());
                        if (item.getSeller().getUserID() != null) {
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
                        //调用getPicUrl方法来获得图片地址
                        spu.setPicUrl(getPicUrl(t.getItemId()));
                        spu.setCreateTime(t.getListingInfo().getStartTime().getTime());
                        spu.setLastTime(t.getListingInfo().getEndTime().getTime());
                        spuDTO.add(spu);
                        //System.out.println(spu.getSupplierId()+"huxia"+spu.getId()+"nihao"+spu.getCategoryName()+spu.getMaterial());
                        try {
                            productFetchService.saveSPU(spu);
                        }catch (DuplicateKeyException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (XmlException e) {
            e.printStackTrace();
        }
        System.out.println(spuDTO.size()+"dasd");

    }

    private String findCommonUrl(String operName) {
        String url = "http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=%s&"
                + "SECURITY-APPNAME=%s&RESPONSE-DATA-FORMAT=XML&REST-PAYLOAD&";
        String appid = "shangpin-8ce3-4e36-8082-464c90ad53bc";
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

    public String getUrl(String storeName) {
        String url = findCommonUrl("findItemsIneBayStores");
        url += "storeName=%s&paginationInput.entriesPerPage=300&paginationInput.pageNumber=1";
        url = String.format(url, storeName);

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
                VariationSpecificPictureSetType[] variationSpecificPictureSetType = item.getVariations().getPictures()[0].getVariationSpecificPictureSet();
                if (variationSpecificPictureSetType != null)
                    for (VariationSpecificPictureSetType var : variationSpecificPictureSetType) {
                        picUrl.append(var.getPictureURL()).append(";");
                    }
            }
        }
        return picUrl.toString();
    }
}

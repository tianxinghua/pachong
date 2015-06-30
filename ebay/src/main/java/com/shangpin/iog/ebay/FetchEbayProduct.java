package com.shangpin.iog.ebay;

import ShangPin.SOP.Api.ApiException;
import com.ebay.sdk.*;
import com.ebay.soap.eBLBaseComponents.*;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by huxia on 2015/6/30.
 */
@Component("ebay")
public class FetchEbayProduct {
    final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    ProductFetchService productFetchService;

    public void FetchSkuAndSave(String itemID) throws ApiException, SdkException {
        Set skuDTO=new HashSet();
        SkuDTO sku=null;
        ApiContext api = getProApiContext();
        ApiCall call = new ApiCall(api);
        GetItemRequestType req=new GetItemRequestType();
        req.setIncludeItemSpecifics(true);
        req.setItemID(itemID);
        req.setDetailLevel(new DetailLevelCodeType[]{DetailLevelCodeType.ITEM_RETURN_ATTRIBUTES});
        GetItemResponseType resp=(GetItemResponseType)call.execute(req);
        ItemType it=resp.getItem();
        VariationType[] variationType = it.getVariations().getVariation();
        for(VariationType variationtype:variationType){
            sku=new SkuDTO();
            sku.setStock(variationtype.getQuantity().toString());
            sku.setProductName(it.getTitle());
            AmountType amountType = variationtype.getStartPrice();
            sku.setSaleCurrency(amountType.getCurrencyID().toString());
            if(!String.valueOf(amountType.getValue()).equals(""))
            {
                sku.setSalePrice(String.valueOf(amountType.getValue()));
                sku.setSupplierPrice(String.valueOf(amountType.getValue()));
            }else {
                sku.setSalePrice(it.getListingDetails().getConvertedStartPrice().toString());
                sku.setSupplierPrice(it.getListingDetails().getConvertedStartPrice().toString());
            }
            com.ebay.soap.eBLBaseComponents.NameValueListType[] nameValueListTypes = variationtype.getVariationSpecifics().getNameValueList();
            for(com.ebay.soap.eBLBaseComponents.NameValueListType nameValueListType : nameValueListTypes){
                if(nameValueListType.getName().contains("color")){
                    sku.setColor(nameValueListType.getValue(0));
                }
                if (nameValueListType.getName().contains("size")){
                    sku.setProductSize(nameValueListType.getValue(0));
                }
            }
            sku.setCreateTime(it.getListingDetails().getStartTime().getTime());
            sku.setLastTime(it.getListingDetails().getEndTime().getTime());
            skuDTO.add(sku);
            try {
                System.out.println("nihaohaoshdoiahdoia");
                productFetchService.saveSKU(sku);
                System.out.println("nihaohaoshdoiahdoia");
            } catch (ServiceException e) {
                e.printStackTrace();
            }

        }
          System.out.println("nihaohaoshdoiahdoia");
    }
    private ApiContext getProApiContext() {
        ApiContext api = new ApiContext();
        String apiUrl="https://api.ebay.com/wsapi";
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

}

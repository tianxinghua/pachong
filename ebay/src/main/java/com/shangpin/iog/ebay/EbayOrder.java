package com.shangpin.iog.ebay;

import com.ebay.sdk.*;
import com.ebay.soap.eBLBaseComponents.*;

/**
 * Created by huxia on 2015/7/6.
 */
public class EbayOrder {

    public void AddOrder(String skuID,Integer quantity) throws SdkException {

        ApiContext api = getApiContext();
        ApiCall apiCall = new ApiCall(api);

        ApiContext api1 = getProApiContext();
        ApiCall call = new ApiCall(api1);

        apiCall.setEndUserIP("www.shangpin.com");
        String itemID = skuID.split("#")[0];
        String sku = skuID.split("#")[1];
        PlaceOfferRequestType req = new PlaceOfferRequestType();
        GetItemRequestType reqItem = new GetItemRequestType();
        reqItem.setItemID(itemID);
        req.setItemID(itemID);
        OfferType offerType = new OfferType();
        AmountType maxBid = new AmountType();
        NameValueListArrayType variation = new NameValueListArrayType();
        NameValueListType nv1 = new com.ebay.soap.eBLBaseComponents.NameValueListType();
        NameValueListType nv2 = new com.ebay.soap.eBLBaseComponents.NameValueListType();
        NameValueListType[] nvs = new com.ebay.soap.eBLBaseComponents.NameValueListType[]{nv1, nv2};
        GetItemResponseType respItem = (GetItemResponseType)call.execute(reqItem);
        System.out.println(respItem+"shang");
        ItemType item = respItem.getItem();
        if (item.getVariations() != null) {
            VariationType[] variationType = item.getVariations().getVariation();
            if (variationType != null) {
                for (VariationType variationtype : variationType) {
                    if (variationtype.equals(sku)) {
                        maxBid.setCurrencyID(variationtype.getStartPrice().getCurrencyID());
                        maxBid.setValue(variationtype.getStartPrice().getValue());
                        NameValueListType[] nameValueListTypes = variationtype.getVariationSpecifics().getNameValueList();
                        for (NameValueListType nameValueListType : nameValueListTypes) {
                            if (nameValueListType.getName().contains("Color")) {
                                nv1.setName(nameValueListType.getName());
                                nv1.setValue(new String[]{nameValueListType.getValue(0)});
                            }
                            if (nameValueListType.getName().contains("Size")) {
                                nv2.setName(nameValueListType.getName());
                                nv2.setValue(new String[]{nameValueListType.getValue(0)});
                            }
                        }
                    }
                }
            }
        }else {
            maxBid.setCurrencyID(item.getStartPrice().getCurrencyID());
            maxBid.setValue(item.getStartPrice().getValue());
            if (item.getItemSpecifics() != null) {
                NameValueListType[] nameValueListType = item.getItemSpecifics().getNameValueList();
                if (nameValueListType != null) {
                    for (NameValueListType nameValueList : nameValueListType) {
                        if (nameValueList.getName().contains("Color")) {
                            nv1.setName(nameValueList.getName());
                            nv1.setValue(new String[]{nameValueList.getValue(0)});
                        }
                        if (nameValueList.getName().contains("Size")) {
                            nv2.setName(nameValueList.getName());
                            nv2.setValue(new String[]{nameValueList.getValue(0)});
                        }
                    }
                }
            }
        }
        offerType.setAction(BidActionCodeType.PURCHASE);
        offerType.setQuantity(quantity);
        offerType.setMaxBid(maxBid);
        variation.setNameValueList(nvs);
        req.setOffer(offerType);
        req.setVariationSpecifics(variation);
        PlaceOfferResponseType resp = (PlaceOfferResponseType) apiCall.execute(req);
        System.out.println(resp);
    }
    //TODO 从配置类取
    private ApiContext getApiContext() {
        ApiContext api = new ApiContext();
        String apiUrl="https://api.sandbox.ebay.com/wsapi";
        api.setApiServerUrl(apiUrl);
        ApiCredential apiCred = new ApiCredential();
        ApiAccount ac = new ApiAccount();
        ac.setApplication("shangpin-eedd-49f1-9034-cb33bb4cba53");
        ac.setDeveloper("7e934edc-e8b1-440d-989c-313c183aae5f");
        ac.setCertificate("6e10c597-d13f-47eb-a883-a373179989a6");
        apiCred.seteBayToken("AgAAAA**AQAAAA**aAAAAA**CFSWVQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6wFk4GhDZiHpAudj6x9nY+seQ**AoADAA**AAMAAA**Rft3eglzujrpsuAdak5NhVMnFV+h1aIRXyvOP28YVdAJpv0znzV23WptVYqfMCwGVVpawiSJf1t8A7ZrnUw8ZHWJ/rkCGra2ii18tZK1A8nv3pBysKzpxALnurUMsj1BPqy2wFgs5B8yqytDzBiWhTPbV4oi/o6SI1eAyooj9Z1koxiynsESQ4zFj1MNUJKz4Mm8l4HXjq7Oh0rNBrOyVeUJvtYFz0UBdPiIqzZDi57c9TmuKgJLm4Ya/tJe63wNR6WvL2NCjWkPtWLRM9NArG2FWQIJKGBtxqRKkT6riCvc2J5cGbhgiaWIR6kF9x/4vkNCqASAb4mdS/D6/RGkxpaB5C/nZOzbkQG9JesvRRpGITeQRGKRL4OhQN7qR4X7Km3HykQckaoPaiK0ijalVTnkSIFNh6+cecAdADad/ERb+8/yvl4DsqHTmEZeZmqhbz/TFJhW4UpHbq7XJZKselI+I6LZtEo8oxFEPae3fNAFZUetUg3ah5pcIsf68aKaKzwlz2tlwDw6fAFHalQvcDBkVwd3ijLDV7+36eOod+q0vnz4kDbnueowh4m2UoRNBdECLpJq1uYKZir9ZGGSOubXOgzkSAykmdtZJWSudHs7I5h1bVliiucUfosBpoEAMh1nt1LSbkkqEa5fNrYwDEYBfuOUQJfhbgguIMgzHvDXEhDvZdPITT+uoGcyQUlOy8vtX3QikrbHRFvlxhd64RNTPKqm34Up6q3lmuV9U9WdTu/aklmE/xnC1ObPAeNq");
        apiCred.setApiAccount(ac);
        api.setApiCredential(apiCred);
        api.setRuName("shangpin-shangpin-8ce3-4-vsffif");
        return api;
    }
    //TODO 从配置类取
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
    //main/java里的提交时移除，否则写在test包中
    public static void main(String args[]) throws SdkException {
        EbayOrder ebayOrder = new EbayOrder();
        ebayOrder.AddOrder("251311485906#01.0741.102, 01.0741.103(var1)",1);
    }
}

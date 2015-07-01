package com.shangpin.igo.ebay.test;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



import com.ebay.sdk.*;
import com.ebay.soap.eBLBaseComponents.*;
import com.shangpin.ebay.finding.FindItemsIneBayStoresResponse;
import com.shangpin.ebay.finding.FindItemsIneBayStoresResponseDocument;
import com.shangpin.ebay.finding.SearchItem;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.ebay.conf.EbayConf;

import org.apache.xmlbeans.XmlException;
import org.junit.Test;

import com.ebay.sdk.ApiAccount;
import com.ebay.sdk.ApiCall;
import com.ebay.sdk.ApiContext;
import com.ebay.sdk.ApiCredential;
import com.ebay.sdk.ApiException;
import com.ebay.sdk.SdkException;
import com.ebay.soap.eBLBaseComponents.DetailLevelCodeType;
import com.ebay.soap.eBLBaseComponents.GetItemRequestType;
import com.ebay.soap.eBLBaseComponents.GetItemResponseType;
import com.ebay.soap.eBLBaseComponents.GetSellerListRequestType;
import com.ebay.soap.eBLBaseComponents.GetSellerListResponseType;
import com.ebay.soap.eBLBaseComponents.GranularityLevelCodeType;
import com.ebay.soap.eBLBaseComponents.ItemType;
import com.ebay.soap.eBLBaseComponents.PaginationType;
import com.ebay.soap.eBLBaseComponents.VariationType;
import com.shangpin.ebay.finding.FindItemsIneBayStoresResponse;
import com.shangpin.ebay.finding.FindItemsIneBayStoresResponseDocument;
import com.shangpin.ebay.finding.SearchItem;
import com.shangpin.ebay.shoping.CurrencyCodeType;
import com.shangpin.ebay.shoping.GetMultipleItemsResponseDocument;
import com.shangpin.ebay.shoping.GetMultipleItemsResponseType;
import com.shangpin.ebay.shoping.GetSingleItemResponseDocument;
import com.shangpin.ebay.shoping.GetSingleItemResponseType;
import com.shangpin.ebay.shoping.NameValueListType;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.ebay.conf.EbayConf;
import com.shangpin.iog.ebay.convert.TradeItemConvert;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月23日
 */
public class EbayTest {
	private Set skuDTO=new HashSet();
	private Set spuDTO=new HashSet();
	private Set itemArray = new HashSet();

	public void setSpuDTO(Set spuDTO) {
		this.spuDTO = spuDTO;
	}


	public Set getSkuDTO() {
		return skuDTO;
	}

	public void setSkuDTO(Set skuDTO) {
		this.skuDTO = skuDTO;
	}



	@Test
	public void testDF(){
		String mnt="1999.00";
		double  da    = Double.parseDouble(mnt);
		float fa=Float.parseFloat(mnt);
		System.out.println(CurrencyCodeType.CNY.toString());
		System.out.println(da+":"+fa);
	}
	
	@Test
	public void testGetItem() throws ApiException, SdkException, Exception{
		SkuDTO sku=null;
		String itemId="221288382861";
		ApiContext api = getProApiContext();
		ApiCall call = new ApiCall(api);
		GetItemRequestType req=new GetItemRequestType();
		req.setIncludeItemSpecifics(true);
		req.setItemID(itemId);
		/*req.setOutputSelector(new String[]{
			"ListingDetails","PrimaryCategory","SellingStatus",
			"PictureDetails","ItemSpecifics","Variations","VariationSpecificsSet",
			
				
		});*/
		req.setDetailLevel(new DetailLevelCodeType[]{DetailLevelCodeType.ITEM_RETURN_ATTRIBUTES});
		GetItemResponseType resp=(GetItemResponseType)call.execute(req);
		ItemType it=resp.getItem();
		String[] pics=it.getPictureDetails().getPictureURL();
		String[] pics2=it.getVariations().getPictures()[0].getVariationSpecificPictureSet()[0].getPictureURL();
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
		}
	}


	public ItemType testGetItem(String itemId) throws ApiException, SdkException, Exception{

		ApiContext api = getProApiContext();
		ApiCall call = new ApiCall(api);
		GetItemRequestType req=new GetItemRequestType();
		req.setIncludeItemSpecifics(true);
		req.setItemID(itemId);
		req.setDetailLevel(new DetailLevelCodeType[]{DetailLevelCodeType.ITEM_RETURN_ATTRIBUTES});
		GetItemResponseType resp=(GetItemResponseType)call.execute(req);
		ItemType item=resp.getItem();
		return item;
	}

	@Test
	public void testGetItemXml(){
		String itemId="331449399948";
		String url= EbayConf.getTradeCallUrl("GetItem");
		url+="&ItemID="+itemId;
		String xml=HttpUtils.get(url);
		System.out.println(xml);
	}
	@Test
	public void getSellerList() throws ApiException, SdkException, Exception{
		ApiContext api = getProApiContext();
		ApiCall call = new ApiCall(api);
		GetSellerListRequestType req = new GetSellerListRequestType();
		String userId="guess_outlet";
		req.setUserID(userId);//pumaboxstore buydig inzara.store happynewbaby2011 guess_outlet
		/*
		 */
		Calendar t1 = Calendar.getInstance();
		t1.setTime(new Date());
		Calendar t2 = Calendar.getInstance();t2.set(Calendar.MONTH, 8);
		/*req.setStartTimeFrom(t2);
		req.setStartTimeTo(t1);*/
		req.setEndTimeFrom(t1);
		req.setEndTimeTo(t2);
		
		PaginationType pg =new PaginationType();
		pg.setPageNumber(1);pg.setEntriesPerPage(8);
		req.setPagination(pg);
		req.setIncludeVariations(true);
		req.setDetailLevel(new DetailLevelCodeType[]{
				DetailLevelCodeType.ITEM_RETURN_DESCRIPTION
				});
//		req.setGranularityLevel(GranularityLevelCodeType.MEDIUM);
		req.setOutputSelector(new String[]{"ItemArray"});
		GetSellerListResponseType resp = (GetSellerListResponseType) call.execute(req);
		ItemType[] tps = resp.getItemArray().getItem();
		Map<String,? extends Collection<?>> skuAndSpu=TradeItemConvert.convert2SKuAndSpu(tps,userId);
		System.out.println(tps[0]+" "+skuAndSpu);
	}

	/**
	 * @return
	 */
	private ApiContext getApiContext() {
		ApiContext api = new ApiContext();
		String apiUrl="https://api.sandbox.ebay.com/wsapi";
		api.setApiServerUrl(apiUrl);
		ApiCredential apiCred = new ApiCredential();
		ApiAccount ac = new ApiAccount();
		ac.setApplication("vansky891-1d37-41f3-b3ba-1806062c992");
		ac.setDeveloper("6ff7eee5-cfdf-49f9-a8ba-be9dbffe0574");
		ac.setCertificate("38011760-86a2-4437-b734-4a2f8e7791a3");
		apiCred.seteBayToken("AgAAAA**AQAAAA**aAAAAA**n8l/VQ**nY+sHZ2PrBmdj6wVnY+sEZ2PrA2dj6wFk4GhDZeLqQSdj6x9nY+seQ**BnwDAA**AAMAAA**D0Omb5k0heLQrEV7cZioL7QRV36eylegxZIUISST2LX9O+u9TMYaXNrsNzHHBoNvv4TDtDj6vsmZGOOCx3ud4V60ggeuissigSC1V6/+T2F7BmdxODsNmb51aPbmmaM0RJdo0yG94slE2AP6M2jC7TMpH/5zzfc0Ut39pzfKr8JDmbOzcCIK6nXtrZicn30bQbfuFaE2IQJC29ag2QDDJFKzTvof3lNaokUfITay5HeHvV/uQjQIkhd765PEAxT0tqxuOahosilJ9uKVk5/4I+p3I817zvPZbmf+/cE4kfPhX22EPSajm6cDR9iYNfc4wzb4REbAv9NQuKCTRG2DpqB+gMVtK+H+JuMGxQv0M5w8qFIvkKdqYeUt5R3m4pcluWD0cL+kY9gmuAeyiZJ0c5b7itdeVCOB+LnrWp4QynzlB+FBJxy3n9IJKWi7Xy9Cg2ALyjy+nmMf7kXIyw+AEd32GjNScwGydA+3V/UAp5NRghFo2SuM7HbnY+qGF3Irx9B2K9cCc/W3ssdo+EYmPKPQI6wqUCWht3yryu+GETm+P/NjRQJPbBs/Ok0R9OAU1UQvqejUve+DWYU7JNtPfbCieG8M9rJ0IVnnM5PsD2/BVJUQBFCXXGKDe1aAziFUzV1523P7ep6fCMn84cQW03T6fGWV+wljFTLAhIVqNb2nB0VW4tqsWkEY/UuaFbuhQvR6hVNmtTTBzgGOGMH9z9bwfbT4y7sYh/mb6HoMVUpmSXbPUMgXERkRxQ7/NGvR");
	    apiCred.setApiAccount(ac);
		api.setApiCredential(apiCred);
		api.setRuName("vansky-vansky891-1d37--qzptrxv");
		return api;
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
//	find service
	/**
	 * 获取商家的产品，但是没有属性信息
	 */
	@Test
	public void testFindItemInStore() throws ApiException, SdkException, Exception{
		SpuDTO spu=null;
		String url=findCommonUrl("findItemsIneBayStores");
		url+="storeName=%s&paginationInput.entriesPerPage=10&paginationInput.pageNumber=1";
		String storeName="pumaboxstore";
		url=String.format(url,storeName);
		System.out.println(url);
		String xml=HttpUtils.get(url);
		System.out.println(xml);
		try{
			FindItemsIneBayStoresResponseDocument doc=FindItemsIneBayStoresResponseDocument.Factory.parse(xml);
			FindItemsIneBayStoresResponse rt = doc.getFindItemsIneBayStoresResponse();
			StringBuilder picUrl =new StringBuilder();
			if(rt.getSearchResult()!=null) {
				SearchItem[] type = rt.getSearchResult().getItemArray();
				if (type != null) {
					for (SearchItem t : type) {
						System.out.println(t.getPictureURLSuperSize());
						spu = new SpuDTO();
						ItemType item = testGetItem(t.getItemId());
						itemArray.add(t.getItemId());
						if (t.getProductId() != null) {
							spu.setSpuId(t.getProductId().getStringValue());
						}
						spu.setSupplierId("ebay#" + item.getSeller().getUserID());
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
					}
				}
			}

			SearchItem[] type = rt.getSearchResult().getItemArray();
			for (int i=0;i<type.length;i++){
				type[i].getItemId();
				SkuDTO sku = new SkuDTO();
				
			}


		} catch (XmlException e) {
			e.printStackTrace();
		}
	}

//	public String testGetPicUrl(){
//
//	}
	
	@Test
	public void findItemsByProduct(){
		String url=findCommonUrl("findItemsByProduct");
		url+="productId.@type=ReferenceID&productId=110602221&paginationInput.entriesPerPage=10&paginationInput.pageNumber=1";
		System.out.println(url);
		String xml=HttpUtils.get(url);
		System.out.println(xml);
	}
	@Test
	public void findItemsAdvanced(){
		String url=findCommonUrl("findItemsAdvanced");
		url+="productId.@type=ReferenceID&productId=110602221&paginationInput.entriesPerPage=10&paginationInput.pageNumber=1";
		System.out.println(url);
		String xml=HttpUtils.get(url);
		System.out.println(xml);
	}
	
	private String findCommonUrl(String operName){
		String url="http://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=%s&"
				+"SECURITY-APPNAME=%s&RESPONSE-DATA-FORMAT=XML&REST-PAYLOAD&";
		String appid="vanskydba-8e2b-46af-adc1-58cae63bf2e";
		return url=String.format(url, operName,appid);
	}
	
//product service
	@Test
	public void getProductDetails(){
		String url=productCommon("getProductDetails")
				+ "productDetailsRequest(0).dataset(0)=DisplayableSearchResults&"
				+ "productDetailsRequest(0).dataset(1)=DisplayableProductDetails&"
				+ "productDetailsRequest(0).dataset(2)=Searchable&"
				+ "productDetailsRequest(0).dataset(3)=Sortable&"
				+ "productDetailsRequest(0).productIdentifier.ePID=%s";
		String epid="110602221";
		url=String.format(url,epid);
		String xml=HttpUtils.get(url);
		System.out.println(xml);
	}
	private String productCommon(String operaName){
		String url="http://svcs.ebay.com/services/marketplacecatalog/ProductService/v1?OPERATION-NAME="+operaName+"&"
				+"SECURITY-APPNAME=vanskydba-8e2b-46af-adc1-58cae63bf2e&GLOBAL-ID=EBAY-US&RESPONSE-DATA-FORMAT=XML&REST-PAYLOAD&";
		//String appid="vanskydba-8e2b-46af-adc1-58cae63bf2e";
		return url;
	}
	@Test
	public void getMetaData(){
		String url="http://svcs.ebay.com/services/marketplacecatalog/ProductMetadataService/v1?OPERATION-NAME=getProductMetadataBulk&SERVICE-VERSION=1.0.0&"
				+"SECURITY-APPNAME=%s&GLOBAL-ID=EBAY-US&RESPONSE-DATA-FORMAT=XML&REST-PAYLOAD&productMetadataRequest.categoryId=%s";
		String appid="vanskydba-8e2b-46af-adc1-58cae63bf2e";
		String category="156955";
		url=String.format(url, appid,category);
		//System.out.println(url);
		String xml=HttpUtils.get(url);
		System.out.println(xml);
	}
//shopping api
	@Test
	public void getSingleItem(){
		String url=shopingCommon("GetSingleItem");
		url+="ItemID=400896098536&IncludeSelector=Variations,ItemSpecifics";
		String xml=HttpUtils.get(url);
		System.out.println(xml);
		try {
			GetSingleItemResponseDocument doc=GetSingleItemResponseDocument.Factory.parse(xml);
			GetSingleItemResponseType rt=doc.getGetSingleItemResponse();
			NameValueListType[] type=rt.getItem().getItemSpecifics().getNameValueListArray();
			System.out.println(rt.getItem().getTitle());
		} catch (XmlException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void GetMultipleItems(){
		String url=shopingCommon("GetMultipleItems");
		url+="ItemID=131503285055,221288382861&IncludeSelector=Variations,ItemSpecifics";
		String xml=HttpUtils.get(url);
		System.out.println(xml);
		try {
			GetMultipleItemsResponseDocument doc=GetMultipleItemsResponseDocument.Factory.parse(xml);
			GetMultipleItemsResponseType rt=doc.getGetMultipleItemsResponse();
			
		} catch (XmlException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void findProducts(){
		String str="this is a size. test";
		System.out.println(str.matches("(size)"));
	}
	
	private String shopingCommon(String callName){
		String url="http://open.api.ebay.com/shopping?callname="+callName+"&responseencoding=XMl&"
				+ "appid=vanskydba-8e2b-46af-adc1-58cae63bf2e&siteid=0&version=515&";
		return url;
	}

	public Set getSpuDTO() {
		return spuDTO;
	}

	public Set getItemArray() {
		return itemArray;
	}

	public void setItemArray(Set itemArray) {
		this.itemArray = itemArray;
	}
}

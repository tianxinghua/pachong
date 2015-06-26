package com.shangpin.igo.ebay.test;

import java.util.Calendar;
import java.util.Date;

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
import com.shangpin.ebay.shoping.GetSingleItemResponseDocument;
import com.shangpin.ebay.shoping.GetSingleItemResponseType;
import com.shangpin.ebay.shoping.NameValueListType;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月23日
 */
public class EbayTest {

	@Test
	public void testDF(){
		String mnt="1999.00";
		double  da    = Double.parseDouble(mnt);
		float fa=Float.parseFloat(mnt);
		System.out.println(da+":"+fa);
	}
	
	
	
	@Test
	public void testGetItem() throws Exception{
		String itemId="131523279309";
		ApiContext api = getProApiContext();
		ApiCall call = new ApiCall(api);
		GetItemRequestType type=new GetItemRequestType();
		type.setItemID(itemId);
		GetItemResponseType resp=(GetItemResponseType) call.execute(type);
		resp.getItem().getItemSpecifics();
		
	}
	@Test
	public void getSellerList() throws ApiException, SdkException, Exception{
		ApiContext api = getProApiContext();
		ApiCall call = new ApiCall(api);
		GetSellerListRequestType req = new GetSellerListRequestType();
		req.setUserID("buydig");
		Calendar t1 = Calendar.getInstance();
		t1.setTime(new Date());
		Calendar t2 = Calendar.getInstance();t2.set(Calendar.MONTH, 4);
		req.setEndTimeFrom(t1);
		req.setStartTimeFrom(t2);
		PaginationType pg =new PaginationType();
		pg.setPageNumber(1);pg.setEntriesPerPage(1);
		req.setPagination(pg);
		req.setIncludeVariations(true);
		req.setDetailLevel(new DetailLevelCodeType[]{
				DetailLevelCodeType.ITEM_RETURN_ATTRIBUTES,
				DetailLevelCodeType.ITEM_RETURN_CATEGORIES,
				DetailLevelCodeType.RETURN_HEADERS
				});
		//call.setStartTimeFilter(startTimeFilter);
		//call.setOutputSelector(new String[]{});
		req.setGranularityLevel(GranularityLevelCodeType.FINE);
		GetSellerListResponseType resp = (GetSellerListResponseType) call.execute(req);
		ItemType[] tps = resp.getItemArray().getItem();
		System.out.println(tps[0]);
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
	public void testFindItemInStore(){
		String url=findCommonUrl("findItemsIneBayStores");
		url+="storeName=%s&paginationInput.entriesPerPage=3&paginationInput.pageNumber=1";
		String storeName="buydig";
		url=String.format(url,storeName);
		System.out.println(url);
		String xml=HttpUtils.get(url);
		System.out.println(xml);
	}
	
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
		String cateId="53159";
		url=String.format(url, appid,cateId);
		System.out.println(url);
		String xml=HttpUtils.get(url);
		System.out.println(xml);
	}
//shopping api
	@Test
	public void getSingleItem(){
		String url=shopingCommon("GetSingleItem");
		url+="ItemID=331449399948&IncludeSelector=Variations,ItemSpecifics";
		String xml=HttpUtils.get(url);
		//XStream xs=new XStream(new DomDriver());
		System.out.println(xml);
		try {
			GetSingleItemResponseDocument doc=GetSingleItemResponseDocument.Factory.parse(xml);
			GetSingleItemResponseType rt=doc.getGetSingleItemResponse();
			NameValueListType[] type=rt.getItem().getItemSpecifics().getNameValueListArray();
			for (NameValueListType nv : type) {
				nv.getName();
			}
			System.out.println(rt.getItem().getTitle());
		} catch (XmlException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void findProducts(){
	}
	
	private String shopingCommon(String callName){
		String url="http://open.api.ebay.com/shopping?callname="+callName+"&responseencoding=XMl&"
				+ "appid=vanskydba-8e2b-46af-adc1-58cae63bf2e&siteid=0&version=515&";
		return url;
	}
}

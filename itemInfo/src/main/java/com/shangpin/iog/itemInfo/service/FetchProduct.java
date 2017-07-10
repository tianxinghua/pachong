package com.shangpin.iog.itemInfo.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.product.AbsSaveProduct;

@Component("itemInfo")
public class FetchProduct extends AbsSaveProduct{

	public static String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
			+ "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n"
			+ "  <soap12:Body>\n"
			+ "    <GetAllItems xmlns=\"http://tempuri.org/\" />\n"
			+ "  </soap12:Body>\n" + "</soap12:Envelope>";
	public static String errRet = "error";
//	final Logger logger = Logger.getLogger(this.getClass());
//	private static Logger logMongo = Logger.getLogger("mongodb");
	private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
	private static String supplierId = "";
	private static ResourceBundle bdl=null;
//	private static int day;
	
	@Autowired
	public ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;
	
	private static String biaoshi = "0";
	private static String localPath = "";
	

	static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
//        day = Integer.valueOf(bdl.getString("day"));
        biaoshi = bdl.getString("biaoshi");
        localPath = bdl.getString("localPath");
    }
	
	/**
	 * 
	 * @param url
	 *            请求的url
	 * @param sopAction
	 *            sopAction的地址
	 */
	public Map<String, Object> fetchProductAndSave() {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
		
		try {
			
			logger.info("localPath============"+localPath);
			logger.info("biaoshi==============="+biaoshi);
			//拉取数据
			//从网站拉取数据，保存到文件
			if("0".equals(biaoshi)){
				Test t= new Test();
				t.getResAsStream(
						"http://service.alducadaosta.com/EcSrv/Services.asmx?op=GetSku4Platform",
						"http://service.alducadaosta.com/EcSrv/GetSku4Platform",
						"text/xml; charset=UTF-8",
						localPath);
			}

			BufferedReader bufferedReader = new BufferedReader(new FileReader(
					localPath));
			StringBuilder stringBuilder = new StringBuilder();
			String content = "";
			while ((content = bufferedReader.readLine()) != null) {
				stringBuilder.append(content);
			}
			String result = stringBuilder.toString();
			
			SOAPMessage msg = formatSoapString(result);
			if(msg != null){
				SOAPBody body = msg.getSOAPBody();
				Iterator<SOAPElement> iterator = body.getChildElements();
				saveSpuDTO(iterator,skuList,spuList,imageMap);
				logger.info("==============================saveSpuDTO  over============================");
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			loggerError.error(ex); 
		}
		
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
		
	}

	
	/**
	 * 传入soap xml格式字符串，返回SOAPMessage对象 <br>
	 * @param soapString soap xml格式字符串
	 * @return
	 */
	public SOAPMessage formatSoapString(String soapString) {
		try {
			MessageFactory msgFactory = MessageFactory
					.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
			SOAPMessage reqMsg = msgFactory.createMessage(new MimeHeaders(),
					new ByteArrayInputStream(soapString.getBytes("UTF-8")));
			reqMsg.saveChanges();
			return reqMsg;
		} catch (Exception e) {
			e.printStackTrace();
			loggerError.error(e); 
			return null;
		}
	}
	
	/**
	 * 遍历迭代器，保存spu和sku信息<br>
	 * @param iterator
	 * @throws Exception 
	 */
	public void saveSpuDTO(Iterator<SOAPElement> iterator,List<SkuDTO> skuList,List<SpuDTO> spuList,Map<String,List<String>> imageMap){

//		Date startDate,endDate= new Date();
//		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
//		//获取原有的SKU 仅仅包含价格和库存
//		Map<String,SkuDTO> skuDTOMap = new HashedMap();
//		try {
//			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
		
		while(iterator.hasNext()){
			SOAPElement element = iterator.next();
			if(element.getNodeName().equals("SkuStok") && element.getChildElements().hasNext()){
				SpuDTO spu = new SpuDTO();
				spu.setId(UUIDGenerator.getUUID());
				spu.setSupplierId(supplierId); //必填
				
				SkuDTO sku = new SkuDTO();
				sku.setId(UUIDGenerator.getUUID());
				sku.setSupplierId(supplierId);//必填
				
				List<String> picList = new ArrayList<String>();
				String spuId = "";
				String skuId = "";
				
				Iterator<SOAPElement> iteratorSku = element.getChildElements();
				while(iteratorSku.hasNext()){
					SOAPElement elementSku = iteratorSku.next();
					String nodeName = elementSku.getNodeName();
					/**********保存spu***********/
					if(nodeName.equals("product_id")){  //必填
						if(elementSku.getValue() != null){
							spu.setSpuId(elementSku.getValue()); 
							sku.setSpuId(elementSku.getValue());
							spuId = elementSku.getValue();
						}else{
							break;  
						}
					}
					else if(nodeName.equals("category")){  //必填
						if(elementSku.getValue() != null){
							spu.setCategoryName(elementSku.getValue());
						}else{
							break;
						}
					}
					else if(nodeName.equals("season")){  //必填
						if(elementSku.getValue() != null){
							spu.setSeasonId(elementSku.getValue());
						}else{
							break;
						}
					}
					else if(nodeName.equals("brand")){  //必填
						if(elementSku.getValue() != null){
							spu.setBrandName(elementSku.getValue());
						}else{
							break;
						}
					}
					else if(nodeName.equals("texture")){  //必填
						if(elementSku.getValue() != null){
							spu.setMaterial(elementSku.getValue());
						}else{
							break;
						}
					}
					else if(nodeName.equals("made")){
						if(elementSku.getValue() != null){
							spu.setProductOrigin(elementSku.getValue());
						}
					}
					else if(nodeName.equals("suitable")){
						if(elementSku.getValue() != null){
							spu.setCategoryGender(elementSku.getValue());
						}
					}
					/**********保存sku***********/
					else if(nodeName.equals("sku_id")){  //必填
						if(elementSku.getValue() != null){
							sku.setSkuId(elementSku.getValue());
							skuId = elementSku.getValue();
						}else{
							break;
						}
					}
					else if(nodeName.equals("market_price")){  //必填
						if(elementSku.getValue() != null){
							sku.setMarketPrice(elementSku.getValue());
						}else{
							sku.setMarketPrice("");
						}
					}
					else if(nodeName.equals("supply_price")){  //必填
						if(elementSku.getValue() != null){
							sku.setSupplierPrice(elementSku.getValue());
						}else{
							sku.setSupplierPrice("");
						}
					}
					else if(nodeName.equals("color")){  //必填
						if(elementSku.getValue() != null){
							sku.setColor(elementSku.getValue());
						}else{
							break;
						}
					}
					else if(nodeName.equals("size")){  //必填
						if(elementSku.getValue() != null){
							String productSize = elementSku.getValue();							
							if(productSize.contains("/2")){
								productSize = productSize.replace("/2", ".5");
							}
							sku.setProductSize(productSize);
						}else{
							break;
						}
					}
					else if(nodeName.equals("stock")){  //必填
						if(elementSku.getValue() != null){
							sku.setStock(elementSku.getValue());
						}else{
							break;
						}
					}
					else if(nodeName.equals("size")){  //必填
						if(elementSku.getValue() != null){
							sku.setProductSize(elementSku.getValue());
						}else{
							break;
						}
					}
					else if(nodeName.equals("item_description")){
						if(elementSku.getValue() != null){
							sku.setProductDescription(elementSku.getValue());
						}
					}
					else if(nodeName.equals("model")){
						if(elementSku.getValue() != null){
							sku.setProductCode(elementSku.getValue());
						}
					}
					else if(nodeName.equals("title")){
						if(elementSku.getValue() != null){
							sku.setProductName(elementSku.getValue());
						}
					}
					/*************图片*****************/
					else if(nodeName.equals("item_imgs")){
						if(elementSku.getValue() != null){
							String urls = elementSku.getValue();
							String param[] = urls.split(",");
							for(String str : param){
								String ss = str.replace(str.substring(0, str.indexOf(":")+1), "");
								picList.add(ss);
							}
						}
					}
					
				}
				sku.setSalePrice(""); 
				skuList.add(sku);				
				imageMap.put(sku.getSkuId()+";"+sku.getProductCode()+" "+sku.getColor(), picList);
				spuList.add(spu);
				
			}else if(element.getChildElements().hasNext()){
				saveSpuDTO(element.getChildElements(),skuList,spuList,imageMap);
			}
		}

		
	}


	/**
	 * @author lubaijiang
	 * 
	 * @param url
	 *            请求的url
	 * @param sopAction
	 *            sopAction的地址
	 * @param contentType
	 * @return
	 */
	public String getResAsStream(String url, String sopAction,
			String contentType) {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.setRequestHeader("SOAPAction", sopAction);
		postMethod.setRequestHeader("Content-Type", contentType);
		StringRequestEntity requestEntity = new StringRequestEntity(
				soapRequestData);
		postMethod.setRequestEntity(requestEntity);

		int returnCode = 0;
		try {
			httpClient.setConnectionTimeout(1000 * 60 * 5);
			returnCode = httpClient.executeMethod(postMethod);

			System.out.println("returnCode==" + returnCode);
			if (returnCode == 200) {
				InputStream in = postMethod.getResponseBodyAsStream();
				byte[] ims = new byte[(int) postMethod
						.getResponseContentLength()];
				in.read(ims);
				// in.close();
				String s = new String(ims);
				return s;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return errRet;
	}
	
}

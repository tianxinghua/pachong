package com.shangpin.iog.itemInfo.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;

@Component("itemInfo")
public class FetchProduct {

	public static String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
			+ "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n"
			+ "  <soap12:Body>\n"
			+ "    <GetAllItems xmlns=\"http://tempuri.org/\" />\n"
			+ "  </soap12:Body>\n" + "</soap12:Envelope>";
	public static String errRet = "error";
	final Logger logger = Logger.getLogger(this.getClass());
	private static Logger logMongo = Logger.getLogger("mongodb");
	private static String supplierId = "";
	private static ResourceBundle bdl=null;
	
	@Autowired
	public ProductFetchService productFetchService;

	static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }
	
	/**
	 * 
	 * @param url
	 *            请求的url
	 * @param sopAction
	 *            sopAction的地址
	 */
	public void fetchProductAndSave(String url) {
		
		try {

			BufferedReader bufferedReader = new BufferedReader(new FileReader(
					url));
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
				saveSpuDTO(iterator);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
			return null;
		}
	}
	
	/**
	 * 遍历迭代器，保存spu和sku信息<br>
	 * @param iterator
	 * @throws Exception 
	 */
	public void saveSpuDTO(Iterator<SOAPElement> iterator){

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
						}
					}
					else if(nodeName.equals("supply_price")){  //必填
						if(elementSku.getValue() != null){
							sku.setSupplierPrice(elementSku.getValue());
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
							sku.setProductSize(elementSku.getValue());
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
				try{
					//保存sku
					productFetchService.saveSKU(sku);
					
				}catch(Exception e){
					try {
                        if(null != e.getMessage() && e.getMessage().equals("数据插入失败键重复")){
                            //更新价格和库存
                            productFetchService.updatePriceAndStock(sku);
                        } else{
                            e.printStackTrace();
                        }

                    } catch (ServiceException e1) {
                        e1.printStackTrace();
                    }
				}
				try{
					//保存spu
					System.out.println(spu.getSpuId());
					productFetchService.saveSPU(spu);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				try{
					if(spuId.length()>0 && skuId.length()>0 && picList.size()>0){
						for(String url :picList){
							ProductPictureDTO dto  = new ProductPictureDTO();
	                        dto.setId(UUIDGenerator.getUUID());
	                        dto.setSkuId(skuId);
	                        dto.setSpuId(spuId);
	                        dto.setPicUrl(url);
	                        dto.setSupplierId(supplierId);
	                        productFetchService.savePictureForMongo(dto);
						}
					}
					
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}else if(element.getChildElements().hasNext()){
				saveSpuDTO(element.getChildElements());
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

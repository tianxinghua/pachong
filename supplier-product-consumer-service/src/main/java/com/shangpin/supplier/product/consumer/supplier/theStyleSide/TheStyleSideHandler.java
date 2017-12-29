package com.shangpin.supplier.product.consumer.supplier.theStyleSide;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.service.SupplierProductMongoService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.theStyleSide.dto.CsvDTO;
import com.shangpin.supplier.product.consumer.supplier.theStyleSide.dto.Product;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:StefaniaHandler.java </p>
 * <p>Description: stefania供货商原始数据处理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijang
 * @date 2016年12月8日 上午11:36:22
 */
@Component("theStyleSideHandler")
@Slf4j
public class TheStyleSideHandler implements ISupplierHandler{
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;
	@Autowired
	private SupplierProductMongoService mongoService;
	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				CsvDTO jsonObject = JsonUtil.deserialize(message.getData(), CsvDTO.class);
				String[] array = jsonObject.getSize().split(";");
				for(String skuSizeAndStock : array) {
					String size = skuSizeAndStock.substring(0, skuSizeAndStock.indexOf("("));
					String stock = skuSizeAndStock.substring(skuSizeAndStock.indexOf("(")+1,skuSizeAndStock.indexOf(")"));
				    Product product = new Product();
				    product.setProductCode(jsonObject.getProduct_code());
				    product.setBrand(jsonObject.getBrand());
				    product.setCategory(jsonObject.getCategory());
				    product.setDescription(jsonObject.getDescription());
				    product.setColor(jsonObject.getColor());
				    product.setItalian_retail_price(jsonObject.getItalian_retail_price());
				    product.setMaterial(jsonObject.getMaterial());
				    product.setName(jsonObject.getModel_number());
				    product.setPics(jsonObject.getPics());
				    product.setSeason(jsonObject.getSeason());
				    product.setSesso(jsonObject.getSesso());
				    product.setSpu(jsonObject.getSku());
				    product.setStock(stock);
				    product.setSize(size);
					String supplierId = message.getSupplierId();
					HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
					List<Image> images = converImage(supplierId,product);
	//				if(null == images){
	//					hubSpu.setIsexistpic(Isexistpic.NO.getIndex());
	//				}else{
	//					hubSpu.setIsexistpic(Isexistpic.YES.getIndex()); 
	//				}
					boolean success = convertSpu(supplierId, product, hubSpu,message.getData());
					
					mongoService.save(supplierId, hubSpu.getSupplierSpuNo(), product);
					
					List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
					HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
					boolean skuSucc = convertSku(supplierId, hubSpu.getSupplierSpuId(), product, hubSku);
					if(skuSucc){
						hubSkus.add(hubSku);
					}
					//处理图片
					SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
					if(success){
						supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
					}
				}
			}	
		} catch (Exception e) {
			log.error("stefania异常："+e.getMessage(),e); 
		}		
		
	}
	
	/**
	 * stefania处理图片
	 * @param stefPicture
	 * @return
	 */
	private List<Image> converImage(String supplierId,Product jsonObject){
//		String supplierSpuNo =jsonObject.getSpu();
//		Map<String,String> existPics = pictureHandler.checkPicExistsOfSpu(supplierId, supplierSpuNo);
		String pics = jsonObject.getPics();
		String[] arraysPic = pics.split(";");
		List<Image> images = new ArrayList<Image>();
		for(String picture : arraysPic) {
			if(org.apache.commons.lang.StringUtils.isNotBlank(picture)){
				log.info("theStyleSide "+picture+" 将推送");
				Image image = new Image();
				image.setUrl(picture);
				images.add(image);
			}else{
				log.info("XXXXXXXXX theStyleSide "+picture+" 已存在XXXXXXXXXXXX");
			}
		}
		return images;
	}
	
	/**
	 * 将stefania原始数据转换成hub spu
	 * @param supplierId 供应商门户编号
	 * @param stefProduct stef 原始dto
	 * @param stefItem stef 原始dto
	 * @param hubSpu hub spu
	 * @return
	 */
	public boolean convertSpu(String supplierId,Product ob,HubSupplierSpuDto hubSpu,String data){
		if(null != ob && ob != null){
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(ob.getSpu());
			hubSpu.setSupplierSpuModel(ob.getProductCode());
			hubSpu.setSupplierSpuName(ob.getName());
			hubSpu.setSupplierSpuColor(ob.getColor());
			hubSpu.setSupplierGender(ob.getSesso());
			hubSpu.setSupplierCategoryname(ob.getCategory());
			hubSpu.setSupplierBrandname(ob.getBrand());
			hubSpu.setSupplierSeasonname(ob.getSeason());
			hubSpu.setSupplierMaterial(ob.getMaterial());
			hubSpu.setSupplierOrigin("");
			hubSpu.setSupplierSpuDesc(ob.getDescription());
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 将stefania原始数据转换成hub sku
	 * @param supplierId
	 * @param supplierSpuId
	 * @param stefItem
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId,Long supplierSpuId,Product ob,HubSupplierSkuDto hubSku){
		if(null != ob){
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(ob.getSpu()+ob.getSize());
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(ob.getItalian_retail_price())));
			hubSku.setMarketPriceCurrencyorg("EUR");
			hubSku.setSalesPrice(new BigDecimal(StringUtil.verifyPrice(ob.getItalian_retail_price())));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(ob.getItalian_retail_price())));
			hubSku.setSupplierBarcode(ob.getSpu()+ob.getSize()+ob.getSize());//sku+size
			if(!StringUtils.isEmpty(ob.getSize())){
				hubSku.setSupplierSkuSize(ob.getSize());
			}
			hubSku.setStock(Integer.parseInt(ob.getStock()));
//			String stock = ob.getString("availability");
//			Pattern pattern = Pattern.compile("[0-9]*"); 
//		    Matcher isNum = pattern.matcher(stock);
//		    if(isNum.matches() ){
//		    	hubSku.setStock(StringUtil.verifyStock(stock));
//		    } 
			return true;
		}else{
			return false;
		}
	}
}

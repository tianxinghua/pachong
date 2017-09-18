package com.shangpin.supplier.product.consumer.supplier.monnalisa;

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
import com.shangpin.supplier.product.consumer.supplier.common.enumeration.Isexistpic;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.monnalisa.dto.CsvDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:StefaniaHandler.java </p>
 * <p>Description: stefania供货商原始数据处理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijang
 * @date 2016年12月8日 上午11:36:22
 */
@Component("monnalisaHandler")
@Slf4j
public class MonnalisaHandler implements ISupplierHandler{
	
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
				String supplierId = message.getSupplierId();
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				List<Image> images = converImage(supplierId,jsonObject);
				if(null == images){
					hubSpu.setIsexistpic(Isexistpic.NO.getIndex());
				}else{
					hubSpu.setIsexistpic(Isexistpic.YES.getIndex()); 
				}
				boolean success = convertSpu(supplierId, jsonObject, hubSpu,message.getData());
				
				mongoService.save(supplierId, hubSpu.getSupplierSpuNo(), jsonObject);
				
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
				boolean skuSucc = convertSku(supplierId, hubSpu.getSupplierSpuId(), jsonObject, hubSku);
				if(skuSucc){
					hubSkus.add(hubSku);
				}
				//处理图片
				SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
				if(success){
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
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
	private List<Image> converImage(String supplierId,CsvDTO jsonObject){
		String supplierSpuNo =jsonObject.getId().substring(0,jsonObject.getId().lastIndexOf("-"));
		Map<String,String> existPics = pictureHandler.checkPicExistsOfSpu(supplierId, supplierSpuNo);
		String picture0 = jsonObject.getImage_link();
		List<Image> images = new ArrayList<Image>();
		if(org.apache.commons.lang.StringUtils.isNotBlank(picture0)&&!existPics.containsKey(picture0)){
			log.info("monnalisa "+picture0+" 将推送");
			Image image = new Image();
			image.setUrl(picture0);
			images.add(image);
		}else{
			log.info("XXXXXXXXX monnalisa "+picture0+" 已存在XXXXXXXXXXXX");
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
	public boolean convertSpu(String supplierId,CsvDTO ob,HubSupplierSpuDto hubSpu,String data){
		if(null != ob && ob != null){
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(ob.getId().substring(0,ob.getId().lastIndexOf("-")));
			hubSpu.setSupplierSpuModel(ob.getId().substring(0,ob.getId().lastIndexOf("-")));
			hubSpu.setSupplierSpuName(ob.getTitle());
			hubSpu.setSupplierSpuColor(ob.getColor());
			hubSpu.setSupplierGender(ob.getGender());
			hubSpu.setSupplierCategoryname(ob.getGoogle_product_category());
			hubSpu.setSupplierBrandname(ob.getBrand());
			hubSpu.setSupplierSeasonname(ob.getSeason());
			hubSpu.setSupplierMaterial(ob.getMaterial());
			hubSpu.setSupplierOrigin(null);
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
	public boolean convertSku(String supplierId,Long supplierSpuId,CsvDTO ob,HubSupplierSkuDto hubSku){
		if(null != ob){
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(ob.getId());
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(ob.getPrice())));
			hubSku.setSalesPrice(new BigDecimal(StringUtil.verifyPrice(ob.getSale_price())));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(ob.getPrice())));
			hubSku.setSupplierBarcode(ob.getBarcode());
			if(!StringUtils.isEmpty(ob.getSize())){
				hubSku.setSupplierSkuSize(ob.getSize());
			}
			
			hubSku.setStock(StringUtil.verifyStock("10"));
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

package com.shangpin.supplier.product.consumer.supplier.coccolebimbi;
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
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerRuntimeException;
import com.shangpin.supplier.product.consumer.service.SupplierProductMongoService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.coccolebimbi.dto.Item;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Component("coccolebimbiCommonHandler")
@Slf4j
public class CoccolebimbiCommonHandler implements ISupplierHandler {
	
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
				Item item = JsonUtil.deserialize(message.getData(), Item.class);
				String supplierId = message.getSupplierId();
				
				mongoService.save(supplierId, item.getCODICE(), item);
				
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(supplierId,item,hubSpu);
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
				
					boolean skuSuc = convertSku(supplierId,hubSpu.getSupplierSpuId(),item,hubSku);
					if(skuSuc){
						hubSkus.add(hubSku);
					}
				
				//处理图片				
				SupplierPicture supplierPicture = null;
//				if(pictureHandler.isCurrentSeason(message.getSupplierId(), hubSpu.getSupplierSeasonname())){
					supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, converImage(item));
//				}
				if(success){
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}	
		} catch (Exception e) {
			log.error("geb异常："+e.getMessage(),e); 
		}
		
	}
	
	/**
	 * geb处理图片
	 * @param itemImages
	 * @return
	 */
	private List<Image> converImage(Item itemImages){
		List<Image> images = new ArrayList<Image>();
		if(null != itemImages){		
			if(!StringUtils.isEmpty(itemImages.getFOTO1())){
				Image image = new Image();
				image.setUrl(itemImages.getUri()+"/foto?"+itemImages.getFOTO1());
				images.add(image);
			}
			if(!StringUtils.isEmpty(itemImages.getFOTO2())){
				Image image = new Image();
				image.setUrl(itemImages.getUri()+"/foto?"+itemImages.getFOTO2());
				images.add(image);
			}
			if(!StringUtils.isEmpty(itemImages.getFOTO3())){
				Image image = new Image();
				image.setUrl(itemImages.getUri()+"/foto?"+itemImages.getFOTO3());
				images.add(image);
			}
			if(!StringUtils.isEmpty(itemImages.getFOTO4())){
				Image image = new Image();
				image.setUrl(itemImages.getUri()+"/foto?"+itemImages.getFOTO4());
				images.add(image);
			}
			if(!StringUtils.isEmpty(itemImages.getFOTO5())){
				Image image = new Image();
				image.setUrl(itemImages.getUri()+"/foto?"+itemImages.getFOTO5());
				images.add(image);
			}
			if(!StringUtils.isEmpty(itemImages.getFOTO6())){
				Image image = new Image();
				image.setUrl(itemImages.getUri()+"/foto?"+itemImages.getFOTO6());
				images.add(image);
			}
			if(!StringUtils.isEmpty(itemImages.getFOTO7())){
				Image image = new Image();
				image.setUrl(itemImages.getUri()+"/foto?"+itemImages.getFOTO7());
				images.add(image);
			}
			if(!StringUtils.isEmpty(itemImages.getFOTO8())){
				Image image = new Image();
				image.setUrl(itemImages.getUri()+"/foto?"+itemImages.getFOTO8());
				images.add(image);
			}
		}
		return images;
	}
	
	/**
	 * 将geb原始dto转换成hub spu
	 * @param supplierId 供应商门户id
	 * @param item 供应商原始dto
	 * @param hubSpu hub spu表
	 */
	public boolean convertSpu(String supplierId,Item item, HubSupplierSpuDto hubSpu) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){			
			
			String desc = item.getDESCRIZIONE_LUNGA_ENG();
			if(desc!=null&&desc.split("#").length>1){
				String productName = desc.split("#")[0];
				hubSpu.setSupplierSpuName(productName);
			}
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(item.getCODICE());
			hubSpu.setSupplierSpuModel(item.getARTICOLO());
			
			
			if(item.getCOLORE_ENG()!=null){
				hubSpu.setSupplierSpuColor(item.getCOLORE_ENG());	
			}else{
				hubSpu.setSupplierSpuColor(item.getCOLORE_ITA());
			}
			
			hubSpu.setSupplierGender(item.getGRTIPO_ENG());
			
			hubSpu.setSupplierCategoryname(item.getMODELLO_ENG());
			hubSpu.setSupplierBrandname(item.getBRAND());
			
			if(item.getSTAGIONE_ENG()!=null){
				hubSpu.setSupplierSeasonname(item.getSTAGIONE_ENG());	
			}else{
				hubSpu.setSupplierSeasonname(item.getSTAGIONE_ITA());
			}
			
			
			if(item.getMATERIALE_ENG()!=null){
				hubSpu.setSupplierMaterial(item.getMATERIALE_ENG());	
			}else{
				hubSpu.setSupplierMaterial(item.getMATERIALE_ITA());
			}
			if(item.getMADEIN_ENG()!=null){
				hubSpu.setSupplierOrigin(item.getMADEIN_ENG());
			}else{
				hubSpu.setSupplierOrigin(item.getMADEIN());
			}
			hubSpu.setSupplierSpuDesc(desc);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 将geb原始dto转换成hub sku
	 * @param supplierId
	 * @param supplierSpuId hub spuid
	 * @param item
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId, Long supplierSpuId,Item item, HubSupplierSkuDto hubSku) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != item){			
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(item.getKEY());
//			hubSku.setSupplierBarcode(item.getBarCode());
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(item.getLISTINO())));
//			hubSku.setMarketPriceCurrencyorg(item.getCurrency());
//			hubSku.setSupplyPriceCurrency(item.getCurrency());
			hubSku.setSupplierSkuSize(item.getTAGLIA());
			hubSku.setStock(StringUtil.verifyStock((item.getDISPO())));
			return true;
		}else{
			return false;
		}
	}

}

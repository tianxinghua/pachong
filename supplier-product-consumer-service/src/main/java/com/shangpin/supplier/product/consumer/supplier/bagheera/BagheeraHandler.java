package com.shangpin.supplier.product.consumer.supplier.bagheera;

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
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.bagheera.dto.BagheeraDTO;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Component("bagheeraHandler")
@Slf4j
public class BagheeraHandler implements ISupplierHandler {
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				BagheeraDTO bagheeraDto = JsonUtil.deserialize(message.getData(), BagheeraDTO.class);
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(message.getSupplierId(),bagheeraDto,hubSpu);
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
				boolean skuSuc = convertSku(message.getSupplierId(),bagheeraDto,hubSku);
				if(skuSuc){
					hubSkus.add(hubSku);
				}
				SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, converImage(bagheeraDto));
				if(success){
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),message.getSupplierId(), message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}
		} catch (Exception e) {
			log.error("bagheera发生异常："+e.getMessage(),e); 
		}
		
	}
	
	private List<Image> converImage(BagheeraDTO dto) {
		List<Image> images = new ArrayList<Image>();
		if(null != dto){
			setImageValue(dto.getIMAGE_URL1(),images);
			setImageValue(dto.getIMAGE_URL2(),images);
			setImageValue(dto.getIMAGE_URL3(),images);
			setImageValue(dto.getIMAGE_URL4(),images);
		}
		return images;
	}
	
	private void setImageValue(String url,List<Image> images){
		if(!StringUtils.isEmpty(url)){
			Image image = new Image();
			image.setUrl(url);
			images.add(image);
		}
	}

	public boolean convertSpu(String supplierId,BagheeraDTO bagheeraDto, HubSupplierSpuDto hubSpu) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != bagheeraDto){
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(getSupplierSpuNo(bagheeraDto));
			hubSpu.setSupplierSpuModel(bagheeraDto.getSUPPLIER_CODE());
			hubSpu.setSupplierSpuName(bagheeraDto.getITEM_GROUP());
			hubSpu.setSupplierSpuColor(bagheeraDto.getCOLOR());
			hubSpu.setSupplierGender(getgender(bagheeraDto));
			hubSpu.setSupplierCategoryname(bagheeraDto.getCATEGORY());
			hubSpu.setSupplierBrandname(bagheeraDto.getITEM_GROUP());
			hubSpu.setSupplierSeasonname(bagheeraDto.getCOLLECTION());
			hubSpu.setSupplierMaterial(bagheeraDto.getMATERIAL());
			hubSpu.setSupplierOrigin(bagheeraDto.getMADE());
			hubSpu.setSupplierSpuDesc(bagheeraDto.getDESCRIPTION());
			return true;
		}else{
			return false;
		}
	}
	
	public boolean convertSku(String supplierId ,BagheeraDTO dto, HubSupplierSkuDto hubSku) throws EpHubSupplierProductConsumerRuntimeException{
		if(null != dto){
			String size = dto.getSIZE();
            if(size.indexOf("½")>0){
                size=size.replace("½","+");
            }
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(dto.getSUPPLIER_CODE()+"-"+size);
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(dto.getRETAIL_PRICE())));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(dto.getLASO_Price())));
			hubSku.setMarketPriceCurrencyorg(dto.getCURRENCY());
			hubSku.setSupplyPriceCurrency(dto.getCURRENCY());
			hubSku.setSupplierSkuSize(size);
			hubSku.setStock(StringUtil.verifyStock(dto.getSTOCK()));
			return true;
		}else{
			return false;
		}
	}

	private String getgender(BagheeraDTO bagheeraDto) {
		try {
			return bagheeraDto.getDEPT().substring(0, bagheeraDto.getDEPT().indexOf(" "));
		} catch (Exception e) {
			log.error(e.getMessage());
			return "";
		}
		
	}

	private String getSupplierSpuNo(BagheeraDTO bagheeraDto) {
		try {
			return bagheeraDto.getCODE().substring(0,bagheeraDto.getCODE().length()-2);
		} catch (Exception e) {
			log.error(e.getMessage()); 
			return "";
		}
		
	}

}

package com.shangpin.supplier.product.consumer.supplier.filippo;

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
import com.shangpin.supplier.product.consumer.supplier.filippo.dto.CsvDTO;

import lombok.extern.slf4j.Slf4j;

@Component("filippoHandler")
@Slf4j
public class FilippoHandler implements ISupplierHandler {
	
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
				CsvDTO csvDto = JsonUtil.deserialize(message.getData(), CsvDTO.class);
				String supplierId = message.getSupplierId();
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(supplierId,csvDto,hubSpu);
				mongoService.save(supplierId, hubSpu.getSupplierSpuNo(), csvDto);
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
				boolean skuSuc = convertSku(supplierId,csvDto,hubSku);
				if(skuSuc){
					hubSkus.add(hubSku);
				}
				SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, converImage(csvDto));
				if(success){
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}
		} catch (Exception e) {
			log.error("filippo发生异常："+e.getMessage(),e); 
		}
		
		
	}
	
	public boolean convertSpu(String supplierId,CsvDTO csvDto,HubSupplierSpuDto hubSpu){
		if(null != csvDto){
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(csvDto.getVAR_ID());
			hubSpu.setSupplierSpuModel(csvDto.getVAR_ID());
			hubSpu.setSupplierSpuName(csvDto.getTG_ID());
			hubSpu.setSupplierSpuColor(replaceIllegalChar(csvDto.getCOL_DES()));
			hubSpu.setSupplierGender(replaceIllegalChar(csvDto.getSR_DES()));
			hubSpu.setSupplierCategoryname(replaceIllegalChar(csvDto.getGRP_DES()));
			hubSpu.setSupplierBrandname(replaceIllegalChar(csvDto.getBND_NAME()));
			hubSpu.setSupplierSeasonname(replaceIllegalChar(csvDto.getSTG()));
			hubSpu.setSupplierMaterial(replaceIllegalChar(csvDto.getCOMP()));
			hubSpu.setSupplierOrigin(replaceIllegalChar(csvDto.getMADEIN()));
			return true;
		}else{
			return false;
		}
	}
	
	public boolean convertSku(String supplierId ,CsvDTO dto, HubSupplierSkuDto hubSku){
		if(null != dto){
			String supplierSkuNo = replaceIllegalChar(dto.getVAR_ID()+"-"+dto.getTG());		
			String size = replaceIllegalChar(dto.getTG());
			if(size.indexOf("1/2")>0){
				size=size.replace("-1/2","+");
			}
			if (size.equals("-")) {
				size = "UNIQUE";
			}
			if (size.substring(size.length()-1, size.length()).equals("-")) {
				size = size.replace("-", ".5");
			}
			size = size.replace(",", ".");
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(supplierSkuNo);
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(replaceIllegalChar(dto.getREF()))));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(replaceIllegalChar(dto.getEUR()))));
			hubSku.setMarketPriceCurrencyorg("EURO");
			hubSku.setSupplyPriceCurrency("EURO");
			hubSku.setSupplierSkuSize(size);
			hubSku.setStock(StringUtil.verifyStock(replaceIllegalChar(dto.getQTY())));
			hubSku.setSupplierBarcode(dto.getART()+"<>"+dto.getART_VAR()+"<>"+dto.getART_COL()+"<>"+dto.getTG().replace(",", ".")); 
			return true;
		}else{
			return false;
		}
	}
	
	private List<Image> converImage(CsvDTO csvDto){
		List<Image> images = new ArrayList<Image>();
		if(null != csvDto){
			String url = csvDto.getIMG();
			if(!StringUtils.isEmpty(url)){
				Image image = new Image();
				image.setUrl(replaceIllegalChar(url));
				images.add(image);
			}
		}
		return images;
	}
	
	private String replaceIllegalChar(String origin){
		if(!StringUtils.isEmpty(origin)){
			return origin.replaceAll("\"","");
		}else{
			return "";
		}
	}
	
}

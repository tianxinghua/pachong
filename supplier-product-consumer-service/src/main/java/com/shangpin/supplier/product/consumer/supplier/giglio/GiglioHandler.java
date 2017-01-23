package com.shangpin.supplier.product.consumer.supplier.giglio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerException;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.enumeration.Isexistpic;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Component("giglioHandler")
@Slf4j
public class GiglioHandler implements ISupplierHandler {
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				Map<?,?> colunms = JsonUtil.deserialize(message.getData(),Map.class);
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(message.getSupplierId(),hubSpu,colunms);
				List<Image> images = converImage(colunms);
				if(CollectionUtils.isNotEmpty(images)){
					hubSpu.setIsexistpic(Isexistpic.YES.getIndex());
				}else{
					hubSpu.setIsexistpic(Isexistpic.NO.getIndex());
				}
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
				boolean sucSku = convertSku(message.getSupplierId(),hubSku,colunms);
				if(sucSku){
					hubSkus.add(hubSku);
				}
				SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
				if(success){
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(), message.getSupplierId(), message.getSupplierName(), hubSpu, hubSkus, supplierPicture);
				}
			}
		} catch (EpHubSupplierProductConsumerException e) {
			log.error("giglio异常："+e.getMessage(),e); 
		}
		
	}
	
	private boolean convertSpu(String supplierId,HubSupplierSpuDto hubSpu,Map<?,?> colunms){
		if(null != colunms && colunms.size() >0){
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(value(colunms,"idProdotto"));
			hubSpu.setSupplierSpuModel(value(colunms,"Codice Prodotto"));
			hubSpu.setSupplierSpuName(value(colunms,"Nome Modello"));
			hubSpu.setSupplierSpuColor(value(colunms,"Colore ENG"));
			hubSpu.setSupplierGender(value(colunms,"Sesso(unisex, uomo, donna)"));
			hubSpu.setSupplierCategoryname(value(colunms,"Categoria"));
			hubSpu.setSupplierBrandname(value(colunms,"Marca"));
			hubSpu.setSupplierSeasonname(value(colunms,"Collezione / Anno"));
			hubSpu.setSupplierMaterial(value(colunms,"Materiale ENG"));
			hubSpu.setSupplierOrigin("");//TODO 无产地
			hubSpu.setSupplierSpuDesc(value(colunms,"Descrizione ENG"));
			return true;
		}else{
			return false;
		}
	}
	
	private boolean convertSku(String supplierId,HubSupplierSkuDto hubSku,Map<?,?> colunms){
		if(null != colunms && colunms.size() >0){
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(value(colunms,"idSKU"));
			hubSku.setSupplierSkuName(value(colunms,"Nome Modello"));
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(value(colunms,"Prezzo Et (€)"))));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(value(colunms,"Price to WS (€)"))));
			hubSku.setSalesPrice(new BigDecimal(StringUtil.verifyPrice(value(colunms,"Prezzo Imp (€)"))));
			hubSku.setMarketPriceCurrencyorg("EUR");
			hubSku.setSupplyPriceCurrency("EUR");
			String size_stock = value(colunms,"Taglie");
			Pattern pattern = Pattern.compile("(.+)\\((\\d+)\\)");
			Matcher matcher = pattern.matcher(size_stock);
			String size = null, stock = null;
            while (matcher.find()) {
                if (matcher.groupCount() > 0) {
                    size = matcher.group(1);
                }
                if (matcher.groupCount() > 1) {
                    stock = matcher.group(2);
                }
            }
			hubSku.setSupplierSkuSize(size);
			hubSku.setStock(StringUtil.verifyStock(stock));
			return true;
		}else{
			return false;
		}
	}
	
	private List<Image> converImage(Map<?,?> colunms){
		if(null == colunms || colunms.size() == 0){
			return null;
		}else{
			String picture = value(colunms,"Foto");
			if(StringUtils.isEmpty(picture)){
				return null;
			}else{
				List<Image> images = new ArrayList<Image>();
				for(String url : picture.split(";")){
					Image image = new Image();
					image.setUrl(url.trim());
					images.add(image);
				}
				return images;
			}
		}
	}
	
	private String value(Map<?,?> colunms,String key){
		if(colunms.containsKey(key)){
			return colunms.get(key).toString();
		}else{
			return "";
		}
	}
	
	/**
	 * 返回数组的第i列和第j，j1...列的组合，其中以sep分割
	 * @param colunms 数组
	 * @param sep 分隔符
	 * @param i 第i列
	 * @param j 第j列...
	 * @return
	 */
	@SuppressWarnings("unused")
	private String value(String[] colunms,String sep,int i,int...j){
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append(colunms[i]);
			for(int g : j){
				buffer.append(sep).append(colunms[g]);
			}
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return buffer.toString();
	}

}

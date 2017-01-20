package com.shangpin.supplier.product.consumer.supplier.giglio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerException;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
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
				String[] colunms = message.getData().split(";");
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(message.getSupplierId(),hubSpu,colunms);
				List<Image> images = converImage();
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
	
	private boolean convertSpu(String supplierId,HubSupplierSpuDto hubSpu,String[] colunms){
		if(null != colunms && colunms.length >0){
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(value(colunms,"",0));
			hubSpu.setSupplierSpuModel(value(colunms,"",2));
			hubSpu.setSupplierSpuName(value(colunms,"",4));
			hubSpu.setSupplierSpuColor(value(colunms,"",5));
			hubSpu.setSupplierGender(value(colunms,"",8));
			hubSpu.setSupplierCategoryname(value(colunms,"",9));
			hubSpu.setSupplierBrandname(value(colunms,"",3));
			hubSpu.setSupplierSeasonname(value(colunms,"",10));
			hubSpu.setSupplierMaterial(value(colunms,"",7));
			hubSpu.setSupplierOrigin("");//TODO 无产地
			hubSpu.setSupplierSpuDesc(value(colunms,"",6));
			return true;
		}else{
			return false;
		}
	}
	
	private boolean convertSku(String supplierId,HubSupplierSkuDto hubSku,String[] colunms){
		if(null != colunms && colunms.length >0){
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(value(colunms,"",1));
			hubSku.setSupplierSkuName(value(colunms,"",4));
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(value(colunms,"",11))));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(value(colunms,"",13))));
			hubSku.setSalesPrice(new BigDecimal(StringUtil.verifyPrice(value(colunms,"",12))));
			hubSku.setMarketPriceCurrencyorg("EUR");
			hubSku.setSupplyPriceCurrency("EUR");
			String size_stock = value(colunms,"",14);
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
	
	private List<Image> converImage(){
		return null;
	}
	
	/**
	 * 返回数组的第i列和第j，j1...列的组合，其中以sep分割
	 * @param colunms 数组
	 * @param sep 分隔符
	 * @param i 第i列
	 * @param j 第j列...
	 * @return
	 */
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

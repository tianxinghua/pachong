package com.shangpin.supplier.product.consumer.supplier.deliberti2;

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
import com.shangpin.supplier.product.consumer.supplier.common.dto.Color;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.deliberti2.dto.*;
import com.shangpin.supplier.product.consumer.supplier.deliberti2.dto.DelibertiSkuDto;
import com.shangpin.supplier.product.consumer.supplier.deliberti2.dto.DelibertiSpuDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 供应商deliberti消费者
 * <p>Title: DelibertiHandler</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年2月24日 上午10:09:49
 *
 */
@Component("delibertiHandler2")
@Slf4j
public class DelibertiHandler implements ISupplierHandler {
	
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
				Product productDto = JsonUtil.deserialize(message.getData(), Product.class);
				String supplierId = message.getSupplierId();
				
				mongoService.save(supplierId, productDto.getSpuId(), productDto);
				
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(supplierId,productDto,hubSpu);
				if(success){
					List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
					String sizeAndquantity = productDto.getSizeAndquantity();
					String[] split2 = sizeAndquantity.split(";");
					for (String s:split2){
						HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
						boolean succSku =convertSku(supplierId,s,productDto,hubSku);
						if(succSku){
							hubSkus.add(hubSku);
						}
					}
					String pictureUrl = productDto.getPictureUrl();
					String[] split = pictureUrl.split(";");
					ArrayList<String> imgs = new ArrayList<>();
					for(String img:split){
						imgs.add(img);
					}


					List<Image> images = converImage(imgs);
					SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}
		} catch (Exception e) {
			log.error("deliberti异常："+e.getMessage(),e);
		}
		
	}
	
	/**
	 * 
	 * @param supplierId
	 * @param proDto
	 * @param hubSpu
	 * @return
	 * @throws EpHubSupplierProductConsumerRuntimeException
	 */
	public boolean convertSpu(String supplierId, Product proDto, HubSupplierSpuDto hubSpu) throws EpHubSupplierProductConsumerRuntimeException{
		if(null == proDto){
			return false;
		}else{
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(proDto.getSpuId());
			//缺少货号,不确定
			hubSpu.setSupplierSpuModel(proDto.getGtin());
			hubSpu.setSupplierSpuName(proDto.getProductName());
			hubSpu.setSupplierSpuColor(proDto.getColor());
			hubSpu.setSupplierGender(proDto.getGender());
			hubSpu.setSupplierCategoryname(proDto.getSubcat());
			hubSpu.setSupplierBrandname(proDto.getBrand());
			hubSpu.setSupplierSeasonname(proDto.getSeason());
			hubSpu.setSupplierMaterial(proDto.getMaterial());
			hubSpu.setSupplierSpuDesc(proDto.getDescription());
			hubSpu.setSupplierOrigin(proDto.getOrigin());
			return true;
		}
	}
	/**
	 * 
	 * @param supplierId
	 * @param
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId,String s, Product productDto, HubSupplierSkuDto hubSku){
		   hubSku.setSupplierId(supplierId);
		System.out.println("ssssssssSize"+s);
		String substring1 = s.substring(0,s.indexOf("("));
		hubSku.setSupplierSkuNo(productDto.getSpuId()+"_"+substring1);
		String stock = s.substring(s.indexOf("(") + 1, s.indexOf(")"));
		hubSku.setStock(Integer.parseInt(stock));
		hubSku.setSupplierSkuSize(substring1);

		hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(productDto.getPrice())));
		if (productDto.getDiscountIfAny().equals("0")){
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(productDto.getPrice())));
		}
		if (!productDto.getDiscountIfAny().equals("0")){
			String str3="100";
			BigDecimal decimal = new BigDecimal(StringUtil.verifyPrice(productDto.getPrice()));//189
			BigDecimal decima2 = new BigDecimal(StringUtil.verifyPrice(productDto.getDiscountIfAny()));//30
			BigDecimal decima3 = new BigDecimal(str3);//100
			BigDecimal subtract = decima3.subtract(decima2);//70
			BigDecimal divide = subtract.divide(decima3);
			BigDecimal multiply = decimal.multiply(divide);
			hubSku.setSupplyPrice(multiply);
		}
		/*hubSku.setSupplierId(supplierId);
		hubSku.setSupplierSkuNo(delibertiSkuDto.getSkuId());
		hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(delibertiSkuDto.getMarketPrice())));
		hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(delibertiSkuDto.getSupplierPrice())));
		hubSku.setMarketPriceCurrencyorg(delibertiSkuDto.getSaleCurrency());
		String sizeType = "";
		if(StringUtils.isEmpty(delibertiSkuDto.getProductDescription())){
			sizeType = delibertiSkuDto.getProductDescription();
		}
		hubSku.setSupplierSkuSize(sizeType + " " + delibertiSkuDto.getProductSize());
		hubSku.setStock(StringUtil.verifyStock((delibertiSkuDto.getStock())));*/
		return true;
	}
	
	private List<Image> converImage(List<String> pictures){
		List<Image> images = new ArrayList<Image>();
		if(CollectionUtils.isNotEmpty(pictures)){			
			for(String url : pictures){
				if(url!=null&&url.endsWith("Z.jpg")){
					Image image = new Image();
					image.setUrl(url);
					images.add(image);
				}
			}
		}
		return images;
	}
}

package com.shangpin.supplier.product.consumer.supplier.raffaellonetwork;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerRuntimeException;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.raffaellonetwork.dto.Product;
import com.shangpin.supplier.product.consumer.supplier.studio69.dto.StudioSkuDto;
import com.shangpin.supplier.product.consumer.supplier.studio69.dto.StudioSpuDto;

import lombok.extern.slf4j.Slf4j;

@Component("raffaellonetworkHandler")
@Slf4j
public class RaffaellonetworkHandler implements ISupplierHandler{
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				Product product = JsonUtil.deserialize(message.getData(), Product.class);
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(message.getSupplierId(),product,hubSpu);
				if(success){
				
					List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
					int beginIndex=product.getSize().indexOf(",");
					//保存sku	
					if(beginIndex!=-1){
						String size[] = product.getSize().split(",");
						String stock[] = product.getQuantity().split(",");
						if(size.length==stock.length){
							for (int i = 0; i < size.length; i++) {
								HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
								boolean skuSuc = convertSku(message.getSupplierId(),hubSpu.getSupplierSpuId(),product,hubSku,size[i],stock[i]);
								if(skuSuc){
									hubSkus.add(hubSku);
								}
							}
						}else{
							return;
						}
					}else{
						HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
						boolean skuSuc = convertSku(message.getSupplierId(),hubSpu.getSupplierSpuId(),product,hubSku,product.getSize(),product.getQuantity());
						if(skuSuc){
							hubSkus.add(hubSku);
						}
					}
					
					List<Image> images = converImage(product);
					SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),message.getSupplierId(), message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}
		} catch (EpHubSupplierProductConsumerException e) {
			log.error("studio69异常："+e.getMessage(),e);
		}
		
	}
	/**
	 * 
	 * @param supplierId
	 * @param studioSpuDto
	 * @param hubSpu
	 * @return
	 * @throws EpHubSupplierProductConsumerRuntimeException
	 */
	public boolean convertSpu(String supplierId,Product product,HubSupplierSpuDto hubSpu) throws EpHubSupplierProductConsumerRuntimeException{
		if(null == product){
			return false;
		}else{
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(product.getId());
			hubSpu.setSupplierSpuModel(product.getMpn());
			hubSpu.setSupplierSpuName(product.getTitle());
			hubSpu.setSupplierSpuColor(product.getColor());
			hubSpu.setSupplierGender(product.getGender());
			hubSpu.setSupplierCategoryname(product.getProduct_type());
			hubSpu.setSupplierBrandname(product.getBrand());
			hubSpu.setSupplierSeasonname(product.getCustom_label_0());
			hubSpu.setSupplierMaterial(product.getMaterial());
			hubSpu.setSupplierOrigin(product.getMade_in_italy());
			return true;
		}
	}
	/**
	 * 
	 * @param supplierId
	 * @param studioSkuDto
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId,Long supplierSpuId,Product studioSkuDto,HubSupplierSkuDto hubSku,String size,String stock){
		
		hubSku.setSupplierId(supplierId);
		hubSku.setSupplierSkuNo(studioSkuDto.getMpn()+"_"+size);
		hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(studioSkuDto.getProductcost())));
		hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(studioSkuDto.getProductcost())));
		hubSku.setSupplierSkuSize(size);
		hubSku.setStock(StringUtil.verifyStock((stock)));
		hubSku.setSupplierBarcode(studioSkuDto.getMpn()+"_"+size);
		return true;
	}
	
	private List<Image> converImage(Product item){
		
		//图片
		List<String> pics = new ArrayList<String>();
		int pic1Index=item.getImage_link().indexOf(",");
		int pic2Index=item.getAdditional_image_link().indexOf(",");
		if(pic1Index!=-1){
			String img1 [] = item.getImage_link().split(",");
			for (int j = 0; j < img1.length; j++) {
				pics.add(img1 [j]);
			}
		}else{
			pics.add(item.getImage_link());
		}
		if(pic2Index!=-1){
			String img2 [] = item.getAdditional_image_link().split(",");
			for (int j = 0; j < img2.length; j++) {
				pics.add(img2[j]);
			}
		}else{
			pics.add(item.getAdditional_image_link());
		}
		
		List<Image> images = new ArrayList<Image>();
		if(CollectionUtils.isNotEmpty(pics)){			
			for(String url : pics){
				Image image = new Image();
				image.setUrl(url);
				images.add(image);
			}
		}
		return images;
	}

}

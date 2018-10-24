package com.shangpin.supplier.product.consumer.supplier.ilcucciolo;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.ProductPicture;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.service.SupplierProductMongoService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.ilcucciolo.dto.IlcuccioloDto;
import com.shangpin.supplier.product.consumer.supplier.ilcucciolo.dto.Result;
import com.shangpin.supplier.product.consumer.util.DateTimeUtil;
import com.shangpin.supplier.product.consumer.util.UUIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("ilcuccioloHandler")
@Slf4j
public class IlcuccioloHandler implements ISupplierHandler {
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private SupplierProductMongoService mongoService;

	@Autowired
	private PictureHandler pictureHandler;

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				Result result = JsonUtil.deserialize(message.getData(), Result.class);
				IlcuccioloDto cucciolo = JsonUtil.deserialize(message.getData(), IlcuccioloDto.class);
				List<String> urlList = new ArrayList<>();
				urlList.add(cucciolo.getImg());  urlList.add(cucciolo.getImg2());
				urlList.add(cucciolo.getImg3()); urlList.add(cucciolo.getImg4());

				String supplierId = message.getSupplierId();
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				boolean success = convertSpu(supplierId,hubSpu,cucciolo);
				
				mongoService.save(supplierId, hubSpu.getSupplierSpuNo(), result);
				
				/*List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				for(Sizes size : result.getSizes()){
					HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
					boolean succ = convertSku(supplierId,hubSku,result.getSku(),size);
					if(succ){
						hubSkus.add(hubSku);
					}
				}*/

				ArrayList<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
				boolean skuSuccess = convertSku(supplierId,hubSpu.getSupplierSpuId(), cucciolo, hubSku);
				if(skuSuccess){
					hubSkus.add(hubSku);
				}
				if(success){
					//处理图片
					SupplierPicture supplierPicture = null;

					supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu,converImage(urlList) );

					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}
			
		} catch (Exception e) {
			log.error("lungolivigno异常："+e.getMessage(),e); 
		}
		
	}
	
	private boolean convertSpu(String supplierId, HubSupplierSpuDto hubSpu, IlcuccioloDto cucciolo){
		if(null != cucciolo){
			//String[] parm={"ABITO","Camicia","Coat","Felpa","FELPA ","GIACCA","Giubbino","Gonna","Jacket","Jeans","MAGLIA","PANT","Pantalone","PARKA","PIUMINO","POLO","PONCHO","Pull","SHIRT","TOP","Trench","T-Shirt","VESTITO"};
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(cucciolo.getSkuTime());
			hubSpu.setSupplierSpuDesc(cucciolo.getDescription());
			hubSpu.setSupplierSpuName(cucciolo.getDescription());
			hubSpu.setSupplierCategoryname(cucciolo.getCateGory());
			hubSpu.setSupplierSeasonname(cucciolo.getSeason());
			hubSpu.setSupplierMaterial(cucciolo.getComposition());
			hubSpu.setSupplierGender(cucciolo.getCender());
			hubSpu.setSupplierSpuColor(cucciolo.getColour());
			//hubSpu.setSupplierSpuModel(result.getAttributes().get(5).getCode());
			hubSpu.setSupplierBrandname(cucciolo.getBrand());
			//String origin = result.getAttributes().get(6).getValue();
			//hubSpu.setSupplierOrigin(!StringUtils.isEmpty(origin)? origin:result.getAttributes().get(6).getCode());

			/*if (supplierId.equals("2016110101955")) {
			   for (int i = 0; i < parm.length; i++) {
			   	if (result.getName()!=null){
				   if (result.getName().toUpperCase().contains(parm[i].toUpperCase())) {
					   System.out.println("---------------i===" + i + " " + parm[i]);
					   hubSpu.setSupplierCategoryname(parm[i]);
				   }
			   }}
		   }*/

			return true;
		}else{
			return false;
		}
	}
	
	private boolean convertSku(String supplierId, Long supplierSpuId, IlcuccioloDto cucciolo,HubSupplierSkuDto hubSku){
		if(null != cucciolo){
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			String supplierSkuNo = cucciolo.getSkuTime()+"_"+cucciolo.getSize();
			hubSku.setSupplierSkuNo(supplierSkuNo);
			hubSku.setSupplierSkuName(cucciolo.getDescription()+"-"+cucciolo.getSize());
			//hubSku.setSupplierBarcode(supplierSkuNo);
			hubSku.setMarketPrice( new BigDecimal(StringUtil.verifyPrice(cucciolo.getPriceList())) );//市场价
			hubSku.setSalesPrice( new BigDecimal(StringUtil.verifyPrice(cucciolo.getPriceList())) );//售价
			hubSku.setSupplyPrice( new BigDecimal(StringUtil.verifyPrice(cucciolo.getPriceList())) );//供价
			hubSku.setSupplierSkuSize(cucciolo.getSize());
			hubSku.setStock(StringUtil.verifyStock(cucciolo.getStock()));
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 初始化发送图片消息体
	 * @param message 供应商原始消息
	 * @param hubSpu
	 * @param images
	 * @return
	 */
	public SupplierPicture initSupplierPicture(SupplierProduct message,HubSupplierSpuDto hubSpu,List<Image> images){
		SupplierPicture supplierPicture = new SupplierPicture();
		supplierPicture.setMessageId(UUIDGenerator.getUUID());
		supplierPicture.setMessageDate(DateTimeUtil.getDateTime());
		supplierPicture.setSupplierId(message.getSupplierId());
		supplierPicture.setSupplierName(message.getSupplierName());
		ProductPicture productPicture = new ProductPicture();
		productPicture.setSupplierSpuNo(hubSpu.getSupplierSpuNo());
		productPicture.setImages(images);
		supplierPicture.setProductPicture(productPicture);
		return supplierPicture;
	}


	private List<Image> converImage(List<String> imgUrl){
		List<Image> images = new ArrayList<Image>();
		if(null!=imgUrl&&imgUrl.size()>0){
			for(String url : imgUrl){
				Image image = new Image();
				image.setUrl(url.trim());
				images.add(image);
			}
		}

		return images;
	}

}

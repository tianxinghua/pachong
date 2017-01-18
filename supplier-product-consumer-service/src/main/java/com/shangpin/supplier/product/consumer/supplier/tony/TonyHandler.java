package com.shangpin.supplier.product.consumer.supplier.tony;

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
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerException;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.enumeration.Isexistpic;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.tony.dto.TonyItems;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:TonyHandler </p>
 * <p>Description: tony供应商处理器 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月12日 下午5:57:05
 *
 */
@Component("tonyHandler")
@Slf4j
public class TonyHandler implements ISupplierHandler {
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				TonyItems tonyItems = JsonUtil.deserialize(message.getData(), TonyItems.class);
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				List<Image> images =  converImage(tonyItems.getImages());
				if(null == images){
					hubSpu.setIsexistpic(Isexistpic.NO.getIndex());
				}else{
					hubSpu.setIsexistpic(Isexistpic.YES.getIndex()); 
				}
				boolean success = convertSpu(message.getSupplierId(), tonyItems, hubSpu,message.getData());
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
				boolean skuSucc = convertSku(message.getSupplierId(), hubSpu.getSupplierSpuId(), tonyItems, hubSku);
				if(skuSucc){
					hubSkus.add(hubSku);
				}
				//处理图片
				SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
				if(success){
					supplierProductSaveAndSendToPending.tonySaveAndSendToPending(message.getSupplierNo(),message.getSupplierId(), message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}
		} catch (EpHubSupplierProductConsumerException e) {
			log.error("tony异常："+e.getMessage(),e);
		} 
	}
	
	/**
	 * tony处理图片
	 * @param tonyImages
	 * @return
	 */
	private List<Image> converImage(String[] tonyImages){
		if(null == tonyImages || tonyImages.length == 0){
			return null;
		}else{
			List<Image> images = new ArrayList<Image>();
			for(String url : tonyImages){
				Image image = new Image();
				image.setUrl(url);
				images.add(image);
			}
			return images;
		}
	}
	
	/**
	 * 将tony原始dto转换成hub spu
	 * @param supplierId
	 * @param tonyItems
	 * @param hubSpu
	 * @return
	 */
	public boolean convertSpu(String supplierId,TonyItems tonyItems,HubSupplierSpuDto hubSpu,String data){
		if(null != tonyItems){
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(tonyItems.getSku_parent());
			hubSpu.setSupplierSpuModel(tonyItems.getMnf_code());
			hubSpu.setSupplierSpuColor(tonyItems.getColor());
			hubSpu.setSupplierGender(tonyItems.getSex());
			hubSpu.setSupplierCategoryname(tonyItems.getCat_ids());
			hubSpu.setSupplierBrandname(tonyItems.getBrand());
			hubSpu.setSupplierSeasonname(tonyItems.getSeason());
			hubSpu.setSupplierMaterial((!StringUtils.isEmpty(tonyItems.getDesc())) ? getMaterial(tonyItems.getDesc()) : getMaterial(tonyItems.getDesc_en()));
			hubSpu.setSupplierOrigin(getOrigin(tonyItems.getDesc()));
			hubSpu.setSupplierSpuDesc(tonyItems.getDesc());
			hubSpu.setMemo(data);
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 将tony原始dto转换成hub sku
	 * @param supplierId
	 * @param supplierSpuId
	 * @param tonyItems
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId,Long supplierSpuId, TonyItems tonyItems,HubSupplierSkuDto hubSku){
		if(null != tonyItems){
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierSkuNo(tonyItems.getSku());
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(tonyItems.getStock_price())));
			hubSku.setSupplierBarcode(tonyItems.getBarcode());
			hubSku.setSupplierSkuSize(tonyItems.getSize());
			hubSku.setStock(StringUtil.verifyStock(tonyItems.getQty()));
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 格式化供应商spuid
	 * @param skuId
	 * @return
	 */
//	private String getSpuId(String skuId){
//        if (skuId.contains("_")){
//            return skuId.split("_")[0];
//        } else if (skuId.contains("-")){
//            return skuId.split("-")[0];
//        }
//        return skuId;
//    }
	
	/**
	 * 格式化供应商货号
	 * @param skuId
	 * @return
	 */
//	 private String getProductCode(String skuId){
//        if (!skuId.contains("-"))
//            return skuId;
//        return skuId.split("-")[0];
//	 }
	 
	 /**
	  * 格式化材质
	  * @param desc
	  * @return
	  */
	 public String getMaterial(String desc){
        String material = "";
        if (desc == null){
            return material;
        }
        String[] descArr = desc.split("<br>");
        for (String s: descArr){
            if (s.contains("%,")){
                material = s;
                break;
            } else if ( s.contains("Leather")){	                
                material = s;
                break;
            } else  if (s.contains("leather")){
                material = "leather";
                break;
            } else if (s.contains("Nylon")){
                material = s;
                break;
            } else if (s.contains("brass")){
                material = s;
                break;
            } else if (s.contains("PVC")){
                material = "PVC";
                break;
            } else if (s.contains("fox")||s.contains("calfskin")){
                material = s;
                break;
            } else if (s.contains("Sheared fabric")){
                material = "Sheared fabric";
                break;
            } else if (s.contains("GG fabric")){
                material = "GG fabric";
                break;
            } else if (s.contains("Elaphe snakeskin")){
                material = "Elaphe snakeskin";
                break;
            }
        }
        return material;
	 }
	 
	 private String getOrigin(String desc){
		 int index = desc.lastIndexOf("Made in");
     	if(index!=-1){
     		desc = desc.substring(desc.lastIndexOf("Made in"));
     		if(desc.indexOf("<br>")!=-1){
     			return desc.substring(0,desc.indexOf("<br>"));
     		}
     	}
     	return "";
	 }
	 
//	 private String getProductSize(String skuId){
//        if (!skuId.contains("_") || !skuId.contains("-"))
//            return "";
//        return skuId.split("_")[1].split("-")[1];
//    }

}

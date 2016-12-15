package com.shangpin.supplier.product.consumer.supplier.tony;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.shangpin.supplier.product.consumer.conf.client.mysql.sku.bean.HubSupplierSku;
import com.shangpin.supplier.product.consumer.conf.client.mysql.spu.bean.HubSupplierSpu;
import com.shangpin.supplier.product.consumer.conf.stream.sink.message.SupplierProduct;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.tony.dto.TonyItems;

/**
 * <p>Title:TonyHandler </p>
 * <p>Description: tony供应商处理器 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月12日 下午5:57:05
 *
 */
@Component("tonyHandler")
public class TonyHandler implements ISupplierHandler {

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		if(!StringUtils.isEmpty(message.getData())){
			TonyItems tonyItems = new Gson().fromJson(message.getData(), TonyItems.class);
			HubSupplierSpu hubSpu = new HubSupplierSpu();
			boolean success = convertSpu(message.getData(), tonyItems, hubSpu);
			if(success){
				//TODO save hubSpu
			}
			HubSupplierSku hubSku = new HubSupplierSku();
			boolean skuSucc = convertSku(message.getData(), hubSpu.getSupplierSpuId(), tonyItems, hubSku);
			if(skuSucc){
				// TODO save hub sku
			}
		}
		
	}
	
	/**
	 * 将tony原始dto转换成hub spu
	 * @param supplierId
	 * @param tonyItems
	 * @param hubSpu
	 * @return
	 */
	public boolean convertSpu(String supplierId,TonyItems tonyItems,HubSupplierSpu hubSpu){
		if(null != tonyItems){
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(getSpuId(tonyItems.getSku()));
			hubSpu.setSupplierSpuModel(getProductCode(tonyItems.getSku()));
			hubSpu.setSupplierSpuColor(tonyItems.getColor());
			hubSpu.setSupplierGender(tonyItems.getSex());
			hubSpu.setSupplierCategoryname(tonyItems.getCat_ids());
			hubSpu.setSupplierBrandname(tonyItems.getBrand());
			hubSpu.setSupplierSeasonname(tonyItems.getSeason());
			hubSpu.setSupplierMaterial((!StringUtils.isEmpty(tonyItems.getDesc())) ? getMaterial(tonyItems.getDesc()) : getMaterial(tonyItems.getDesc_en()));
			hubSpu.setSupplierOrigin(getOrigin(tonyItems.getDesc()));
			hubSpu.setSupplierSpuDesc(tonyItems.getDesc());
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
	public boolean convertSku(String supplierId,Long supplierSpuId, TonyItems tonyItems,HubSupplierSku hubSku){
		if(null != tonyItems){
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierSkuNo(tonyItems.getSku());
			hubSku.setMarketPrice(new BigDecimal(tonyItems.getStock_price()));
			hubSku.setSupplierBarcode(tonyItems.getBarcode());
			hubSku.setSupplierSkuSize(getProductSize(tonyItems.getSku()));
			hubSku.setStock(Integer.parseInt(tonyItems.getQty()));
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
	private String getSpuId(String skuId){
        if (skuId.contains("_")){
            return skuId.split("_")[0];
        } else if (skuId.contains("-")){
            return skuId.split("-")[0];
        }
        return skuId;
    }
	
	/**
	 * 格式化供应商货号
	 * @param skuId
	 * @return
	 */
	 private String getProductCode(String skuId){
        if (!skuId.contains("-"))
            return skuId;
        return skuId.split("-")[0];
	 }
	 
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
            } else if ( s.contains("Leather")){	                
                material = s;
            } else  if (s.contains("leather")){
                material = "leather";
            } else if (s.contains("Nylon")){
                material = s;
            } else if (s.contains("brass")){
                material = s;
            } else if (s.contains("PVC")){
                material = "PVC";
            } else if (s.contains("fox")||s.contains("calfskin")){
                material = s;
            } else if (s.contains("Sheared fabric")){
                material = "Sheared fabric";
            } else if (s.contains("GG fabric")){
                material = "GG fabric";
            } else if (s.contains("Elaphe snakeskin")){
                material = "Elaphe snakeskin";
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
	 
	 private String getProductSize(String skuId){
        if (!skuId.contains("_") || !skuId.contains("-"))
            return "";
        return skuId.split("_")[1].split("-")[1];
    }

}

package com.shangpin.supplier.product.consumer.supplier.tony;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
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
			//hubSpu.setSupplierCategoryname(supplierCategoryname);
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
	private static String getSpuId(String skuId){
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
	 private static String getProductCode(String skuId){
	        if (!skuId.contains("-"))
	            return skuId;
	        return skuId.split("-")[0];
	 }

}

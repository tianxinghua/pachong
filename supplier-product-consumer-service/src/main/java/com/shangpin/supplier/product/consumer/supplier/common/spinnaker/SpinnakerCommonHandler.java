package com.shangpin.supplier.product.consumer.supplier.common.spinnaker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.supplier.product.consumer.supplier.common.spinnaker.dto.Sku;
import com.shangpin.supplier.product.consumer.supplier.common.spinnaker.dto.Spu;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;

/**
 * <p>Title:SpinnakerCommonHandler </p>
 * <p>Description: spinnaker供应商一般处理逻辑类 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月10日 下午2:59:57
 *
 */
@Component
public class SpinnakerCommonHandler extends ISpinnakerHandler {

	@Override
	public boolean convertSpu(String supplierId, Spu spu, Sku sku, HubSupplierSpuDto hubSpu,String data) {
		if(null != spu){
			
			String colorCode = "";
			String colorName = "";
			String color = sku.getColor();
			if(color.contains(" ")){
				String firstStr = color.substring(0, color.indexOf(" "));
				if(hasDigit(firstStr)){
					colorCode = firstStr;
					colorName = color.substring(color.indexOf(" ")).trim();
				}else{
					colorCode = color;
					colorName = color;
				}
			}else{
				colorCode = color;
				colorName = color;
			}
			
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(spu.getProduct_id()+"-"+colorCode);
			hubSpu.setSupplierSpuModel(spu.getProduct_name()+" "+colorCode);
			hubSpu.setSupplierSpuName(spu.getDescription());
			hubSpu.setSupplierSpuColor(colorName);
			hubSpu.setSupplierGender(spu.getType());
			hubSpu.setSupplierCategoryname(spu.getCategory());
			hubSpu.setSupplierBrandname(spu.getProducer_id());
			hubSpu.setSupplierSeasonname(spu.getSeason());
			hubSpu.setSupplierMaterial(spu.getProduct_detail());
			hubSpu.setSupplierOrigin(spu.getProduct_MadeIn());
			hubSpu.setSupplierSpuDesc(spu.getProduct_detail()); 
			hubSpu.setMemo(data);
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean convertSku(String supplierId, Long supplierSpuId, Sku sku, HubSupplierSkuDto hubSku) {
		if(null != sku){
			
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(sku.getBarcode());
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(sku.getPrice().getMarket_price())));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(sku.getPrice().getSuply_price()))); 
			hubSku.setSupplierBarcode(sku.getBarcode());
			if(sku.getItem_size().length()>4) {
				hubSku.setSupplierSkuSize(sku.getItem_size().substring(0,sku.getItem_size().length()-4));
            }else{
            	hubSku.setSupplierSkuSize(sku.getItem_size());
            }
			String stock = sku.getStock();
			if (StringUtils.isEmpty(stock)) {
				stock = "0";
			}else if (Integer.valueOf(stock) <= 0) {
				stock = "0";
			}
			hubSku.setStock(Integer.valueOf(stock)); 
			return true;
		}else{
			return false;
		}
	}

	@Override
	public List<Image> converImage(Sku sku) {
		if(null == sku || null == sku.getPictures() || sku.getPictures().size() == 0){
			return null;
		}else{
			List<Image> images = new ArrayList<Image>();
			for(String url : sku.getPictures()){
				Image image = new Image();
				image.setUrl(url);
				images.add(image);
			}
			return images;
		}
	}
	
	/**
	 * 判断字符串中是否含有数字
	 * @param content
	 * @return
	 */
	private boolean hasDigit(String content) {
		boolean flag = false;
		Pattern p = Pattern.compile(".*\\d+.*");
		Matcher m = p.matcher(content);
		if (m.matches())
			flag = true;
		return flag;
	}

}

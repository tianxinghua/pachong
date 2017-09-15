package com.shangpin.supplier.product.consumer.supplier.bagheera;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.supplier.product.consumer.supplier.common.atelier.AtelierCommonHandler;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierSpu;
/**
 * <p>Title: BagheeraHandler</p>
 * <p>Description: 8月29日更换系统，改为了atelier系统,此类不可删除或注销 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年8月29日 下午4:05:00
 *
 */
@Component("bagheeraHandler")
public class BagheeraHandler extends AtelierCommonHandler {
	
	@Override
	public AtelierSpu handleSpuData(String spuColumn){
		if(!StringUtils.isEmpty(spuColumn)){
			String[] spuArr = spuColumn.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
			AtelierSpu atelierSpu = new AtelierSpu();
			atelierSpu.setSpuId(spuArr[0]);
			atelierSpu.setSeasonName(spuArr[1]);
			atelierSpu.setBrandName(spuArr[2]);
			atelierSpu.setStyleCode(spuArr[3]);
			atelierSpu.setColorCode(spuArr[4]);
			atelierSpu.setCategoryGender(spuArr[5]);
			atelierSpu.setCategoryName(spuArr[8]);
			atelierSpu.setColorName(StringUtils.isEmpty(spuArr[10])? spuArr[4] : spuArr[10]);
			atelierSpu.setMaterial1(spuArr[14]);
			atelierSpu.setDescription(spuArr[15]+spuArr[34]); 
			atelierSpu.setSupplierPrice(spuArr[16]);
			atelierSpu.setMaterial3(spuArr[42]);
			atelierSpu.setProductOrigin(spuArr[14]); 
			atelierSpu.setSizeDetail(spuArr[14]); 
			atelierSpu.setSizeType(spuArr[20]);



			atelierSpu.setMaterial1(spuArr[11]);


			atelierSpu.setProductOrigin(spuArr[40]);





			return atelierSpu;
		}else{
			return null;
		}
	}

}

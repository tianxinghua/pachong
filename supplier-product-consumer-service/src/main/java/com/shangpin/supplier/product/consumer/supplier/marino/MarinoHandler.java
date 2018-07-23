package com.shangpin.supplier.product.consumer.supplier.marino;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.AtelierCommonHandler;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.IAtelierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierPrice;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierSku;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierSpu;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * *
 * <p>Title:MarinoHandler </p>
 * <p>Description: ostore供应商原始数据处理器</p>
 * <p>Company: www.shangpin.com</p>
 * @author lubaijiang
 * @date 2016年12月8日 下午4:00:05
 *
 */
@Component("marinoHandler")
public class MarinoHandler extends IAtelierHandler{

	@Autowired
	private AtelierCommonHandler atelierCommonHandler;

	@Override
	public AtelierSpu handleSpuData(String spuColumn) {

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
			String str=spuArr[15]+spuArr[34];
			//获取含有% 的个数 作为判断 为1 或多个
			int count = queryCount(str);
			if (spuArr[11]!=null&&!spuArr[11].equals("")){
				atelierSpu.setMaterial1(spuArr[11]);
			}else {
				if (count==1){
					String ss = sub(str);
					String trim = ss.substring(ss.indexOf("%") - 3).trim();
					atelierSpu.setMaterial1(trim);
				}else {
					String array = Array(str);
					atelierSpu.setMaterial1(array);
				}
			}
			//atelierSpu.setMaterial1(spuArr[11]);
			atelierSpu.setDescription(spuArr[15]+spuArr[34]);
			atelierSpu.setSupplierPrice(spuArr[16]);
			atelierSpu.setMaterial3(spuArr[42]);
			atelierSpu.setProductOrigin(spuArr[40]);
			atelierSpu.setSizeDetail(spuArr[14]);
			atelierSpu.setSizeType(spuArr[20]);
			return atelierSpu;
		}else{
			return null;
		}
	}
	public int queryCount(String str){


		String[] split = str.split(";");
		StringBuffer buffer = new StringBuffer(split.length);
		String s=null;
		for (int i = 0; i < split.length; i++) {
			s = split[0];

		}
		Pattern pattern = Pattern.compile("%");
		Matcher matcher = pattern.matcher(s);
		int count=0;
		while (matcher.find()){ // 如果匹配,则数量+1
			count++;
		}
		return count;
	}
	public String sub(String str){
		String[] split = str.split(";");
		String s=null;
		for (int i = 0; i < split.length; i++) {
			s = split[0];
		}
		return s;
	}
	public String Array(String str){
		String arr= sub(str);
		String[] strings = arr.split(" ");
		String s2="";
		for (int i = 0; i < strings.length; i++) {
			String s = strings[i];
			// System.out.println(s);
			if (s.contains("%")){
				s2+=s;
			}
		}
		return s2;
	}

	@Override
	public AtelierSku handleSkuData(String skuColumn) {

		return atelierCommonHandler.handleSkuData(skuColumn);
	}

	@Override
	public AtelierPrice handlePriceData(String priceColumn) {

		return atelierCommonHandler.handlePriceData(priceColumn);
	}

	@Override
	public void setProductPrice(HubSupplierSkuDto hubSku, AtelierSpu atelierSpu, AtelierPrice atelierPrice) {
		if("A16".equals(atelierSpu.getSeasonName())){
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(atelierPrice.getPrice3())));
		}else{
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(atelierPrice.getPrice2())));
		}
		hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(atelierPrice.getPrice1())));

	}

	@Override
	public List<Image> converImage(List<String> atelierImags) {
		return atelierCommonHandler.converImage(atelierImags);
	}


}

package com.shangpin.api.airshop.product.d;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.csvreader.CsvReader;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class ReadCSV {
	 
	public static List<ProductDTO> readLocalCSV(InputStream in)
			throws Exception {
		List<ProductDTO> dtoList = new ArrayList<ProductDTO>();
		int i = 0;
		String rowString = null;
		CsvReader cr = new CsvReader(in, Charset.forName("utf-8"));
		log.info("创建cr对象成功");
		// 得到列名集合
		cr.readRecord();
		Pattern pattern = Pattern.compile("\\G(?:^|,)(?:\"([^\"]*+(?:\"\"[^\"]*+)*+)\"|([^\",]*+))");
		
		while (cr.readRecord()) {
			List<String> colValueList = new ArrayList<String>();
			i++;
			System.out.println(i);
			rowString = cr.getRawRecord();
			Matcher matcher = pattern.matcher(rowString);
			
			 while (matcher.find()) {
				 String temp = matcher.group();
				 if(StringUtils.isNotBlank(temp)){
					 if(temp.startsWith(",")){
						 colValueList.add(temp.trim().substring(1));
					 }else{
						 colValueList.add(temp);
					 }
				 }
				
			}
			 if(colValueList!=null&&!colValueList.isEmpty()){
				 boolean flag = true;
				 ProductDTO item = new ProductDTO();
					if (StringUtils.isNotBlank(colValueList.get(0))) {
						item.setCategoryName(colValueList.get(0));
					}else{
						flag = false;
					}
					if (StringUtils.isNotBlank(colValueList.get(1))) {
						item.setBrandName(colValueList.get(1));
					}else{
						flag = false;
					}
					if (StringUtils.isNotBlank(colValueList.get(2))) {
						item.setProductCode(colValueList.get(2));
					}else{
						flag = false;
					}
					if (StringUtils.isNotBlank(colValueList.get(4))) {
						item.setSeasonName(colValueList.get(3) +"-"+ colValueList.get(4));
					}
					if (StringUtils.isNotBlank(colValueList.get(5))) {
						item.setCategoryGender(colValueList.get(5));
					}
				
					if (StringUtils.isNotBlank(colValueList.get(6))) {
						item.setSkuId(colValueList.get(6));
					}else{
						flag = false;
					}
					if (StringUtils.isNotBlank(colValueList.get(7))) {
						String m = colValueList.get(7);
						if(m.startsWith("\"")){
							item.setProductName(m.substring(1,m.length()-1));
						}else{
							item.setProductName(m);
						}
					}
					if (StringUtils.isNotBlank(colValueList.get(8))) {
						item.setBarcode(colValueList.get(8));
					}
					if (StringUtils.isNotBlank(colValueList.get(9))) {
						item.setColor(colValueList.get(9));
					}else{
						flag = false;
					}
					if (StringUtils.isNotBlank(colValueList.get(11))) {
						item.setSize(colValueList.get(10) + "-" + colValueList.get(11));
					}else{
						flag = false;
					}
					if (StringUtils.isNotBlank(colValueList.get(12))) {
						String m = colValueList.get(12);
						if(m.startsWith("\"")){
							item.setMaterial(m.substring(1,m.length()-1));
						}else{
							item.setMaterial(m);
						}
					}
					if (StringUtils.isNotBlank(colValueList.get(13))) {
						item.setProductOrigin(colValueList.get(13));
					}
					if (StringUtils.isNotBlank(colValueList.get(14))) {
						item.setStock((int) Double.parseDouble(colValueList.get(14)));
					}else{
						item.setStock(1);
					}
					if (StringUtils.isNotBlank(colValueList.get(15))) {
						item.setMarketPrice(new BigDecimal(colValueList.get(15)));
					}
					if (StringUtils.isNotBlank(colValueList.get(16))) {
						item.setSupplierPrice(new BigDecimal(colValueList.get(16)));
					}
					if (StringUtils.isNotBlank(colValueList.get(17))) {
						item.setSaleCurrency(colValueList.get(17));
					}
					StringBuffer str = new StringBuffer();
					for (int j = 18; j < colValueList.size(); j++) {
						if(StringUtils.isNotBlank(colValueList.get(j))){
							str.append("|" + colValueList.get(j));
						}else{
							break;
						}
					}
					if (str.length() > 0) {
						item.setSkuPicture(str.toString().substring(1));
					}
					if(flag){
						item.setSpuId(item.getProductCode()
								+ item.getColor());
						dtoList.add(item);
					}
			 }
			
		}
		cr.close();
		return dtoList;
	}
}

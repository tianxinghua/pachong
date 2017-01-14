package com.shangpin.ephub.client.product.business.hubproduct.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubProductDto implements Serializable {
	
	private String categoryName;
	private String categoryNo;
	private String brandNo;
	private String brandName;
	private String spuModel;
	private String marketTime;
	private String season;
	private String gender;
	private String hubColor;
	private String specificationType;
	private String sizeType;
	private String skuSize;
	private String material;
	private String origin;
	
	public static String [] getHubProductTemplate(){
		 String[] headers = {"品类名称","品类编号*" ,"品牌编号*","品牌名称","货号*","上市年份*","上市季节*","适应性别*",
					"颜色*","规格类型","原尺码类型","原尺码值","材质*","产地*"};
		 return headers;
	}
	public static String [] getHubProductValueTemplate(){
		 String[] headers = { "categoryName", "categoryNo","brandNo","brandName","spuModel","marketTime","season","gender",
					"hubColor","specificationType","sizeType","skuSize","material","origin"};
		 return headers;
	 }
	private static final long serialVersionUID = 2215182249308660796L;
}

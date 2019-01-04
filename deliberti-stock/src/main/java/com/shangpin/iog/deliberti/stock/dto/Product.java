package com.shangpin.iog.deliberti.stock.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.StringUtils;

@Getter
@Setter
public class Product {
	private String spuId;
	private String brandCode;
	private String brandDesc;
	private String seasonCode;
	private String seasonDesc;
	private String genderCode;
	private String genderDesc;
	private String MainCategorycode;
	private String MainCategoryDesc;
	private String SubCategoryCode;
	private String SubCategoryDesc;
	private String buyPrice;
	private String retailPrice;
	private String currentPrice;
	private String size35;
	private String size35x;
	private String size36;
	private String size36x;
	private String size37;
	private String size37x;
	private String size38;
	private String size38x;
	private String size39;
	private String size39x;
	private String size40;
	private String size40x;
	private String size41;
	private String size41x;
	private String size42;
	private String size42x;
	private String size43;
	private String size43x;
	private String size44;
	private String size44x;
	private String size45;
	private String size45x;
	private String size46;
	private String size46x;
	private String size47;
	private String size47x;
	private String size48;
	private String size48x;
	private String size49;
	private String size49x;
	private String image;
	private String groupId;
	private String articleCode;// 货号
	private String material;// 材质
	private String color;// 颜色
	private String colorDesc;
	private String description;// 描述

	public List<String> getStock() {
		List<String> returnList = new ArrayList();
		String[] sizes = { this.size35, this.size35x, this.size36,
				this.size36x, this.size37, this.size37x, this.size38,
				this.size38x, this.size39, this.size39x, this.size40,
				this.size40x, this.size41, this.size41x, this.size42,
				this.size42x, this.size43, this.size43x, this.size44,
				this.size44x, this.size45, this.size45x, this.size46,
				this.size46x, this.size47, this.size47x, this.size48,
				this.size48x, this.size49, this.size49x };
		String replace = "";
		for (String size : sizes) {
			if (!StringUtils.isBlank(size)) {
				if ((!size.contains("0")) && (!size.contains("-"))) {
					replace = size.replace("x", ".5");
					returnList.add(replace);
				}
			}
		}
		return returnList;
	}
}

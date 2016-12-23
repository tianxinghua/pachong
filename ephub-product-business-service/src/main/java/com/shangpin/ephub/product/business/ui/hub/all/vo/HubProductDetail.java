package com.shangpin.ephub.product.business.ui.hub.all.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HubProductDetail {

	private List<HubProduct> hubDetails;
	private String categoryNo;
	private String brandNo;
	private String originalProductModle;//原厂货号
	private String productName;//商品名称 spu_name
	private String productUnit;//商品单位
	private String homeMarketPrice;//国内市场价
}

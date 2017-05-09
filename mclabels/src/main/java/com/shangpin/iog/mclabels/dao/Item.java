package com.shangpin.iog.mclabels.dao;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Item {

	private String _id;
	private String channel_id;
	private String Sku;
	private String Title;
	private String ShortDescription;
	private String Description;
	private String Weight;
	private String EAN;
	private String Manufacturer;
	private String Brand;
	private String Condition;
	private String Height;
	private String Length;
	private String Width;
	private String Classification;
	private AttributeList AttributeList;
	private DistributionCenterList DistributionCenterList;
	private PriceInfo PriceInfo;
	private VariationInfo VariationInfo;
	private StoreInfo StoreInfo;
	private ImageList ImageList;
}

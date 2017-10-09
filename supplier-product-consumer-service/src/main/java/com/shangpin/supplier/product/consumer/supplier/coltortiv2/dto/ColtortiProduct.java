package com.shangpin.supplier.product.consumer.supplier.coltortiv2.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColtortiProduct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -317402101407867718L;
	
	private String supplierSpuNo;
	
	private Name name;
	private Description description;
	private String product_id;
	private String variation_id;  //颜色编号
	private String variation_name;  //颜色
	private String retail_price;
	private String sizerange_id;
	private List<String> other_ids;
	private String ms5_group_id;
	private String ms5_subgroup_id;
	private String ms5_category_id;
	private String brand_id;
	private String season_id;
	private String family_id;
	private String line_id;
	private List<String> images;
	private String macro_category_id;
	private String group_id;
	private String subgroup_id;
	private String category_id;
	private String updated_at;
	
	private List<ColtortiSkuDto> coltortiSkus;
	private String seasonName;
	private String brandName;
	private String categoryName;
	private String genderName;
	private Map<String,Map<String,Object>> attributes;
	private DiscountDto discount;

}

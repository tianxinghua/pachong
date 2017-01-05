package com.shangpin.ephub.product.business.ui.hub.all.vo;

import java.util.List;

import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPicDto;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title:HubProductDetails </p>
 * <p>Description: hub详情页的实体</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月23日 下午7:50:34
 *
 */
@Getter
@Setter
public class HubProductDetails {

	private List<HubProductDetail> hubDetails;
	private Long spuId;//主键，必须有
	private String categoryNo;
	private String brandNo;
	private String originalProductModle;//原厂货号
	private String productName;//商品名称 spu_name
	private String productUnit;//商品单位
	private String homeMarketPrice;//国内市场价
	private List<HubSpuPicDto> spPicUrls;
}

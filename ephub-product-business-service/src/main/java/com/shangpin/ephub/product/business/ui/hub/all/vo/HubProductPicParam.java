package com.shangpin.ephub.product.business.ui.hub.all.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Title:HubProductDto </p>
 * <p>Description: hub详情页上的单个实体类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月22日 下午2:42:54
 *
 */
@Getter
@Setter
public class HubProductPicParam{
	private static final long serialVersionUID = -6160605233668373417L;
	
	private String spuId;
	private String spuPicId;
	private String spPicUrl;
}

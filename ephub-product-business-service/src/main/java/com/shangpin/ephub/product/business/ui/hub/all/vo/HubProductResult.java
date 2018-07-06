package com.shangpin.ephub.product.business.ui.hub.all.vo;

import java.util.List;

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
public class HubProductResult{
	private static final long serialVersionUID = -6060605233668373417L;
	
	private List<HubProductSpuModel> hubProductSpuModel;//货号
	private int total;
}

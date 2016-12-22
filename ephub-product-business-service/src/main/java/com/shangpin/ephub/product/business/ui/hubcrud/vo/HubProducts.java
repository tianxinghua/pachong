package com.shangpin.ephub.product.business.ui.hubcrud.vo;

import java.util.List;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <p>Title:HubProducts </p>
 * <p>Description: hub页面返回数据实体</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月22日 下午2:38:33
 *
 */
@Getter
@Setter
public class HubProducts {

	private List<HubSpuDto> hubProducts;
	private int total;
}

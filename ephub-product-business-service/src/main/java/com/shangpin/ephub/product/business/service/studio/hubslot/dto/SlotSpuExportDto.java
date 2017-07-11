package com.shangpin.ephub.product.business.service.studio.hubslot.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: SlotSpuExportDto</p>
 * <p>Description: 已提交页面导出实体 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月11日 下午3:28:20
 *
 */
@Getter
@Setter
public class SlotSpuExportDto extends SlotSpuDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2528909338966058899L;
	
	private String hubBrandName;
	private String hubCategoryName;
	
}

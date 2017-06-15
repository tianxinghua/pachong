package com.shangpin.ephub.product.business.ui.studio.defective.vo;

import java.util.List;

import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuDto;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: DefectiveProductVo</p>
 * <p>Description: 残次品记录 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月13日 下午4:07:14
 *
 */
@Getter
@Setter
public class DefectiveProductVo {

	/**
	 * 残次品记录
	 */
	private List<StudioSlotDefectiveSpuDto> defectiveSpus;
}

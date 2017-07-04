package com.shangpin.ephub.product.business.ui.studio.slot.vo.detail;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title: SlotDetailVo</p>
 * <p>Description: 拍摄批次详情页返回的实体 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月4日 下午2:15:53
 *
 */
@Getter
@Setter
public class SlotDetailVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2480083405274324429L;
	/**
	 * 批次基本信息
	 */
	private SlotInfo slotInfo;
	/**
	 * 批次商品信息
	 */
	private List<SlotSpuDetail> slotSpuDetails;

}

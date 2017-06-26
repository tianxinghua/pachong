package com.shangpin.ephub.product.business.ui.studio.defective.service;

import java.util.List;

import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicDto;
import com.shangpin.ephub.product.business.ui.studio.defective.dto.DefectiveQuery;
import com.shangpin.ephub.product.business.ui.studio.defective.vo.DefectiveProductVo;

/**
 * <p>Title: DefectiveProductService</p>
 * <p>Description: 残次品处理页面 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月13日 下午3:05:46
 *
 */
public interface DefectiveProductService {

	/**
	 * 残次品列表
	 * @param studioNo 摄影棚编号
	 * @return
	 */
	public DefectiveProductVo list(DefectiveQuery defectiveQuery); 
	/**
	 * 残次品如果存在则查询，否则添加
	 * @param slotNoSpuId
	 * @return 该残次品实体
	 */
	public StudioSlotDefectiveSpuDto add(String slotNoSpuId);
	/**
	 * 添加残次品图片
	 * @param defctiveSouDot 
	 * @return
	 */
	public Long insert(StudioSlotDefectiveSpuDto defctiveSouDot, String spPicUrl, String extension);
	/**
	 * 根据报残表主键查找查找所有图片
	 * @param studioSlotDefectiveSpuId
	 * @return
	 */
	public List<StudioSlotDefectiveSpuPicDto> selectDefectivePic(String studioSlotDefectiveSpuId);
	/**
	 * 根据主键查找
	 * @param studioSlotDefectiveSpuId
	 * @return
	 */
	public StudioSlotDefectiveSpuDto selectByPrimarykey(Long studioSlotDefectiveSpuId);
	/**
	 * 判断是否含有该图片url
	 * @param spPicUrl
	 * @return
	 */
	public boolean hasDefectiveSpuPic(String spPicUrl);
	/**
	 * 删除图片
	 * @param spPicUrl
	 * @return
	 */
	public boolean deleteDefectivePic(String spPicUrl);
}

package com.shangpin.picture.product.consumer.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.fdfs.dto.UploadPicDto;
import com.shangpin.ephub.client.fdfs.gateway.UploadPicGateway;

/**
 * <p>Title:SupplierProductPictureManager.java </p>
 * <p>Description: 负责外部系统调用的管理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月2日 下午12:44:02
 */
@Component
public class SupplierProductPictureManager {
	
	@Autowired
	private HubSpuPendingPicGateWay hubSpuPendingPicGateWay;
	
	@Autowired
	private UploadPicGateway uploadPicGateway;
	/**
	 * 保存初始化数据
	 * @param picDto
	 * @return 主键返回
	 */
	public Long save(HubSpuPendingPicDto picDto) {
		return hubSpuPendingPicGateWay.insert(picDto);
	}
	/**
	 * 上传图片
	 * @param uploadPicDto
	 */
	public String uploadPic(UploadPicDto uploadPicDto) {
		return uploadPicGateway.upload(uploadPicDto);
	}
	/**
	 * 更新数据表
	 * @param updateDto
	 */
	public void updateSelective(HubSpuPendingPicDto updateDto) {
		hubSpuPendingPicGateWay.updateByPrimaryKeySelective(updateDto);
	}
	/**
	 * 查询该spu是否有图片
	 * @param supplierSpuId 供应商原始spu表id
	 * @return 检查是否存在，返回原始url和处理状态集合
	 */
	public Map<String,Byte> exists(Long supplierSpuId) {
		Map<String,Byte> picUrlMaps = new HashMap<String,Byte>();
		List<HubSpuPendingPicDto> lists = selectHubSpuPendingPic(supplierSpuId);
		if (CollectionUtils.isNotEmpty(lists)) {
			for(HubSpuPendingPicDto dto : lists){
				picUrlMaps.put(dto.getPicUrl(), dto.getPicHandleState());
			}
		}
		return picUrlMaps;
	}
	/**
	 * 查找图片
	 * @param supplierSpuId 供应商原始spu表id
	 * @return
	 */
	public List<HubSpuPendingPicDto> selectHubSpuPendingPic(Long supplierSpuId) {
		HubSpuPendingPicCriteriaDto criteria = new HubSpuPendingPicCriteriaDto();
		criteria.createCriteria().andSupplierSpuIdEqualTo(supplierSpuId);
		criteria.setFields("pic_url,pic_handle_state");
		List<HubSpuPendingPicDto> lists = hubSpuPendingPicGateWay.selectByCriteria(criteria);
		return lists;
	}
	/**
	 * 根据查询条件查询总记录数
	 * @param criteria
	 * @return
	 */
	public int countFailedPictureTotal(HubSpuPendingPicCriteriaDto criteria) {
		return hubSpuPendingPicGateWay.countByCriteria(criteria);
	}
	/**
	 * 根据条件查询数据
	 * @param criteria 查询条件对象
	 * @return 数据
	 */
	public List<HubSpuPendingPicDto> queryByCriteria(HubSpuPendingPicCriteriaDto criteria) {
		return hubSpuPendingPicGateWay.selectByCriteria(criteria);
	}
	/**
	 * 根据主键查询
	 * @param spuPendingPicId 主键
	 * @return 数据
	 */
	public HubSpuPendingPicDto queryById(Long spuPendingPicId) {
		return hubSpuPendingPicGateWay.selectByPrimaryKey(spuPendingPicId);
	}

}

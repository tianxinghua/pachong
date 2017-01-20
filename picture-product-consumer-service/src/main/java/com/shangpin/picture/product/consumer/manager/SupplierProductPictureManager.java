package com.shangpin.picture.product.consumer.manager;

import java.util.List;

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
	 * @param supplierId 供应商门户id
	 * @param picUrl 原始图片地址
	 * @return 检查是否存在，如果存在则返回true，否则返回false
	 */
	public boolean exists(String supplierId,String picUrl) {
		HubSpuPendingPicCriteriaDto criteria = new HubSpuPendingPicCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(supplierId).andPicUrlEqualTo(picUrl);
		criteria.setFields("spu_pending_pic_id");
		if (CollectionUtils.isEmpty(hubSpuPendingPicGateWay.selectByCriteria(criteria))) {
			return true;
		} else {
			return false;
		}
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

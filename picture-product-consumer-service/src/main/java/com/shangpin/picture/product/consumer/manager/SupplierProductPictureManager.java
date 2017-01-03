package com.shangpin.picture.product.consumer.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

}

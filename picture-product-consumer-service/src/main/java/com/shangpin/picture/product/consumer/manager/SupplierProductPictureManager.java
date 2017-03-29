package com.shangpin.picture.product.consumer.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDeletedCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDeletedDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicDeletedGateWay;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.fdfs.dto.DeletePicDto;
import com.shangpin.ephub.client.fdfs.dto.UploadPicDto;
import com.shangpin.ephub.client.fdfs.gateway.DeletePicGateWay;
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
	private HubSpuPendingPicDeletedGateWay hubSpuPendingPicDeletedGateWay;	
	@Autowired
	private UploadPicGateway uploadPicGateway;
	
	@Autowired
	private DeletePicGateWay deletePicGateWay;
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
//	/**
//	 * 查询该spu是否有图片
//	 * @param supplierSpuId 供应商原始spu表id
//	 * @return 检查是否存在，返回原始url和处理状态集合
//	 */
//	public Map<String,Byte> exists(Long supplierSpuId) {
//		Map<String,Byte> picUrlMaps = new HashMap<String,Byte>();
//		List<HubSpuPendingPicDto> lists = selectHubSpuPendingPic(supplierSpuId);
//		if (CollectionUtils.isNotEmpty(lists)) {
//			for(HubSpuPendingPicDto dto : lists){
//				picUrlMaps.put(dto.getPicUrl(), dto.getPicHandleState());
//			}
//		}
//		return picUrlMaps;
//	}
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
	/**
	 * 根据数据库表的主键删除记录
	 * @param spuPendingPicId
	 */
	public void deleteById(Long spuPendingPicId) {
		hubSpuPendingPicGateWay.deleteByPrimaryKey(spuPendingPicId);
	}
	/**
	 * 删除图片之前备份图片信息数据
	 * @param hubSpuPendingPicDto 需要备份的数据
	 * @throws Throwable 
	 * @throws IllegalAccessException 
	 */
	public void backupHubSpuPendingPicDtoToDeleted(HubSpuPendingPicDto hubSpuPendingPicDto) throws Throwable {
		HubSpuPendingPicDeletedCriteriaDto criteriaDto = new HubSpuPendingPicDeletedCriteriaDto();
		criteriaDto.createCriteria().andSpPicUrlEqualTo(hubSpuPendingPicDto.getSpPicUrl());
		List<HubSpuPendingPicDeletedDto> list = hubSpuPendingPicDeletedGateWay.selectByCriteria(criteriaDto);
		if (CollectionUtils.isEmpty(list)) {
		HubSpuPendingPicDeletedDto deletedDto = new HubSpuPendingPicDeletedDto();
		BeanUtils.copyProperties(deletedDto, hubSpuPendingPicDto);
		deletedDto.setSpuPendingPicId(null);
		hubSpuPendingPicDeletedGateWay.insert(deletedDto);
		}
	}
	/**
	 * 清除图片
	 * @param hubSpuPendingPicDto 图片数据
	 */
	public void deleteImageAndSetNull(HubSpuPendingPicDto hubSpuPendingPicDto) {
		DeletePicDto deletePicDto = new DeletePicDto();
		List<String> urls = new ArrayList<>();
		urls.add(hubSpuPendingPicDto.getSpPicUrl());
		deletePicDto.setUrls(urls);
		Map<String, Integer> result = deletePicGateWay.delete(deletePicDto);
		Integer code = result.get(hubSpuPendingPicDto.getSpPicUrl());
		if (code == 0) {
			hubSpuPendingPicDto.setSpPicUrl(null);
			hubSpuPendingPicGateWay.updateByPrimaryKey(hubSpuPendingPicDto);
		} else {
			throw new RuntimeException("删除图片时发生异常【"+hubSpuPendingPicDto+"】");
		}
	}

}

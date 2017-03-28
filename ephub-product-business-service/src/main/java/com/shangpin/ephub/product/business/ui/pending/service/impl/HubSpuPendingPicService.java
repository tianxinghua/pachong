package com.shangpin.ephub.product.business.ui.pending.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.consumer.picture.dto.RetryPictureDto;
import com.shangpin.ephub.client.consumer.picture.gateway.PictureGateWay;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.product.business.ui.pending.service.IHubSpuPendingPicService;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title: HubSpuPendingPicService</p>
 * <p>Description: 图片的业务实现类 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年3月28日 下午2:10:13
 *
 */
@Service
@Slf4j
public class HubSpuPendingPicService implements IHubSpuPendingPicService {
	
	@Autowired
    private HubSpuPendingPicGateWay hubSpuPendingPicGateWay;
	@Autowired
	private PictureGateWay pictureGateWay;

	@Override
	public boolean retryPictures(List<String> spPicUrl) {
		try {
			List<HubSpuPendingPicDto> lists = findPendingPics(spPicUrl);
			if(CollectionUtils.isNotEmpty(lists)){
				List<Long> ids = new ArrayList<Long>();
				for(HubSpuPendingPicDto dto : lists){
					ids.add(dto.getSpuPendingPicId());
				}
				RetryPictureDto pictureDto = new RetryPictureDto();
				pictureDto.setIds(ids); 
				pictureGateWay.retry(pictureDto); 
				return true;
			}
		} catch (Exception e) {
			log.error("重新拉去图片出错："+e.getMessage(),e);
		}
		return false;
	}
	
	@Override
    public List<HubSpuPendingPicDto> findSpPicUrl(String supplierId,String supplierSpuNo){
    	HubSpuPendingPicCriteriaDto criteria = new HubSpuPendingPicCriteriaDto();
    	criteria.setFields("sp_pic_url,memo,pic_url,pic_handle_state");
    	criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSpuNoEqualTo(supplierSpuNo);
    	List<HubSpuPendingPicDto> spuPendingPics = hubSpuPendingPicGateWay.selectByCriteria(criteria);
    	return spuPendingPics;
    }

	@Override
	public List<HubSpuPendingPicDto> findPendingPics(List<String> spPicUrl) {
		HubSpuPendingPicCriteriaDto criteria = new HubSpuPendingPicCriteriaDto();
    	criteria.setFields("spu_pending_pic_id");
    	criteria.createCriteria().andSpPicUrlIn(spPicUrl);
    	List<HubSpuPendingPicDto> spuPendingPics = hubSpuPendingPicGateWay.selectByCriteria(criteria);
    	return spuPendingPics;
	}

}

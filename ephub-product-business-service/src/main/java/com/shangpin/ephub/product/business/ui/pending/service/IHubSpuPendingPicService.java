package com.shangpin.ephub.product.business.ui.pending.service;

import java.util.List;
/**
 * <p>Title: IHubSpuPendingPicService</p>
 * <p>Description: 图片的业务类 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年3月28日 下午2:09:39
 *
 */

import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
public interface IHubSpuPendingPicService {

	/**
	 * 重新拉取图片
	 * @param spPicUrl 尚品图片链接
	 * @return
	 */
	public boolean retryPictures(List<String> spPicUrl);
	/**
	 * 根据尚品图片链接查询图片对象
	 * @param spPicUrl
	 * @return
	 */
	public List<HubSpuPendingPicDto> findPendingPics(List<String> spPicUrl);
	/**
	 * 根据供应商门户编号/供应商spu编号查询图片地址
	 * @param supplierId
	 * @param supplierSpuNo
	 * @return
	 */
	public List<HubSpuPendingPicDto> findSpPicUrl(String supplierId,String supplierSpuNo);
	
}

package com.shangpin.ephub.product.business.ui.hub.all.service;

import com.shangpin.ephub.product.business.ui.hub.all.vo.HubProductDetails;
import com.shangpin.ephub.product.business.ui.hub.all.vo.HubProducts;
import com.shangpin.ephub.product.business.ui.hub.common.dto.HubQuryDto;

/**
 * <p>Title:IHubProductService </p>
 * <p>Description: hub页面service接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月21日 下午5:16:58
 *
 */
public interface IHubProductService {

	/**
	 * 根据页面请求参数，返回hub表产品
	 * @param hubQuryDto 查询条件
	 * @return
	 */
	public HubProducts findHubProductds(HubQuryDto hubQuryDto);
	/**
	 * 根据spuId查询该spu下所有sku详情
	 * @param spuId
	 * @return
	 */
	public HubProductDetails findProductDtails(String spuId);
	/**
	 * hub详情页的编辑，更新涉及的表有：hub_sku/hub_spu
	 * @param hubProductDetail
	 * @return
	 */
	public boolean updateHubProductDetails(HubProductDetails hubProductDetail);
}

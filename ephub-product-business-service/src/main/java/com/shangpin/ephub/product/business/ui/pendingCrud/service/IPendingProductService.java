package com.shangpin.ephub.product.business.ui.pendingCrud.service;

import java.util.List;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.product.business.ui.pendingCrud.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pendingCrud.vo.PendingProductDto;

/**
 * <p>Title:IPendingProductService </p>
 * <p>Description: 待处理页面Service接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月20日 下午3:59:51
 *
 */
public interface IPendingProductService {

	/**
	 * 根据页面查询条件查询待处理表产品
	 * @param pendingQuryDto
	 * @return
	 */
	public List<PendingProductDto> findPendingProduct(PendingQuryDto pendingQuryDto);
	/**
	 * 根据spu查找sku
	 * @param spuPendingId
	 * @return
	 */
	public List<HubSkuPendingDto> findPendingSkuBySpuPendingId(Long spuPendingId);
	/**
	 * 更新单个pending数据，一个pending数据包括一个PengdingSpu和对应的多个PendingSku
	 * @param pendingProductDto
	 */
	public void updatePendingProduct(PendingProductDto pendingProductDto);
	/**
	 * 批量更新pending数据
	 * @param pendingProductDto
	 */
	public void batchUpdatePendingProduct(List<PendingProductDto> pendingProducts);
}

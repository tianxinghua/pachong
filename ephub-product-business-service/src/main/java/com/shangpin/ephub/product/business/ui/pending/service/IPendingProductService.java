package com.shangpin.ephub.product.business.ui.pending.service;

import java.util.List;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProducts;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingUpdatedVo;
import com.shangpin.ephub.response.HubResponse;

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
	 * 待处理页面导出sku
	 * @param pendingQuryDto
	 * @return
	 */
	public HubResponse<?> exportSku(PendingQuryDto pendingQuryDto);
	/**
	 * 待处理页面导出spu
	 * @param pendingQuryDto
	 * @return
	 */
	public HubResponse<?> exportSpu(PendingQuryDto pendingQuryDto);
	/**
	 * 根据页面查询条件查询待处理表，只包含spu信息
	 * @param pendingQuryDto
	 * @return
	 */
	public List<PendingProductDto> findPengdingSpu(PendingQuryDto pendingQuryDto);

	/**
	 * 根据页面查询条件查询待处理表产品，包括spu/sku信息
	 * @param pendingQuryDto
	 * @return
	 */
	public PendingProducts findPendingProducts(PendingQuryDto pendingQuryDto);
	/**
	 * 根据spu查找sku
	 * @param spuPendingId
	 * @return
	 */
	public List<HubSkuPendingDto> findPendingSkuBySpuPendingId(Long spuPendingId) throws Exception;
	/**
	 * 更新单个pending数据，一个pending数据包括一个PengdingSpu和对应的多个PendingSku
	 * @param pendingProductDto
	 */
	public HubResponse<PendingUpdatedVo> updatePendingProduct(PendingProductDto pendingProductDto);
	/**
	 * 批量更新pending数据
	 * @param pendingProductDto
	 */
	public HubResponse<List<PendingUpdatedVo>> batchUpdatePendingProduct(PendingProducts pendingProducts);
	/**
	 * 将HubSpuPendingDto更新为无法处理
	 * @param spuPendingId
	 */
	public boolean updatePendingProductToUnableToProcess(String spuPendingId) throws Exception;
	/**
	 * 批量更新为无法处理
	 * @param spuPendingIds
	 */
	public boolean batchUpdatePendingProductToUnableToProcess(List<String> spuPendingIds);
}

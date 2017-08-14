package com.shangpin.ephub.product.business.ui.pending.vo;

import java.util.List;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PendingProductDto extends HubSpuPendingDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -656519843276622266L;

	private String updateTimeStr;
	private String creatTimeStr;
	private String supplierName;
	private String hubBrandName;
	private String hubCategoryName;
	private List<HubSkuPendingDto> hubSkus;
	/**
	 * 主图
	 */
	private String spPicUrl;
	/**
	 * 尚品图片链接
	 */
	private List<String> picUrls;
	/**
	 * 供应商图片链接
	 */
	private List<String> supplierUrls;
	/**
	 * 图片加载失败原因
	 */
	private String picReason;
	
	private String supplierCategoryname;
	private String auditDateStr;
	/**
	 * 错误原因
	 */
	private String errorReason;
	
}

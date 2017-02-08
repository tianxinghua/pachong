package com.shangpin.ephub.product.business.ui.pending.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
/**
 * <p>Title:PendingQuryDto </p>
 * <p>Description: 待处理页面请求参数实体类 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月17日 下午2:58:15
 *
 */
@Getter
@Setter
public class PendingQuryDto {

	private Integer pageIndex;
    private Integer pageSize;
    private String supplierNo;
    private String spuModel;
    private String hubCategoryNo;
    private String hubBrandNo;
    private String hubSeason;
    private String hubYear;
    private List<Integer> inconformities;//不符合的项
    private String statTime;
    private String endTime;
    private String createUser;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * HUB品类
     */
    private String categoryName;
    /**
     * 是否导出图片
     */
    private Integer isExportPic;
    /**
     * 导出第几页
     */
    private String exportPageIndex;
    /**
     * 导出每页的商品数量
     */
    private String exportPageSize;
    /**
     * 图片状态
     */
    private Integer picState;
    /**
     * spu主键
     */
    private Long spuPendingId;
}

package com.shangpin.ephub.product.business.pendingCrud.dto;

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
    private String supplierName;
    private String spuModel;
    private String hubCategory;
    private String hubBrand;
    private String hubSeason;
    private String hubYear;
    private List<Integer> inconformities;
    private String statTime;
    private String endTime;
}

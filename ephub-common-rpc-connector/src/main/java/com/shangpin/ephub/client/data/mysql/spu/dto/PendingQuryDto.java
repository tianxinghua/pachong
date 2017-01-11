package com.shangpin.ephub.client.data.mysql.spu.dto;

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
}
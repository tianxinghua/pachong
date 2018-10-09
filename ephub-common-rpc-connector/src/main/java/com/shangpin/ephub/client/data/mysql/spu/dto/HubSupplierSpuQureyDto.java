package com.shangpin.ephub.client.data.mysql.spu.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSupplierSpuQureyDto {

	private Integer pageIndex;
    private Integer pageSize;
    private String supplierId;
    private String brandName;
    private String brandNo;
    private String channel;//渠道
    
}

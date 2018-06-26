package com.shangpin.ephub.data.mysql.spu.supplier.po;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSupplierSpuQureyDto {

	private Integer pageIndex;
    private Integer pageSize;
    private String supplierId;
    private String brandName;
    private String brandNo;
    
}

package com.shangpin.ephub.client.data.mysql.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

/**
 * Created by loyalty on 16/12/23.
 * spu pending
 */

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpuPendingCommonDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -7596677954605182860L;

	private Long spuPendingId;//spuPending表主键

    private String spuModel;//货号

    private String color;

    private String brandNo;

    private String categoryNo;

    private String supplierNo;

    private String supplierId;

    private String material;//材质

    private String origin;//产地

    private String updateTime ;
}

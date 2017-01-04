package com.shangpin.ephub.client.data.mysql.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

/**
 * Created by loyalty on 16/12/23.
 */

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpuPendingPicDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5803282635248632412L;

	/**
     * spuPengding表主键
     */
    private Long spuPendingId;

    /**
     * 图片存储的主键
     */
    private Long  picId;
    /**
     * 尚品的图片链接
     */
    private String spPicUrl;

}

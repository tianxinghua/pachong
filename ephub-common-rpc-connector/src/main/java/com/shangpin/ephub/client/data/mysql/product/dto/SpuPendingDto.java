package com.shangpin.ephub.client.data.mysql.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by loyalty on 16/12/23.
 */

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpuPendingDto extends SpuPendingCommonDto implements Serializable {




    /**
     * 是否默认供货商
     */
    private Boolean isDefaultSupplier;

    /**
     * 供货商图片信息
     */

    private List<SpuPendingPicDto>  picVOs;



}

package com.shangpin.ephub.data.mysql.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by loyalty on 16/12/23.
 */
@Getter
@Setter
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

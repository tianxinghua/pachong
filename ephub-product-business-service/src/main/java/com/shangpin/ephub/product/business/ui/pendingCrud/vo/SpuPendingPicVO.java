package com.shangpin.ephub.product.business.ui.pendingCrud.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by loyalty on 16/12/23.
 */
@Getter
@Setter
public class SpuPendingPicVO implements Serializable {

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

package com.shangpin.ephub.product.business.ui.studio.pending.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lizhongren on 2017/7/27.
 */
@Getter
@Setter
public class ShootPendingVo implements Serializable {
    /**
     * 操作人
     */
    private String operator;
    /**
     *
     */
    private List<Long>  spuPendingIds;

}

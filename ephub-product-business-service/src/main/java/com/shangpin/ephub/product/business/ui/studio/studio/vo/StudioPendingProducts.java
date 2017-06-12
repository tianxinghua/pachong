package com.shangpin.ephub.product.business.ui.studio.studio.vo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 */
@Getter
@Setter
public class StudioPendingProducts implements Serializable {

    private static final long serialVersionUID = 4357243781682368686L;

    private List<StudioPendingProductVo> hubProducts;
    private int total;
}

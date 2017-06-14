package com.shangpin.ephub.product.business.ui.studio.studio.vo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Administrator on 2017/6/13.
 */

@Getter
@Setter
@ToString
public class SlotsVo implements Serializable{

    private static final long serialVersionUID = 7005555312014316538L;

    private List<SlotInfo> slotInfoList;

    private int total;
}

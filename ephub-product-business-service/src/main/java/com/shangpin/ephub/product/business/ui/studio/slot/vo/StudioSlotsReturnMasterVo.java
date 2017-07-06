package com.shangpin.ephub.product.business.ui.studio.slot.vo;


import java.io.Serializable;
import java.util.List;

import com.shangpin.ephub.product.business.ui.studio.slot.vo.detail.StudioSlotReturnMasterInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Created by Administrator on 2017/6/13.
 */

@Getter
@Setter
@ToString
public class StudioSlotsReturnMasterVo implements Serializable{

    private static final long serialVersionUID = 7005555312014316567L;

    private List<StudioSlotReturnMasterInfo> studioSlotReturnMasterDtoList;

    private int total;
}

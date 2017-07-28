package com.shangpin.ephub.product.business.ui.studio.slot.vo;


import java.io.Serializable;
import java.util.List;

import com.shangpin.ephub.product.business.ui.studio.slot.vo.detail.StudioDicSlotInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Created by Administrator on 2017/6/13.
 */

@Getter
@Setter
@ToString
public class StudioDicSlotVo implements Serializable{

    private static final long serialVersionUID = 7005555312014316537L;

    private List<StudioDicSlotInfo> studioDicSlotDtoList;

    private int total;
}

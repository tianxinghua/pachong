package com.shangpin.ephub.product.business.ui.studio.studio.vo;

import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnMasterDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */
@Setter
@Getter
public class ReturnSlotListVo implements Serializable {
    private static final long serialVersionUID = -697348993655923653L;

    private List<StudioSlotReturnMasterDto>  slotReturnList;

    private int total;
}

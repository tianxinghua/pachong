package com.shangpin.ephub.product.business.ui.studio.studio.vo;

import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailDto;
import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnMasterDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */
@Getter
@Setter
@ToString
public class ReturnSlotInfo extends StudioSlotReturnMasterDto implements Serializable {
    private static final long serialVersionUID = -1446648664796607965L;
    private List<StudioSlotReturnDetailDto> detailDtoList;
}

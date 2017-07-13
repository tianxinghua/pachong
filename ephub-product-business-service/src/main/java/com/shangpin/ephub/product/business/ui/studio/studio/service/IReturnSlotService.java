package com.shangpin.ephub.product.business.ui.studio.studio.service;

import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailDto;
import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnMasterDto;
import com.shangpin.ephub.product.business.ui.studio.studio.dto.DefectiveSpuDto;
import com.shangpin.ephub.product.business.ui.studio.studio.dto.ReturnSlotQueryDto;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.DefectiveListVo;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.ReturnSlotInfo;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.ReturnSlotListVo;
import com.shangpin.ephub.response.HubResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */
public interface IReturnSlotService {

    ReturnSlotListVo getReturnSlotList(ReturnSlotQueryDto queryDto);

    boolean ReceiveReturnSlot(String supplierId,Long id,String userName);

    ReturnSlotInfo getReceivedSlotInfo(String supplierId, Long id);

    HubResponse<StudioSlotReturnDetailDto> addProductFromScan(String supplierId, Long id, String barcode, String userName);

    HubResponse<ReturnSlotInfo>  confirmSlotInfo(String supplierId, Long id,String userName);

    Long addDefective(DefectiveSpuDto queryDto);

    List<DefectiveListVo> getDefectiveList(String supplierId ,String startTime,String endTime);

    boolean DeleteDefective(String supplierId, Long id,String userName);

}

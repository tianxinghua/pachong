package com.shangpin.ephub.product.business.ui.studio.studio.service;

import com.shangpin.ephub.client.data.studio.slot.logistic.dto.StudioSlotLogistictTrackDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.product.business.ui.studio.studio.dto.StudioSlotQueryDto;
import com.shangpin.ephub.product.business.ui.studio.studio.vo.*;
import com.shangpin.ephub.response.HubResponse;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/8.
 */
public interface IStudioService {

    StudioPendingProductVo getPendingProductList(SlotSpuSupplierQueryDto queryDto);
    SlotsVo getSupplierSlotList(String supplierId);
    HubResponse<?> releaseStudioSlot(String supplierId ,Long slotId,String createUser);

    boolean delProductFromSlot(String supplierId ,Long slotSSId,String createUser);
    SlotInfoExtends getSlotInfo( String supplierId ,String slotNo);
    HubResponse<?> addProductIntoSlot(String supplierId , String slotNo, List<Long> slotSSIds, String createUser);
    HubResponse<?> delProductFromSlot(String supplierId ,String slotNo,Long slotSSId,Long slotSSDId,String createUser);
    HubResponse<?> checkProductAndSendSlot(String supplierId ,String slotNo,String handleUser);
    boolean insertSlotLogistic(Long studioSlotId,String logisticName,String trackingNo,String memo,String createUser);
    StudioSlotLogistictTrackDto getSlotLogisticInfo(Long studioSlotId);
    boolean slotPrint(Long slotId);
    SlotsVo getStudioSlot(Long StudioId, String startTime, String endTime ,String categoryNos,Long supplierId);
    SlotsVo getStudioSlot(Long StudioId,String startTime,String endTime,String categoryNos,Long supplierId,int pageIndex,int pageSize,int history);
    List<ErrorConent> applyUpdateSlot(StudioSlotQueryDto upDto);


    List<StudioDto> getStudioList();
    List<StudioDto> getStudioListByCategory(List<String> categoryNos);


}

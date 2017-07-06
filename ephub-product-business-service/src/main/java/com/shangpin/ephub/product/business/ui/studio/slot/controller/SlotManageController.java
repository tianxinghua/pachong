package com.shangpin.ephub.product.business.ui.studio.slot.controller;

import com.shangpin.ephub.product.business.service.studio.slotsendreturn.SlotSendReturnService;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.detail.SlotSpuDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.studio.slot.slot.dto.SlotManageQuery;
import com.shangpin.ephub.product.business.ui.studio.slot.service.SlotDetailService;
import com.shangpin.ephub.product.business.ui.studio.slot.service.SlotManageService;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.detail.SlotInfo;
import com.shangpin.ephub.response.HubResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>Title: OpenBoxController</p>
 * <p>Description: 批次管理接口</p>
 * <p>Company: </p> 
 * @author zhaogenchun
 * @date 2017年6月12日 
 *
 */
@RestController
@RequestMapping("/api/airstudio/slot-manage")
public class SlotManageController {

	@Autowired
	SlotManageService slotManageService;
	@Autowired
	SlotDetailService slotDetailService;

	@Autowired
	SlotSendReturnService slotSendReturnService;
	
	
	@RequestMapping(value="/slot-list",method = RequestMethod.POST)
	public HubResponse<?> slotList(@RequestBody SlotManageQuery slotManageQuery){
		return slotManageService.findSlotManageList(slotManageQuery);
	}
	
	@RequestMapping(value="/slot-detail",method = RequestMethod.POST)
	public HubResponse<?> slotDetail(@RequestBody String slotNo){
		SlotInfo slotInfo = new SlotInfo();//slotDetailService.getSlotInfo(slotNo);
		slotInfo.setSlotNo("20170705S001001");
		slotInfo.setActualQuantity(50);
		slotInfo.setArriveQty(50);
		slotInfo.setArriveTime(new Date());
		slotInfo.setSlotStatus(1);
		if(null != slotInfo){
			return HubResponse.successResp(slotInfo);
		}else{
			return HubResponse.errorResp("查询明细异常");
		}
	}
	
	@RequestMapping(value="/slot-product-detail",method = RequestMethod.POST)
	public HubResponse<?> slotProductDetail(@RequestBody String slotNo){
        List<SlotSpuDetail> slotSpuDetailList = new ArrayList<>();
		SlotSpuDetail detail1 = new SlotSpuDetail();
		detail1.setHubBrandNo("B0005");
		detail1.setReceived(true);
		detail1.setHubCategoryNo("A03B01C02D05");
		detail1.setSpuModel("1234");
		detail1.setReturned(false);
		detail1.setSupplierSpuModel("");
		slotSpuDetailList.add(detail1);

		SlotSpuDetail detail2 = new SlotSpuDetail();
		detail2.setHubBrandNo("B0005");
		detail2.setReceived(true);
		detail2.setHubCategoryNo("A03B01C02D05");
		detail2.setSpuModel("1234");
		detail2.setReturned(false);

		detail2.setSupplierSpuModel("");
		slotSpuDetailList.add(detail2);
		return HubResponse.successResp(slotSpuDetailList);
	}
	
	@RequestMapping(value="/slot-export",method = RequestMethod.POST)
	public HubResponse<?> slotDetailCheck(@RequestBody SlotManageQuery slotManageQuery){
		return null;
	}
	
	@RequestMapping(value="/save-return",method = RequestMethod.POST)
	public HubResponse<?> saveSlotReturnDetailAndMaster(@RequestBody SlotManageQuery slotManageQuery){
		return slotManageService.createSlotReturnDetailAndMaster(slotManageQuery);
	}
	
	@RequestMapping(value="/return-master",method = RequestMethod.POST)
	public HubResponse<?> selectSlotReturnMaster(@RequestBody SlotManageQuery slotManageQuery){
		return slotManageService.selectSlotReturnMaster(slotManageQuery);
	}
	
	@RequestMapping(value="/return-slot",method = RequestMethod.POST)
	public HubResponse<?> selectSlotReturnDetail(@RequestBody SlotManageQuery slotManageQuery){
		return slotManageService.selectSlotReturnDetail(slotManageQuery);
	}
	
	@RequestMapping(value="/update-returnslot",method = RequestMethod.POST)
	public HubResponse<?> updateSlotReturnDetail(@RequestBody SlotManageQuery slotManageQuery){
		return slotManageService.updateSlotReturnDetail(slotManageQuery);
	}
	
	@RequestMapping(value="/save-logistictTrack",method = RequestMethod.POST)
	public HubResponse<?> createStudioSlotLogistictTrack(@RequestBody SlotManageQuery slotManageQuery){
		return slotManageService.createStudioSlotLogistictTrack(slotManageQuery);
	}
}

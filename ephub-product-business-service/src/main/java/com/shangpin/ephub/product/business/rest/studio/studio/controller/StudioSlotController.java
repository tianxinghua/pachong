package com.shangpin.ephub.product.business.rest.studio.studio.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicCalendarDto;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSlotDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.product.business.rest.studio.studio.dto.ResultObjList;
import com.shangpin.ephub.product.business.rest.studio.studio.dto.ResultResponseDto;
import com.shangpin.ephub.product.business.rest.studio.studio.service.StudioDicCalendarService;
import com.shangpin.ephub.product.business.rest.studio.studio.service.StudioDicSlotService;
import com.shangpin.ephub.product.business.rest.studio.studio.service.StudioService;
import com.shangpin.ephub.product.business.rest.studio.studio.service.StudioSlotService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 创建studio批次 wangchao 2017-06-16
 * 
 */
@RestController
@RequestMapping(value = "/studio-slot")
@Slf4j
public class StudioSlotController {

	@Autowired
	StudioService studioService;
	@Autowired
	StudioDicCalendarService studioDicCalendarService;
	@Autowired
	StudioSlotService studioSlotService;
	@Autowired
	StudioDicSlotService studioDicSlotService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfomat = new SimpleDateFormat("yyyy/MM/dd");

	@RequestMapping(value = "/create")
	public boolean createStudioSlot() {
		log.info("摄影棚批次创建----start");
		try {
			List<StudioDto> listStudioDto = studioService.getAllStudio();
			for (StudioDto studioDto : listStudioDto) {
				long studioId = studioDto.getStudioId();
				String studioNo = studioDto.getStudioNo();
				List<StudioDicCalendarDto> listStudioDicCalendarDto = studioDicCalendarService
						.getStudioDicCalendarByStudioId(studioId);
				if (listStudioDicCalendarDto == null || listStudioDicCalendarDto.size() == 0) {
					log.error("studioId:" + studioId + "studioNo:" + studioNo + "摄影棚日历不存在，请检查！");
					continue;
				}

				int period = studioDto.getPeriod();
				Calendar c = Calendar.getInstance();
				String from = sdf.format(c.getTime());
				String to;
				if (period - 1 < 0) {
					log.error("studioId:" + studioId + "studioNo:" + studioNo + "摄影棚批次周期设置有误，批次生成失败");
					continue;
				} else {
					c.add(Calendar.DAY_OF_MONTH, period - 1);
					to = sdf.format(c.getTime());
				}
				String calenderTemplateId = listStudioDicCalendarDto.get(0).getCalenderTemplateId();

				// 根据studioid 查询 studio_dic_slot表，确认每天生成几个批次
				List<StudioDicSlotDto> studioDicSlotList = studioDicSlotService.getStudioDicSlotByStudioId(studioId);
				if (studioDicSlotList == null || studioDicSlotList.size() == 0) {
					log.info("studioId:" + studioId + "studioNo:" + studioNo + "摄影棚批次基础表不存在数据,批次生成失败");
					continue;
				}
				// 调用api
				ResultResponseDto<ResultObjList> data = studioDicCalendarService.getStudioOffDayCalendarByApi(from, to,
						calenderTemplateId);
				if (!data.getSuccess()) {
					log.error("studioId:" + studioId + "studioNo:" + studioNo + "调用摄影日历API返回false！,批次生成失败");
					continue;
				}
				int num = data.getResultObjList().size();
				// 倒序判断是否需要生成批次
				for (int i = num - 1; i >= 0; i--) {
					// 根据当天时间查询批次管理表，当天是否有待拍摄批次
					ResultObjList resultObjList = data.getResultObjList().get(i);
					String DT = resultObjList.getDT();
					List<StudioSlotDto> studioSlotList = studioSlotService.getStudioSlotBySlotDate(sdfomat.parse(DT));
					if (studioSlotList == null || studioSlotList.size() == 0) {
						if (resultObjList.getIsOffDay() == 0) {
							int slot_efficiency = studioDicSlotList.get(0).getSlotEfficiency();
							for (int j = 1; j <= slot_efficiency; j++) {
								StudioSlotDto studioSlotDto = createStudioSlotDto(studioId, DT, studioNo, j);
								studioSlotService.insertStudioSlot(studioSlotDto);
							}
						}
					} else {
						break;
					}
				}
			}
			log.info("摄影棚批次创建-----end");
			return true;
		} catch (Exception e) {
			log.error("createStudioSlot处理发生异常：{}", e);
			e.printStackTrace();
		}
		return false;
	}
	
	@RequestMapping(value = "/check")
	public boolean checkStudioSlot() {
		try {
			// 1.当日之前，未被申请的批次需要处理为已过期。
			studioSlotService.selectAndUpdateStudioSlotBeforeSlotDate();
			// 2. 查询摄影棚当天收货状态，对超过预定的收货时间的批次进行处理
			studioSlotService.selectAndUpdateStudioSlotAfterPlanArriveDate();
			// 3. 查询当天之前的拍摄状态，如果有未设置拍摄状态的批次進行处理
			studioSlotService.selectAndUpdateStudioSlotBeforeSlotDateByShotStatus();
			// 查询当天拍摄的批次进行处理。
			studioSlotService.selectAndUpdateStudioSlotBySlotDate();
			return true;
		} catch (Exception e) {
			log.error("checkStudioSlot处理发生异常：{}", e);
			e.printStackTrace();
		}
		return false;
	}

	private StudioSlotDto createStudioSlotDto(long studioId, String DT, String studioNo, int num) {
		StudioSlotDto dto = new StudioSlotDto();
		dto.setStudioId(studioId);
		String slotNo = DT.replaceAll("/", "") + studioNo + "00" + num;
		dto.setSlotNo(slotNo);
		dto.setSlotStatus((byte) 0);
		dto.setApplyStatus((byte) 0);
		dto.setCreateTime(new Date());
		dto.setUpdateTime(new Date());
		dto.setShotStatus((byte) 0);
		try {
			dto.setSlotDate(sdfomat.parse(DT));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dto;
	}
	public static void main(String[] args){
		System.out.println("2017/06/19".replaceAll("/", ""));
		
	}
}

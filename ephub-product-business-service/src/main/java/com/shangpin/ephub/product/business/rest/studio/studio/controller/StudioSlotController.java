package com.shangpin.ephub.product.business.rest.studio.studio.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;
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
import com.shangpin.ephub.client.product.business.gms.result.HubResponseDto;
import com.shangpin.ephub.product.business.rest.studio.studio.dto.ResultObjList;
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
	SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");

	@RequestMapping(value = "/create")
	public boolean createStudioSlot() {
		log.info("摄影棚批次创建----start");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			List<StudioDto> listStudioDto = studioService.getAllStudio();
			for (StudioDto studioDto : listStudioDto) {
				long studioId = studioDto.getStudioId();
                String studioNo = studioDto.getStudioNo();
				List<StudioDicCalendarDto> listStudioDicCalendarDto = studioDicCalendarService
						.getStudioDicCalendarByStudioId(studioId);
				if(listStudioDicCalendarDto==null||listStudioDicCalendarDto.size()==0){
					log.error("studioId:"+studioId+"摄影棚日历不存在，请检查！");
					continue;
				}

				int period = studioDto.getPeriod();
				Calendar c = Calendar.getInstance();
				String from = sdf.format(c.getTime());
				String to;
				if (period - 1 < 0) {
					log.error("studioId:" + studioId + "摄影棚批次周期设置有误，请检查！");
					continue;
				} else {
					c.add(Calendar.DAY_OF_MONTH, period - 1);
					to = sdf.format(c.getTime());
				}
				String calenderTemplateId = listStudioDicCalendarDto.get(0).getCalenderTemplateId();
				log.info("nowDate:"+from+"endDate:"+to+"calenderTemplateId:"+calenderTemplateId);
				//根据studioid 查询 studio_dic_slot表，确认每天生成几个批次
				List<StudioDicSlotDto> studioDicSlotList = studioDicSlotService.getStudioDicSlotByStudioId(studioId);
				if(studioDicSlotList==null||studioDicSlotList.size()==0){
					log.info("studioId:"+studioId+"摄影棚批次基础表不存在数据！");
//					continue;
				}
				//调用api
//				HubResponseDto<ResultObjList> data =  studioDicCalendarService.getStudioOffDayCalendarByApi(from, to, calenderTemplateId);
				
//				int num = data.getResDatas().size();
				int num = 1;
				//倒序判断是否需要生成批次
				for(int i = num-1; i>=0;i--){
					//根据当天时间查询批次管理表，当天是否有待拍摄批次
//					ResultObjList resultObjList = data.getResDatas().get(i);
//					String DT = resultObjList.getDT();
					String DT = "2017/06/18";
					List<StudioSlotDto> studioSlotList = studioSlotService.getStudioSlotByCreateDate(DT);
					if(studioSlotList==null||studioSlotList.size()==0){
//						if(resultObjList.getIsOffDay()==0){
						if(true){
//							int slot_efficiency = studioDicSlotList.get(0).getSlotEfficiency();
							int slot_efficiency = 2;
							for(int j=1;j<=slot_efficiency;j++){
								StudioSlotDto studioSlotDto = createStudioSlotDto(studioId,DT,studioNo,j);
								studioSlotService.insertStudioSlot(studioSlotDto);
							}
						}
					}else{
						break;
					}
				}
			}
			// 当日之前，未被申请的批次需要处理为已过期。
			//过期时间字段暂定
			studioSlotService.selectAndUpdateStudioSlotBeforePlanDate(sdf.parse(sdf.format(new Date())));
			
			// 查询本摄影棚昨天收到货，但已过了预定的拍摄时间的批次。查询当天的批次是否被申请，如果未被申请,占用当天的拍摄批次。如果已被申请，则使用第二天的数据，以此推算
            
			
			return true;
		} catch (Exception e) {
			log.error("studioSlotService.studioSlotService处理发生异常：{}", e);
			e.printStackTrace();
		}
		log.info("摄影棚批次创建-----end");
		return false;
	}
	private StudioSlotDto createStudioSlotDto(long studioId,String DT,String studioNo,int num){
		StudioSlotDto dto = new StudioSlotDto();
		dto.setStudioId(studioId);
		String slotNo = sd.format(new Date())+studioNo+"00"+num;
		dto.setSlotNo(slotNo);
		dto.setSlotStatus((byte) 0);
		dto.setApplyStatus((byte) 0);
		dto.setCreateTime(new Date());
		dto.setUpdateTime(new Date());
		dto.setShotStatus((byte) 0);
		return dto;
	}
}

package com.shangpin.ephub.product.business.rest.studio.studio.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotApplyState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotArriveState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotShootState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotState;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.slot.slot.gateway.StudioSlotGateWay;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by wangchao on 2017/06/19.
 */
@Service
@Slf4j
public class StudioSlotService {
	@Autowired
	private StudioSlotGateWay studioSlotGateWay;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	SimpleDateFormat sdfomat = new SimpleDateFormat("yyyy/MM/dd");
	SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");

	public List<StudioSlotDto> getStudioSlotBySlotDate(Date DT,long studioId) {
		List<StudioSlotDto> listStudioDto = null;
		log.info("查询当天是否生成了批次信息----start");
		StudioSlotCriteriaDto dto = new StudioSlotCriteriaDto();
		dto.createCriteria().andSlotDateEqualTo(DT).andStudioIdEqualTo(studioId);
		dto.setPageSize(50);
		listStudioDto = studioSlotGateWay.selectByCriteria(dto);
		log.info("查询当天是否生成了批次信息----end");
		return listStudioDto;
	}

	public void insertStudioSlot(StudioSlotDto dto) {
		try {
			studioSlotGateWay.insert(dto);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("slotNo:" + dto.getSlotNo() + "创建摄影棚批次出错!");
		}

	}

	// 当日之前，未被申请的批次需要处理为已过期。
	public void selectAndUpdateStudioSlotBeforeSlotDate() {
		List<StudioSlotDto> listStudioDto = null;
		log.info("查询并更新当天计划拍摄日期之前未被申请的批次信息----start");
		try {
			Calendar calendar = Calendar.getInstance();
		    calendar.setTime(new Date());
		    calendar.add(Calendar.DAY_OF_MONTH, +2);//+2今天的时间加2天
			String nowDate = sdfomat.format(calendar.getTime()) + " 00:00:00";
			Date nowDateTime = sdf.parse(nowDate);
			StudioSlotCriteriaDto dto = new StudioSlotCriteriaDto();
			dto.createCriteria().andSlotDateLessThan(nowDateTime).andApplyStatusEqualTo((byte) 0);
			dto.setPageSize(500);
			listStudioDto = studioSlotGateWay.selectByCriteria(dto);
			for (StudioSlotDto studioSlotDto : listStudioDto) {
				studioSlotDto.setApplyStatus((byte) 2);// 2 已过期
				studioSlotGateWay.updateByPrimaryKey(studioSlotDto);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		log.info("查询并更新当天计划拍摄日期之前未被申请的批次信息----end");
	}

	// 查询摄影棚当天收货状态，对超过预定的收货时间的批次进行处理。
	public void selectAndUpdateStudioSlotAfterPlanArriveDate() {
		List<StudioSlotDto> listStudioDto = null;
		try {
			log.info("查询摄影棚当天收货状态并处理----start");
			String nowDate = sdfomat.format(new Date());
			String startDate = nowDate + " 00:00:00";
			String endDate = nowDate + " 23:59:59";
			Date sDate = sdf.parse(startDate);
			Date eDate = sdf.parse(endDate);
			StudioSlotCriteriaDto dto = new StudioSlotCriteriaDto();
			List<Byte> list = new ArrayList<>();
			list.add(StudioSlotState.HAVE_SHOOT.getIndex().byteValue());
			list.add(StudioSlotState.STUDIO_RETURN.getIndex().byteValue());
			list.add(StudioSlotState.HAVE_FINISHED.getIndex().byteValue());
			dto.createCriteria().andArriveTimeBetween(sDate, eDate).andApplyStatusEqualTo(StudioSlotApplyState.APPLYED.getIndex().byteValue()).andSlotStatusNotIn(list);
			dto.setPageSize(500);
			listStudioDto = studioSlotGateWay.selectByCriteria(dto);
			for (StudioSlotDto studioSlotDto : listStudioDto) {
				
				Calendar calendar = Calendar.getInstance();
			    calendar.setTime(studioSlotDto.getPlanShootTime());
			    calendar.add(Calendar.DAY_OF_MONTH, -1);//+2今天的时间加2天
				String planArriveDate = sdfomat.format(calendar.getTime()) + " 23:59:59";
				Date planArriveDateTime = sdf.parse(planArriveDate);
				if (studioSlotDto.getArriveTime().after(planArriveDateTime)) {
					boolean isflg = true;
					int i = 0;
					while (isflg) {
						Calendar c = Calendar.getInstance();
						c.add(Calendar.DAY_OF_MONTH, i);
						List<StudioSlotDto> slotDtoList = getStudioSlotBySlotDate(c.getTime(),studioSlotDto.getStudioId());
						for (StudioSlotDto slotDto : slotDtoList) {
							if (slotDto.getApplyStatus() == (byte) 0) {
								slotDto.setApplyStatus(StudioSlotApplyState.INTERNAL_OCCUPANCY.getIndex().byteValue());
								slotDto.setSlotStatus(StudioSlotState.RECEIVED.getIndex().byteValue());
								slotDto.setOriginSlotNo(studioSlotDto.getSlotNo());
								slotDto.setApplyUser(studioSlotDto.getSlotNo());
								slotDto.setApplyTime(new Date());
								studioSlotGateWay.updateByPrimaryKey(slotDto);
								isflg = false;

								// 迟到批次修改计划拍摄时间
								studioSlotDto.setPlanShootTime(slotDto.getPlanShootTime());
								studioSlotGateWay.updateByPrimaryKey(studioSlotDto);
								break;
							}
						}
						i++;
					}
				}

			}
			log.info("查询摄影棚当天收货状态并处理----end");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 查询当天之前的拍摄状态，如果有未设置拍摄状态的批次进行处理。
	public void selectAndUpdateStudioSlotBeforeSlotDateByShotStatus() {
		List<StudioSlotDto> listStudioDto = null;
		log.info("查询摄影棚当天之前的拍摄状态并处理----start");
		try {
			String nowDate = sdfomat.format(new Date()) + " 00:00:00";
			Date nowDateTime = sdf.parse(nowDate);
			StudioSlotCriteriaDto dto = new StudioSlotCriteriaDto();
			dto.createCriteria().andSlotDateLessThan(nowDateTime)
					.andShotStatusEqualTo(StudioSlotShootState.WAIT_SHOOT.getIndex().byteValue())
					.andArriveStatusEqualTo(StudioSlotArriveState.RECEIVED.getIndex().byteValue())
					.andApplyStatusNotEqualTo(StudioSlotApplyState.INTERNAL_OCCUPANCY.getIndex().byteValue())
					.andPlanShootTimeLessThan(new Date());
			dto.setPageSize(100);
			listStudioDto = studioSlotGateWay.selectByCriteria(dto);
			for (StudioSlotDto studioSlotDto : listStudioDto) {
				boolean isflg = true;
				int i = 0;
				while (isflg) {
					Calendar c = Calendar.getInstance();
					c.add(Calendar.DAY_OF_MONTH, i);
					List<StudioSlotDto> slotDtoList = getStudioSlotBySlotDate(c.getTime(), studioSlotDto.getStudioId());
					for (StudioSlotDto slotDto : slotDtoList) {
						if (slotDto.getApplyStatus() == (byte) 0) {
							slotDto.setApplyStatus(StudioSlotApplyState.INTERNAL_OCCUPANCY.getIndex().byteValue());
							slotDto.setSlotStatus(StudioSlotState.RECEIVED.getIndex().byteValue());
							slotDto.setOriginSlotNo(studioSlotDto.getSlotNo());
							slotDto.setApplyUser(studioSlotDto.getSlotNo());
							slotDto.setApplyTime(new Date());
							studioSlotGateWay.updateByPrimaryKey(slotDto);
							isflg = false;

							// 迟到批次修改计划拍摄时间
							studioSlotDto.setPlanShootTime(slotDto.getPlanShootTime());
							studioSlotGateWay.updateByPrimaryKey(studioSlotDto);
							break;
						}
					}
					i++;
				}
			}
		} catch (Exception e) {
		}
		log.info("查询摄影棚当天之前的拍摄状态并处理----end");
	}

	// 查询当天拍摄的批次进行处理。
	public void selectAndUpdateStudioSlotByShootTime() {
		log.info("查询当天拍摄的批次进行处理----start");
		try {
			StudioSlotCriteriaDto dto = new StudioSlotCriteriaDto();
			String nowDate = sdfomat.format(new Date());
			String startDate = nowDate + " 00:00:00";
			String endDate = nowDate + " 23:59:59";
			Date sDate = sdf.parse(startDate);
			Date eDate = sdf.parse(endDate);
			
			dto.createCriteria().andShootTimeBetween(sDate, eDate).andApplyStatusEqualTo(StudioSlotApplyState.APPLYED.getIndex().byteValue());
			dto.setPageSize(100);
			List<StudioSlotDto> listStudioDto = studioSlotGateWay.selectByCriteria(dto);
			for (StudioSlotDto studioDto : listStudioDto) {
				if (studioDto.getPlanShootTime().after(studioDto.getShootTime())) {
					StudioSlotDto slotDto = new StudioSlotDto();
					slotDto.setStudioId(studioDto.getStudioId());
					slotDto.setSlotNo("L" + studioDto.getSlotNo());
					slotDto.setCreateTime(new Date());
					slotDto.setUpdateTime(new Date());
					slotDto.setShotStatus((byte) 0);
					slotDto.setSlotDate(studioDto.getPlanShootTime());
					slotDto.setSendState((byte) 0);
					slotDto.setArriveStatus((byte) 0);
					
					Calendar calendar = Calendar.getInstance();
				    calendar.setTime(studioDto.getPlanShootTime());
				    calendar.add(Calendar.DAY_OF_MONTH, -1);//+2今天的时间加2天
					String planArriveDate = sdfomat.format(calendar.getTime()) + " 23:59:59";
					Date planArriveDateTime = sdf.parse(planArriveDate);
					slotDto.setPlanArriveTime(planArriveDateTime);
					
					slotDto.setPlanShootTime(studioDto.getPlanShootTime());
					
					StudioSlotCriteriaDto studioSlotCriteriaDto = new StudioSlotCriteriaDto();
					studioSlotCriteriaDto.createCriteria().andSlotDateGreaterThan(studioDto.getPlanShootTime()).andApplyStatusEqualTo(StudioSlotApplyState.INTERNAL_OCCUPANCY.getIndex().byteValue());
					studioSlotCriteriaDto.setOrderByClause("slot_date");
					List<StudioSlotDto> listStudiodto = studioSlotGateWay.selectByCriteria(studioSlotCriteriaDto);
					if(listStudiodto!=null&&listStudiodto.size()!=0){
						String slotNo = listStudiodto.get(0).getOriginSlotNo();
						listStudiodto.get(0).setApplyStatus(StudioSlotApplyState.WAIT_APPLY.getIndex().byteValue());
						listStudiodto.get(0).setSlotStatus(StudioSlotState.WAIT_APPLY.getIndex().byteValue());
						listStudiodto.get(0).setOriginSlotNo("");
						studioSlotGateWay.updateByPrimaryKey(listStudiodto.get(0));
						
						slotDto.setOriginSlotNo(slotNo);
						slotDto.setApplyStatus(StudioSlotApplyState.INTERNAL_OCCUPANCY.getIndex().byteValue());
						slotDto.setSlotStatus(StudioSlotState.RECEIVED.getIndex().byteValue());
						studioSlotGateWay.insert(slotDto);
						
						StudioSlotCriteriaDto slotdto = new StudioSlotCriteriaDto();
						slotdto.createCriteria().andSlotNoEqualTo(slotNo);

						List<StudioSlotDto> listSlot = studioSlotGateWay.selectByCriteria(slotdto);
						listSlot.get(0).setPlanShootTime(studioDto.getPlanShootTime());
						studioSlotGateWay.updateByPrimaryKey(listSlot.get(0));
					}else{
						slotDto.setApplyStatus(StudioSlotApplyState.WAIT_APPLY.getIndex().byteValue());
						slotDto.setSlotStatus(StudioSlotState.WAIT_APPLY.getIndex().byteValue());
						studioSlotGateWay.insert(slotDto);
					}
					studioDto.setApplyStatus(StudioSlotApplyState.HAS_APPLYED_AND_CREATE_STUDIO.getIndex().byteValue());
					studioSlotGateWay.updateByPrimaryKey(studioDto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("查询当天拍摄的批次进行处理----end");
	}
	
	// 查询当天拍摄的批次
		public List<StudioSlotDto> selectStudioSlotByShootTime() {
			log.info("查询当天拍摄的批次进行处理----start");
			List<StudioSlotDto> listStudioDto = null;
			try {
				StudioSlotCriteriaDto dto = new StudioSlotCriteriaDto();
				Calendar calendar = Calendar.getInstance();  
		        calendar.setTime(new Date());  
		        calendar.add(Calendar.DAY_OF_MONTH, -1);  
				String nowDate = sdfomat.format(calendar.getTime());
				String startDate = nowDate + " 00:00:00";
				String endDate = nowDate + " 23:59:59";
				Date sDate = sdf.parse(startDate);
				Date eDate = sdf.parse(endDate);
				dto.createCriteria().andShootTimeBetween(sDate, eDate);
				dto.setPageSize(100);
				listStudioDto = studioSlotGateWay.selectByCriteria(dto);
			} catch (Exception e) {
				e.printStackTrace();
			}
			log.info("查询当天拍摄的批次进行处理----end");
			return listStudioDto;
			
		}

}
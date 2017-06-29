package com.shangpin.ephub.product.business.ui.studio.slot.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esotericsoftware.minlog.Log;
import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.SlotManageQuery;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.slot.slot.gateway.StudioSlotGateWay;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioCriteriaDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.client.data.studio.studio.gateway.StudioGateWay;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.StudioSlotsVo;
import com.shangpin.ephub.response.HubResponse;

/**
 * <p>
 * Title: OpenBoxController
 * </p>
 * <p>
 * Description: 批次管理接口
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author zhaogenchun
 * @date 2017年6月12日
 *
 */
@Service
public class SlotManageService {

	@Autowired
	StudioSlotGateWay studioSlotGateWay;
	@Autowired
	StudioGateWay studioGateWay;
	@Autowired
	private IShangpinRedis shangpinRedis;
	SimpleDateFormat sdfomat = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public HubResponse<?> findSlotManageList(SlotManageQuery slotManageQuery) {
		StudioSlotsVo vo = new StudioSlotsVo();
		try {
			StudioSlotCriteriaDto studioSlotCriteriaDto = new StudioSlotCriteriaDto();
			Criteria criteria = studioSlotCriteriaDto.createCriteria();
			StudioCriteriaDto studioCriteriaDto = new StudioCriteriaDto();
			if (!shangpinRedis.exists("shangpinstudioslot")) {
				List<StudioDto> studioDtoList = studioGateWay.selectByCriteria(studioCriteriaDto);
				for (StudioDto studiotDto : studioDtoList) {
					shangpinRedis.set("studioName" + studiotDto.getStudioId(), studiotDto.getStudioName());
					shangpinRedis.set("period" + studiotDto.getStudioId(), studiotDto.getPeriod().toString());
					shangpinRedis.set("studio_no" + studiotDto.getStudioId(), studiotDto.getStudioNo());
				}
				// 摄影棚基础数据初始化到redis 用于判断
				shangpinRedis.set("shangpinstudioslot", "studioSlot");
			}

			Long studioId = null;
			if (slotManageQuery.getStudioNo() != null && !slotManageQuery.getStudioNo().equals("")) {
				studioCriteriaDto.createCriteria().andStudioNameEqualTo(slotManageQuery.getStudioNo());
				List<StudioDto> studioDtoList = studioGateWay.selectByCriteria(studioCriteriaDto);
				if (studioDtoList.size() == 0 || studioDtoList == null) {
					Log.error(slotManageQuery.getStudioNo() + ":此摄影棚不存在!");
					return null;
				}
				studioId = studioDtoList.get(0).getStudioId();
				criteria.andStudioIdEqualTo(studioId);
			}
			if (slotManageQuery.getDate() != null && !slotManageQuery.getDate().equals("")) {
				criteria.andSlotDateEqualTo(sdfomat.parse(slotManageQuery.getDate()));
			}
			if (slotManageQuery.getSlotNo() != null && !slotManageQuery.getSlotNo().equals("")) {
				criteria.andSlotNoEqualTo(slotManageQuery.getSlotNo());
			}
			if (slotManageQuery.getSlotStatus() != null) {
				criteria.andSlotStatusEqualTo(slotManageQuery.getSlotStatus());
			}
			if (slotManageQuery.getApplyStatus() != null) {
				criteria.andApplyStatusEqualTo(slotManageQuery.getApplyStatus());
			}
			if (slotManageQuery.getApplySupplierId() != null && !slotManageQuery.getApplySupplierId().equals("")) {
				criteria.andApplySupplierIdEqualTo(slotManageQuery.getApplySupplierId());
			}
			if (slotManageQuery.getApplyUser() != null && !slotManageQuery.getApplyUser().equals("")) {
				criteria.andApplyUserEqualTo(slotManageQuery.getApplyUser());
			}
			if (slotManageQuery.getApplyTime() != null && !slotManageQuery.getApplyTime().equals("")) {
				String applyTimeStart = slotManageQuery.getApplyTime() + " 00:00:00";
				String applyTimeEnd = slotManageQuery.getApplyTime() + " 23:59:59";
				criteria.andApplyTimeBetween(sdf.parse(applyTimeStart), sdf.parse(applyTimeEnd));
			}
			if (slotManageQuery.getArriveTime() != null && !slotManageQuery.getArriveTime().equals("")) {
				String arriveTimeStart = slotManageQuery.getArriveTime() + " 00:00:00";
				String arriveTimeEnd = slotManageQuery.getArriveTime() + " 23:59:59";
				criteria.andArriveTimeBetween(sdf.parse(arriveTimeStart), sdf.parse(arriveTimeEnd));
			}
			if (slotManageQuery.getArriveStatus() != null) {
				criteria.andArriveStatusEqualTo(slotManageQuery.getArriveStatus());
			}
			if (slotManageQuery.getShotStatus() != null) {
				criteria.andShotStatusEqualTo(slotManageQuery.getShotStatus());
			}
			if (slotManageQuery.getPlanShootTime() != null && !slotManageQuery.getPlanShootTime().equals("")) {
				String planShootTimeStart = slotManageQuery.getPlanShootTime() + " 00:00:00";
				String planShootTimeEnd = slotManageQuery.getPlanShootTime() + " 23:59:59";
				criteria.andPlanShootTimeBetween(sdf.parse(planShootTimeStart), sdf.parse(planShootTimeEnd));
			}
			if (slotManageQuery.getShootTime() != null && !slotManageQuery.getShootTime().equals("")) {
				criteria.andShootTimeEqualTo(sdfomat.parse(slotManageQuery.getShootTime()));
				String shootTimeStart = slotManageQuery.getShootTime() + " 00:00:00";
				String shootTimeEnd = slotManageQuery.getShootTime() + " 23:59:59";
				criteria.andShootTimeBetween(sdf.parse(shootTimeStart), sdf.parse(shootTimeEnd));
			}
			int count = studioSlotGateWay.countByCriteria(studioSlotCriteriaDto);

			if (slotManageQuery.getPageSize()!=null) {
				studioSlotCriteriaDto.setPageSize(slotManageQuery.getPageSize());
			}
			if(slotManageQuery.getPageNo()!=null){
				studioSlotCriteriaDto.setPageNo(slotManageQuery.getPageNo());
			}
			studioSlotCriteriaDto.setOrderByClause("slot_date");

			List<StudioSlotDto> studioSlotDtoList = studioSlotGateWay.selectByCriteria(studioSlotCriteriaDto);
			// 如果查询批次摄影棚名称参数为null，循环查询
			for (StudioSlotDto studioSlotDto : studioSlotDtoList) {
				// memo字段临时存取摄影棚名称字段 不更新数据库
				studioSlotDto.setStudioName(shangpinRedis.get("studioName" + studioSlotDto.getStudioId()));
			}
			vo.setStudioSlotList(studioSlotDtoList);
			vo.setTotal(count);

		} catch (Exception e) {
			Log.error("查询批次失败!");
			e.printStackTrace();
		}
		return HubResponse.successResp(vo);
	}
}

package com.shangpin.ephub.product.business.ui.studio.slot.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esotericsoftware.minlog.Log;
import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailDto;
import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnMasterCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnMasterDto;
import com.shangpin.ephub.client.data.studio.slot.returning.gateway.StudioSlotReturnDetailGateWay;
import com.shangpin.ephub.client.data.studio.slot.returning.gateway.StudioSlotReturnMasterGateWay;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.SlotManageQuery;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto.Criteria;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.slot.slot.gateway.StudioSlotGateWay;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailDto;
import com.shangpin.ephub.client.data.studio.slot.spu.gateway.StudioSlotSpuSendDetailGateWay;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioCriteriaDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.client.data.studio.studio.gateway.StudioGateWay;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.StudioSlotsReturnDetailVo;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.StudioSlotsReturnMasterVo;
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
	@Autowired
	StudioSlotSpuSendDetailGateWay studioSlotSpuSendDetailGateWay;
	@Autowired
	StudioSlotReturnMasterGateWay studioSlotReturnMasterGateWay;
	@Autowired
	StudioSlotReturnDetailGateWay StudioSlotReturnDetailGateWay;
	SimpleDateFormat sdfomat = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");

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
					return HubResponse.errorResp("1", "摄影棚不存在!");
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

			if (slotManageQuery.getPageSize() != null) {
				studioSlotCriteriaDto.setPageSize(slotManageQuery.getPageSize());
			}
			if (slotManageQuery.getPageNo() != null) {
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
			return HubResponse.errorResp("查询批次失败!");
		}
		return HubResponse.successResp(vo);
	}

	// 生成返货信息主表和返货批次明细
	public HubResponse<?> createSlotReturnDetailAndMaster(SlotManageQuery slotManageQuery) {
		try {
			if (slotManageQuery.getSlotNo() == null) {
				return HubResponse.errorResp("slotNo不能为null!");
			}
			StudioSlotSpuSendDetailCriteriaDto dto = new StudioSlotSpuSendDetailCriteriaDto();
			com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailCriteriaDto.Criteria crieria = dto
					.createCriteria();
			crieria.andSlotNoEqualTo(slotManageQuery.getSlotNo());
			List<StudioSlotSpuSendDetailDto> studioSlotSpuSendDetailDtoList = studioSlotSpuSendDetailGateWay
					.selectByCriteria(dto);
			StudioSlotCriteriaDto studioSlotCriteriaDto = new StudioSlotCriteriaDto();
			Criteria criteria = studioSlotCriteriaDto.createCriteria();
			criteria.andSlotNoEqualTo(slotManageQuery.getSlotNo());
			List<StudioSlotDto> studioSlotDtoList = studioSlotGateWay.selectByCriteria(studioSlotCriteriaDto);
			long studioId = -1;
			if (studioSlotDtoList != null && studioSlotDtoList.size() > 0) {
				studioId = studioSlotDtoList.get(0).getStudioId();
			}
			long masterId = 0;
			int i = 0;
			String studioSendNo = sd.format(new Date());
			for (StudioSlotSpuSendDetailDto studioSlotSpuSendDetailDto : studioSlotSpuSendDetailDtoList) {
				if (i == 0) {
					StudioSlotReturnMasterDto studioSlotReturnMasterDto = new StudioSlotReturnMasterDto();
					studioSlotReturnMasterDto.setStudioSendNo(studioSendNo);
					studioSlotReturnMasterDto.setSupplierId(studioSlotSpuSendDetailDto.getSupplierId());
					studioSlotReturnMasterDto.setSupplierNo(studioSlotSpuSendDetailDto.getSupplierNo());
					studioSlotReturnMasterDto.setQuantity(50);
					studioSlotReturnMasterDto.setStudioId(studioId);
					studioSlotReturnMasterDto.setActualSendQuantity(studioSlotSpuSendDetailDtoList.size());
					studioSlotReturnMasterDto.setQuantity(studioSlotSpuSendDetailDtoList.size());
					studioSlotReturnMasterDto.setDamagedQuantity(0);
					studioSlotReturnMasterDto.setMissingQuantity(0);
					studioSlotReturnMasterDto.setState((byte) 0);
					studioSlotReturnMasterDto.setCreateTime(new Date());
					studioSlotReturnMasterDto.setCreateUser("admin");
					studioSlotReturnMasterDto.setUpdateTime(new Date());
					studioSlotReturnMasterDto.setUpdateUser("admin");
					studioSlotReturnMasterGateWay.insertSelective(studioSlotReturnMasterDto);

					StudioSlotReturnMasterCriteriaDto studioSlotReturnMasterCriteriaDto = new StudioSlotReturnMasterCriteriaDto();
					studioSlotReturnMasterCriteriaDto.createCriteria().andStudioSendNoEqualTo(studioSendNo);
					List<StudioSlotReturnMasterDto> studioSlotReturnMasterDtoLists = studioSlotReturnMasterGateWay
							.selectByCriteria(studioSlotReturnMasterCriteriaDto);
					if (studioSlotReturnMasterDtoLists != null && studioSlotReturnMasterDtoLists.size() > 0) {
						masterId = studioSlotReturnMasterDtoLists.get(0).getStudioSlotReturnMasterId();
					}
				}
				StudioSlotReturnDetailDto studioSlotReturnDetailDto = new StudioSlotReturnDetailDto();
				studioSlotReturnDetailDto.setStudioSlotReturnMasterId(masterId);
				studioSlotReturnDetailDto.setSlotNo(studioSlotSpuSendDetailDto.getSlotNo());
				studioSlotReturnDetailDto.setSupplierNo(studioSlotSpuSendDetailDto.getSupplierId());
				studioSlotReturnDetailDto.setSupplierId(studioSlotSpuSendDetailDto.getSupplierId());
				studioSlotReturnDetailDto.setSpuPendingId(studioSlotSpuSendDetailDto.getSupplierSpuId());
				studioSlotReturnDetailDto.setSupplierSpuId(studioSlotSpuSendDetailDto.getSupplierSpuId());
				studioSlotReturnDetailDto.setSlotSpuNo(studioSlotSpuSendDetailDto.getSlotSpuNo());
				studioSlotReturnDetailDto.setSupplierSpuName(studioSlotSpuSendDetailDto.getSupplierSpuName());
				studioSlotReturnDetailDto.setSupplierSpuModel(studioSlotSpuSendDetailDto.getSupplierSpuModel());
				studioSlotReturnDetailDto.setSupplierBrandName(studioSlotSpuSendDetailDto.getSupplierBrandName());
				studioSlotReturnDetailDto.setSupplierCategoryName(studioSlotSpuSendDetailDto.getSupplierCategoryName());
				studioSlotReturnDetailDto.setSupplierSeasonName(studioSlotSpuSendDetailDto.getSupplierSeasonName());
				studioSlotReturnDetailDto.setState((byte) 0);
				studioSlotReturnDetailDto.setSendState((byte) 0);
				studioSlotReturnDetailDto.setArriveState((byte) 0);
				studioSlotReturnDetailDto.setBarcode(studioSlotSpuSendDetailDto.getBarcode());
				studioSlotReturnDetailDto.setCreateTime(new Date());
				studioSlotReturnDetailDto.setCreateUser("admin");
				studioSlotReturnDetailDto.setSendUser("admin");
				studioSlotReturnDetailDto.setUpdateTime(new Date());
				studioSlotReturnDetailDto.setUpdateUser("admin");
				StudioSlotReturnDetailGateWay.insertSelective(studioSlotReturnDetailDto);
				i++;
			}

		} catch (Exception e) {
			Log.error("生成studio返回明细失败!");
			e.printStackTrace();
			return HubResponse.errorResp("生成studio返回明细失败!");
		}
		return HubResponse.successResp(null);
	}

	// 查询返货信息主表
	public HubResponse<?> selectSlotReturnMaster(SlotManageQuery slotManageQuery) {
		StudioSlotsReturnMasterVo vo = new StudioSlotsReturnMasterVo();
		try {
			StudioSlotReturnDetailCriteriaDto detailDto = new StudioSlotReturnDetailCriteriaDto();
			com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailCriteriaDto.Criteria detailCriteria = detailDto
					.createCriteria();
			if (slotManageQuery.getSlotNo() != null) {
				detailCriteria.andSlotNoEqualTo(slotManageQuery.getSlotNo());
			}
			// 供应商名称
			if (slotManageQuery.getStudioNo() != null) {
			}
			int count = StudioSlotReturnDetailGateWay.countByCriteria(detailDto);
			if (slotManageQuery.getPageSize() != null) {
				detailDto.setPageSize(slotManageQuery.getPageSize());
			}
			if (slotManageQuery.getPageNo() != null) {
				detailDto.setPageNo(slotManageQuery.getPageNo());
			}
			detailDto.setDistinct(true);
			detailDto.setFields(" studio_slot_return_master_id ");
			List<StudioSlotReturnDetailDto> studioSlotReturnDetailDtoLists = StudioSlotReturnDetailGateWay
					.selectByCriteria(detailDto);
			List<StudioSlotReturnMasterDto> studioSlotReturnMasterDtoLists = new ArrayList<>();
			for (StudioSlotReturnDetailDto studioSlotReturnDetailDto : studioSlotReturnDetailDtoLists) {
				StudioSlotReturnMasterCriteriaDto dto = new StudioSlotReturnMasterCriteriaDto();
				com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnMasterCriteriaDto.Criteria criteria = dto
						.createCriteria();
				criteria.andStudioSlotReturnMasterIdEqualTo(studioSlotReturnDetailDto.getStudioSlotReturnMasterId());
				List<StudioSlotReturnMasterDto> studioSlotReturnMasterDtoList = studioSlotReturnMasterGateWay
						.selectByCriteria(dto);
				if (studioSlotReturnMasterDtoList != null && studioSlotReturnMasterDtoList.size() > 0) {
					studioSlotReturnMasterDtoLists.add(studioSlotReturnMasterDtoList.get(0));
				}
			}
			vo.setStudioSlotReturnMasterDtoList(studioSlotReturnMasterDtoLists);
			vo.setTotal(count);
		} catch (Exception e) {
			Log.error("生成studio返回明细失败!");
			e.printStackTrace();
			return HubResponse.errorResp("生成studio返回明细失败!");
		}
		return HubResponse.successResp(vo);
	}

	// 查询批次号下所有商品明细
	public HubResponse<?> selectSlotReturnDetail(SlotManageQuery slotManageQuery) {
		StudioSlotsReturnDetailVo vo = new StudioSlotsReturnDetailVo();
		try {
			StudioSlotReturnDetailCriteriaDto detailDto = new StudioSlotReturnDetailCriteriaDto();
			com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailCriteriaDto.Criteria detailCriteria = detailDto
					.createCriteria();
			if (slotManageQuery.getSlotNo() != null) {
				detailCriteria.andSlotNoEqualTo(slotManageQuery.getSlotNo());
			}
			List<StudioSlotReturnDetailDto> studioSlotReturnDetailDtoLists = StudioSlotReturnDetailGateWay
					.selectByCriteria(detailDto);
			vo.setStudioSlotReturnDetailDtoList(studioSlotReturnDetailDtoLists);
		} catch (Exception e) {
			Log.error("查询批次号下所有商品明细失败!");
			e.printStackTrace();
			return HubResponse.errorResp("查询批次号下所有商品明细失败!");
		}
		return HubResponse.successResp(vo);
	}

	// 更新商品明细
	public HubResponse<?> updateSlotReturnDetail(SlotManageQuery slotManageQuery) {
		try {
			StudioSlotReturnDetailCriteriaDto detailDto = new StudioSlotReturnDetailCriteriaDto();
			com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailCriteriaDto.Criteria detailCriteria = detailDto
					.createCriteria();
			if (slotManageQuery.getSlotNo() != null) {
				detailCriteria.andSlotNoEqualTo(slotManageQuery.getSlotNo());
			}
			List<StudioSlotReturnDetailDto> studioSlotReturnDetailDtoLists = StudioSlotReturnDetailGateWay
					.selectByCriteria(detailDto);
		} catch (Exception e) {
			Log.error("查询批次号下所有商品明细失败!");
			e.printStackTrace();
			return HubResponse.errorResp("查询批次号下所有商品明细失败!");
		}
		return HubResponse.successResp("更新成功！");
	}

}

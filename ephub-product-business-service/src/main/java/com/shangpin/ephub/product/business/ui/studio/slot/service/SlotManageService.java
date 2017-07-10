package com.shangpin.ephub.product.business.ui.studio.slot.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.esotericsoftware.minlog.Log;
import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.studio.slot.logistic.dto.StudioSlotLogistictTrackDto;
import com.shangpin.ephub.client.data.studio.slot.logistic.gateway.StudioSlotLogistictTrackGateWay;
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
import com.shangpin.ephub.product.business.conf.rpc.ApiAddressProperties;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.StudioSlotsReturnDetailVo;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.StudioSlotsReturnMasterVo;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.StudioSlotsVo;
import com.shangpin.ephub.response.HubResponse;
import com.shangpin.ephub.client.util.JsonUtil;
import com.alibaba.fastjson.JSONObject;

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
	@Autowired
	StudioSlotLogistictTrackGateWay studioSlotLogistictTrackGateWay;
	@Autowired
	private ApiAddressProperties apiAddressProperties;
	@Autowired
	private RestTemplate restTemplate;
	SimpleDateFormat sdfomat = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

	public HubResponse<?> findSlotManageList(SlotManageQuery slotManageQuery) {
		Log.info("start findSlotManageList -------批次查询接口");
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
		Log.info("end findSlotManageList -------批次查询接口");
		return HubResponse.successResp(vo);
	}

	// 生成返货信息主表和返货批次明细
	public HubResponse<?> createSlotReturnDetailAndMaster(SlotManageQuery slotManageQuery) {
		try {
			Log.info("start createSlotReturnDetailAndMaster---生成返货信息主表和返货批次明细");
			Log.info("params: slotNo:"+slotManageQuery.getSlotNo());
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
			if (studioSlotDtoList == null || studioSlotDtoList.size() == 0) {
				Log.info(slotManageQuery.getSlotNo() + "编号在studioslot表中不存在!");
				return HubResponse.errorResp("生成studio返回明细失败!");
			}
			long studioId = studioSlotDtoList.get(0).getStudioId();
			long masterId = 0;
			int i = 0;
			String studioSendNo = sd.format(new Date());
			for (StudioSlotSpuSendDetailDto studioSlotSpuSendDetailDto : studioSlotSpuSendDetailDtoList) {
				if (i == 0) {
					StudioSlotReturnMasterDto studioSlotReturnMasterDto = new StudioSlotReturnMasterDto();
					studioSlotReturnMasterDto.setStudioSendNo(studioSendNo);
					studioSlotReturnMasterDto.setSupplierId(studioSlotSpuSendDetailDto.getSupplierId());
					studioSlotReturnMasterDto.setSupplierNo(studioSlotSpuSendDetailDto.getSupplierNo());
					studioSlotReturnMasterDto.setQuantity(studioSlotSpuSendDetailDtoList.size());
					studioSlotReturnMasterDto.setStudioId(studioId);
					studioSlotReturnMasterDto.setActualSendQuantity(0);
					studioSlotReturnMasterDto.setDamagedQuantity(0);
					studioSlotReturnMasterDto.setMissingQuantity(0);
					studioSlotReturnMasterDto.setAddedQuantiy(0);
					studioSlotReturnMasterDto.setState((byte) 0);
					studioSlotReturnMasterDto.setArriveState((byte) 0);
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
				// 根据供应商id 通过api查找到供应商名称
				String supplierName = shangpinRedis.get("SupplierName_" + studioSlotSpuSendDetailDto.getSupplierNo());
				if (StringUtils.isNotBlank(supplierName)) {
					studioSlotReturnDetailDto.setSupplierName(supplierName);
				} else {
					String supplierUrl = apiAddressProperties.getScmsSupplierInfoUrl()
							+ studioSlotSpuSendDetailDto.getSupplierNo();
					String reSupplierMsg = restTemplate.getForObject(supplierUrl, String.class);
					JSONObject supplierDto = JsonUtil.deserialize2(reSupplierMsg, JSONObject.class);
					shangpinRedis.set("SupplierName_" + studioSlotSpuSendDetailDto.getSupplierNo(),
							supplierDto.get("SupplierName").toString());
					studioSlotReturnDetailDto.setSupplierName(supplierDto.get("SupplierName").toString());
				}

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
		Log.info("end createSlotReturnDetailAndMaster---生成返货信息主表和返货批次明细");
		return HubResponse.successResp(null);
	}

	// 查询返货信息主表
	public HubResponse<?> selectSlotReturnMaster(SlotManageQuery slotManageQuery) {
		Log.info("start selectSlotReturnMaster---查询返货信息主表");
		Log.info("params: slotNo:"+slotManageQuery.getSlotNo()+"supplierName:"+slotManageQuery.getSupplierName()+"pageSize:"+slotManageQuery.getPageSize()+"pageNo:"+slotManageQuery.getPageNo());
		StudioSlotsReturnMasterVo vo = new StudioSlotsReturnMasterVo();
		try {
			StudioSlotReturnDetailCriteriaDto detailDto = new StudioSlotReturnDetailCriteriaDto();
			com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailCriteriaDto.Criteria detailCriteria = detailDto
					.createCriteria();
			if (slotManageQuery.getSlotNo() != null) {
				detailCriteria.andSlotNoEqualTo(slotManageQuery.getSlotNo());
			}
			// 供应商名称
			if (slotManageQuery.getSupplierName() != null) {
				detailCriteria.andSupplierNameEqualTo(slotManageQuery.getSupplierName());
			}
			if (slotManageQuery.getPageSize() != null) {
				detailDto.setPageSize(slotManageQuery.getPageSize());
			}
			if (slotManageQuery.getPageNo() != null) {
				detailDto.setPageNo(slotManageQuery.getPageNo());
			}
			detailDto.setDistinct(true);
			detailDto.setFields(" studio_slot_return_master_id,slot_no ");

			List<StudioSlotReturnDetailDto> studioSlotReturnDetailDtoLists = StudioSlotReturnDetailGateWay
					.selectByCriteria(detailDto);

			int count = studioSlotReturnDetailDtoLists.size();
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
			Log.error("查询返货信息主表失败!");
			e.printStackTrace();
			return HubResponse.errorResp("查询返货信息主表失败!");
		}
		Log.info("end selectSlotReturnMaster---查询返货信息主表");
		return HubResponse.successResp(vo);
	}

	// 查询批次号下所有商品明细
	public HubResponse<?> selectSlotReturnDetail(SlotManageQuery slotManageQuery) {
		Log.info("start selectSlotReturnDetail---查询批次号下所有商品明细");
		Log.info("params: slotNo:"+slotManageQuery.getSlotNo());
		StudioSlotsReturnDetailVo vo = new StudioSlotsReturnDetailVo();
		try {
			StudioSlotReturnDetailCriteriaDto detailDto = new StudioSlotReturnDetailCriteriaDto();
			com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailCriteriaDto.Criteria detailCriteria = detailDto
					.createCriteria();
			if (slotManageQuery.getSlotNo() == null) {
				return HubResponse.errorResp("批次号不能为null!");
			}
			detailCriteria.andSlotNoEqualTo(slotManageQuery.getSlotNo());
			List<StudioSlotReturnDetailDto> studioSlotReturnDetailDtoLists = StudioSlotReturnDetailGateWay
					.selectByCriteria(detailDto);
			vo.setStudioSlotReturnDetailDtoList(studioSlotReturnDetailDtoLists);
		} catch (Exception e) {
			Log.error("查询批次号下所有商品明细失败!");
			e.printStackTrace();
			return HubResponse.errorResp("查询批次号下所有商品明细失败!");
		}
		Log.info("end selectSlotReturnDetail---查询批次号下所有商品明细");
		return HubResponse.successResp(vo);
	}

	// 更新商品明细
	public HubResponse<?> updateSlotReturnDetail(SlotManageQuery slotManageQuery) {
		Log.info("start updateSlotReturnDetail---更新商品明细");
		Log.info("params: slotNo:"+slotManageQuery.getSlotNo()+"barCode:"+slotManageQuery.getBarCode()+"masterId:"+slotManageQuery.getMasterId()+"state:"+slotManageQuery.getState());
		try {
			StudioSlotReturnDetailCriteriaDto detailDto = new StudioSlotReturnDetailCriteriaDto();
			com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailCriteriaDto.Criteria detailCriteria = detailDto
					.createCriteria();
			if (slotManageQuery.getSlotNo() == null) {
				return HubResponse.errorResp("slotNo不能为null");
			}
			if (slotManageQuery.getMasterId() == null) {
				return HubResponse.errorResp("masterId不能为null");
			}
			if (slotManageQuery.getState() == null) {
				return HubResponse.errorResp("state不能为null");
			}
			if (slotManageQuery.getBarCode() == null) {
				return HubResponse.errorResp("barCode不能为null");
			}
			detailCriteria.andBarcodeEqualTo(slotManageQuery.getBarCode());
			List<StudioSlotReturnDetailDto> studioSlotReturnDetailDtoLists = StudioSlotReturnDetailGateWay
					.selectByCriteria(detailDto);
			if (studioSlotReturnDetailDtoLists == null || studioSlotReturnDetailDtoLists.size() == 0) {
				return HubResponse.errorResp("barCode:" + slotManageQuery.getBarCode() + "返货明细表不存在!");
			}

			StudioSlotReturnMasterCriteriaDto studioSlotReturnMasterCriteriaDto = new StudioSlotReturnMasterCriteriaDto();
			studioSlotReturnMasterCriteriaDto.createCriteria()
					.andStudioSlotReturnMasterIdEqualTo(Long.parseLong(slotManageQuery.getMasterId()));
			List<StudioSlotReturnMasterDto> studioSlotReturnMasterDtoLists = studioSlotReturnMasterGateWay
					.selectByCriteria(studioSlotReturnMasterCriteriaDto);
			if (studioSlotReturnMasterDtoLists == null || studioSlotReturnMasterDtoLists.size() == 0) {
				return HubResponse.errorResp("MasterId:" + slotManageQuery.getMasterId() + "返货主表不存在!");
			}
			if (studioSlotReturnDetailDtoLists.get(0).getStudioSlotReturnMasterId().toString()
					.equals(slotManageQuery.getMasterId())) {
				if (studioSlotReturnDetailDtoLists.get(0).getState() == slotManageQuery.getState().byteValue()) {
					return HubResponse.successResp("更新成功！");
				} else {

					if (studioSlotReturnDetailDtoLists.get(0).getState() == 0) {
						if (slotManageQuery.getState().byteValue() == 1) {
							studioSlotReturnMasterDtoLists.get(0).setActualSendQuantity(
									studioSlotReturnMasterDtoLists.get(0).getActualSendQuantity() + 1);
						}
						if (slotManageQuery.getState().byteValue() == 2) {
							studioSlotReturnMasterDtoLists.get(0)
									.setDamagedQuantity(studioSlotReturnMasterDtoLists.get(0).getDamagedQuantity() + 1);
						}
						if (slotManageQuery.getState().byteValue() == 3) {
							studioSlotReturnMasterDtoLists.get(0)
									.setMissingQuantity(studioSlotReturnMasterDtoLists.get(0).getMissingQuantity() + 1);
						}
					}
					if (studioSlotReturnDetailDtoLists.get(0).getState() == 1) {
						if (slotManageQuery.getState().byteValue() == 2) {
							studioSlotReturnMasterDtoLists.get(0).setActualSendQuantity(
									studioSlotReturnMasterDtoLists.get(0).getActualSendQuantity() - 1);
							studioSlotReturnMasterDtoLists.get(0)
									.setDamagedQuantity(studioSlotReturnMasterDtoLists.get(0).getDamagedQuantity() + 1);
						}
						if (slotManageQuery.getState().byteValue() == 3) {
							studioSlotReturnMasterDtoLists.get(0).setActualSendQuantity(
									studioSlotReturnMasterDtoLists.get(0).getActualSendQuantity() - 1);
							studioSlotReturnMasterDtoLists.get(0)
									.setMissingQuantity(studioSlotReturnMasterDtoLists.get(0).getMissingQuantity() + 1);
						}
					}
					if (studioSlotReturnDetailDtoLists.get(0).getState() == 2) {
						if (slotManageQuery.getState().byteValue() == 1) {
							studioSlotReturnMasterDtoLists.get(0).setActualSendQuantity(
									studioSlotReturnMasterDtoLists.get(0).getActualSendQuantity() + 1);
							studioSlotReturnMasterDtoLists.get(0)
									.setDamagedQuantity(studioSlotReturnMasterDtoLists.get(0).getDamagedQuantity() - 1);
						}
						if (slotManageQuery.getState().byteValue() == 3) {
							studioSlotReturnMasterDtoLists.get(0)
									.setMissingQuantity(studioSlotReturnMasterDtoLists.get(0).getMissingQuantity() + 1);
							studioSlotReturnMasterDtoLists.get(0)
									.setDamagedQuantity(studioSlotReturnMasterDtoLists.get(0).getDamagedQuantity() - 1);
						}
					}
					if (studioSlotReturnDetailDtoLists.get(0).getState() == 3) {
						if (slotManageQuery.getState().byteValue() == 1) {
							studioSlotReturnMasterDtoLists.get(0).setActualSendQuantity(
									studioSlotReturnMasterDtoLists.get(0).getActualSendQuantity() + 1);
							studioSlotReturnMasterDtoLists.get(0)
									.setMissingQuantity(studioSlotReturnMasterDtoLists.get(0).getMissingQuantity() - 1);
						}
						if (slotManageQuery.getState().byteValue() == 2) {
							studioSlotReturnMasterDtoLists.get(0)
									.setDamagedQuantity(studioSlotReturnMasterDtoLists.get(0).getDamagedQuantity() + 1);
							studioSlotReturnMasterDtoLists.get(0)
									.setMissingQuantity(studioSlotReturnMasterDtoLists.get(0).getMissingQuantity() - 1);
						}
					}
					studioSlotReturnDetailDtoLists.get(0).setState(slotManageQuery.getState().byteValue());
					StudioSlotReturnDetailGateWay.updateByPrimaryKey(studioSlotReturnDetailDtoLists.get(0));
					studioSlotReturnMasterGateWay.updateByPrimaryKey(studioSlotReturnMasterDtoLists.get(0));

				}
			} else {
				String slotNo = studioSlotReturnDetailDtoLists.get(0).getSlotNo().substring(0, 8);
				String paramSlotNo = slotManageQuery.getSlotNo().substring(0, 8);
				Date date = simpleDateFormat.parse(slotNo);
				Date newDate = simpleDateFormat.parse(paramSlotNo);
				if (date.before(newDate)) {
					StudioSlotReturnDetailDto studioSlotReturnDetail = new StudioSlotReturnDetailDto();
					StudioSlotReturnDetailDto studioSlotReturnDetailDto = studioSlotReturnDetailDtoLists.get(0);

					studioSlotReturnDetail.setStudioSlotReturnMasterId(Long.parseLong(slotManageQuery.getMasterId()));
					studioSlotReturnDetail.setSlotNo(studioSlotReturnDetailDto.getSlotNo());
					studioSlotReturnDetail.setSupplierNo(studioSlotReturnDetailDto.getSupplierId());
					// 根据供应商id 通过api查找到供应商名称
					// 待开发
					studioSlotReturnDetail.setSupplierName(studioSlotReturnDetailDto.getSupplierName());

					studioSlotReturnDetail.setSupplierId(studioSlotReturnDetailDto.getSupplierId());
					studioSlotReturnDetail.setSpuPendingId(studioSlotReturnDetailDto.getSupplierSpuId());
					studioSlotReturnDetail.setSupplierSpuId(studioSlotReturnDetailDto.getSupplierSpuId());
					studioSlotReturnDetail.setSlotSpuNo(studioSlotReturnDetailDto.getSlotSpuNo());
					studioSlotReturnDetail.setSupplierSpuName(studioSlotReturnDetailDto.getSupplierSpuName());
					studioSlotReturnDetail.setSupplierSpuModel(studioSlotReturnDetailDto.getSupplierSpuModel());
					studioSlotReturnDetail.setSupplierBrandName(studioSlotReturnDetailDto.getSupplierBrandName());
					studioSlotReturnDetail.setSupplierCategoryName(studioSlotReturnDetailDto.getSupplierCategoryName());
					studioSlotReturnDetail.setSupplierSeasonName(studioSlotReturnDetailDto.getSupplierSeasonName());
					studioSlotReturnDetail.setState(slotManageQuery.getState().byteValue());
					studioSlotReturnDetail.setSendState((byte) 0);
					studioSlotReturnDetail.setArriveState((byte) 0);
					studioSlotReturnDetail.setBarcode(studioSlotReturnDetailDto.getBarcode());
					studioSlotReturnDetail.setCreateTime(new Date());
					studioSlotReturnDetail.setCreateUser("admin");
					studioSlotReturnDetail.setSendUser("admin");
					studioSlotReturnDetail.setUpdateTime(new Date());
					studioSlotReturnDetail.setUpdateUser("admin");
					StudioSlotReturnDetailGateWay.insertSelective(studioSlotReturnDetail);

					studioSlotReturnMasterDtoLists.get(0)
							.setAddedQuantiy(studioSlotReturnMasterDtoLists.get(0).getAddedQuantiy() + 1);
					studioSlotReturnMasterGateWay.updateByPrimaryKey(studioSlotReturnMasterDtoLists.get(0));
				} else {
					return HubResponse.errorResp("更新商品明细失败!");
				}
			}
		} catch (Exception e) {
			Log.error("更新商品明细失败!");
			e.printStackTrace();
			return HubResponse.errorResp("更新商品明细失败!");
		}
		Log.info("end updateSlotReturnDetail---更新商品明细");
		return HubResponse.successResp("更新成功！");
	}

	// 创建批次物流信息
	public HubResponse<?> createStudioSlotLogistictTrack(SlotManageQuery slotManageQuery) {
		Log.info("start createStudioSlotLogistictTrack---创建批次物流信息");
		Log.info("params: trackName:"+slotManageQuery.getTrackName()+"trackNo:"+slotManageQuery.getTrackNo()+"masterId:"+slotManageQuery.getMasterId());
		try {
			StudioSlotLogistictTrackDto dto = new StudioSlotLogistictTrackDto();

			if (slotManageQuery.getTrackName() == null) {
				return HubResponse.errorResp("trackName不能为null");
			}
			if (slotManageQuery.getTrackNo() == null) {
				return HubResponse.errorResp("trackNo不能为null");
			}
			if (slotManageQuery.getMasterId() == null) {
				return HubResponse.errorResp("masterId不能为null");
			}

			dto.setTrackName(slotManageQuery.getTrackName());
			dto.setTrackNo(slotManageQuery.getTrackNo());
			StudioSlotReturnMasterCriteriaDto criteriaDto = new StudioSlotReturnMasterCriteriaDto();
			com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnMasterCriteriaDto.Criteria criteria = criteriaDto
					.createCriteria();
			criteria.andStudioSlotReturnMasterIdEqualTo(Long.parseLong(slotManageQuery.getMasterId().toString()));
			List<StudioSlotReturnMasterDto> studioSlotReturnMasterDtoList = studioSlotReturnMasterGateWay
					.selectByCriteria(criteriaDto);
			if (studioSlotReturnMasterDtoList == null || studioSlotReturnMasterDtoList.size() == 0) {
				return HubResponse.errorResp("masterId:"+slotManageQuery.getMasterId().toString()+"返货主表不存在!");
			}
			dto.setSendMasterId(studioSlotReturnMasterDtoList.get(0).getStudioSlotReturnMasterId());
			dto.setQuantity(studioSlotReturnMasterDtoList.get(0).getActualSendQuantity());
			dto.setActualNumber(0);
			dto.setTrackStatus((byte) 0);
			dto.setType((byte) 1);
			dto.setCreateTime(new Date());
			dto.setCreateUser("admin");
			dto.setUpdateTime(new Date());
			studioSlotLogistictTrackGateWay.insertSelective(dto);
		} catch (Exception e) {
			Log.error("创建批次物流信息失败!");
			e.printStackTrace();
			return HubResponse.errorResp("创建批次物流信息失败!");
		}
		Log.info("end createStudioSlotLogistictTrack---创建批次物流信息");
		return HubResponse.successResp("更新成功！");
	}
}
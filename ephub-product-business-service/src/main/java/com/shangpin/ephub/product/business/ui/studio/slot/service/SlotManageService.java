package com.shangpin.ephub.product.business.ui.studio.slot.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.esotericsoftware.minlog.Log;
import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.studio.enumeration.StudioReturnDeatilState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotApplyState;
import com.shangpin.ephub.client.data.studio.enumeration.StudioSlotState;
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
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.conf.rpc.ApiAddressProperties;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.StudioSlotsHistoriesVo;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.StudioSlotsReturnDetailVo;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.StudioSlotsReturnMasterVo;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.StudioSlotsVo;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.detail.StudioSlotReturnDetailInfo;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.detail.StudioSlotReturnMasterInfo;
import com.shangpin.ephub.product.business.ui.studio.slot.vo.detail.StudioSlotsHistories;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
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
		log.info("批次查询参数："+JsonUtil.serialize(slotManageQuery));  
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
				if(slotManageQuery.getApplyStatus()==1){
					List<Byte> list = new ArrayList<>();
					list.add(StudioSlotApplyState.APPLYED.getIndex().byteValue());
					list.add(StudioSlotApplyState.HAS_APPLYED_AND_CREATE_STUDIO.getIndex().byteValue());
					criteria.andApplyStatusIn(list);
				}else{
					criteria.andApplyStatusEqualTo(slotManageQuery.getApplyStatus());
				}
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
				String shootTimeStart = slotManageQuery.getShootTime() + " 00:00:00";
				String shootTimeEnd = slotManageQuery.getShootTime() + " 23:59:59";
				criteria.andShootTimeBetween(sdf.parse(shootTimeStart), sdf.parse(shootTimeEnd));
			}
			int count = studioSlotGateWay.countByCriteria(studioSlotCriteriaDto);
			log.info("查到的总数："+count); 

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
			log.error("查询批次失败! "+e.getMessage(),e); 
			e.printStackTrace();
			return HubResponse.errorResp("查询批次失败!");
		}
		return HubResponse.successResp(vo);
	}

	// 生成返货信息主表和返货批次明细
	public HubResponse<?> createSlotReturnDetailAndMaster(SlotManageQuery slotManageQuery) {
		try {
			log.info("start createSlotReturnDetailAndMaster---生成返货信息主表和返货批次明细");
			log.info("params: slotNo:"+slotManageQuery.getSlotNo());
			if (slotManageQuery.getSlotNo() == null) {
				return HubResponse.errorResp("slotNo不能为null!");
			}
			StudioSlotSpuSendDetailCriteriaDto dto = new StudioSlotSpuSendDetailCriteriaDto();
			com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailCriteriaDto.Criteria crieria = dto
					.createCriteria();
			crieria.andSlotNoEqualTo(slotManageQuery.getSlotNo()).andArriveStateEqualTo((byte) 1);
			dto.setPageSize(50);
			List<StudioSlotSpuSendDetailDto> studioSlotSpuSendDetailDtoList = studioSlotSpuSendDetailGateWay
					.selectByCriteria(dto);
			StudioSlotCriteriaDto studioSlotCriteriaDto = new StudioSlotCriteriaDto();
			Criteria criteria = studioSlotCriteriaDto.createCriteria();
			criteria.andSlotNoEqualTo(slotManageQuery.getSlotNo());
			List<StudioSlotDto> studioSlotDtoList = studioSlotGateWay.selectByCriteria(studioSlotCriteriaDto);
			if (studioSlotDtoList == null || studioSlotDtoList.size() == 0) {
				log.info(slotManageQuery.getSlotNo() + "编号在studioslot表中不存在!");
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
		log.info("end createSlotReturnDetailAndMaster---生成返货信息主表和返货批次明细");
		return HubResponse.successResp(null);
	}

	// 查询返货信息主表
	public HubResponse<?> selectSlotReturnMaster(SlotManageQuery slotManageQuery) {
		log.info("start selectSlotReturnMaster---查询返货信息主表");
		log.info("params: slotNo:"+slotManageQuery.getSlotNo()+"supplierName:"+slotManageQuery.getSupplierName()+"pageSize:"+slotManageQuery.getPageSize()+"pageNo:"+slotManageQuery.getPageNo());
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
			detailDto.setFields(" studio_slot_return_master_id,slot_no,supplier_name,send_state ");

			List<StudioSlotReturnDetailDto> studioSlotReturnDetailDtoLists = StudioSlotReturnDetailGateWay
					.selectByCriteria(detailDto);

			int count = StudioSlotReturnDetailGateWay.countByCriteria(detailDto);
			List<StudioSlotReturnMasterInfo> StudioSlotReturnMasterInfoLists = new ArrayList<>();
			List<StudioSlotReturnDetailDto> StudioSlotReturnDetailDtoNewLists = new ArrayList<>();
			HashMap<String,Object> map = new  HashMap<>();
			for (StudioSlotReturnDetailDto studioSlotReturnDetail : studioSlotReturnDetailDtoLists) {
				if(map.containsKey("master_id_"+studioSlotReturnDetail.getStudioSlotReturnMasterId())){
					String slotNo = studioSlotReturnDetail.getSlotNo().substring(0, 8);
					String paramSlotNo = map.get("master_id_"+studioSlotReturnDetail.getStudioSlotReturnMasterId()).toString();
					Date date = simpleDateFormat.parse(slotNo);
					Date newDate = simpleDateFormat.parse(paramSlotNo);
					if (newDate.before(date)) {
						map.put("master_id_"+studioSlotReturnDetail.getStudioSlotReturnMasterId(), slotNo);
					}
				}else{
					map.put("master_id_"+studioSlotReturnDetail.getStudioSlotReturnMasterId(), studioSlotReturnDetail.getSlotNo());
					StudioSlotReturnDetailDtoNewLists.add(studioSlotReturnDetail);
				}
			}
			for (StudioSlotReturnDetailDto studioSlotReturnDetailDto : StudioSlotReturnDetailDtoNewLists) {
				StudioSlotReturnMasterDto studioSlotReturnMasterDto = studioSlotReturnMasterGateWay.selectByPrimaryKey(studioSlotReturnDetailDto.getStudioSlotReturnMasterId());
				if (studioSlotReturnMasterDto != null) {
					if(studioSlotReturnMasterDto.getArriveState()!=0){
//						count = count -1;
						continue;
					}
//					if(!studioSlotReturnMasterDto.getStudioId().toString().equals(slotManageQuery.getStudioId())){
//						continue;
//					}
					StudioSlotReturnMasterInfo info = new StudioSlotReturnMasterInfo();
					info.setSlotNo(map.get("master_id_"+studioSlotReturnDetailDto.getStudioSlotReturnMasterId()).toString());
					info.setQty(studioSlotReturnMasterDto.getQuantity().toString());
					info.setActualQty(studioSlotReturnMasterDto.getActualSendQuantity().toString());
					info.setMissingQty(studioSlotReturnMasterDto.getMissingQuantity().toString());
					info.setDamagedQty(studioSlotReturnMasterDto.getDamagedQuantity().toString());
					info.setAddedQty(studioSlotReturnMasterDto.getAddedQuantiy().toString());
					info.setDestination(studioSlotReturnDetailDto.getSupplierName());
					info.setMasterId(studioSlotReturnDetailDto.getStudioSlotReturnMasterId().toString());
					info.setSendState(studioSlotReturnMasterDto.getState().toString());
					StudioSlotReturnMasterInfoLists.add(info);
				}
			}
			vo.setStudioSlotReturnMasterDtoList(StudioSlotReturnMasterInfoLists);
			vo.setTotal(count);
		} catch (Exception e) {
			Log.error("查询返货信息主表失败!");
			e.printStackTrace();
			return HubResponse.errorResp("查询返货信息主表失败!");
		}
		log.info("end selectSlotReturnMaster---查询返货信息主表");
		return HubResponse.successResp(vo);
	}

	// 查询masterID下所有商品明细
	public HubResponse<?> selectSlotReturnDetail(SlotManageQuery slotManageQuery) {
		log.info("start selectSlotReturnDetail---查询masterId所有商品明细");
		log.info("params: masterId:"+slotManageQuery.getMasterId());
		StudioSlotsReturnDetailVo vo = new StudioSlotsReturnDetailVo();
		try {
			StudioSlotReturnDetailCriteriaDto detailDto = new StudioSlotReturnDetailCriteriaDto();
			com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailCriteriaDto.Criteria detailCriteria = detailDto
					.createCriteria();
			if (slotManageQuery.getMasterId() == null) {
				return HubResponse.errorResp("masterId不能为null!");
			}
			if (slotManageQuery.getSlotNo() == null) {
				return HubResponse.errorResp("slotNo不能为null!");
			}
			detailCriteria.andStudioSlotReturnMasterIdEqualTo(Long.parseLong(slotManageQuery.getMasterId()));
			detailDto.setPageSize(100);
			List<StudioSlotReturnDetailDto> studioSlotReturnDetailDtoLists = StudioSlotReturnDetailGateWay
					.selectByCriteria(detailDto);
			List<StudioSlotReturnDetailInfo> StudioSlotReturnDetailInfoLists = new ArrayList<>();
			for(StudioSlotReturnDetailDto studioSlotReturnDetailDto : studioSlotReturnDetailDtoLists){
				StudioSlotReturnDetailInfo info = new StudioSlotReturnDetailInfo();
				info.setStatus(studioSlotReturnDetailDto.getState().toString());
				info.setBrand(studioSlotReturnDetailDto.getSupplierBrandName());
				info.setItemName(studioSlotReturnDetailDto.getSupplierSpuName());
				info.setItemCode(studioSlotReturnDetailDto.getSupplierSpuModel());
				info.setBarCode(studioSlotReturnDetailDto.getBarcode());
				info.setSlotNo(studioSlotReturnDetailDto.getSlotNo());
				info.setMasterId(studioSlotReturnDetailDto.getStudioSlotReturnMasterId().toString());
				if(studioSlotReturnDetailDto.getSlotNo().equals(slotManageQuery.getSlotNo())){
					info.setIsSlot("0");//0代表属于这个批次
				}else{
					info.setIsSlot("1");//1代表不属于这个批次，新增的以前未发送的商品
				}
				StudioSlotReturnDetailInfoLists.add(info);
			}
			vo.setStudioSlotReturnDetailDtoList(StudioSlotReturnDetailInfoLists);
			vo.setTotal(StudioSlotReturnDetailInfoLists.size());
		} catch (Exception e) {
			Log.error("查询masterId所有商品明细失败!");
			e.printStackTrace();
			return HubResponse.errorResp("查询masterId所有商品明细失败!");
		}
		log.info("end selectSlotReturnDetail---查询masterId所有商品明细");
		return HubResponse.successResp(vo);
	}

	// 更新商品明细
	public HubResponse<?> updateSlotReturnDetail(SlotManageQuery slotManageQuery) {
		log.info("start updateSlotReturnDetail---更新商品明细");
		log.info("params: slotNo:"+slotManageQuery.getSlotNo()+"barCode:"+slotManageQuery.getBarCode()+"masterId:"+slotManageQuery.getMasterId()+"state:"+slotManageQuery.getState());
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

			boolean flg = false;
			for(StudioSlotReturnDetailDto studioSlotReturnDetailDto : studioSlotReturnDetailDtoLists){
				if(studioSlotReturnDetailDto.getStudioSlotReturnMasterId().toString().equals(slotManageQuery.getMasterId())){
					flg = true;
				}
			}
			
			StudioSlotReturnMasterDto studioSlotReturnMasterDto = studioSlotReturnMasterGateWay.selectByPrimaryKey(Long.parseLong(slotManageQuery.getMasterId()));
						if (studioSlotReturnMasterDto == null) {
				return HubResponse.errorResp("MasterId:" + slotManageQuery.getMasterId() + "返货主表不存在!");
			}
			if (flg) {
				StudioSlotReturnDetailDto studioSlotReturnDetailDto = null;
				for(StudioSlotReturnDetailDto studioslotreturndetaildto : studioSlotReturnDetailDtoLists){
					if(studioslotreturndetaildto.getStudioSlotReturnMasterId()==Long.parseLong(slotManageQuery.getMasterId())){
						studioSlotReturnDetailDto = studioslotreturndetaildto;
					}
				}
				if (studioSlotReturnDetailDto.getState() == slotManageQuery.getState().byteValue()) {
					return HubResponse.successResp("更新成功！");
				} else {
                    
					if (studioSlotReturnDetailDto.getState() == StudioReturnDeatilState.WAIT.getIndex().byteValue()) {
						if (slotManageQuery.getState().byteValue() == StudioReturnDeatilState.GOOD.getIndex().byteValue()) {
							studioSlotReturnMasterDto.setActualSendQuantity(
									studioSlotReturnMasterDto.getActualSendQuantity() + 1);
						}
						if (slotManageQuery.getState().byteValue() == StudioReturnDeatilState.DAMAGED.getIndex().byteValue()) {
							studioSlotReturnMasterDto
									.setDamagedQuantity(studioSlotReturnMasterDto.getDamagedQuantity() + 1);
						}
						if (slotManageQuery.getState().byteValue() == StudioReturnDeatilState.MISS.getIndex().byteValue()) {
							studioSlotReturnMasterDto
									.setMissingQuantity(studioSlotReturnMasterDto.getMissingQuantity() + 1);
						}
					}
					if (studioSlotReturnDetailDto.getState() == StudioReturnDeatilState.GOOD.getIndex().byteValue()) {
						if (slotManageQuery.getState().byteValue() == StudioReturnDeatilState.DAMAGED.getIndex().byteValue()) {
							studioSlotReturnMasterDto.setActualSendQuantity(
									studioSlotReturnMasterDto.getActualSendQuantity() - 1);
							studioSlotReturnMasterDto
									.setDamagedQuantity(studioSlotReturnMasterDto.getDamagedQuantity() + 1);
						}
						if (slotManageQuery.getState().byteValue() == StudioReturnDeatilState.MISS.getIndex().byteValue()) {
							studioSlotReturnMasterDto.setActualSendQuantity(
									studioSlotReturnMasterDto.getActualSendQuantity() - 1);
							studioSlotReturnMasterDto
									.setMissingQuantity(studioSlotReturnMasterDto.getMissingQuantity() + 1);
						}
						if(!studioSlotReturnDetailDto.getSlotNo().equals(slotManageQuery.getSlotNo())){
							studioSlotReturnMasterDto.setAddedQuantiy(studioSlotReturnMasterDto.getAddedQuantiy()-1);
						}

					}
					if (studioSlotReturnDetailDto.getState() == StudioReturnDeatilState.DAMAGED.getIndex().byteValue()) {
						if (slotManageQuery.getState().byteValue() == StudioReturnDeatilState.GOOD.getIndex().byteValue()) {
							studioSlotReturnMasterDto.setActualSendQuantity(
									studioSlotReturnMasterDto.getActualSendQuantity() + 1);
							studioSlotReturnMasterDto
									.setDamagedQuantity(studioSlotReturnMasterDto.getDamagedQuantity() - 1);
							if(!studioSlotReturnDetailDto.getSlotNo().equals(slotManageQuery.getSlotNo())){
								studioSlotReturnMasterDto.setAddedQuantiy(studioSlotReturnMasterDto.getAddedQuantiy()+1);
							}
						}
						if (slotManageQuery.getState().byteValue() == StudioReturnDeatilState.MISS.getIndex().byteValue()) {
							studioSlotReturnMasterDto
									.setMissingQuantity(studioSlotReturnMasterDto.getMissingQuantity() + 1);
							studioSlotReturnMasterDto
									.setDamagedQuantity(studioSlotReturnMasterDto.getDamagedQuantity() - 1);
						}
					}
					if (studioSlotReturnDetailDto.getState() == StudioReturnDeatilState.MISS.getIndex().byteValue()) {
						if (slotManageQuery.getState().byteValue() == StudioReturnDeatilState.GOOD.getIndex().byteValue()) {
							studioSlotReturnMasterDto.setActualSendQuantity(
									studioSlotReturnMasterDto.getActualSendQuantity() + 1);
							studioSlotReturnMasterDto
									.setMissingQuantity(studioSlotReturnMasterDto.getMissingQuantity() - 1);
							if(!studioSlotReturnDetailDto.getSlotNo().equals(slotManageQuery.getSlotNo())){
								studioSlotReturnMasterDto.setAddedQuantiy(studioSlotReturnMasterDto.getAddedQuantiy()+1);
							}
						}
						if (slotManageQuery.getState().byteValue() == StudioReturnDeatilState.DAMAGED.getIndex().byteValue()) {
							studioSlotReturnMasterDto
									.setDamagedQuantity(studioSlotReturnMasterDto.getDamagedQuantity() + 1);
							studioSlotReturnMasterDto
									.setMissingQuantity(studioSlotReturnMasterDto.getMissingQuantity() - 1);
						}
					}
					studioSlotReturnDetailDto.setState(slotManageQuery.getState().byteValue());
					StudioSlotReturnDetailGateWay.updateByPrimaryKey(studioSlotReturnDetailDto);
					studioSlotReturnMasterGateWay.updateByPrimaryKey(studioSlotReturnMasterDto);

				}
			} else {
				
				StudioSlotReturnMasterDto returnMasterDto = studioSlotReturnMasterGateWay.selectByPrimaryKey(studioSlotReturnDetailDtoLists.get(0).getStudioSlotReturnMasterId());
				if(returnMasterDto.getState()==0){
					log.info("barCode:" + slotManageQuery.getBarCode()+"slotNo:"+slotManageQuery.getBarCode() + "此批次还未返货，请扫码对应返货批次!");
					return HubResponse.errorResp("update failure！");
				}
				StudioSlotReturnDetailCriteriaDto studioslotreturndetailcriteriadto = new StudioSlotReturnDetailCriteriaDto();
				studioslotreturndetailcriteriadto.createCriteria().andSlotNoEqualTo(studioSlotReturnDetailDtoLists.get(0).getSlotNo()).andBarcodeEqualTo(slotManageQuery.getBarCode()).andStudioSlotReturnMasterIdEqualTo(Long.parseLong(slotManageQuery.getMasterId()));
				List<StudioSlotReturnDetailDto> detaildtolists = StudioSlotReturnDetailGateWay.selectByCriteria(studioslotreturndetailcriteriadto);
				if(detaildtolists.size()>0){
					log.info("此商品已经新增到此批次");
					return HubResponse.errorResp("1", "update failure！");
				}
				
//				String slotNo = studioSlotReturnDetailDtoLists.get(0).getSlotNo().substring(0, 8);
//				String paramSlotNo = slotManageQuery.getSlotNo().substring(0, 8);
//				Date date = simpleDateFormat.parse(slotNo);
//				Date newDate = simpleDateFormat.parse(paramSlotNo);
//				if (date.before(newDate)) {
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

					studioSlotReturnMasterDto.setAddedQuantiy(studioSlotReturnMasterDto.getAddedQuantiy() + 1);
					studioSlotReturnMasterGateWay.updateByPrimaryKey(studioSlotReturnMasterDto);
					log.info("end updateSlotReturnDetail---更新商品明细");
					log.info("不属于当前批次,但批次时间在当前批次之前,可以返货");
					return HubResponse.errorResp("2", "update failure！");
//				} else {
//					log.info("end updateSlotReturnDetail---更新商品明细");
//					return HubResponse.errorResp("3", "不属于当前批次,并且批次时间在当前批次之后,不能返货！");
//				}
			}
		} catch (Exception e) {
			Log.error("更新商品明细失败!");
			e.printStackTrace();
			return HubResponse.errorResp("update failure!");
		}
		log.info("end updateSlotReturnDetail---更新商品明细");
		return HubResponse.successResp("update success！");
	}

	// 创建批次物流信息
	public HubResponse<?> createStudioSlotLogistictTrack(SlotManageQuery slotManageQuery) {
		log.info("start createStudioSlotLogistictTrack---创建批次物流信息");
		log.info("params: trackName:"+slotManageQuery.getTrackName()+"trackNo:"+slotManageQuery.getTrackNo()+"masterId:"+slotManageQuery.getMasterId());
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
			if (slotManageQuery.getUserName() == null) {
				return HubResponse.errorResp("userName不能为null");
			}
			
			StudioSlotReturnMasterDto studioSlotReturnMaster= studioSlotReturnMasterGateWay.selectByPrimaryKey(Long.parseLong(slotManageQuery.getMasterId()));
            if(studioSlotReturnMaster==null){
            	return HubResponse.errorResp("masterId:"+slotManageQuery.getMasterId()+"没有对应的返货主表!");
            }
            
            studioSlotReturnMaster.setTrackNo(slotManageQuery.getTrackNo());
            studioSlotReturnMaster.setState((byte) 1);
            studioSlotReturnMaster.setSendUser(slotManageQuery.getOperatorName());
            studioSlotReturnMaster.setSendTime(new Date());
            studioSlotReturnMaster.setSendState((byte) 1);
            studioSlotReturnMasterGateWay.updateByPrimaryKey(studioSlotReturnMaster);
			
			dto.setTrackName(slotManageQuery.getTrackName());
			dto.setTrackNo(slotManageQuery.getTrackNo());
			StudioSlotReturnMasterDto studioSlotReturnMasterDto = studioSlotReturnMasterGateWay.selectByPrimaryKey(Long.parseLong(slotManageQuery.getMasterId()));
			if (studioSlotReturnMasterDto == null) {
				return HubResponse.errorResp("masterId:"+slotManageQuery.getMasterId().toString()+"返货主表不存在!");
			}
			dto.setSendMasterId(studioSlotReturnMasterDto.getStudioSlotReturnMasterId());
			dto.setQuantity(studioSlotReturnMasterDto.getActualSendQuantity());
			dto.setActualNumber(0);
			dto.setTrackStatus((byte) 0);
			dto.setType((byte) 1);
			dto.setCreateTime(new Date());
			dto.setCreateUser(slotManageQuery.getUserName());
			dto.setUpdateTime(new Date());
			studioSlotLogistictTrackGateWay.insertSelective(dto);
			
			StudioSlotReturnDetailCriteriaDto detailDto = new StudioSlotReturnDetailCriteriaDto();
			com.shangpin.ephub.client.data.studio.slot.returning.dto.StudioSlotReturnDetailCriteriaDto.Criteria detailCriteria = detailDto
					.createCriteria();
			detailCriteria.andStudioSlotReturnMasterIdEqualTo(studioSlotReturnMasterDto.getStudioSlotReturnMasterId());
			detailDto.setDistinct(true);
			detailDto.setFields(" studio_slot_return_master_id,slot_no ");
			List<StudioSlotReturnDetailDto> studioSlotReturnDetailDtoLists = StudioSlotReturnDetailGateWay
					.selectByCriteria(detailDto);
			String slotNo = "";
			if(studioSlotReturnDetailDtoLists==null||studioSlotReturnDetailDtoLists.size()==0){
				log.info("没有找到主表对应的批次表!");
			}
			if(studioSlotReturnDetailDtoLists.size()==1){
				slotNo = studioSlotReturnDetailDtoLists.get(0).getSlotNo();
			}
			if(studioSlotReturnDetailDtoLists.size()>1){
				HashMap<String, Object> map = new HashMap<>();
				for (StudioSlotReturnDetailDto studioSlotReturnDetail : studioSlotReturnDetailDtoLists) {
					if(map.containsKey("master_id_"+studioSlotReturnDetail.getStudioSlotReturnMasterId())){
						String slotno = studioSlotReturnDetail.getSlotNo().substring(0, 8);
						String paramSlotno = map.get("master_id_"+studioSlotReturnDetail.getStudioSlotReturnMasterId()).toString();
						Date date = simpleDateFormat.parse(slotno);
						Date newDate = simpleDateFormat.parse(paramSlotno);
						if (newDate.before(date)) {
							map.put("master_id_"+studioSlotReturnDetail.getStudioSlotReturnMasterId(), slotNo);
						}
					}else{
						map.put("master_id_"+studioSlotReturnDetail.getStudioSlotReturnMasterId(), studioSlotReturnDetail.getSlotNo());
					}
				}
				slotNo = map.get("master_id_"+studioSlotReturnMasterDto.getStudioSlotReturnMasterId()).toString();
			}
			StudioSlotCriteriaDto StudioSlotcriteriadto = new StudioSlotCriteriaDto();
			StudioSlotcriteriadto.createCriteria().andSlotNoEqualTo(slotNo);
			List<StudioSlotDto> studioSlotDtoList = studioSlotGateWay.selectByCriteria(StudioSlotcriteriadto);
			if(studioSlotDtoList==null||studioSlotDtoList.size()==0){
				log.info("slotNo:"+slotNo+"不存在对应的批次!");
			}
			studioSlotDtoList.get(0).setSlotStatus(StudioSlotState.STUDIO_RETURN.getIndex().byteValue());
			studioSlotGateWay.updateByPrimaryKey(studioSlotDtoList.get(0));

			StudioSlotReturnDetailCriteriaDto studioSlotReturnDetailCriteriaDto = new StudioSlotReturnDetailCriteriaDto();
			studioSlotReturnDetailCriteriaDto.createCriteria().andStudioSlotReturnMasterIdEqualTo(Long.parseLong(slotManageQuery.getMasterId()));
			List<StudioSlotReturnDetailDto> list = StudioSlotReturnDetailGateWay
					.selectByCriteria(studioSlotReturnDetailCriteriaDto);
			for(StudioSlotReturnDetailDto studioSlotReturnDetailDto : list){
				if(studioSlotReturnDetailDto.getState()==1){
					studioSlotReturnDetailDto.setSendState((byte) 1);
					studioSlotReturnDetailDto.setSendTime(new Date());
					studioSlotReturnDetailDto.setSendUser(slotManageQuery.getUserName());
					StudioSlotReturnDetailGateWay.updateByPrimaryKey(studioSlotReturnDetailDto);
				}
			}
		} catch (Exception e) {
			Log.error("创建批次物流信息失败!");
			e.printStackTrace();
			return HubResponse.errorResp("创建批次物流信息失败!");
		}
		log.info("end createStudioSlotLogistictTrack---创建批次物流信息");
		return HubResponse.successResp("更新成功！");
	}
	// 查询批次当前节点
	public HubResponse<?> selectHisttoryStudioSlot(SlotManageQuery slotManageQuery) {
		log.info("start selectHisttoryStudioSlot---查询批次当前节点");
		log.info("params: SlotNo:" + slotManageQuery.getSlotNo() + "status:" + slotManageQuery.getSlotStatus()
				+ "sender:" + slotManageQuery.getSender() + "Milestone:" + slotManageQuery.getMilestone()
				+ "startDate:" + slotManageQuery.getStartDate() + "endDate:" + slotManageQuery.getEndDate());
		StudioSlotsHistoriesVo vo = new StudioSlotsHistoriesVo();
		try {
			StudioSlotCriteriaDto dto = new StudioSlotCriteriaDto();
			com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotCriteriaDto.Criteria criteria = dto.createCriteria();
					
			if (slotManageQuery.getSlotNo() != null) {
				criteria.andSlotNoEqualTo(slotManageQuery.getSlotNo());
			}
			if (slotManageQuery.getSlotStatus() != null) {
				criteria.andSlotStatusEqualTo(slotManageQuery.getSlotStatus());
			}else{
				List<Byte> list = new ArrayList<>();
				list.add((byte) 0);
				list.add((byte) 1);
				criteria.andSlotStatusNotIn(list);
			}
			if (slotManageQuery.getSender() != null) {
				criteria.andSendUserEqualTo(slotManageQuery.getSender());
			}
			if (slotManageQuery.getStartDate() != null&&slotManageQuery.getEndDate() !=null) {
				if(slotManageQuery.getMilestone()==0){
					criteria.andSendTimeBetween(slotManageQuery.getStartDate(), slotManageQuery.getEndDate());
				}
				if(slotManageQuery.getMilestone()==1){
					criteria.andPlanArriveTimeBetween(slotManageQuery.getStartDate(), slotManageQuery.getEndDate());
				}
				if(slotManageQuery.getMilestone()==2){
					criteria.andArriveTimeBetween(slotManageQuery.getStartDate(), slotManageQuery.getEndDate());
				}
				if(slotManageQuery.getMilestone()==3){
					criteria.andShootTimeBetween(slotManageQuery.getStartDate(), slotManageQuery.getEndDate());
				}
			}
			if (slotManageQuery.getPageSize() != null) {
				dto.setPageSize(slotManageQuery.getPageSize());
			}
			if (slotManageQuery.getPageNo() != null) {
				dto.setPageNo(slotManageQuery.getPageNo());
			}
			List<StudioSlotDto> studioSlotDtoList = studioSlotGateWay.selectByCriteria(dto);
			int count = studioSlotGateWay.countByCriteria(dto);
			List<StudioSlotsHistories> studioSlotsHistoriesList = new ArrayList<>();
			for (StudioSlotDto studioSlotDto : studioSlotDtoList) {
//				if(!studioSlotDto.getStudioId().toString().equals(slotManageQuery.getStudioId())){
//					continue;
//				}
				StudioSlotsHistories studioSlotsHistories = new StudioSlotsHistories();
				studioSlotsHistories.setSlotNo(studioSlotDto.getSlotNo());
				if (studioSlotDto.getSlotStatus() == 2) {
					studioSlotsHistories.setStatus("incoming");
				}
				if (studioSlotDto.getSlotStatus() == 3) {
					studioSlotsHistories.setStatus("received");
				}
				if (studioSlotDto.getSlotStatus() == 4) {
					studioSlotsHistories.setStatus("inspected");
				}
				if (studioSlotDto.getSlotStatus() == 5) {
					studioSlotsHistories.setStatus("pre_return");
				}
				if (studioSlotDto.getSlotStatus() == 6) {
					studioSlotsHistories.setStatus("returnd");
				}
				if (studioSlotDto.getSlotStatus() == 7) {
					studioSlotsHistories.setStatus("finished");
				}
				studioSlotsHistories.setSender(studioSlotDto.getSendUser());
				if (studioSlotDto.getSendTime() != null) {
					studioSlotsHistories.setSendingDate(sdf.format(studioSlotDto.getSendTime()));
				}
				if (studioSlotDto.getArriveTime() != null && studioSlotDto.getArriveUser() != null) {
					studioSlotsHistories
							.setQA(studioSlotDto.getArriveUser() + "|" + sdf.format(studioSlotDto.getArriveTime()));
				}
				StudioSlotReturnDetailCriteriaDto detailDto = new StudioSlotReturnDetailCriteriaDto();
				detailDto.createCriteria().andSlotNoEqualTo(studioSlotDto.getSlotNo());
				detailDto.setDistinct(true);
				detailDto.setFields(" studio_slot_return_master_id ");
				List<StudioSlotReturnDetailDto> studioSlotReturnDetailDtoLists = StudioSlotReturnDetailGateWay
						.selectByCriteria(detailDto);
				if (studioSlotReturnDetailDtoLists != null && studioSlotReturnDetailDtoLists.size() != 0) {
					long masterId = 0;
					if (studioSlotReturnDetailDtoLists.size() > 1) {
						for (StudioSlotReturnDetailDto studioslotreturndetaildto : studioSlotReturnDetailDtoLists) {
							if (masterId == 0) {
								masterId = studioslotreturndetaildto.getStudioSlotReturnMasterId();
							} else {
								if (masterId > studioslotreturndetaildto.getStudioSlotReturnMasterId()) {
									masterId = studioslotreturndetaildto.getStudioSlotReturnMasterId();
								}
							}
						}
					} else {
						masterId = studioSlotReturnDetailDtoLists.get(0).getStudioSlotReturnMasterId();
					}

					StudioSlotReturnMasterDto studioSlotReturnMasterDto = studioSlotReturnMasterGateWay
							.selectByPrimaryKey(masterId);
					if (studioSlotReturnMasterDto == null) {
						count = count - 1;
						continue;
					}
					if (slotManageQuery.getStartDate() != null && slotManageQuery.getEndDate() != null) {
						if (slotManageQuery.getMilestone() == 4 && studioSlotReturnMasterDto.getSendTime() != null) {
							if (!studioSlotReturnMasterDto.getSendTime().before(slotManageQuery.getEndDate())
									|| !studioSlotReturnMasterDto.getSendTime().after(slotManageQuery.getStartDate())) {
								count = count - 1;
								continue;
							}
						}
						if (slotManageQuery.getMilestone() == 5 && studioSlotReturnMasterDto.getArriveTime() != null) {
							if (!studioSlotReturnMasterDto.getArriveTime().before(slotManageQuery.getEndDate())
									&& !studioSlotReturnMasterDto.getArriveTime()
											.after(slotManageQuery.getStartDate())) {
								count = count - 1;
								continue;
							}
						}
					}
					if (studioSlotReturnMasterDto.getSendTime() != null
							&& studioSlotReturnMasterDto.getSendUser() != null) {
						studioSlotsHistories.setReturn(studioSlotReturnMasterDto.getSendUser() + "|"
								+ sdf.format(studioSlotReturnMasterDto.getSendTime()));
					}

					if (studioSlotReturnMasterDto.getArriveTime() != null) {
						studioSlotsHistories
								.setReturnConFirmDate(sdf.format(studioSlotReturnMasterDto.getArriveTime()));
					}
				}
				studioSlotsHistoriesList.add(studioSlotsHistories);
			}
			vo.setStudioSlotsHistoriesList(studioSlotsHistoriesList);
			vo.setTotal(count);

		} catch (Exception e) {
			Log.error("查询批次当前节点失败!");
			e.printStackTrace();
			return HubResponse.errorResp("查询批次当前节点失败!");
		}
		log.info("end selectHisttoryStudioSlot---查询批次当前节点息");
		return HubResponse.successResp(vo);
	}
}

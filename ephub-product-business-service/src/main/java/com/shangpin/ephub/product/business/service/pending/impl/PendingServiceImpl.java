package com.shangpin.ephub.product.business.service.pending.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shangpin.ephub.client.data.mysql.enumeration.*;
import com.shangpin.ephub.client.data.mysql.spu.dto.*;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.client.data.mysql.studio.pic.dto.HubSlotSpuPicDto;
import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import com.shangpin.ephub.client.data.studio.enumeration.UploadPicSign;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuService;
import com.shangpin.ephub.product.business.service.studio.hubslotpic.SlotPicService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.consumer.pending.gateway.HubSpuPendingAuditGateWay;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.data.mysql.product.dto.SpuModelDto;
import com.shangpin.ephub.client.data.mysql.product.gateway.PengdingToHubGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.util.DateTimeUtil;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.client.util.RegexUtil;
import com.shangpin.ephub.product.business.common.enumeration.GlobalConstant;
import com.shangpin.ephub.product.business.common.enumeration.SpuStatus;
import com.shangpin.ephub.product.business.rest.gms.dto.FourLevelCategory;
import com.shangpin.ephub.product.business.rest.gms.service.CategoryService;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuModelMsgVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuModelVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuPendingAuditQueryVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuPendingAuditVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuPendingPicVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuPendingVO;

import lombok.extern.slf4j.Slf4j;

import static com.shangpin.ephub.product.business.service.ServiceConstant.HUB_SPU_PIC_USE_SHOOT;

/**
 * Created by loyalty on 16/12/24.
 * @param
 */
@Service
@Slf4j
public class PendingServiceImpl implements com.shangpin.ephub.product.business.service.pending.PendingService {

    @Autowired
    HubSpuPendingGateWay spuPendingGateWay;

    @Autowired
    HubSkuPendingGateWay skuPendingGateWay;

    @Autowired
    HubSpuPendingPicGateWay spuPendingPicGateWay;

    @Autowired
    HubSpuGateWay hubSpuGateWay;


    @Autowired
    PengdingToHubGateWay pengdingToHubGateWay;

    @Autowired
    HubSpuPendingAuditGateWay hubSpuPendingAuditGateWay;

    @Autowired
    HubSlotSpuService slotSpuService;

    @Autowired
    SlotPicService slotPicService;

    @Autowired
    private TaskExecutor executor;

    @Autowired
    private CategoryService categoryService;

    @Override
    public SpuModelMsgVO getSpuModel(SpuPendingAuditQueryVO queryVO) {

        HubSpuPendingCriteriaDto criteria = getHubSpuPendingCriteria(queryVO);
        if(null==queryVO.getPage()) queryVO.setPage(1);
        if(null==queryVO.getPageSize()) queryVO.setPageSize(10);
        criteria.setPageSize(queryVO.getPageSize());
        criteria.setPageNo(queryVO.getPage());

        HubSpuPendingCriteriaDto criteriaCount = getHubSpuPendingCriteria(queryVO);
        Integer count = spuPendingGateWay.countDistinctBrandNoAndSpuModelByCriteria(criteriaCount);
        List<HubSpuPendingDto> hubSpuPendingDtos = spuPendingGateWay.selectByCriteria(criteria);


        SpuModelMsgVO spuModelMsgVO = new SpuModelMsgVO();
        spuModelMsgVO.setTotal(0);
        List<SpuModelVO> spuModelVOS = new ArrayList<>();
        if(count>0){
            spuModelMsgVO.setTotal(count);

            for(HubSpuPendingDto dto:hubSpuPendingDtos){
                SpuModelVO spuModelVO = new SpuModelVO();
                spuModelVO.setSpuModel(dto.getSpuModel());
                spuModelVO.setBrandNo(dto.getHubBrandNo());
                spuModelVOS.add(spuModelVO);

            }

        }
        spuModelMsgVO.setSpuModels(spuModelVOS);
        return spuModelMsgVO;

    }

    private HubSpuPendingCriteriaDto getHubSpuPendingCriteria(SpuPendingAuditQueryVO queryVO) {
        HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
        HubSpuPendingCriteriaDto.Criteria criterion = criteria.createCriteria();
        criteria.setDistinct(true);
        criteria.setFields(" spu_model,hub_brand_no  ");
        if(StringUtils.isNotBlank(queryVO.getSpuModel())){
            criterion.andSpuModelLike(queryVO.getSpuModel() + "%");

        }
        if(StringUtils.isNotBlank(queryVO.getBrandNo())){
            criterion.andHubBrandNoEqualTo(queryVO.getBrandNo());
        }
        if(StringUtils.isNotBlank(queryVO.getCategoryNo())){
            criterion.andHubCategoryNoLike(queryVO.getCategoryNo() +"%");
        }

        if(StringUtils.isNotBlank(queryVO.getSupplierNo())){
            criterion.andSupplierNoEqualTo(queryVO.getSupplierNo());
        }

        if(StringUtils.isNotBlank(queryVO.getStartDate())){
//            log.info("sartDate = " + DateTimeUtil.getDateTimeFormate(queryVO.getStartDate() +" 00:00:00").toString());
//            System.out.println(DateTimeUtil.getShortDate(queryVO.getStartDate() ));
				criterion.andUpdateTimeGreaterThanOrEqualTo(DateTimeUtil.parse(queryVO.getStartDate()  ));
//            criterion.andUpdateTimeGreaterThanOrEqualTo(DateTimeUtil.getDateTimeFormate(queryVO.getStartDate() +" 08:00:00" ));
        }
        if(StringUtils.isNotBlank(queryVO.getEndDate())){
//            log.info("getEndDate = " + DateTimeUtil.getDateTimeFormate(queryVO.getEndDate() +" 00:00:00").toString());
//            System.out.println(DateTimeUtil.getShortDate(queryVO.getEndDate()));
				criterion.andUpdateTimeLessThan(DateTimeUtil.parse(queryVO.getEndDate() ));
        }
        if(null==queryVO.getStatus()){
            criterion.andSpuStateEqualTo(SpuStatus.SPU_WAIT_AUDIT.getIndex().byteValue());
        }else{
            criterion.andSpuStateEqualTo(queryVO.getStatus().byteValue());
        }
        if(StringUtils.isNotBlank(queryVO.getOperator())){
        	criterion.andUpdateUserLike(queryVO.getOperator()+"%"); 
        }

        return criteria;
    }




    @Override
    public List<SpuPendingVO> getSpuPendingByBrandNoAndSpuModel(String brandNo, String spuModel) {

        List<SpuPendingVO> spuPendingVOS= new ArrayList<>();
        if(StringUtils.isBlank(brandNo)||StringUtils.isBlank(spuModel)){
            return spuPendingVOS;
        }

        HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
        HubSpuPendingCriteriaDto.Criteria criterion = criteria.createCriteria();
        criterion.andSpuModelEqualTo(spuModel).andHubBrandNoEqualTo(brandNo)
                .andSpuStateEqualTo(SpuStatus.SPU_WAIT_AUDIT.getIndex().byteValue());

        List<HubSpuPendingDto> hubSpuPendingDtos = spuPendingGateWay.selectByCriteria(criteria);

        int index= 0;
        if(null!=hubSpuPendingDtos&&hubSpuPendingDtos.size()>0){
            int maxPic = 0;

            //获取是否有hub_spu
            Boolean isHaveHubSpu = false;
            HubSpuDto spuDto = getHubSpu(brandNo, spuModel);
            if(null!=spuDto){
                isHaveHubSpu = true;
            }


            //获取hubslotspupic
            Map<String, List<HubSlotSpuPicDto>> shootPicMap =     getShootPic(brandNo, spuModel);
            boolean isUseShootPic = false;
            for(int i=0;i<hubSpuPendingDtos.size();i++){
                HubSpuPendingDto spuPendingDto = hubSpuPendingDtos.get(i);
                SpuPendingVO spuPendingVO = new SpuPendingVO();
                BeanUtils.copyProperties(spuPendingDto,spuPendingVO);
                spuPendingVO.setColor(spuPendingDto.getHubColor());
                spuPendingVO.setBrandNo(spuPendingDto.getHubBrandNo());
                spuPendingVO.setCategoryNo(spuPendingDto.getHubCategoryNo());
                spuPendingVO.setMaterial(spuPendingDto.getHubMaterial());
                spuPendingVO.setOrigin(spuPendingDto.getHubOrigin());
                spuPendingVO.setIsDefaultSupplier(false);
                spuPendingVO.setHaveHubSpu(isHaveHubSpu);
                if(isHaveHubSpu){
                    //赋值hubspu属性
                    spuPendingVO.setHubCategory(spuDto.getCategoryNo());
                    spuPendingVO.setHubColor(spuDto.getHubColor());
                    spuPendingVO.setHubMaterial(spuDto.getMaterial());

                }
                if(null!=spuPendingDto.getUpdateTime()){
                    spuPendingVO.setUpdateTime(DateTimeUtil.convertFormat(spuPendingDto.getUpdateTime(),"yyyy-MM-dd HH:mm:ss"));
                }


                //获取图片
                if(null!=shootPicMap&&shootPicMap.size()>0){
                    isUseShootPic = true;
                    index = i;
                    useShootPic(shootPicMap,  spuPendingDto, spuPendingVO);
                }else{

                    HubSpuPendingPicCriteriaDto criteriaPic = new HubSpuPendingPicCriteriaDto();
                    criteriaPic.setPageNo(1);
                    criteriaPic.setPageSize(100);
                    criteriaPic.createCriteria().andSupplierSpuIdEqualTo(spuPendingDto.getSupplierSpuId())
                            .andPicHandleStateEqualTo(PicState.HANDLED.getIndex()).andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
                    List<HubSpuPendingPicDto> hubSpuPendingPicDtos = spuPendingPicGateWay.selectByCriteria(criteriaPic);
                    if(null!=hubSpuPendingDtos){
                        if(hubSpuPendingDtos.size()>maxPic){
                            maxPic = hubSpuPendingPicDtos.size();
                            index = i;//获取拥有图片数量最多的spupending
                        }

                        List<SpuPendingPicVO>  picVOs = new ArrayList<>();

                        for(HubSpuPendingPicDto picDto:hubSpuPendingPicDtos){
                            //首先查询 图片是否已经有spuPending编号，如果没有反写进去
//                        if(null==picDto.getSpuPendingId()){
//                            HubSpuPendingPicDto tmp = new HubSpuPendingPicDto();
//                            tmp.setSpuPendingPicId(picDto.getSpuPendingPicId());
//                            tmp.setSpuPendingId(spuPendingDto.getSpuPendingId());
//                            spuPendingPicGateWay.updateByPrimaryKeySelective(tmp);
//                            picDto.setSpuPendingId(tmp.getSpuPendingId());
//                        }
                            SpuPendingPicVO picVO = new SpuPendingPicVO();
                            picVO.setSpuPendingId(null==picDto.getSpuPendingId()?0:picDto.getSpuPendingId());
                            picVO.setPicId(picDto.getSpuPendingPicId());
                            picVO.setSpPicUrl(picDto.getSpPicUrl());
                            picVOs.add(picVO);
                        }

                        spuPendingVO.setPicVOs(picVOs);
                    }
                }
                spuPendingVO.setHaveShootPic(isUseShootPic);
                spuPendingVOS.add(spuPendingVO);
            }

            spuPendingVOS.get(index).setIsDefaultSupplier(true);
        }

        return spuPendingVOS;
    }

    private void useShootPic(Map<String, List<HubSlotSpuPicDto>> shootPicMap, HubSpuPendingDto spuPendingDto, SpuPendingVO spuPendingVO) {

        List<HubSlotSpuPicDto> slotSpuPicDtos = null;
        if(shootPicMap.containsKey(spuPendingDto.getSupplierId())) {
            slotSpuPicDtos = shootPicMap.get(spuPendingDto.getSupplierId());
        }else{
            slotSpuPicDtos = shootPicMap.get(shootPicMap.keySet().iterator().next());
        }
        List<SpuPendingPicVO> picVOs = new ArrayList<>();
        for (HubSlotSpuPicDto picDto : slotSpuPicDtos) {
            SpuPendingPicVO picVO = new SpuPendingPicVO();
            picVO.setSpuPendingId(spuPendingDto.getSpuPendingId());
            picVO.setPicId(picDto.getSlotSpuPicId());
            picVO.setSpPicUrl(picDto.getSpPicUrl());
            picVO.setMemo(HUB_SPU_PIC_USE_SHOOT);
            picVOs.add(picVO);
        }

        spuPendingVO.setPicVOs(picVOs);

    }

    private Map<String, List<HubSlotSpuPicDto>>  getShootPic(String brandNo, String spuModel) {
        Map<String, List<HubSlotSpuPicDto>> shootPic =null;
        HubSlotSpuDto hubSlotSpu = slotSpuService.findHubSlotSpu(brandNo, spuModel);
        if(null!=hubSlotSpu&&hubSlotSpu.getPicSign()!=null){
            if(UploadPicSign.HAVE_UPLOADED.getIndex().byteValue() ==hubSlotSpu.getPicSign()){//有图片
                 shootPic = slotPicService.findShootPic(hubSlotSpu.getSlotSpuId());
            }
        }
        return shootPic;
    }

    private HubSpuDto getHubSpu(String brandNo, String spuModel) {
        HubSpuCriteriaDto spuCriteriaDto = new HubSpuCriteriaDto();
        spuCriteriaDto.createCriteria().andSpuModelEqualTo(spuModel).andBrandNoEqualTo(brandNo);
        List<HubSpuDto> hubSpuDtos = hubSpuGateWay.selectByCriteria(spuCriteriaDto);
        if(null!=hubSpuDtos&&hubSpuDtos.size()>0){
              return hubSpuDtos.get(0);
        }
        return null;
    }

    @Override
    /**
     * 事务问题 需要考虑  是否整体处理
     *
     * 异步处理 先处理pending的状态为审核中，
     * 线程启动 调用hubspu和hubsku的生成
     * 成功的修改PENDING 状态为审核通过 ，失败的修改pending状态为待处理
     *
     */
    public boolean audit(SpuPendingAuditVO auditVO) throws Exception {
    	log.info(auditVO.getSpuModel()+" >>>开始复核校验");
        //更新状态
        HubSpuPendingDto hubSpuPending = new HubSpuPendingDto();
        hubSpuPending.setUpdateTime(new Date());
        //设置审核状态 返回成功的则返回
        if (setAuditMsg(auditVO, hubSpuPending)) return false;
        log.info(auditVO.getSpuModel()+" >>>待复核设置审核状态OK");

        //设置查询条件
        HubSpuPendingCriteriaDto criteria = getHubSpuPendingCriteriaDto(auditVO, hubSpuPending);

        //查询出spuPending ，以便查询出skuPending
        List<HubSpuPendingDto> hubSpuPendingDtos = spuPendingGateWay.selectByCriteria(criteria);
        //加强判断  如果不是同一品类  不让通过
        if(auditVO.getAuditStatus()==SpuStatus.SPU_HANDLED.getIndex()) {
            //不通过 直接修改SPU的状态为待处理
            if (judgeCategory(auditVO, hubSpuPending, hubSpuPendingDtos)) return false;
        }
        log.info(auditVO.getSpuModel()+" >>>待复核校验品类OK");

        if(null!=hubSpuPendingDtos&&hubSpuPendingDtos.size()>0){
            if(auditVO.getAuditStatus()==SpuStatus.SPU_HANDLED.getIndex()) {
                //审核尺码  若不符合 直接修改SPU的状态为待处理
                if (auditSize(auditVO, hubSpuPending, hubSpuPendingDtos)) return false;

            }
            log.info(auditVO.getSpuModel()+" >>>待复核校验尺码OK");
            HubSpuPendingWithCriteriaDto criteriaDto = new HubSpuPendingWithCriteriaDto( hubSpuPending,  criteria);
            //更新spuPending 状态
            spuPendingGateWay.updateByCriteriaSelective(criteriaDto);

            //更新 skuPendign 状态
            setSkuPendingSkuStatus(auditVO, hubSpuPendingDtos);

            //只有审核通过的才处理
            if(SpuStatus.getOrderStatus(auditVO.getAuditStatus()).getIndex()==SpuStatus.SPU_HANDLED.getIndex()){
                SpuModelDto spuModelVO = new SpuModelDto();
                BeanUtils.copyProperties(auditVO,spuModelVO);

                //赋值spuName
                setSpuNameToSpuModelDto(spuModelVO);

                List<Long> spuPendingIdList = new ArrayList<>();
                for(HubSpuPendingDto pendingDto:hubSpuPendingDtos ){
                    spuPendingIdList.add(pendingDto.getSpuPendingId());
                }
                spuModelVO.setSpuPendingIds(spuPendingIdList);

//                CreateSpuAndSkuTask task = new CreateSpuAndSkuTask(pengdingToHubGateWay,spuModelVO,spuPendingGateWay,skuPendingGateWay);
//                executor.execute(task);
                log.info("待复核全部校验通过，调用接口=======>>>"+JsonUtil.serialize(spuModelVO));
                hubSpuPendingAuditGateWay.auditSpu(spuModelVO);

                return true;


            }else{
                return true;
            }
        }else{
            auditVO.setMemo("未发现待审核的数据，或者状态已被改变。");
            return false;
        }
    }

    @Override
    public void updatePendingSlotStateAndUser(Long spuPendingId,String operator, SpuPendingStudioState slotStudioState) throws Exception {
        HubSpuPendingDto spuPendingDto = new HubSpuPendingDto();
        spuPendingDto.setSpuPendingId(spuPendingId);
        spuPendingDto.setSlotState(slotStudioState.getIndex().byteValue());
        spuPendingDto.setSlotHandleDate(new Date());
        spuPendingDto.setSlotHandleUser(operator);
        int i = spuPendingGateWay.updateByPrimaryKeySelective(spuPendingDto);
    }

    @Override
    public boolean isCanUpdateSpuMode(Long spuPendingId) {
        HubSpuPendingDto tmp = this.getSpuPendingByKey(spuPendingId);
        if(null==tmp){
             return true;
        }else{
            if(tmp.getSlotState().intValue()==SpuPendingStudioState.SEND.getIndex()){
                //已发货，不允许修改
                return  false;
            }
        }
        return  true;
    }

    @Override
    public HubSpuPendingDto getSpuPendingByKey(Long spuPendingId) {
        return  spuPendingGateWay.selectByPrimaryKey(spuPendingId);
    }

    private boolean judgeCategory(SpuPendingAuditVO auditVO, HubSpuPendingDto hubSpuPending, List<HubSpuPendingDto> hubSpuPendingDtos) {
        Map<String,String> categoryMap = new HashMap<>();
        if(null!=hubSpuPendingDtos&&hubSpuPendingDtos.size()>0){
            for(HubSpuPendingDto spuPendingDto:hubSpuPendingDtos){
                categoryMap.put(spuPendingDto.getHubCategoryNo(),"");
            }
            if(categoryMap.size()>1){
                String errMsg = "同一品牌同一货号，品类必须唯一";
                hubSpuPending.setMemo(errMsg);
                hubSpuPending.setSpuState(SpuStatus.SPU_WAIT_HANDLE.getIndex().byteValue());
                //设置审核状态为不同意
                setAuditState(hubSpuPending, AuditState.DISAGREE,auditVO.getAuditUser(),errMsg);
                updateSpuPendingState(auditVO, hubSpuPending);
                auditVO.setMemo(errMsg);
                return true;
            }
        }
        return false;
    }

    private boolean auditSize(SpuPendingAuditVO auditVO, HubSpuPendingDto hubSpuPending, List<HubSpuPendingDto> hubSpuPendingDtos) {
        //判断skupending 尺码是否符合标准
        if(!hasSkuSize(hubSpuPendingDtos)){
            hubSpuPending.setSpuState(SpuStatus.SPU_WAIT_HANDLE.getIndex().byteValue());
            hubSpuPending.setMemo("数据错误，SPU下没有SKU，请与技术部沟通。对此引起的不便，报以歉意。");
            updateSpuPendingState(auditVO, hubSpuPending);
            auditVO.setMemo("数据错误，SPU下没有SKU，请与技术部沟通。对此引起的不便，报以歉意。");
            return true;
        }
        if (hasNohandleSkuSize(hubSpuPendingDtos)) {
            hubSpuPending.setSpuState(SpuStatus.SPU_WAIT_HANDLE.getIndex().byteValue());
            hubSpuPending.setMemo("同品牌同货号的产品，尺码有未匹配的,整体不能审核通过");
            updateSpuPendingState(auditVO, hubSpuPending);
            auditVO.setMemo("尺码有未匹配的,不能审核通过");
            return true;
        }
        Map<String,String> sizeType = new HashMap<>();
        if(!hasNeedHandingSkuSize(hubSpuPendingDtos,sizeType)){
            hubSpuPending.setSpuState(SpuStatus.SPU_WAIT_HANDLE.getIndex().byteValue());
            hubSpuPending.setMemo("SPU下没有待处理的SKU,请重新处理尺码");
            updateSpuPendingState(auditVO, hubSpuPending);
            auditVO.setMemo("SPU下没有待处理的SKU,请重新处理尺码");
            return true;
        }else{
            //判断尺码类型是否唯一  含有尺寸的 就不能有尺码
            if(sizeType.size()>1){
                if(sizeType.containsKey(GlobalConstant.REDIS_HUB_MEASURE_SIGN_KEY)){
                    hubSpuPending.setSpuState(SpuStatus.SPU_WAIT_HANDLE.getIndex().byteValue());
                    hubSpuPending.setMemo("SKU尺码类型包含尺寸以及非尺寸,请重新处理");
                    updateSpuPendingState(auditVO, hubSpuPending);
                    auditVO.setMemo("SKU尺码类型包含尺寸以及非尺寸,请重新处理");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 设置SPU状态  不通过的返回true
     * @param auditVO
     * @param hubSpuPending
     * @return
     */
    private boolean setAuditMsg(SpuPendingAuditVO auditVO, HubSpuPendingDto hubSpuPending) {
        if(auditVO.getAuditStatus()== SpuStatus.SPU_HANDLED.getIndex()){ //审核成功的 赋值为审核中
            //状态检查
            String judgeResult = judgeStatue(auditVO);
            if( StringUtils.isNotBlank(judgeResult)){
                //如果不符合 直接返回
                hubSpuPending.setMemo(judgeResult);
                hubSpuPending.setSpuState(SpuStatus.SPU_WAIT_HANDLE.getIndex().byteValue());
                //设置审核状态为不同意
                setAuditState(hubSpuPending, AuditState.DISAGREE,auditVO.getAuditUser(),judgeResult);
                updateSpuPendingState(auditVO, hubSpuPending);
                auditVO.setMemo(judgeResult);
                return true;

            }else{
            	//设置审核状态为同意
            	setAuditState(hubSpuPending,AuditState.AGREE,auditVO.getAuditUser(),"");
                hubSpuPending.setSpuState(SpuStatus.SPU_HANDLING.getIndex().byteValue());
                hubSpuPending.setMemo("");
            }

        }else{
        	//设置审核状态为不同意
            setAuditState(hubSpuPending,AuditState.DISAGREE,auditVO.getAuditUser(),auditVO.getMemo());
            hubSpuPending.setSpuState(auditVO.getAuditStatus().byteValue());
            hubSpuPending.setMemo(auditVO.getMemo());
        }
        return false;
    }

    /**
     * 设置审核状态
     * @param hubSpuPending 被赋值的对象
     * @param auditState 审核状态
     * @param auditUser 审核人
     * @param auditOpinion 审核意见
     */
    private void setAuditState(HubSpuPendingDto hubSpuPending,AuditState auditState,String auditUser,String auditOpinion){
    	hubSpuPending.setAuditState(auditState.getIndex());
    	hubSpuPending.setAuditDate(new Date());
    	hubSpuPending.setAuditUser(auditUser);
    	hubSpuPending.setAuditOpinion(auditOpinion); 
    }

    private void setSpuNameToSpuModelDto(SpuModelDto spuModelVO) {
        FourLevelCategory gmsCateGory = categoryService.getGmsCateGory(spuModelVO.getCategoryNo());
        if(null!=gmsCateGory){
            spuModelVO.setSpuName(categoryService.getHubCategoryNameByHubCategory(spuModelVO.getCategoryNo(), gmsCateGory));
        }


    }

    private void updateSpuPendingState(SpuPendingAuditVO auditVO, HubSpuPendingDto hubSpuPending) {
        HubSpuPendingCriteriaDto errCriteria = getHubSpuPendingCriteriaDto(auditVO, hubSpuPending);
        HubSpuPendingWithCriteriaDto criteriaDto = new HubSpuPendingWithCriteriaDto( hubSpuPending,  errCriteria);
        //更新spuPending 状态
        spuPendingGateWay.updateByCriteriaSelective(criteriaDto);
    }

    public String  judgeStatue(SpuPendingAuditVO auditVO){
        String result = "";
        List<SpuPendingPicVO> picVOs = auditVO.getPicVOs();
        if(null==picVOs||picVOs.size()==0){
            result = "图片不能为空 ";
        }
        if(StringUtils.isBlank(auditVO.getMaterial())){
            result = result + "材质不能为空 ";
        }else {
            result=RegexUtil.specialCategoryMatch(auditVO.getCategoryNo(),auditVO.getMaterial());
            if(null==result){
                result = "";
            }

        }
        if(StringUtils.isBlank(auditVO.getOrigin())){
            result = result + "产地不能为空";
        }
        return result;


    }

    private HubSpuPendingCriteriaDto getHubSpuPendingCriteriaDto(SpuPendingAuditVO auditVO, HubSpuPendingDto hubSpuPending) {
        HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
        HubSpuPendingCriteriaDto.Criteria criterion = criteria.createCriteria();
        if(auditVO.isMulti()){//批量的更新
            if(StringUtils.isNotBlank(auditVO.getBrandNo())){
                criterion.andHubBrandNoEqualTo(auditVO.getBrandNo());
            }
            if(StringUtils.isNotBlank(auditVO.getCategoryNo())){
                criterion.andHubCategoryNoEqualTo(auditVO.getCategoryNo());
            }
            if(StringUtils.isNotBlank(auditVO.getStartDate())){
                criterion.andUpdateTimeGreaterThanOrEqualTo(DateTimeUtil.getDateTimeFormate(auditVO.getStartDate()));
            }
            if(StringUtils.isNotBlank(auditVO.getEndDate())){
                criterion.andUpdateTimeLessThanOrEqualTo(DateTimeUtil.getDateTimeFormate(auditVO.getEndDate()));
            }
            if(StringUtils.isNotBlank(auditVO.getSpuModel())){
                criterion.andSpuModelEqualTo(auditVO.getSpuModel());
            }
            criterion.andSpuStateEqualTo(SpuStatus.SPU_WAIT_AUDIT.getIndex().byteValue());
        }else{
            if(StringUtils.isNotBlank(auditVO.getSpuModel())){//单个货号更新
                criterion.andHubBrandNoEqualTo(auditVO.getBrandNo()).andSpuModelEqualTo(auditVO.getSpuModel()).andSpuStateEqualTo(SpuStatus.SPU_WAIT_AUDIT.getIndex().byteValue());

//                if(StringUtils.isNotBlank(auditVO.getCategoryNo())){
//                    hubSpuPending.setHubCategoryNo(auditVO.getCategoryNo());
//                }
//
//                if(StringUtils.isNotBlank(auditVO.getColor())){
//
//                    hubSpuPending.setHubColor(auditVO.getColor());
//                }
//                if(StringUtils.isNotBlank(auditVO.getMaterial())){
//
//                    hubSpuPending.setHubMaterial(auditVO.getMaterial());
//                }
//                if(StringUtils.isNotBlank(auditVO.getOrigin())){
//
//                    hubSpuPending.setHubOrigin(auditVO.getOrigin());
//                }
            }
        }
        return criteria;
    }

    private void setSkuPendingSkuStatus(SpuPendingAuditVO auditVO, List<HubSpuPendingDto> hubSpuPendingDtos) {
        List<Long> spuIdList = new ArrayList<>();
        for(HubSpuPendingDto spuDto:hubSpuPendingDtos){
            spuIdList.add(spuDto.getSpuPendingId());
        }
        HubSkuPendingDto hubSkuPending = new HubSkuPendingDto();
        if(auditVO.getAuditStatus()== SpuStatus.SPU_HANDLED.getIndex()){ //审核成功的 赋值为审核中  状态借用SPU的状态

            hubSkuPending.setSkuState(SpuStatus.SPU_HANDLING.getIndex().byteValue());
        }else{
            hubSkuPending.setSkuState(auditVO.getAuditStatus().byteValue());
        }

        HubSkuPendingCriteriaDto criteriaSku = new HubSkuPendingCriteriaDto();
        criteriaSku.createCriteria().andSpuPendingIdIn(spuIdList).andSkuStateEqualTo(SpuStatus.SPU_WAIT_AUDIT.getIndex().byteValue());
        HubSkuPendingWithCriteriaDto criteriaSkuDto = new HubSkuPendingWithCriteriaDto(hubSkuPending,criteriaSku);
        skuPendingGateWay.updateByCriteriaSelective(criteriaSkuDto);
    }

    private boolean hasNohandleSkuSize(List<HubSpuPendingDto> hubSpuPendingDtos) {
        List<Long> spuIdList = new ArrayList<>();
        for(HubSpuPendingDto spuDto:hubSpuPendingDtos){
            spuIdList.add(spuDto.getSpuPendingId());
        }

        HubSkuPendingCriteriaDto criteriaSku = new HubSkuPendingCriteriaDto();
        criteriaSku.createCriteria().andSpuPendingIdIn(spuIdList)
                .andSkuStateNotEqualTo(SpuStatus.SPU_HANDLED.getIndex().byteValue())
        .andSpSkuSizeStateEqualTo(CommonHandleState.UNHANDLED.getIndex().byteValue())
        .andFilterFlagEqualTo(FilterFlag.EFFECTIVE.getIndex());

        List<HubSkuPendingDto> hubSkuPendingDtos = skuPendingGateWay.selectByCriteria(criteriaSku);
        if(null!=hubSkuPendingDtos&&hubSkuPendingDtos.size()>0){
            return true;
        }else{
            return false;
        }
    }

    private boolean hasNeedHandingSkuSize(List<HubSpuPendingDto> hubSpuPendingDtos,Map<String,String> sizeType) {
        List<Long> spuIdList = new ArrayList<>();
        for(HubSpuPendingDto spuDto:hubSpuPendingDtos){
            spuIdList.add(spuDto.getSpuPendingId());
        }

        HubSkuPendingCriteriaDto criteriaSku = new HubSkuPendingCriteriaDto();
        criteriaSku.createCriteria().andSpuPendingIdIn(spuIdList)

//                .andSkuStateEqualTo(SpuStatus.SPU_HANDLING.getIndex().byteValue())   //spu 和 sku 状态保持一致
                .andSpSkuSizeStateEqualTo(CommonHandleState.HANDLED.getIndex().byteValue())       //尺码已映射
                .andFilterFlagEqualTo(FilterFlag.EFFECTIVE.getIndex());  //不过滤的才使用

        List<HubSkuPendingDto> hubSkuPendingDtos = skuPendingGateWay.selectByCriteria(criteriaSku);

        if(null!=hubSkuPendingDtos&&hubSkuPendingDtos.size()>0){
            for(HubSkuPendingDto hubSkuPendingDto:hubSkuPendingDtos){
                sizeType.put(hubSkuPendingDto.getHubSkuSizeType(),"");
            }
            return true;
        }else{
            return false;
        }
    }

    private boolean hasSkuSize(List<HubSpuPendingDto> hubSpuPendingDtos) {
        List<Long> spuIdList = new ArrayList<>();
        for(HubSpuPendingDto spuDto:hubSpuPendingDtos){
            spuIdList.add(spuDto.getSpuPendingId());
        }
        HubSkuPendingCriteriaDto criteriaSku = new HubSkuPendingCriteriaDto();
        criteriaSku.createCriteria().andSpuPendingIdIn(spuIdList);
        int i =  skuPendingGateWay.countByCriteria(criteriaSku);
        if(i>0){
            return true;
        }else{
            return false;
        }
    }


    @SuppressWarnings("unused")
	private boolean hasSkuStock(List<HubSpuPendingDto> hubSpuPendingDtos) {
        List<Long> spuIdList = new ArrayList<>();
        for(HubSpuPendingDto spuDto:hubSpuPendingDtos){
            spuIdList.add(spuDto.getSpuPendingId());
        }
        HubSkuPendingCriteriaDto criteriaSku = new HubSkuPendingCriteriaDto();
        criteriaSku.createCriteria().andSpuPendingIdIn(spuIdList).andStockGreaterThan(0);
        int i =  skuPendingGateWay.countByCriteria(criteriaSku);
        if(i>0){
            return true;
        }else{
            return false;
        }
    }









}

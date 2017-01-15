package com.shangpin.ephub.product.business.service.pending.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.data.mysql.product.dto.SpuModelDto;
import com.shangpin.ephub.client.data.mysql.product.gateway.PengdingToHubGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.util.RegexUtil;
import com.shangpin.ephub.product.business.common.enumeration.SpuStatus;
import com.shangpin.ephub.product.business.common.util.DateTimeUtil;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuModelMsgVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuModelVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuPendingAuditQueryVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuPendingAuditVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuPendingPicVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuPendingVO;

/**
 * Created by loyalty on 16/12/24.
 * @param
 */
@Service
public class PendingServiceImpl implements com.shangpin.ephub.product.business.service.pending.PendingService {

    @Autowired
    HubSpuPendingGateWay spuPendingGateWay;

    @Autowired
    HubSkuPendingGateWay skuPendingGateWay;

    @Autowired
    HubSpuPendingPicGateWay spuPendingPicGateWay;


    @Autowired
    PengdingToHubGateWay pengdingToHubGateWay;

    @Autowired
    private TaskExecutor executor;

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
            criterion.andSpuModelLike(queryVO.getSpuModel());

        }
        if(StringUtils.isNotBlank(queryVO.getBrandNo())){
            criterion.andHubBrandNoEqualTo(queryVO.getBrandNo());
        }
        if(StringUtils.isNotBlank(queryVO.getCategoryNo())){
            criterion.andHubCategoryNoLike(queryVO.getCategoryNo());
        }
        if(StringUtils.isNotBlank(queryVO.getStartDate())){
            criterion.andUpdateTimeGreaterThanOrEqualTo(DateTimeUtil.getDateTimeFormate(queryVO.getStartDate()));
        }
        if(StringUtils.isNotBlank(queryVO.getEndDate())){
            criterion.andUpdateTimeLessThanOrEqualTo(DateTimeUtil.getDateTimeFormate(queryVO.getEndDate()));
        }
        if(null==queryVO.getStatus()){
            criterion.andSpuStateEqualTo(SpuStatus.SPU_WAIT_AUDIT.getIndex().byteValue());
        }else{
            criterion.andSpuStateEqualTo(queryVO.getStatus().byteValue());
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
                if(null!=spuPendingDto.getUpdateTime()){
                    spuPendingVO.setUpdateTime(DateTimeUtil.convertFormat(spuPendingDto.getUpdateTime(),"yyyy-MM-dd HH:mm:ss"));
                }

                //获取图片
                HubSpuPendingPicCriteriaDto criteriaPic = new HubSpuPendingPicCriteriaDto();
                criteriaPic.setPageNo(1);
                criteriaPic.setPageSize(100);
                criteriaPic.createCriteria().andSupplierIdEqualTo(spuPendingDto.getSupplierId())
                        .andSupplierSpuNoEqualTo(spuPendingDto.getSupplierSpuNo())
                        .andPicHandleStateEqualTo(PicState.HANDLED.getIndex());

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

                spuPendingVOS.add(spuPendingVO);
            }

            spuPendingVOS.get(index).setIsDefaultSupplier(true);
        }

        return spuPendingVOS;
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
        //更新状态
        HubSpuPendingDto hubSpuPending = new HubSpuPendingDto();




        if(auditVO.getAuditStatus()==SpuStatus.SPU_HANDLED.getIndex()){ //审核成功的 赋值为审核中
            //状态检查
            String judgeResult = judgeStatue(auditVO);
            if( StringUtils.isNotBlank(judgeResult)){
                hubSpuPending.setMemo(judgeResult);
                auditVO.setAuditStatus(SpuStatus.SPU_WAIT_HANDLE.getIndex());
            }else{

                hubSpuPending.setSpuState(SpuStatus.SPU_HANDLING.getIndex().byteValue());
                hubSpuPending.setMemo("");
            }

        }else{
            hubSpuPending.setSpuState(auditVO.getAuditStatus().byteValue());
            hubSpuPending.setMemo(auditVO.getMemo());
        }

        hubSpuPending.setUpdateTime(new Date());
        //设置查询条件
        HubSpuPendingCriteriaDto criteria = getHubSpuPendingCriteriaDto(auditVO, hubSpuPending);

        //查询出spuPending ，以便查询出skuPending
        List<HubSpuPendingDto> hubSpuPendingDtos = spuPendingGateWay.selectByCriteria(criteria);


        HubSpuPendingWithCriteriaDto criteriaDto = new HubSpuPendingWithCriteriaDto( hubSpuPending,  criteria);
        //更新spuPending 状态
        spuPendingGateWay.updateByCriteriaSelective(criteriaDto);
        //更新 skuPendign 状态
        setSkuPendingSkuStatus(auditVO, hubSpuPendingDtos);

          //只有审核通过的才处理
         if(SpuStatus.getOrderStatus(auditVO.getAuditStatus()).getIndex()==SpuStatus.SPU_HANDLED.getIndex()){
             //获取
             SpuPendingAuditQueryVO queryVO = new SpuPendingAuditQueryVO();
             BeanUtils.copyProperties(auditVO,queryVO);
             //查询审核中的
             queryVO.setStatus(SpuStatus.SPU_HANDLING.getIndex());
             SpuModelMsgVO spuModelMsgVO=this.getSpuModel(queryVO);

             List<SpuModelVO> spuModels = spuModelMsgVO.getSpuModels();
             for(SpuModelVO spuModelVO:spuModels){
                 //TODO 扔进消息队列中
                 CreateSpuAndSkuTask task = new CreateSpuAndSkuTask(pengdingToHubGateWay,spuModelVO,spuPendingGateWay,skuPendingGateWay);
                 executor.execute(task);
             }
         }




        return true;
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
            if(RegexUtil.isLetter(auditVO.getMaterial())){
                result = result + "材质中包含不能包含英文 ";
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
                criterion.andSpuModelEqualTo(auditVO.getSpuModel()).andSpuStateEqualTo(SpuStatus.SPU_WAIT_AUDIT.getIndex().byteValue());
                if(StringUtils.isNotBlank(auditVO.getCategoryNo())){
                    hubSpuPending.setHubCategoryNo(auditVO.getCategoryNo());
                }

                if(StringUtils.isNotBlank(auditVO.getColor())){

                    hubSpuPending.setHubColor(auditVO.getColor());
                }
                if(StringUtils.isNotBlank(auditVO.getMaterial())){

                    hubSpuPending.setHubMaterial(auditVO.getMaterial());
                }
                if(StringUtils.isNotBlank(auditVO.getOrigin())){

                    hubSpuPending.setHubOrigin(auditVO.getOrigin());
                }
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
}

class CreateSpuAndSkuTask implements Runnable{

    PengdingToHubGateWay pengdingToHubGateWay;
    SpuModelVO spuModelVO;
    HubSpuPendingGateWay spuPendingGateWay;
    HubSkuPendingGateWay skuPendingGateWay;

    CreateSpuAndSkuTask(PengdingToHubGateWay pengdingToHubGateWay,SpuModelVO spuModelVO, HubSpuPendingGateWay spuPendingGateWay, HubSkuPendingGateWay skuPendingGateWay){
        this.pengdingToHubGateWay = pengdingToHubGateWay;
        this.spuModelVO = spuModelVO;
        this.spuPendingGateWay = spuPendingGateWay;
        this.skuPendingGateWay = skuPendingGateWay;
    }

    @Override
    public void run() {
        SpuModelDto dto  =new SpuModelDto();
        BeanUtils.copyProperties(spuModelVO,dto);
        boolean result= pengdingToHubGateWay.auditPending(dto);
        if(result){
             this.updateHubSpuState(dto,SpuStatus.SPU_HANDLED.getIndex().byteValue());
        }else{
            this.updateHubSpuState(dto,SpuStatus.SPU_WAIT_AUDIT.getIndex().byteValue());
        }
    }

    private void updateHubSpuState(SpuModelDto spuModelDto,byte spuStatus){
        Date date = new Date() ;
        //获取所有的审核中的spupending数据
        HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
        criteria.createCriteria().andSpuStateEqualTo(SpuStatus.SPU_HANDLING.getIndex().byteValue())
        .andHubBrandNoEqualTo(spuModelDto.getBrandNo()).andSpuModelEqualTo(spuModelDto.getSpuModel());

        HubSpuPendingDto hubSpuPending = new HubSpuPendingDto();
        hubSpuPending.setSpuState(spuStatus);
        hubSpuPending.setUpdateTime(date);


        List<HubSpuPendingDto> hubSpuPendingDtos = spuPendingGateWay.selectByCriteria(criteria);

        HubSpuPendingWithCriteriaDto criteriaDto = new HubSpuPendingWithCriteriaDto( hubSpuPending,  criteria);

        spuPendingGateWay.updateByCriteriaSelective(criteriaDto);

        //操作所有的审核中的skupending数据

        List<Long> spuIdList = new ArrayList<>();
        for(HubSpuPendingDto spuDto:hubSpuPendingDtos){
            spuIdList.add(spuDto.getSpuPendingId());
        }
        HubSkuPendingDto hubSkuPending = new HubSkuPendingDto();

        hubSkuPending.setSkuState(spuStatus);
        hubSkuPending.setUpdateTime(date);

        HubSkuPendingCriteriaDto criteriaSku = new HubSkuPendingCriteriaDto();
        criteriaSku.createCriteria().andSpuPendingIdIn(spuIdList).andSkuStateEqualTo(SpuStatus.SPU_HANDLING.getIndex().byteValue());

        HubSkuPendingWithCriteriaDto criteriaSkuDto = new HubSkuPendingWithCriteriaDto(hubSkuPending,criteriaSku);
        skuPendingGateWay.updateByCriteriaSelective(criteriaSkuDto);


    }
}

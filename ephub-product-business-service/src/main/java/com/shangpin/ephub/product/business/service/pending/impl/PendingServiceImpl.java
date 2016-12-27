package com.shangpin.ephub.product.business.service.pending.impl;

import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.product.business.common.SpuStatus;
import com.shangpin.ephub.product.business.common.util.DateTimeUtil;
import com.shangpin.ephub.product.business.ui.pendingCrud.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by loyalty on 16/12/24.
 */
@Service
@Slf4j
public class PendingServiceImpl implements com.shangpin.ephub.product.business.service.pending.PendingService {

    @Autowired
    HubSpuPendingGateWay spuPendingGateWay;

    @Autowired
    HubSpuPendingPicGateWay spuPendingPicGateWay;


    public List<SpuPendingCommonVO> getSpuPending(SpuPendingAuditQueryVO queryVO){


        return  null;

    }

    @Override
    public SpuModelMsgVO getSpuModel(SpuPendingAuditQueryVO queryVO) {

        HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
        HubSpuPendingCriteriaDto.Criteria criterion = criteria.createCriteria();
        criteria.setPageSize(queryVO.getPageSize());
        criteria.setPageNo(queryVO.getPage());
        criteria.setDistinct(true);
        criteria.setFields(" spu_model,hub_brand_no  ");
        if(StringUtils.isNotBlank(queryVO.getSpuModel())){
            criterion.andSpuModelLike(queryVO.getSpuModel());

        }
        if(StringUtils.isNotBlank(queryVO.getBrandNo())){
            criterion.andHubBrandNoEqualTo(queryVO.getBrandNo());
        }
        if(StringUtils.isNotBlank(queryVO.getCategroyNo())){
            criterion.andHubCategoryNoEqualTo(queryVO.getCategroyNo());
        }
        if(StringUtils.isNotBlank(queryVO.getStartDate())){
            criterion.andUpdateTimeGreaterThanOrEqualTo(DateTimeUtil.getDateTimeFormate(queryVO.getStartDate()));
        }
        if(StringUtils.isNotBlank(queryVO.getEndDate())){
            criterion.andUpdateTimeLessThanOrEqualTo(DateTimeUtil.getDateTimeFormate(queryVO.getEndDate()));
        }

        criterion.andSpuStateEqualTo(SpuStatus.SPU_WAIT_AUDIT.getIndex().byteValue());


        Integer count = spuPendingGateWay.countByCriteria(criteria);
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
                //获取图片
                HubSpuPendingPicCriteriaDto criteriaPic = new HubSpuPendingPicCriteriaDto();

                criteriaPic.createCriteria().andSpuPendingIdEqualTo(spuPendingDto.getSpuPendingId());

                List<HubSpuPendingPicDto> hubSpuPendingPicDtos = spuPendingPicGateWay.selectByCriteria(criteriaPic);
                if(null!=hubSpuPendingDtos){
                    if(hubSpuPendingDtos.size()>maxPic){
                        maxPic = hubSpuPendingPicDtos.size();
                        index = i;//获取拥有图片数量最多的spupending
                    }
                    List<SpuPendingPicVO>  picVOs = new ArrayList<>();

                    for(HubSpuPendingPicDto picDto:hubSpuPendingPicDtos){
                        SpuPendingPicVO picVO = new SpuPendingPicVO();
                        picVO.setSpuPendingId(picDto.getSpuPendingId());
                        picVO.setPicId(picDto.getPicId());
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
     */
    public boolean audit(SpuPendingAuditVO auditVO) throws Exception {


        return true;
    }
}

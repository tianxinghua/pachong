package com.shangpin.ephub.data.mysql.service.impl;

import com.shangpin.ephub.data.mysql.service.PengingToHubService;
import com.shangpin.ephub.data.mysql.service.common.DataCommonStatus;
import com.shangpin.ephub.data.mysql.service.common.HubSpuStatus;
import com.shangpin.ephub.data.mysql.service.vo.SpuPendingAuditVO;
import com.shangpin.ephub.data.mysql.service.vo.SpuPendingPicVO;
import com.shangpin.ephub.data.mysql.sku.pending.mapper.HubSkuPendingMapper;
import com.shangpin.ephub.data.mysql.sku.pending.po.HubSkuPending;
import com.shangpin.ephub.data.mysql.sku.pending.po.HubSkuPendingCriteria;
import com.shangpin.ephub.data.mysql.spu.hub.mapper.HubSpuMapper;
import com.shangpin.ephub.data.mysql.spu.hub.po.HubSpu;
import com.shangpin.ephub.data.mysql.spu.hub.po.HubSpuCriteria;
import com.shangpin.ephub.data.mysql.spu.pending.mapper.HubSpuPendingMapper;
import com.shangpin.ephub.data.mysql.spu.pending.po.HubSpuPending;
import com.shangpin.ephub.data.mysql.spu.pending.po.HubSpuPendingCriteria;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by loyalty on 16/12/27.
 */
@Service
public class PengdingToHubServiceImpl implements PengingToHubService {

    @Autowired
    private HubSpuPendingMapper hubSpuPendingMapper;

    @Autowired
    private HubSkuPendingMapper hubSkuPendingMapper;

    @Autowired
    private HubSpuMapper hubSpuMapper;


    public PengdingToHubServiceImpl() {
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditPending(SpuPendingAuditVO auditVO) throws Exception {
        //第一步 更改状态和部分数据
        List<Long> spuPendingIds = updatePengdingStatus(auditVO);

        //第二步  插入新的hub SPU 记录 以及SKU 记录

        //查询是否存在
        HubSpuCriteria criteria = new HubSpuCriteria();
        criteria.createCriteria().andSpuModelEqualTo(auditVO.getSpuModel())
                .andBrandNoEqualTo(auditVO.getBrandNo());
        List<HubSpu> hubSpus = hubSpuMapper.selectByExample(criteria);
        if(null==hubSpus||(null!=hubSpus&&hubSpus.size()==0)){//不存在插入新的SPU记录 以及 SKU 记录
            //合并sku pending的尺码 生成HUBSKU
            Map<String,List<HubSkuPending>> sizeSkuMap = new HashMap<>();
            for(Long spuPendingId:spuPendingIds){
                HubSkuPendingCriteria skuPendingCriteria = new HubSkuPendingCriteria();
                skuPendingCriteria.createCriteria().andSpuPendingIdEqualTo(spuPendingId);
                List<HubSkuPending> hubSkuPendings = hubSkuPendingMapper.selectByExample(skuPendingCriteria);
                for(HubSkuPending hubSkuPending:hubSkuPendings){
                    if(hubSkuPending.getSkuState().intValue()== DataCommonStatus.HANDLED.getIndex()){//信息已完善
                        if(sizeSkuMap.containsKey(hubSkuPending.getScreenSize())){
                            sizeSkuMap.get(hubSkuPending.getScreenSize()).add(hubSkuPending);
                        }else{
                            List<HubSkuPending> hubSkuPendingList = new ArrayList<>();
                            hubSkuPendingList.add(hubSkuPending);
                            sizeSkuMap.put(hubSkuPending.getScreenSize(),hubSkuPendingList);
                        }
                    }
                }
            }

            //插入新的SPU
            HubSpu hubSpu = new HubSpu();
            HubSpuPending spuPending = null;
            if(null!=auditVO.getSpuPendingId()){
                 spuPending = this.getHubSpuPendingById(auditVO.getSpuPendingId());

            }else{
                spuPending = this.getHubSpuPendingById(spuPendingIds.get(0));
            }
            if(null!=spuPending){
                BeanUtils.copyProperties(spuPending,hubSpu);
                Date date  = new Date();
                hubSpu.setCreateTime(date);
                hubSpu.setUpdateTime(date);
                hubSpu.setCategoryNo(spuPending.getHubCategoryNo());
                hubSpu.setBrandNo(spuPending.getHubBrandNo());
                String pendingSeason = spuPending.getHubSeason();
                if(StringUtils.isNotBlank(pendingSeason)&&pendingSeason.indexOf("_")>0){
                    hubSpu.setMarketTime(pendingSeason.substring(0,pendingSeason.indexOf("_")));
                    hubSpu.setSeason(pendingSeason.substring(pendingSeason.indexOf("_")+1,pendingSeason.length());
                }
                hubSpu.setHubColor(spuPending.getHubColor());
                hubSpu.setHubColorNo(spuPending.getHubColorNo());
                hubSpu.setSpuState(DataCommonStatus.HANDLED.getIndex().byteValue());
                hubSpu.setSpuSelectState(DataCommonStatus.WAIT_HANDLE.getIndex().byteValue());


                hubSpuMapper.insert(hubSpu);
            }


        }
        List<SpuPendingPicVO> picVOs = auditVO.getPicVOs();
        for(SpuPendingPicVO picVO:picVOs){

        }
        return false;
    }

    private List<Long> updatePengdingStatus(SpuPendingAuditVO auditVO) {

        //获取spuPendingID 列表 以便后续得到SKUPENDING

        HubSpuPendingCriteria criteriaForId = new HubSpuPendingCriteria();
        criteriaForId.setFields("spu_pending_id");
        HubSpuPendingCriteria.Criteria criterionForId = criteriaForId.createCriteria();
        criterionForId.andSpuModelEqualTo(auditVO.getSpuModel()).andHubBrandNoEqualTo(auditVO.getBrandNo())
                .andSpuStateEqualTo(HubSpuStatus.WAIT_AUDIT.getIndex().byteValue());

        List<HubSpuPending> hubSpuPendingIds = hubSpuPendingMapper.selectByExample(criteriaForId);
        List<Long> pendIdList = new ArrayList<>();
        for(HubSpuPending spuPending:hubSpuPendingIds){
            pendIdList.add(spuPending.getSpuPendingId());
        }
        //处理spuPending 数据
        HubSpuPendingCriteria criteria = new HubSpuPendingCriteria();
        HubSpuPendingCriteria.Criteria criterion = criteria.createCriteria();
        criterion.andSpuModelEqualTo(auditVO.getSpuModel()).andHubBrandNoEqualTo(auditVO.getBrandNo())
                .andSpuStateEqualTo(HubSpuStatus.WAIT_AUDIT.getIndex().byteValue());

        HubSpuPending hubSpuPending = new HubSpuPending();
        hubSpuPending.setSpuState(HubSpuStatus.HANDLED.getIndex().byteValue());
        hubSpuPending.setHubColor(auditVO.getColor());
        hubSpuPending.setHubMaterial(auditVO.getMaterial());
        hubSpuPending.setHubOrigin(auditVO.getOrigin());


        hubSpuPendingMapper.updateByExampleSelective(hubSpuPending,criteria);
        return pendIdList;
    }


    private HubSpuPending getHubSpuPendingById(Long id){

        return  hubSpuPendingMapper.selectByPrimaryKey(id);

    }
}

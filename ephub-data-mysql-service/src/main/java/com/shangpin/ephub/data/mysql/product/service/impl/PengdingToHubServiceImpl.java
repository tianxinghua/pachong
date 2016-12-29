package com.shangpin.ephub.data.mysql.product.service.impl;

import com.shangpin.ephub.data.mysql.mapping.sku.mapper.HubSkuSupplierMappingMapper;
import com.shangpin.ephub.data.mysql.mapping.sku.po.HubSkuSupplierMapping;

import com.shangpin.ephub.data.mysql.product.common.*;
import com.shangpin.ephub.data.mysql.product.dto.SpuModelDto;
import com.shangpin.ephub.data.mysql.product.service.PengingToHubService;
import com.shangpin.ephub.data.mysql.sku.hub.mapper.HubSkuMapper;
import com.shangpin.ephub.data.mysql.sku.hub.po.HubSku;
import com.shangpin.ephub.data.mysql.sku.pending.mapper.HubSkuPendingMapper;
import com.shangpin.ephub.data.mysql.sku.pending.po.HubSkuPending;
import com.shangpin.ephub.data.mysql.sku.pending.po.HubSkuPendingCriteria;
import com.shangpin.ephub.data.mysql.sku.supplier.mapper.HubSupplierSkuMapper;
import com.shangpin.ephub.data.mysql.sku.supplier.po.HubSupplierSku;
import com.shangpin.ephub.data.mysql.sku.supplier.po.HubSupplierSkuCriteria;
import com.shangpin.ephub.data.mysql.spu.hub.mapper.HubSpuMapper;
import com.shangpin.ephub.data.mysql.spu.hub.po.HubSpu;
import com.shangpin.ephub.data.mysql.spu.hub.po.HubSpuCriteria;
import com.shangpin.ephub.data.mysql.spu.pending.mapper.HubSpuPendingMapper;
import com.shangpin.ephub.data.mysql.spu.pending.po.HubSpuPending;
import com.shangpin.ephub.data.mysql.spu.pending.po.HubSpuPendingCriteria;
import com.shangpin.ephub.data.mysql.spu.supplier.mapper.HubSupplierSpuMapper;
import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpu;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class PengdingToHubServiceImpl implements PengingToHubService {

    @Autowired
    private HubSpuPendingMapper hubSpuPendingMapper;

    @Autowired
    private HubSkuPendingMapper hubSkuPendingMapper;

    @Autowired
    private HubSpuMapper hubSpuMapper;

    @Autowired
    private HubSkuMapper hubSkuMapper;

    @Autowired
    private HubSkuSupplierMappingMapper hubSkuSupplierMappingMapper;

    @Autowired
    private HubSupplierSkuMapper hubSupplierSkuMapper;

    @Autowired
    private HubSupplierSpuMapper hubSupplierSpuMapper;

    @Autowired
    private HubSpuUtil hubSpuUtil;


    public PengdingToHubServiceImpl() {
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditPending(SpuModelDto auditVO) throws Exception {
        //第一步 根据货号和品牌 获取相同的信息下的hubSpu
        List<Long> spuPendingIds = getSpuPendIdList(auditVO);

        //第二步  插入新的hub SPU 记录 以及SKU 记录
        createHubData(auditVO, spuPendingIds);


        return true;
    }

    private void createHubData(SpuModelDto auditVO, List<Long> spuPendingIds) {
        //查询是否存在
        HubSpuCriteria criteria = new HubSpuCriteria();
        criteria.createCriteria().andSpuModelEqualTo(auditVO.getSpuModel())
                .andBrandNoEqualTo(auditVO.getBrandNo());
        List<HubSpu> hubSpus = hubSpuMapper.selectByExample(criteria);
        if(null==hubSpus||(null!=hubSpus&&hubSpus.size()==0)){ //不存在插入新的SPU记录 以及 SKU 记录
            //合并sku pending的尺码 生成HUBSKU
            Map<String,List<HubSkuPending>> sizeSkuMap = new HashMap<>();
            //根据尺码合并不同供货商的SKU信息
            setSizeSkuMap(spuPendingIds, sizeSkuMap);
            //插入新的SPU
            HubSpu hubSpu = new HubSpu();
            HubSpuPending spuPending = null;
            spuPending = this.getHubSpuPendingById(spuPendingIds.get(0));
            if(null!=spuPending){
                //创建hubspu
                createHubSpu(hubSpu, spuPending);
                //插入hubSKU 和 供货商的对应关系
                Set<String> sizeSet = sizeSkuMap.keySet();
                if(sizeSet.size()>0){
                    createHubSkuAndMapping(sizeSkuMap, hubSpu, sizeSet);
                }
            }


        }else{ //TODO 已存在

        }
    }

    private void createHubSkuAndMapping(Map<String, List<HubSkuPending>> sizeSkuMap, HubSpu hubSpu, Set<String> sizeSet) {
        String skuNoAll = hubSpuUtil.createHubSkuNo(hubSpu.getSpuNo(),sizeSet.size());
        if(StringUtils.isNotBlank(skuNoAll)){
            String[] skuNoArray = skuNoAll.split(",");
            Object[] sizeArray = sizeSet.toArray();
            String size="",skuNo="";
            Date date = new Date();

            if(null!=skuNoArray&&skuNoArray.length>0){
                for(int i =0;i<skuNoArray.length;i++){
                    List<HubSkuPending> hubSkuPendings = sizeSkuMap.get(sizeArray[i]);
                    HubSku hubSku = new HubSku();

                    hubSku.setSpuNo(hubSpu.getSpuNo());
                    hubSku.setColor(hubSpu.getCategoryNo());
                    hubSku.setSkuNo(skuNoArray[i]);
                    hubSku.setSkuSize((String)sizeArray[i]);
                    hubSku.setSkuSizeId(hubSkuPendings.get(0).getScreenSize());
                    hubSku.setCreateTime(date);
                    hubSku.setCreateUser(ConstantProperty.DATA_CREATE_USER);
                    hubSku.setUpdateTime(date);
                    hubSkuMapper.insert(hubSku);
                    //增加SKU的营生关系
                    for(HubSkuPending skuPending:hubSkuPendings){

                        HubSkuSupplierMapping hubSkuSupplierMapping = new HubSkuSupplierMapping();
                        BeanUtils.copyProperties(skuPending,hubSkuSupplierMapping);
                        hubSkuSupplierMapping.setSupplierSelectState(DataSelectStatus.NOT_SELECT.getIndex().byteValue());
                        HubSpuPending spuPendingTmp =  this.getHubSpuPendingById(skuPending.getSpuPendingId());
                        hubSkuSupplierMapping.setSupplierNo(spuPendingTmp.getSupplierNo());
//                                    hubSkuSupplierMapping.setIsNewSupplier();  //不知从哪里获取值
                        hubSkuSupplierMapping.setCreateTime(date);
                        hubSkuSupplierMapping.setCreateUser(ConstantProperty.DATA_CREATE_USER);
                        hubSkuSupplierMapping.setUpdateTime(date);
                        HubSupplierSku supplierSku = this.getSupplierSku(skuPending.getSupplierId(), skuPending.getSupplierSkuNo());

                        if(null!=supplierSku){
                            HubSupplierSpu supplierSpu = this.getSupplierSpuById(supplierSku.getSupplierSpuId());
                            hubSkuSupplierMapping.setSupplierSkuId(supplierSku.getSupplierSkuId());
                            hubSkuSupplierMapping.setSupplierSpuModel(supplierSpu.getSupplierSpuModel());
                        }
                        hubSkuSupplierMapping.setDataState(DataStatus.NOT_DELETE.getIndex().byteValue());
                        hubSkuSupplierMappingMapper.insert(hubSkuSupplierMapping);
                    }

                }
            }

        }
    }

    /**
     * 根据尺码合并不同供货商的SKU信息
     * @param spuPendingIds
     * @param sizeSkuMap
     */
    private void setSizeSkuMap(List<Long> spuPendingIds, Map<String, List<HubSkuPending>> sizeSkuMap) {
        for(Long spuPendingId:spuPendingIds){
            HubSkuPendingCriteria skuPendingCriteria = new HubSkuPendingCriteria();
            skuPendingCriteria.setPageNo(1);
            skuPendingCriteria.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
            skuPendingCriteria.createCriteria().andSpuPendingIdEqualTo(spuPendingId);
            List<HubSkuPending> hubSkuPendings = hubSkuPendingMapper.selectByExample(skuPendingCriteria);
            for(HubSkuPending hubSkuPending:hubSkuPendings){
                if(null!=hubSkuPending.getSkuState()&&hubSkuPending.getSkuState().intValue()== DataBusinessStatus.HANDLED.getIndex()){//信息已完善
                    if(sizeSkuMap.containsKey(hubSkuPending.getHubSkuSize())){
                        sizeSkuMap.get(hubSkuPending.getHubSkuSize()).add(hubSkuPending);
                    }else{
                        List<HubSkuPending> hubSkuPendingList = new ArrayList<>();
                        hubSkuPendingList.add(hubSkuPending);
                        sizeSkuMap.put(hubSkuPending.getHubSkuSize(),hubSkuPendingList);
                    }
                }
            }
        }
    }


    private void createHubSpu(HubSpu hubSpu, HubSpuPending spuPending) {
        BeanUtils.copyProperties(spuPending,hubSpu);
        Date date  = new Date();
        hubSpu.setCreateTime(date);
        hubSpu.setUpdateTime(date);
        hubSpu.setCategoryNo(spuPending.getHubCategoryNo());
        hubSpu.setBrandNo(spuPending.getHubBrandNo());
        String pendingSeason = spuPending.getHubSeason();
        if(StringUtils.isNotBlank(pendingSeason)&&pendingSeason.indexOf("_")>0){
            hubSpu.setMarketTime(pendingSeason.substring(0,pendingSeason.indexOf("_")));
            hubSpu.setSeason(pendingSeason.substring(pendingSeason.indexOf("_")+1,pendingSeason.length()));
        }
        hubSpu.setHubColor(spuPending.getHubColor());
        hubSpu.setHubColorNo(spuPending.getHubColorNo());
        hubSpu.setSpuState(DataBusinessStatus.HANDLED.getIndex().byteValue());
        hubSpu.setSpuSelectState(DataBusinessStatus.WAIT_HANDLE.getIndex().byteValue());
        hubSpu.setOrigin(spuPending.getHubOrigin());
        hubSpu.setMaterial(spuPending.getHubMaterial());
        hubSpu.setDataState(DataStatus.NOT_DELETE.getIndex().byteValue());
        hubSpu.setSpuNo(hubSpuUtil.createHubSpuNo(0L));//插入SPU编号
        hubSpuMapper.insert(hubSpu);
    }

    private List<Long> getSpuPendIdList(SpuModelDto spuModelVO) {

        //获取spuPendingID 列表 以便后续得到SKUPENDING

        HubSpuPendingCriteria criteriaForId = new HubSpuPendingCriteria();
        criteriaForId.setFields("spu_pending_id");
        HubSpuPendingCriteria.Criteria criterionForId = criteriaForId.createCriteria();
        criterionForId.andSpuModelEqualTo(spuModelVO.getSpuModel()).andHubBrandNoEqualTo(spuModelVO.getBrandNo())
                .andSpuStateEqualTo(HubSpuPendigStatus.HANDLING.getIndex().byteValue());
        criteriaForId.setPageNo(1);
        criteriaForId.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
        List<HubSpuPending> hubSpuPendingIds = hubSpuPendingMapper.selectByExample(criteriaForId);
        List<Long> pendIdList = new ArrayList<>();
        for(HubSpuPending spuPending:hubSpuPendingIds){
            log.info("spuPending.getSpuPendingId()= "+ spuPending.getSpuPendingId());

            pendIdList.add(spuPending.getSpuPendingId());
        }
//        //处理spuPending 数据
//        HubSpuPendingCriteria criteria = new HubSpuPendingCriteria();
//        HubSpuPendingCriteria.Criteria criterion = criteria.createCriteria();
//        criterion.andSpuModelEqualTo(auditVO.getSpuModel()).andHubBrandNoEqualTo(auditVO.getBrandNo())
//                .andSpuStateEqualTo(HubSpuStatus.WAIT_AUDIT.getIndex().byteValue());
//
//        HubSpuPending hubSpuPending = new HubSpuPending();
//        hubSpuPending.setSpuState(HubSpuStatus.HANDLING.getIndex().byteValue());
//        if(StringUtils.isNotBlank(auditVO.getColor())){
//
//            hubSpuPending.setHubColor(auditVO.getColor());
//        }
//        if(StringUtils.isNotBlank(auditVO.getMaterial())){
//
//            hubSpuPending.setHubMaterial(auditVO.getMaterial());
//        }
//        if(StringUtils.isNotBlank(auditVO.getOrigin())){
//
//            hubSpuPending.setHubOrigin(auditVO.getOrigin());
//        }
//
//
//        hubSpuPendingMapper.updateByExampleSelective(hubSpuPending,criteria);
        return pendIdList;
    }


    private HubSpuPending getHubSpuPendingById(Long id){

        return  hubSpuPendingMapper.selectByPrimaryKey(id);

    }

    private HubSupplierSku getSupplierSku(String supplierId,String supplierSkuNo){
        HubSupplierSkuCriteria example = new HubSupplierSkuCriteria();
        example.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSkuNoEqualTo(supplierSkuNo);
        List<HubSupplierSku> hubSupplierSkus = hubSupplierSkuMapper.selectByExample(example);
        if(null!=hubSupplierSkus&&hubSupplierSkus.size()>0){
            return hubSupplierSkus.get(0);

        }else{
            return null;
        }
    }
    private HubSupplierSpu getSupplierSpuById(Long supplierSpuId) {
       return hubSupplierSpuMapper.selectByPrimaryKey(supplierSpuId);
    }

}

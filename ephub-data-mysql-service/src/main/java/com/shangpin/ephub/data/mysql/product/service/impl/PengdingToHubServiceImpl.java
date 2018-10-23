package com.shangpin.ephub.data.mysql.product.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.shangpin.ephub.data.mysql.common.PropertyConstant;
import com.shangpin.ephub.data.mysql.product.common.enumeration.SupplierValueMappingType;
import com.shangpin.ephub.data.mysql.mapping.value.mapper.HubSupplierValueMappingMapper;
import com.shangpin.ephub.data.mysql.mapping.value.po.HubSupplierValueMapping;
import com.shangpin.ephub.data.mysql.mapping.value.po.HubSupplierValueMappingCriteria;
import com.shangpin.ephub.data.mysql.sku.supplier.po.HubSupplierSku;
import com.shangpin.ephub.data.mysql.sku.supplier.po.HubSupplierSkuCriteria;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shangpin.ephub.data.mysql.common.enumeration.PicState;
import com.shangpin.ephub.data.mysql.common.enumeration.SpuState;
import com.shangpin.ephub.data.mysql.mapping.sku.mapper.HubSkuSupplierMappingMapper;
import com.shangpin.ephub.data.mysql.mapping.sku.po.HubSkuSupplierMapping;
import com.shangpin.ephub.data.mysql.mapping.sku.po.HubSkuSupplierMappingCriteria;
import com.shangpin.ephub.data.mysql.picture.pending.mapper.HubSpuPendingPicMapper;
import com.shangpin.ephub.data.mysql.picture.pending.po.HubSpuPendingPic;
import com.shangpin.ephub.data.mysql.picture.pending.po.HubSpuPendingPicCriteria;
import com.shangpin.ephub.data.mysql.picture.spu.mapper.HubSpuPicMapper;
import com.shangpin.ephub.data.mysql.picture.spu.po.HubSpuPic;
import com.shangpin.ephub.data.mysql.product.common.ConstantProperty;
import com.shangpin.ephub.data.mysql.product.common.HubSpuUtil;
import com.shangpin.ephub.data.mysql.product.common.enumeration.DataBusinessStatus;
import com.shangpin.ephub.data.mysql.product.common.enumeration.DataSelectStatus;
import com.shangpin.ephub.data.mysql.product.common.enumeration.DataStatus;
import com.shangpin.ephub.data.mysql.product.common.enumeration.FilterFlag;
import com.shangpin.ephub.data.mysql.product.common.enumeration.HubSpuPendigStatus;
import com.shangpin.ephub.data.mysql.product.dto.HubPendingDto;
import com.shangpin.ephub.data.mysql.product.dto.SpuModelDto;
import com.shangpin.ephub.data.mysql.product.dto.SpuPendingPicDto;
import com.shangpin.ephub.data.mysql.product.service.PengingToHubService;
import com.shangpin.ephub.data.mysql.sku.hub.mapper.HubSkuMapper;
import com.shangpin.ephub.data.mysql.sku.hub.po.HubSku;
import com.shangpin.ephub.data.mysql.sku.hub.po.HubSkuCriteria;
import com.shangpin.ephub.data.mysql.sku.pending.mapper.HubSkuPendingMapper;
import com.shangpin.ephub.data.mysql.sku.pending.po.HubSkuPending;
import com.shangpin.ephub.data.mysql.sku.pending.po.HubSkuPendingCriteria;
import com.shangpin.ephub.data.mysql.sku.supplier.mapper.HubSupplierSkuMapper;
import com.shangpin.ephub.data.mysql.spu.hub.mapper.HubSpuMapper;
import com.shangpin.ephub.data.mysql.spu.hub.po.HubSpu;
import com.shangpin.ephub.data.mysql.spu.hub.po.HubSpuCriteria;
import com.shangpin.ephub.data.mysql.spu.pending.mapper.HubSpuPendingMapper;
import com.shangpin.ephub.data.mysql.spu.pending.po.HubSpuPending;
import com.shangpin.ephub.data.mysql.spu.pending.po.HubSpuPendingCriteria;
import com.shangpin.ephub.data.mysql.spu.supplier.mapper.HubSupplierSpuMapper;
import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpu;

import lombok.extern.slf4j.Slf4j;

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

    @Autowired
    private HubSpuPendingPicMapper  hubSpuPendingPicMapper;

    @Autowired
    private HubSpuPicMapper hubSpuPicMapper;


    @Autowired
    private HubSupplierValueMappingMapper supplierValueMappingMapper;

    public PengdingToHubServiceImpl() {
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean auditPending(SpuModelDto auditVO) throws Exception {
        //第一步 根据货号和品牌 获取相同的信息下的hubSpu
        List<Long> spuPendingIds = getSpuPendIdList(auditVO);

        //第二步  插入新的hub SPU 记录 以及SKU 记录
        if(null!=spuPendingIds&&spuPendingIds.size()>0){
           return  createHubData(auditVO, spuPendingIds);
        }else{
            return false;
        }

    }

    @Override
    public boolean addSkuOrSkuSupplierMapping(HubPendingDto hubPendingDto) throws Exception {

        List<Long> spuPendingIds = new ArrayList<>();
        spuPendingIds.add(hubPendingDto.getHubSpuPendingId());

        log.debug("存在spu");
        Map<String,List<HubSkuPending>> sizeSkuMap = new HashMap<>();
        //根据尺码合并不同供货商的SKU信息
        setSizeSkuMap(spuPendingIds, sizeSkuMap);
        //获取SPU
        HubSpu hubSpu = hubSpuMapper.selectByPrimaryKey(hubPendingDto.getHubSpuId());
        log.debug("hubSpu:{}",hubSpu);
        HubSpuPending spuPending = null;
        spuPending = this.getHubSpuPendingById(hubPendingDto.getHubSpuPendingId());
        if(null!=spuPending){
            //插入hubSKU 和 供货商的对应关系
            Set<String> sizeSet = sizeSkuMap.keySet();
            if(sizeSet.size()>0){
                searchAndCreateHubSkuAndMapping(sizeSkuMap, hubSpu);
            }
        }

        return true;
    }

    private boolean  createHubData(SpuModelDto auditVO, List<Long> spuPendingIds) throws Exception{
        //查询是否存在
        HubSpuCriteria criteria = new HubSpuCriteria();
        criteria.createCriteria().andSpuModelEqualTo(auditVO.getSpuModel())
                .andBrandNoEqualTo(auditVO.getBrandNo());
        List<HubSpu> hubSpus = hubSpuMapper.selectByExample(criteria);
        if(null==hubSpus||(null!=hubSpus&&hubSpus.size()==0)){ //不存在插入新的SPU记录 以及 SKU 记录
            log.info("不存在spu");
            return insertNewHubSpuAndHubSkuAndMapping(auditVO, spuPendingIds);


        }else{ //
            log.info("存在spu");
            return insertHubSkuAndMappingWhenExistHubSpu(spuPendingIds, hubSpus);
        }
    }

    private boolean insertNewHubSpuAndHubSkuAndMapping(SpuModelDto auditVO, List<Long> spuPendingIds) throws Exception {
        //合并sku pending的尺码 生成HUBSKU
        Map<String,List<HubSkuPending>> sizeSkuMap = new HashMap<>();
        //根据尺码合并不同供货商的SKU信息
        setSizeSkuMap(spuPendingIds, sizeSkuMap);
        log.info("sizeSkuMap:"+sizeSkuMap.toString());
        if(sizeSkuMap.size()>0){
            //插入新的SPU
            HubSpu hubSpu = new HubSpu();
            HubSpuPending spuPending = null;
            spuPending = this.getHubSpuPendingByIdAndSetValueByAuditVO(spuPendingIds.get(0),auditVO);
            if(null!=spuPending){
                //创建hubspu
                createHubSpu(hubSpu, spuPending);
                //在spupending中反写spuNo以及状态
                updatespuPending(spuPendingIds,hubSpu.getSpuNo());
                //创建SPU图片
                createSpuPic(auditVO.getPicVOs(),spuPendingIds,hubSpu.getSpuId());
                //插入hubSKU 和 供货商的对应关系
                Set<String> sizeSet = sizeSkuMap.keySet();
                log.info(sizeSet.size()+"");
                if(sizeSet.size()>0){
                    createHubSkuAndMapping(sizeSkuMap, hubSpu, sizeSet);
                }
                return true;
            }else{
                return false;
            }
        }else{ //  无尺码映射
            //修改pending状态为待处理
            updateSpuState(spuPendingIds, SpuState.INFO_IMPECCABLE.getIndex());

            return false;

        }
    }

    private boolean insertHubSkuAndMappingWhenExistHubSpu(List<Long> spuPendingIds, List<HubSpu> hubSpus) throws Exception {
        Map<String,List<HubSkuPending>> sizeSkuMap = new HashMap<>();
        //根据尺码合并不同供货商的SKU信息
        setSizeSkuMap(spuPendingIds, sizeSkuMap);
        //获取SPU
        HubSpu hubSpu = hubSpus.get(0);
        HubSpuPending spuPending = null;
        spuPending = this.getHubSpuPendingById(spuPendingIds.get(0));
        if(null!=spuPending){
            //插入hubSKU 和 供货商的对应关系
            Set<String> sizeSet = sizeSkuMap.keySet();
            if(sizeSet.size()>0){
                searchAndCreateHubSkuAndMapping(sizeSkuMap, hubSpu);
                //更新Spu 状态
                //在spupending中反写spuNo以及状态
                updatespuPending(spuPendingIds,hubSpu.getSpuNo());
            }else{
                updateSpuState(spuPendingIds, SpuState.INFO_IMPECCABLE.getIndex());
            }
            return true;
        }else{
            //无spu
            return false;
        }
    }


    private void searchAndCreateHubSkuAndMapping(Map<String, List<HubSkuPending>> sizeSkuMap, HubSpu hubSpu) throws Exception {
         //首先 查询尺码是否存在
        Set<String> sizeSet = sizeSkuMap.keySet();

        Date date = new Date();
        String sizeTypeAndSize ="",tmpSize="",tmpSizeType="";
        for(String size:sizeSet){
            log.debug("spuno =" + hubSpu.getSpuNo());
            log.debug("size = " + size.trim());
            sizeTypeAndSize = size;
            if(sizeTypeAndSize.indexOf(":")>0){
                tmpSizeType = sizeTypeAndSize.substring(0,sizeTypeAndSize.indexOf(":"));
                tmpSize= sizeTypeAndSize.substring(sizeTypeAndSize.indexOf(":")+1,sizeTypeAndSize.length());
            }else{
                tmpSizeType = "";
                tmpSize = size;
            }

            List<HubSku> hubSkus = this.getSkuBySpuNoAndSizeAndSizeType(hubSpu.getSpuNo(),
                    tmpSize,tmpSizeType);

            List<HubSkuPending> hubSkuPendings = sizeSkuMap.get(size);
            HubSku hubSku = null;
            if(null!=hubSkus&&hubSkus.size()>0){  //已存在
                 hubSku = hubSkus.get(0);
            }else{
                log.debug("no hubsku");
                String skuNo = hubSpuUtil.createHubSkuNo(hubSpu.getSpuNo(),1);
                hubSku = insertHubSku(hubSpu, skuNo, size, date, hubSkuPendings);

            }
            //增加SKU的映射关系  并更新hubskupending
            inserSkuSupplierMapping(date, hubSkuPendings, hubSku);


        }



    }

    @SuppressWarnings("unused")
	private void updateSkuPendingSkuStatus(List<HubSkuPending> hubSkuPendings) {
        for(HubSkuPending skuPendingDto:hubSkuPendings){
            HubSkuPending skuPengding = new HubSkuPending();
            skuPengding.setSkuPendingId(skuPendingDto.getSkuPendingId());
            skuPengding.setSkuState(HubSpuPendigStatus.HANDLED.getIndex().byteValue());//sku 可使用SPU的状态码
            hubSkuPendingMapper.updateByPrimaryKeySelective(skuPengding);
        }
    }


    private void createHubSkuAndMapping(Map<String, List<HubSkuPending>> sizeSkuMap, HubSpu hubSpu, Set<String> sizeSet) throws Exception{
        String skuNoAll = hubSpuUtil.createHubSkuNo(hubSpu.getSpuNo(),sizeSet.size());
        if(StringUtils.isNotBlank(skuNoAll)){
            String[] skuNoArray = skuNoAll.split(",");
            Object[] sizeArray = sizeSet.toArray();

            Date date = new Date();

            if(null!=skuNoArray&&skuNoArray.length>0){
                for(int i =0;i<skuNoArray.length;i++){
                    List<HubSkuPending> hubSkuPendings = sizeSkuMap.get(sizeArray[i]);
                    HubSku hubSku = insertHubSku(hubSpu, skuNoArray[i], sizeArray[i], date, hubSkuPendings);
                    //增加SKU的映射关系 并 反写hub_sku_no
                    inserSkuSupplierMapping(date, hubSkuPendings, hubSku);

                }
            }

        }
    }

    private HubSku insertHubSku(HubSpu hubSpu, String skuNo, Object o, Date date, List<HubSkuPending> hubSkuPendings) throws Exception {

        String sizeTypeAndSize = (String) o;
        String sizeType= sizeTypeAndSize.substring(0,sizeTypeAndSize.indexOf(":"));


        HubSku hubSku = new HubSku();

        hubSku.setSpuNo(hubSpu.getSpuNo());
        hubSku.setColor(hubSpu.getHubColor());
        hubSku.setSkuNo(skuNo);

        hubSku.setSkuSizeType(sizeTypeAndSize.substring(0,sizeTypeAndSize.indexOf(":")));
        hubSku.setSkuSize(sizeTypeAndSize.substring(sizeTypeAndSize.indexOf(":")+1,sizeTypeAndSize.length()));
        hubSku.setSkuSizeId(hubSkuPendings.get(0).getScreenSize());
        hubSku.setCreateTime(date);
        hubSku.setCreateUser(ConstantProperty.DATA_CREATE_USER);
        hubSku.setUpdateTime(date);
        hubSkuMapper.insert(hubSku);
        return hubSku;
    }

    private void inserSkuSupplierMapping(Date date, List<HubSkuPending> hubSkuPendings, HubSku hubSku) throws Exception{
        String barcode = "";
        for(HubSkuPending skuPending:hubSkuPendings){

            HubSkuSupplierMapping skuSupplierMapping = getSkuSupplierMappint(skuPending.getSupplierId(), skuPending.getSupplierSkuNo());
            if(null!=skuSupplierMapping){
                 continue;
            }


            HubSkuSupplierMapping hubSkuSupplierMapping = new HubSkuSupplierMapping();
            BeanUtils.copyProperties(skuPending,hubSkuSupplierMapping);
            barcode =  skuPending.getSupplierBarcode();
            if(StringUtils.isBlank(barcode)) barcode = skuPending.getSupplierSkuNo();
            hubSkuSupplierMapping.setBarcode(barcode);
            hubSkuSupplierMapping.setSkuNo(hubSku.getSkuNo());
            hubSkuSupplierMapping.setSupplierSelectState(DataSelectStatus.NOT_SELECT.getIndex().byteValue());
//            HubSpuPending spuPendingTmp =  this.getHubSpuPendingById(skuPending.getSpuPendingId());
            hubSkuSupplierMapping.setSupplierNo(skuPending.getSupplierNo());
//                                    hubSkuSupplierMapping.setIsNewSupplier();  //不知从哪里获取值
            hubSkuSupplierMapping.setCreateTime(date);
            hubSkuSupplierMapping.setCreateUser(ConstantProperty.DATA_CREATE_USER);
            hubSkuSupplierMapping.setUpdateTime(date);
            HubSupplierSku supplierSku = this.getSupplierSku(skuPending.getSupplierId(), skuPending.getSupplierSkuNo());

            if(null!=supplierSku){
                hubSkuSupplierMapping.setSupplierSkuId(supplierSku.getSupplierSkuId());
                if(null!=supplierSku.getSupplierSpuId()){
                    HubSupplierSpu supplierSpu = this.getSupplierSpuById(supplierSku.getSupplierSpuId());
                    if(null!=supplierSpu){
                        hubSkuSupplierMapping.setSupplierSpuModel(supplierSpu.getSupplierSpuModel());
                    }
                }
            }
            hubSkuSupplierMapping.setDataState(DataStatus.NOT_DELETE.getIndex().byteValue());
            hubSkuSupplierMappingMapper.insert(hubSkuSupplierMapping);

            //反写hub_sku_pending
            HubSkuPending tmp = new HubSkuPending();
            tmp.setSkuPendingId(skuPending.getSkuPendingId());
            tmp.setHubSkuNo(hubSku.getSkuNo());
            tmp.setSkuState(HubSpuPendigStatus.HANDLED.getIndex().byteValue());
            tmp.setUpdateTime(new Date());
            hubSkuPendingMapper.updateByPrimaryKeySelective(tmp);
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
            skuPendingCriteria.createCriteria().andSpuPendingIdEqualTo(spuPendingId)
                    .andSkuStateEqualTo(HubSpuPendigStatus.HANDLING.getIndex().byteValue())   //spu 和 sku 状态保持一致
                    .andSpSkuSizeStateEqualTo(DataBusinessStatus.HANDLED.getIndex().byteValue())       //尺码已映射
                    .andFilterFlagEqualTo(FilterFlag.EFFECTIVE.getIndex());  //不过滤的才使用
            
            log.info("hubSkuPendings:{}",spuPendingId+"-"+HubSpuPendigStatus.HANDLING.getIndex().byteValue()+"-"+DataBusinessStatus.HANDLED.getIndex().byteValue());
            List<HubSkuPending> hubSkuPendings = hubSkuPendingMapper.selectByExample(skuPendingCriteria);
            log.info("hubSkuPendings:{}",hubSkuPendings);
            
            for(HubSkuPending hubSkuPending:hubSkuPendings){
                if(null!=hubSkuPending.getSkuState()&&hubSkuPending.getSkuState().intValue()== HubSpuPendigStatus.HANDLING.getIndex()){//信息已完善 处理中
                    if(sizeSkuMap.containsKey(hubSkuPending.getHubSkuSizeType()+":"+hubSkuPending.getHubSkuSize())){
                        sizeSkuMap.get(hubSkuPending.getHubSkuSizeType()+":"+hubSkuPending.getHubSkuSize()).add(hubSkuPending);
                    }else{
                        List<HubSkuPending> hubSkuPendingList = new ArrayList<>();
                        hubSkuPendingList.add(hubSkuPending);
                        sizeSkuMap.put(hubSkuPending.getHubSkuSizeType()+":"+hubSkuPending.getHubSkuSize(),hubSkuPendingList);
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
        hubSpu.setCreateUser(ConstantProperty.DATA_CREATE_USER);
        hubSpu.setCategoryNo(spuPending.getHubCategoryNo());
        //

        hubSpu.setGender(spuPending.getHubGender());
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
        hubSpu.setPictureState((byte)0);
        //判断是否是代购的供货商
        if(isHotBoomSupplierId(spuPending.getSupplierId())){

            hubSpu.setHotboomSign(DataStatus.NOT_DELETE.getIndex().byteValue());
        }else{
            hubSpu.setHotboomSign(DataStatus.DELETE.getIndex().byteValue());
        }
        hubSpuMapper.insert(hubSpu);


    }

    private List<Long> getSpuPendIdList(SpuModelDto spuModelVO) {

        //获取spuPendingID 列表 以便后续得到SKUPENDING
        List<Long> spuPendingIds =  spuModelVO.getSpuPendingIds();
        if(null!=spuPendingIds&&spuPendingIds.size()>0){
            return  spuPendingIds;
        } else{

            HubSpuPendingCriteria criteriaForId = new HubSpuPendingCriteria();
            criteriaForId.setFields("spu_pending_id");
            HubSpuPendingCriteria.Criteria criterionForId = criteriaForId.createCriteria();
            criterionForId.andSpuModelEqualTo(spuModelVO.getSpuModel()).andHubBrandNoEqualTo(spuModelVO.getBrandNo())
                    .andSpuStateEqualTo(HubSpuPendigStatus.HANDLING.getIndex().byteValue()) ;
//        .andSpSkuSizeStateEqualTo(DataBusinessStatus.HANDLED.getIndex().byteValue());
            criteriaForId.setPageNo(1);
            criteriaForId.setPageSize(ConstantProperty.MAX_COMMON_QUERY_NUM);
            List<HubSpuPending> hubSpuPendingIds = hubSpuPendingMapper.selectByExample(criteriaForId);
            List<Long> pendIdList = new ArrayList<>();
            for(HubSpuPending spuPending:hubSpuPendingIds){
                log.info("spuPending.getSpuPendingId()= "+ spuPending.getSpuPendingId());

                pendIdList.add(spuPending.getSpuPendingId());

            }
            return pendIdList;
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

    }


    private HubSpuPending getHubSpuPendingByIdAndSetValueByAuditVO(Long id,SpuModelDto auditVO){
//        HubSpuPendingCriteria criteria = new HubSpuPendingCriteria();
//        criteria.createCriteria().andSpuPendingIdEqualTo(id);
//        List<HubSpuPending> hubSpuPendings = hubSpuPendingMapper.selectByExample(criteria);
        HubSpuPending hubSpuPending = this.getHubSpuPendingById(id);
        if(null!=hubSpuPending){
            if(null!=auditVO&&null!=auditVO.getCategoryNo()){
                hubSpuPending.setHubColor(auditVO.getColor());
                hubSpuPending.setHubCategoryNo(auditVO.getCategoryNo());
                hubSpuPending.setHubOrigin(auditVO.getOrigin());
                hubSpuPending.setHubMaterial(auditVO.getMaterial());
                if(StringUtils.isNotBlank(auditVO.getSpuName())){
                    hubSpuPending.setSpuName(auditVO.getSpuName());
                }
            }
            return    hubSpuPending;
        }else{
            return null;
        }

    }

    private HubSpuPending getHubSpuPendingById(Long id){
       return  hubSpuPendingMapper.selectByPrimaryKey(id);
    }

    //更改spupending的hubspu编号 以及spuState状态
    private void updatespuPending(List<Long> spuPendingIds,String spuNo){
        HubSpuPendingCriteria criteria = new HubSpuPendingCriteria();
        criteria.createCriteria().andSpuPendingIdIn(spuPendingIds);
        HubSpuPending record = new HubSpuPending();
        record.setHubSpuNo(spuNo);
        record.setSpuState(SpuState.HANDLED.getIndex());
        record.setUpdateTime(new Date());
        hubSpuPendingMapper.updateByExampleSelective(record,criteria);


    }


    private void updateSpuState(List<Long> spuPendingIds,byte spuState){
        HubSpuPendingCriteria criteria = new HubSpuPendingCriteria();
        criteria.createCriteria().andSpuPendingIdIn(spuPendingIds);
        HubSpuPending record = new HubSpuPending();
        record.setSpuState(spuState);
        record.setMemo("无符合的SKU,请重新处理");
        hubSpuPendingMapper.updateByExampleSelective(record,criteria);


    }

    private void createSpuPic(List<SpuPendingPicDto> picVOs,List<Long> spuPendingIds, Long spuId){
        //循环查找最多的图片
        List<HubSpuPendingPic> hubSpuPendingPics = null;
        if(null!=picVOs&&picVOs.size()>0){
            hubSpuPendingPics = new ArrayList<>();
            for(SpuPendingPicDto dto:picVOs){
                HubSpuPendingPic spuPendingPic = new HubSpuPendingPic();
                spuPendingPic.setSpuPendingPicId(dto.getPicId());
                spuPendingPic.setSpPicUrl(dto.getSpPicUrl());
                spuPendingPic.setMemo(dto.getMemo());
                hubSpuPendingPics.add(spuPendingPic);
            }
        }else{
            int i=0;
            int max=0;
            Long maxCountSupplierSpuId =0L;
            HubSpuPending spuPending = null;
            for(Long spuPendId:spuPendingIds){
                spuPending = this.getHubSpuPendingById(spuPendId);
                if(null!=spuPending){

                    HubSpuPendingPicCriteria criteria = new HubSpuPendingPicCriteria();
                    criteria.createCriteria().andSupplierSpuIdEqualTo(spuPending.getSupplierSpuId())
                            .andPicHandleStateEqualTo(PicState.HANDLED.getIndex());
                    i = hubSpuPendingPicMapper.countByExample(criteria);
                    if(i>max){
                        max = i;
                        maxCountSupplierSpuId = spuPending.getSupplierSpuId();
                    }
                }
            }

            HubSpuPendingPicCriteria criteria = new HubSpuPendingPicCriteria();
            criteria.setPageSize(20);
            criteria.setPageNo(1);
            criteria.createCriteria().andSupplierSpuIdEqualTo(maxCountSupplierSpuId);
            hubSpuPendingPics = hubSpuPendingPicMapper.selectByExample(criteria);

        }




        Date date = new Date();
        String url = "";  int i=0;
        for(HubSpuPendingPic spuPendingPic:hubSpuPendingPics){
            HubSpuPic hubSpuPic= new HubSpuPic();
            hubSpuPic.setCreateTime(date);
            hubSpuPic.setUpdateTime(date);
            hubSpuPic.setSpPicUrl(spuPendingPic.getSpPicUrl());
            if(0==i){
                url = spuPendingPic.getSpPicUrl();
            }
            hubSpuPic.setSpuId(spuId);
            hubSpuPic.setDataState(DataStatus.NOT_DELETE.getIndex().byteValue());
            hubSpuPic.setPicId(spuPendingPic.getSpuPendingPicId());
            hubSpuPicMapper.insert(hubSpuPic);
            i++;

        }
        //更新HUBSPU 增加图片地址
        if(!"".equals(url)){

            HubSpu hubspu=new HubSpu();
            hubspu.setSpuId(spuId);
            hubspu.setPicUrl(url);
            hubSpuMapper.updateByPrimaryKeySelective(hubspu);
        }

    }

    private HubSupplierSku getSupplierSku(String supplierId, String supplierSkuNo){
        HubSupplierSkuCriteria example = new HubSupplierSkuCriteria();
        example.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSkuNoEqualTo(supplierSkuNo);
        List<HubSupplierSku> hubSupplierSkus = hubSupplierSkuMapper.selectByExample(example);
        if(null!=hubSupplierSkus&&hubSupplierSkus.size()>0){
            return hubSupplierSkus.get(0);

        }else{
            return null;
        }
    }

    private HubSkuSupplierMapping getSkuSupplierMappint(String supplierId,String supplierSkuNo){
        HubSkuSupplierMappingCriteria example = new HubSkuSupplierMappingCriteria();
        example.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSkuNoEqualTo(supplierSkuNo);
        List<HubSkuSupplierMapping> skuSupplierMappings = hubSkuSupplierMappingMapper.selectByExample(example);
        if(null!=skuSupplierMappings&&skuSupplierMappings.size()>0){
            return skuSupplierMappings.get(0);
        }else{
            return null;
        }
    }

    private HubSupplierSpu getSupplierSpuById(Long supplierSpuId) {
       return hubSupplierSpuMapper.selectByPrimaryKey(supplierSpuId);
    }

    private List<HubSku> getSkuBySpuNoAndSizeAndSizeType(String spuNo,String size,String sizeType){

        //如果size类型=尺寸 不需要查询尺寸  20181015 修改为要查询尺码
        HubSkuCriteria hubSkuCriteria =new HubSkuCriteria();
        if(PropertyConstant.SIZE_TYPE.equals(sizeType)){
            hubSkuCriteria.createCriteria().andSpuNoEqualTo(spuNo).andSkuSizeEqualTo(size).andSkuSizeTypeEqualTo(sizeType);
        }else{
            hubSkuCriteria.createCriteria().andSpuNoEqualTo(spuNo).andSkuSizeEqualTo(size).andSkuSizeTypeEqualTo(sizeType);
        }
        return  hubSkuMapper.selectByExample(hubSkuCriteria);


    }

    private boolean isHotBoomSupplierId(String supplierId ){
        HubSupplierValueMappingCriteria criteria = new HubSupplierValueMappingCriteria();
        criteria.createCriteria().andHubValTypeEqualTo(SupplierValueMappingType.TYPE_BRAND_SUPPLIER.getIndex().byteValue())
                .andSupplierIdEqualTo(supplierId).andDataStateEqualTo(DataStatus.NOT_DELETE.getIndex().byteValue());
        List<HubSupplierValueMapping> hubSupplierValueMappings = supplierValueMappingMapper.selectByExample(criteria);
        if(null!=hubSupplierValueMappings&&hubSupplierValueMappings.size()>0){
            return true;
        }
        return false;
    }

}

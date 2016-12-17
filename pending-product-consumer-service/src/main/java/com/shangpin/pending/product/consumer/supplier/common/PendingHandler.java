package com.shangpin.pending.product.consumer.supplier.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubBrandDicGateway;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.message.pending.body.PendingProduct;
import com.shangpin.ephub.client.message.pending.body.sku.PendingSku;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.pending.product.consumer.common.ConstantProperty;
import com.shangpin.pending.product.consumer.common.DateUtils;
import com.shangpin.pending.product.consumer.common.enumeration.MessageType;
import com.shangpin.pending.product.consumer.common.enumeration.PropertyStatus;
import com.shangpin.pending.product.consumer.conf.clients.mysql.sku.bean.HubSkuPending;
import com.shangpin.pending.product.consumer.conf.clients.mysql.spu.bean.HubSpuPending;

import com.shangpin.pending.product.consumer.supplier.dto.ColorDTO;
import com.shangpin.pending.product.consumer.supplier.dto.PendingHeaderSku;
import com.shangpin.pending.product.consumer.supplier.dto.PendingHeaderSpu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * Created by loyalty on 16/12/13.
 * 数据从缓存中拉取
 */
@Component
@Slf4j

public class PendingHandler {


    @Autowired
    IShangpinRedis shangpinRedis;


    @Autowired
    DataServiceHandler dataServiceHandler;






    static Map<String,Map<String,String>> supplierGenderStaticMap = null;

    static Map<String,Map<String,String>> supplierCategoryMappingStaticMap = null;

    static Map<String,String> brandStaticMap = null;

    static Map<String,String> colorStaticMap = null;

    static Map<String,String> seasonStaticMap = null;

    static Map<String,String> materialStaticMap = null;


    public void receiveMsg(PendingProduct message, Map<String, Object> headers) throws Exception{
        log.info("receive message :" + message.toString() + " message header :"+
        headers.toString());

        Map<String,Integer> messageMap = this.getMessageStatus(headers);

        PendingSpu pendingSpu = message.getData();
        HubSpuPendingDto hubSpuPending = null;
        if(messageMap.containsKey(pendingSpu.getSupplierSpuId())){

            Integer spuStatus = messageMap.get(pendingSpu.getSupplierSpuId());
            if(spuStatus== MessageType.NEW.getIndex()){
                hubSpuPending = this.addNewSpu(pendingSpu,headers);

            }else if(spuStatus==MessageType.UPDATE.getIndex()){
                hubSpuPending = this.updateSpu(pendingSpu,headers);

            }else{
                //TODO  获取hubspu对象
            }
        }

        List<PendingSku> skus = pendingSpu.getSkus();
        Integer skuStatus = 0;
        for(PendingSku sku:skus){
            if(messageMap.containsKey(sku.getSupplierSkuNo())){
                skuStatus = messageMap.get(sku.getSupplierSkuNo());
                if(skuStatus== MessageType.NEW.getIndex()){
                    this.addNewSku(sku,hubSpuPending.getHubCategoryNo(),hubSpuPending.getHubBrandNo(),headers);

                }else if(skuStatus==MessageType.UPDATE.getIndex()){
                    //TODO UPDATE OLD

                }else if(skuStatus==MessageType.MODIFY_PRICE.getIndex()){
                   //TODO 处理自动调整价格
                }
            }
        }






    }

    private  Map<String,Integer> getMessageStatus(Map<String, Object> headers){

        Map<String,Integer> result = new HashMap<>();
        String key = ConstantProperty.PENDING_PRODUCT_MAP_KEY;
        String value = "";
        if(headers.containsKey(key)){
            value = (String) headers.get(key);
            ObjectMapper om = new ObjectMapper();
            try {
                PendingHeaderSpu spu =  om.readValue(value,PendingHeaderSpu.class);
                if(null!=spu){
                    result.put(spu.getSpuNo(),spu.getStatus());
                    List<PendingHeaderSku> skus = spu.getSkus();
                    for(PendingHeaderSku sku:skus){
                        result.put(sku.getSkuNo(),sku.getStatus());
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return result;

    }

    private HubSpuPendingDto addNewSpu(PendingSpu spu, Map<String, Object> headers) throws Exception{

        //judage in hub_spu by product_code  ,if exist ,set value from hub_spu and set spu status value is 1
         String productCode = spu.getSpuModel();
        HubSpuPendingDto hubSpuPending = new HubSpuPendingDto();
        HubSpuDto hubSpuDto = dataServiceHandler.getHubSpuByProductModel(spu.getSpuModel());
        if(null!=hubSpuDto){
              //TODO  直接复制HUB-SPU里的信息  ，SPU状态 直接为审核通过
            hubSpuPending.setHubBrandNo(hubSpuDto.getBrandNo());
            hubSpuPending.setHubCategoryNo(hubSpuDto.getCategoryNo());
        }else{

            BeanUtils.copyProperties(spu,hubSpuPending);
            boolean allStatus=true;
            //设置性别
            if(!setGenderMapping(spu, hubSpuPending)) allStatus=false;

            //获取品类
            if(!setCategoryMapping(spu, hubSpuPending)) allStatus=false;

            //获取品牌
            if(!setBrandMapping(spu, hubSpuPending)) allStatus=false;

            //获取颜色
            if(!setColorMapping(spu, hubSpuPending)) allStatus = false;

            //获取季节
            if(!setSeasonMapping(spu, hubSpuPending)) allStatus = false;

            //获取材质
            replaceMaterial(spu, hubSpuPending);

            if(allStatus){
                hubSpuPending.setSpuState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
            }else{
                hubSpuPending.setSpuState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            }


            dataServiceHandler.savePendingSpu(hubSpuPending);

        }
        return hubSpuPending;

    }

    private void replaceMaterial(PendingSpu spu, HubSpuPendingDto hubSpuPending) {
        Map<String, String> materialMap = this.getMaterialMap();
        Set<String> materialSet = materialMap.keySet();
        for(String material:materialSet){
            if(spu.getHubMaterial().toLowerCase().indexOf(material.toLowerCase())>=0){
                 spu.setHubMaterial(spu.getHubMaterial().toLowerCase().replaceAll(material.toLowerCase(),
                         materialMap.get(material.toLowerCase())));
            }
        }
        hubSpuPending.setHubMaterial(spu.getHubMaterial());
    }

    private boolean setSeasonMapping(PendingSpu spu, HubSpuPendingDto hubSpuPending) throws Exception {
        Map<String, String> seasonMap = this.getSeasonMap(spu.getSupplierId());
        boolean result = true;

        if(seasonMap.containsKey(spu.getSupplierId()+"_"+ spu.getHubSeason())){
            //包含时转化赋值
            hubSpuPending.setHubSeason(seasonMap.get(spu.getSupplierId()+"_"+ spu.getHubSeason()));
            hubSpuPending.setSpuSeasonState( PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());

        }else{//
            result = false;
            hubSpuPending.setSpuSeasonState( PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            dataServiceHandler.saveBrand(spu.getSupplierId(),spu.getHubSeason());
        }
        return  result;
    }

    public  boolean setColorMapping(PendingSpu spu, HubSpuPendingDto hubSpuPending) throws Exception {
        boolean result = true;
        Map<String, String> colorMap = this.getColorMap();
        if(colorMap.containsKey(spu.getHubColor())&!StringUtils.isEmpty(colorMap.get(spu.getHubColor()))){
            //包含时转化赋值
            hubSpuPending.setHubColor(colorMap.get(spu.getHubColor()));
            hubSpuPending.setSpuColorState( PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());

        }else{
            result = false;
            hubSpuPending.setSpuColorState( PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            dataServiceHandler.saveColorItem(spu.getHubColor());

        }
        return result;
    }

    public  boolean  setBrandMapping(PendingSpu spu, HubSpuPendingDto hubSpuPending) throws Exception {
        boolean result = true;
        Map<String, String> brandMap = this.getBrandMap();
        if(brandMap.containsKey(spu.getHubBrandNo())){
            //包含时转化赋值
            hubSpuPending.setHubBrandNo(brandMap.get(spu.getHubBrandNo()));
            hubSpuPending.setSpuBrandState( PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());

        }else{
            result = false;
            hubSpuPending.setSpuBrandState( PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            dataServiceHandler.saveBrand(spu.getSupplierId(),spu.getHubBrandNo());

        }
        return  result;
    }

    public  boolean  setCategoryMapping(PendingSpu spu, HubSpuPendingDto hubSpuPending) throws Exception {
        boolean result = true;
        String categoryAndStatus="";
        Integer mapStatus=0;
        Map<String, Map<String,String>> supplierCategoryMappingMap = this.getCategoryMappingMap(spu.getSupplierId());
        if(supplierCategoryMappingMap.containsKey(spu.getSupplierId())){
            Map<String, String> categoryMappingMap = supplierCategoryMappingMap.get(spu.getSupplierId());

            if(categoryMappingMap.containsKey(spu.getHubCategoryNo()+"_"+spu.getHubGender())){
                //包含时转化赋值
                categoryAndStatus = categoryMappingMap.get(spu.getHubCategoryNo()+"_"+spu.getHubGender());
                if(categoryAndStatus.contains("_")){
                    hubSpuPending.setHubCategoryNo(categoryAndStatus.substring(0,categoryAndStatus.indexOf("_")));
                    mapStatus = Integer.valueOf(categoryAndStatus.substring(categoryAndStatus.indexOf("_")+1));
                    hubSpuPending.setCatgoryState( mapStatus.byteValue());

                }else{
                    result = false;

                    hubSpuPending.setCatgoryState( PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
                }

            }else{

                result = false;
                hubSpuPending.setCatgoryState( PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
                dataServiceHandler.saveHubCategory(spu.getSupplierId(),spu.getHubCategoryNo(),spu.getHubGender());


            }
        }else {
            result = false;
            hubSpuPending.setCatgoryState( PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            dataServiceHandler.saveHubCategory(spu.getSupplierId(),spu.getHubCategoryNo(),spu.getHubGender());

        }
        return result;
    }

    public  boolean setGenderMapping(PendingSpu spu, HubSpuPendingDto hubSpuPending) throws Exception {
        boolean result = true;
        //获取性别
        Map<String,Map<String,String>>  supplierGenderMap = this.getGenderMap(spu.getSupplierId());
        if(supplierGenderMap.containsKey(spu.getSupplierId())){
            Map<String, String> genderMap = supplierGenderMap.get(spu.getSupplierId());

            if(genderMap.containsKey(spu.getHubGender())){
                 //包含时转化赋值
                hubSpuPending.setHubGender(genderMap.get(spu.getHubGender()));
                hubSpuPending.setSpuGenderState( PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
            }else{
                result = false;
                hubSpuPending.setSpuGenderState( PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
                dataServiceHandler.saveHubGender(spu.getSupplierId(),spu.getHubGender());

            }
        }else{
            result = false;
            hubSpuPending.setSpuGenderState( PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            dataServiceHandler.saveHubGender(spu.getSupplierId(),spu.getHubGender());
        }
        return  result;
    }


    private HubSpuPendingDto updateSpu(PendingSpu spu, Map<String, Object> headers) throws Exception{

        //TODO 判断状态 是否可以修改
        return  null;

    }

    private void addNewSku(PendingSku sku,String categoryNo,String brandNo, Map<String, Object> headers) throws Exception{
        HubSkuPending hubSkuPending = new HubSkuPending();
        BeanUtils.copyProperties(sku,hubSkuPending);
        String hubSize=this.getHubSize(categoryNo,brandNo,sku.getHubSkuSize());
        if("".equals(hubSize)){
            hubSkuPending.setSpSkuSizeState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
        }else{
            hubSkuPending.setSpSkuSizeState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
            hubSkuPending.setHubSkuSize(hubSize);
            hubSkuPending.setCreateTime(new Date());

        }
        //TODO 增加SKU

    }


    private void updateSku(PendingSku sku, Map<String, Object> headers) throws Exception{
        //TODO 判断状态 是否可以修改

    }

    private void handlePrice(PendingSku sku, Map<String, Object> headers) throws Exception{

    }

    /**
     * 按供货商获取品类的映射关系  主键  supplier_category_gender  value 尚品的品类编号+"_"+匹配状态
     * @param supplierId
     * @return
     */
    private Map<String,Map<String,String>> getCategoryMappingMap(String supplierId) throws Exception {
        if(null==supplierCategoryMappingStaticMap){//初始化
            supplierCategoryMappingStaticMap = new HashMap<>();
            this.setSupplierCategoryValueToMap(supplierId);

        }else{
            if(!supplierCategoryMappingStaticMap.containsKey(supplierId)){//未包含
                this.setSupplierCategoryValueToMap(supplierId);
            }else{
                if(isNeedHandle()){//包含 需要重新拉取
                    this.setSupplierCategoryValueToMap(supplierId);
                }
            }
        }
          return null;
    }

    private void setSupplierCategoryValueToMap(String supplierId)  throws Exception{

        List<HubGenderDicDto> hubGenderDicDtos = dataServiceHandler.getHubGenderDicBySupplierId(supplierId);
        Map<Long,String> hubGenderMap = new HashMap<>();
        for(HubGenderDicDto dto:hubGenderDicDtos){
            hubGenderMap.put(dto.getGenderDicId(),dto.getSupplierGender());
        }

        List<HubSupplierCategroyDicDto> hubSupplierCategroyDicDtos = dataServiceHandler.getSupplierCategoryBySupplierId(supplierId);
        if(null!=hubSupplierCategroyDicDtos&&hubSupplierCategroyDicDtos.size()>0){
            Map<String,String> categoryMap = new HashMap<>();
            for(HubSupplierCategroyDicDto dto:hubSupplierCategroyDicDtos){
                // map 的key 供货商的品类 + "_"+供货商的性别 ，value ： 尚品的品类 + "_"+ 匹配状态
                if(hubGenderMap.containsKey(dto.getGenderDicId())){
                    categoryMap.put(dto.getSupplierCategory()+"_"+hubGenderMap.get(dto.getGenderDicId()),dto.getHubCategoryNo()+"_"+dto.getMappingState());
                }
            }
            supplierCategoryMappingStaticMap.put(supplierId,categoryMap);
        }

    }

    /**
     * key : supplierId_supplierGender
     * @param supplierId
     * @return
     */
    private Map<String,Map<String,String>> getGenderMap(String supplierId){
        if(null==supplierGenderStaticMap){
            supplierGenderStaticMap = new HashMap<>();
            setGenderValueToMap(supplierId);
        }else{
            if(!supplierGenderStaticMap.containsKey(supplierId)){
                setGenderValueToMap(supplierId);
            }else{
                if(isNeedHandle()){
                    setGenderValueToMap(supplierId);
                }
            }

        }

        return supplierGenderStaticMap;
    }

    private void setGenderValueToMap(String supplierId) {
        List<HubGenderDicDto> hubGenderDics = dataServiceHandler.getHubGenderDicBySupplierId(supplierId);
        if(null!=hubGenderDics&&hubGenderDics.size()>0){

            Map<String,String> genderMap = new HashMap<>();
            for(HubGenderDicDto dto:hubGenderDics){
                genderMap.put(dto.getSupplierGender(),dto.getHubGender());
            }
            supplierGenderStaticMap.put(supplierId,genderMap);

        }
    }

    /**
     * key 供货商品牌名称  value 尚品的品牌编号
     * @return
     */
    private Map<String,String>  getBrandMap() throws Exception {


        if(null==brandStaticMap){
            for (HubBrandDicDto hubBrandDicDto : dataServiceHandler.getBrand()) {
                brandStaticMap.put(hubBrandDicDto.getSupplierBrand(),hubBrandDicDto.getHubBrandNo());
            }
            ;

        }else{
            if(isNeedHandle()){
                for (HubBrandDicDto hubBrandDicDto : dataServiceHandler.getBrand()) {
                    brandStaticMap.put(hubBrandDicDto.getSupplierBrand(),hubBrandDicDto.getHubBrandNo());
                }
                ;
                //无用的内容 暂时不考虑

            }
        }
        return brandStaticMap;
    }




    private Map<String,String> getColorMap(){
        if(null==colorStaticMap){
            colorStaticMap = new HashMap<>();
            List<ColorDTO> colorDTOS = dataServiceHandler.getColorDTO();
            for(ColorDTO dto:colorDTOS){
                colorStaticMap.put(dto.getSupplierColor(),dto.getHubColorName());
            }

        }else{
            if(isNeedHandle()){
                for (ColorDTO dto : dataServiceHandler.getColorDTO()) {
                    colorStaticMap.put(dto.getSupplierColor(),dto.getHubColorName());
                }

                //无用的内容 暂时不考虑

            }
        }
        return colorStaticMap;
    }

    /**
     * key :supplierId+"_"+supplierSeason value:hub_year+"_"+hub_season
     * @param supplierId
     * @return
     */
    private  Map<String,String> getSeasonMap(String supplierId){
        if(null==seasonStaticMap){
            seasonStaticMap = new HashMap<>();
            List<HubSeasonDicDto> hubSeasonDics = dataServiceHandler.getHubSeasonDic();
            for(HubSeasonDicDto dicDto:hubSeasonDics){
                seasonStaticMap.put(dicDto.getSupplierid()+"_"+dicDto.getSupplierSeason(),
                        dicDto.getHubMarketTime()+"_"+dicDto.getHubSeason());
            }

        }else{
            if(isNeedHandle()){
                List<HubSeasonDicDto> hubSeasonDics = dataServiceHandler.getHubSeasonDic();
                for(HubSeasonDicDto dicDto:hubSeasonDics){
                    seasonStaticMap.put(dicDto.getSupplierid()+"_"+dicDto.getSupplierSeason(),
                            dicDto.getHubMarketTime()+"_"+dicDto.getHubSeason());
                }
            }

        }
       return seasonStaticMap;
    }

    /**
     * 材质获取  做替换
     *
     *
     * @return
     */
    private Map<String,String> getMaterialMap(){
        if(null==materialStaticMap){
            materialStaticMap = new HashMap<>();
        }else{

        }
        return materialStaticMap;
    }

    private  String getHubSize(String hubCategoryNo,String hubBrandNo,String supplierSize){
        String result = "";
        //TODO CALL API OF SOP
        return result;
    }

    /**
     * 在指定时间段 重新获取所有数据
     * @return
     */
    private boolean  isNeedHandle(){
        int min= DateUtils.getCurrentMin();
        if(min%60<2){
            return true;
        }else{
            return false;
        }


    }



}

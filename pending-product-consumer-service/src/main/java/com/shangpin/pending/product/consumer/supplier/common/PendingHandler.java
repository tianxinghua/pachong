package com.shangpin.pending.product.consumer.supplier.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.pending.product.consumer.common.ConstantProperty;
import com.shangpin.pending.product.consumer.common.DateUtils;
import com.shangpin.pending.product.consumer.common.enumeration.MessageType;
import com.shangpin.pending.product.consumer.common.enumeration.PropertyStatus;
import com.shangpin.pending.product.consumer.conf.clients.mysql.sku.bean.HubSkuPending;
import com.shangpin.pending.product.consumer.conf.clients.mysql.spu.bean.HubSpuPending;
import com.shangpin.pending.product.consumer.conf.stream.sink.message.PendingProduct;
import com.shangpin.pending.product.consumer.conf.stream.sink.message.sku.PendingSku;
import com.shangpin.pending.product.consumer.conf.stream.sink.message.spu.PendingSpu;
import com.shangpin.pending.product.consumer.supplier.dto.PendingHeaderSku;
import com.shangpin.pending.product.consumer.supplier.dto.PendingHeaderSpu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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


    static Map<String,Map<String,String>> supplierGenderStaticMap = null;

    static Map<String,Map<String,String>> categoryMappingStaticMap = null;

    static Map<String,String> brandStaticMap = null;

    static Map<String,String> colorStaticMap = null;

    static Map<String,String> seasonStaticMap = null;

    static Map<String,String> materialStaticMap = null;


    public void receiveMsg(PendingProduct message, Map<String, Object> headers){
        //TODO judge message type
        try {
            Map<String,Integer> messageMap = this.getMessageStatus(headers);

            PendingSpu pendingSpu = message.getData();
            HubSpuPending hubSpuPending = null;
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


        } catch (Exception e) {
            e.printStackTrace();
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

    private HubSpuPending addNewSpu(PendingSpu spu, Map<String, Object> headers) throws Exception{

        //judage in hub_spu by product_code  ,if exist ,set value from hub_spu and set spu status value is 1
         String productCode = spu.getSpuModel();
         //TODO 根据货号 获取hub-spu 的信息
        HubSpuPending hubSpuPending = new HubSpuPending();
        if(true){
              //TODO  直接复制HUB-SPU里的信息  ，SPU状态 直接为审核通过
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
            //TODO 存储SPU

        }
        return hubSpuPending;

    }

    private void replaceMaterial(PendingSpu spu, HubSpuPending hubSpuPending) {
        Map<String, String> materialMap = this.getMaterialMap(spu.getSupplierId());
        Set<String> materialSet = materialMap.keySet();
        for(String material:materialSet){
            if(spu.getHubMaterial().toLowerCase().indexOf(material.toLowerCase())>=0){
                 spu.setHubMaterial(spu.getHubMaterial().toLowerCase().replaceAll(material.toLowerCase(),
                         materialMap.get(material.toLowerCase())));
            }
        }
        hubSpuPending.setHubMaterial(spu.getHubMaterial());
    }

    private boolean setSeasonMapping(PendingSpu spu, HubSpuPending hubSpuPending) {
        Map<String, String> seasonMap = this.getSeasonMap(spu.getSupplierId());
        boolean result = true;

        if(seasonMap.containsKey(spu.getSupplierId()+"_"+ spu.getHubSeason())){
            //包含时转化赋值
            hubSpuPending.setHubSeason(seasonMap.get(spu.getSupplierId()+"_"+ spu.getHubSeason()));
            hubSpuPending.setSpuSeasonState( PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());

        }else{//TODO 未包含 此供货商 需要添加到季节字典表 人工维护
            result = false;
            hubSpuPending.setSpuSeasonState( PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());

        }
        return  result;
    }

    private boolean setColorMapping(PendingSpu spu, HubSpuPending hubSpuPending) {
        boolean result = true;
        Map<String, String> colorMap = this.getColorMap();
        if(colorMap.containsKey(spu.getHubColor())){
            //包含时转化赋值
            hubSpuPending.setHubColor(colorMap.get(spu.getHubColor()));
            hubSpuPending.setSpuColorState( PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());

        }else{//TODO 未包含 此供货商 需要添加到人工维护
            result = false;
            hubSpuPending.setSpuColorState( PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());

        }
        return result;
    }

    private boolean  setBrandMapping(PendingSpu spu, HubSpuPending hubSpuPending) {
        boolean result = true;
        Map<String, String> brandMap = this.getBrandMap();
        if(brandMap.containsKey(spu.getHubBrandNo())){
            //包含时转化赋值
            hubSpuPending.setHubBrandNo(brandMap.get(spu.getHubBrandNo()));
            hubSpuPending.setSpuBrandState( PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());

        }else{//TODO 未包含 此供货商 需要添加到品牌字典 以及 供货商的品牌字典人工维护
            result = false;
            hubSpuPending.setSpuBrandState( PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
        }
        return  result;
    }

    private boolean  setCategoryMapping(PendingSpu spu, HubSpuPending hubSpuPending) {
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
                //TODO 不包含插入字典表 需要人工维护
                result = false;
                hubSpuPending.setCatgoryState( PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());


            }
        }else {//TODO 未包含 此供货商 需要添加到字典 人工维护
            result = false;
            hubSpuPending.setCatgoryState( PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());

        }
        return result;
    }

    private boolean setGenderMapping(PendingSpu spu, HubSpuPending hubSpuPending) {
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
                //TODO 不包含插入字典表 需要人工维护
                result = false;
                hubSpuPending.setSpuGenderState( PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());


            }
        }else{//TODO 未包含 此供货商 需要添加到字典 人工维护
            result = false;
            hubSpuPending.setSpuGenderState( PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());

        }
        return  result;
    }


    private HubSpuPending updateSpu(PendingSpu spu, Map<String, Object> headers) throws Exception{

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
    private Map<String,Map<String,String>> getCategoryMappingMap(String supplierId){
          return null;
    }


    /**
     * key : supplierId_supplierGender
     * @param supplierId
     * @return
     */
    private Map<String,Map<String,String>> getGenderMap(String supplierId){
        return null;
    }

    /**
     * key 供货商品牌名称  value 尚品的品牌编号
     * @return
     */
    private Map<String,String>  getBrandMap(){
        if(null==brandStaticMap){

        }else{
            if(isNeedHandle()){

            }
        }
        return brandStaticMap;
    }


    private Map<String,String> getColorMap(){
        return null;
    }

    /**
     * key :supplierId+"_"+supplierSeason value:hub_year+"_"+hub_season
     * @param supplierId
     * @return
     */
    private  Map<String,String> getSeasonMap(String supplierId){
       return null;
    }

    /**
     * 材质获取  做替换
     *
     * @param supplierId
     * @return
     */
    private Map<String,String> getMaterialMap(String supplierId){
        return null;
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

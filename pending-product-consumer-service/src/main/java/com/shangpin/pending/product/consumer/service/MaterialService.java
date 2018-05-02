package com.shangpin.pending.product.consumer.service;



import com.shangpin.ephub.client.data.mysql.mapping.dto.HubMaterialMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubMaterialMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubMaterialMappingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.*;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.ephub.client.util.RegexUtil;
import com.shangpin.ephub.response.HubResponse;
import com.shangpin.pending.product.consumer.common.ConstantProperty;
import com.shangpin.pending.product.consumer.common.enumeration.DataBusinessStatus;
import com.shangpin.pending.product.consumer.common.enumeration.InfoState;
import com.shangpin.pending.product.consumer.common.enumeration.MaterialSourceEnum;
import com.shangpin.pending.product.consumer.common.enumeration.PropertyStatus;
import com.shangpin.pending.product.consumer.rest.dto.MaterialQuery;
import com.shangpin.pending.product.consumer.supplier.common.PendingCommonHandler;
import com.shangpin.pending.product.consumer.supplier.common.TranslationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by lizhongren on 2018/3/1.
 */
@Service
@Slf4j
public class MaterialService {

    @Autowired
    MaterialProperties properties;




    @Autowired
    private HubMaterialMappingGateWay hubMaterialMappingGateWay;

    @Autowired
    private HubSupplierSpuGateWay supplierSpuGateWay;

    @Autowired
    private HubSpuPendingGateWay spuPendingGateWay;

    @Autowired
    TranslationUtil translationUtil;

    @Autowired
    PendingCommonHandler pendingCommonHandler;

    /**
     * 查询supplierSpuNum 的数量
     */
    int searchSupplierSpuNum = 500;

    /**
     * 一次需要翻译的数量
     */
    int PAGE_SIZE = 200;


    static Map<String,String> supplierIdMap = null;

    /**
     * 先查询完全匹配的 ，如果能找到供货商材质的 直接替换,否则看是否是重点供货商，是翻译，否则不翻译
     * @param spu
     * @param hubSpuPending
     * @return
     */
    public boolean changeSupplierToHub(PendingSpu spu, HubSpuPendingDto hubSpuPending){
        long start = System.currentTimeMillis();
        //第一级匹配
        if(firstLevelTranslation(spu, hubSpuPending)){
            log.info("一级翻译总耗时 =="+ (System.currentTimeMillis()- start ));
            return true;
        }else{

            // 二级 词组替换 三级 单词替换
            return secondAndThirdTranslate(spu.getHubMaterial(), hubSpuPending, start);
        }

    }




    public boolean translateMaterial(HubSpuPendingDto dto,String supplierMaterial){
        long start = System.currentTimeMillis();
        if(StringUtils.isBlank(supplierMaterial)) return false;
        String hubMaterial = this.getHubMaterialOfFirstLevel(supplierMaterial);
        if(StringUtils.isBlank(hubMaterial)) {
            //材质字典中没有

            //第一级匹配
            if (firstLevelTranslation(dto, supplierMaterial)) {
                log.info("一级翻译总耗时 =="+ (System.currentTimeMillis()- start ));
                return true;
            } else {
                // 二级 词组替换 三级 单词替换
                return secondAndThirdTranslate(supplierMaterial, dto, start);
            }


        }else{
            dto.setHubMaterial(hubMaterial);
            if (!RegexUtil.excludeLetter(hubMaterial)) {
                // 材质含有英文 返回false
                return false;
            } else {
                return true;
            }

        }

    }


    private boolean secondAndThirdTranslate(String supplierMaterial , HubSpuPendingDto hubSpuPending, long start) {
        String hubMaterial = hubSpuPending.getHubMaterial();

        boolean result =  replaceMaterial(hubSpuPending);
        log.debug("一二三级翻译总耗时 =="+ (System.currentTimeMillis()- start ));
        if(StringUtils.isNotBlank(hubMaterial)){
            if(!hubMaterial.equals(hubSpuPending.getHubMaterial())){
                // 处理过 更新
                if(result){
                    this.updateeMaterialMapping(supplierMaterial,hubSpuPending.getHubMaterial(), DataBusinessStatus.PUSH);
                }else{
                    this.updateeMaterialMapping(supplierMaterial,hubSpuPending.getHubMaterial(), DataBusinessStatus.NO_PUSH);
                }
            }
            //
        }
        return result;
    }

    /**
     * 获取需要刷新材质的商品
     * @return
     */
    public  List<HubSupplierSpuDto> getSupplierSpuList(){
        HubSupplierSpuCriteriaDto criteria = new HubSupplierSpuCriteriaDto();
        criteria.createCriteria().andInfoStateEqualTo(InfoState.RefreshMaterial.getIndex().byteValue());
        criteria.setPageNo(1);
        criteria.setPageSize(searchSupplierSpuNum);
        List<HubSupplierSpuDto> products = supplierSpuGateWay.selectByCriteria(criteria);
        if(products!=null&&products.size()>0){
            List<Long> supplierSpuIdList= new ArrayList<>();
            for(HubSupplierSpuDto supplierSpuDto : products){
                supplierSpuIdList.add(supplierSpuDto.getSupplierSpuId());
            }
            this.updateSupplierInfoState(supplierSpuIdList);
        }
        return products;
    }


    public void translateMaterial(List<HubSupplierSpuDto> supplierSpuList){
        try {
            int  i= 0;
            Map<Long,String> supplierMaterialMap = new HashMap<>();
            //每二百条处理一次
            List<Long> supplierSpuPendingIdList = new ArrayList<>();

            for(HubSupplierSpuDto supplierSpuDto:supplierSpuList){

                supplierMaterialMap.put(supplierSpuDto.getSupplierSpuId(),supplierSpuDto.getSupplierMaterial());

                if(i%199==0){
                    if(i!=0){
                        //翻译
                        List<HubSpuPendingDto>  spuPendingDtoList = this.searchMaterial(supplierSpuPendingIdList);
                        translateMaterial(spuPendingDtoList,supplierMaterialMap);
                        supplierSpuPendingIdList = new ArrayList<>();
                    }
                }
                supplierSpuPendingIdList.add(supplierSpuDto.getSupplierSpuId());
                i++;
            }
            //补不够200的查询
            if(supplierSpuPendingIdList.size()>0){

                List<HubSpuPendingDto>  spuPendingDtoList = this.searchMaterial(supplierSpuPendingIdList);
                //翻译
                translateMaterial(spuPendingDtoList,supplierMaterialMap);
            }
            log.info("翻译处理成功");

        } catch (Exception e) {
            log.error("update  error. reason :"+ e.getMessage(),e);

        }
    }


    private  List<HubSpuPendingDto>   searchMaterial(List<Long> supplierSpuIdList) throws Exception{

        HubSpuPendingCriteriaDto criterial = new  HubSpuPendingCriteriaDto();
        criterial.setPageSize(PAGE_SIZE);
        criterial.createCriteria().andSupplierSpuIdIn(supplierSpuIdList)
                .andMaterialStateEqualTo(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue()).andHubMaterialIsNotNull()
        .andHubMaterialNotEqualTo("");

        return spuPendingGateWay.selectByCriteria(criterial);

    }


    private void translateMaterial(List<HubSpuPendingDto> spuPendingDtoList,Map<Long,String> supplierMaterialMap) {
        if(null!=spuPendingDtoList&&spuPendingDtoList.size()>0){

            spuPendingDtoList.forEach(dto->{
                HubSpuPendingDto updateDto =new HubSpuPendingDto();
                updateDto.setSpuPendingId(dto.getSpuPendingId());
                if(this.translateMaterial(dto,supplierMaterialMap.get(dto.getSupplierSpuId()))){

                    updateDto.setMaterialState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
                }else{
                    updateDto.setMaterialState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
                }
                updateDto.setHubMaterial(dto.getHubMaterial());
                try {
                    spuPendingGateWay.updateByPrimaryKeySelective(updateDto);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        }
    }


    private boolean firstLevelTranslation(HubSpuPendingDto dto,String supplierMaterial) {

        String  hubMaterial = "";
        if(StringUtils.isNotBlank(supplierMaterial)){
            //判断是否需要翻译翻译

            hubMaterial = translationUtil.translate(supplierMaterial.toLowerCase());
            //插入到材质表中
            if (!RegexUtil.excludeLetter(hubMaterial)) {
                // 材质含有英文 返回false
                this.saveMaterialMapping(supplierMaterial,hubMaterial, DataBusinessStatus.NO_PUSH);

            } else {
                this.saveMaterialMapping(supplierMaterial,hubMaterial,DataBusinessStatus.PUSH);

            }

        }
        dto.setHubMaterial(hubMaterial);
        if (!RegexUtil.excludeLetter(hubMaterial)) {
            // 材质含有英文 返回false
            return false;
        } else {
            return true;
        }



    }

    private boolean firstLevelTranslation(PendingSpu spuDto, HubSpuPendingDto hubSpuPending) {

        String supplierMaterial = spuDto.getHubMaterial();
        if(StringUtils.isBlank(supplierMaterial)){
            //无值的不处理
            hubSpuPending.setMaterialState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            return false;
        }
        String hubMaterial = this.getHubMaterialOfFirstLevel(supplierMaterial);
        if(StringUtils.isBlank(hubMaterial)){
            //判断是否需要翻译翻译
            if(!properties.isTranslateAll()){
                if(null==supplierIdMap){
                    supplierIdMap = new HashMap<>();
                    String supplierId = properties.getSupplierId();
                    if(StringUtils.isNotBlank(supplierId)){
                        String[] ids = supplierId.split(",");
                        if(null!=ids&&ids.length>0){
                            for(String id:ids){
                                supplierIdMap.put(id,"");
                            }
                        }
                    }
                }
                if(supplierIdMap.containsKey(spuDto.getSupplierId())){
                    hubMaterial = translationUtil.translate(supplierMaterial.toLowerCase());

                }else{
                    //不需要翻译
                    hubMaterial = supplierMaterial;
                }

            }else{
                //全部翻译
                hubMaterial = translationUtil.translate(supplierMaterial.toLowerCase());
            }

            //插入到材质表中
            if (!RegexUtil.excludeLetter(hubMaterial)) {
                // 材质含有英文 返回false
                this.saveMaterialMapping(supplierMaterial,hubMaterial, DataBusinessStatus.NO_PUSH);
            } else {
                this.saveMaterialMapping(supplierMaterial,hubMaterial,DataBusinessStatus.PUSH);
            }
        }
        hubMaterial = filterChars(hubMaterial);
        hubSpuPending.setHubMaterial(hubMaterial);
        if (!RegexUtil.excludeLetter(hubMaterial)) {
            hubSpuPending.setMaterialState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());;
            return false;
        }else{
            hubSpuPending.setMaterialState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
            return true;
        }
    }

    private String filterChars(String hubMaterial) {
        hubMaterial = hubMaterial.replaceAll("％","%").replaceAll(" %","%").replaceAll("."," ")
        .replaceAll(","," ").replaceAll(";"," ").replaceAll("-"," ").replaceAll("\""," ")
                .replaceAll("（"," ").replaceAll("）"," ");
        return hubMaterial;
    }

    /**
     * 如果没有找到 返回"" 找到但未发布也返回"" 未找到的话 需要插入到材质映射中
     * @param supplierMaterial
     * @return
     */
    private String  getHubMaterialOfFirstLevel(String supplierMaterial){
        HubMaterialMappingCriteriaDto criteria = new HubMaterialMappingCriteriaDto();
        criteria.createCriteria().andSupplierMaterialEqualTo(supplierMaterial).andMappingLevelEqualTo((byte)1);

        List<HubMaterialMappingDto> hubMaterialMappingDtos = hubMaterialMappingGateWay.selectByCriteria(criteria);
        if(null!=hubMaterialMappingDtos&&hubMaterialMappingDtos.size()>0){
            HubMaterialMappingDto materialMappingDto = hubMaterialMappingDtos.get(0);
                return materialMappingDto.getHubMaterial();
        }else{
            return "";
        }
    }

    private void saveMaterialMapping(String supplierMaterial,String hubMaterial,DataBusinessStatus dataBusinessStatus){
        try {
            if(StringUtils.isBlank(supplierMaterial)||StringUtils.isBlank(hubMaterial)) return;
            HubMaterialMappingDto materialMapping = new HubMaterialMappingDto();
            materialMapping.setSupplierMaterial(supplierMaterial);
            materialMapping.setHubMaterial(hubMaterial);
            materialMapping.setCreateTime(new Date());
            materialMapping.setMappingLevel((byte)1);
            materialMapping.setSource(MaterialSourceEnum.GOOGLE_TRANSLATION.getIndex().byteValue());
            materialMapping.setDataState(dataBusinessStatus.getIndex().byteValue());
            materialMapping.setCreateUser(ConstantProperty.OPERATOR);
            Long insert = hubMaterialMappingGateWay.insert(materialMapping);
        } catch (Exception e) {
            log.error("save supplier material:"+ supplierMaterial + "  hub material :" + hubMaterial + "  mapping error:" + e.getMessage(),e);
            e.printStackTrace();
        }
    }


    private void updateeMaterialMapping(String supplierMaterial,String hubMaterial,DataBusinessStatus dataBusinessStatus){
        if(StringUtils.isBlank(supplierMaterial)||StringUtils.isBlank(hubMaterial)) return;

        HubMaterialMappingCriteriaDto criteria = new HubMaterialMappingCriteriaDto();
        criteria.createCriteria().andSupplierMaterialEqualTo(supplierMaterial).andMappingLevelEqualTo((byte)1);

        List<HubMaterialMappingDto> hubMaterialMappingDtos = hubMaterialMappingGateWay.selectByCriteria(criteria);
        if(null!=hubMaterialMappingDtos&&hubMaterialMappingDtos.size()>0){
            HubMaterialMappingDto materialMapping = new HubMaterialMappingDto();
            materialMapping.setMaterialMappingId(hubMaterialMappingDtos.get(0).getMaterialMappingId());
            materialMapping.setHubMaterial(hubMaterial);
            materialMapping.setUpdateTime(new Date());
            materialMapping.setDataState(dataBusinessStatus.getIndex().byteValue());
            materialMapping.setUpdateUser(ConstantProperty.OPERATOR);
            try {
                hubMaterialMappingGateWay.updateByPrimaryKeySelective(materialMapping);
            } catch (Exception e) {
                log.error("update  supplier material:"+ supplierMaterial + "  hub material :" + hubMaterial + "  mapping error:" + e.getMessage(),e);
                e.printStackTrace();
            }
        }

    }

    private boolean replaceMaterial(HubSpuPendingDto hubSpuPending) {
        //材质替换顺序：firstMaterialMap 全匹配 、secondMaterialMap 词组替换、threeMaterialMap单词替换

        Map<String, String> secondMaterialMap = pendingCommonHandler.getSecondMaterialMap();
//        Map<String, String> threeMaterialMap = pendingCommonHandler.getThreeMaterialMap();
        Map<String, String> replaceMaterialMap = pendingCommonHandler.getReplaceMaterialMap();


        String supplierMaterial = replace(hubSpuPending.getHubMaterial());
        if(StringUtils.isBlank(supplierMaterial)){
            //无值的不处理
            hubSpuPending.setMaterialState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            return false;
        }

        log.debug("after first translate material  =  " + supplierMaterial);
        //词组处理
        Set<String> secondMaterialSet = secondMaterialMap.keySet();
        for (String material : secondMaterialSet) {
            if (StringUtils.isNotBlank(supplierMaterial)&&supplierMaterial.toLowerCase().trim().contains(material)) {
                hubSpuPending.setHubMaterial(supplierMaterial.toLowerCase().trim().replaceAll(material, secondMaterialMap.get(material)).trim());
                supplierMaterial = hubSpuPending.getHubMaterial();
            }
        }


        //拆分材质，每个单词去替换中
        if(StringUtils.isNotBlank(supplierMaterial)){

            String[] words = this.splitString(supplierMaterial);
            if(null!=words&&words.length>0){
                supplierMaterial = getMatrial(words);
                hubSpuPending.setHubMaterial(supplierMaterial);
            }
        }

        //替换处理
        Set<String> replaceMaterialSet = replaceMaterialMap.keySet();
        for (String material : replaceMaterialSet) {
            if (StringUtils.isNotBlank(supplierMaterial)&&supplierMaterial.toLowerCase().trim().contains(material)) {
                hubSpuPending.setHubMaterial(supplierMaterial.toLowerCase().trim().replaceAll(material, "").trim());
                supplierMaterial = hubSpuPending.getHubMaterial();
            }
        }

        log.debug(" after 2,3 translate =" + hubSpuPending.getHubMaterial());

        if (!RegexUtil.excludeLetter(hubSpuPending.getHubMaterial())) {
            hubSpuPending.setMaterialState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            // 材质含有英文 返回false
            return false;
        } else {
            hubSpuPending.setMaterialState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
            return true;
        }

    }


    private void updateSupplierInfoState(Long  supplierSpuId) {
        HubSupplierSpuDto hubSupplierSpu = new HubSupplierSpuDto();
        hubSupplierSpu.setSupplierSpuId(supplierSpuId);
        hubSupplierSpu.setInfoState((byte)0);
        hubSupplierSpu.setUpdateTime(new Date());
        supplierSpuGateWay.updateByPrimaryKeySelective(hubSupplierSpu);
    }

    /**
     * 回复供货商数据的刷新状态
     * @param supplierSpuId
     */
    private void updateSupplierInfoState(List<Long>  supplierSpuId) {



        HubSupplierSpuCriteriaDto criteriaDto =new HubSupplierSpuCriteriaDto();
        criteriaDto.createCriteria().andSupplierSpuIdIn(supplierSpuId);

        HubSupplierSpuDto supplierSpuDto = new HubSupplierSpuDto();
        supplierSpuDto.setInfoState((byte)0);
        supplierSpuDto.setUpdateTime(new Date());
        HubSupplierSpuWithCriteriaDto criterial = new HubSupplierSpuWithCriteriaDto(supplierSpuDto,criteriaDto);

        supplierSpuGateWay.updateByCriteriaSelective(criterial);
    }




    public static String replace(String str) // 识别括号并将括号内容替换的函数
    {
        if(StringUtils.isBlank(str)){
            return str;
        }
        int head = str.indexOf('('); // 标记第一个使用左括号的位置
        if (head == -1)
            ; // 如果str中不存在括号，什么也不做，直接跑到函数底端返回初值str
        else {
            int next = head + 1; // 从head+1起检查每个字符
            int count = 1; // 记录括号情况
            do {
                if (str.charAt(next) == '(')
                    count++;
                else if (str.charAt(next) == ')')
                    count--;
                next++; // 更新即将读取的下一个字符的位置
                if (count == 0) // 已经找到匹配的括号
                {
                    String temp = str.substring(head, next); // 将两括号之间的内容及括号提取到temp中
                    str = str.replace(temp, ""); // 用空内容替换，复制给str
                    head = str.indexOf('('); // 找寻下一个左括号
                    next = head + 1; // 标记下一个左括号后的字符位置
                    count = 1; // count的值还原成1
                }
            } while (head != -1); // 如果在该段落中找不到左括号了，就终止循环
        }
        return str; // 返回更新后的str
    }


    private String getMatrial(String[] words) {
        String supplierMaterial;
        StringBuilder builder = new StringBuilder();


        for(String word:words){
            convertMaterial(builder, word,",");
        }
        supplierMaterial = builder.toString();
        return supplierMaterial;
    }


    /**
     * 非中文的需要判断前面是否加上空格
     * @param builder
     * @param word
     * @param symbol
     */
    private void convertMaterial(StringBuilder builder, String word,String symbol) {
        String material="";
        int i=0;
        i= word.indexOf(symbol);
        if(i==0){//开始含有符号
            builder.append(symbol);
            word = word.substring(1);
            material = pendingCommonHandler.getHubMaterialWordFromRedis(word);
            if(StringUtils.isBlank(material)){
                builder.append(word);
            }else{
                builder.append(material);
            }
        }else if(i>0){//中间或者结尾还有符号
            if(i==word.length()-1){//结尾
                word = word.substring(0,word.length()-1);
                material = pendingCommonHandler.getHubMaterialWordFromRedis(word);
                if(StringUtils.isBlank(material)){
                    if(this.isChinese(word)){

                        builder.append(word).append(symbol);
                    }else{
                        builder.append(" ").append(word).append(symbol);
                    }
                }else{
                    builder.append(material).append(symbol);
                }
            }else{//中间
                String[]  wordList = word.split(symbol);
                int j=1;
                for(String wd:wordList){
                    //material 有值 代表着映射的为中文 前面不需要空格
                    material = pendingCommonHandler.getHubMaterialWordFromRedis(wd);
                    if(StringUtils.isBlank(material)){
                        if(this.isChinese(word)) {
                            builder.append(word);
                        }else{
                            if(j==1){//第一个需要非中文的需要加空格

                                builder.append(" ").append(word);
                            }else{
                                builder.append(word);
                            }
                        }
                    }else{
                        builder.append(material);
                    }
                    if(j!=wordList.length){
                        builder.append(symbol);
                    }
                    j++;
                }

            }

        }else{
            material = pendingCommonHandler.getHubMaterialWordFromRedis(word);
            if(StringUtils.isBlank(material)){
                if(this.isChinese(word)) {
                    builder.append(word);
                }else{
                    builder.append(" ").append(word);
                }
            }else{
                builder.append(material);
            }
        }
    }


    //是否是中文
    private  boolean isChinese(String str) {
        String regex = "^[\u4e00-\u9fa5]+$";
        return str.matches(regex);
    }


    private   String[] splitString(String line){
        return  line.split("\\s+|\\t| ");

    }

    private void replaceExcludeLetter(){

    }
}

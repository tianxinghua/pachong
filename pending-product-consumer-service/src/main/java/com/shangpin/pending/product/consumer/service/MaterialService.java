package com.shangpin.pending.product.consumer.service;



import com.shangpin.ephub.client.data.mysql.mapping.dto.HubMaterialMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubMaterialMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubMaterialMappingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.ephub.client.util.RegexUtil;
import com.shangpin.pending.product.consumer.common.ConstantProperty;
import com.shangpin.pending.product.consumer.common.enumeration.DataBusinessStatus;
import com.shangpin.pending.product.consumer.common.enumeration.MaterialSourceEnum;
import com.shangpin.pending.product.consumer.common.enumeration.PropertyStatus;
import com.shangpin.pending.product.consumer.supplier.common.CommonProperties;
import com.shangpin.pending.product.consumer.supplier.common.PendingCommonHandler;
import com.shangpin.pending.product.consumer.supplier.common.TranslationUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by lizhongren on 2018/3/1.
 */
@Service
public class MaterialService {

    @Autowired
    CommonProperties properties;


    @Autowired
    private HubMaterialMappingGateWay hubMaterialMappingGateWay;

    @Autowired
    TranslationUtil translationUtil;

    @Autowired
    PendingCommonHandler pendingCommonHandler;


    static Map<String,String> supplierIdMap = null;

    /**
     * 先查询完全匹配的 ，如果能找到供货商材质的 直接替换,否则看是否是重点供货商，是翻译，否则不翻译
     * @param spu
     * @param hubSpuPending
     * @return
     */
    public boolean changeSupplierToHub(PendingSpu spu, HubSpuPendingDto hubSpuPending){

        //第一级匹配
        if(firstLevelTranslation(spu, hubSpuPending)){
            return true;
        }else{
            // 二级 词组替换 三级 单词替换
            return replaceMaterialByRedis(spu,hubSpuPending);
        }
    }

    private boolean firstLevelTranslation(PendingSpu spu, HubSpuPendingDto hubSpuPending) {

        String supplierMaterial = spu.getHubMaterial();
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
                if(supplierIdMap.containsKey(spu.getSupplierId())){
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
                this.saveMaterialToDB(supplierMaterial,hubMaterial, DataBusinessStatus.NO_PUSH);
            } else {
                this.saveMaterialToDB(supplierMaterial,hubMaterial,DataBusinessStatus.PUSH);
            }
        }

        hubSpuPending.setHubMaterial(hubMaterial);
        if (!RegexUtil.excludeLetter(hubMaterial)) {           ;
            return false;
        }else{
            hubSpuPending.setMaterialState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
            return true;
        }
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

    private void saveMaterialToDB(String supplierMaterial,String hubMaterial,DataBusinessStatus dataBusinessStatus){
        HubMaterialMappingDto materialMapping = new HubMaterialMappingDto();
        materialMapping.setSupplierMaterial(supplierMaterial);
        materialMapping.setHubMaterial(hubMaterial);
        materialMapping.setCreateTime(new Date());
        materialMapping.setMappingLevel((byte)1);
        materialMapping.setSource(MaterialSourceEnum.GOOGLE_TRANSLATION.getIndex().byteValue());
        materialMapping.setDataState(dataBusinessStatus.getIndex().byteValue());
        materialMapping.setCreateUser(ConstantProperty.OPERATOR);
        Long insert = hubMaterialMappingGateWay.insert(materialMapping);
    }

    private boolean replaceMaterialByRedis(PendingSpu spu, HubSpuPendingDto hubSpuPending) {
        //材质替换顺序：firstMaterialMap 全匹配 、secondMaterialMap 词组替换、threeMaterialMap单词替换

        Map<String, String> secondMaterialMap = pendingCommonHandler.getSecondMaterialMap();
        Map<String, String> threeMaterialMap = pendingCommonHandler.getThreeMaterialMap();
        Map<String, String> replaceMaterialMap = pendingCommonHandler.getReplaceMaterialMap();

        String supplierMaterial = replace(hubSpuPending.getHubMaterial());

        Set<String> secondMaterialSet = secondMaterialMap.keySet();
        for (String material : secondMaterialSet) {
            if (StringUtils.isNotBlank(supplierMaterial)&&supplierMaterial.toLowerCase().trim().contains(material)) {
                spu.setHubMaterial(supplierMaterial.toLowerCase().trim().replaceAll(material, secondMaterialMap.get(material)).trim());
                hubSpuPending.setHubMaterial(spu.getHubMaterial());
                supplierMaterial = spu.getHubMaterial();
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


        Set<String> replaceMaterialSet = replaceMaterialMap.keySet();
        for (String material : replaceMaterialSet) {
            if (StringUtils.isNotBlank(supplierMaterial)&&supplierMaterial.toLowerCase().trim().contains(material)) {
                spu.setHubMaterial(supplierMaterial.toLowerCase().trim().replaceAll(material, "").trim());
                hubSpuPending.setHubMaterial(spu.getHubMaterial());
                supplierMaterial = spu.getHubMaterial();
            }
        }

        if (!RegexUtil.excludeLetter(hubSpuPending.getHubMaterial())) {
            hubSpuPending.setMaterialState(PropertyStatus.MESSAGE_WAIT_HANDLE.getIndex().byteValue());
            // 材质含有英文 返回false
            return false;
        } else {
            hubSpuPending.setMaterialState(PropertyStatus.MESSAGE_HANDLED.getIndex().byteValue());
            return true;
        }

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
}

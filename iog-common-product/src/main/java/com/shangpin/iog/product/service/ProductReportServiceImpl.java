package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.dto.HubSupplierValueMappingDTO;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.SeasonRelationDTO;
import com.shangpin.iog.mongodao.PictureDAO;
import com.shangpin.iog.mongodomain.ProductPicture;
import com.shangpin.iog.product.dao.*;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductReportService;
import com.shangpin.iog.service.SeasonRelationService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by lizhongren on 2016/8/18.
 */
@Service
public class ProductReportServiceImpl implements ProductReportService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    EPRuleMapper ePRuleDAO;

    @Autowired
    ProductsMapper productDAO;

    @Autowired
    BrandSpMapper brandSpDAO;

    @Autowired
    ColorContrastMapper colorContrastDAO;

    @Autowired
    MaterialContrastMapper materialContrastDAO;

    @Autowired
    SupplierMapper supplierDAO;

    @Autowired
    PictureDAO pictureDAO;

    @Autowired
    SeasonRelationService seasonRelationService;

    @Autowired
    ProductFetchService pfs;

    @Autowired
    HubSupplierValueMappingMapper hubSupplierValueMappingService;

    @Override
    public Map<String,Integer> findProductReport() throws ServiceException {


        List<ProductDTO> productList =   productDAO.findReport();
        if(null==productList||productList.size()==0){
            logger.warn("未获得到数据");
            return new HashMap<>();
        }
        logger.warn("获取部分过滤的数据量为："+productList.size());
        Map<String,Integer> suppliercountMap = new HashMap<>();

        getNohandleCount(productList, suppliercountMap);


        logger.warn("获取过滤的数据量为："+suppliercountMap.toString());
        return suppliercountMap;
    }

    /**
     * 获取未处理的数量
     * @param productList
     * @param suppliercountMap
     * @throws ServiceException
     */
    private void getNohandleCount(List<ProductDTO> productList, Map<String, Integer> suppliercountMap) throws ServiceException {
        //获取季节map
        Map<String,String> reasonMap=new HashMap<>();
        List<SeasonRelationDTO> currentSeasonList  =   seasonRelationService.getAllCurrentSeason();
        for(SeasonRelationDTO dto:currentSeasonList){
            reasonMap.put(dto.getSupplierId()+"||"+(null==dto.getSupplierSeason()?"":dto.getSupplierSeason()),dto.getSpYear()+dto.getSpSeason());

        }
        logger.warn("映射的季节："+reasonMap.toString());

        //品牌
        List<String> brandList = new ArrayList<String>();
        for(String brand:ePRuleDAO.findAll(2, 1)){
            brandList.add(brand.toUpperCase());

        }
        logger.warn("需要的品牌："+brandList.toString());
        //品类 排除
        List<String> categeryList = new ArrayList<String>();
        for(String cat:ePRuleDAO.findAll(3, 0)){
            categeryList.add(cat.toUpperCase());

        }
        logger.warn("不需要的品类："+categeryList.toString());
        //季节 排除
        List<String> seasonList = new ArrayList<String>();
        for(String season:ePRuleDAO.findAll(5, 0)){
            seasonList.add(season.toUpperCase());

        }
        logger.warn("不需要的季节："+seasonList.toString());
        //性别 排除
        List<String> genderList = new ArrayList<String>();
        for(String gender:ePRuleDAO.findAll(6, 0)){
            genderList.add(gender.toUpperCase());

        }
        logger.warn("不需要的性别："+genderList.toString());


        String supplierName="", seasonKey = "", productName = "";

        String reasonKey ="",reasonViewName ="";
        Date today = DateTimeUtil.convertDateFormat(new Date(),"yyyy-MM-dd");
        String daykey = "";
        int diffDay = 0;

        for (ProductDTO dto : productList) {
            try {


                    if(StringUtils.isNotBlank(dto.getCategoryGender()) && !genderList.contains(dto.getCategoryGender().toUpperCase())){
//                        logger.warn("getCategoryGender");
                        if((StringUtils.isNotBlank(dto.getSeasonId()) && !seasonList.contains(dto.getSeasonId().toUpperCase())) || (StringUtils.isNotBlank(dto.getSeasonName()) && !seasonList.contains(dto.getSeasonName().toUpperCase()))){
//                            logger.warn("getSeasonId");
                            if((StringUtils.isNotBlank(dto.getCategoryName()) && !categeryList.contains(dto.getCategoryName().toUpperCase())) || (StringUtils.isNotBlank(dto.getSubCategoryName()) && !categeryList.contains(dto.getSubCategoryName().toUpperCase()))){
//                                logger.warn("getCategoryName");
                                if(null != dto.getBrandName() && (brandList.contains(dto.getBrandName().toUpperCase()) || dto.getBrandName().equals("Chloé") || dto.getBrandName().equals("Chloe'"))){
//                                    logger.warn("getBrandName");
                                    try {
                                        //supplier 供货商
                                        supplierName = dto.getSupplierName();
                                        //过滤图片
                                        List<ProductPicture> skuPictureList = pictureDAO
                                                .findDistinctProductPictureBySupplierIdAndSkuId(
                                                        dto.getSupplierId(), dto.getSkuId());
                                        if (skuPictureList.isEmpty()) {
                                            // 查询公共的图片
                                            List<ProductPicture> spuPictureList = pictureDAO
                                                    .findDistinctProductPictureBySupplierIdAndSpuIdAndSkuIdNull(
                                                            dto.getSupplierId(), dto.getSpuId());
                                            if (spuPictureList.isEmpty()) {
                                                logger.warn(dto.getSupplierId()+"----" + dto.getSkuId()+"---无图片");
                                               continue;
                                            }
                                        }
                                        logger.info(dto.getSupplierId()+"----" + dto.getSkuId()+"---有图片");
                                        //获取数据
                                        if(reasonMap.containsKey(dto.getSupplierId()+"||"+(null==dto.getSeasonId()?"":dto.getSeasonId()))){
                                            reasonKey =   dto.getSupplierId()+"||"+(null==dto.getSeasonId()?"":dto.getSeasonId());
                                            reasonViewName =  reasonMap.get(reasonKey);

                                        }else if (reasonMap.containsKey(dto.getSupplierId()+"||"+(null==dto.getSeasonName()?"":dto.getSeasonName()))){
                                            reasonKey =   dto.getSupplierId()+"||"+(null==dto.getSeasonName()?"":dto.getSeasonName());
                                            reasonViewName =  reasonMap.get(reasonKey);
                                        }else{
                                            continue;
                                        }

//                                        diffDay = DateTimeUtil.getDateifference(dto.getCreateTime(),today);

//                                        daykey = DateTimeUtil.shortFmt(dto.getCreateTime());
                                        if(suppliercountMap.containsKey(supplierName+"||"+reasonViewName)){
                                            suppliercountMap.put(supplierName+"||"+reasonViewName,suppliercountMap.get(supplierName+"||"+reasonViewName)+1);
                                        }else{
                                            suppliercountMap.put(supplierName+"||"+reasonViewName,1);
                                        }


                                    } catch (Exception e) {
                                        logger.debug(dto.getSkuId() + "拉取失败" + e.getMessage());
                                        continue;
                                    }
                                }
                            }
                        }
                    }

            } catch (Exception e) {
                logger.debug(dto.getSkuId() + "拉取失败" + e.getMessage());
                continue;
            }
        }
    }

    @Override
    public Map<String, Integer> findProductReport(String startDate, String endDate) throws ServiceException {
        Date start = null;
        Date end = null;
        if(StringUtils.isNotBlank(startDate)){
            start=DateTimeUtil.convertFormat(startDate+" 00:00:00","yyyy-MM-dd HH:mm:ss");
        }else{  //未指定时间 则统计当天新拉取的未处理的数据
            start = DateTimeUtil.convertFormat(DateTimeUtil.shortFmt(new Date()) +" 00:00:00","yyyy-MM-dd HH:mm:ss");
        }
        if(StringUtils.isNotBlank(endDate)){
            endDate = DateTimeUtil.convertFormat(DateTimeUtil.getAppointDayFromSpecifiedDay(DateTimeUtil.convertFormat(endDate,"yyyy-MM-dd"),1,"D"),"yyyy-MM-dd");
            end=DateTimeUtil.convertFormat(endDate+" 00:00:00","yyyy-MM-dd HH:mm:ss");
        }

        List<ProductDTO> productList =   productDAO.findReportBySupplierIdAndCreateTime(null,start,end);
        if(null==productList||productList.size()==0){
            logger.warn("未获得到数据");
            return new HashMap<>();
        }
        logger.warn("获取部分过滤的数据量为："+productList.size());


        Map<String,Integer> suppliercountMap = new HashMap<>();

        getNohandleCount(productList, suppliercountMap);


        logger.warn("获取过滤的数据量为："+suppliercountMap.toString());
        return suppliercountMap;
    }

    @Override
    public  Map<String,String> findPicture(Map<String,String> picMap ,String supplierId ,String startDate,String endDate ,String excludeSupplierId) throws ServiceException {
        Date start = null;
        Date end = null;
        if(StringUtils.isNotBlank(startDate)) start=DateTimeUtil.convertFormat(startDate+" 00:00:00","yyyy-MM-dd HH:mm:ss");
        if(StringUtils.isNotBlank(endDate)){
            endDate = DateTimeUtil.convertFormat(DateTimeUtil.getAppointDayFromSpecifiedDay(DateTimeUtil.convertFormat(endDate,"yyyy-MM-dd"),1,"D"),"yyyy-MM-dd");
            end=DateTimeUtil.convertFormat(endDate+" 00:00:00","yyyy-MM-dd HH:mm:ss");
        }
        Map<String,String> supplierDateMap = new HashMap<>();
        //排除的供货商
        Map<String,String> excludeSupplierMap  = new HashMap<>();
        if(StringUtils.isNotBlank(excludeSupplierId)){
             String[] supplierArray = excludeSupplierId.split(",");
             if(null!=supplierArray&&supplierArray.length>0){
                 for(String supplier:supplierArray){
                     excludeSupplierMap.put(supplier,"");
                 }
             }
        }
        if(StringUtils.isNotBlank(supplierId)){
            if(excludeSupplierMap.containsKey(supplierId)) return supplierDateMap;
        }
        List<ProductDTO> productList =   productDAO.findReportBySupplierIdAndCreateTime(supplierId,start,end);
        if(null==productList||productList.size()==0){
            logger.warn("未获得到数据");
            return new HashMap<>();
        }
        logger.warn("获取部分过滤的数据量为："+productList.size());

        Map<String,String > skuspuMap = new HashMap<>();

        for (ProductDTO productDTO : productList) {
            skuspuMap.put(productDTO.getSkuId(),productDTO.getSpuId());
        }

        //获取季节map
        Map<String,String> reasonMap=new HashMap<>();
        List<SeasonRelationDTO> currentSeasonList  =   seasonRelationService.getAllCurrentSeason();
        for(SeasonRelationDTO dto:currentSeasonList){
            reasonMap.put(dto.getSupplierId()+"||"+(null==dto.getSupplierSeason()?"":dto.getSupplierSeason()),dto.getSpYear()+dto.getSpSeason());

        }
        logger.warn("映射的季节："+reasonMap.toString());

        //品牌
        Map<String,String> brandMap = this.getHubBrandMap();
//        for(String brand:ePRuleDAO.findAll(2, 1)){
//            brandList.add(brand.toUpperCase());
//
//        }

        logger.warn("需要的品牌："+brandMap.toString());
        //品类 排除
        List<String> categeryList = new ArrayList<String>();
        for(String cat:ePRuleDAO.findAll(3, 0)){
            categeryList.add(cat.toUpperCase());

        }
        logger.warn("不需要的品类："+categeryList.toString());
        //季节 排除
        List<String> seasonList = new ArrayList<String>();
        for(String season:ePRuleDAO.findAll(5, 0)){
            seasonList.add(season.toUpperCase());

        }
        logger.warn("不需要的季节："+seasonList.toString());
        //性别 排除
        List<String> genderList = new ArrayList<String>();
        for(String gender:ePRuleDAO.findAll(6, 0)){
            genderList.add(gender.toUpperCase());

        }
        logger.warn("不需要的性别："+genderList.toString());





        int diffDay = 0;
        Map<String, String> findMap = null;
        Map<String,String> handledSpuPicMap = new HashMap<>();
        if(null==picMap) picMap = new HashMap<>();
        String spu= "",originSpu="",key="";
        String color = "",picMapKey="";
        for (ProductDTO dto : productList) {
            if(excludeSupplierMap.containsKey(dto.getSupplierId())) continue;
            try {
                //已处理过的SPU图片 不在处理
                if(handledSpuPicMap.containsKey(dto.getSpuId()+dto.getColor())) continue;
                if(StringUtils.isNotBlank(dto.getCategoryGender()) && !genderList.contains(dto.getCategoryGender().toUpperCase())){
//                        logger.warn("getCategoryGender");
                    if((StringUtils.isNotBlank(dto.getSeasonId()) && !seasonList.contains(dto.getSeasonId().toUpperCase())) || (StringUtils.isNotBlank(dto.getSeasonName()) && !seasonList.contains(dto.getSeasonName().toUpperCase()))){
//                            logger.warn("getSeasonId");
                        if((StringUtils.isNotBlank(dto.getCategoryName()) && !categeryList.contains(dto.getCategoryName().toUpperCase())) || (StringUtils.isNotBlank(dto.getSubCategoryName()) && !categeryList.contains(dto.getSubCategoryName().toUpperCase()))){
//                                logger.warn("getCategoryName");
                            if(null != dto.getBrandName() && (brandMap.containsKey(dto.getBrandName().toUpperCase()) || dto.getBrandName().equals("Chloé") || dto.getBrandName().equals("Chloe'"))){
//                                    logger.warn("getBrandName");
                                try {
                                    logger.info(dto.getSpuId() + " ---" + dto.getSkuId() +" 进入季节前验证");
                                    //获取数据
                                    logger.info(dto.getSupplierId()+"||"+null==dto.getSeasonId()?"":dto.getSeasonId()+ "  -------" + dto.getSupplierId()+"||"+(null==dto.getSeasonName()?"":dto.getSeasonName())+"--");
                                    if(reasonMap.containsKey(dto.getSupplierId()+"||"+(null==dto.getSeasonId()?"":dto.getSeasonId()).trim())){

                                    }else if (reasonMap.containsKey(dto.getSupplierId()+"||"+(null==dto.getSeasonName()?"":dto.getSeasonName()).trim())){

                                    }else{
                                        continue;
                                    }
                                    logger.info(dto.getSpuId() + " ---" + dto.getSkuId() +" 进入图片验证");
                                    findMap = pfs.findPictureBySupplierIdAndSkuIdOrSpuId(dto.getSupplierId(), dto.getSkuId(),null);
                                    if (null==findMap||findMap.size()<1) {
                                        findMap =pfs.findPictureBySupplierIdAndSkuIdOrSpuId(dto.getSupplierId(), null,dto.getSpuId());
                                    }
                                    if (null!=findMap&&findMap.size()>0) {

//                                        logger.warn( dto.getSpuId() + " ---" + dto.getSkuId() + "---- pic map size =" + findMap.size());
//
                                        for (Map.Entry<String, String> m : findMap.entrySet()) {

                                            if(skuspuMap.containsKey(m.getKey())){//sku
                                                try {
                                                    originSpu= skuspuMap.get(m.getKey());
                                                    spu = getBASE64(skuspuMap.get(m.getKey()));
                                                } catch (Exception e) {
                                                    logger.error("转码失败");
                                                    e.printStackTrace();
                                                }
                                            }else{//spu

                                                try {
                                                    originSpu= m.getKey();
                                                    spu = getBASE64(m.getKey());
                                                } catch (Exception e) {
                                                    logger.error("转码失败");
                                                    e.printStackTrace();
                                                }

                                            }

                                            picMapKey =  "SPID"+dto.getSupplierId()+"-"+spu+"-"+getBASE64(dto.getColor());
                                            picMap.put(picMapKey, m.getValue());
                                            key =dto.getSupplierId()+"|"+DateTimeUtil.convertFormat(dto.getLastTime(),"yyyy-MM-dd");
                                            if(supplierDateMap.containsKey(key)){
                                                supplierDateMap.put(key,supplierDateMap.get(key)+"|||||"+picMapKey);
                                            }else {
                                                supplierDateMap.put(key,picMapKey);
                                            }

                                            handledSpuPicMap.put(originSpu+dto.getColor(),"");
                                        }



                                    }else{
                                        logger.warn( dto.getSpuId() + " ---" + dto.getSkuId() + "---- 无图片" );
                                    }





                                } catch (Exception e) {
                                    logger.debug(dto.getSkuId() + "拉取失败" + e.getMessage());
                                    continue;
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                logger.debug(dto.getSkuId() + "拉取失败" + e.getMessage());
                continue;
            }
        }

        logger.warn("获取过滤的数据量为："+supplierDateMap.toString());
        return supplierDateMap;
    }
    public static String getBASE64(String s) {
        if (s == null) return null;
        return (new sun.misc.BASE64Encoder()).encode( s.getBytes() );
    }


    /**
     * 新的设置尚品品牌方法
     */
    private Map<String,String> getHubBrandMap(){
        Map<String,String> brandMap = new HashMap();
        try {

                List<HubSupplierValueMappingDTO> list = hubSupplierValueMappingService.findListBySpvalueType(1);
                for(HubSupplierValueMappingDTO dto : list){
                    brandMap.put(dto.getSupplierValue().toUpperCase(),"");
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return brandMap;

    }

}

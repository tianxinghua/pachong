package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.SeasonRelationDTO;
import com.shangpin.iog.mongodao.PictureDAO;
import com.shangpin.iog.mongodomain.ProductPicture;
import com.shangpin.iog.product.dao.*;
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


    @Override
    public Map<String,Integer> findProductReport() throws ServiceException {


        List<ProductDTO> productList =   productDAO.findReport();
        if(null==productList||productList.size()==0){
            logger.warn("未获得到数据");
            return new HashMap<>();
        }
        logger.warn("获取部分过滤的数据量为："+productList.size());

        //获取季节map
        Map<String,String> reasonMap=new HashMap<>();
        List<SeasonRelationDTO> currentSeasonList  =   seasonRelationService.getAllCurrentSeason();
        for(SeasonRelationDTO dto:currentSeasonList){
            reasonMap.put(dto.getSupplierId()+"||"+dto.getSupplierSeason(),dto.getSpYear()+dto.getSpSeason());

        }
        //品牌
        List<String> brandList = new ArrayList<String>();
        for(String brand:ePRuleDAO.findAll(2, 1)){
            brandList.add(brand.toUpperCase());
        }
        //品类 排除
        List<String> categeryList = new ArrayList<String>();
        for(String cat:ePRuleDAO.findAll(3, 0)){
            categeryList.add(cat.toUpperCase());
        }
        //季节 排除
        List<String> seasonList = new ArrayList<String>();
        for(String season:ePRuleDAO.findAll(5, 0)){
            seasonList.add(season.toUpperCase());
        }
        //性别 排除
        List<String> genderList = new ArrayList<String>();
        for(String gender:ePRuleDAO.findAll(6, 0)){
            genderList.add(gender.toUpperCase());
        }

        String productSize, productDetail = "", brandName = "", brandId = "", color = "", material = "", productOrigin = "";

        String supplierName="", categoryName = "", productName = "";
        Map<String,Integer> suppliercountMap = new HashMap<>();
        String reasonKey ="",reasonViewName ="";
        for (ProductDTO dto : productList) {
            try {

                if(StringUtils.isBlank(dto.getSpSkuId()) && StringUtils.isNotBlank(dto.getColor()) && StringUtils.isNotBlank(dto.getSize()) && StringUtils.isNotBlank(dto.getMaterial()) ){
                    if(StringUtils.isNotBlank(dto.getCategoryGender()) && !genderList.contains(dto.getCategoryGender().toUpperCase())){
                        if((StringUtils.isNotBlank(dto.getSeasonId()) && !seasonList.contains(dto.getSeasonId().toUpperCase())) || (StringUtils.isNotBlank(dto.getSeasonName()) && !seasonList.contains(dto.getSeasonName().toUpperCase()))){
                            if((StringUtils.isNotBlank(dto.getCategoryName()) && !categeryList.contains(dto.getCategoryName().toUpperCase())) || (StringUtils.isNotBlank(dto.getSubCategoryName()) && !categeryList.contains(dto.getSubCategoryName().toUpperCase()))){
                                if(null != dto.getBrandName() && (brandList.contains(dto.getBrandName().toUpperCase()) || dto.getBrandName().equals("Chloé") || dto.getBrandName().equals("Chloe'"))){
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
                                               continue;
                                            }
                                        }

                                        //获取数据
                                        if(reasonMap.containsKey(dto.getSupplierId()+"||"+dto.getSeasonId())){
                                            reasonKey =   dto.getSupplierId()+"||"+dto.getSeasonId();
                                            reasonViewName =  reasonMap.get(reasonKey);

                                        }else if (reasonMap.containsKey(dto.getSupplierId()+"||"+dto.getSeasonName())){
                                            reasonKey =   dto.getSupplierId()+"||"+dto.getSeasonName();
                                            reasonViewName =  reasonMap.get(reasonKey);
                                        }else{
                                            continue;
                                        }

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
                }
            } catch (Exception e) {
                logger.debug(dto.getSkuId() + "拉取失败" + e.getMessage());
                continue;
            }
        }

        logger.warn("获取过滤的数据量为："+suppliercountMap.toString());
        return suppliercountMap;
    }
}

package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.dto.*;
import com.shangpin.iog.product.dao.*;
import com.shangpin.iog.service.ProductSearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by loyalty on 15/5/20.
 */
@Service
public class ProductSearchServiceImpl implements ProductSearchService {
    protected  final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SkuMapper skuDAO;

    @Autowired
    SpuMapper spuDAO;

    @Autowired
    ProductPictureMapper picDAO;

    @Autowired
    ProductsMapper productDAO;

    @Autowired
    BrandSpMapper brandSpDAO;

    @Autowired
    ColorContrastMapper colorContrastDAO;

    @Autowired
   MaterialContrastMapper materialContrastDAO;




    private static     Map<String,String> spBrandMap = new HashMap<>();
    private static     Map<String,String> colorContrastMap = new HashMap<>();

    private static     Map<String,String> materialContrastMap = new HashMap<>();

    //key 均为小写 以便匹配
    private static Map<String,String>  cityMap= new HashMap<String,String>(){
        {
            put("italy","意大�?");
            put("america","美国");
            put("england","英国");
            put("canada","加拿�?");
            put("brazil","巴西");
            put("argentina","阿根�?");
            put("mexico","墨西�?");
            put("germany","德国");
            put("france","法国");
            put("russia","俄罗�?");
            put("japan","日本");
            put("australia","澳大利亚");
            put("korea","韩国");
            put("china","中国");
            put("finland","芬兰");
            put("switzerland","瑞士");
            put("sweden","瑞典");
            put("singapore","新加�?");
            put("thailand","泰国");
            put("new Zealand","新西�?");
            put("ireland","爱尔�?");
        }
    };


    @Override
    public Page<ProductDTO> findProductPageBySupplierAndTime(String supplier, Date startDate, Date endDate, Integer pageIndex, Integer pageSize) throws ServiceException {
        List<ProductDTO> productList = null;
        Page<ProductDTO> page  =  null;
        try {
            if(null!=pageIndex&&null!=pageSize){
                page = new Page<>(pageIndex,pageSize);
                productList = productDAO.findListBySupplierAndLastDate(supplier,startDate,endDate, new RowBounds(pageIndex, pageSize));


            }else{
                productList = productDAO.findListBySupplierAndLastDate(supplier, startDate, endDate);
                page = new Page<>(1,productList.size());
            }

            for(ProductDTO dto :productList){

                if(null!=dto.getSupplierId()&&null!=dto.getSkuId()) {
                    List<ProductPictureDTO> picList = picDAO.findBySupplierAndSku(dto.getSupplierId(), dto.getSkuId());
                    this.setPic(dto,picList);
                }


            }
            page.setItems(productList);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
            throw new ServiceMessageException("数据导出失败");
        }

        return page;
    }

    @Override
    public List<ProductDTO> findProductListBySupplierAndTime(String supplier, Date startDate, Date endDate) throws ServiceException {


        List<ProductDTO> productList = productDAO.findListBySupplierAndLastDate(supplier, startDate, endDate);
        for(ProductDTO dto :productList){

            if(null!=dto.getSupplierId()&&null!=dto.getSkuId()) {
                List<ProductPictureDTO> picList = picDAO.findBySupplierAndSku(dto.getSupplierId(), dto.getSkuId());
                this.setPic(dto,picList);
            }


        }
        return productList;
    }



    @Override
    public StringBuffer exportProduct(String supplier, Date startDate, Date endDate, Integer pageIndex, Integer pageSize) throws ServiceException {
        StringBuffer buffer = new StringBuffer("CategoryName品类名称," +
                "CategroyNo品类编号,BrandNo品牌编号,BrandName品牌,ProductModel货号,SupplierSkuNo供应商SkuNo," +
                " 性别 ,"+
                "SopProductName 商品名称,BarCode 条形�?,ProductColor 颜色,ProductSize 尺码,material 材质,ProductOrigin 产地,productUrl1," +
                "productUrl2,productUrl3,productUrl4,productUrl5,productUrl6,productUrl7,productUrl8,productUrl9," +
                "PcDesc 描述,Stock 库存,Price 进货�?,Currency 币种,上市季节").append("\r\n");
        Page<ProductDTO> page = this.findProductPageBySupplierAndTime(supplier, startDate, endDate, pageIndex, pageSize);

        //设置尚品网品�?
        this.setBrandMap();
        //颜色Map赋�?
        this.setColorContrastMap();
        //材质Map 赋�?
        this.setMaterialContrastMap();

        String productSize,season="", productDetail="",brandName="",brandId="",color="",material="",productOrigin="";

        String categoryId="",categoryName="",productName="";
        for(ProductDTO dto:page.getItems()){

            try {
                //品类名称
                categoryName= dto.getSubCategoryName();
                if(StringUtils.isBlank(categoryName)){
                    categoryName = dto.getCategoryName();
                }
                if(StringUtils.isBlank(categoryName)){
                    categoryName= "";
                }else{
                    categoryName= categoryName.replace(",","... ");
                }


                buffer.append(categoryName).append(",");

                categoryId = dto.getSubCategoryId();
                if(StringUtils.isBlank(categoryId)){
                    categoryId = dto.getCategoryId();
                }

                buffer.append(StringUtils.isNotBlank(categoryId)?categoryId :"品类编号").append(",");
                //品牌
                brandName=dto.getBrandName();
                if(spBrandMap.containsKey(brandName.toLowerCase())){
                    brandId=spBrandMap.get(brandName.toLowerCase());
                }else{
                    brandId ="";
                }

                buffer.append(!"".equals(brandId)?brandId :"品牌编号").append(",");
                buffer.append(brandName).append(",");
                //货号
                buffer.append(dto.getProductCode()).append(",").append(dto.getSkuId()).append(",");
                //欧洲习惯 第一个先�? 男女
                buffer.append(dto.getCategoryGender()).append(",");
                //产品名称
                productName =   dto.getProductName();
                if(StringUtils.isBlank(productName)){
                    productName = dto.getSpuName();
                }
                buffer.append(productName).append(",");


                buffer.append("\"\t" + dto.getBarcode() + "\"").append(",");

                //获取颜色
                color =dto.getColor();
                if(colorContrastMap.containsKey(color.toLowerCase())){
                    color=colorContrastMap.get(color.toLowerCase());
                }
                buffer.append(color).append(",");

                //获取尺码
                productSize=dto.getSize();
                if(StringUtils.isNotBlank(productSize)){

                    if(productSize.indexOf("+")>0){
                        productSize=productSize.replace("+",".5");
                    }

                }else{
                    productSize="";
                }


                buffer.append(productSize).append(",");

                //获取材质
                material =dto.getMaterial();
                if(null==material) {
                    material="";
                }else{

                     Set<Map.Entry<String,String>> materialSet =  materialContrastMap.entrySet();
                    for(Map.Entry<String,String> entry:materialSet) {

                        material = material.toLowerCase().replaceAll(entry.getKey(), entry.getValue()).replaceAll(",", "...");
                    }

                }

                buffer.append(material).append(",");



                //获取产地
                productOrigin = dto.getProductOrigin();
                if (cityMap.containsKey(productOrigin.toLowerCase())){
                    productOrigin=cityMap.get(productOrigin.toLowerCase());
                }
            
                buffer.append(productOrigin).append(",").append(dto.getPicUrl()).append(",");
                buffer.append(dto.getItemPictureUrl1()).append(",").append(dto.getItemPictureUrl2()).append(",").append(dto.getItemPictureUrl3()).append(",")
                        .append(dto.getItemPictureUrl4()).append(",").append(dto.getItemPictureUrl5()).append(",")
                        .append(dto.getItemPictureUrl6()).append(",").append(dto.getItemPictureUrl7()).append(",")
                        .append(dto.getItemPictureUrl8()).append(",");
                //明细描述
                productDetail = dto.getProductDescription();
                if(StringUtils.isNotBlank(productDetail)&&productDetail.indexOf(",")>0){
                    productDetail = productDetail.replace(",","...");
                }

                buffer.append(productDetail).append(",");


                buffer.append(dto.getStock()).append(",")
                        .append(dto.getSupplierPrice()).append(",").append("").append(",");
                //季节


                buffer.append(null==dto.getSeasonName()?dto.getSeasonId():dto.getSeasonName());

                buffer.append("\r\n");
            } catch (Exception e) {
                logger.debug(dto.getSkuId()+"拉取失败"+  e.getMessage());
                continue;
            }

        }
        return buffer ;
    }

    /**
     * 设置map
     */
    private  void setBrandMap(){

            int num = brandSpDAO.findCount();
            if(spBrandMap.size() < num){
                List<BrandSpDTO> brandSpDTOList = null;
                try {
                    brandSpDTOList = brandSpDAO.findAll();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return;
                }
                for(BrandSpDTO dto:brandSpDTOList){
                    spBrandMap.put(dto.getBrandName().toLowerCase(),dto.getBrandId());
                }
            }
    }

    /**
     * 设置colorContrastMap
     */
    private void setColorContrastMap() {
        int num = colorContrastDAO.findCount();
        if(colorContrastMap.size() < num){
            List<ColorContrastDTO> colorContrastDTOList = null;

            try {
                colorContrastDTOList = colorContrastDAO.findAll();
            } catch (Exception e) {
                e.printStackTrace();
                return ;
            }

            for(ColorContrastDTO dto:colorContrastDTOList){
                colorContrastMap.put(dto.getColor().toLowerCase(),dto.getColorCh());
            }
        }
    }


    /**
     * 设置materialContrastMap
     */
    private  void setMaterialContrastMap() {
        int num =materialContrastDAO.findCount();
        if(materialContrastMap.size() < num){
            List<MaterialContrastDTO> materialContrastDTOList = null;

            try {
                materialContrastDTOList = materialContrastDAO.findAll();
            } catch (Exception e) {
                e.printStackTrace();
                return ;
            }

            for(MaterialContrastDTO dto:materialContrastDTOList){
                materialContrastMap.put(dto.getMaterial().toLowerCase(),dto.getMaterialCh());
            }
        }
    }


    /**
     * 图片赋�?
     * @param dto
     * @param picList
     */
    private void setPic(ProductDTO dto,List<ProductPictureDTO> picList){
        if(null!=picList&&!picList.isEmpty()){
            Boolean isHavePic=true;
            //如果原始无图�?  则从picUrl开始赋�? 赋值为picList的第一张图�?
            if(StringUtils.isBlank(dto.getPicUrl())){
                isHavePic=false;
            }
            for(int i=0;i<picList.size();i++){
                switch (i){
                    case 0:
                        if(isHavePic){
                            dto.setItemPictureUrl1(picList.get(i).getPicUrl());
                        }else{
                            dto.setPicUrl(picList.get(i).getPicUrl());
                        }
                        break;
                    case 1:

                        if(isHavePic){
                            dto.setItemPictureUrl2(picList.get(i).getPicUrl());
                        }else{
                            dto.setItemPictureUrl1(picList.get(i).getPicUrl()) ;
                        }

                        break;
                    case 2:
                        if(isHavePic){
                            dto.setItemPictureUrl3(picList.get(i).getPicUrl());
                        }else{
                            dto.setItemPictureUrl2(picList.get(i).getPicUrl()) ;
                        }
                        break;
                    case 3:
                        if(isHavePic){
                            dto.setItemPictureUrl4(picList.get(i).getPicUrl());
                        }else{
                            dto.setItemPictureUrl3(picList.get(i).getPicUrl()) ;
                        }
                        break;
                    case 4:
                        if(isHavePic){
                            dto.setItemPictureUrl5(picList.get(i).getPicUrl());
                        }else{
                            dto.setItemPictureUrl4(picList.get(i).getPicUrl()) ;
                        }
                        break;
                    case 5:
                        if(isHavePic){
                            dto.setItemPictureUrl6(picList.get(i).getPicUrl());
                        }else{
                            dto.setItemPictureUrl5(picList.get(i).getPicUrl()) ;
                        }
                        break;
                    case 6:
                        if(isHavePic){
                            dto.setItemPictureUrl7(picList.get(i).getPicUrl());
                        }else{
                            dto.setItemPictureUrl6(picList.get(i).getPicUrl()) ;
                        }
                        break;
                    case 7:
                        if(isHavePic){
                            dto.setItemPictureUrl8(picList.get(i).getPicUrl());
                        }else{
                            dto.setItemPictureUrl7(picList.get(i).getPicUrl()) ;
                        };
                        break;


                }
            }
        }

    }

}

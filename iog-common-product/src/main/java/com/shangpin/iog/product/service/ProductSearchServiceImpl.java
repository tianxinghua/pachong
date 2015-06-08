package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SpinnakerProductDTO;
import com.shangpin.iog.product.dao.ProductPictureMapper;
import com.shangpin.iog.product.dao.ProductsMapper;
import com.shangpin.iog.product.dao.SkuMapper;
import com.shangpin.iog.product.dao.SpuMapper;
import com.shangpin.iog.service.ProductSearchService;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
                " type ,"+
                "SopProductName商品名称,BarCode条形码,ProductColor颜色,ProductSize尺码,Materia材质,ProductOrigin产地,productUrl1," +
                "productUrl2,productUrl3,productUrl4,productUrl5,productUrl6,productUrl7,productUrl8,productUrl9," +
                "PcDesc PC端描述,MDesc 手机端描述,Stock 库存,Price 进货价,Currency 币种,上市季节").append("\r\n");
        Page<ProductDTO> page = this.findProductPageBySupplierAndTime(supplier, startDate, endDate, pageIndex, pageSize);
        String productSize,season="", productDetail="",brandId="";
        for(ProductDTO dto:page.getItems()){

//            try {
//                //处理图片
//                dto.setItemPic();
//                buffer.append(dto.getCategory()).append(",").append("品类编号").append(",");
//                //品牌
//                brandId=dto.getProducerId().trim();
////                if(brandMap.containsKey(brandId)){
////                    brandId=brandMap.get(brandId);
////                }else{
////                    brandId ="";
////                }
//                buffer.append(brandId).append(",");
//                buffer.append(dto.getProducerId()).append(",");
//                //货号
//                buffer.append(dto.getProductName() + " " + dto.getColor()).append(",").append(dto.getItemId()).append(",");
//                //欧洲习惯 第一个先看 男女
//                buffer.append(dto.getType()).append(",");
//                //产品名称
//                buffer.append(dto.getDescription()).append(",");
//                buffer.append("\"\t" + dto.getBarcode() + "\"").append(",").append(dto.getColor()).append(",");
//                //获取尺码
//                productSize=dto.getItemSize();
//                if(StringUtils.isNotBlank(productSize)){
//                    productSize=productSize.substring(0,dto.getItemSize().length()-4);
//                    if(productSize.indexOf("+")>0){
//                        productSize=productSize.replace("+",".5");
//                    }
//
//                }else{
//                    productSize="";
//                }
//
//                buffer.append(productSize).append(",");
//
//
//                //明细描述
//                productDetail = dto.getProductDetail();
//                if(StringUtils.isNotBlank(productDetail)&&productDetail.indexOf(",")>0){
//                    productDetail = productDetail.replace(",",".");
//                }
//
//
//                buffer.append(productDetail).append(",")
//                        .append(productDetail).append(",").append(dto.getUrl()).append(",");
//                buffer.append(dto.getItemPictureUrl1()).append(",").append(dto.getItemPictureUrl2()).append(",").append(dto.getItemPictureUrl3()).append(",")
//                        .append(dto.getItemPictureUrl4()).append(",").append(dto.getItemPictureUrl5()).append(",")
//                        .append(dto.getItemPictureUrl6()).append(",").append(dto.getItemPictureUrl7()).append(",")
//                        .append(dto.getItemPictureUrl8()).append(",").append(dto.getDescription()).append(",");
//
//                buffer.append(productDetail).append(",");
//                buffer.append(dto.getStock()).append(",")
//                        .append(dto.getSupplyPrice()).append(",").append("").append(",");
//                //季节
//
//                if(StringUtils.isNotBlank(dto.getSeason())){
////                    if(seasonMap.containsKey(dto.getSeason())){
////                        season = seasonMap.get(dto.getSeason());
////                    }else{
////                        season = "";
////                    }
//                }else{
//                    season = "";
//                }
//                buffer.append(season);
//
//                buffer.append("\r\n");
//            } catch (Exception e) {
//                logger.debug(dto.getItemId()+"拉取失败"+  e.getMessage());
//                continue;
//            }

        }
        return buffer ;
    }


    private void setPic(ProductDTO dto,List<ProductPictureDTO> picList){
        if(null!=picList&&!picList.isEmpty()){
            for(int i=0;i<picList.size();i++){
                switch (i){
                    case 0:
                        dto.setItemPictureUrl1(picList.get(i).getPicUrl());
                        break;
                    case 1:
                        dto.setItemPictureUrl2(picList.get(i).getPicUrl());
                        break;
                    case 2:
                        dto.setItemPictureUrl3(picList.get(i).getPicUrl());
                        break;
                    case 3:
                        dto.setItemPictureUrl4(picList.get(i).getPicUrl());
                        break;
                    case 4:
                        dto.setItemPictureUrl5(picList.get(i).getPicUrl());
                        break;
                    case 5:
                        dto.setItemPictureUrl6(picList.get(i).getPicUrl());
                        break;
                    case 6:
                        dto.setItemPictureUrl7(picList.get(i).getPicUrl());
                        break;
                    case 7:
                        dto.setItemPictureUrl8(picList.get(i).getPicUrl());
                        break;


                }
            }
        }

    }

}

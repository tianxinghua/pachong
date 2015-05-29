package com.shangpin.iog.spinnaker.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.common.utils.InVoke;
import com.shangpin.iog.common.utils.excel.AccountsExcelTemplate;
import com.shangpin.iog.dto.SpinnakerProductDTO;
import com.shangpin.iog.service.ProductService;
import com.shangpin.iog.spinnaker.dao.ProductsMapper;
import com.shangpin.iog.spinnaker.domain.Product;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by loyalty on 15/5/20.
 */
@Service
public class ProductServiceImpl implements ProductService {
    protected  final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ProductsMapper productsDAO;



    @Override
    public void fetchProduct() throws ServiceException {




    }
    @Override
    public Page<SpinnakerProductDTO> findProduct(String category,Date startDate, Date endDate, Integer pageIndex, Integer pageSize) throws ServiceException {




        List<SpinnakerProductDTO> productList = null;
        Page<SpinnakerProductDTO> page  =  null;
        try {
            if(null!=pageIndex&&null!=pageSize){
                page = new Page<>(pageIndex,pageSize);
                productList = productsDAO.findListByCategoryAndLastDate(category,startDate,endDate, new RowBounds(pageIndex, pageSize));


            }else{
                productList = productsDAO.findListByCategoryAndLastDate(category,startDate,endDate);
                page = new Page<>(1,productList.size());
            }

            for(SpinnakerProductDTO dto :productList){


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
    public AccountsExcelTemplate exportProduct(String templatePath ,String category,Date startDate, Date endDate, Integer pageIndex, Integer pageSize) throws ServiceException {

        try {
            AccountsExcelTemplate template = AccountsExcelTemplate.newInstance(templatePath);

            Page<SpinnakerProductDTO> page = this.findProduct(category, startDate, endDate, pageIndex, pageSize);
            int count =0;
            for(SpinnakerProductDTO dto:page.getItems()){
//                template.setCellStyle("STYLE_1");
                template.createRow(count);
                template.createCell("");//商品品类
                template.createCell("");//商品品类编号
                template.createCell("");// 品牌编号
                template.createCell("");// 品牌名称
                template.createCell(dto.getProducerId());//货号
                template.createCell(dto.getItemId());//供应商sku

                template.createCell(dto.getProductName());//商品名称
                template.createCell(dto.getBarcode());//条形码
                template.createCell(dto.getColor());//颜色
                template.createCell("");//尺码
                template.createCell("");//材质
                template.createCell("");//产地
                template.createCell(dto.getUrl());//图片
                dto.setItemPic();
                template.createCell(dto.getItemPictureUrl1());
                template.createCell(dto.getItemPictureUrl2());
                template.createCell(dto.getItemPictureUrl3());
                template.createCell(dto.getItemPictureUrl4());
                template.createCell(dto.getItemPictureUrl5());
                template.createCell(dto.getItemPictureUrl6());
                template.createCell(dto.getItemPictureUrl7());
                template.createCell(dto.getItemPictureUrl8());
                template.createCell("");//PC端描述
                template.createCell("");//移动端描述
                template.createCell(dto.getStock());//库存
                template.createCell(dto.getSupplyPrice());//进货价
                template.createCell("");//币种




                count++;
            }
            return template;
        } catch (ServiceException e) {
            throw e;
        }

    }
}

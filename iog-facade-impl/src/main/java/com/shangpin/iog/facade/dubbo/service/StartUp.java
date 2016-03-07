package com.shangpin.iog.facade.dubbo.service;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.facade.dubbo.dto.ProductDTO;
import com.shangpin.iog.facade.dubbo.dto.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.math.BigDecimal;

/**
 * Created by huxia on 2015/9/22.
 */
public class StartUp {
    static Logger logger = LoggerFactory.getLogger(StartUp.class);

    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String args[]) throws ServiceException {
        logger.info("加载Spring");
        loadSpringContext();
        logger.info("保存dubbo数据");
        ProductionService productionService = (ProductionService)factory.getBean("productionFacadeServiceImpl");
        ProductDTO productDTO = new ProductDTO();
        productDTO.setSpuId("3");
        productDTO.setSupplierId("2015999999");
        productDTO.setSkuId("123456");
        productDTO.setCategoryName("shirt");//不能为空
        productDTO.setBrandName("victora");//不能为空
        productDTO.setMaterial("coal");
        productDTO.setProductName("cloth");//可以为空
        productDTO.setMarketPrice(new BigDecimal(120));
        productDTO.setSupplierPrice(new BigDecimal(102));
        productDTO.setStock(5);
        productDTO.setProductCode("1003kdkdk");//可以为空
        productDTO.setColor("red");//不能为空
        productDTO.setSaleCurrency("US");//不能为空
        productDTO.setSize("6");//不能为空
        productDTO.setSkuPicture("http://1.jpg");//不能为空
        productDTO.setSpuPicture("http://1.jpg");//不能为空
        productionService.saveProduct(productDTO);
        logger.info("保存完成dubbo数据");
        System.exit(0);
    }
}

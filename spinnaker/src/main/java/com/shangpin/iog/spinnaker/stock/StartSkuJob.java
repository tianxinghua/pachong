package com.shangpin.iog.spinnaker.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.product.service.ProductFetchServiceImpl;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.spinnaker.stock.dto.*;
import com.shangpin.iog.spinnaker.stock.service.FetchProduct;
import com.shangpin.iog.spinnaker.stock.service.FrameFetchProduct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/8.
 */

public class StartSkuJob {

    private static Logger log = Logger.getLogger("info");

    private static ApplicationContext factory;
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args) throws Exception {
        //加载spring
        log.info("----拉取spinnaker数据开始----");
        loadSpringContext();
        log.info("----初始SPRING成功----");
        System.out.println("开始拉取:");
        //拉取数据
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("spinnaker");
        //FrameFetchProduct fetchProduct =(FrameFetchProduct)factory.getBean("frameSpinnaker");
        try {
            fetchProduct.fetchProductAndSave();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("----拉取spinnaker数据完成----");
        System.out.println("-------fetch end---------");
        System.exit(0);
    }

}

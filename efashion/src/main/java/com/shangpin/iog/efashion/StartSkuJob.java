package com.shangpin.iog.efashion;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.efashion.dao.ReturnObject;
import com.shangpin.iog.efashion.service.FetchProduct;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by zhaogenchun on 2015/9/25.
 */
public class StartSkuJob {

    private static Logger log = Logger.getLogger("info");

    private static ApplicationContext factory;
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    public static void main(String[] args){
    	
    	TempObj oo = new TempObj();
    	List list = new ArrayList();
    	Pararm p = new Pararm();
    	p.setMes("30004162002");
    	list.add(p);
    	oo.setList(list);
    	
    	
    	for(Pararm pp : oo.getList()){
    		String json = HttpUtil45.get("http://192.168.1.105:8082/ListingCatalog/getPicListBySkuNoList?skuNoList="+pp.getMes(),new OutTimeConfig(), null);
        	PictureObj obj = new Gson().fromJson(json, PictureObj.class);
        	pp.setPic(obj.getContent().getList().get(0).getPicUrl());
    	}
    	
        //鍔犺浇spring
        loadSpringContext();
        log.info("--------spring初始化成功------");
        //鎷夊彇鏁版嵁
        log.info("----拉取数据开始----");              
        System.out.println("-------fetch start---------");
        FetchProduct fetchProduct =(FetchProduct)factory.getBean("efashion");
        fetchProduct.fetchProductAndSave();
        log.info("----拉取数据结束----");
        System.out.println("-------fetch end---------");
        System.exit(0);
    }
    
}

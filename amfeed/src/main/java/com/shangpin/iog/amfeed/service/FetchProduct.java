package com.shangpin.iog.amfeed.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.shangpin.iog.service.ProductFetchService;

import java.util.ResourceBundle;

/**
 * Created by wangyuzhi on 2015/11/09.
 */
@Component("amfeed")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static ResourceBundle bdl=null;
    private static String supplierId;

    @Autowired
    private ProductFetchService pfs;

    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
            supplierId = bdl.getString("supplierId");
    }

    public void fetchProductAndSave(){

    }

}

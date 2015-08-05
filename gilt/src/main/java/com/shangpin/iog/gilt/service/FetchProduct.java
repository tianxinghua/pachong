package com.shangpin.iog.gilt.service;

import com.shangpin.iog.service.ProductFetchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sunny on 2015/8/5.
 */
@Component("gilt")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    ProductFetchService productFetchService;
    String supplierId = "";//
}

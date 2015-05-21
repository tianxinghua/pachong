package com.shangpin.iog.spinnaker.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil;
import com.shangpin.iog.service.ProductService;
import org.springframework.stereotype.Service;

/**
 * Created by loyalty on 15/5/20.
 */
@Service
public class ProductServiceImpl implements ProductService {


    @Override
    public void fetchProduct() throws ServiceException {

        String url="http://185.58.119.177/spinnakerapi/Myapi/Productslist/GetAllTypes?DBContext=default&key=8IZk2x5tVN";

        try {
            String kk=  HttpUtil.getData(url, false);
            System.out.println("content = "  + kk);
        } catch (ServiceException e) {
            e.printStackTrace();
        }


    }
}

package com.shangpin.ephub.product.business.ui.purchase.controller;

import com.alibaba.fastjson.JSON;
import com.shangpin.ephub.product.business.ui.hub.all.service.IHubProductService;
import com.shangpin.ephub.product.business.ui.hub.common.dto.HubQuryDto;
import com.shangpin.ephub.product.business.ui.purchase.service.IProductService;
import com.shangpin.ephub.product.business.ui.purchase.vo.Product;
import com.shangpin.ephub.product.business.ui.purchase.vo.QueryDto;
import com.shangpin.ephub.response.HubResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 */
@RestController
@RequestMapping("/product")
public class productController {

    @Autowired
    private IProductService iproductService;

    @RequestMapping(value="/postsearch",method= RequestMethod.POST)
    public String hubProductList(@RequestBody QueryDto dto){
        return JSON.toJSONString(iproductService.getProductList(dto));
    }
}

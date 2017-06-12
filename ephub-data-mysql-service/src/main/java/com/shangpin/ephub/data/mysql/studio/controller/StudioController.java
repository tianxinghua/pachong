package com.shangpin.ephub.data.mysql.studio.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/6/8.
 */
@RestController
@RequestMapping("/studio")
public class StudioController {

    @RequestMapping(value = "/get-pending-product-list")
    public void getPendingProductList(){

    }
}

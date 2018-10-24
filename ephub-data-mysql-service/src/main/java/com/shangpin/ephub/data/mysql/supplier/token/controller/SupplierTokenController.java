package com.shangpin.ephub.data.mysql.supplier.token.controller;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.shangpin.ephub.data.mysql.common.ReplyResult;
import com.shangpin.ephub.data.mysql.supplier.channel.bean.SupplierChannelPic;
import com.shangpin.ephub.data.mysql.supplier.channel.service.SupplierChannelService;
import com.shangpin.ephub.data.mysql.supplier.token.bean.SupplierToken;
import com.shangpin.ephub.data.mysql.supplier.token.service.SupplierTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hub-supplier-token-mapping")
public class SupplierTokenController {
    @Autowired
    private SupplierTokenService supplierTokenService;

    private Gson gson = new Gson();
    @RequestMapping(value = "/supplierToken",method=RequestMethod.GET)
    public String getSupplierTokenBySupplierId(@RequestParam(name="supplierId")String supplierId){
        ReplyResult reply = new ReplyResult();
        SupplierToken sc = supplierTokenService.getSupplierTokenBySupplierId(supplierId);
        reply.success();
        reply.setData(JSONObject.toJSONString(sc));
        return JSONObject.toJSONString(reply);
    }
    @RequestMapping(value = "/supplierToken",method=RequestMethod.PUT)
    public String updateSupplierTokenBySupplierId(@RequestParam(name="SupplierToken")String supplierToken){
        ReplyResult reply = new ReplyResult();
        if(supplierToken==null || "".equals(supplierToken)){
            reply.success();
            return JSONObject.toJSONString(reply);
        }
        SupplierToken sc =JSONObject.parseObject(supplierToken,SupplierToken.class) ;
        supplierTokenService.updateSupplierTokenBySupplierId(sc);
        reply.success();
      //  reply.setData(JSONObject.toJSONString(sc));
        return JSONObject.toJSONString(reply);
    }
    @RequestMapping(value = "/supplierToken",method=RequestMethod.POST)
    public String addSupplierTokenBySupplierId(@RequestParam(name="SupplierToken")String supplierToken){
        ReplyResult reply = new ReplyResult();
        if(supplierToken==null || "".equals(supplierToken)){
            reply.success();
            return JSONObject.toJSONString(reply);
        }
        SupplierToken sc =JSONObject.parseObject(supplierToken,SupplierToken.class) ;
        supplierTokenService.addSupplierTokenBySupplierId(sc);
        reply.success();
        //  reply.setData(JSONObject.toJSONString(sc));
        return JSONObject.toJSONString(reply);
    }

    @RequestMapping(value = "/supplierToken",method=RequestMethod.DELETE)
    public String delSupplierTokenBySupplierId(@RequestParam(name="supplierId")String supplierId){
        ReplyResult reply = new ReplyResult();
        if(supplierId==null || "".equals(supplierId)){
            reply.success();
            return JSONObject.toJSONString(reply);
        }
        supplierTokenService.delSupplierTokenBySupplierId(supplierId);
        reply.success();
        //  reply.setData(JSONObject.toJSONString(sc));
        return JSONObject.toJSONString(reply);
    }
}

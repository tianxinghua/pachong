package com.shangpin.ephub.data.mysql.supplier.channel.controller;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.shangpin.ephub.data.mysql.common.ReplyResult;
import com.shangpin.ephub.data.mysql.supplier.channel.bean.SupplierChannelPic;
import com.shangpin.ephub.data.mysql.supplier.channel.service.SupplierChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hub-supplier-channel-mapping")
public class SupplierChannelController {
    @Autowired
    private SupplierChannelService supplierChannelService;

    @RequestMapping(value = "/supplierChannel",method=RequestMethod.GET)
    public String getSupplierChannelByMap(@RequestParam(name="supplierId")String supplierId, @RequestParam(name="supplierNo") String supplierNo){
        ReplyResult reply = new ReplyResult();
        Map<String ,String> map = new HashMap<String,String>();
        map.put("supplierNo",supplierNo);
        map.put("supplierId",supplierId);
        List<SupplierChannelPic> sc = supplierChannelService.getSupplierChannelPicByMap(map);
        reply.success();
        reply.setData(JSONObject.toJSONString(sc));
        return JSONObject.toJSONString(reply);
    }
}

package com.shangpin.ep.order.rest.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.shangpin.ep.order.module.order.service.impl.OrderCommonUtil;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import com.shangpin.ep.order.module.orderapiservice.impl.OrderHandleSearch;
import com.shangpin.ep.order.module.supplier.bean.SupplierDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ep.order.module.order.bean.SkuCountDTO;
import com.shangpin.ep.order.module.order.bean.SkuSearchResult;
import com.shangpin.ep.order.module.order.service.IHubOrderService;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by lizhongren on 2016/11/18.
 * 获取尚未发货的订单数量
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderCountController {
    @Autowired
    IHubOrderService hubOrderService;

    @Autowired
    OrderHandleSearch orderHandleSearch;

    @Autowired
    OrderCommonUtil orderCommonUtil;

    @RequestMapping(value="/sp", method = RequestMethod.POST)
    public SkuSearchResult getOrderCountBySpSkuNo(@RequestParam(value = "supplierNo")  String  supplierNo,@RequestParam(value = "supplierId")  String  supplierId, @RequestParam(value = "skuNo")  String  skuNo){
        SkuSearchResult result = new SkuSearchResult();
        List<SkuCountDTO> skuCountDTOs = new ArrayList<>();
        //初始化返回结果
        if (setCommonResult(supplierId, result, skuCountDTOs)) return result;
        //获取供货商信息
        boolean isOrderApi = false;


        IOrderService iOrderService = orderHandleSearch.getHander(supplierId );
        if(null!=iOrderService){//订单对接
            isOrderApi= true ;
        }
        //-----------调用实现
        List<String> skuNos = getSkuList(skuNo);
        Map<String,Integer>  skuMap = hubOrderService.getNoShippedSkuCount(supplierId,isOrderApi,true,skuNos);
        //设置返回的具体内容
        setSearchResult(skuCountDTOs, skuMap);

        return result;
    }




    @RequestMapping(value="/supplier", method = RequestMethod.POST)
    public SkuSearchResult getOrderCountBySupplierSkuNo(@RequestParam(value = "supplierNo")  String  supplierNo,@RequestParam(value = "supplierId")  String  supplierId,@RequestParam(value = "skuNo")  String  skuNo){
        SkuSearchResult result = new SkuSearchResult();
        List<SkuCountDTO> skuCountDTOs = new ArrayList<>();
        //初始化返回结果
        if (setCommonResult(supplierId, result, skuCountDTOs)) return result;
        //获取供货商信息
        boolean isOrderApi = false;
        //-----------调用实现
        List<String> skuNos = getSkuList(skuNo);
        Map<String,Integer>  skuMap = hubOrderService.getNoShippedSkuCount(supplierId,isOrderApi,false,skuNos);
        //设置返回的具体内容
        setSearchResult(skuCountDTOs, skuMap);

        return result;
    }

    /***
     * 通用的返回结果调用
     * @param supplierId
     * @param result
     * @param skuCountDTOs
     * @return
     */
    private boolean setCommonResult(String supplierId, SkuSearchResult result, List<SkuCountDTO> skuCountDTOs) {
        result.setResponseCode("0");
        result.setResponseMsg("");
        //初始化返回内容

        result.setResutl(skuCountDTOs);
        //判断参数是否争取
        if(StringUtils.isBlank(supplierId)){
            result.setResponseCode("1");
            result.setResponseMsg("请输入供货商编号");
            return true;
        }
        return false;
    }

    /**
     * 拆解sku
     * @param skuNo
     * @return
     */
    private List<String> getSkuList(String skuNo) {
        List<String> skuNos = new ArrayList<>();
        if(StringUtils.isNotBlank(skuNo)){
            String[] skuArray = skuNo.split(",");
            skuNos  = Arrays.asList(skuArray);
        }
        return skuNos;
    }

    /**
     * 赋值返回结果
     * @param skuCountDTOs ：返回的对象
     * @param skuMap  ：查询的结果
     */
    private void setSearchResult(List<SkuCountDTO> skuCountDTOs, Map<String, Integer> skuMap) {
        if(null!=skuMap){
            Set<Map.Entry<String,Integer>> entrySet  =  skuMap.entrySet();
            for(Map.Entry<String,Integer> entry :entrySet){
                SkuCountDTO dto = new SkuCountDTO();
                dto.setSkuNo(entry.getKey());
                dto.setQuantity(entry.getValue());
                skuCountDTOs.add(dto);
            }
        }
    }

}

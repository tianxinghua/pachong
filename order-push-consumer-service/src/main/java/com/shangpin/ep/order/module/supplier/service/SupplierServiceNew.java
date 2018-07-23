package com.shangpin.ep.order.module.supplier.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ep.order.conf.mail.message.ShangpinMail;
import com.shangpin.ep.order.conf.mail.sender.ShangpinMailSender;
import com.shangpin.ep.order.conf.rpc.ApiAddressProperties;
import com.shangpin.ep.order.module.supplier.bean.SupplierDTONew;
import com.shangpin.ep.order.push.common.GlobalConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by lizhongren on 2017/5/9.
 * 供货商信息获取服务
 */
@Service
@Slf4j
public class SupplierServiceNew {

    @Autowired
    private IShangpinRedis shangpinRedis;

    @Autowired
    private ApiAddressProperties apiAddressProperties;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    ShangpinMailSender shangpinMailSender;

    ObjectMapper om = new ObjectMapper();

    public SupplierDTONew getSupplier(String supplierNo) {
        SupplierDTONew dto = null;
        //先获取缓存中的数据
        String supplierMsg = shangpinRedis.get(GlobalConstant.REDIS_PRICE_PUSH_CONSUMER_SERVICE_SUPPLIER_KEY+"_"+supplierNo);
        log.info("supplierMsg = " +supplierMsg);
        if(StringUtils.isNotBlank(supplierMsg)){
            try {
                dto = om.readValue(supplierMsg, SupplierDTONew.class);
                if(null!=dto){
                    if(null!=dto.getSupplierContract()&&null!=dto.getSupplierContract().get(0).getQuoteMode()){
                        return dto;
                    }

                }
            } catch (Exception e) {
                log.error("供货商"+supplierNo + "从redis中获取信息后，转化对象失败");
            }
        }

        //调用接口获取供货商信息

        try {
            String supplierUrl =apiAddressProperties.getScmsSupplierInfoUrl()+supplierNo;

            String reSupplierMsg = restTemplate.getForObject(supplierUrl, String.class);
            log.info("SupplierMsg from api = " +reSupplierMsg);
            dto = om.readValue(reSupplierMsg, SupplierDTONew.class);
            //记录到REDIS缓存中
            shangpinRedis.setex(GlobalConstant.REDIS_PRICE_PUSH_CONSUMER_SERVICE_SUPPLIER_KEY+"_"+supplierNo,1000*60*5,om.writeValueAsString(dto));

        } catch (Exception e) {
            log.error("未获取到供货商信息. reason：" +e.getMessage(),e);


        }

        return  dto;
    }


    public void sendMail(String supplierNO){
        String text = "编号" +supplierNO+ "未找到供货商信息";
        ShangpinMail shangpinMail = new ShangpinMail();
        shangpinMail.setFrom("chengxu@shangpin.com");
        shangpinMail.setSubject("price_send_Mail");
        shangpinMail.setText(text);
        shangpinMail.setTo("ephub_support.list@shangpin.com");
        try {
            shangpinMailSender.sendShangpinMail(shangpinMail);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}

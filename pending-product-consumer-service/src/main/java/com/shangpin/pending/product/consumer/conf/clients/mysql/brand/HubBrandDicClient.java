package com.shangpin.pending.product.consumer.conf.clients.mysql.brand;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.pending.product.consumer.conf.clients.mysql.brand.bean.HubBrandDic;

@FeignClient("ephub-data-mysql-service")
public interface  HubBrandDicClient {
	
	@RequestMapping(value = "/hub-brand-dic/insert",method = RequestMethod.POST,consumes = "application/json")
    public int insert(HubBrandDic hubBrandDic);

}

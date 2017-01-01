package com.shangpin.ephub.product.business.rest.hubpending.spu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.product.business.common.service.check.HubCheckService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubCheckRuleService.java </p>
 * <p>Description: huaPendingSpu校验实现</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月23日 下午4:15:16
 */
@SuppressWarnings("unused")
@Service
@Slf4j
public class HubPendingSpuCheckService {
	
	@Autowired
	HubCheckService hubCheckService;
	public String checkHubPendingSpu(HubSpuPendingDto hubProduct){
		StringBuffer str = new StringBuffer();
		if(hubProduct!=null){
			boolean flag = false;
			//品牌
			if(hubProduct.getHubBrandNo()!=null){
				flag = hubCheckService.checkHubBrand(hubProduct.getHubBrandNo());
				if(!flag){
					str.append("brandNo在尚品品牌库中不存在,");
				}
			}else{
				str.append("brandNo为空,");
			}
			//颜色
			if(hubProduct.getHubColor()!=null){
				flag = hubCheckService.checkHubColor(hubProduct.getHubColor());
				if(!flag){
					str.append("hubColor在尚品颜色库中不存在,");
				}
			}else{
				str.append("hubColor为空,");
			}
			
			//校验品类
			if(hubProduct.getHubCategoryNo()!=null){
				if(!hubCheckService.checkHubCategory(hubProduct.getHubCategoryNo())){
					str.append("品类编号:"+hubProduct.getHubCategoryNo()+"不存在,") ;
				}	
			}else{
				str.append("品类为空,");
			}
			
			
			//性别
			if(hubProduct.getHubGender()!=null){
				flag = hubCheckService.checkHubGender(hubProduct.getHubGender());
				if(!flag){
					str.append("hubGender在尚品性别库中不存在,");
				}
			}else{
				str.append("hubGender为空,");
			}
			
			//季节
			if(hubProduct.getHubSeason()!=null){
				flag = hubCheckService.checkHubSeason(hubProduct.getHubSeason());
				if(!flag){
					str.append("hubGender在尚品性别库中不存在,");
				}
			}else{
				str.append("hubGender为空,");
			}
			
		}
		
		
	
		return str.toString();
	}
	

}

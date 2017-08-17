package com.shangpin.ephub.product.business.service.pending;

/**
 * Created by wangchao on 17/08/09.
 */
public interface CheckService {	
	//品类和性别 一致性校验
    public Boolean checkCategoryAndGender(String gender,String category);
}

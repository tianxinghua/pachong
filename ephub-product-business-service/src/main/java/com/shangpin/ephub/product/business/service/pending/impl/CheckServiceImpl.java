package com.shangpin.ephub.product.business.service.pending.impl;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by loyalty on 16/12/24.
 * @param
 */
@Service
@Slf4j
public class CheckServiceImpl implements com.shangpin.ephub.product.business.service.pending.CheckService {

	@Override
	public Boolean checkCategoryAndGender(String gender, String category) {
		log.info("checkCategoryAndGender params: gender="+gender+"category="+category);
		if(category.contains("A01")||category.contains("A02")||category.contains("A03")){
			if((gender.contains("男")&&category.contains("B02"))||(gender.contains("女")&&category.contains("B01"))){
				return true;
			}else{
				return false;
			}
		}
		if(category.contains("A11")){
			if((gender.contains("男")&&category.contains("B04"))||(gender.contains("女")&&category.contains("B02"))){
				return true;
			}else{
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args){
		CheckServiceImpl impl = new CheckServiceImpl();
		impl.checkCategoryAndGender("男童","A01B02");
	}

}

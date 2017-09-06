package com.shangpin.ephub.product.business.service.pending.impl;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by loyalty on 16/12/24.
 * 
 * @param
 */
@Service
@Slf4j
public class CheckServiceImpl implements com.shangpin.ephub.product.business.service.pending.CheckService {

	@Override
	public Boolean checkCategoryAndGender(String gender, String category) {
		log.info("checkCategoryAndGender params: gender=" + gender + "category=" + category);
		if (category.contains("A01")) {
			if (gender.contains("中性")) {
				return true;
			} else if ((gender.contains("男") && (category.contains("B02") || category.contains("B12")))
					|| (gender.contains("女") && (category.contains("B01") || category.contains("B11")))) {
				return true;
			} else {
				return false;
			}
		}
		if (category.contains("A02") || category.contains("A03")) {
			if (gender.contains("中性")) {
				return true;
			} else if ((gender.contains("男") && category.contains("B02"))
					|| (gender.contains("女") && category.contains("B01"))) {
				return true;
			} else {
				return false;
			}
		}

		if (category.contains("A11")) {
			if (gender.contains("儿童")) {
				return true;
			} else if ((gender.contains("男")
					&& (category.contains("B04") || category.contains("B08") || category.contains("B12")))
					|| (gender.contains("女")
							&& (category.contains("B02") || category.contains("B07") || category.contains("B11")))) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) {
		CheckServiceImpl impl = new CheckServiceImpl();
		System.out.println(impl.checkCategoryAndGender("女", "A11B11"));
	}

}

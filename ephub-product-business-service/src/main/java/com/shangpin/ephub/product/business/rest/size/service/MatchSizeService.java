package com.shangpin.ephub.product.business.rest.size.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.product.business.common.dto.CategoryScreenSizeDom;
import com.shangpin.ephub.product.business.common.dto.SizeStandardItem;
import com.shangpin.ephub.product.business.common.service.gms.SizeService;
import com.shangpin.ephub.product.business.rest.size.dto.MatchSizeDto;
import com.shangpin.ephub.product.business.rest.size.result.MatchSizeResult;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:HubBrandModelRuleService.java </p>
 * <p>Description: 品牌型号规则接口规范实现</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月21日 下午4:15:16
 */
@Service
@Slf4j
public class MatchSizeService{

	@Autowired
	SizeService sizeService;
	/**
	 * 正则校验
	 * @param hubBrandNo 品牌编号
	 * @param hubCategoryNo 品类编号
	 * @param brandMode 品牌方型号
	 * @return 如果校验通过将返回通过的品牌型号，否则将返回null；
	 */
	public MatchSizeResult matchSize(MatchSizeDto dto) {
		MatchSizeResult matchSizeResult = new MatchSizeResult();
		CategoryScreenSizeDom size = sizeService.getGmsSize(dto.getHubBrandNo(),dto.getHubCategoryNo());
		boolean flag = false;
		String result = null;
		if(size!=null){
			List<SizeStandardItem> list = size.getSizeStandardItemList();	
			if(list!=null&&list.size()>0){
				int i=0;
				for(SizeStandardItem item:list){
					String sizeValue = item.getSizeStandardValue();
					if(dto.getSize().equals(sizeValue)){
						i++;
						result = item.getSizeStandardName();
						flag = true;
						if(i>=2){
							result = "此尺码下含有多个尺码类型";
							flag = false;
						}
					}
				}
			}
		}
		matchSizeResult.setResult(result);
		matchSizeResult.setPassing(flag);
		return matchSizeResult;
	}
}

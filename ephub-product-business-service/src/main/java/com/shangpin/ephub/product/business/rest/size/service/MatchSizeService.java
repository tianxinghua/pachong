package com.shangpin.ephub.product.business.rest.size.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.product.business.common.dto.CategoryScreenSizeDom;
import com.shangpin.ephub.product.business.common.dto.SizeStandardItem;
import com.shangpin.ephub.product.business.common.service.gms.SizeService;
import com.shangpin.ephub.product.business.rest.size.dto.MatchSizeDto;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Title:HubBrandModelRuleService.java
 * </p>
 * <p>
 * Description: 品牌型号规则接口规范实现
 * </p>
 * <p>
 * Company: www.shangpin.com
 * </p>
 * 
 * @author yanxiaobin
 * @date 2016年12月21日 下午4:15:16
 */
@Service
@Slf4j
public class MatchSizeService {

	@Autowired
	SizeService sizeService;

	/**
	 * 正则校验
	 * 
	 * @param hubBrandNo
	 *            品牌编号
	 * @param hubCategoryNo
	 *            品类编号
	 * @param brandMode
	 *            品牌方型号
	 * @return 如果校验通过将返回通过的品牌型号，否则将返回null；
	 */
	public MatchSizeResult matchSize(MatchSizeDto dto) {
		MatchSizeResult matchSizeResult = new MatchSizeResult();
		CategoryScreenSizeDom size = sizeService.getGmsSize(dto.getHubBrandNo(), dto.getHubCategoryNo());
		boolean sizeIsExist = false;
		String result = null;
		boolean isNotTemplate = false;
		Map<String,String> screenSizeMap = new HashMap<String,String>();
		Map<String,String> standardSizeMap = new HashMap<String,String>();
		if(size!=null){
			
			List<SizeStandardItem> list = size.getSizeStandardItemList();	
			if(list!=null&&list.size()>0){
				//获取筛选尺码和标准尺码map集合
				getSizeMap(list,screenSizeMap,standardSizeMap);
				//第一步：从标准尺码中查找匹配尺码
				sizeIsExist = matchStandardSize(dto.getSize(),standardSizeMap,matchSizeResult);
				if(!sizeIsExist){
					sizeIsExist = matchScreenSize(dto.getSize(),screenSizeMap,matchSizeResult);
				}
			}else{
				isNotTemplate = true;
			}
		}else{
			isNotTemplate = true;
		}
		
		if(matchSizeResult.isPassing()){
			matchSizeResult.setPassing(true);
		}else{
			if(isNotTemplate){
				matchSizeResult.setNotTemplate(isNotTemplate);
				result = "scm没有尺码模板";	
			}else{
				//sizeIsExist为true，说明匹配到尺码
				if(sizeIsExist){
					matchSizeResult.setMultiSizeType(true);
					result = "含有多个尺码模板";		
				}else{
					if(standardSizeMap.size()>0){
						result = "尺码："+size+"未匹配成功";		
						matchSizeResult.setFilter(true);	
					}else{
						result = "scm没有尺码模板";	
						matchSizeResult.setNotTemplate(true);		
					}
				}
			}
		}
		matchSizeResult.setResult(result);
		return matchSizeResult;
	}
	private boolean matchScreenSize(String size, Map<String, String> map2, MatchSizeResult matchSizeResult) {
		boolean sizeIsExist = false;
		if (map2.size() > 0) {
			int i = 0;
			for (Map.Entry<String, String> entry : map2.entrySet()) {
				String value = entry.getValue();
				String key = entry.getKey();
				if (size.equals(value)) {
					sizeIsExist = true;
					i++;
					String[] arr = key.split(":", -1);
					if (arr.length == 2) {
						matchSizeResult.setSizeType(arr[0]);
						matchSizeResult.setSizeValue(arr[1]);
						matchSizeResult.setPassing(true);
						if (i >= 2) {
							matchSizeResult.setPassing(false);
							break;
						}
					}
				}
			}
		}
		return sizeIsExist;

	}

	private boolean matchStandardSize(String size, Map<String, String> map0, MatchSizeResult matchSizeResult) {
		boolean isExist = false;
		if (map0.size() > 0) {
			int i = 0;
			for (Map.Entry<String, String> entry : map0.entrySet()) {
				String value = entry.getValue();
				String key = entry.getKey();
				if (size.equals(value)) {
					String[] arr = key.split(":", -1);
					isExist = true;
					i++;
					if (arr.length == 2) {
						matchSizeResult.setSizeType(arr[0]);
						matchSizeResult.setSizeValue(arr[1]);
						matchSizeResult.setPassing(true);
						if (i >= 2) {
							matchSizeResult.setPassing(false);
							break;
						}
					}
				}
			}
		}
		return isExist;
	}

	private void getSizeMap(List<SizeStandardItem> list, Map<String, String> map2, Map<String, String> map0) {
		for (SizeStandardItem item : list) {
			String sizeValue = item.getSizeStandardValue();
			String sizeStandardName = item.getSizeStandardName();
			// 筛选尺码
			if (item.getIsScreening() == (byte) 2) {
				map2.put(sizeStandardName + ":" + sizeValue, sizeValue);
			}
			// 标准尺码
			if (item.getIsScreening() == (byte) 0) {
				map0.put(sizeStandardName + ":" + sizeValue, sizeValue);
			}
		}

	}
}

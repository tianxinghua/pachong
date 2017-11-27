package com.shangpin.ephub.product.business.rest.size.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.product.business.rest.gms.dto.CategoryScreenSizeDom;
import com.shangpin.ephub.product.business.rest.gms.dto.SizeStandardItem;
import com.shangpin.ephub.product.business.rest.gms.service.SizeService;
import com.shangpin.ephub.product.business.rest.size.dto.MatchSizeDto;

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
public class MatchSizeService {

	@Autowired
	SizeService sizeService;

	/**
	 * 根据品牌、品类编号和尺码匹配出尺码类型
	 * @param dto
	 * @return
	 */
	public MatchSizeResult matchSize(MatchSizeDto dto) {
		if(dto.getSize()!=null){
			String regex = "\\s+";
			dto.setSize(dto.getSize().replaceAll(regex, ""));
		}
		MatchSizeResult matchSizeResult = new MatchSizeResult();
		matchSizeResult.setSizeValue(dto.getSize());
		CategoryScreenSizeDom size = sizeService.getGmsSize(dto.getHubBrandNo(), dto.getHubCategoryNo());
		boolean sizeIsExist = false;
		Map<String,String> screenSizeMap = new HashMap<String,String>();
		Map<String,String> standardSizeMap = new HashMap<String,String>();
		if(size!=null&&size.getSizeStandardItemList()!=null&&size.getSizeStandardItemList().size()>0){
			List<SizeStandardItem> list = size.getSizeStandardItemList();
			//获取筛选尺码和标准尺码map集合
			getSizeMap(list,screenSizeMap,standardSizeMap);
			//第一步：从标准尺码中查找匹配尺码
			sizeIsExist = matchStandardSize(dto.getSize(),standardSizeMap,matchSizeResult);
			if(!sizeIsExist){
				//第二步：从标准尺码中未匹配到尺码。继续从筛选尺码中匹配，如果匹配到则排除，
				sizeIsExist = matchScreenSize(dto.getSize(),screenSizeMap,matchSizeResult);
			}
		}else{
			matchSizeResult.setPassing(false);
			matchSizeResult.setNotTemplate(false);
		}
		return matchSizeResult;
	}
	private boolean matchScreenSize(String size, Map<String, String> map2, MatchSizeResult matchSizeResult) {
		boolean sizeIsExist = false;
		matchSizeResult.setPassing(false);
		if (map2.size() > 0) {
			for (Map.Entry<String, String> entry : map2.entrySet()) {
				String value = entry.getValue();
				if(value!=null){
					String [] arrAcm = value.split("\\|",-1);
					for(String scmSize:arrAcm){
						if (size.equals(scmSize)) {
							sizeIsExist = true;
							matchSizeResult.setFilter(true);
							matchSizeResult.setResult("尺码："+size+"排除");
							break;
						}
					}
					if(!sizeIsExist){
						matchSizeResult.setResult("scm没有尺码模板");
						matchSizeResult.setNotTemplate(true);
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
						matchSizeResult.setResult("尺码："+size+"匹配成功");
						matchSizeResult.setSizeType(arr[0]);
						matchSizeResult.setSizeValue(arr[1]);
						matchSizeResult.setPassing(true);
						if (i >= 2) {
							matchSizeResult.setResult("含有多个尺码模板");
							matchSizeResult.setPassing(false);
							matchSizeResult.setMultiSizeType(true);
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
//			// 筛选尺码
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

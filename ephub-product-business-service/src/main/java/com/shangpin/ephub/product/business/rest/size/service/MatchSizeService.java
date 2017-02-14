package com.shangpin.ephub.product.business.rest.size.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			
				Map<String,String> map2 = new HashMap<String,String>();
				Map<String,String> map0 = new HashMap<String,String>();
				for(SizeStandardItem item:list){
					String sizeValue = item.getSizeStandardValue();
					String sizeStandardName = item.getSizeStandardName();
					//筛选尺码
					if(item.getIsScreening()==(byte)2){
						map2.put(item.getScreenSizeStandardValueId()+","+sizeStandardName+":"+sizeValue,sizeValue);
					}
					//标准尺码
					if(item.getIsScreening()==(byte)0){
						map0.put(item.getScreenSizeStandardValueId()+","+sizeStandardName+":"+sizeValue,sizeValue);
					}
				}
				
				boolean isExist = false;
				if (map0.size() > 0) {
					int i = 0;
					for (Map.Entry<String, String> entry : map0.entrySet()) {
						String value = entry.getValue();
						String key = entry.getKey();
						if (dto.getSize().equals(value)) {
							isExist = true;
							i++;
							String screenSizeStandardValueId = key.split(",", -1)[0];
							String[] arr = key.split(",", -1)[1].split(":", -1);
							if (arr.length == 2) {
								matchSizeResult.setSizeType(arr[0]);
								matchSizeResult.setSizeId(screenSizeStandardValueId);
								matchSizeResult.setSizeValue(arr[1]);
								matchSizeResult.setMultiSizeType(false);
								flag = true;
								if (i >= 2) {
									result = "尺码：" + arr[1] + "下含有多个尺码类型";
									flag = false;
									matchSizeResult.setMultiSizeType(true);
									break;
								}
							}
						}
					}
				}

				if(!isExist){
					if (map2.size() > 0) {
						int i = 0;
						for (Map.Entry<String, String> entry : map2.entrySet()) {
							String value = entry.getValue();
							String key = entry.getKey();
							if (dto.getSize().equals(value)) {
								isExist = true;
								i++;
								String screenSizeStandardValueId = key.split(",", -1)[0];
								String[] arr = key.split(",", -1)[1].split(":", -1);
								if (arr.length == 2) {
									matchSizeResult.setSizeType(arr[0]);
									matchSizeResult.setSizeId(screenSizeStandardValueId);
									matchSizeResult.setSizeValue(arr[1]);
									matchSizeResult.setMultiSizeType(false);
									flag = true;
									if (i >= 2) {
										result = "尺码：" + arr[1] + "下含有多个尺码类型";
										flag = false;
										matchSizeResult.setMultiSizeType(true);
										break;
									}
								}
							}
						}
					}
				}
				if(!flag){
					if(result==null){
						result = "尺码："+dto.getSize()+"未匹配成功";						
					}
				}
				
			}
		}else{
			result = "尺码："+dto.getSize()+"未匹配成功";
		}
		matchSizeResult.setResult(result);
		matchSizeResult.setPassing(flag);
		return matchSizeResult;
	}
}

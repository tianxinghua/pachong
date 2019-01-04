package com.shangpin.iog.coltorti.service.attribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.coltorti.conf.ApiURL;
import com.shangpin.iog.coltorti.dto.attribute.AttriDto;
import com.shangpin.iog.coltorti.dto.attribute.DiscountDto;
import com.shangpin.iog.coltorti.service.ColtortiUtil;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
/**
 * <p>Title: AttributeService</p>
 * <p>Description: 获取各种属性的服务类，包括获取所有季节名称、品牌名称、品类名称等 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年9月7日 下午6:06:55
 *
 */
public class AttributeService {
	
	private static OutTimeConfig outTimeConfig = new OutTimeConfig(1000*10*10,1000*10*10,1000*10*10);
	private static Gson gson = new Gson();

	/**
	 * 获取所有季节信息
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> getAllSeasons() throws Exception{
		int page = 1, size = 500;
		String apiUrl = ApiURL.SEASONS;
		return getAttributes(page, size, apiUrl);
	}
	/**
	 * 获取所有品牌信息
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> getAllBrands() throws Exception{
		int page = 1, size = 500;
		String apiUrl = ApiURL.BRANDS;
		return getAttributes(page, size, apiUrl);
	}
	/**
	 * 获取所有品类信息
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> getAllCategories() throws Exception{
		int page = 1, size = 500;
		String apiUrl = ApiURL.CATEGORIES;
		return getAttributes(page, size, apiUrl);
	}
	/**
	 * 获取所有性别信息
	 */
	public static Map<String,String> getAllGenders() throws Exception{
		int page = 1, size = 500;
		String apiUrl = ApiURL.GENDERS;
		return getAttributes(page, size, apiUrl);
	}
	/**
	 * 获取其他属性
	 * @param supplierSpuNo
	 * @return
	 * @throws Exception
	 */
	public static Map<String,Map<String,Object>> getAttributeBySpuNo(String supplierSpuNo) throws Exception{
		Map<String,String> param=ColtortiUtil.getCommonParam(1 ,10);
		String apiUrl = "";
		if(!StringUtils.isEmpty(supplierSpuNo)){
			apiUrl = ApiURL.ATTRIBUTES + "/"+supplierSpuNo;
		}else{
			apiUrl = ApiURL.ATTRIBUTES;
		}
		String body=HttpUtil45.get(ColtortiUtil.paramGetUrl(apiUrl ,param),outTimeConfig,null);
		Map<String,Map<String,Object>> attriDto= gson.fromJson(body, new TypeToken<Map<String,Map<String,Object>>>(){}.getType());
		return attriDto;
	}
	/**
	 * 获取价格信息
	 * @param supplierSpuNo
	 * @return
	 * @throws Exception
	 */
	public static DiscountDto getDiscount(String supplierSpuNo) throws Exception{
		Map<String,String> param=ColtortiUtil.getCommonParam(1 ,10);
		String apiUrl = "";
		if(!StringUtils.isEmpty(supplierSpuNo)){
			apiUrl = ApiURL.DISCOUNTS + "/"+supplierSpuNo;
		}else{
			throw new Exception("请传入supplierSpuNo");
		}
		String body=HttpUtil45.get(ColtortiUtil.paramGetUrl(apiUrl ,param),outTimeConfig,null);
		Map<String,DiscountDto> discounts = gson.fromJson(body, new TypeToken<Map<String,DiscountDto>>(){}.getType());
		return discounts.get(supplierSpuNo);
	}
	
	/**
	 * 获取各种属性信息
	 * @param page
	 * @param size
	 * @param apiUrl
	 * @return
	 * @throws ServiceException
	 */
	private static Map<String, String> getAttributes(int page, int size, String apiUrl) throws Exception {
		Map<String,String> attributes =  new HashMap<String,String>();
		Map<String,String> param=ColtortiUtil.getCommonParam(page ,size);
		String body=HttpUtil45.get(ColtortiUtil.paramGetUrl(apiUrl,param),outTimeConfig,null);
		if(HttpUtil45.errorResult.equals(body)){
			throw new Exception("获取接口数据异常，接口为"+apiUrl);
		}
		List<AttriDto> lists = gson.fromJson(body, new TypeToken<List<AttriDto>>(){}.getType());
		if(CollectionUtils.isNotEmpty(lists)){
			for(AttriDto attriDto : lists){
				attributes.put(attriDto.getId(), null != attriDto.getName().getEn() ? attriDto.getName().getEn() : attriDto.getName().getIt());
			}
		}
		return attributes;
	}
	
	public static void main(String[] args) {
		AttributeService s = new AttributeService();
		try {
			s.getAttributeBySpuNo("172954DPN000003-604");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

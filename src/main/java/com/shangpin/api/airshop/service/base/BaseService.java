package com.shangpin.api.airshop.service.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangpin.api.airshop.dto.base.ApiContentStr;
import com.shangpin.api.airshop.dto.base.ResponseContentList;
import com.shangpin.api.airshop.dto.base.ResponseContentOne;
import com.shangpin.api.airshop.util.DateFormat;
import com.shangpin.common.utils.FastJsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@Service
public class BaseService {

	private static final Logger logger = LoggerFactory.getLogger(BaseService.class);
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 请求服务层，并将结果json序列化为具体的对象<br/>
	 * @param url 请求的服务url
	 * @param param 请求的服务参数
	 * @param T 待返回的数据类型
	 * @return ResponseContent
	 */
	public <T> ResponseContentList<T> requestAPI(String url, Map<String,Object> param, Class<T> T) throws UnsupportedEncodingException {
		String requestJson = FastJsonUtil.serialize2String(param);
		return requestAPI(url, requestJson, T);
	}
	/**
	 * 请求服务层，并将结果json序列化为具体的对象<br/>
	 * @param url 请求的服务url
	 * @param requestJson 请求的服务参数
	 * @param T 待返回的数据类型
	 * @return 实体
	 */
	public <T> ResponseContentList<T> requestAPI(String url, String requestJson, Class<T> T) throws UnsupportedEncodingException {

		ApiContentStr apiContentStr = postApi(url, requestJson);

		if(StringUtils.isEmpty(apiContentStr)){
			return ResponseContentList.errorResp("请求供应商API http请求接口数据异常!");
		}

		//判断返回是否成功
		if(!apiContentStr.getIsSuccess()){
			logger.info("请求供应商API error ! responseMessageRes ={}", apiContentStr.toString());
			return ResponseContentList.errorRespWithApi(apiContentStr.getResCode());
		}

		//返回数据转换成业务实体
		List<T> list = FastJsonUtil.deserializeString2ObjectList(apiContentStr.getMessageRes(), T);

		return ResponseContentList.successResp(list);
	}

	public JSONObject getReturnBeforOrder(String url, JSONObject param){
		JSONObject result = new JSONObject();
		String resultString = restTemplate.postForObject(url, getHttpPostData(param.toJSONString()), String.class);
		JSONObject resultJson= JSONObject.parseObject(resultString);
		if (resultJson!=null&&resultJson.getBooleanValue("isSuccess")&&resultJson.getJSONObject("messageRes")!=null) {
			JSONArray list = resultJson.getJSONObject("messageRes").getJSONArray("returnOrders");
			JSONArray newList = new JSONArray();
			for (int i = 0; i <list.size(); i++) {
				JSONObject item = new JSONObject();
				item.put("sopReturnOrderNo", list.getJSONObject(i).getString("sopReturnOrderNo"));
				item.put("logisticsOrderNo", list.getJSONObject(i).getString("logisticsOrderNo"));
				item.put("returnQuantity", list.getJSONObject(i).getString("returnQuantity"));
				item.put("createTime", DateFormat.TimeFormatChangeToString(list.getJSONObject(i).getString("createTime"), "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy HH:mm:ss") );
				newList.add(item);
			}
			result.put("content",JSONObject.parseObject("{\"total\":"+resultJson.getJSONObject("messageRes").getString("total")+",\"returnOrders\":"+newList.toJSONString()+"}") );
		}
		result.put("code", "0");
		result.put("msg", "");
		return result;
	}
	
	public JSONObject getReturnAfterOrder(String url, JSONObject param){
		JSONObject result = new JSONObject();
		String resultString = restTemplate.postForObject(url, getHttpPostData(param.toJSONString()), String.class);
		JSONObject resultJson= JSONObject.parseObject(resultString);
		if (resultJson!=null&&resultJson.getBooleanValue("isSuccess")&&resultJson.getJSONObject("messageRes")!=null) {
			JSONArray list = resultJson.getJSONObject("messageRes").getJSONArray("secondReturnOrders");
			JSONArray newList = new JSONArray();
			for (int i = 0; i <list.size(); i++) {
				JSONObject item = new JSONObject();
				item.put("sopSecondReturnOrderNo", list.getJSONObject(i).getString("sopSecondReturnOrderNo"));
				item.put("logisticsOrderNo", list.getJSONObject(i).getString("logisticsOrderNo"));
				item.put("totalQuantity", list.getJSONObject(i).getString("totalQuantity"));
				item.put("createTime", DateFormat.TimeFormatChangeToString(list.getJSONObject(i).getString("createTime"), "yyyy/MM/dd HH:mm:ss", "dd-MM-yyyy HH:mm:ss") );
				newList.add(item);
			}
			result.put("content",JSONObject.parseObject("{\"total\":"+resultJson.getJSONObject("messageRes").getString("total")+",\"secondReturnOrders\":"+newList.toJSONString()+"}") );
		}
		result.put("code", "0");
		result.put("msg", "");
		return result;
	}
	
	/**
	 * 请求服务层，并将结果json序列化为具体的对象<br/>
	 * @param url 请求的服务url
	 * @param param 请求的服务参数
	 * @param clazz 待返回的数据类型
	 * @return 实体
	 */
	public <T> ResponseContentOne<T> requestAPI4One(String url, Map<String,Object> param, Class<T> clazz) throws UnsupportedEncodingException {
		String requestJson = FastJsonUtil.serialize2String(param);
		
		return requestAPI4One(url, requestJson, clazz);
	}

	/**
	 * 请求服务层，并将结果json序列化为具体的对象<br/>
	 * @param url 请求的服务url
	 * @param requestJson 请求的服务参数
	 * @param clazz 待返回的数据类型
	 * @return 实体
	 */
	public <T> ResponseContentOne<T> requestAPI4One(String url, String requestJson, Class<T> clazz) throws UnsupportedEncodingException {

		ApiContentStr apiContentStr = postApi(url, requestJson);

		if(StringUtils.isEmpty(apiContentStr)){
			return ResponseContentOne.errorResp("请求供应商API http请求接口数据异常!");
		}

		//判断返回是否成功
		if(!apiContentStr.getIsSuccess()){
			logger.info("请求供应商API error ! responseMessageRes ={}", apiContentStr.toString());
			return ResponseContentOne.errorRespWithApi(apiContentStr.getResCode());
		}

		//直接返回String的处理
		if(clazz.equals(String.class)){
			return ResponseContentOne.successResp((T)apiContentStr.getMessageRes());
		}

		//返回数据转换成业务实体
		T t = FastJsonUtil.deserializeString2Obj(apiContentStr.getMessageRes(), clazz);
		return ResponseContentOne.successResp(t);
	}

	/**
	 * 获取api数据
	 * @return ResponseMessageRes
     */
	public ApiContentStr postApi(String url, String requestJson){

		long startTime = System.currentTimeMillis();
		logger.info("请求供应商API url={},请求数据 ={}", url, requestJson);

		//发起请求
		HttpHeaders headers =new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<String> request = new HttpEntity<>("=" + requestJson, headers);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);

		//http响应码
		int httpStatus = responseEntity.getStatusCode().value();
		logger.info("请求供应商API http响应吗 :"+httpStatus+"，耗时："+(System.currentTimeMillis()-startTime));
		if( httpStatus != HttpStatus.OK.value()){
			return null;
		}

		//消息体
		String body = responseEntity.getBody();
		logger.info("请求供应商API 返回数据 ={}",body);

		//返回数据转换成基本实体
//		ApiContentStr apiContentStr = FastJsonUtil.deserializeString2Obj(body, ApiContentStr.class);

		ApiContentStr apiContentStr = JSON.parseObject(body, ApiContentStr.class);

		return apiContentStr;
	}

		//包装Http请求参数
		private HttpEntity<String> getHttpPostData(String param){
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<String> paramEntity  = new HttpEntity<String>("=" +param,headers);
			return paramEntity;
		}
}

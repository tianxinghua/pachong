/**
 * 
 */
package com.shangpin.iog.coltorti.service;

import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.coltorti.conf.ApiURL;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;

/**
 * @description
 * @author 陈小峰 <br/>
 *         2015年6月5日
 */
public class ColtortiTokenService {
	static Logger logger =LoggerFactory.getLogger(ColtortiTokenService.class);
	public static String token = null;
	public static long tokenExpire = 0;
	public static long tokenCreate = 0;
	private static final ReentrantLock lock = new ReentrantLock();

	public static String getToken() throws ServiceException {
		//TODO 测试
		//if (token == null)		return "6c9ade4c5fea79a5c0b060c67b55f4a2a59316dff3a18f047990484b8cc74d8c6ecddbbbb03139211f017ee9ea983f908ae5a46cf087294ccfdb46a78107fd015f5802e1d5f7675ed03af16d8e86d2908ccf9b68e740e114553d6d4f81b6ca74";
		lock.lock();
		if (token == null
				|| System.currentTimeMillis() - tokenCreate > tokenExpire) {
			token = initToken();
		}
		lock.unlock();
		return token;
	}

	/**
	 * { "access_token": "abcd1234abcd1234abcd1234abcd1234", "expires_in": 3600
	 * }
	 * 
	 * @return 获取到的token
	 */
	protected static String initToken() throws ServiceException {
		logger.info("初始化token......");
		try {
			token = null;
			String body = HttpUtils.post(ApiURL.AUTH, null, false, true,
					ApiURL.userName, ApiURL.password);
			logger.info("token:" + body);
			ColtortiUtil.check(body);
			JsonObject jo = new JsonParser().parse(body).getAsJsonObject();
			token = jo.get("access_token").getAsString();
			tokenExpire = jo.get("expires_in").getAsInt() * 1000;
			tokenCreate = System.currentTimeMillis();
		} catch (Exception e) {
			logger.error("初始化token错误", e);
			throw new ServiceMessageException(e.getMessage());
		}
		return token;
	}
}

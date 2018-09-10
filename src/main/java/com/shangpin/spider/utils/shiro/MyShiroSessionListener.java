package com.shangpin.spider.utils.shiro;

import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

/** 
 * @author  njt 
 * @date 创建时间：2017年11月10日 下午2:09:12 
 * @version 1.0 
 * @parameter  
 */

public class MyShiroSessionListener implements SessionListener {
	
	private Logger log = Logger.getLogger(MyShiroSessionListener.class);
	@Override
	public void onStart(Session session) {
		log.info("----会话创建:"+session.getId());
	}

	@Override
	public void onStop(Session session) {
		log.info("----会话停止:"+session.getId());
	}

	@Override
	public void onExpiration(Session session) {
		log.info("----会话过期:"+session.getId());
	}

}

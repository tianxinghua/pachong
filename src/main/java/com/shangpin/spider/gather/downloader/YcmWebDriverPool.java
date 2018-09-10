package com.shangpin.spider.gather.downloader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jetty.util.StringUtil;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.entity.gather.SpiderRules;


/**
 * @author njt
 * @date 创建时间：2017年10月30日 下午2:11:33
 * @version 1.0
 * @parameter
 */

public class YcmWebDriverPool {

	private static final Logger logger = LoggerFactory
			.getLogger(YcmWebDriverPool.class);
	/**
	 * 是否执行get()方法的标识
	 */
	private boolean flag = false;
	private int defaultCapacity = 5;
	private AtomicInteger refCount = new AtomicInteger(0);
	private int driverDeathFlag = 0;
	private int thraedDeathNum = 0;
	

	/**
	 * store webDrivers available
	 */
	private BlockingDeque<WebDriver> innerQueue = new LinkedBlockingDeque<WebDriver>(
			defaultCapacity);

	private static String PHANTOMJS_PATH;
	private static DesiredCapabilities caps = DesiredCapabilities.phantomjs();
	static {
		PHANTOMJS_PATH = "";
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", false);
		List<String> cmdList = new ArrayList<>();  
        // 禁用图片  
        cmdList.add("--load-images=false");  
        // 本地缓存  
        cmdList.add("--disk-cache=true");
        cmdList.add("--web-security=false");
        cmdList.add("--ssl-protocol=any");
        cmdList.add("--ignore-ssl-errors=true");
        
        
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cmdList);
		// caps.setCapability("load-images","no");
		// caps.setCapability("disk-cache", "yes");
		// caps.setCapability("ignore-ssl-errors", true);
	}

	public YcmWebDriverPool() {
	}

	public YcmWebDriverPool(int poolsize) {
		this.defaultCapacity = poolsize;
		innerQueue = new LinkedBlockingDeque<WebDriver>(poolsize);
	}

	public YcmWebDriverPool(int poolsize, String phantomjsPath, SpiderRules spiderRuleInfo) {
		PHANTOMJS_PATH = phantomjsPath;
		String proxyIpAndPort = "";
		/*String proxyHost = spiderRuleInfo.getProxyHost();
		int proxyPort = spiderRuleInfo.getProxyPort();
		if (StringUtil.isNotBlank(proxyHost) && proxyPort > 0) {
			proxyIpAndPort = proxyHost + ":" + proxyPort;
		}*/
		if(StringUtil.isNotBlank(proxyIpAndPort)){
			Proxy proxy=new Proxy();
			  proxy.setHttpProxy(proxyIpAndPort).setFtpProxy(proxyIpAndPort).setSslProxy(proxyIpAndPort);
			  caps.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
			  caps.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
			  System.setProperty("http.nonProxyHosts", "localhost");
			  caps.setCapability(CapabilityType.PROXY, proxy);
		}
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX+ "User-Agent",spiderRuleInfo.getUserAgent());
		caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,PHANTOMJS_PATH);
		this.defaultCapacity = poolsize;
		innerQueue = new LinkedBlockingDeque<WebDriver>(poolsize);
	}

	public WebDriver get() throws InterruptedException {
		int size = innerQueue.size();
		logger.info("innerQueue.size()为："+innerQueue.size());
		if(size>0){
			WebDriver poll = innerQueue.poll();
			if (poll != null) {
				System.out.println("---------------------poll不为空"+poll);
				flag = true;
				return poll;
			}
		}
		logger.info("refCount.get()为："+refCount.get());
		logger.info("DEFAULT_CAPACITY为："+defaultCapacity);
		if (!flag) {
			if(refCount.get() < defaultCapacity){
				while (refCount.get() < defaultCapacity) {
					synchronized (innerQueue) {
						if (refCount.get() < defaultCapacity) {
							logger.info("refCount.get()小于DEFAULT_CAPACITY："+refCount.get());
							WebDriver mDriver = null;
							try {
								mDriver = new PhantomJSDriver(caps);
							} catch (Exception e) {
								logger.info("初始化phantomjs出错：");
								e.printStackTrace();
							}
							
							mDriver = setWebDriverTimeout(mDriver);
							// mDriver.manage().window().setSize(new Dimension(1366,
							// 768));
							innerQueue.add(mDriver);
							refCount.incrementAndGet();
						}
					}
				}
			}else{
				logger.info("webDriverpool为空！");
				synchronized (innerQueue) {
						WebDriver mDriver = new PhantomJSDriver(caps);
						mDriver = setWebDriverTimeout(mDriver);
						innerQueue.add(mDriver);
						refCount = new AtomicInteger(0);
						this.defaultCapacity = innerQueue.size();
						logger.info("新的refCount.get()为："+refCount.get());
						logger.info("新的DEFAULT_CAPACITY为："+defaultCapacity);
				}
			}
			
		}

		flag = true;
		return innerQueue.take();
	}

	public void returnToPool(WebDriver webDriver) {
		// webDriver.quit();
		// webDriver=null;
		if(webDriver!=null){
			innerQueue.add(webDriver);
			logger.info("-------------------returnToPool:"+innerQueue.size());
			if (driverDeathFlag >= defaultCapacity) {
				shutdown();
				logger.info("js挂掉次数频繁，关闭当前线程下的js！");
				thraedDeathNum++;
			}
		}
		
		/*
		 * int i = innerQueue.size();
		 * logger.info("***************--------web池为--"+i);
		 * if(i==DEFAULT_CAPACITY){ shutdown();
		 * logger.info("webDriver已满，关闭phantomjs,程序正常结束！"); }
		 */
	}

	public void shutdown() {
		if (innerQueue.size() == 0) {
			if (thraedDeathNum >= 1) {
				for (WebDriver driver : innerQueue) {
					driver.quit();
					logger.info("js挂掉个数达到极限***driver退出------");
				}
				innerQueue.clear();
				logger.info("js挂掉个数达到极限，不再生成新的js，等待各线程的js关闭");
			} else {
				driverDeathFlag++;
				logger.info("记录一次js挂掉！再重新启动一个js");
				WebDriver mDriver = new PhantomJSDriver(caps);
				mDriver = setWebDriverTimeout(mDriver);
				innerQueue.add(mDriver);
				logger.info("当前线程下innerQueue已经为空！");
			}
		} else {
			for (WebDriver driver : innerQueue) {
				driver.quit();
				logger.info("**************driver退出------" + driver);
			}
			innerQueue.clear();
		}

	}

	public int driverDeathSite() {
		return driverDeathFlag;
	}

	public int thraedDeathSite() {
		return thraedDeathNum;
	}

	public int innerSite() {
		if (flag) {
			int i = innerQueue.size();
			return i;
		} else {
			return 100;
		}
	}

	public void shutdownEnd() {
		for (WebDriver driver : innerQueue) {
			driver.quit();
			logger.info("关闭所有driver------" + driver);
		}
		innerQueue.clear();
	}
	
	private WebDriver setWebDriverTimeout(WebDriver mDriver){
//		页面超时时间,避免phantomjs卡住
		mDriver.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);
//		脚本超时时间
		mDriver.manage().timeouts().setScriptTimeout(180, TimeUnit.SECONDS);
//		隐性等待，有BUG，暂不使用
//		mDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return mDriver;
	}

}

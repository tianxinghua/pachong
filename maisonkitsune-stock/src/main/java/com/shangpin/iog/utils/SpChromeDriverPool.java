package com.shangpin.iog.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;
/**
 * 
 * @author njt
 * SpChromeDriverPool
 */
public class SpChromeDriverPool{
	private final static Logger LOG = LoggerFactory.getLogger(SpChromeDriverPool.class);
	/**
	 * 初始化的标识
	 */
	private volatile boolean flag = false;
	/**
	 * 默认队列容量5
	 */
	private volatile int poolSize = 5;
	private BlockingDeque<WebDriver> innerQueue = null;
	private BlockingDeque<WebDriver> outQueue = null;
	
//	记录Pool中的driver的数量变化
	private AtomicInteger changeCount = new AtomicInteger(0);
	
	private String webDriverPath;
	
	private static DesiredCapabilities caps = DesiredCapabilities.chrome();
	
	public SpChromeDriverPool() {
		super();
		this.innerQueue = new LinkedBlockingDeque<WebDriver>(poolSize);
	}
	
	public SpChromeDriverPool(int poolSize) {
		super();
		this.poolSize = poolSize;
		this.innerQueue = new LinkedBlockingDeque<WebDriver>(poolSize);
	}
	
	public SpChromeDriverPool(int poolSize, String webDriverPath, Boolean headless) {
		super();
		this.poolSize = poolSize;
		this.innerQueue = new LinkedBlockingDeque<WebDriver>(poolSize);
		this.outQueue = new LinkedBlockingDeque<WebDriver>(poolSize);
		this.webDriverPath = webDriverPath;
//		caps.setCapability(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY,webDriverPath);
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", false);
		caps.setCapability("acceptSslCerts", true);
		//css搜索支持
		caps.setCapability("cssSelectorsEnabled", true);
		if(headless) {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("Content-Type: text/plain;charset=UTF-8");
			options.addArguments("--headless");
			caps.setCapability("chromeOptions", options);
		}
		BrowserMobProxyServer server = new BrowserMobProxyServer();
		server.start(0);
		Proxy proxy = ClientUtil.createSeleniumProxy(server);
		caps.setCapability(CapabilityType.PROXY, proxy);
		server.addRequestFilter(new RequestFilter() {

			@Override
			public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents,
					HttpMessageInfo messageInfo) {
				// TODO Auto-generated method stub
				request.headers().add("cookie",
						"_ga=GA1.2.1174513810.1541560894; _gid=GA1.2.1412481170.1541560894; form_key=t8k59H0RZA958gMm; PHPSESSID=1al3vr6st09j62mc1iau3n9634; geoip_default_store=hk; soon_cookienotice=1");
				return null;
			}
		});

	}
	public int innerSite() {
		if (flag) {
			int i = innerQueue.size();
			return i;
		} else {
			return 100;
		}
	}
	
	/**
	 * 取出driver
	 * @param uniqueFlag 
	 * @return
	 */
	public WebDriver get(Boolean uniqueFlag) {
		if(uniqueFlag) {
			poolSize = 1;
		}
		WebDriver driver = null;
		int size = innerQueue.size();
		LOG.info("-----innerQueue.size()为："+innerQueue.size());
		if(size>0){
			try {
				driver = innerQueue.take();
			} catch (InterruptedException e) {
				LOG.debug("Pool中取出driver阻塞异常--{}",e.getMessage());
				e.printStackTrace();
			}
			if (driver != null) {
				LOG.info("--Pool中取出一个driver--{}",driver);
//				outQueue.add(driver);
				outQueue.offer(driver);
				flag = true;
				return driver;
			}
		}
		if(!flag) {
			synchronized (innerQueue) {
				if(changeCount.get() < poolSize) {
					while(changeCount.get() < poolSize) {
						System.setProperty("webdriver.chrome.driver", webDriverPath);
						RemoteWebDriver remoteDriver = new ChromeDriver(caps);
						remoteDriver = setWebDriverTimeout(remoteDriver);
						innerQueue.add(remoteDriver);
						LOG.error("Pool创建webDriver--{}",changeCount.get());
						changeCount.incrementAndGet();
					}
				}
				
			}
		}
		
		
		try {
			driver = innerQueue.take();
			if(driver!=null) {
				flag = true;
			}
		} catch (InterruptedException e) {
			LOG.error("Pool中取出driver阻塞异常--{}",e.getMessage());
			e.printStackTrace();
		}
//		changeCount.decrementAndGet();
//		outQueue.add(driver);
		outQueue.offer(driver);
		return driver;
	}
	
	/**
	 * 返还driver
	 * @param driver
	 */
	public void returnToPool(WebDriver driver) {
		if(driver!=null) {
//			changeCount.incrementAndGet();
			innerQueue.add(driver);
//			失败的话，不阻塞，会抛出异常
//			outQueue.remove(driver);
		}
	}
	
	/**
	 * 关闭所有driver
	 */
	public void shutdownEnd() {
		for (WebDriver driver : innerQueue) {
			driver.quit();
			LOG.info("关闭innerQueue中所有driver------" + driver);
		}
		innerQueue.clear();
		for (WebDriver driver : outQueue) {
			driver.quit();
			LOG.info("关闭outQueue中所有driver------" + driver);
		}
		outQueue.clear();
	}
	
	private RemoteWebDriver setWebDriverTimeout(RemoteWebDriver mDriver){
//		页面超时时间,避免phantomjs卡住
		mDriver.manage().timeouts().pageLoadTimeout(360, TimeUnit.SECONDS);
//		脚本超时时间
		mDriver.manage().timeouts().setScriptTimeout(240, TimeUnit.SECONDS);
//		隐性等待，不推荐使用
//		mDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return mDriver;
	}
	
	
}

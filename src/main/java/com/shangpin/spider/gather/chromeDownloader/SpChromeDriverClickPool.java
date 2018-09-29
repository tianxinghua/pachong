package com.shangpin.spider.gather.chromeDownloader;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.gather.downloader.WebDriverPool;

public class SpChromeDriverClickPool extends WebDriverPool{
	private final static Logger LOG = LoggerFactory.getLogger(SpChromeDriverClickPool.class);
	/**
	 * 初始化的标识
	 */
	private volatile boolean flag = false;
	/**
	 * 默认队列容量5
	 */
	private volatile int poolSize = 5;
	private volatile BlockingDeque<WebDriver> innerQueue = null;
	
//	记录Pool中的driver的数量变化
	private volatile AtomicInteger changeCount = new AtomicInteger(0);
	
	private String webDriverPath;
	
	private static DesiredCapabilities caps = DesiredCapabilities.chrome();
	
	public SpChromeDriverClickPool(int poolSize, String webDriverPath) {
		super();
		this.poolSize = poolSize;
		this.innerQueue = new LinkedBlockingDeque<WebDriver>(poolSize);
		this.webDriverPath = webDriverPath;
//		caps.setCapability(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY,webDriverPath);
		caps.setJavascriptEnabled(true);
		caps.setCapability("takesScreenshot", false);
		caps.setCapability("acceptSslCerts", true);
		//css搜索支持
		caps.setCapability("cssSelectorsEnabled", true);
		
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
	 * @return
	 */
	public synchronized WebDriver get() {
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
				flag = true;
				return driver;
			}
		}
		if(!flag) {
			if(changeCount.get() < poolSize) {
				while(changeCount.get() < poolSize) {
					synchronized (innerQueue) {
						System.setProperty("webdriver.chrome.driver", webDriverPath);
						RemoteWebDriver remoteDriver = new ChromeDriver(caps);
						remoteDriver = setWebDriverTimeout(remoteDriver);
						innerQueue.add(remoteDriver);
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
		changeCount.decrementAndGet();
		return driver;
	}
	
	/**
	 * 返还driver
	 * @param driver
	 */
	public synchronized void returnToPool(WebDriver driver) {
		if(driver!=null) {
			changeCount.incrementAndGet();
			innerQueue.add(driver);
		}
	}
	
	/**
	 * 关闭所有driver
	 */
	public void shutdownEnd() {
		for (WebDriver driver : innerQueue) {
			driver.quit();
			LOG.info("关闭所有driver------" + driver);
		}
		innerQueue.clear();
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

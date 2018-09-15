package com.shangpin.spider.gather.downloader;

import org.openqa.selenium.WebDriver;

/**
 * @date 2018-09-12 14:03:32
 * @author njt
 *
 */
public abstract class WebDriverPool {
	
	public WebDriver get() {
		return null;
	};
	
	public void returnToPool(WebDriver driver) {};
	
	public void shutdownEnd() {};
}

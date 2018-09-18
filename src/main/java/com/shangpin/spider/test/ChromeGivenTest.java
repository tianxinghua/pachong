package com.shangpin.spider.test;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class ChromeGivenTest {
	
	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "D:/software/driver/chromedriver2.38.exe");
//		DesiredCapabilities caps = DesiredCapabilities.chrome();
//		caps.setJavascriptEnabled(true);
//		caps.setCapability("takesScreenshot", false);
//		caps.setCapability("acceptSslCerts", true);
//		//css搜索支持
//		caps.setCapability("cssSelectorsEnabled", true);
//		
//		ChromeOptions options = new ChromeOptions();
//		options.addArguments("--User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
//		options.addArguments("--Content-Type: text/plain;charset=UTF-8");
//		options.addArguments("--cookie=store=gb; _wt.session=56657be5-0d87-44db-aabc-44c53e6d39dd; mage-translation-storage=%7B%7D; mage-translation-file-version=%7B%7D; D_SID=122.112.116.222:zhkUFgFXmN1cJSt7QHRwL843Ys6dpvCjR5l9QUSCetU; rxVisitor=1537150116338SS1A5UGR11MDCGHO44VT4DPJU04JSQNS; dtSa=-; _ga=GA1.2.1808296314.1537150118; _gid=GA1.2.1500684367.1537150118; PAPVisitorId=JtIUabbwfJcF0qUeijLDRebG2yVVuQjN; __exponea_etc__=4e3cfda7-f795-4066-bfbd-58bdefab472b; form_key=0FmYV9SmsmGGAiMo; _wt.mode-1457312=WT3qiL0Mj3cat0~; mage-cache-storage=%7B%7D; mage-cache-storage-section-invalidation=%7B%7D; mage-messages=%5B%5D; _wt.control-1457312-ta_NavigationDropdown2=WT3ttLBqvRpEZxZxj_ukXScwCXZTTWNMQGRUzJEtjwKSvUUVUOECsFjLBOkvMGKeUWGr2i7I_G4x5x_V0PL9_e7G180Pg0pe4VSwmqpdCtacjz5aqsBK17XhMakA1Pl5nw-j2Er6QoAf4gwoq65AGlDF6Oh8GN9_MN00wQASgimO94oBvenKdsM0TDDv7GB8oPuU5wQsne6GNjnYCX324w8U9mG_ajrSyeynlsV8zMQtYqju_2HOKl-DO-ys0W5bFaKX7Hy9A1PjCBpsBWj; PHPSESSID=p7pjsqm6ir0bcn3onudb5i3791; private_content_version=66c6069f04c77287f0ecaf2218894ad5; __zlcmid=oRheEog8lj02EM; newscookie=newscookie; mage-cache-sessid=true; section_data_ids=%7B%22cart%22%3A1537150182%2C%22customer%22%3A1537150180%2C%22compare-products%22%3A1537150180%2C%22magepal-gtm-jsdatalayer%22%3A1537150180%2C%22react-eventnotification%22%3A1537150180%2C%22last-ordered-items%22%3A1537150180%2C%22exponea-customer-data%22%3A1537150180%2C%22directory-data%22%3A1537150181%2C%22wishlist%22%3A1537150180%2C%22multiplewishlist%22%3A1537150180%2C%22paypal-billing-agreement%22%3A1537150180%7D; _wt.user-1457312=WT3UHeTqh3V1hy9l2KJrmJUmau7rZ374pDKGLqztwqVAKPWIaxNnponBR-UTx12kN38qqoXnylYM4ovbnvm22dl5PbmP4ft3bvUD6C7pKrgqOORMxrUtaw5Bdbgivehl8MmJY8BkLPwwu8IDI1ydVXbYUaVdL5NKV4WaIhcNeJQgVQFG0Q7i9wjhSrseq-DL0iAw5BWEjINQgRJ3IpLjUjiLnyDZ0U~; AKA_A2=A; __exponea_time2__=-1.061147928237915; D_IID=689C418E-A387-3142-AA52-ED7F0A8E99BC; D_UID=254203B8-9D3D-387C-B74E-D9A63CC46E53; D_ZID=35842707-E4FE-37B2-B188-4C04613ABAA3; D_ZUID=718DCE2C-E207-357F-BF94-705F4785AA93; D_HID=096A897A-AF5D-376E-9C14-031A474C7FB8; stc111813=env:1537154332%7C20181018031852%7C20180917034852%7C1%7C1017719:20190917031852|uid:1537150120058.1572489630.935657.111813.1290510679.:20190917031852|srchist:1017719%3A1537154332%3A20181018031852:20190917031852|tsa:1537154332602.1906006498.1911278.6971091340160174.:20180917034852; rxvt=1537156133387|1537150116344; dtPC=4$554329941_647h-vCEBTOIVHUSBSXBUVFXLXAXDSAAHAVUJTFWPX; dtLatC=361; WT_FPC=id=d009734c-7f95-4889-bb6d-d040fbcfb9c7:lv=1537096734537:ss=1537092520423; dtCookie=4$GEM6SL2NM0IP2NFO1T5S4265NK8N5OFS|063974da2eae43d3|1");
//		options.addArguments("--upgrade-insecure-requests=1");
//		options.addArguments("--accept=text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		options.addArguments("method: GET");
//		options.addArguments("--headless:true");
//		options.addArguments("--:path: /eclddxbuqxwsryaxufxacftvazvsrxuydqrwxw.js?PID=F6F1D317-7D89-32DA-BE50-388CA5ADDD95");
//		caps.setCapability("chromeOptions", options);
		ChromeDriver driver = new ChromeDriver();
		String url = "https://www.endclothing.com/gb/givenchy-logo-polo-bm709b3006-420.html";
//		driver.navigate().to(url);
		driver.get(url);
		String name = driver.findElement(By.cssSelector("#maincontent > div > div > div > section:nth-child(1) > div > div.page-title-wrapper.row.c-page-title.product > h1 > span")).getText();
		System.out.println("名称为："+name);
	}

}

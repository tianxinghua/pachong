package com.shangpin.spider.gather.utils.ClickUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.entity.gather.CrawlResult;
import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.gather.chromeDownloader.SpChromeDriverClickPool;
import com.shangpin.spider.gather.crawlData.ReflectTypeMap;
import com.shangpin.spider.gather.utils.GatherUtil;

/**
 * 需两层点击后获取值
 * 
 * @author njt
 * @date 创建时间：2018年9月13日 上午02:32:01
 * @version 1.0
 * @parameter
 */
public abstract class MoreClickUtil {
	private static Logger LOG = LoggerFactory.getLogger(MoreClickUtil.class);

	/**
	 * 需点击字段及规则的映射
	 */
	protected Map<String, Map<String, String>> clickFieldRulesMap = null;

	public static ThreadLocal<ArrayList<Map<String, String>>> localList = new ThreadLocal<ArrayList<Map<String, String>>>();

	private static SpChromeDriverClickPool driverPool = null;

//	private static final ReentrantLock rtl = new ReentrantLock();
	/**
	 * 
	 * @param clickFieldMap
	 * @param needClickField
	 * @param spiderRuleInfo
	 * @param url
	 * @param crawlResult
	 * @param page
	 * @param page
	 * @param resultMap
	 * @return
	 */
	public void crawlByClick(String[] needClickFieldAry, String[] menuRuleArray,
			Map<String, Map<String, String>> clickFieldMap, SpiderRules spiderRuleInfo, String url,
			CrawlResult crawlResult, List<CrawlResult> crawlList) {
//		rtl.lock();
		clickFieldRulesMap = clickFieldMap;
		if (spiderRuleInfo.getDriverPool() != null) {
			driverPool = spiderRuleInfo.getDriverPool();
		}
		try {
			Class<?> resultClass = Class.forName(CrawlResult.class.getName());
			Field[] resultFields = resultClass.getDeclaredFields();
//			处理点击
			List<Map<String, String>> resultList = handleClick(url, menuRuleArray);

			if (resultList != null && resultList.size() > 0) {
				for (Map<String, String> map : resultList) {
					CrawlResult crawlResultNew = new CrawlResult();
					crawlResultNew = (CrawlResult) crawlResult.clone();
					for (String clickfield : needClickFieldAry) {
						if (map.containsKey(clickfield)) {
							String clickValue = map.get(clickfield);
							for (Field resultField : resultFields) {
								resultField.setAccessible(true);
								String typeName = resultField.getGenericType().toString();
								String resultFieldName = resultField.getName();
								if (clickfield.equals(resultFieldName)) {
									ReflectTypeMap.typeMap(typeName, clickValue, resultFieldName, resultField,
											crawlResultNew);
									break;
								}
							}
						}
					}

					if (StringUtils.isNotBlank(crawlResultNew.getSpu())) {
//						以spu和size定唯一
						crawlResultNew.setSppuHash(
								GatherUtil.longHashCode(crawlResultNew.getSpu() + crawlResultNew.getSize()));
					} else {
						crawlResultNew.setSppuHash(0L);
					}
					if (StringUtils.isNotBlank(crawlResultNew.getSpu())) {
						crawlResultNew.setProductModel(crawlResultNew.getSpu());
					}
					crawlList.add(crawlResultNew);
				}
			} else {
				LOG.info("链接{}点击后，数据为空！", url);
			}

		} catch (IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
//			rtl.unlock();
			LOG.info("点击获取{}数据结束---！", url);
		}
	}

	private List<Map<String, String>> handleClick(String url, String[] menuRuleArray) {
//		从Pool里取出driver
		ChromeDriver driver = null;
		List<Map<String, String>> resultList = null;
		try {
			if (driverPool != null) {
				driver = (ChromeDriver) driverPool.get();
			}
			driver.get(url);
			driver.manage().window().maximize();
			initClick(driver);
			resultList = executeClick(driver, url, menuRuleArray);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driverPool.returnToPool(driver);
		}
		return resultList;
	}

	public abstract void initClick(ChromeDriver driver);

	public abstract List<Map<String, String>> executeClick(ChromeDriver driver, String url, String[] menuRuleArray);
}

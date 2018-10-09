package com.shangpin.spider.gather.utils.ClickUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author njt
 * @date 2018年10月9日 下午2:48:53
 * @desc
 * OneClick
 */
public class OneClick extends MoreClickUtil{
	
	private static Map<String, Map<String, String>> clickFieldRulesMap = null;
//	计数器，第一层的下标
	private static ThreadLocal<AtomicInteger> init_i = new ThreadLocal<AtomicInteger>();
	private static ThreadLocal<AtomicInteger> endLocalInt = new ThreadLocal<AtomicInteger>();
	private static AtomicInteger endInt = endLocalInt.get();
	private static ThreadLocal<Integer> firstLocalSize = new ThreadLocal<Integer>();
	private static Integer firstSize = firstLocalSize.get();
	private static Logger LOG = LoggerFactory.getLogger(OneClick.class);
	
	@Override
	public void initClick() {
		clickFieldRulesMap = super.clickFieldRulesMap;
	}
	
	@Override
	public List<Map<String, String>> executeClick(ChromeDriver driver, String url, String[] menuRuleArray) {
		endInt = new AtomicInteger(0);
		firstSize = 0;
		AtomicInteger initI = init_i.get();
		initI = new AtomicInteger(0);
		ThreadLocal<Boolean> recursionLocalFlag = new ThreadLocal<Boolean>();
		Boolean recursionFlag = recursionLocalFlag.get();
		recursionFlag = false;
		List<Map<String, String>> list = localList.get();
		list = new ArrayList<Map<String, String>>();
		oneClick(list, driver, initI, recursionFlag, url, menuRuleArray);
		initI = new AtomicInteger(0);
		return null;
	}
	
	private List<Map<String, String>> oneClick(List<Map<String, String>> list, ChromeDriver driver,
			AtomicInteger initI, Boolean recursionFlag, String url, String[] menuRuleArray) {
		int i = initI.get();
//		第一步，模拟点击第一个动态元素
		List<WebElement> elements1 = null;
		try {
			elements1 = driver.findElements(By.cssSelector(menuRuleArray[0]));
		} catch (Exception e) {
			LOG.error("链接{}模拟点击的第一层元素NO SUCH ELEMENT,错误信息：{}", url, e.getMessage());
		}

//		第一层动态元素的个数（一般第一层元素个数是不变的，后面层级的元素是随着父级改变的）
		firstSize = elements1.size();
		System.err.println("----\t第一层的元素个数为：" + firstSize);
		System.err.println("----\t第一层此时的下标为：" + i);
		if (i + 1 > firstSize) {
			System.err.println("----\t第一层下标越界。");
			return list;
		}
		WebElement element = null;
		if (recursionFlag || (i == 0)) {
			if (firstSize != 1) {
				recursionFlag = false;
				element = elements1.get(i);
				try {
//					WebDriverWait wait = new WebDriverWait(driver, 10);
//					wait.until(ExpectedConditions.elementToBeClickable(element));
					element.click();
				} catch (Exception e) {
					LOG.error("链接{}第一次点击事件有误！", url);
				}
//				避免点击频繁，异步加载
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (i != firstSize - 1) {
			initI.incrementAndGet();
		}
		recursionFlag = true;
		if ((!recursionFlag) || (i != firstSize - 1)) {
//			点击后获取的字段值，在此获取
			list = AnalyticData.handleClickFieldRulesMap(url, list, driver, clickFieldRulesMap);
//			递归			
			oneClick(list, driver, initI, recursionFlag, url, menuRuleArray);
		}
//		确保最后一次入库
		if (endInt.get() == 0) {
			if (i == firstSize - 1) {
				LOG.info("{}链接最后一次点击入库！", url);
				list = AnalyticData.handleClickFieldRulesMap(url, list, driver, clickFieldRulesMap);
				endInt.incrementAndGet();
			}
		}

		return list;
	}


}

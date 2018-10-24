package com.shangpin.spider.gather.utils.ClickUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.gather.utils.CrackDspiderUtil;

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
	private static ThreadLocal<AtomicInteger> firstLocalSize = new ThreadLocal<AtomicInteger>();
//	private static Integer firstSize = firstLocalSize.get();
	private static Logger LOG = LoggerFactory.getLogger(OneClick.class);
	
	@Override
	public void initClick(ChromeDriver driver) {
		clickFieldRulesMap = super.clickFieldRulesMap;
		//		测试--网站有弹窗的情况，用X的CSS捕获到，解除反爬
		CrackDspiderUtil.crackMask(driver, super.spiderRuleInfo);
	}
	
	@Override
	public List<Map<String, String>> executeClick(ChromeDriver driver, String[] menuRuleArray, String oneClickedRules, String oneClickedStrategy) {
		endInt = new AtomicInteger(0);
		AtomicInteger firstSizeAtom = firstLocalSize.get();
		firstSizeAtom = new AtomicInteger(0);
		AtomicInteger initI = init_i.get();
		initI = new AtomicInteger(0);
		ThreadLocal<Boolean> recursionLocalFlag = new ThreadLocal<Boolean>();
		Boolean recursionFlag = recursionLocalFlag.get();
		recursionFlag = false;
		List<Map<String, String>> list = localList.get();
		list = new ArrayList<Map<String, String>>();
		list = oneClick(list, driver, initI, firstSizeAtom, recursionFlag, menuRuleArray, oneClickedRules);
		initI = new AtomicInteger(0);
		return list;
	}
	
	private List<Map<String, String>> oneClick(List<Map<String, String>> list, ChromeDriver driver,
			AtomicInteger initI, AtomicInteger firstSizeAtom, Boolean recursionFlag, String[] menuRuleArray, String oneClickedRules) {
//		确保网页点击后链接发生改变。
		String url = driver.getCurrentUrl();
		int i = initI.get();
		int firstSize = firstSizeAtom.get();
//		第一步，模拟点击第一个动态元素
		List<WebElement> elements1 = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 1);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(menuRuleArray[0])));
			elements1 = driver.findElements(By.cssSelector(menuRuleArray[0]));
		} catch (Exception e) {
			LOG.error("链接{}模拟点击的第一层元素NO SUCH ELEMENT,错误信息：{}", url, e.getMessage());
		}

//		第一层动态元素的个数（一般第一层元素个数是不变的，后面层级的元素是随着父级改变的）
		firstSize = elements1.size();
		System.err.println("----链接："+url);
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
//					WebDriverWait wait = new WebDriverWait(driver, 2);
//					wait.until(ExpectedConditions.elementToBeClickable(element));
//					element.click();
					if(i==0) {
						if(judgeOneClicked(driver, oneClickedRules)) {
							((JavascriptExecutor)driver).executeScript("arguments[0].click()", element);
						}
					}else {
						((JavascriptExecutor)driver).executeScript("arguments[0].click()", element);
					}
					
				} catch (Exception e) {
					LOG.error("链接{}第一次点击事件有误！", url);
					e.printStackTrace();
				}
//				避免点击频繁，异步加载
				try {
					Thread.sleep(1000);
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
			list = AnalyticData.handleClickFieldRulesMap(url, list, driver, clickFieldRulesMap, i, true);
//			递归	
			firstSizeAtom = new AtomicInteger(firstSize);
			oneClick(list, driver, initI, firstSizeAtom, recursionFlag, menuRuleArray,  oneClickedRules);
		}
//		确保最后一次入库
		if (endInt.get() == 0) {
			if (i == firstSize - 1) {
				LOG.info("{}链接最后一次点击入库！", url);
				list = AnalyticData.handleClickFieldRulesMap(url, list, driver, clickFieldRulesMap, i, true);
				endInt.incrementAndGet();
			}
		}

		return list;
	}


}

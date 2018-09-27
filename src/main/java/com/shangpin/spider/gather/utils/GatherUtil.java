package com.shangpin.spider.gather.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.spider.common.StrategyConstants;
import com.shangpin.spider.common.SymbolConstants;
import com.shangpin.spider.entity.gather.SpiderRules;

import us.codecraft.webmagic.Page;

/**
 * @author njt
 * @date 创建时间：2017年11月22日 下午2:24:16
 * @version 1.0
 * @parameter
 */

public class GatherUtil {
	private final static Logger LOG = LoggerFactory.getLogger(GatherUtil.class);
	/**
	 * 通过链接截取域名
	 * 
	 * @param referrer
	 * @return
	 */
	public static String getFefferrerHost(String referrer) {
		if (SymbolConstants.REFER_MARK.equals(referrer)) {
			return SymbolConstants.REFER_MARK;
		}
		if (referrer.contains(SymbolConstants.QUESTION_MARK)) {// 去掉参数
			referrer = referrer.substring(0, referrer.indexOf(SymbolConstants.QUESTION_MARK));
		}
		// //pos
		int startPos = referrer.indexOf(SymbolConstants.DOUBLE_SLASH);
		if (startPos > 0) {
			startPos = startPos + SymbolConstants.DOUBLE_SLASH.length();
			if (startPos > referrer.length()) {
				return SymbolConstants.REFER_MARK;
			}

			int endPos = referrer.indexOf(SymbolConstants.SINGLE_SLASH, startPos);
			if (endPos > -1) {
				return referrer.substring(startPos, endPos);
			} else {
				return referrer.substring(startPos);
			}
		} else {
			if (referrer.startsWith(SymbolConstants.DOUBLE_SLASH)) {
				startPos = startPos + SymbolConstants.DOUBLE_SLASH.length();
				int endPos = referrer.indexOf(SymbolConstants.SINGLE_SLASH, startPos);
				if (endPos == -1) {
					endPos = referrer.length();
				}
				return referrer.substring(startPos, endPos);
			}
		}
		return SymbolConstants.REFER_MARK;
	}

	/**
	 * 将字符创转换为long hashcode值
	 * 
	 * @param s
	 * @return
	 */
	public static Long longHashCode(String s) {
		if (s == null) {
			return null;
		}
		Long h = 0L;
		char val[] = s.toCharArray();
		if (val.length > 0) {
			for (int i = 0; i < val.length; i++) {
				h = 31 * h + val[i];
			}
		}
		return h;
	}

	/**
	 * 获取纯文本的正文内容
	 * @param content
	 * @param flag(正文是否带样式的标识)
	 * @return
	 */
	public static String getContent(String content, boolean flag) {
		content = content.replaceAll("\r", " ");
		content = content.replaceAll("\t", " ");
//		content = content.replace("***", "<br/>");
		content = content.replace("\n", "<br/>");
		content = content.replaceAll("(\\<br/\\>\\s*){2,}", "<br/> ");
		content = content.replaceAll("(&nbsp;\\s*)+", " ");
		// remove the header
		content = content.replaceAll("(<head>).*(</head>)", "");
		content = content.replaceAll("<( )*script([^>])*><script>", "");
		content = content.replaceAll("(<script>).*(</script>)", "");
		content = content.replaceAll("<( )*noscript([^>])*><noscript>", "");
		content = content.replaceAll("(<noscript>).*(</noscript>)", "");
		content = content.replaceAll("<noscript>.*</noscript>", "");
		content = content.replaceAll("(<noScript>).*(</noScript>)", "");
		content = content.replaceAll("<noScript>.*</noScript>", "");

		// remove all styles
		content = content.replaceAll("<( )*style([^>])*><style>", "");
		// clearing attributes
		content = content.replaceAll("(<style>).*(</style>)", "");
		// insert tabs in spaces of <td> tags
		content = content.replaceAll("<( )*td([^>])*>", "");
		// insert line breaks in places of <tr> and <li> tags
		content = content.replaceAll("<( )*tr([^>])*>", "");
		content = content.replaceAll("<( )*li( )*>", "");
		// insert line paragraphs in places of <br> and <p> tags
		if(flag){
			content = content.replaceAll("<( )*br( )*>", "");
			content = content.replaceAll("<( )*p([^>])*>", "");
		}
		// remove anything thats enclosed inside < >
//		content = content.replaceAll("<[^>]*>", "");
		// replace special characters:
		content = content.replaceAll("&", "");
		content = content.replaceAll(" ", "");
		if(flag){
			content = content.replaceAll("<([\\s\\S]*?)>", "");
			content = content.replaceAll("<", "");
			content = content.replaceAll(">", "");
			content = content.replaceAll("&(.{2,6});", "");
		}
		// remove extra line breaks and tabs
		content = content.replaceAll(" ( )+", "");
		content = content.replaceAll("(\r)( )+(\r)", "");
		content = content.replaceAll("(\r\r)+", "");
		return content;
	}

	/**
	 * 绝对路径
	 * 
	 * @param doc
	 * @param url
	 */
	public static void makeAbs(Document doc, String url) {
		if (url != null) {
			doc.setBaseUri(url);
		}
		doc.traverse(new NodeVisitor() {
			@Override
			public void head(Node node, int i) {
				if (node instanceof Element) {
					Element tag = (Element) node;
					if (tag.hasAttr(SymbolConstants.HREF)) {
						String absHref = tag.attr(SymbolConstants.ABS_HREF);
						tag.attr(SymbolConstants.HREF, absHref);
					}
					if (tag.hasAttr(SymbolConstants.SRC)) {
						String absSrc = tag.attr(SymbolConstants.ABS_SRC);
						tag.attr(SymbolConstants.SRC, absSrc);
					}
				}
			}

			@Override
			public void tail(Node node, int i) {
			}
		});
	}
	/**
	 * 详情页的正则判断
	 * @param url
	 * @param spiderRuleInfo
	 * @return
	 */
	public static Boolean isDetailUrl(String url,SpiderRules spiderRuleInfo) {
		Boolean flag = false;
		if(StringUtils.isNotBlank(spiderRuleInfo.getDetailUrlReg())) {
			Pattern detailPattern = Pattern.compile(spiderRuleInfo.getDetailUrlReg());
			Matcher detailMatcger = detailPattern.matcher(url);
			if(detailMatcger.find()) {
				String group = detailMatcger.group();
				if(url.equals(group)) {
					LOG.info("---详情页链接{}！",url);
					flag = true;
				}
				
			}
		}else {
			LOG.error("---源网站-{}-的详情链接正则规则为空！",spiderRuleInfo.getWhiteName());
		}
		return flag;
	}
	/**
	 * 列表页的正则判断
	 * @param url
	 * @param spiderRuleInfo
	 * @return
	 */
	public static Boolean isLieUrl(String url,SpiderRules spiderRuleInfo) {
		Boolean flag = false;
		if(StringUtils.isNotBlank(spiderRuleInfo.getLieUrlReg())) {
			Pattern liePattern = Pattern.compile(spiderRuleInfo.getLieUrlReg());
			Matcher lieMatcger = liePattern.matcher(url);
			if(lieMatcger.find()) {
				String group = lieMatcger.group();
				if(url.equals(group)) {
					LOG.info("---列表页链接{}！",url);
					flag = true;
				}
				
			}
		}else {
			LOG.error("---源网站-{}-的列表链接正则规则为空！",spiderRuleInfo.getWhiteName());
		}
		return flag;
	}
	/**
	 * 过滤链接的正则判断
	 * @param url
	 * @param spiderRuleInfo
	 * @return
	 */
	public static Boolean isFilterUrl(String url,SpiderRules spiderRuleInfo) {
		Boolean flag = false;
		if(StringUtils.isNotBlank(spiderRuleInfo.getFilterUrlReg())) {
			Pattern filterPattern = Pattern.compile(spiderRuleInfo.getFilterUrlReg());
			Matcher filterMatcger = filterPattern.matcher(url);
			if(filterMatcger.find()) {
				LOG.info("---需过滤链接{}！",url);
				flag = true;
			}
		}else {
			LOG.error("---源网站-{}-的过滤链接正则规则为空！",spiderRuleInfo.getWhiteName());
		}
		
		return flag;
	}
	/**
	 * 处理DE-C策略（针对多图片，多颜色，多尺寸）
	 * @param gg 规则
	 * @param i 下标
	 * @param map 结果集
	 * @param driver 
	 * @return 
	 */
	private static Map<String, Object> handleDEC(String[] gg,int i,Map<String, Object> map) {
		Page page = (Page) map.get("page");
		Document document = page.getHtml().getDocument();
		String specialValue = "";
//		去除/n/t等空格
		String detailRule = gg[i].trim();
		if(i==gg.length-1) {
			if(detailRule.contains(SymbolConstants.ATTR_FLAG)){
				String attrRule = detailRule.substring(detailRule.indexOf(SymbolConstants.ATTR_FLAG)+SymbolConstants.ATTR_FLAG.length(), detailRule.length());
				detailRule = detailRule.substring(0,detailRule.indexOf(SymbolConstants.ATTR_FLAG));
				if(map.containsKey("elements")) {
					Elements elements = (Elements) map.get("elements");
					Elements elements2 = elements.select(detailRule);
					for (Element element : elements2) {
						specialValue += element.attr(attrRule).toString()+SymbolConstants.SPLIT_FLAG;
					}
					if(specialValue.endsWith(SymbolConstants.SPLIT_FLAG)) {
						specialValue = specialValue.substring(0, specialValue.length()-SymbolConstants.SPLIT_FLAG.length());
					}
				}else {
					Elements elements = document.select(detailRule);
					for (Element element : elements) {
						specialValue += element.attr(attrRule).toString()+SymbolConstants.SPLIT_FLAG;
					}
					if(specialValue.endsWith(SymbolConstants.SPLIT_FLAG)) {
						specialValue = specialValue.substring(0, specialValue.length()-SymbolConstants.SPLIT_FLAG.length());
					}
				}
			}else {
				if(map.containsKey("elements")) {
					Elements elements = (Elements) map.get("elements");
					specialValue = elements.select(detailRule).text();
				}else {
					specialValue = document.select(detailRule).text();
				}
				
			}
		}else {
			Elements elements = null;
			if(map.containsKey("elements")) {
				elements = (Elements) map.get("elements");
				elements = elements.select(detailRule);
			}else {
				elements = document.select(detailRule);
			}
			map.put("elements", elements);
			i++;
			map.put("specialValue", specialValue);
			handleDEC(gg,i,map);
			if(map.containsKey("specialValue")) {
				specialValue = (String) map.get("specialValue");
			}
		}
		map.put("specialValue", specialValue);
		return map;
	}
	
	
	/**
	 * 
	 * @param page 网页
	 * @param driver 浏览器内核驱动
	 * @param crawlValue 获取的值
	 * @param strategyStr 抓取的策略字符串
	 * @param rulesStr 抓取的规则字符串
	 * @return result
	 */
	public static String getValue(Page page, String crawlValue, String strategyStr, String rulesStr, String fieldName) {
		/*Html html = page.getHtml();
		Document document = page.getHtml().getDocument();
		System.out.println("--源码：--"+html);*/
		if(StringUtils.isBlank(strategyStr)||StringUtils.isBlank(rulesStr)) {
			LOG.error("---字段-{}-的取值正则规则为空！",fieldName);
			return "";
		}
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("strategyStr", strategyStr);
		resultMap.put("crawlValue", crawlValue);
		String[] gg = null;
		if(rulesStr.contains(SymbolConstants.RULE_SPLIT_FLAG)) {
			gg = rulesStr.split(SymbolConstants.RULE_SPLIT_FLAG);
		}else {
			gg = new String[]{rulesStr};
		}
//		对重复XPATH策略的处理(出发点针对图片路径)
		if(strategyStr.contains(StrategyConstants.DE_X)) {
			for (String g : gg) {
				try {
					crawlValue += page.getHtml().xpath(g).get()+SymbolConstants.SPLIT_FLAG;
				} catch (Exception e) {
					LOG.error("--字段{}的-规则{}-取值为空！异常：{}",fieldName,"DE-X",e.getMessage());
				}
				
			}
			if(crawlValue.length()>0) {
				crawlValue = crawlValue.substring(0, crawlValue.length()-1);
			}
			return crawlValue;
		}
//		针对颜色和尺寸的处理
		if(strategyStr.contains(StrategyConstants.DE_C)) {
			int i = 0;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("page", page);
			map = handleDEC(gg,i,map);
			return (String) map.get("specialValue");
		}
//		对填写默认值策略的处理
		if(strategyStr.contains(StrategyConstants.DE_S)) {
			return rulesStr.trim();
		}
//		对库存量的标识
		if(StrategyConstants.SP_QTY_C.equals(strategyStr)) {
			return handleQty(page, null, rulesStr);
		}
		
//		下标初始值100
//		策略：xpath,css,sub,reg，策略间以|，&间隔。
//		规则间以@,间隔。
		int X_INDEX = 100;int C_INDEX = 100;int SUB_INDEX = 100;int RG_INDEX = 100;
		Map<String,Integer> map = new HashMap<String,Integer>();
		if(strategyStr.contains(StrategyConstants.X)) {
			X_INDEX = strategyStr.indexOf(StrategyConstants.X);
			map.put(StrategyConstants.X, X_INDEX);
		}
		if(strategyStr.contains(StrategyConstants.C)) {
			C_INDEX = strategyStr.indexOf(StrategyConstants.C);
			map.put(StrategyConstants.C, C_INDEX);
		}
		if(strategyStr.contains(StrategyConstants.SUB)) {
			SUB_INDEX = strategyStr.indexOf(StrategyConstants.SUB);
			map.put(StrategyConstants.SUB, SUB_INDEX);
		}
		if(strategyStr.contains(StrategyConstants.RG)) {
			RG_INDEX = strategyStr.indexOf(StrategyConstants.RG);
			map.put(StrategyConstants.RG, RG_INDEX);
		}
		List<Entry<String,Integer>> list = rankMapByValue(map,new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return Integer.compare(o1, o2);
			}
		});
		System.err.println("\t\t<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		System.err.println("抓取--"+fieldName+"-开始--的策略顺序：");
//		第二步，确定策略间的关系
		int i = 0;
		for (Map.Entry<String,Integer> entry : list) {
			String deStrategy = entry.getKey();
            System.err.println("\tKey : " + deStrategy + " , Value : " + entry.getValue());
            resultMap = orAnd(page,resultMap,deStrategy,gg,i);
            i++;
        }
		System.err.println("抓取--"+fieldName+"-结果----："+resultMap.get("crawlValue")+"\n----"+fieldName+"策略剩余：---"+resultMap.get("strategyStr"));
		return crawlValue = resultMap.get("crawlValue");
	}
	
	private static <K, V> List<Entry<K, V>> rankMapByValue(Map<K, V> map,
            final Comparator<V> valueComparator) {
        List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>(
                map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return valueComparator.compare(o1.getValue(), o2.getValue());
            }
        });
        return list;
    }
	
	private static Map<String, String> orAnd(Page page,Map<String,String> resultMap,String deStrategy,String[] gg,int i) {
		String strategyStr = resultMap.get("strategyStr");
		String crawlValue = resultMap.get("crawlValue");
		String strategyFilterStr = strategyStr.replace(deStrategy, "");
		String detailRule = gg[i];
		if(i==0) {
			System.err.println("第一步："+gg[i]);
			crawlValue = xpathStrategy(deStrategy,detailRule,i,crawlValue,page);
			crawlValue = cssStrategy(deStrategy,detailRule,i,crawlValue,page);
		}else {	
			String symbol = strategyFilterStr.split("")[0];
			strategyFilterStr = strategyFilterStr.substring(1, strategyFilterStr.length());
			if(SymbolConstants.AND_FLAG.equals(symbol)) {
				System.err.println("且---的规则"+gg[i]);
				crawlValue = subStrategy(deStrategy,detailRule,i,crawlValue);
				crawlValue = regStrategy(deStrategy,detailRule,i,crawlValue);
			}
			if(SymbolConstants.SPLIT_FLAG.equals(symbol)) {
				System.err.println("或---的规则"+gg[i]);
				if(StringUtils.isBlank(crawlValue)) {
					crawlValue = cssStrategy(deStrategy,detailRule,i,crawlValue,page);
					crawlValue = subStrategy(deStrategy,detailRule,i,crawlValue);
					crawlValue = regStrategy(deStrategy,detailRule,i,crawlValue);
				}
			}
		}
		resultMap.put("strategyStr", strategyFilterStr);
		resultMap.put("crawlValue", crawlValue);
		return resultMap;
	}
	/**
	 * xpath的匹配
	 * @param deStrategy
	 * @param detailRule
	 * @param i
	 * @param crawlValue
	 * @param page
	 * @return
	 */
	private static String xpathStrategy(String deStrategy,String detailRule,int i,String crawlValue,Page page) {
		if(StrategyConstants.X.equals(deStrategy)) {
			try {
				crawlValue = page.getHtml().xpath(detailRule).get();
			} catch (Exception e) {
				LOG.error("---------XPATH解析方法异常{}",e.getMessage());
			}
		}
		return crawlValue;
	}
	/**
	 * css的匹配，具体规则涉及到具体属性以@|间隔
	 * @param deStrategy
	 * @param detailRule
	 * @param i
	 * @param crawlValue
	 * @param page
	 * @return
	 */
	private static String cssStrategy(String deStrategy,String detailRule,int i,String crawlValue,Page page) {
		if(StrategyConstants.C.equals(deStrategy)) {
			try {
				if(detailRule.contains(SymbolConstants.ATTR_FLAG)){
					String attrRule = detailRule.substring(detailRule.indexOf(SymbolConstants.ATTR_FLAG)+SymbolConstants.ATTR_FLAG.length(), detailRule.length());
					crawlValue = page.getHtml().getDocument().select(detailRule).attr(attrRule).toString();
				}else {
					crawlValue = page.getHtml().getDocument().select(detailRule).text();
				}
			} catch (Exception e) {
				LOG.error("---------CSS解析方法异常{}",e.getMessage());
			}
			
		}
		return crawlValue;
	}
	/**
	 * 截取的规则表达式格式{xxx@;xxx}（第二个xxx默认为length，表示截取到最后）
	 * @param deStrategy
	 * @param detailRule
	 * @param i
	 * @param crawlValue
	 * @return
	 */
	private static String subStrategy(String deStrategy,String detailRule,int i,String crawlValue) {
		if(StrategyConstants.SUB.equals(deStrategy)) {
			try {
				crawlValue = subCore(detailRule,crawlValue);
			} catch (Exception e) {
				LOG.error("---------SUB解析方法异常{}",e.getMessage());
			}
		}
		return crawlValue;
	}
	public static String subCore(String detailRule,String crawlValue) {
		if(detailRule.contains(SymbolConstants.LEFT_PART)&&detailRule.contains(SymbolConstants.RIGHT_PART)) {
			detailRule = detailRule.replace(SymbolConstants.LEFT_PART, "");
			detailRule = detailRule.replace(SymbolConstants.RIGHT_PART, "");
			if(detailRule.contains(SymbolConstants.SUB_SPLIT_FLAG)) {
				String[] detailRuleSplit = detailRule.split(SymbolConstants.SUB_SPLIT_FLAG);
				String detailRuleSplitOne = detailRuleSplit[0].trim();
				int oneLength = 0;
				int indexOf = 0;
//				若是截取包括前缀的，则以#T打头
				Boolean flag = true;
				if(detailRuleSplitOne.startsWith(SymbolConstants.SUB_SUFIX)) {
					detailRuleSplitOne = detailRuleSplitOne.replace(SymbolConstants.SUB_SUFIX, "");
				}else {
					flag = false;
					oneLength = detailRuleSplitOne.length();
				}
//				多种匹配按#OR的关系（符号易出错）
				if(detailRuleSplitOne.contains(SymbolConstants.SUB_FIRSTOR)) {
					String[] OneStr = detailRuleSplitOne.split(SymbolConstants.SUB_FIRSTOR);
					for (String subDe : OneStr) {
						if(crawlValue.indexOf(subDe)>0) {
							indexOf = crawlValue.indexOf(subDe);
							oneLength = subDe.length();
							break;
						}
					}
				}else {
					indexOf = crawlValue.indexOf(detailRuleSplitOne);
				}
				if(flag) {
					oneLength = 0;
				}
				String detailRuleSplitTwo = detailRuleSplit[1].trim();
				if(SymbolConstants.SUB_LENGTH.equals(detailRuleSplitTwo)) {
					crawlValue = crawlValue.substring(indexOf+oneLength, crawlValue.length());
				}else {
					int indexOf2 = crawlValue.indexOf(detailRuleSplitTwo);
					crawlValue = crawlValue.substring(indexOf+oneLength, indexOf2);
				}
			}else {
				LOG.error("规则配置---SUB表达式格式有误！！");
			}
		}else {
			LOG.error("规则配置---SUB表达式格式有误！！");
		}
		return crawlValue;
	}
	/**
	 * regex正则表达式，1，直接匹配出结果；2，匹配出要清除的字符或内容，正则表达式前加!开头
	 * @param deStrategy
	 * @param detailRule
	 * @param i
	 * @param crawlValue
	 * @param page
	 * @return
	 */
	private static String regStrategy(String deStrategy,String detailRule,int i,String crawlValue) {
		if(StrategyConstants.RG.equals(deStrategy)) {
			try {
				if(detailRule.startsWith(SymbolConstants.FALSE_MARK)) {
					Pattern compile = Pattern.compile(detailRule);
					Matcher matcher = compile.matcher(crawlValue);
					if(matcher.find()) {
						String filterValue = matcher.group();
						crawlValue.replaceAll(filterValue, "");
					}
				}else {
					Pattern compile = Pattern.compile(detailRule);
					Matcher matcher = compile.matcher(crawlValue);
					if(matcher.find()) {
						crawlValue = matcher.group();
					}
				}
			} catch (Exception e) {
				LOG.error("---------REG解析方法异常{}",e.getMessage());
			}
		}
		return crawlValue;
	}
	
	/**
	 * 过滤无需传值的字段
	 * @param resultFieldName
	 * @return
	 */
	public static Boolean filterField(String resultFieldName) {
		Boolean flag = false;
		switch(resultFieldName) {
			case "serialVersionUID":
				flag = true;
			case "id":
				flag = true;
			case "createTime":
				flag = true;	
			case "updateTime":
				flag = true;
			default:;
		}
		return flag;
	}
	
	/**
	 * 过滤需要点击后获取的字段
	 * @param resultFieldName
	 * @return
	 */
	public static Boolean filterNeedClick(String resultFieldName,String[] needClickField) {
		Boolean flag = false;
		for (String field : needClickField) {
			if(resultFieldName.equals(field)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	/**
	 * 用于driver获取数据
	 * @param clickfield 
	 * @param map2
	 * @param driver
	 * @return
	 * @DES C&SUB&UNION&C&SUB策略按顺序来，没有多种结合，串联一种得结果。规则按@,间隔。U以@U间隔。CSS的属性用@|标识。截取按照{#T@OR@;}规则进行
	 */
	public static String getFieldValue(String clickfield, Map<String, String> map2, ChromeDriver driver) {
		String rulesStr = map2.get("rulesStr");
		String strategyStr = map2.get("strategyStr");
		if(StrategyConstants.SP_C.equals(strategyStr)) {
			return handleDriverImg(driver, rulesStr);
		}
		if(StrategyConstants.SP_QTY_C.equals(strategyStr)) {
			return handleQty(null, driver, rulesStr);
		}
		String[] rulesArray = rulesStr.split(SymbolConstants.RULE_SPLIT_FLAG);
		String[] strategyArray = strategyStr.split(SymbolConstants.AND_FLAG);
		String fieldValue = "";
		Boolean unionFlag = false;
		for (int i = 0; i < strategyArray.length; i++) {
			String detailStrategy = strategyArray[i];
			String detailRule = rulesArray[i];
			if(StrategyConstants.C.equals(detailStrategy)) {
				if(detailRule.contains(SymbolConstants.ATTR_FLAG)) {
					String[] detailRuleArray = detailRule.split(SymbolConstants.ATTR_FLAG);
					if(unionFlag) {
						fieldValue += driver.findElement(By.cssSelector(detailRuleArray[0])).getAttribute(detailRuleArray[1]).toString();
					}else {
						fieldValue = driver.findElement(By.cssSelector(detailRuleArray[0])).getAttribute(detailRuleArray[1]).toString();
					}
				}else {
					if(unionFlag) {
						fieldValue += driver.findElement(By.cssSelector(detailRule)).getText();
					}else {
						fieldValue = driver.findElement(By.cssSelector(detailRule)).getText();
					}
					
				}
			}
			if(StrategyConstants.SUB.equals(detailStrategy)) {
				if(fieldValue=="") {
					LOG.error("----两层点击中，首次抓取"+clickfield+"策略C的值为空！");
				}else {
					if(unionFlag) {
						fieldValue += subCore(detailRule,fieldValue);
					}else {
						fieldValue = subCore(detailRule,fieldValue);
					}
				}
			}
			if(StrategyConstants.UNION.equals(detailStrategy)) {
				unionFlag = true;
			}
		}
		return fieldValue;
	}
	/**
	 * 处理qty，策略SP-QTY-C，规则中以@|标识属性，以@F标识判断是否有库存的字符，!放于字符前标识不包含该字符为有库存
	 * @param page 
	 * @param driver
	 * @param rulesStr
	 * @return
	 */
	private static String handleQty(Page page, ChromeDriver driver, String rulesStr) {
		String qtyFlag = "";
		String qtyFlagValue = "";
		if(rulesStr.contains(SymbolConstants.QTY_FLAG)) {
			qtyFlagValue = rulesStr.substring(rulesStr.indexOf(SymbolConstants.QTY_FLAG)+SymbolConstants.QTY_FLAG.length(),rulesStr.length());
			rulesStr = rulesStr.substring(0, rulesStr.indexOf(SymbolConstants.QTY_FLAG));
		}else {
			qtyFlagValue = "0";
		}
		if(driver!=null) {
			if(rulesStr.contains(SymbolConstants.ATTR_FLAG)) {
				String[] rulesArray = rulesStr.split(SymbolConstants.ATTR_FLAG);
				qtyFlag = driver.findElement(By.cssSelector(rulesArray[0])).getAttribute(rulesArray[1]);
			}else {
				qtyFlag = driver.findElement(By.cssSelector(rulesStr)).getText();
			}
		}
		if(page!=null) {
			if(rulesStr.contains(SymbolConstants.ATTR_FLAG)) {
				String[] rulesArray = rulesStr.split(SymbolConstants.ATTR_FLAG);
				qtyFlag = page.getHtml().getDocument().select(rulesArray[0]).attr(rulesArray[1]);
			}else {
				qtyFlag = page.getHtml().getDocument().select(rulesStr).text();
			}
		}
		
		int qty = 0;
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(qtyFlag);
		if(matcher.find()) {
//			以数据判断
			int i = Integer.parseInt(matcher.group());
			int j = Integer.parseInt(qtyFlagValue);
			if(i>j) {
				qty = 5;
			}
		}else {
//			以字符判断
			if(qtyFlagValue.contains(SymbolConstants.FALSE_MARK)) {
				if(!qtyFlag.contains(qtyFlagValue)) {
					qty = 5;
				}
			}else {
				if(qtyFlag.contains(qtyFlagValue)) {
					qty = 5;
				}
			}
				
		}
		return String.valueOf(qty);
	}

	/**
	 * SP-C策略，针对driver中获取多张图片
	 * @param driver
	 * @param rulesStr
	 * @return
	 * @DES 如果图片为点击后获取，则图片的策略只能是SP-C
	 */
	private static String handleDriverImg(WebDriver driver, String rulesStr) {
		List<WebElement> imgElements = driver.findElements(By.cssSelector(rulesStr));
		String pics = "";
		for (WebElement imgEle : imgElements) {
			pics += imgEle.getAttribute(SymbolConstants.SRC)+SymbolConstants.SPLIT_FLAG;
		}
		if(pics.contains(SymbolConstants.SPLIT_FLAG)) {
			pics = pics.substring(0, pics.length()-1);
		}
		return pics;
	}
	
}

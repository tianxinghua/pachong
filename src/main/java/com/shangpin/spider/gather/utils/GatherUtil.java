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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		if ("-".equals(referrer)) {
			return "-";
		}
		if (referrer.contains("?")) {// 去掉参数
			referrer = referrer.substring(0, referrer.indexOf("?"));
		}
		// //pos
		int startPos = referrer.indexOf("//");
		if (startPos > 0) {
			startPos = startPos + 2;
			if (startPos > referrer.length()) {
				return "-";
			}

			int endPos = referrer.indexOf("/", startPos);
			if (endPos > -1) {
				return referrer.substring(startPos, endPos);
			} else {
				return referrer.substring(startPos);
			}
		} else {
			if (referrer.startsWith("//")) {
				startPos = startPos + 2;
				int endPos = referrer.indexOf("/", startPos);
				if (endPos == -1) {
					endPos = referrer.length();
				}
				return referrer.substring(startPos, endPos);
			}
		}
		return "-";
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
					if (tag.hasAttr("href")) {
						String absHref = tag.attr("abs:href");
						tag.attr("href", absHref);
					}
					if (tag.hasAttr("src")) {
						String absSrc = tag.attr("abs:src");
						tag.attr("src", absSrc);
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
	 * @return 
	 */
	private static Map<String, Object> handleDEC(String[] gg,int i,Map<String, Object> map) {
		Page page = (Page) map.get("page");
		Document document = page.getHtml().getDocument();
		String specialValue = "";
//		去除/n/t等空格
		String detailRule = gg[i].trim();
		if(i==gg.length-1) {
			if(detailRule.contains("@|")){
				String attrRule = detailRule.substring(detailRule.indexOf("@|")+2, detailRule.length());
				detailRule = detailRule.substring(0,detailRule.indexOf("@|"));
				if(map.containsKey("elements")) {
					Elements elements = (Elements) map.get("elements");
					Elements elements2 = elements.select(detailRule);
					for (Element element : elements2) {
						specialValue += element.attr(attrRule).toString()+"|";
					}
					if(specialValue.endsWith("|")) {
						specialValue = specialValue.substring(0, specialValue.length()-1);
					}
				}else {
					Elements elements = document.select(detailRule);
					for (Element element : elements) {
						specialValue += element.attr(attrRule).toString()+"|";
					}
					if(specialValue.endsWith("|")) {
						specialValue = specialValue.substring(0, specialValue.length()-1);
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
		if(rulesStr.contains("@,")) {
			gg = rulesStr.split("@,");
		}else {
			gg = new String[]{rulesStr};
		}
//		对重复XPATH策略的处理(出发点针对图片路径)
		if(strategyStr.contains("DE-X")) {
			for (String g : gg) {
				try {
					crawlValue += page.getHtml().xpath(g).get()+"|";
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
		if(strategyStr.contains("DE-C")) {
			int i = 0;
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("page", page);
			map = handleDEC(gg,i,map);
			return (String) map.get("specialValue");
		}
//		对填写默认值策略的处理
		if(strategyStr.contains("DE-S")) {
			return rulesStr.trim();
		}
		
//		下标初始值100
//		策略：xpath,css,sub,reg，策略间以|，&间隔。
//		规则间以@,间隔。
		int X_INDEX = 100;int C_INDEX = 100;int SUB_INDEX = 100;int RG_INDEX = 100;
		Map<String,Integer> map = new HashMap<String,Integer>();
		if(strategyStr.contains("X")) {
			X_INDEX = strategyStr.indexOf("X");
			map.put("X", X_INDEX);
		}
		if(strategyStr.contains("C")) {
			C_INDEX = strategyStr.indexOf("C");
			map.put("C", C_INDEX);
		}
		if(strategyStr.contains("SUB")) {
			SUB_INDEX = strategyStr.indexOf("SUB");
			map.put("SUB", SUB_INDEX);
		}
		if(strategyStr.contains("RG")) {
			RG_INDEX = strategyStr.indexOf("RG");
			map.put("RG", RG_INDEX);
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
			if("&".equals(symbol)) {
				System.err.println("且---的规则"+gg[i]);
				crawlValue = subStrategy(deStrategy,detailRule,i,crawlValue);
				crawlValue = regStrategy(deStrategy,detailRule,i,crawlValue);
			}
			if("|".equals(symbol)) {
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
		if("X".equals(deStrategy)) {
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
		if("C".equals(deStrategy)) {
			try {
				if(detailRule.contains("@|")){
					String attrRule = detailRule.substring(detailRule.indexOf("@|")+2, detailRule.length());
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
		if("SUB".equals(deStrategy)) {
			try {
				if(detailRule.contains("{")&&detailRule.contains("}")) {
					detailRule = detailRule.replace("{", "");
					detailRule = detailRule.replace("}", "");
					if(detailRule.contains("@;")) {
						String[] detailRuleSplit = detailRule.split("@;");
						String detailRuleSplitOne = detailRuleSplit[0].trim();
						int oneLength = 0;
						int indexOf = 0;
//						若是截取包括前缀的，则以#T打头
						Boolean flag = true;
						if(detailRuleSplitOne.startsWith("#T")) {
							detailRuleSplitOne = detailRuleSplitOne.replace("#T", "");
						}else {
							flag = false;
							oneLength = detailRuleSplitOne.length();
						}
//						多种匹配按#OR的关系（符号易出错）
						if(detailRuleSplitOne.contains("#OR")) {
							String[] OneStr = detailRuleSplitOne.split("#OR");
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
						if("length".equals(detailRuleSplitTwo)) {
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
			} catch (Exception e) {
				LOG.error("---------SUB解析方法异常{}",e.getMessage());
			}
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
		if("RG".equals(deStrategy)) {
			try {
				if(detailRule.startsWith("!")) {
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
			case "color":
				flag = true;
			case "size":
				flag = true;
			case "detailLink":
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
	public static Boolean filterNeedClick(String resultFieldName) {
		Boolean flag = false;
		String[] needClickField = {"color","size","qty","pics"};
		for (String field : needClickField) {
			if(resultFieldName.equals(field)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
}

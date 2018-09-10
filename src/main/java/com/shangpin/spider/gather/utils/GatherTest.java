package com.shangpin.spider.gather.utils;

public class GatherTest {
	/*public static void main(String[] args) {
//	X代表xpath，C代表css；RP代表replace,RG代表regex
	String ss1 = "X|C&RP|RG";
	
	String ss2 = "X|C&RP";String ss3 = "X|C&RG";
	String ss4 = "X&RP|RG";String sss4 = "X&RP&RG";
	String ss5 = "C&RP|RG";String sss5 = "C&RP&RG";
	
	String s6 = "X&RG";String ss6 = "X&RP";
	String s7 = "C&RG";String ss7 = "C&RP";
	String s8 = "X|C";
	
	String ss10 = "X";
	String ss11 = "C";
//	String sss1 = "X|C|RP|RG";
	
//	String ssS2 = "X|C|RP";
	
//	String sss3 = "X|C|RG";
//	String sss6 = "X|RP";String ssss6 = "X|RG";
//	String sss7 = "C|RP";String ssss7 = "C|RG";
//	String ss8 = "RP|RG";String sss8 = "RP&RG";
	
//	String ss12 = "RP";
//	String ss13 = "RG";
	String[] gg = {"zz","xx","cc","vv"};
//	以ss1为例：
//	第一步，确定有几个策略
	int stragetySize = 0;
	String rep = ss1.replace("|", ",");rep = rep.replace("&", ",");
	String[] split = rep.split(",");
	stragetySize = split.length;
	System.err.println("有几个策略："+stragetySize);
//	第二步，确定策略的顺序
	int X_INDEX = 100;int C_INDEX = 100;int RP_INDEX = 100;int RG_INDEX = 100;
	Map<String,Integer> map = new HashMap<String,Integer>();
	if(ss1.contains("X")) {
		X_INDEX = ss1.indexOf("X");
		map.put("X", X_INDEX);
	}
	if(ss1.contains("C")) {
		C_INDEX = ss1.indexOf("C");
		map.put("C", C_INDEX);
	}
	if(ss1.contains("RP")) {
		RP_INDEX = ss1.indexOf("RP");
		map.put("RP", RP_INDEX);
	}
	if(ss1.contains("RG")) {
		RG_INDEX = ss1.indexOf("RG");
		map.put("RG", RG_INDEX);
	}
	List<Entry<String,Integer>> list = rankMapByValue(map,new Comparator<Integer>() {
		@Override
		public int compare(Integer o1, Integer o2) {
			return Integer.compare(o1, o2);
		}
	});
	System.err.println("--策略的顺序：");
//	第三步，确定策略间的关系
	int i = 0;
	String crawlValue = "";
	Map<String,String> map2 = new HashMap<String,String>();
	map2.put("strategyStr", ss1);
	map2.put("crawlValue", crawlValue);
	for (Map.Entry<String,Integer> entry : list) {
		String deStrategy = entry.getKey();
        System.err.println("Key : " + deStrategy + " , Value : " + entry.getValue());
        map2 = orAnd(new Page(),map2,deStrategy,gg,i);
        i++;
    }
	
	System.out.println("结果----："+map2.get("crawlValue")+"----------"+map2.get("strategyStr"));
	
	
}

*//**
 * 
 * @param page 网页
 * @param crawlValue 获取的值
 * @param strategyStr 抓取的策略字符串
 * @param deStrategy 抓取的具体策略
 * @param gg 抓取的规则
 * @param i 规则的下标
 * @return
 *//*
public static Map<String, String> orAnd(Page page,Map<String,String> map2,String deStrategy,String[] gg,int i) {
	String strategyStr = map2.get("strategyStr");
	String crawlValue = map2.get("crawlValue");
	String strategyFilterStr = strategyStr.replace(deStrategy, "");
	if(i==0) {
		System.err.println("解析page的第一步："+gg[i]);
		crawlValue = "--page--------"+i;
	}else {	
		String symbol = strategyFilterStr.split("")[0];
		strategyFilterStr = strategyFilterStr.substring(1, strategyFilterStr.length());
		if("&".equals(symbol)) {
			System.err.println("且---的规则"+gg[i]);
			crawlValue = crawlValue+"-且-"+i;
		}
		if("|".equals(symbol)) {
			System.err.println("或---的规则"+gg[i]);
			if(StringUtils.isBlank(crawlValue)) {
				crawlValue = crawlValue+"----或----"+i;
			}
		}
	}
	map2.put("strategyStr", strategyFilterStr);
	map2.put("crawlValue", crawlValue);
	return map2;
}

public static <K, V> List<Entry<K, V>> rankMapByValue(Map<K, V> map,
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
}*/
}

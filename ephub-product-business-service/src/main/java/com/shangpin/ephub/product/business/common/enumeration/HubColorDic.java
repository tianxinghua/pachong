package com.shangpin.ephub.product.business.common.enumeration;

import java.util.HashMap;
import java.util.Map;

public class HubColorDic {
	
	private static Map<Long,String> colorMap = new HashMap<Long,String>();
	private static Map<String,Long> colorNameMap = new HashMap<String,Long>();
	static{
		colorMap.put(1L,"多色");
		colorMap.put(2L,"黑色");
		colorMap.put(3L,"红色");
		colorMap.put(5L,"棕色");
		colorMap.put(6L,"蓝色");
		colorMap.put(11L,"粉色");
		colorMap.put(15L,"米色");
		colorMap.put(21L,"绿色");
		colorMap.put(22L,"白色");
		colorMap.put(23L,"灰色");
		colorMap.put(28L,"黄色");
		colorMap.put(42L,"紫色");
		colorMap.put(60L,"橙色");
		colorMap.put(65L,"金属色");
		
		
		
		colorNameMap.put("多色",1L) ;
		colorNameMap.put("黑色",2L );
		colorNameMap.put("红色",3L );
		colorNameMap.put("棕色",5L );
		colorNameMap.put("蓝色",6L );
		colorNameMap.put("粉色",11L);
		colorNameMap.put("米色",15L);
		colorNameMap.put("绿色",21L);
		colorNameMap.put("白色",22L);
		colorNameMap.put("灰色",23L);
		colorNameMap.put("黄色",28L);
		colorNameMap.put("紫色",42L);
		colorNameMap.put("橙色",60L);
		colorNameMap.put("金属色",65L);
	}
	public static String getHubColor(Long index){
		return colorMap.get(index);
	}
	
	public static Long getHubColorId(String index){
		return colorNameMap.get(index);
	}
	public static Map<Long,String> getHubColorMap(){
		return colorMap;
	}
}

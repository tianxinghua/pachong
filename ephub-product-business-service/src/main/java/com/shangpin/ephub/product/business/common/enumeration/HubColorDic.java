package com.shangpin.ephub.product.business.common.enumeration;

import java.util.HashMap;
import java.util.Map;

public class HubColorDic {
	
	private static Map<Long,String> colorMap = new HashMap<Long,String>();
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
	}
	public static String getHubColor(Long index){
		return colorMap.get(index);
	}
	public static Map<Long,String> getHubColorMap(){
		return colorMap;
	}
}

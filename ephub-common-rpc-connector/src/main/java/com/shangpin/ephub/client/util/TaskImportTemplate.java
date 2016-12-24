package com.shangpin.ephub.client.util;

import java.util.HashMap;
import java.util.Map;

public class TaskImportTemplate {
	
	 private static Map<Integer,String> pendingSkuMap = new HashMap<Integer,String>();
	 private static Map<Integer,String> pendingSpuMap = new HashMap<Integer,String>();
	 private static Map<Integer,String> hubSkuMap = new HashMap<Integer,String>();
	 
	 public static Map<Integer,String> getPendingSkuTemplate(){
		 pendingSkuMap.put(0,"");
		 pendingSkuMap.put(0,"");
		 pendingSkuMap.put(0,"");
		 pendingSkuMap.put(0,"");
		 pendingSkuMap.put(0,"");
		 pendingSkuMap.put(0,"");
		 pendingSkuMap.put(0,"");
		 pendingSkuMap.put(0,"");
		 pendingSkuMap.put(0,"");
		 pendingSkuMap.put(0,"");
		 pendingSkuMap.put(0,"");
		 pendingSkuMap.put(0,"");
		 pendingSkuMap.put(0,"");
		 pendingSkuMap.put(0,"");
		 pendingSkuMap.put(0,"");
		 pendingSkuMap.put(0,"");
		 pendingSkuMap.put(0,"");
		 return pendingSkuMap;
	 }
	 
	 public static Map<Integer,String> getPendingSpuTemplate(){
		 
		 pendingSpuMap.put(0,"");
		 pendingSpuMap.put(0,"");
		 pendingSpuMap.put(0,"");
		 pendingSpuMap.put(0,"");
		 pendingSpuMap.put(0,"");
		 pendingSpuMap.put(0,"");
		 pendingSpuMap.put(0,"");
		 pendingSpuMap.put(0,"");
		 pendingSpuMap.put(0,"");
		 pendingSpuMap.put(0,"");
		 pendingSpuMap.put(0,"");
		 pendingSpuMap.put(0,"");
		 pendingSpuMap.put(0,"");
		 pendingSpuMap.put(0,"");
		 pendingSpuMap.put(0,"");
		 
		 return pendingSpuMap;
	 }

}

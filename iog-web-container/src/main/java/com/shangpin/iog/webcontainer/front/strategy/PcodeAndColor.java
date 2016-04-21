package com.shangpin.iog.webcontainer.front.strategy;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.shangpin.iog.dto.ProductDTO;

public class PcodeAndColor implements IStrategy {

	@Override
	public Map<String, List<File>> generateName(List<ProductDTO> pList) {
	 	Map<String,List<File>> nameMap = new HashMap<String,List<File>>();
    	for (ProductDTO p : pList) {
    		nameMap.put(p.getProductCode()+" "+null==p.getColor()?"":p.getColor(), null);
    	}
    	return nameMap;
	}
	
}

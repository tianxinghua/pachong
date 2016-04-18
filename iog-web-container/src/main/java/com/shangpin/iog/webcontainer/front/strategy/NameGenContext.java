package com.shangpin.iog.webcontainer.front.strategy;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.shangpin.iog.dto.ProductDTO;
/**
 *	命名策略条件选择 
 * @author Administrator
 */
public class NameGenContext {
	private static ResourceBundle bdl = null;
	private static String pcode = null;
	private static String pcodecolor = null;
	private static String skuwithoutsize = null;
	private static String efashion = null;
	private static String pavin = null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		pcode = bdl.getString("pcode");
		pcodecolor = bdl.getString("pcodecolor");
		skuwithoutsize = bdl.getString("skuwithoutsize");
		efashion = bdl.getString("efashion");
		pavin = bdl.getString("pavin");
	}
	private IStrategy istrategy;
	public NameGenContext(String supplier) {
		super();
		this.istrategy = checkContation(supplier);
	}
	
	public Map<String,List<File>> operate(List<ProductDTO> pList){
		return this.istrategy.generateName(pList);
	}
	
	
	private IStrategy checkContation(String supplier){
		
	 	if (pcode.contains(supplier)) {
	 		return new PcodeAsName();
    	}else if(pcodecolor.contains(supplier)){
    		return new PcodeAndColor();
    	}else if(skuwithoutsize.contains(supplier)){
    		
    		return null;
    	}else if(efashion.contains(supplier)){
    		
    		return null;
    	}else if(pavin.contains(supplier)){
    		
    		return null;
    	}else{
    		//配置中没有默认使用货号加颜色
    		return new PcodeAndColor();
    	}
	}
	
}

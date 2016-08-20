package com.shangpin.iog.generater.util.json;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonToken;
import com.shangpin.iog.generater.dto.ProductDTO;

public class JsonHandler {
	private List<ProductDTO> spuList;
	private List<ProductDTO> skuList;
	private ProductDTO spu;
	private ProductDTO sku;
	private String preTag;//要对照的属性名
	private String flag;//创建sku 或 spu 的标识
	private String spuStartElementTag;
	private String skuStartElementTag;
	private Map<String, String> tagNameMap;
	
	
	
	public JsonHandler(String spuStartElementTag, String skuStartElementTag,
			Map<String, String> tagNameMap) {
		super();
		this.spuStartElementTag = spuStartElementTag;
		this.skuStartElementTag = skuStartElementTag;
		this.tagNameMap = tagNameMap;
	}
	public List<ProductDTO> getSpuList() {
		return spuList;
	}
	
	
	public void startToken(JsonToken token,String currentName){
		if(token==JsonToken.START_OBJECT){
			
		}
		
		
	}
}

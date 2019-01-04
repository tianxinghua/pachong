package com.shangpin.iog.generater.util.xml;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.shangpin.iog.generater.dto.ProductDTO;

public class XMLConvertHandler extends DefaultHandler {
	private List<ProductDTO> spuList;
	private List<ProductDTO> skuList;
	private ProductDTO spu;
	private ProductDTO sku;
	private String preTag;
	private String spuStartElementTag;
	private String skuStartElementTag;
	private Map<String, String> tagNameMap;
	private Map<String,List<String>> theSame;

	public XMLConvertHandler(String spuStartElementTag,
			String skuStartElementTag, Map<String, String> tagNameMap,
			Map<String,List<String>> theSame) {
		super();
		this.spuStartElementTag = spuStartElementTag;
		this.skuStartElementTag = skuStartElementTag;
		this.tagNameMap = tagNameMap;
		this.theSame = theSame;
	}
	
	public List<ProductDTO> getSpuList() {
		return spuList;
	}



	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String data = new String(ch, start, length);
		if (sku !=null && preTag!=null) {
			setDTOValue(data, preTag, sku,theSame);
			return;
		}
		if (spu != null && preTag!=null) {
			setDTOValue(data, preTag, spu,theSame);
		}

	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		
		if (skuStartElementTag.equals(name)&&sku!=null) {
			skuList.add(sku);
			sku = null;
		}
		if (spuStartElementTag.equals(name)&&spu!=null) {
			spu.setProductList(skuList);
			spuList.add(spu);
			spu = null;
			skuList = new ArrayList<ProductDTO>();
		}
		preTag = null;
	}

	@Override
	public void startDocument() throws SAXException {
		skuList = new ArrayList<ProductDTO>();
		spuList = new ArrayList<ProductDTO>();
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		if (spuStartElementTag.equals(name)) {
			spu = new ProductDTO();
		}
		if (skuStartElementTag.equals(name)) {
			sku = new ProductDTO();
		}
		preTag = tagNameMap.get(name);
	}
	
	//赋值productDTO
	private void setDTOValue(String data, String preTag, ProductDTO productDTO,Map<String,List<String>> theSame){
		try {
//			Field field = productDTO.getClass().getDeclaredField(preTag);
//			field.setAccessible(true);
//			field.set(productDTO, data);
			for(Entry<String,List<String>> entry :theSame.entrySet()){
				if(preTag.equals(entry.getKey())){
					for(String name : entry.getValue()){
						Field fieldSame = productDTO.getClass().getDeclaredField(name);
						fieldSame.setAccessible(true);
						fieldSame.set(productDTO, data);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

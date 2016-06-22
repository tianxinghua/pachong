package com.shangpin.iog.generater.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.shangpin.iog.generater.dto.ProductDTO;
import com.shangpin.iog.generater.util.xml.XMLConvertHandler;


public class Xml2DTO {
	/**
	 * 
	 * @param xmlFile
	 * @param skuStartTag sku开始标签,若为"",则返回为sku的list,否则对象中的productList保存了sku对象
	 * @param spuStartTag spu开始标签
	 * @param tagNameMap 标签与productDTO字段对照   如 标签名skucode,skucode=skuId
	 * @return
	 */
	public static List<ProductDTO> toDTO(File xmlFile,String skuStartTag,String spuStartTag,Map<String,String> tagNameMap){
		List<ProductDTO> spuList = null;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			XMLConvertHandler handler = new XMLConvertHandler(spuStartTag,skuStartTag, tagNameMap);
			saxParser.parse(xmlFile, handler);
			spuList = handler.getSpuList();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return spuList;
	}
	
}

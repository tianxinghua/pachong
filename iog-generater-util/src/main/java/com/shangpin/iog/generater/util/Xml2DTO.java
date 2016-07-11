package com.shangpin.iog.generater.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXException;

import com.shangpin.iog.generater.dto.ProductDTO;
import com.shangpin.iog.generater.util.publicJ.DowLoad;
import com.shangpin.iog.generater.util.xml.XMLConvertHandler;


public class Xml2DTO {
	/**
	 * 
	 * @param url
	 * @param xmlFile
	 * @param skuStartTag sku开始标签,若为"",则返回为sku的list,否则对象中的productList保存了sku对象
	 * @param spuStartTag spu开始标签
	 * @param tagNameMap 标签与productDTO字段对照   如 标签名skucode,skucode=skuId
	 * @param theSame productDTO的多个属性使用同一个标签 如啊{spuId : [spuId,productCode]}
	 * @return
	 */
	public static List<ProductDTO> toDTO(String url,String xmlFile,String skuStartTag,String spuStartTag,Map<String,String> tagNameMap,Map<String,List<String>> theSame){
		List<ProductDTO> spuList = null;
		try {
			if(StringUtils.isNotBlank(url) && StringUtils.isNotBlank(xmlFile)){
				DowLoad.txtDownload(url,xmlFile);
			}
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			XMLConvertHandler handler = new XMLConvertHandler(spuStartTag,skuStartTag, tagNameMap,theSame);
			saxParser.parse(new File(xmlFile), handler);
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

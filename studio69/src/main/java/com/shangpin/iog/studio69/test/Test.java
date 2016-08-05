package com.shangpin.iog.studio69.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.studio69.dto.GoodDetail;
import com.shangpin.iog.studio69.dto.GoodsCategory;
import com.shangpin.iog.studio69.dto.GoodsDetail;
import com.shangpin.iog.studio69.util.DataTransUtil;

public class Test {
	public static void main(String[] args) {
//		Map<String, GoodDetail> goodsDetailList = DataTransUtil.getGoodsDetailList();
//		GoodDetail goodDetail = goodsDetailList.get("6");
//		System.out.println(StringUtils.isBlank(goodDetail.getMadeIn()));
		Map<String, String> param = new HashMap<String, String>();
		param.put("GoodsID", "29223");
		String priceData = HttpUtil45.postAuth("http://studio69.atelier98.net/api_studio69/api_studio69.asmx/GetGoodsDetailByGoodsID",
				param ,new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10),"SHANGPIN","2MWWKgNSxgf");
		
		try {
//			GoodDetail goodDetail = ObjectXMLUtil.xml2Obj(GoodDetail.class, priceData);
			GoodsDetail categorys = ObjectXMLUtil.xml2Obj(GoodsDetail.class, priceData);
			System.out.println(categorys.getGoodDetials().get(0).getStock().getItemlist().size());
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}
		System.out.println(priceData);
		File file = new File("F://studio/GetGoodsDetailByGoodsID.xml");
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				file.createNewFile();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fwriter = null;
		try {
			fwriter = new FileWriter("F://studio/GetGoodsDetailByGoodsID.xml");
			fwriter.write(priceData);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				fwriter.flush();
				fwriter.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}

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
import com.shangpin.iog.studio69.util.DataTransUtil;

public class Test {
	public static void main(String[] args) {
		Map<String, GoodDetail> goodsDetailList = DataTransUtil.getGoodsDetailList();
		GoodDetail goodDetail = goodsDetailList.get("6");
		System.out.println(StringUtils.isBlank(goodDetail.getMadeIn()));
		Map<String, String> param = new HashMap<String, String>();
//		param.put("GoodsID", "6");
		String priceData = HttpUtil45.postAuth("http://studio69.atelier98.net/api_studio69/api_studio69.asmx/GetGoodsCatg",
				param ,new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10),"studio69","fw6pMqvDnoRAQ2");
		
		try {
			GoodsCategory categorys = ObjectXMLUtil.xml2Obj(GoodsCategory.class, priceData);
			System.out.println(categorys.getCategory().size());
		} catch (JAXBException e1) {
			e1.printStackTrace();
		}
		System.out.println(priceData);
		File file = new File("D://studio/GetGoodsDetailByGoodsID.xml");
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
			fwriter = new FileWriter("D://studio/GetGoodsDetailByGoodsID.xml");
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

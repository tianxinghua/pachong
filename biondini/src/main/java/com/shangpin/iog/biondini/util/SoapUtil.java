package com.shangpin.iog.biondini.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.shangpin.iog.biondini.dao.ArticleQty;
import com.shangpin.iog.biondini.dao.IdTable;
import com.shangpin.iog.biondini.dao.Modele;
import com.shangpin.iog.biondini.dao.Qty;
import com.shangpin.iog.biondini.dao.SOAP;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;

public class SoapUtil {
	private static Logger logger = Logger.getLogger("info");
	private static List<IdTable> listTabArtIdTable = null;
	private static List<IdTable> listTabModeIdTable = null;
	public static void getTableModeleAndArticle() {
		SOAP ps = null;
		try {
			ps = ObjectXMLUtil.xml2Obj(SOAP.class,
					HttpUtils.getLectureDesTablesBySoap());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ps != null) {
			listTabModeIdTable = ps.getBody().getReturns().getResult()
					.getTableModele().getIdTable();
			listTabArtIdTable = ps.getBody().getReturns().getResult()
					.getTable_Article().getIdTable();
		}
	}

	public static List<IdTable> getTableModele() {

		return listTabModeIdTable;
	}

	public static List<IdTable> getTableArtcile() {

		return listTabArtIdTable;
	}

	public static List<Modele> getProductList() {
		
		SOAP ps = null;
		List<Modele> array = null;
		try {
			String xml = HttpUtils.getProductsBySoap();
			if (xml.equals(HttpUtil45.errorResult)) {
				System.out.println("===========链接超时==============");
				logger.info("===========链接超时==============");
			} else {
				ps = ObjectXMLUtil.xml2Obj(SOAP.class, xml);
			}
			array = ps.getBody().getReturns().getResult().getModele();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return array;
	}

	public static Map<String, String> getProductStockList() {

		String xml = HttpUtils.getProductStocksBySoap();
		Map<String, String> map = new HashMap<String, String>();
		SOAP ps = null;
		try {
			ps = ObjectXMLUtil.xml2Obj(SOAP.class, xml);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<ArticleQty> array = ps.getBody().getReturns().getResult()
				.getListArticleQty();
		for (ArticleQty aty : array) {
			List<Qty> listQty = aty.getListQtTaille();
			for (Qty qtyObj : listQty) {
				String stock = qtyObj.getQuantite();
				String size = qtyObj.getTaille();
				if (size != null) {
					if (size.indexOf("½") > 0) {
						size = size.replace("½", "+");
					}
				} else {
					size = "A";
				}
				map.put(aty.getNumArticle() + "|" + size, stock);
			}
		}
		return map;
	}
}

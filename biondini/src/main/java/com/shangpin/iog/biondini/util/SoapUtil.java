package com.shangpin.iog.biondini.util;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.shangpin.iog.biondini.dao.Article;
import com.shangpin.iog.biondini.dao.ArticleQty;
import com.shangpin.iog.biondini.dao.IdTable;
import com.shangpin.iog.biondini.dao.Modele;
import com.shangpin.iog.biondini.dao.QtTaille;
import com.shangpin.iog.biondini.dao.Qty;
import com.shangpin.iog.biondini.dao.SOAP;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;

public class SoapUtil {

	private static List<IdTable> listTabArtIdTable = null;
	private static List<IdTable> listTabModeIdTable = null;
	private static ResourceBundle bdl = null;
	private static String path;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		path = bdl.getString("path");
	}
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
				ps = ObjectXMLUtil.xml2Obj(SOAP.class, getJson(path));
			} else {
				ps = ObjectXMLUtil.xml2Obj(SOAP.class, xml);
			}
			array = ps.getBody().getReturns().getResult().getModele();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return array;
	}
	private static String getJson(String path) {

		String fullFileName = path;

		File file = new File(fullFileName);
		Scanner scanner = null;
		StringBuilder buffer = new StringBuilder();
		try {
			scanner = new Scanner(file, "utf-8");
			while (scanner.hasNextLine()) {
				buffer.append(scanner.nextLine());
			}
		} catch (Exception e) {

		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		System.out.println(buffer.toString());
		return buffer.toString();
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
	
	public static List getProductSizeAndPriceBySoap(String spuId){
		
		List<QtTaille> ta = null;
		String xml1 = HttpUtils.getProductSizeAndPriceBySoap(spuId);
		SOAP ps = null;
		try {
			ps = ObjectXMLUtil.xml2Obj(SOAP.class, xml1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<ArticleQty> array = ps.getBody().getReturns().getResult().getListArticleQty();
		for(ArticleQty art : array){
			if(spuId.equals(art.getCodeArticle())){
				ta = art.getTarifMagInternet().getList();
				break;
			}
		}
		return ta;
	}
}

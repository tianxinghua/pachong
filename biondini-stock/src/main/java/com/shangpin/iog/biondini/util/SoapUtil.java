package com.shangpin.iog.biondini.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
	
	public static Map<String,String> getProductStockList(){
		
		String xml = HttpUtils.getProductStocksBySoap();
		System.out.println(xml);
		Map<String,String> map = new HashMap<String,String>();
		SOAP ps = null;
		try {
			ps = ObjectXMLUtil.xml2Obj(SOAP.class, xml);
			List<ArticleQty> array = ps.getBody().getReturns().getResult().getListArticleQty();
			for(ArticleQty aty:array){
				List<Qty> listQty = aty.getListQtTaille();
				for(Qty qtyObj : listQty){
					String stock = qtyObj.getQuantite();
					String size = qtyObj.getTaille();
					if(size!=null){
						if(size.indexOf("½")>0){
							size = size.replace("½","+");	
						}
					}else{
						size = "A";
					}
					map.put(aty.getNumArticle()+"|"+size,stock);
				}
			}
		} catch (Exception e) {
			System.out.println("======发生异常错误"+e.getMessage()+"==========");
			logger.info("======发生异常错误"+e.getMessage()+"==========");;
		}
		return map;
	}
}

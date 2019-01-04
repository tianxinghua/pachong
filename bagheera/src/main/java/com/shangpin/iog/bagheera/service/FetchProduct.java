package com.shangpin.iog.bagheera.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.bagheera.dto.BagheeraDTO;
import com.shangpin.iog.bagheera.utils.DownloadAndReadExcel;
import com.shangpin.product.AbsSaveProduct;

/**
 * Created by sunny on 2015/9/8.
 */
@Component("bagheera")
public class FetchProduct extends AbsSaveProduct {
	private static Logger loggerError = Logger.getLogger("error");
	private static Gson gson = new Gson();
	
    public Map<String, Object> fetchProductAndSave(){
        try {
            List<BagheeraDTO> list=DownloadAndReadExcel.readLocalExcel();
            for (BagheeraDTO dto:list){
            	supp.setData(gson.toJson(dto));  
            	pushMessage(null);            	
            }
        } catch (IOException e) {
            e.printStackTrace();
            loggerError.error(e.getMessage(),e); 
        }
        return null;
    }
    
//    public static void main(String[] args) {
//		String str = "{\"LAST_UPDATE\":\"28-Dec-16\",\"SUPPLIER_CODE\":\"DR18WR17300/999\",\"CODE\":\"16572\",\"DEPT\":\"Women Abbigliamento\",\"CATEGORY\":\"Clothes\",\"ITEM_GROUP\":\"J.W. ANDERSON\",\"MATERIAL\":\"Composition 75% viscose 25% silk   Composition II 70% triacetate 30% polyester   Lining 71% acetate 29% silk   Concelaed hook and zip fastening at back   Mock collar with back tie closure  Long sleeves with split flare cuffs  Flattering asymmetric hem   Lined   Dry clean only   Made in Portugal\",\"SIZE_AND_FIT\":\"True to size, take your normal size   Fitted at the hip with a loosely cut top\",\"MADE\":\"ITALY\",\"DESCRIPTION\":\"Colour: black  Designer colour: black\",\"COLOR\":\"Black  \",\"SIZE\":\"UK 8\",\"STOCK\":\"0\",\"COLLECTION\":\"spring summer 2017\",\"LIST_PRICE\":\"852\",\"RETAIL_PRICE\":\"1039\",\"DISCOUNTED\":\"852\",\"DISCOUNT\":\"15\",\"LASO_Price\":\"724\",\"CURRENCY\":\"EURO\",\"IMAGE_URL1\":\"https://bagheerastorage.blob.core.windows.net/product/16572/original/3396472c-25e4-4b27-bbcc-33734fd94cc6.jpg\",\"IMAGE_URL2\":\"https://bagheerastorage.blob.core.windows.net/product/16572/original/8f5832b5-40f7-4d09-adcb-0e8cc8de220c.jpg\",\"IMAGE_URL3\":\"https://bagheerastorage.blob.core.windows.net/product/16572/original/60531b3d-5c44-47c0-8486-1e4d23274cbf.jpg\",\"IMAGE_URL4\":\"https://bagheerastorage.blob.core.windows.net/product/16572/original/a03eaaf6-d678-4b60-b503-1ddf98821a01.jpg\"}";
//		supp.setData(str);  
//    	new FetchProduct().pushMessage(null);
//    	System.out.println("gooddddd"); 
//	}
}

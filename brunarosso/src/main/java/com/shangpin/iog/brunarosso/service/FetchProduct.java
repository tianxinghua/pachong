package com.shangpin.iog.brunarosso.service;

import com.shangpin.framework.ServiceException;

import com.shangpin.iog.brunarosso.utils.XmlReader;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;

import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by sunny on 2015/7/10.
 */
@Component("brunarosso")
public class FetchProduct {
	 private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");

    @Autowired
    ProductFetchService productFetchService;
    //String supplierId = "2015091801507";//2015071701342  new20150727
    
    public static final String PROPERTIES_FILE_NAME = "param";
    static ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_FILE_NAME) ;
    
    static String supplierId = bundle.getString("supplierId");
    /**
     *
     * @param map 尺寸集合,key是sku，value是尺寸list
     * @param url
     */
    public void fetchProductAndSave(Map<String,List<String>>map,Map<String,String> spuMap,String url) {
        //String result = HttpUtils.get(url, false, 360000);
        try {
            /*Products products = ObjectXMLUtil.xml2Obj(Products.class, result);
            List<Product> productList = products.getProducts();*/
            List<org.jdom2.Element>list  = XmlReader.getProductElement(url);
                for (org.jdom2.Element element:list){
                    SpuDTO spu = new SpuDTO();
                    //SkuDTO sku = new SkuDTO();
                    spu.setId(UUIDGenerator.getUUID());
                    spu.setBrandName(element.getChildText("BRAND"));
                    spu.setMaterial(element.getChildText("COMPOSIZIONE_DETTAGLIATA"));
                    spu.setCategoryGender(element.getChildText("SETTORE"));
                    spu.setSpuId(element.getChildText("CODICE_MODELLO"));
                    spu.setSeasonId(element.getChildText("SIGLA_STAGIONE"));
                    spu.setSeasonName(element.getChildText("TIPO_STAGIONE"));
                    spu.setCategoryName(element.getChildText("GRUPPO_SUPER"));
                    spu.setSubCategoryId(element.getChildText("GRUPPO"));
                    spu.setProductOrigin(element.getChildText("PAESE_PRODUZIONE"));
                    spu.setSupplierId(supplierId);//2015071701342
                    try{
                        productFetchService.saveSPU(spu);
                    }catch (ServiceException e) {
                        e.printStackTrace();
                    }
                    if(map.containsKey(element.getChildText("ID_ARTICOLO"))){
                        //List<SkuDTO>skuDTOList=new ArrayList<>();
                        String key ="";
                        String value="";
                        String skuId="";
                        String barcode="";
                        String stock="";
                        for (int j = 0;j<map.get(element.getChildText("ID_ARTICOLO")).size();j++){
                            SkuDTO sku = new SkuDTO();
                            sku.setId(UUIDGenerator.getUUID());
                            key=element.getChildText("ID_ARTICOLO");
                            value = map.get(key).get(j);
                            String[]str= value.split(",");
                            value=str[0];
                            barcode=str[1];
                            stock=str[2];
                            if(value.indexOf("½")>0){
                                value=value.replace("½","+");
                            }
                            skuId=key+"-"+value;
                            sku.setSkuId(skuId);
                            sku.setColor(element.getChildText("COLORE"));
                            sku.setSupplierPrice(element.getChildText("PREZZO_VENDITA_SENZA_IVA"));
                            //sku.setSkuId(element.getChildText("ID_ARTICOLO"));
                            sku.setSpuId(element.getChildText("CODICE_MODELLO"));
                            sku.setProductSize(value);
                            sku.setProductCode(element.getChildText("CODICE_MODELLO")+" "+element.getChildText("CODICE_VARIANTE"));
                            sku.setProductName(element.getChildText("BRAND")+" "+element.getChildText("COLORE")+" "+element.getChildText("GRUPPO"));
                            sku.setSalePrice(element.getChildText("PREZZO_VENDITA"));
                            sku.setMarketPrice(element.getChildText("PREZZO_VENDITA"));
                            sku.setProductDescription(element.getChildText("DESCRIZIONE"));
                            sku.setBarcode(barcode);
                            // StockClientImp.getStock(key,value);
                            sku.setStock(stock);
                            sku.setSupplierId(supplierId);
                            try {
                                productFetchService.saveSKU(sku);
                                spuMap.put(key,"");
                                logger.info("key"+key);
                            }catch (Exception e) {
                                try {
                                    if(e.getMessage().equals("数据插入失败键重复")){
                                        //更新价格和库存
                                        productFetchService.updatePriceAndStock(sku);
                                    } else{
                                        e.printStackTrace();
                                    }

                                } catch (ServiceException e1) {
                                    e1.printStackTrace();
                                }
                            }

                        }
                    }
//                    else{
//                        SkuDTO sku = new SkuDTO();
//                        sku.setId(UUIDGenerator.getUUID());
//                        sku.setColor(element.getChildText("COLORE"));
//                        sku.setSupplierPrice(element.getChildText("PREZZO_VENDITA_SENZA_IVA"));
//                        sku.setSkuId(element.getChildText("ID_ARTICOLO"));
//                        sku.setSpuId(element.getChildText("CODICE_MODELLO"));
//                        sku.setProductCode(element.getChildText("CODICE_MODELLO") + " " + element.getChildText("CODICE_VARIANTE"));
//                        sku.setProductName(element.getChildText("DESCRIZIONE_MODELLO"));
//                        sku.setSalePrice(element.getChildText("PREZZO_VENDITA"));
//                        sku.setProductDescription(element.getChildText("DESCRIZIONE_SPECIALE"));
//                        //StockClientImp.getStock(element.getChildText("ID_ARTICOLO"), "");
//                        sku.setStock("0");
//                        sku.setSupplierId(supplierId);
//                        try {
//                            productFetchService.saveSKU(sku);
//                            returnMap.put(element.getChildText("ID_ARTICOLO"),"");
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//
//                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       
    }
    public void savePic(String url,Map<String,String>returnMap){
        List<org.jdom2.Element>picList = XmlReader.getPictureElement(url);
        for (org.jdom2.Element element:picList){
        	
        	logger.info( " RF_RECORD_ID = " + element.getChildText("RF_RECORD_ID"));
            if(returnMap.containsKey(element.getChildText("RF_RECORD_ID"))){
            	logger.info( "contain key  RF_RECORD_ID = " + element.getChildText("RF_RECORD_ID"));
                ProductPictureDTO dto  = new ProductPictureDTO();
                dto.setPicUrl(element.getChildText("RIFERIMENTO"));
                dto.setId(UUIDGenerator.getUUID());
                dto.setSupplierId(supplierId);
                dto.setSpuId(element.getChildText("RF_RECORD_ID"));
                //dto.setSpuId("");
                try {
                    productFetchService.savePictureForMongo(dto);
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String args[]) {
        String url="http://85.159.181.250/ws_sito/ws_sito.asmx/DisponibilitaVarianteTaglia";
        Map<String,String> param = new HashMap<>();
        param.put("ID_ARTICOLO","6759079");
        param.put("TAGLIA","");
        OutTimeConfig outTimeConf = new OutTimeConfig();
        String result = HttpUtil45.post(url,param,outTimeConf);
        System.out.println(result);
    }
}

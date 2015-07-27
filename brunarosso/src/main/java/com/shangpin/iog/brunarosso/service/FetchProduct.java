package com.shangpin.iog.brunarosso.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.brunarosso.dto.Product;
import com.shangpin.iog.brunarosso.dto.Products;
import com.shangpin.iog.brunarosso.stock.StockClientImp;
import com.shangpin.iog.brunarosso.utils.XmlReader;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sunny on 2015/7/10.
 */
@Component("brunarosso")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    ProductFetchService productFetchService;

    /**
     *
     * @param map 尺寸集合,key是sku，value是尺寸list
     * @param url
     */
    public void fetchProductAndSave(Map<String,List<String>>map,String url) {
        String supplierId = "";
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
                    spu.setSupplierId("2015071701342");//2015071701342
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
                        for (int j = 0;j<map.get(element.getChildText("ID_ARTICOLO")).size();j++){
                            SkuDTO sku = new SkuDTO();
                            sku.setId(UUIDGenerator.getUUID());
                            key=element.getChildText("ID_ARTICOLO");
                            value = map.get(key).get(j);
                            skuId=key+"—"+value;
                            sku.setSkuId(skuId);
                            sku.setColor(element.getChildText("COLORE"));
                            sku.setSupplierPrice(element.getChildText("PREZZO_VENDITA_SENZA_IVA"));
                            //sku.setSkuId(element.getChildText("ID_ARTICOLO"));
                            sku.setSpuId(element.getChildText("CODICE_MODELLO"));
                            sku.setProductSize(value);
                            sku.setProductCode(element.getChildText("CODICE_MODELLO")+" "+element.getChildText("CODICE_VARIANTE"));
                            sku.setProductName(element.getChildText("DESCRIZIONE_MODELLO"));
                            String stock = StockClientImp.getStock(key,value);
                            sku.setStock(stock);
                            sku.setSupplierId("2015071701342");
                            try {
                                productFetchService.saveSKU(sku);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }else{
                        SkuDTO sku = new SkuDTO();
                        sku.setId(UUIDGenerator.getUUID());
                        sku.setColor(element.getChildText("COLORE"));
                        sku.setSupplierPrice(element.getChildText("PREZZO_VENDITA_SENZA_IVA"));
                        sku.setSkuId(element.getChildText("ID_ARTICOLO"));
                        sku.setSpuId(element.getChildText("CODICE_MODELLO"));
                        sku.setProductCode(element.getChildText("CODICE_MODELLO")+" "+element.getChildText("CODICE_VARIANTE"));
                        sku.setProductName(element.getChildText("DESCRIZIONE_MODELLO"));
                        String stock = StockClientImp.getStock(element.getChildText("ID_ARTICOLO"),"");
                        sku.setStock(stock);
                        sku.setSupplierId("2015071701342");
                        try {
                            productFetchService.saveSKU(sku);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void savePic(String url){
        List<org.jdom2.Element>picList = XmlReader.getPictureElement(url);
        for (org.jdom2.Element element:picList){
            ProductPictureDTO dto  = new ProductPictureDTO();
            dto.setPicUrl(element.getChildText("RIFERIMENTO"));
            dto.setId(UUIDGenerator.getUUID());
            dto.setSupplierId("2015071701342");
            dto.setSkuId(element.getChildText("RF_RECORD_ID"));
            dto.setSpuId("");
            try {
                productFetchService.savePictureForMongo(dto);
            } catch (ServiceException e) {
                e.printStackTrace();
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

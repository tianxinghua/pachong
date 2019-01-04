package com.shangpin.iog.acanfora.dto;

import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;

import javax.xml.bind.JAXBException;
import java.util.List;

/**
 * Created by loyalty on 15/6/5.
 */
public class Test {
    public static void main(String[] args){
        String xml="<products number=\"2379\">" +
                "<product>\n" +
                "<productId><![CDATA[49767]]></productId>\n" +
                "<product_name><![CDATA[Pt01 Pants CPKTEG EB19 0460]]></product_name>\n" +
                "<season_code><![CDATA[AI]]></season_code>\n" +
                "<description><![CDATA[Pantalone Tinto Skinny Edgar]]></description>\n" +
                "<category><![CDATA[Clothing]]></category>\n" +
                "<product_brand><![CDATA[Pt01]]></product_brand>\n" +
                "<product_detail><![CDATA[• Pantalone Chiusura Con Un Bottone A Vista E Bottone E Zip A Scomparsa\n" +
                "• Due Tasche Sul Fronte\n" +
                "• Due Tasche Sul Retro\n" +
                "• Un Taschino Sul Retro\n" +
                "• Il Modello Indossa La Taglia 50]]></product_detail>\n" +
                "<product_material><![CDATA[98%co 2%ea]]></product_material><gender><![CDATA[Man]]></gender>\n" +
                "<url><![CDATA[http://www.acanfora.it/det_art.aspx?cod=49767]]></url>\n" +
                "<supply_price><![CDATA[88.93]]></supply_price>\n" +
                "<producer_id><![CDATA[CPKTEG EB19 0460]]></producer_id>\n" +
                "<items>\n" +
                "<item>\n" +
                "<item_id><![CDATA[49767-50]]></item_id>\n" +
                "<item_size><![CDATA[50]]></item_size>\n" +
                "<market_price><![CDATA[155]]></market_price>\n" +
                "<sell_price><![CDATA[77.5]]></sell_price>\n" +
                "<supply_price><![CDATA[88.93]]></supply_price>\n" +
                "<color><![CDATA[]]></color>\n" +
                "<description><![CDATA[Pantalone Tinto Skinny Edgar]]></description>\n" +
                "<item_detail><![CDATA[]]></item_detail>\n" +
                "<stock><![CDATA[1]]></stock>\n" +
                "<picture><![CDATA[http://www.acanfora.it/images/articoli/orig/Image_49767.jpg|http://www.acanfora.it/images/articoli/orig/Image_49767_2.jpg|http://www.acanfora.it/images/articoli/orig/Image_49767_3.jpg|http://www.acanfora.it/images/articoli/orig/Image_49767_4.jpg|http://www.acanfora.it/images/articoli/orig/Image_49767_5.jpg|http://www.acanfora.it/images/articoli/orig/Image_49767_6.jpg|http://www.acanfora.it/images/articoli/orig/Image_49767_7.jpg|http://www.acanfora.it/images/articoli/orig/Image_49767_8.jpg]]></picture>\n" +
                "</item>\n" +
                "</items>\n" +
                "</product>\n" +
                "<product>\n" +
                "<productId><![CDATA[55468]]></productId>\n" +
                "<product_name><![CDATA[Pt01 Pants KL01 TU12 0230]]></product_name>\n" +
                "<season_code><![CDATA[PE]]></season_code>\n" +
                "<description><![CDATA[Pantaloni Fantasia Grigio Quadri]]></description>\n" +
                "<category><![CDATA[Clothing]]></category>\n" +
                "<product_brand><![CDATA[Pt01]]></product_brand>\n" +
                "<product_detail><![CDATA[]]></product_detail>\n" +
                "<product_material><![CDATA[]]></product_material><gender><![CDATA[Man]]></gender>\n" +
                "<url><![CDATA[http://www.acanfora.it/det_art.aspx?cod=55468]]></url>\n" +
                "<supply_price><![CDATA[111.89]]></supply_price>\n" +
                "<producer_id><![CDATA[KL01 TU12 0230]]></producer_id>\n" +
                "<items>\n" +
                "<item>\n" +
                "<item_id><![CDATA[55468-48]]></item_id>\n" +
                "<item_size><![CDATA[48]]></item_size>\n" +
                "<market_price><![CDATA[195]]></market_price>\n" +
                "<sell_price><![CDATA[195]]></sell_price>\n" +
                "<supply_price><![CDATA[111.89]]></supply_price>\n" +
                "<color><![CDATA[Grey]]></color>\n" +
                "<description><![CDATA[Pantaloni Fantasia Grigio Quadri]]></description>\n" +
                "<item_detail><![CDATA[]]></item_detail>\n" +
                "<stock><![CDATA[1]]></stock>\n" +
                "<picture><![CDATA[http://www.acanfora.it/images/articoli/orig/Image_55468.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_2.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_3.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_4.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_5.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_6.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_7.jpg]]></picture>\n" +
                "</item>\n" +
                "<item>\n" +
                "<item_id><![CDATA[55468-52]]></item_id>\n" +
                "<item_size><![CDATA[52]]></item_size>\n" +
                "<market_price><![CDATA[195]]></market_price>\n" +
                "<sell_price><![CDATA[195]]></sell_price>\n" +
                "<supply_price><![CDATA[111.89]]></supply_price>\n" +
                "<color><![CDATA[Grey]]></color>\n" +
                "<description><![CDATA[Pantaloni Fantasia Grigio Quadri]]></description>\n" +
                "<item_detail><![CDATA[]]></item_detail>\n" +
                "<stock><![CDATA[1]]></stock>\n" +
                "<picture><![CDATA[http://www.acanfora.it/images/articoli/orig/Image_55468.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_2.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_3.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_4.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_5.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_6.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_7.jpg]]></picture>\n" +
                "</item>\n" +
                "<item>\n" +
                "<item_id><![CDATA[55468-54]]></item_id>\n" +
                "<item_size><![CDATA[54]]></item_size>\n" +
                "<market_price><![CDATA[195]]></market_price>\n" +
                "<sell_price><![CDATA[195]]></sell_price>\n" +
                "<supply_price><![CDATA[111.89]]></supply_price>\n" +
                "<color><![CDATA[Grey]]></color>\n" +
                "<description><![CDATA[Pantaloni Fantasia Grigio Quadri]]></description>\n" +
                "<item_detail><![CDATA[]]></item_detail>\n" +
                "<stock><![CDATA[1]]></stock>\n" +
                "<picture><![CDATA[http://www.acanfora.it/images/articoli/orig/Image_55468.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_2.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_3.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_4.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_5.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_6.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_7.jpg]]></picture>\n" +
                "</item>\n" +
                "</items>\n" +
                "</product>\n" +

                "</products>";

        String productXml =   "<product>\n" +
                "<productId><![CDATA[55468]]></productId>\n" +
                "<product_name><![CDATA[Pt01 Pants KL01 TU12 0230]]></product_name>\n" +
                "<season_code><![CDATA[PE]]></season_code>\n" +
                "<description><![CDATA[Pantaloni Fantasia Grigio Quadri]]></description>\n" +
                "<category><![CDATA[Clothing]]></category>\n" +
                "<product_brand><![CDATA[Pt01]]></product_brand>\n" +
                "<product_detail><![CDATA[]]></product_detail>\n" +
                "<product_material><![CDATA[]]></product_material><gender><![CDATA[Man]]></gender>\n" +
                "<url><![CDATA[http://www.acanfora.it/det_art.aspx?cod=55468]]></url>\n" +
                "<supply_price><![CDATA[111.89]]></supply_price>\n" +
                "<producer_id><![CDATA[KL01 TU12 0230]]></producer_id>\n" +
                "<items>\n" +
                "<item>\n" +
                "<item_id><![CDATA[55468-48]]></item_id>\n" +
                "<item_size><![CDATA[48]]></item_size>\n" +
                "<market_price><![CDATA[195]]></market_price>\n" +
                "<sell_price><![CDATA[195]]></sell_price>\n" +
                "<supply_price><![CDATA[111.89]]></supply_price>\n" +
                "<color><![CDATA[Grey]]></color>\n" +
                "<description><![CDATA[Pantaloni Fantasia Grigio Quadri]]></description>\n" +
                "<item_detail><![CDATA[]]></item_detail>\n" +
                "<stock><![CDATA[1]]></stock>\n" +
                "<picture><![CDATA[http://www.acanfora.it/images/articoli/orig/Image_55468.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_2.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_3.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_4.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_5.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_6.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_7.jpg]]></picture>\n" +
                "</item>\n" +
                "<item>\n" +
                "<item_id><![CDATA[55468-52]]></item_id>\n" +
                "<item_size><![CDATA[52]]></item_size>\n" +
                "<market_price><![CDATA[195]]></market_price>\n" +
                "<sell_price><![CDATA[195]]></sell_price>\n" +
                "<supply_price><![CDATA[111.89]]></supply_price>\n" +
                "<color><![CDATA[Grey]]></color>\n" +
                "<description><![CDATA[Pantaloni Fantasia Grigio Quadri]]></description>\n" +
                "<item_detail><![CDATA[]]></item_detail>\n" +
                "<stock><![CDATA[1]]></stock>\n" +
                "<picture><![CDATA[http://www.acanfora.it/images/articoli/orig/Image_55468.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_2.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_3.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_4.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_5.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_6.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_7.jpg]]></picture>\n" +
                "</item>\n" +
                "<item>\n" +
                "<item_id><![CDATA[55468-54]]></item_id>\n" +
                "<item_size><![CDATA[54]]></item_size>\n" +
                "<market_price><![CDATA[195]]></market_price>\n" +
                "<sell_price><![CDATA[195]]></sell_price>\n" +
                "<supply_price><![CDATA[111.89]]></supply_price>\n" +
                "<color><![CDATA[Grey]]></color>\n" +
                "<description><![CDATA[Pantaloni Fantasia Grigio Quadri]]></description>\n" +
                "<item_detail><![CDATA[]]></item_detail>\n" +
                "<stock><![CDATA[1]]></stock>\n" +
                "<picture><![CDATA[http://www.acanfora.it/images/articoli/orig/Image_55468.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_2.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_3.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_4.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_5.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_6.jpg|http://www.acanfora.it/images/articoli/orig/Image_55468_7.jpg]]></picture>\n" +
                "</item>\n" +
                "</items>\n" +
                "</product>" ;
        try {
//           Products product= ObjectXMLUtil.xml2Obj(Products.class,xml);
            String kk=    HttpUtils.get("http://www.acanfora.it/api_ecommerce_v2.aspx");
                Products products= ObjectXMLUtil.xml2Obj(Products.class, kk);
                List<Product> p=  products.getProducts();
            System.out.print("kk ========"+p.get(0).getProductId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

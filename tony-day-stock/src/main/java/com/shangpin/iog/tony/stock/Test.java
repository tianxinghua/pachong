package com.shangpin.iog.tony.stock;

import com.google.gson.Gson;
import com.shangpin.iog.tony.common.Constant;
import com.shangpin.iog.tony.dto.Data;
import com.shangpin.iog.tony.dto.Items;
import com.shangpin.iog.tony.dto.ReturnObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhongren on 2016/3/7.
 */
public class Test {

    public static void main(String[] args){
        String itemsJson= "{\n" +
                "    \"status\": \"ok\",\n" +
                "    \"data\": {\n" +
                "        \"inventory\": [\n" +
                "            {\n" +
                "                \"_id\": {\n" +
                "                    \"$id\": \"561d9c8d2ec771f6c9f22977\"\n" +
                "                },\n" +
                "                \"cur\": 1,\n" +
                "                \"color\": \"Fucsia\",\n" +
                "                \"sex\": \"Female\",\n" +
                "                \"title\": \"Bauletto in pelle By The Way Mini\",\n" +
                "                \"cat_ids\": [\n" +
                "                    {\n" +
                "                        \"$id\": \"561d7300b49dbb9c2c551c8f\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"$id\": \"561d7300b49dbb9c2c551c4d\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"shopCatId\": \"81\",\n" +
                "                \"season\": \"AW-1516\",\n" +
                "                \"shopId\": \"5614354328499f45040041a7\",\n" +
                "                \"sku\": \"8BL1351D5_F022E-TU\",\n" +
                "                \"brand\": \"Fendi\",\n" +
                "                \"barcode\": \"21500369401\",\n" +
                "                \"age\": \"Adult\",\n" +
                "                \"desc\": \"Bauletto in pelle By The Way Mini<br>Modello in pelle di vitello<br />Chiusura centrale con zip<br />Tasca interna portaoggetti<br />Hardware argentati<br />Fodera in tessuto<br />2 manici piatti con borchie<br />Accessorio in pelle amovibile<br />Tracolla removibile e regolabile<br />Larghezza: 19 cm<br />Altezza: 13 cm<br />Profondità: 9 cm<br />Tracolla: 110 cm<br />Certificato di autenticità incluso<br>Posizione del logo: Targhetta di metallo sul davanti con logo inciso<br>100%, Pelle<br>Confezione: Sacchetto anti-polvere incluso<br>Made in: Made in Italy<br>Colore: fucsia<br>Product Model: By The Way\",\n" +
                "                \"size\": \"TU\",\n" +
                "                \"desc_en\": \"By The Way small leather handbag<br>Calf leather model<br />Central zip closure<br />Internal pocket<br />Fabric lining<br />2 flat handles with studs<br />Leather accessory removable<br />Silver metal hardware<br />Removable and adjustable shoulder belt<br />Width: 19 cm<br />Height: 13 cm<br />Depth: 9 cm<br />Strap: 110 cm<br />Certificate of authenticity included<br>Logo position: Metallic tag on the front with engraved logo<br>100%, Leather<br>Tailoring: Dust bag included<br>Colour: Fuchsia<br>Product Model: By The Way\",\n" +
                "                \"title_en\": \"By The Way small leather handbag\",\n" +
                "                \"color_en\": \"Fuxia\",\n" +
                "                \"qty\": 2,\n" +
                "                \"stock_price\": 920,\n" +
                "                \"images\": [\n" +
                "                    \"http://www.tonyboutique.com/media/catalog/product/8/b/8bl1351d5_f022e-1_3.jpg\",\n" +
                "                    \"http://www.tonyboutique.com/media/catalog/product/8/b/8bl1351d5_f022e-2_3.jpg\",\n" +
                "                    \"http://www.tonyboutique.com/media/catalog/product/8/b/8bl1351d5_f022e-6_3.jpg\",\n" +
                "                    \"http://www.tonyboutique.com/media/catalog/product/8/b/8bl1351d5_f022e-4_3.jpg\"\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"_id\": {\n" +
                "                    \"$id\": \"561d9c902ec771f6c9f22978\"\n" +
                "                },\n" +
                "                \"cur\": 1,\n" +
                "                \"color\": \"Argento\",\n" +
                "                \"sex\": \"Female\",\n" +
                "                \"title\": \"Pochette in pelle laminata\",\n" +
                "                \"cat_ids\": [\n" +
                "                    {\n" +
                "                        \"$id\": \"561d7300b49dbb9c2c551c8f\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"$id\": \"561d7300b49dbb9c2c551c4d\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"shopCatId\": \"81\",\n" +
                "                \"season\": \"AW-1516\",\n" +
                "                \"shopId\": \"5614354328499f45040041a7\",\n" +
                "                \"sku\": \"8M0346U9H_F0MK5-TU\",\n" +
                "                \"brand\": \"Fendi\",\n" +
                "                \"barcode\": \"21500377501\",\n" +
                "                \"age\": \"Adult\",\n" +
                "                \"desc\": \"Pochette in pelle laminata<br>Modello con logo in rilievo sulla patta<br />Chiusura con calamita<br />Interno attrezzato a portafoglio:<br />1 tasca<br />6 slot portatessera<br />1 tasca con chiusura a zip<br />Tracolla a catena removibile<br />Hardware argentati<br />Fodera in tessuto e pelle<br />Sottopatta in pelle scamosciata<br />Larghezza: 19 cm<br />Altezza: 13 cm<br />Profondità: 3 cm<br />Tracolla: 117 cm<br />Certificato di autenticità incluso<br>Posizione del logo: Targhetta di metallo sul davanti con logo inciso<br>Pelle<br>Confezione: Sacchetto anti-polvere incluso<br>Made in: Made in Italy<br>Colore: argento\",\n" +
                "                \"size\": \"TU\",\n" +
                "                \"desc_en\": \"Metallic leather clutch<br>Model with Fendi logo in relief on the flap<br />Flap with magnetic closure<br />1 flat pocket<br />1 zipped pocket<br />6 credit card slot<br />Model with removable chain shoulder strap<br />Fabric and leather lining<br />Silver metal hardware<br />Suede under flap<br />Width: 19 cm<br />Height: 13 cm<br />Depth: 3 cm<br />Strap: 117 cm<br />Certificate of authenticity included<br>Logo position: Metallic tag on the front with engraved logo<br>Leather<br>Tailoring: Dust bag included<br>Colour: Silver\",\n" +
                "                \"title_en\": \"Metallic leather clutch\",\n" +
                "                \"color_en\": \"Silver\",\n" +
                "                \"qty\": 3,\n" +
                "                \"stock_price\": 760,\n" +
                "                \"images\": [\n" +
                "                    \"http://www.tonyboutique.com/media/catalog/product/8/m/8m0346u9h_f0mk5-1_3.jpg\",\n" +
                "                    \"http://www.tonyboutique.com/media/catalog/product/8/m/8m0346u9h_f0mk5-2_3.jpg\",\n" +
                "                    \"http://www.tonyboutique.com/media/catalog/product/8/m/8m0346u9h_f0mk5-3_3.jpg\",\n" +
                "                    \"http://www.tonyboutique.com/media/catalog/product/8/m/8m0346u9h_f0mk5-4_3.jpg\"\n" +
                "                ]\n" +
                "            }" +

                "           ]\n" +
                "    },\n" +
                "    \"next_step\": 2\n" +
                "}";

        ReturnObject obj = new Gson().fromJson(itemsJson, ReturnObject.class);
        if(obj!=null){
            String next = obj.getNext_step();
            Data data = obj.getData();
            if(data!=null){
                Items[] array = data.getInventory();
                String skuId = "";
                if(array!=null){
                    for(Items item:array){
                        List<Map<String,String>> catMap = (ArrayList)item.getCat_ids();
                        System.out.println("id="+catMap.get(0).get("$id"));
                        skuId = item.getSku();
                        System.out.println("message =" + skuId+item.getQty());
                    }
                }

            }
        }
    }
}

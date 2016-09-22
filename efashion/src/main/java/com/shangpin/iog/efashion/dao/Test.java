package com.shangpin.iog.efashion.dao;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by lizhongren on 2016/9/21.
 */
public class Test {
    public static void main(String[] args){
        String json = "{\n" +
                "    \"results\":{\n" +
                "        \"reqCode\":200,\n" +
                "        \"count\":2,\n" +
                "        \"items\":[\n" +
                "            {\n" +
                "                \"product_id\":\"5731b94f2b33300afbc3b491\",\n" +
                "                \"product_reference\":\"0PALE\",\n" +
                "                \"color_reference\":\"ECRUU\",\n" +
                "                \"first_category\":\"CLOTHING\",\n" +
                "                \"second_category\":\"DRESSES\",\n" +
                "                \"gender\":\"WOMAN\",\n" +
                "                \"brand\":\"MES DEMOISELLES\",\n" +
                "                \"item_name\":\"Cotton Opale dress\",\n" +
                "                \"item_intro\":\"Cotton Opale dress\",\n" +
                "                \"item_description\":\"Flared dress realized in pure cotton. The model is enriched by V neckline with embroidered cut-out detail at front and a delicate pleating that gives movement. It includes three quarter sleeves with patch embroidery on hemline and two side seam pocket. Comfort fit.\",\n" +
                "                \"size\":\"51\",\n" +
                "                \"color\":\"Ecru\",\n" +
                "                \"quantity\":0,\n" +
                "                \"season_year\":\"2016\",\n" +
                "                \"season_reference\":\"PE\",\n" +
                "                \"made_in\":\"Undefined\",\n" +
                "                \"suitable\":[\n" +
                "                    {\n" +
                "                        \"name\":\"Sample Size\",\n" +
                "                        \"value\":\"36\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\":\"Shoulder Width \",\n" +
                "                        \"value\":\"14,17\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\":\"Chest Circumference \",\n" +
                "                        \"value\":\"19,68\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\":\"Back Lenght \",\n" +
                "                        \"value\":\"53,54\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"name\":\"Sleeve Length \",\n" +
                "                        \"value\":\"25,59\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"technical_info\":[\n" +
                "                    {\n" +
                "                        \"name\":\"100% Cotton\",\n" +
                "                        \"percentage\":\"\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"price\":122.95,\n" +
                "                \"currency\":\"EUR\",\n" +
                "                \"item_images\": [],\n" +
                "                \"retail_price\":150\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";

        try {
            json = json.replace("\"item_images\": [],","");
            System.out.println(json);
            ReturnObject obj = new Gson().fromJson(json, ReturnObject.class);
            System.out.print("ss" + obj.toString());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        /**
         *  "item_images":{
         " +
         "                    \"full\":[\n" +
         "                        \"http://geb-production.edstema.it/media/products/images/sheet/0PALEECRUU_1.jpg\",\n" +
         "                        \"http://geb-production.edstema.it/media/products/images/sheet/0PALEECRUU_2.jpg\",\n" +
         "                        \"http://geb-production.edstema.it/media/products/images/sheet/0PALEECRUU_3.jpg\"\n" +
         "                    ],\n" +
         "                    \"thumb\":[\n" +
         "                        \"http://geb-production.edstema.it/media/products/images/sheet_variant/0PALEECRUU_1.jpg\",\n" +
         "                        \"http://geb-production.edstema.it/media/products/images/sheet_variant/0PALEECRUU_2.jpg\",\n" +
         "                        \"http://geb-production.edstema.it/media/products/images/sheet_variant/0PALEECRUU_3.jpg\"\n" +
         "                    ]\n" +
         "                },\n" +
         */

    }
}

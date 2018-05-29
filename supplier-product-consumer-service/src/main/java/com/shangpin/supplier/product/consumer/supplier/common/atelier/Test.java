package com.shangpin.supplier.product.consumer.supplier.common.atelier;

import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierDate;

public class Test {
    public static void main(String[] args){

        String data = "{\"spuId\":\"4132945\",\"spu\":\"4132945;SS18;ORCIANI;B01979;NAPONOS SABBIA;Donna;Primavera/Estate;Borse;TRACOLLA;;BEIGE;;;;PITONE;Women bag by Orciani in beige python leather- Size 38x28x15 cm;1150;1150;;942,62;;NO;0;Chiari;;0;;;;;;;;;;01/03/2018 00:00:00;;;0;ORCIANI BEIGE PYTHON BAG;Italy;;Python;;Unica;;200;\",\"sku\":[\"4132945;UNI;1;0;01/03/2018 CONSEGNA INBS;2119523250828;0;0;0;0;0;0;0;0\"],\"image\":[\"4132945;http://93.39.241.194:8080/foto/SS18/oriciani/B01979NAPONOS SABBIA.JPG;0;\",\"4132945;http://93.39.241.194:8080/foto/SS18/oriciani/B01979NAPONOS SABBIA_1_P.JPG;0;\",\"4132945;http://93.39.241.194:8080/foto/SS18/oriciani/B01979NAPONOS SABBIA_2_P.JPG;0;\"],\"price\":\"4132945;SHANG;942,6205;1150;1150;0;;0\"}";
        AtelierDate atelierDate =  JsonUtil.deserialize(data,AtelierDate.class);
        System.out.println("atelierDate = " +atelierDate );

    }
}

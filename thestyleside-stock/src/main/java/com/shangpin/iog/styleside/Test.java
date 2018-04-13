package com.shangpin.iog.styleside;

/**
 * Created by lizhongren on 2018/4/13.
 */
public class Test {

    public static void main(String[] args){
        String kk = "81-991010RC-1083-0042-10;Bathingsuit BEACH BIMBA;DUE PEZZI FASCIA ROSES;Girl > Spring Summer, Girl > From 2 to 12 years, Girl > Beachwear;http://www.e-monnalisa.cn/cn/catalog/product/view/id/268925;http://www.e-monnalisa.cn/media/catalog/product/9/9/991010RC_1083_0001_A.jpg;;is in stock;1228;1228;;Monnalisa Beach;no;Girl > Spring Summer, Girl > From 2 to 12 years, Girl > Beachwear;http://www.e-monnalisa.cn/cn/catalog/product/view/id/268925;new;81-991010RC-1083;Coral;female;;\"主要面料：86％涤纶，14％氨纶/氨纶;内衬：92％涤纶，8％氨纶/氨纶“主要面料：86％涤纶，14％氨纶/氨纶;内衬：92％涤纶，8％氨纶/氨纶“\";;10;IT::Standard:10 EUR;;FALSE;;Primavera Estate;EU;2018";
        String[] sur = kk.split(";");
        System.out.println("size = " +sur.length);
        for(int i = 0;i<sur.length;i++){
            System.out.println(i + "=" + sur[i]);
        }

        String title = "id;title;description;google_product_category;link;image_link;additional_image_link;availability;price;sale_price;barcode;brand;adult;product_type;mobile_link;condition;item_group_id;color;gender;age_group;material;pattern;size;shipping;multipack;is_bundle;custom_label_0;season;country;anno";
        System.out.println("length ="+title.split(";").length);
    }
}

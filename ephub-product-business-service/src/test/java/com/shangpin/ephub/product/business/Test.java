
package com.shangpin.ephub.product.business;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by lizhongren on 2017/9/7.
 */

public class Test {

    public static void main(String[] args){
        try {
             String kk = "ARD AGNEAU CRAQUELE";
             String[] ar = kk.split("\\s+");
             for(String v:ar){
                 System.out.println("v = "+ v);
             }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


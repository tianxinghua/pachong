package com.shangpin.pending.product.consumer;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by lizhongren on 2017/1/19.
 */
public class CommonTest {

    public static void main(String[] args){
        String materail="尼龙 con 细节 in 真皮<br />锦纶tta con chiusura a scatto<br />chiusura superiore con cerniera<br />taschino 内部<br />2 manici in 真皮<br />larghezza: 30 cm<br />altezza: 28 cm<br />profoncità: 20 cmposizione del logo: logo inciso sul bottone100%, poliammide, 100%, pvc, 100%, 真皮colore: nero, marroneproduct model: le pliage";
        if (StringUtils.isNotBlank(materail)) {
            System.out.println(materail.replaceAll("<br />", "\r\n").replaceAll("<html>", "")
                    .replaceAll("</html>", "").replaceAll("<br>","\r\n"));
        }
        Map<String,String> commonSizeMap = new LinkedHashMap<>();
        commonSizeMap.put("++",".5");
        commonSizeMap.put("+",".5");
        Set<String> sizeSet  = commonSizeMap.keySet();//sizeMap.keySet();

        String size = "32+";


        String size1 = "33++";

        String commkey = "";
        for(String sizeKey:sizeSet){
            if("++".equals(sizeKey)){
                commkey = "\\++";
            }else  if("+".equals(sizeKey)){
                commkey = "\\+";
            }
            if(size.indexOf(sizeKey)>=0){
                size = size.replaceAll(commkey,commonSizeMap.get(sizeKey));
                System.out.println("size =" + size );
            }
            if(size1.indexOf(sizeKey)>=0){
                size1 = size1.replaceAll(commkey,commonSizeMap.get(sizeKey));
                System.out.println("size1 =" + size1 );
            }
        }
    }
}

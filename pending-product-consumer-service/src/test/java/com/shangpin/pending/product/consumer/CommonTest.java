package com.shangpin.pending.product.consumer;

import org.apache.commons.lang3.StringUtils;

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
    }
}

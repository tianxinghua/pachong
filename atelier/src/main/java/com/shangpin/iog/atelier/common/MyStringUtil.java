package com.shangpin.iog.atelier.common;

import java.io.FileInputStream;

/**
 * Created by Administrator on 2015/9/30.
 */
public class MyStringUtil {
    /**
     *get pictrue url
     * ex:171003;http://image.atelieronweb.com/foto/thumbs2_P14\BURBERRY\171003.JPG;0
     * */
     static String[] getPicUrl(String skuId,String itemImages){
        String[] images = itemImages.split(skuId+";");
        return images;
    }

    /**
     * get stock
     * ex:457904;2;1;0;;2113409332486;;;;;;;;
     * */
    public static String getStockBySkuId(String item){
        return item.split(";")[2];
    }
    /**
     * get stock
     * ex:457904;2;1;0;;2113409332486;;;;;;;;
     * */
    public static String getBarcodeBySkuId(String item){
        return item.split(";")[5];
    }
    /**
     *parse file to string
     */
    public static  String parseFile2Str(String file) {

        StringBuffer sb = new StringBuffer();
        try {
            // 创建文件输入流对象
            FileInputStream is = new FileInputStream(file);
            // 设定读取的字节数
            int n = 2048;
            byte buffer[] = new byte[n];
            // 读取输入流
            while ((is.read(buffer, 0, n) != -1) && (n > 0)) {
                sb.append(new String(buffer,"utf-8"));
            }
            // 关闭输入流
            is.close();
        }  catch (Exception e) {
            System.out.println("parseFile2Str获取文件字符串失败");
        }
        //System.out.println("output io string==="+sb.toString().length());
        return sb.toString();
    }
}

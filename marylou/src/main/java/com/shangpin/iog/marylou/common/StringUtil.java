package com.shangpin.iog.marylou.common;

import java.io.FileInputStream;

/**
 * Created by wangyuzhi on 2015/9/10.
 */
public class StringUtil {

    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
        String localFile = StringUtil.parseXml2Str();
        System.out.println(localFile);
        System.out.println("00000000000000000000000000000");
    }

    /**
     *字节流方式读取本地HTTP方式获取的xml文件
     */
    public static  String parseXml2Str() {

        StringBuffer sb = new StringBuffer();
        try {
            // 创建文件输入流对象
            FileInputStream is = new FileInputStream("E:/shangpin.xml");
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
            System.out.println(e);
        }
        return sb.toString();
    }
}

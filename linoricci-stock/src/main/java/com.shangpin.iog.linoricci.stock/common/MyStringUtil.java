package com.shangpin.iog.linoricci.stock.common;

import com.shangpin.iog.linoricci.stock.common.Constant;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by wangyuzhi on 2015/9/10.
 */
public class MyStringUtil {
    private static Logger loggerError = Logger.getLogger("error");

    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
        String localFile = new MyStringUtil().parseXml2Str("");
        //System.out.println(localFile);
        System.out.println("00000000000000000000000000000");
    }

    /**
     *字节流方式读取本地ftp方式获取的xml文件
     */
    public  String parseXml2Str(String fileName) {

        StringBuffer sb = new StringBuffer();
        try {
            // 创建文件输入流对象
            FileInputStream is = new FileInputStream(fileName);
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
            System.out.println("e=="+e);
            loggerError.error("解析LINORICCI数据失败，返回空串");
            return "";
        }
        System.out.println("output io string==="+sb.toString().length());
        return sb.toString();
    }

    /**
     *获取本地FTP空文件，如果没有则创建
     */
    public String getLocalFileName(){
        File file = new File(Constant.LOCAL_ITEMS_FILE);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Constant.LOCAL_ITEMS_FILE;
    }

    /**
     * get stock
     * ex:457904;2;1;0;;2113409332486;;;;;;;;
     * */
    public static String getStockBySkuId(String item){
        return item.split(";")[2];
    }

}

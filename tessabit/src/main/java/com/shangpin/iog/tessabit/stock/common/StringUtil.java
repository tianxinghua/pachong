package com.shangpin.iog.tessabit.stock.common;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.xml.stream.events.Attribute;
import java.io.*;
import java.util.List;

/**
 * Created by wangyuzhi on 2015/9/10.
 */
public class StringUtil {
    private static Logger loggerError = Logger.getLogger("error");

    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
        String localFile = new StringUtil().parseXml2Str();
        //System.out.println(localFile);
        System.out.println("00000000000000000000000000000");
        System.out.println(StringUtil.getSubBySub(localFile,"1984127411_12","1984127411_12",1500));
    }

    /**
     *字节流方式读取本地ftp方式获取的xml文件
     */
    public  String parseXml2Str() {

        StringBuffer sb = new StringBuffer();
        try {
            // 创建文件输入流对象
            FileInputStream is = new FileInputStream(Constant.LOCAL_FILE);
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
            loggerError.error("解析TESSABIT数据失败，返回空串");
            return "";
        }
        System.out.println("output io string==="+sb.toString().length());
        return sb.toString();
    }

    /**
     *获取本地FTP空文件，如果没有则创建
     */
    public String getLocalFileName(){
        File file = new File(Constant.LOCAL_FILE);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Constant.LOCAL_FILE;
    }
    public String getLocalPictureFileName(){
        File file = new File(Constant.LOCAL_PICTURE);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Constant.LOCAL_PICTURE;
    }
    /**
     *获取单品数量和价格
     */
    public static String getStockAndSupplyPrice(String item){

        System.out.println("item=="+item);
        String rtnStr = new StringBuffer(item.substring(item.indexOf("<stock>")+7,item.indexOf("</stock>")))
                .append("/")
                .append(item.substring(item.indexOf("<supply_price>")+14, item.indexOf("</supply_price>"))).toString();
        //System.out.println("rtnStr=="+rtnStr);
        return rtnStr;
    }
    /**
     *获取单品数量
     */
    public static String getSubBySub(String str,String begin,String end,int eAdd){
        if("".equals(str)){
            System.out.println("file is null,stock changed into 0");
            return "0";
        }
        str = str.substring(str.indexOf(begin)+begin.length(),str.indexOf(end)+eAdd);
        if(str.contains("stock")){
            str = getSubBySub(str, "<stock>", "</stock>", 0);
        }
        return str;
    }

    /**
     *将数据中乱码替换为加号
     */
    public static  String convertStr(String str) {
        if(str.indexOf("½")>0){
            str = str.replace("½","+");
        }
        return str;
    }
}

package com.shangpin.iog.onsite.base.constance;

import java.util.ResourceBundle;

/**
 * Created by wangyuzhi on 2015/9/11.
 */
public class Constant {
    public static ResourceBundle bundle = ResourceBundle.getBundle("conf");
    //供应商ID
    public static String SUPPLIER_ID = bundle.getString("supplierId");
    //供应商ID_MARYLOU
    public static String SUPP_ID_MARYLOU = bundle.getString("supplierIdMarylou");
    //供应商ID_TONY
    public static String SUPP_ID_TONY = bundle.getString("supplierIdTony");
    //FTP服务器hostname
    public static String URL = bundle.getString("url");
    //FTP服务器根目录
    public static String REMOTE_PATH = bundle.getString("remotePath");
    //FTP服务器端口
    public static String PORT = bundle.getString("port");
    //用户名
    public static String USER = bundle.getString("user");
    //密码
    public static String PASSWORD = bundle.getString("password");
    //服务器端文件名(包括完整路径)
    public static String SERVER_FILE = bundle.getString("serverFile");
    //本地文件名(包括完整路径)
    public static String LOCAL_FILE = bundle.getString("localFile");
    //item在ftp文本中所占字符串长度
    public static int ITEM_LENTH = 1500;
    //marylou 的URL
    public static String URL_MARYLOU = "http://shop.marylou.it/shangpin.aspx";
    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
         System.out.println("Constant.URL=" + Constant.URL);
    }
}

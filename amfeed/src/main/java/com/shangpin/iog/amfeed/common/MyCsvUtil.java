package com.shangpin.iog.amfeed.common;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2015/11/9.
 */
public class MyCsvUtil {
    private static ResourceBundle bdl = null;
    private static String httpurl;
    private static String path;
    static {
        if (bdl == null)
            bdl = ResourceBundle.getBundle("conf");
        System.out.println(bdl+"---");
        httpurl = bdl.getString("product");
        System.out.println(httpurl+"--33-");
        httpurl = bdl.getString("url");
            path = bdl.getString("path");
    }
    /**
     * http下载csv文件到本地路径
     * @throws MalformedURLException
     */
    public static void csvDownload() throws MalformedURLException {
        int bytesum = 0;
        int byteread = 0;

        URL url = new URL(httpurl);
        String realPath=path;
        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream(realPath);

            byte[] buffer = new byte[1204];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/**
 * test
 * */
    public static void main(String[] args) {
        try {
            System.out.println("下载中");
            new MyCsvUtil().csvDownload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.shangpin.picture.product.consumer.service;

import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.fdfs.dto.UploadPicDto;
import com.shangpin.picture.product.consumer.bean.AuthenticationInformation;
import com.shangpin.picture.product.consumer.e.PicHandleState;
import org.apache.commons.io.IOUtils;

import sun.misc.BASE64Encoder;
import sun.net.www.protocol.ftp.FtpURLConnection;

import java.io.InputStream;
import java.net.*;
import java.util.Date;

public class Test {

    public static void main(String[] args){
        Test test = new Test();
        HubSpuPendingPicDto dto  = new HubSpuPendingPicDto();
        AuthenticationInformation auth = new AuthenticationInformation();
        auth.setUsername("Shangpin");
        auth.setPassword("Shangpin17!");
        test.pullPicAndPushToPicServer("ftp://Shangpin:Shangpin17!@88.149.230.95:24/Marketplace_Photo/740955002181001 (1).jpg",dto,auth);
    }


    private int pullPicAndPushToPicServer(String picUrl, HubSpuPendingPicDto dto, AuthenticationInformation
            authenticationInformation){
        InputStream inputStream = null;
        FtpURLConnection httpUrlConnection = null;
        int flag = 0;
        try {
            if (authenticationInformation != null) {//需要认证
                Authenticator.setDefault(new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(authenticationInformation.getUsername(),
                                new String(authenticationInformation.getPassword()).toCharArray());
                    }
                });
            }
            URL url = new URL(picUrl.replaceAll(" +", "%20"));
            URLConnection openConnection = url.openConnection();
            httpUrlConnection  =  (FtpURLConnection) openConnection;
            httpUrlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
            httpUrlConnection.setConnectTimeout(10*1000);
            httpUrlConnection.setReadTimeout(45*60*1000);
            httpUrlConnection.connect();

//            if (flag == 404 || flag == 400) {
//                return flag;
//            }else if(flag == 301 || flag == 302){
//
//                String newPicUrl = picUrl.replaceFirst("http", "https");
//                return pullPicAndPushToPicServer(newPicUrl,dto,authenticationInformation);
//            }
            inputStream = openConnection.getInputStream();
            byte[] byteArray = IOUtils.toByteArray(inputStream);
            if (byteArray == null || byteArray.length == 0) {
                throw new RuntimeException("读取到的图片字节为空,无法获取图片");
            }
            String base64 = new BASE64Encoder().encode(byteArray);


            dto.setPicHandleState(PicHandleState.HANDLED.getIndex());
            dto.setMemo("图片拉取成功");

        }catch (Throwable e) {

            e.printStackTrace();
            dto.setPicHandleState(PicHandleState.HANDLE_ERROR.getIndex());
            dto.setMemo("图片拉取失败:"+flag);
        } finally {
            closeFtpURL(inputStream,httpUrlConnection);
        }
        dto.setUpdateTime(new Date());
        return flag;
    }

    private void closeFtpURL(InputStream inputStream, FtpURLConnection httpUrlConnection) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Throwable e) {
//                log.error("关闭资源流发生异常", e);
                e.printStackTrace();
                throw new RuntimeException("关闭资源流发生异常");
            }
        }
        if (httpUrlConnection != null) {
            try {
                httpUrlConnection.close();
            } catch (Throwable e) {
//                log.error("关闭链接发生异常", e);
                e.printStackTrace();
                throw new RuntimeException("关闭链接发生异常");
            }
        }
    }
}

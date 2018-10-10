package com.shangpin.picture.product.consumer.service;

import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.fdfs.dto.UploadPicDto;
import com.shangpin.picture.product.consumer.bean.AuthenticationInformation;
import com.shangpin.picture.product.consumer.e.PicHandleState;
import com.shangpin.picture.product.consumer.manager.SupplierProductPictureManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.*;

/**
 * Created by 极客世界 on 2018/9/27.
 */
public class ImageDownload {
    @Autowired
    private SupplierProductPictureManager supplierProductPictureManager;
  /*  public static void main(String[] args) {
        String url = "https://cache.mrporter.com/images/products/1054190/1054190_mrp_in_l.jpg";
        downloadPicture(url);
    }*/
    //链接url下载图片
    public   int downloadPicture(String urlList,HubSpuPendingPicDto dto, AuthenticationInformation authenticationInformation) {
        URL url = null;
            int imageNumber = 0;

            try {
                if (authenticationInformation != null) {//需要认证
                    Authenticator.setDefault(new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(authenticationInformation.getUsername(),
                                    new String(authenticationInformation.getPassword()).toCharArray());
                        }
                    });
                }

                url = new URL(urlList);
                urlList= urlList.replaceAll(" +", "%20");//.replaceFirst("http", "https");
                //DataInputStream dataInputStream = new DataInputStream(url.openStream());
                URLConnection openConnection = url.openConnection();
                InputStream inputStream = openConnection.getInputStream();
                byte[] byteArray = IOUtils.toByteArray(inputStream);
                if (byteArray == null || byteArray.length == 0) {
                    throw new RuntimeException("读取到的图片字节为空,无法获取图片");
                }
                String base64 = new BASE64Encoder().encode(byteArray);
                UploadPicDto uploadPicDto = new UploadPicDto();
                uploadPicDto.setRequestId(String.valueOf(dto.getSpuPendingPicId()));
                uploadPicDto.setBase64(base64);
                uploadPicDto.setExtension(getExtension(urlList));
                String fdfsURL = supplierProductPictureManager.uploadPic(uploadPicDto);
                dto.setSpPicUrl(fdfsURL);
                dto.setPicHandleState(PicHandleState.HANDLED.getIndex());
                dto.setMemo("图片拉取成功");
                inputStream.close();
                return 200;
        } catch (Exception e) {
            e.printStackTrace();
                return 400;
        }
    }
    private String getExtension(String url){
        if (StringUtils.isNotBlank(url)) {
            String suffix = url.substring(url.lastIndexOf(".")+1);
            if (suffix == null) {
                return "jpg";
            }
            if (suffix != null && suffix.length() > 5) {
                return "jpg";
            }
            return suffix;
        } else {
            return null;
        }
    }
}

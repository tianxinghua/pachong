package com.shangpin.ephub.fdfs.client.service.upload.manager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import com.github.tobato.fastdfs.service.DefaultFastFileStorageClient;
import com.shangpin.ephub.fdfs.client.service.conf.fdfs.ApiAddressProperties;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.shangpin.ephub.fdfs.client.service.conf.fdfs.FastDFSConf;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;
import sun.security.provider.MD5;

/**
 * <p>Title:UploadPicManager.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月2日 下午5:37:37
 */
@Component
@Slf4j
public class FastDFSClientManager {

	@Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private FastDFSConf appConfig;

    private static final int TIMEOUT = 45*60*1000;

    private static final int CONNECT_TIMEOUT = 10*1000;

    @Autowired
    ApiAddressProperties apiAddressProperties;
    /**
     * 上传文件
     * @param buffer 文件对象
     * @return 文件访问地址
     * @throws IOException
     */
    public  String uploadFile(byte[] buffer, String extension,String requestId) {
//        FastFileStorageClient storageClient = new DefaultFastFileStorageClient();
        boolean upload = true;
        String picUrl = "";
        int i=0;
        while(upload){

            StorePath storePath = storageClient.uploadFile(apiAddressProperties.getGroupName(), new ByteArrayInputStream(buffer), buffer.length, extension);

//        StorePath storePath = storageClient.uploadFile(file.getInputStream(),file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()),null);
             picUrl  =  getResAccessUrl(storePath);
             byte[]  newPicByte = this.pullPicAndPushToPicServer(picUrl);
            //md5 校验
            try {
              String oldMd5 = this.MD5(buffer);
              String newMd5 = this.MD5(newPicByte);
              if(oldMd5.equals(newMd5)){

                  upload = false;
              }else{
                  i++;
                  if(5==i){
                      //防止异常
                      upload = false;
                  }
                  log.error("请求编号" + null!=requestId?requestId:"" + ",第" + i+"次发生错乱");
              }

            } catch (Exception e) {
                e.printStackTrace();
            }
            //


        }
        return picUrl;
    }



    /**
     * 将一段字符串生成一个文件上传
     * @param content 文件内容
     * @param fileExtension
     * @return
     */
    public String uploadFile(String content, String fileExtension) {
        byte[] buff = content.getBytes(Charset.forName("UTF-8"));
        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
        StorePath storePath = storageClient.uploadFile(stream,buff.length, fileExtension,null);
        return getResAccessUrl(storePath);
    }

    // 封装图片完整URL地址
    private String getResAccessUrl(StorePath storePath) {
        String fileUrl = FastDFSConf.HTTP_PRODOCOL + appConfig.getResHost()
                + ":" + appConfig.getFdfsStoragePort() + "/" + storePath.getFullPath();
        return fileUrl;
    }

    /**
     * 删除文件
     * @param fileUrl 文件访问地址
     * @return
     */
    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }
        try {
            StorePath storePath = StorePath.praseFromUrl(fileUrl);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        } catch (FdfsUnsupportStorePathException e) {
            log.warn(e.getMessage(),e);
            throw new RuntimeException(e.getMessage(),e);
        }
    }


    public  String MD5(byte[] btInput) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {

            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    private byte[] pullPicAndPushToPicServer(String picUrl){
        InputStream inputStream = null;
        HttpURLConnection httpUrlConnection = null;
        int flag = 0;
        try {

            URL url = new URL(picUrl.replaceAll(" +", "%20"));
            URLConnection openConnection = url.openConnection();
            httpUrlConnection  =  (HttpURLConnection) openConnection;
            httpUrlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
            httpUrlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpUrlConnection.setReadTimeout(TIMEOUT);
            httpUrlConnection.connect();
            flag = httpUrlConnection.getResponseCode();

            inputStream = openConnection.getInputStream();
            byte[] byteArray = IOUtils.toByteArray(inputStream);
            if (byteArray == null || byteArray.length == 0) {
                throw new RuntimeException("读取到的图片字节为空,无法获取图片");
            }
            return byteArray;


        }catch (Throwable e) {
            log.error("系统拉取图片时发生异常,url ="+picUrl,e);

        } finally {
            close(inputStream, httpUrlConnection);
        }

        return null;
    }

    private void close(InputStream inputStream, HttpURLConnection httpUrlConnection) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Throwable e) {
                log.error("关闭资源流发生异常", e);
                e.printStackTrace();
                throw new RuntimeException("关闭资源流发生异常");
            }
        }
        if (httpUrlConnection != null) {
            try {
                httpUrlConnection.disconnect();
            } catch (Throwable e) {
                log.error("关闭链接发生异常", e);
                e.printStackTrace();
                throw new RuntimeException("关闭链接发生异常");
            }
        }
    }


}
package com.shangpin.ephub.fdfs.client.service.upload.manager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.exception.FdfsUnsupportStorePathException;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.shangpin.ephub.fdfs.client.service.conf.fdfs.FastDFSConf;

import lombok.extern.slf4j.Slf4j;

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
    /**
     * 上传文件
     * @param buffer 文件对象
     * @return 文件访问地址
     * @throws IOException
     */
    public String uploadFile(byte[] buffer) {
    	StorePath storePath = storageClient.uploadFile("group2", new ByteArrayInputStream(buffer), buffer.length, ".jpg");
      //  StorePath storePath = storageClient.uploadFile(file.getInputStream(),file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()),null);
        return getResAccessUrl(storePath);
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
            log.warn(e.getMessage());
        }
    }
}
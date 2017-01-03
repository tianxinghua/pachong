package com.shangpin.ephub.fdfs.client.service.conf.fdfs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.tobato.fastdfs.FdfsClientConfig;

/**
 * <p>Title:FastDFSConf.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月2日 下午5:46:46
 */
@Configuration
@Import(FdfsClientConfig.class)
public class FastDFSConf {
	
	public static final String HTTP_PRODOCOL = "http://";

	public String getFdfsStoragePort() {
		return "80";
	}

	public String getResHost() {
		return "192.168.9.71";
	}

}

package com.shangpin.ephub.fdfs.client.service.conf.fdfs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * <p>Title:FastDFSConf.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月2日 下午5:46:46
 */
@Configuration
@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FastDFSConf {

	@Autowired
	ApiAddressProperties apiAddressProperties;

	public static final String HTTP_PRODOCOL = "http://";

	public String getFdfsStoragePort() {
		return apiAddressProperties.getPort();
	}

	public String getResHost() {
		return apiAddressProperties.getResHost();
	}

}

package com.shangpin.asynchronous.task.consumer.conf.ftp;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by lizhongren on 2016/11/20.
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "spring.ftp")
@Component
public class FtpProperties implements Serializable {

		
    /**
	 * 
	 */
	private static final long serialVersionUID = -1608829698675620854L;

	private String host;
	private String port;
    private String userName;
    private String password;
    private String ftpHubPath;
    private String ftpPendingPath;
}

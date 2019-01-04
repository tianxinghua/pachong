package com.shangpin.api.airshop.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/** 
 * ClassName:ImageCDNConfig <br/> 
 * TODO 该类的作用<br/>
 * @date    2016年4月30日 <br/> 
 * @author   陈小峰
 * @since    JDK 7
 */
@Setter
@Getter
@ConfigurationProperties("airshop.imageCDN")
@Component
public class ImageCDNConfig {

	private List<String> cdnList;
}

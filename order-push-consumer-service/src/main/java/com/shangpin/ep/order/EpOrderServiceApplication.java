package com.shangpin.ep.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
/**
 * EP订单实时推送系统入口
 * <p>Title:EpOrderServiceApplication.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月9日 上午11:06:26
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class EpOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpOrderServiceApplication.class, args);
	}
}

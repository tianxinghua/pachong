package com.shangpin.ephub.data.schedule.conf.schedule.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
@ConfigurationProperties(prefix = "spring.schedule")
public class ScheduleConfig {

	/**
	 * 产品任务是否开始
	 */
	private boolean startPro;
	/**
	 * 库存任务是否开始
	 */
	private boolean startStock;
}

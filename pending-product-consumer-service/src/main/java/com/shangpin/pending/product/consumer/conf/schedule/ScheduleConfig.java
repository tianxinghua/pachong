package com.shangpin.pending.product.consumer.conf.schedule;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
@ConfigurationProperties(prefix = "shangpin.schedule")
public class ScheduleConfig {

	/**
	 * 品牌任务是否开始
	 */
	private boolean brand;

}

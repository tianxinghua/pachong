package com.shangpin.ephub.client.message.task.product.body;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>Title:ProductTask.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月19日 下午7:19:28
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductImportTask {
	/**
	 * 消息编号
	 */
	private String messageId;
	/**
	 * 消息发送时间
	 */
	private String messageDate;
	
	/**
	 * 任务类型  =0 一般任务  =1 pending-spu导入 =2 pending-sku导入处   =3 hub导入  =4 pending-spu导出  =5 pending-sku导出
	 */
	private int type;
	
    /**
     * 任务编号
     */
	private String taskNo;
	
	/**
	 * json格式的数据
	 */
	private String data;

}

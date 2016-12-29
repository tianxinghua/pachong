package com.shangpin.ephub.client.data.mysql.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

/**
 * Created by loyalty on 16/12/23.
 * 货号列表
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpuModelDto implements Serializable {

   /**
	 * 
	 */
   private static final long serialVersionUID = 5147199192561457186L;

   private String spuModel;//货号

   private String brandNo;
}

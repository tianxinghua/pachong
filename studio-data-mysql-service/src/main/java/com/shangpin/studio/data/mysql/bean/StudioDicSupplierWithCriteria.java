package com.shangpin.studio.data.mysql.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.shangpin.studio.data.mysql.po.dic.StudioDicSupplier;
import com.shangpin.studio.data.mysql.po.dic.StudioDicSupplierCriteria;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudioDicSupplierWithCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5103951128810875746L;

	private StudioDicSupplier studioDicSupplier;
	
	private StudioDicSupplierCriteria criteria;
}

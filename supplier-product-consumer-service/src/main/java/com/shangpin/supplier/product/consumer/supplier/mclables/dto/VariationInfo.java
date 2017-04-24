package com.shangpin.supplier.product.consumer.supplier.mclables.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VariationInfo {

	private String IsInRelationship ;
	private String RelationshipName;
	private String IsParent;
	private String ParentSku;
}

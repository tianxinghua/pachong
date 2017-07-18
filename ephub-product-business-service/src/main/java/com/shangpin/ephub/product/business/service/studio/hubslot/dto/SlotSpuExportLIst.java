package com.shangpin.ephub.product.business.service.studio.hubslot.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SlotSpuExportLIst implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4904184962327167262L;
	
	private List<SlotSpuExportDto> slotSpus;
}

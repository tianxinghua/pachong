package com.shangpin.ephub.client.data.mysql.studio.supplier.dto;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HubSlotSpuSupplierWithCriteriaDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4015078084915268295L;

	private HubSlotSpuSupplierDto hubSlotSpuSupplierDto;
	
	private HubSlotSpuSupplierCriteriaDto criteria;
}

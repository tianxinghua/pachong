package com.shangpin.ephub.product.business.ui.hub.waitselected.vo;

import java.io.Serializable;
import java.util.List;

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
public class HubWaitSelectedResponseWithPageDto implements Serializable {
	private static final long serialVersionUID = -9080013205013160520L;

	private int total;
	private List<HubWaitSelectedResponseDto> list;

}
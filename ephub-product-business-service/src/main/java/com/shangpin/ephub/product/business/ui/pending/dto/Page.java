package com.shangpin.ephub.product.business.ui.pending.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Page {

	private int total;
	private List<ReasonResponseDto> list;
}

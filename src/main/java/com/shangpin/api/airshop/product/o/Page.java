package com.shangpin.api.airshop.product.o;

import java.util.List;

import com.shangpin.api.airshop.dto.NotOnShelfDTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Page {

	private int total;
	private List<NotOnShelfDTO> list;
}

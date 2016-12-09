package com.shangpin.ep.order.module.orderapiservice.impl.dto.inviqa;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Messages {

	private List<Errors> error;
}

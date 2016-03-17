package com.shangpin.iog.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderTimeUpdateDTO implements Serializable{

	private static final long serialVersionUID = 4078982631318898469L;
	
	private String supplierId;
	private Date updateTime ;

	
}

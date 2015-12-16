package com.shangpin.iog.coltorti.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
	private String name;
	private String address;
	private String city;
	private String country;
	private String mobile;
}

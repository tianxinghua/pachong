package com.shangpin.iog.papini.jsonDTO;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Items {
	
	private String number_of_items;
    private List<Item> item;

}

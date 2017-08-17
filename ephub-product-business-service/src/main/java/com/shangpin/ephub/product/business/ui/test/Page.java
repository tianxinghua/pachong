package com.shangpin.ephub.product.business.ui.test;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Page {

	private int total;
	private List<JSONObject> list;
}

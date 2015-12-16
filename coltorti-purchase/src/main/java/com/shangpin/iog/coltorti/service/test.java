package com.shangpin.iog.coltorti.service;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.shangpin.iog.coltorti.dto.Customer;
import com.shangpin.iog.coltorti.dto.OrderJson;
import com.shangpin.iog.coltorti.dto.Product;

public class test {
	public static void main(String[] args) {
		Gson gson = new Gson();
		OrderJson oj = new OrderJson();
		oj.setCustomer(new Customer("aaa", "bbb", "ccc", "ddd", "eee"));
		oj.setOrder_id("111");
		List<Product> products = new ArrayList<Product>();
		products.add(new Product("zzz", "xxx", "vvv"));
		oj.setProducts(products );
		String json = gson.toJson(oj);
		System.out.println(json);
	}
}

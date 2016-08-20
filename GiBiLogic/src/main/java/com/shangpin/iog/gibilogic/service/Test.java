package com.shangpin.iog.gibilogic.service;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

public class Test {
	public static void main(String[] args) {
		String str = HttpUtil45.get("http://shop.areadocks.it/en/api/category?page=1&pagesize=100",
				new OutTimeConfig(1000 * 60 * 10, 1000 * 60 * 10,
						1000 * 60 * 10), null);
		System.out.println(str);

		URL url;
		try {
			url = new URL("http://shop.areadocks.it/en/api/category?page=1&pagesize=100");
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			String string = IOUtils.toString(inStream);
			
			System.out.println(string);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

package com.shangpin.iog.revolve.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.csvreader.CsvReader;

public class Test {
	public static void main(String[] args) {
		try {
			InputStream in = new FileInputStream("F:\\head-revolveChinaMarketplace.txt");
			CsvReader csvReader = new CsvReader(in, Charset.forName("utf-8"));
//			CsvReader csvReader = new CsvReader("F:\\head-revolveChinaMarketplace.txt");
			while(csvReader.readRecord()){
				System.out.println(csvReader.getRawRecord());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

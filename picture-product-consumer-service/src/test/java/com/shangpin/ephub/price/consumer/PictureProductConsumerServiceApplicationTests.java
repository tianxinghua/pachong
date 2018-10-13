package com.shangpin.ephub.price.consumer;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.JsonArray;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.DateUtil;
import org.junit.Test;

import sun.misc.BASE64Encoder;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PictureProductConsumerServiceApplicationTests {


	public static void main(String[] args) throws Throwable {
//		try {
//			String ou = "https://assets.burberry.com/is/image/Burberryltd/53c086827d53f890e8893581c5bdc42a05263062.jpg?$BBY_V2_ML_1X1$&wid=1920&hei=1920";
////			URL url = new URL(ou.replaceAll(" +", "%20"));
////			URLConnection openConnection = url.openConnection();
////			openConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
////			openConnection.setConnectTimeout(45*60*1000);
////			openConnection.setReadTimeout(45*60*1000);
////			InputStream inputStream = openConnection.getInputStream();
//
//
//			URL url = new URL(ou);
//			ou= ou.replaceAll(" +", "%20");//.replaceFirst("http", "https");
//			//DataInputStream dataInputStream = new DataInputStream(url.openStream());
//			URLConnection openConnection = url.openConnection();
//
//			InputStream inputStream = openConnection.getInputStream();
//			byte[] byteArray = IOUtils.toByteArray(inputStream);
//			String base64 = new BASE64Encoder().encode(byteArray);
//			System.out.println(base64);
//			if (StringUtils.isBlank(base64)) {
//				throw new RuntimeException("读取到的图片内容为空,无法获取图片");
//			}
//		} catch (Throwable e) {
//			e.printStackTrace();
//			System.out.println("发生异常。。。。。。。");
//		}
//		System.out.println("over.......");

		 Pattern pattern = Pattern.compile("^\\[(.*) \\+\\d+\\](.*)key=(.*)$");
         String ss = "[12/Oct/2018:09:00:02 +0800] 27.186.110.182  key=%5B%7B%22location%22%3A%22116.581832%2C39.922102%22%2C%22ip%22%3A%22%22%2C%22position%22%3A%22%22%2C%22bhv_from%22%3A%22%22%2C%22url%22%3A%22%22%2C%22cookie%22%3A%22%22%2C%22user_id%22%3A%22F18C49A548E29CEF1DF9EB36DEBA7582%22%2C%22vid%22%3A%223.2.6%22%2C%22UAChannelFrom%22%3A%22%22%2C%22imei%22%3A%22ccec3d58b9db0765d8a1baff33d9b11b%22%2C%22agent%22%3A%22%22%2C%22query%22%3A%22%22%2C%22media%22%3A%22%22%2C%22os%22%3A%2211.4%22%2C%22UAEventKey%22%3A%22entry%22%2C%22bhv%22%3A%22entry%22%2C%22USPositionKey%22%3A%22%22%2C%22referrer%22%3A%22%22%2C%22grade%22%3A%22%22%2C%22timestamp%22%3A%221539306000421.010986%22%2C%22network%22%3A%221%22%2C%22item_id%22%3A%22%22%2C%22model%22%3A%22Unknown%20iPhone%22%2C%22USIdKey%22%3A%22%22%2C%22UALevelRelation%22%3A%224%7C2%22%2C%22device%22%3A%220%22%2C%22trace_id%22%3A%22%22%7D%2C%7B%22location%22%3A%22116.581832%2C39.922102%22%2C%22ip%22%3A%22%22%2C%22position%22%3A%22%22%2C%22bhv_from%22%3A%22B0109%22%2C%22url%22%3A%22%22%2C%22cookie%22%3A%22%22%2C%22user_id%22%3A%22F18C49A548E29CEF1DF9EB36DEBA7582%22%2C%22vid%22%3A%223.2.6%22%2C%22UAChannelFrom%22%3A%22%22%2C%22imei%22%3A%22ccec3d58b9db0765d8a1baff33d9b11b%22%2C%22agent%22%3A%22%22%2C%22query%22%3A%22%22%2C%22media%22%3A%22%22%2C%22os%22%3A%2211.4%22%2C%22UAEventKey%22%3A%22List_Click_Sort_New%22%2C%22bhv%22%3A%22List_Click_Sort_New%22%2C%22USPositionKey%22%3A%22%22%2C%22referrer%22%3A%22%22%2C%22USTypeKey%22%3A%22Brand_List%22%2C%22grade%22%3A%22%22%2C%22timestamp%22%3A%221539306000434.122070%22%2C%22network%22%3A%221%22%2C%22item_id%22%3A%22%22%2C%22model%22%3A%22Unknown%20iPhone%22%2C%22USIdKey%22%3A%22%22%2C%22UALevelRelation%22%3A%224%7C2%22%2C%22device%22%3A%220%22%2C%22trace_id%22%3A%22%22%7D%5D";
		Matcher matcher = pattern.matcher(ss);
		String rec_time="",ip="";
		if (matcher.find()) {
			//获取接收时间
			System.out.println(matcher.group(0).trim());
			System.out.println(matcher.group(1).trim());
			System.out.println(matcher.group(2).trim());

			System.out.println(URLDecoder.decode(matcher.group(3), "UTF-8"));
			}
	}
}

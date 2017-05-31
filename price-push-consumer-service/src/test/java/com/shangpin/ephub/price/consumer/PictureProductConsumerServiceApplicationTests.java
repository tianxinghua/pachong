package com.shangpin.ephub.price.consumer;

import com.shangpin.ephub.price.consumer.service.SupplierPriceService;
import com.shangpin.ephub.price.consumer.service.SupplierService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PictureProductConsumerServiceApplicationTests {
    @Autowired
	SupplierService supplierService;

    @Autowired
	SupplierPriceService supplierPriceService;

	@Test
	public void contextLoads() {
		try {
			supplierPriceService.getSupplierMsg("S0000394");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*public static void main(String[] args) throws Throwable {
		try {
			String ou = "https://s1.just-fashion.co.uk/JF/Images/ImgAPI.ashx?I=ITM_270_S25120_000_C_SAL002&C=270&U=636235443045622316&P=16871740055838213385";
			URL url = new URL(ou.replaceAll(" +", "%20"));
			URLConnection openConnection = url.openConnection();
			openConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
			openConnection.setConnectTimeout(45*60*1000);
			openConnection.setReadTimeout(45*60*1000);
			InputStream inputStream = openConnection.getInputStream();
			byte[] byteArray = IOUtils.toByteArray(inputStream);
			String base64 = new BASE64Encoder().encode(byteArray);
			System.out.println(base64);
			if (StringUtils.isBlank(base64)) {
				throw new RuntimeException("读取到的图片内容为空,无法获取图片");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println("发生异常。。。。。。。");
		}
		System.out.println("over.......");
	}*/
}

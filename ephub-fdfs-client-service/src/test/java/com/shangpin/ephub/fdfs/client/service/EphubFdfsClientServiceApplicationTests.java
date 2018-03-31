package com.shangpin.ephub.fdfs.client.service;

import com.shangpin.ephub.fdfs.client.service.upload.manager.FastDFSClientManager;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EphubFdfsClientServiceApplicationTests {

	@Autowired
	FastDFSClientManager fastDFSClientManager;
	@Test
	public void contextLoads() {


		File file=  new File("d:/20141114_221139.jpg");
		try {
			 byte[] picByte = IOUtils.toByteArray(new FileInputStream(file));
			fastDFSClientManager.uploadFile(picByte,"jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

package com.shangpin.ephub.product.business.ui.task.spuimport.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.shangpin.ephub.product.business.ui.task.common.util.FTPClientUtil;
import com.shangpin.ephub.response.HubResponse;

/**
 * <p>
 * Title:SupplierOrderService.java
 * </p>
 * <p>
 * Description: task
 * </p>
 * <p>
 * Company: www.shangpin.com
 * </p>
 * 
 * @author zhaogenchun
 * @date 2016年11月23日 下午4:06:52
 */
@SuppressWarnings("rawtypes")
@Service
public class HubImportTaskService {
	private static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	public HubResponse<byte[]> downResultFile(String resultFilePath) throws Exception{
		// TODO Auto-generated method stub
		InputStream in = FTPClientUtil.downFile(resultFilePath);
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream(); 
		byte[] buff = new byte[1024]; //buff用于存放循环读取的临时数据 
		int rc = 0; 
		while ((rc = in.read(buff, 0, 100)) > 0) { 
		swapStream.write(buff, 0, rc); 
		} 
		swapStream.flush();
		byte[] in_b = swapStream.toByteArray(); //in_b为转换之后的结果 
		return HubResponse.successResp(in_b);
	}

}

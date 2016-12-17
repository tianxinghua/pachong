package com.shangpin.ephub.product.business.task.spuimport.service;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shangpin.ephub.product.business.task.spuimport.dto.HubSpuImportTaskDto;
import com.sun.mail.iap.ByteArray;

/**
 * <p>
 * Title:SupplierOrderService.java
 * </p>
 * <p>
 * Description: 供货商订单流数据业务逻辑处理
 * </p>
 * <p>
 * Company: www.shangpin.com
 * </p>
 * 
 * @author zhaogenchun
 * @date 2016年11月23日 下午4:06:52
 */
@Service
public class TaskService {

	public boolean uploadFileAndSave(HubSpuImportTaskDto task) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		try {
			File dir = new File(task.getFileName());
			fos = new FileOutputStream(dir);
			bos = new BufferedOutputStream(fos);
			bos.write(task.getUploadfile());
			bos.flush();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}

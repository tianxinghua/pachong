package com.shangpin.asynchronous.task.consumer.productexport.type.common;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.shangpin.asynchronous.task.consumer.productexport.type.IExportService;
import com.shangpin.ephub.client.message.task.product.body.Task;
/**
 * <p>Title: CommonExporter</p>
 * <p>Description: 导出操作通用业务处理 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月7日 下午2:02:37
 *
 */
@Service
public abstract class CommonExporter implements IExportService {
	
	/**
	 * 获取表格头行
	 * @return
	 */
	public abstract String[] getExcelHeader();

	@Override
	public void productExportTask(Task message, Map<String, Object> headers) {
		//TODO 加载表头
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("产品信息");
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		String[] header = getExcelHeader();
		if(header != null && header.length > 0){
			
		}
	}
	
	
}

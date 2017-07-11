package com.shangpin.asynchronous.task.consumer.productexport.type.common;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.asynchronous.task.consumer.productexport.type.IExportService;
import com.shangpin.asynchronous.task.consumer.productexport.type.pending.ExportServiceImpl;
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.client.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title: CommonExporter</p>
 * <p>Description: 导出操作通用业务处理 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月7日 下午2:02:37
 *
 */
@Service
@Slf4j
public  abstract class CommonExporter<T,S> implements IExportService {
	
	private static final Integer PAGESIZE = 50;
	
	@Autowired
	private ExportServiceImpl exportServiceImpl;
	
	/**
	 * 获取表格头行
	 * @return
	 */
	public abstract String[] getExcelHeader();
	/**
	 * 获取模板
	 * @return
	 */
	public abstract String[] getExcelRowKeys();
	/**
	 * 从参数中获取总记录数
	 * @param t
	 * @return
	 */
	public abstract int getTotalSize(T t); 	
	/**
	 * 具体的业务处理方法，查找并返回写入Excel的数据集合
	 * @param pageIndex 分页开始页
	 * @param pageSize 分页每次查询的页数
	 * @param t 传入的参数
	 * @return 返回的实体集合要能直接写入Excel
	 */
	public abstract List<S> searchAndConvert(int pageIndex, Integer pageSize, T t);
	/**
	 * 从参数里获取创建人
	 * @param t
	 * @return
	 */
	public abstract String getCreateUser(T t);
	

	@Override
	public void productExportTask(Task message, Map<String, Object> headers) throws Exception {
		/**
		 * 加载表头
		 */
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("产品信息");
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle style = wb.createCellStyle();
		String[] header = getExcelHeader();
		if(header != null && header.length > 0){
			for (int i = 0; i < header.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(header[i]);
				cell.setCellStyle(style);
			}
		}
		/**
		 * 加载表数据
		 */
		String[] rowKeys = getExcelRowKeys();
		if(null == rowKeys || rowKeys.length == 0){
			throw new Exception("加载导出模板失败。");
		}
		T t = JsonUtil.deserialize(message.getData(), getClazz());
		int total = getTotalSize(t);
		if(total > 0){
			int pageCount = getPageCount(total, PAGESIZE);// 页数
			log.info("导出总页数：" + pageCount);
			int j = 0;
			for (int i = 1; i <= pageCount; i++) {
				List<S> list = searchAndConvert(i,PAGESIZE,t);
				if(CollectionUtils.isNotEmpty(list)){
					for(S s : list){
						try {
							j++;
							row = sheet.createRow(j);
							insertRow(row, s, rowKeys);
						} catch (Exception e) {
							log.error("insertRow异常：" + e.getMessage(), e);
							j--;
						}
					}
				}
			}
		}
		/**
		 * 最后一步：上传文件
		 */
		boolean result = exportServiceImpl.saveAndUploadExcel(message.getTaskNo(),getCreateUser(t), wb);
		log.info(message.getTaskNo()+"上传结果========="+result); 
	}
	
	/**
	 * 根据查找到的实体类以及导出模板生成excel文件
	 * @param row excel的行
	 * @param s 实体类
	 * @param rowKeys 导出模板
	 */
	private void insertRow(HSSFRow row, S s, String[] rowKeys) throws Exception {
		Class<?> sClazz = s.getClass();
		Method fieldSetMet = null;
		Object value = null;
		for(int i=0;i<rowKeys.length;i++){
			String methodName = parSetName(rowKeys[i]);
			fieldSetMet = sClazz.getMethod(methodName);
			value = fieldSetMet.invoke(s);
			row.createCell(i).setCellValue(null != value ? value.toString() : "");
		}
	}
	/**
	 * 获取总页数
	 * 
	 * @param totalSize
	 *            总计路数
	 * @param pagesize
	 *            每页记录数
	 * @return
	 */
	private Integer getPageCount(Integer totalSize, Integer pageSize) {
		if (totalSize % pageSize == 0) {
			return totalSize / pageSize;
		} else {
			return (totalSize / pageSize) + 1;
		}
	}
	
	/**
	 * 获取泛型的Class
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Class<T> getClazz(){ 
		return (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	/**
	 * 构造属性的get方法，比如传入name，返回getName
	 * 
	 * @param fieldName
	 * @return
	 */
	private String parSetName(String fieldName) {
		if (null == fieldName || "".equals(fieldName)) {
			return null;
		}
		int startIndex = 0;
		if (fieldName.charAt(0) == '_')
			startIndex = 1;
		return "get" + fieldName.substring(startIndex, startIndex + 1).toUpperCase()
				+ fieldName.substring(startIndex + 1);
	}
	
}

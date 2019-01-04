package com.shangpin.api.airshop.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangpin.api.airshop.dto.PurImport;
import com.shangpin.api.airshop.dto.UserInfo;
import com.shangpin.api.airshop.service.StockService;
import com.shangpin.api.airshop.util.Constants;
import com.shangpin.api.airshop.util.ExportExcelUtils;

@RestController
@RequestMapping("/stock")
@SessionAttributes(Constants.SESSION_USER)
public class StockController {
	private static Logger logger = LoggerFactory.getLogger(StockController.class);
	
	@Autowired
	private StockService stockService;
	
	
	/**供应商库存分页查询
	 * @param sopUserNo
	 * @param supplierSkuNo
	 * @param productModel
	 * @param skuNo
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public JSONObject getSkuStockPageByCondition(@RequestParam(value="supplierSkuNo",defaultValue="") String supplierSkuNo
			,@RequestParam(value="productModel",defaultValue="") String productModel
			,@RequestParam(value="skuNo",defaultValue="")String skuNo
			,@RequestParam(value="brandName",defaultValue="")String brandName
			,@RequestParam(value="pageIndex",defaultValue="1")int pageIndex
			,@RequestParam(value="pageSize",defaultValue="30")int pageSize
			,@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo){
		
			return stockService.getSkuStockPageByCondition(userInfo.getSopUserNo(), supplierSkuNo, productModel, skuNo,brandName, pageIndex, pageSize);
	}
	
	/**导出库存数据
	 * @param supplierSkuNo
	 * @param productModel
	 * @param skuNo
	 * @param userInfo
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/listExport")
	public void getSkuStockPageByConditionExport (@RequestParam(value="supplierSkuNo",defaultValue="") String supplierSkuNo
			,@RequestParam(value="productModel",defaultValue="") String productModel
			,@RequestParam(value="skuNo",defaultValue="")String skuNo
			,@RequestParam(value="brandName",defaultValue="")String brandName
			,@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo
			,HttpServletResponse response) throws Exception{
		
		List<HashMap<String, Object>> result2 = new ArrayList<HashMap<String, Object>>();
		JSONObject jsonResult= stockService.getSkuStockPageByConditionExport(userInfo.getSopUserNo(), supplierSkuNo, productModel, skuNo,brandName);
		response.setContentType("application/x-download");// 设置为下载application/x-download
		String fileName = "Stock Correction";
		response.addHeader("Content-Disposition","attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xls");
		OutputStream out = response.getOutputStream();
		if (jsonResult.getString("code").equals("0")&&jsonResult.getJSONObject("content").getJSONArray("list").size()>0) {
			JSONArray list=jsonResult.getJSONObject("content").getJSONArray("list");
			for (int i = 0; i < list.size(); i++) {
				HashMap<String, Object> item=new HashMap<String, Object>();
				item.put("SKUID", list.getJSONObject(i).getString("SopSkuNo"));
				item.put("Brand", list.getJSONObject(i).getString("BrandName"));
				item.put("Supplier SKU", list.getJSONObject(i).getString("SupplierSkuNo"));
				item.put("Shangpin SKU", list.getJSONObject(i).getString("SkuNo"));
				item.put("Item Code", list.getJSONObject(i).getString("ProductModel"));
				item.put("Item Name", list.getJSONObject(i).getString("productName")+" "+list.getJSONObject(i).getString("color")+" "+list.getJSONObject(i).getString("size"));
				item.put("BarCode", list.getJSONObject(i).getString("BarCode"));
				item.put("Category", list.getJSONObject(i).getString("CategoryNameEn"));
				item.put("Available Qty", list.getJSONObject(i).getString("RealInventoryQuantity"));
				item.put("Lock Qty", list.getJSONObject(i).getString("LockInventoryQuantity"));
				result2.add(item);
			}
			String[] headers = {"SKUID","Brand","Supplier SKU","Shangpin SKU","Item Code","Item Name","BarCode","Category","Available Qty","Lock Qty"};
			String[] columns = {"SKUID", "Brand","Supplier SKU","Shangpin SKU","Item Code","Item Name","BarCode","Category","Available Qty","Lock Qty"};
			ExportExcelUtils.exportExcel(fileName, headers, columns, result2, out, "");
			out.close();
		}else{
			ExportExcelUtils.exportExcel(fileName, new String[0], new String[0], result2, out, "");
		}
		
	}
	
	/**单条修改库存
	 * @param userInfo
	 * @param sopSkuNo
	 * @param qty
	 * @return
	 */
	@RequestMapping(value="/updateSkuStock")
	public JSONObject updateSkuStock(@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo
			,String sopSkuNo,String qty){
		return stockService.updateSkuStock(userInfo.getSopUserNo(), sopSkuNo, qty);
	}
	
	/**；批量导入文件修改库存
	 * @param skuStockfile
	 * @param userInfo
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/importSkuStock")
	public JSONObject importSkuStock(@RequestParam(value = "stockFile", required = true) MultipartFile stockFile,
			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo,
			HttpServletResponse response) throws Exception{
		
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(stockFile.getInputStream());
		// 循环工作表Sheet
		JSONArray importList = new JSONArray();
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			// 循环行Row
			
			List<PurImport> pList = new ArrayList<>();
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow == null) {
					continue;
				}
				if (hssfRow.getCell(8)==null) {
					continue;
				}else {
					if (String.valueOf(hssfRow.getCell(8))==null) {
						continue;
					}
				}
				JSONObject item = new JSONObject();
				item.put("SopUserNo", userInfo.getSopUserNo());
				item.put("SopSkuNo", String.valueOf(hssfRow.getCell(0)));
				if (hssfRow.getCell(8).toString().indexOf('.')>0) {
					item.put("Qty",hssfRow.getCell(8).toString().substring(0,hssfRow.getCell(8).toString().indexOf('.')) );
				}else {
					item.put("Qty",hssfRow.getCell(8).toString() );
				}
				
				
				importList.add(item);
			}
		}
		
		if (importList.size()>0) {
			JSONObject result= stockService.updateSkuStockImport(importList);
			if (result.getBooleanValue("IsSuccess")) {
				return JSONObject.parseObject("{code:\"0\",msg:\"\",content:{}}");
			}else {
				return JSONObject.parseObject("{code:\"1\",msg:\"Beware, you've imported invalid data. Import failed!\",content:{}}");
			}
		}else {
			return JSONObject.parseObject("{code:\"1\",msg:\"no data!\",content:{}}");
		}
		
	}
	
	
	
	
}

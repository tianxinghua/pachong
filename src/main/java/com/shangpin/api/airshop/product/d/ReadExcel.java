package com.shangpin.api.airshop.product.d;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shangpin.common.utils.StringUtil;

public class ReadExcel {

	/**
	 * Read the Excel 2010
	 * 
	 * @param path
	 *            the path of the excel file
	 * @return
	 * @throws Exception 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> readXlsx(InputStream is) throws Exception {
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		List<T> list = new ArrayList<T>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);

				if (xssfRow != null) {
					boolean flag = true;
					try {
						ProductDTO item = new ProductDTO();
						if (xssfRow.getCell(0) != null) {
							item.setCategoryName(xssfRow.getCell(0).toString());
						}else{
							flag = false;
						}
						if (xssfRow.getCell(1) != null) {
							item.setBrandName(xssfRow.getCell(1).toString());
						}else{
							flag = false;
						}

						if (xssfRow.getCell(2) != null) {
							xssfRow.getCell(2).setCellType(
									Cell.CELL_TYPE_STRING);
							item.setProductCode(xssfRow.getCell(2).toString());
						}else{
							flag = false;
						}

						if (xssfRow.getCell(4) != null) {
							item.setSeasonName(((int) Double
									.parseDouble(xssfRow.getCell(3).toString()))
									+ "-" + xssfRow.getCell(4).toString());
						}
						if (xssfRow.getCell(5) != null) {
							item.setCategoryGender(xssfRow.getCell(5)
									.toString());
						}

						if (xssfRow.getCell(6) != null) {
							xssfRow.getCell(6).setCellType(
									Cell.CELL_TYPE_STRING);
							item.setSkuId(xssfRow.getCell(6).toString());
						}else{
							flag = false;
						}
						if (xssfRow.getCell(7) != null) {
							item.setProductName(xssfRow.getCell(7).toString());
						}

						if (xssfRow.getCell(8) != null) {
							xssfRow.getCell(8).setCellType(
									Cell.CELL_TYPE_STRING);
							item.setBarcode(xssfRow.getCell(8).toString());
						}
						if (xssfRow.getCell(9) != null) {
							item.setColor(xssfRow.getCell(9).toString());
						}else{
							flag = false;
						}

						if (xssfRow.getCell(11) != null) {
							xssfRow.getCell(11).setCellType(
									Cell.CELL_TYPE_STRING);
							item.setSize(xssfRow.getCell(10).toString() + "-"
									+ xssfRow.getCell(11).toString());
						}else{
							flag = false;
						}
						if (xssfRow.getCell(12) != null) {
							item.setMaterial(xssfRow.getCell(12).toString());
						}
						if (xssfRow.getCell(13) != null) {
							item.setProductOrigin(xssfRow.getCell(13)
									.toString());
						}
						if (xssfRow.getCell(15) != null) {
							item.setMarketPrice(new BigDecimal(xssfRow.getCell(
									15).toString()));
						}

						if (xssfRow.getCell(14) != null&&StringUtils.isNotBlank(xssfRow.getCell(14).toString())) {
							item.setStock((int) Double.parseDouble(xssfRow
									.getCell(14).toString()));
						}else{
							item.setStock(1);
						}

						if (xssfRow.getCell(16) != null&&StringUtils.isNotBlank(xssfRow.getCell(16).toString())) {
							String s = xssfRow
									.getCell(16).toString();
							item.setSupplierPrice(new BigDecimal(xssfRow
									.getCell(16).toString()));
						}

						if (xssfRow.getCell(17) != null) {
							item.setSaleCurrency(xssfRow.getCell(17).toString());
						}
						StringBuffer str = new StringBuffer();
						for (int i = 18; i < 28; i++) {
							if (xssfRow.getCell(i) == null) {
								break;
							} else {
								str.append("|" + xssfRow.getCell(i));
							}
						}
						if (str.length() > 0) {
							item.setSkuPicture(str.toString().substring(1));
						}
						if (flag) {
							item.setSpuId(item.getProductCode()
									+ item.getColor());
							list.add((T) item);
						}
					} catch (Exception ex) {
						ex.getStackTrace();
					}
				}
			}
		}
		return list;
	}

	/**
	 * Read the Excel 2003-2007
	 * 
	 * @param path
	 *            the path of the Excel
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> readXls(InputStream is) throws Exception {
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		List<T> list = new ArrayList<T>();
		// Read the Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				HSSFRow xssfRow = hssfSheet.getRow(rowNum);
				if (xssfRow != null) {
					boolean flag = true;
					try {
						ProductDTO item = new ProductDTO();
						if (xssfRow.getCell(0) != null) {
							item.setCategoryName(xssfRow.getCell(0).toString());
						}else{
							flag = false;
						}
						if (xssfRow.getCell(1) != null) {
							item.setBrandName(xssfRow.getCell(1).toString());
						}else{
							flag = false;
						}

						if (xssfRow.getCell(2) != null) {
							xssfRow.getCell(2).setCellType(
									Cell.CELL_TYPE_STRING);
							item.setProductCode(xssfRow.getCell(2).toString());
						}else{
							flag = false;
						}

						if (xssfRow.getCell(4) != null) {
							item.setSeasonName(((int) Double
									.parseDouble(xssfRow.getCell(3).toString()))
									+ "-" + xssfRow.getCell(4).toString());
						}
						if (xssfRow.getCell(5) != null) {
							item.setCategoryGender(xssfRow.getCell(5)
									.toString());
						}

						if (xssfRow.getCell(6) != null) {
							xssfRow.getCell(6).setCellType(
									Cell.CELL_TYPE_STRING);
							item.setSkuId(xssfRow.getCell(6).toString());
						}else{
							flag = false;
						}
						if (xssfRow.getCell(7) != null) {
							item.setProductName(xssfRow.getCell(7).toString());
						}

						if (xssfRow.getCell(8) != null) {
							xssfRow.getCell(8).setCellType(
									Cell.CELL_TYPE_STRING);
							item.setBarcode(xssfRow.getCell(8).toString());
						}
						if (xssfRow.getCell(9) != null) {
							item.setColor(xssfRow.getCell(9).toString());
						}else{
							flag = false;
						}

						if (xssfRow.getCell(11) != null) {
							xssfRow.getCell(11).setCellType(
									Cell.CELL_TYPE_STRING);
							item.setSize(xssfRow.getCell(10).toString() + "-"
									+ xssfRow.getCell(11).toString());
						}else{
							flag = false;
						}
						if (xssfRow.getCell(12) != null) {
							item.setMaterial(xssfRow.getCell(12).toString());
						}
						if (xssfRow.getCell(13) != null) {
							item.setProductOrigin(xssfRow.getCell(13)
									.toString());
						}
						if (xssfRow.getCell(15) != null) {
							item.setMarketPrice(new BigDecimal(xssfRow.getCell(
									15).toString()));
						}

						if (xssfRow.getCell(14) != null&&StringUtils.isNotBlank(xssfRow.getCell(14).toString())) {
							item.setStock((int) Double.parseDouble(xssfRow
									.getCell(14).toString()));
						}else{
							item.setStock(1);
						}

						if (xssfRow.getCell(16) != null&&StringUtils.isNotBlank(xssfRow.getCell(16).toString())) {
							item.setSupplierPrice(new BigDecimal(xssfRow
									.getCell(16).toString()));
						}

						if (xssfRow.getCell(17) != null) {
							item.setSaleCurrency(xssfRow.getCell(17).toString());
						}
						StringBuffer str = new StringBuffer();
						for (int i = 18; i < 28; i++) {
							if (xssfRow.getCell(i) == null) {
								break;
							} else {
								str.append("|" + xssfRow.getCell(i));
							}
						}
						if (str.length() > 0) {
							item.setSkuPicture(str.toString().substring(1));
						}
						if (flag) {
							item.setSpuId(item.getProductCode()
									+ item.getColor());
							list.add((T) item);
						}
					} catch (Exception ex) {
						// throw new
						// ExceptionMessage("the "+(rowNum+1)+" lines ");
					}
				}
			}
		}
		return list;
	}
}
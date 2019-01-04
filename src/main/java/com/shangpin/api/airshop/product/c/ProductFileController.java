package com.shangpin.api.airshop.product.c;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shangpin.api.airshop.dto.UserInfo;
import com.shangpin.api.airshop.product.d.ProductDTO;
import com.shangpin.api.airshop.product.d.ReadCSV;
import com.shangpin.api.airshop.product.d.ReadExcel;
import com.shangpin.api.airshop.product.d.ResponseDTO;
import com.shangpin.api.airshop.product.m.ProductManager;
import com.shangpin.api.airshop.product.o.ProductResponse;
import com.shangpin.api.airshop.util.Constants;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:ProductFileController </p>
 * <p>Description: 商品相关文件上传下载控制器</p>
 * <p>Company: shangpin</p> 
 * @author : yanxiaobin
 * @date :2016年4月22日 下午5:36:45
 */
@RestController
@Slf4j
@RequestMapping("/product")
public class ProductFileController{
	/**
	 * 提供模板下载
	 * @param req
	 * @param res
	 */
	@RequestMapping("/download")
	public void  download(HttpServletRequest req,HttpServletResponse res) {
		String fileName = "ShangpinTemplate.xlsx";
		InputStream stream = null;
		try {
			 stream = this.getClass().getClassLoader().getResourceAsStream(fileName);
			String mimeType = req.getServletContext().getMimeType(fileName);
			res.setContentType(mimeType);
			res.setHeader("Content-Disposition", "attachment;filename="+fileName);
			ServletOutputStream outputStream = res.getOutputStream();
			IOUtils.copy(stream, outputStream);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.toString());
		}finally{
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException("When close a stream occur Exception",e);
				}
			}
		}
		
	}
	@Autowired
	private ProductManager pm;
	/**
	 * 文件上传
	 */
	@RequestMapping(value = "/upload")
	public ProductResponse inventoryImport(MultipartFile uploadfile,HttpSession session) throws Exception {
		
		ProductResponse pr = new ProductResponse();
		InputStream stream = null;
		try {
			if (uploadfile == null || uploadfile.getInputStream() == null || uploadfile.getOriginalFilename() == null) {
				pr.setCode(2);
				pr.setMsg("File is null or filename is null or stream is null !");
				return pr;
			} 
			String filename = uploadfile.getOriginalFilename();
			stream = uploadfile.getInputStream();
			List<ProductDTO> products = new ArrayList<>();
			try {
				if (filename.endsWith(".csv")) {
					products = ReadCSV.readLocalCSV(stream);
				}else if (filename.endsWith(".xlsx")) {
					products = ReadExcel.readXlsx(stream);
				}else if(filename.endsWith(".xls")){
					products = ReadExcel.readXls(stream);
				}else{
					pr.setCode(2);
					pr.setMsg("File format is not correct, please upload csv or xlsx format file !");
				}				
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("parse CSV or XLSX occur exception !", e);
			}
			log.info("product quantity :{}",products.size());
			 UserInfo user = (UserInfo) session.getAttribute(Constants.SESSION_USER);
			  ResponseDTO responseDTO = pm.batchSaveProductHub(products,user.getSopUserNo());
			 System.out.println("responseDTO====>>>"+responseDTO);
			  if (responseDTO!= null && responseDTO.getResponseCode() == 1) {
				  pr.setCode(0);
				  pr.setMsg("OK");
			   }else{
				  pr.setCode(2);
			   }
		} catch (IOException e) {
			  pr.setCode(2);
			  pr.setMsg("Upload failure , please try it later !");
			  e.printStackTrace();
			log.error(e.toString());
		}finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return pr;
	}

}

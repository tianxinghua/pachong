package com.shangpin.api.airshop.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.barcodelib.barcode.Linear;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.service.TMSService;

@RestController
@RequestMapping("/wayprint")
public class PrintController {
	private static Logger logger = LoggerFactory
			.getLogger(PrintController.class);
	@Autowired
	private TMSService tmsService;

	/**
	 * TMS打印面单服务
	 * */
	@RequestMapping(value = "/print")
	public void print(
			@RequestParam(value = "path", defaultValue = "") String path,
			HttpServletResponse response) {

		DefaultHttpClient httpClient = new DefaultHttpClient();
		OutputStream out = null;
		InputStream in = null;
		try {
			response.setContentType("application/octet-stream");
			response.setHeader("content-disposition", "attachment;filename="
					+ URLEncoder.encode("Thermal.txt", "UTF-8"));
			HttpGet httpGet = new HttpGet(ApiServiceUrlConfig.getTMS()
					+ "/Delivery/Print/GetPrintOverseasData?path=" + path);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			in = entity.getContent();

			out = new BufferedOutputStream(response.getOutputStream());
			byte[] data = new byte[1024];
			int len = 0;
			while (-1 != (len = in.read(data, 0, data.length))) {
				out.write(data, 0, len);
			}

		} catch (Exception s) {

		} finally {

			try {
				if (null != out)
					out.close();
				if (null != in)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * TMS打印价签服务
	 * */
	@RequestMapping(value = "/printPID")
	public void printPID(
			@RequestParam(value = "supplierOrderNo", defaultValue = "") String supplierOrderNo,
			HttpServletResponse response) {

		DefaultHttpClient httpClient = new DefaultHttpClient();
		OutputStream out = null;
		InputStream in = null;
		try {
			out = response.getOutputStream();
			response.setContentType("application/octet-stream");
			HttpGet httpGet = new HttpGet(
					ApiServiceUrlConfig.getTMS()
							+ "/Delivery/OrderManage/GetPrintDataBySupplierOrderNo?opType=0&supplierOrderNo="
							+ supplierOrderNo);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			in = entity.getContent();
			try {
				int byteread = 0;
				byte[] buffer = new byte[1204];
				while ((byteread = in.read(buffer)) != -1) {
					out.write(buffer, 0, byteread);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			out.flush();
			out.close();
		} catch (Exception s) {

		}
	}

	static String path = "/data/www/8082/";
//	static String path = "D://";
//	public static void main(String[] args) {
//		String spSkuNo = "30380410002";
//	String supplierOrderNo = "2017011038735";
//	String pid = "PID3004009966";
//	new PrintController().printPIDAndSupplierOrderNo(supplierOrderNo,pid,spSkuNo,"1",null);
//	}
//	 static String path = "D://";
	/**
	 * TMS打印价签服务
	 * */
	@RequestMapping(value = "/printPIDAndSupplierOrderNo")
	public void printPIDAndSupplierOrderNo(
			@RequestParam(value = "supplierOrderNo", defaultValue = "") String supplierOrderNo,
			@RequestParam(value = "pid", defaultValue = "") String pid,
			@RequestParam(value = "spSkuNo", defaultValue = "") String spSkuNo,
			@RequestParam(value = "type", defaultValue = "") String type,
			HttpServletResponse response) {
	
		logger.info("打印价签参数supplierOrderNo：" + supplierOrderNo);
		logger.info("打印价签参数spSkuNo：" + spSkuNo);
		logger.info("打印价签参数pid：" + pid);
		logger.info("打印价签参数type：" + type);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		OutputStream out = null;
		try {
			BufferedImage target = new BufferedImage(544, 1182,
					BufferedImage.TYPE_BYTE_GRAY);

			BufferedImage logo = null;
			// 2代表一般贸易
			if (type != null && "2".equals(type)) {
				logo = ImageIO.read(new File(path + "G.jpg"));
			}
			if (type != null && "1".equals(type)) {
				logo = ImageIO.read(new File(path + "B.jpg"));
			}
			if (type != null && "3".equals(type)) {
				logo = ImageIO.read(new File(path + "P.jpg"));
			}
			out = response.getOutputStream();
			response.setContentType("application/octet-stream");
//			 FileOutputStream ss = new FileOutputStream(new
//			 File("D://cc.jpg"));
//			 ImageIO.write(modifyImagetogeter(logo, barcode(supplierOrderNo),barcode(spSkuNo),
//			 barcode(pid),
//			 target), "jpg",ss);
//			 ss.close();
			ImageIO.write(
					modifyImagetogeter(logo, barcode(supplierOrderNo),barcode(pid), target,spSkuNo), "jpg", out);
			out.flush();
			out.close();
		} catch (Exception s) {

		}
	}
		@RequestMapping(value = "/printDeliveryNo")
		public void printDelierv(
				@RequestParam(value = "deliveryNo", defaultValue = "") String deliveryNo,
				HttpServletResponse response) {
			
			logger.info("打印价签参数deliveryNo："+deliveryNo);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			OutputStream out = null;
			InputStream in = null;
			try {
				BufferedImage target = new BufferedImage(544, 1182,
						BufferedImage.TYPE_BYTE_GRAY);
				
				BufferedImage logo = null;
				//2代表一般贸易
					logo = ImageIO.read(new File(path+"P.jpg"));	
				out = response.getOutputStream();
				response.setContentType("application/octet-stream");
				ImageIO.write(modifyImagetogeter(logo, barcode(deliveryNo),null,
							target,null), "jpg", out);
				out.flush();
				out.close();
			} catch (Exception s) {

			}
		}
	
	private Font font = new Font("Arial", 0, 40);// 添加字体的属性设置
	private BufferedImage barcode(String Data) throws Exception {
		Linear barcode = new Linear();
		barcode.setType(Linear.CODE128);
		barcode.setData(Data);
		barcode.setUOM(Linear.CODE128);
		barcode.setX(4f);
		barcode.setY(180f);
		barcode.setLeftMargin(0f);
		barcode.setRightMargin(30f);
		barcode.setTopMargin(10f);
		barcode.setBottomMargin(0f);
		barcode.setShowText(true);
		barcode.setTextFont(new Font("Arial", 0, 50));
		barcode.setRotate(Linear.ANGLE_0);
		return barcode.renderBarcode();
	}

	public BufferedImage modifyImage(BufferedImage img, Object content, int x,
			int y) {

		try {
			Graphics2D g = img.createGraphics();
			g.setBackground(Color.WHITE);
			g.setColor(Color.BLACK);//设置字体颜色
			if (this.font != null)
				g.setFont(this.font);
			// 验证输出位置的纵坐标和横坐标
			if (content != null) {
				g.drawString(content.toString(), x,y);
			}
			g.dispose();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return img;
	}
	public BufferedImage modifyImagetogeter(BufferedImage logo,
			BufferedImage supplierOrderNo,BufferedImage pid, BufferedImage d,String spSkuNo) {
		Graphics2D g = null;
		try {
			g = d.createGraphics();
//			g.setColor(Color.white);
			g.fillRect(0, 0, d.getWidth(), d.getHeight());
//			g.setColor(Color.BLACK);// 设置字体颜色
			// 150代表左间距 100代表上边距 300代表宽度 300代表高度
			g.drawImage(logo, 150, 100, 300, 300, null);
			// 602 和 244代表条形码的宽度和高度，此值已经是打印纸最大限度
			g.drawImage(supplierOrderNo, 0, 550, 600, 244, null);
			//左边距、上边距、宽带、高度
		 	g.drawImage(pid, 0, 950, 600, 200, null); 	
		 	if(spSkuNo!=null){
		 		modifyImage(d,"SKU:"+spSkuNo,100,950);
		 	}
		
			g.dispose();
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return d;
	}

}

package com.shangpin.api.airshop.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.barcodelib.barcode.Linear;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.dto.TMSTrack;
import com.shangpin.api.airshop.dto.UserInfo;
import com.shangpin.api.airshop.service.TMSService;
import com.shangpin.api.airshop.util.Constants;
import com.shangpin.api.airshop.util.ExportExcelUtils;
import com.shangpin.api.airshop.util.HttpUtil45;
import com.shangpin.api.airshop.util.OutTimeConfig;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

@RestController
@RequestMapping("/waybill")
@SessionAttributes(Constants.SESSION_USER)
public class TMSController {
	private static Logger logger = LoggerFactory.getLogger(TMSController.class);
	@Autowired
	private TMSService tmsService;

	/**
	 * TMS出入库操作
	 *
	 * @param sopUserNo
	 * @param supplierSkuNo
	 * @param productModel
	 * @param skuNo
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/inAndOutBound")
	@ResponseBody
	public String saveInAndOutBound(
			@RequestParam(value = "supplierOrderNo", defaultValue = "") String supplierOrderNo,
			@RequestParam(value = "purchase", defaultValue = "") String purchase,
			@RequestParam(value = "opUser", defaultValue = "") String opUser,
			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {

		return tmsService.saveInAndOutBound(userInfo.getSopUserNo(),
				supplierOrderNo, purchase, opUser);
	}

	/**
	 * TMS创建批次操作
	 *
	 * @return
	 */
	@RequestMapping(value = "/createTrack")
	@ResponseBody
	public String createTrack(
			@RequestParam(value = "supplierOrders", defaultValue = "") String supplierOrders,
			@RequestParam(value = "sopPurchaseOrderNo", defaultValue = "") String sopPurchaseOrderNo,
			@RequestParam(value = "opUser", defaultValue = "") String opUser,
			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {

		return tmsService.createTrack(userInfo.getSupplierNo(),
				userInfo.getSopUserNo(), supplierOrders, sopPurchaseOrderNo,
				opUser);
	}

	/**
	 * TMS打印面单服务
	 * */
	// @RequestMapping(value="/print")
	public void print(
			@RequestParam(value = "path", defaultValue = "") String path,
			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo,
			HttpServletResponse response) {

		DefaultHttpClient httpClient = new DefaultHttpClient();
		OutputStream out = null;
		InputStream in = null;
		try {
			response.setContentType("application/octet-stream");
			response.setHeader("content-disposition", "attachment;filename="
					+ URLEncoder.encode("Thermal.txt", "UTF-8"));
			out = response.getOutputStream();
			// HttpGet httpGet = new
			// HttpGet("http://tms0.shangpin.com/Delivery/Print/GetPrintOverseasData?path=/FedExShipmentLabels/15/02/03/TCI201502030006_O20150203046753_C794629626325.txt");
			HttpGet httpGet = new HttpGet(ApiServiceUrlConfig.getTMS()
					+ "/Delivery/Print/GetPrintOverseasData?path=" + path);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			in = entity.getContent();
			long length = entity.getContentLength();
			if (length <= 0) {
				logger.info("下载文件不存在！");
			}
			byte[] b = new byte[10 * 1024];
			while (in.read(b, 0, 10240) != -1) {
				out.write(b, 0, 10240);
			}
			out.close();
		} catch (Exception s) {

		}

	}

	/**
	 * TMS打印价签服务
	 * */
	// @RequestMapping(value="/printPID")
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

	// static String path = "D://";
	/**
	 * TMS打印价签服务
	 * */
	@RequestMapping(value = "/printPIDAndSupplierOrderNo")
	public void printPIDAndSupplierOrderNo(
			@RequestParam(value = "supplierOrderNo", defaultValue = "") String supplierOrderNo,
			@RequestParam(value = "pid", defaultValue = "") String pid,
			@RequestParam(value = "type", defaultValue = "") String type,
			HttpServletResponse response) {

		logger.info("打印价签参数supplierOrderNo：" + supplierOrderNo);
		logger.info("打印价签参数pid：" + pid);
		logger.info("打印价签参数type：" + type);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		OutputStream out = null;
		InputStream in = null;
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
			out = response.getOutputStream();
			response.setContentType("application/octet-stream");
			// FileOutputStream ss = new FileOutputStream(new
			// File("D://cc.jpg"));
			// ImageIO.write(modifyImagetogeter(logo, barcode2(supplierOrderNo),
			// barcode1(pid),
			// target), "jpg",ss);
			// ss.close();
			ImageIO.write(
					modifyImagetogeter(logo, barcode(supplierOrderNo),
							barcode(pid), target), "jpg", out);
			out.flush();
			out.close();
		} catch (Exception s) {

		}
	}

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

	public BufferedImage modifyImagetogeter(BufferedImage logo,
			BufferedImage supplierOrderNo, BufferedImage pid, BufferedImage d) {
		Graphics2D g = null;
		try {

			int w = supplierOrderNo.getWidth();
			int h = supplierOrderNo.getHeight();
			g = d.createGraphics();
			g.setColor(Color.white);
			g.fillRect(0, 0, d.getWidth(), d.getHeight());
			g.setColor(Color.BLACK);// 设置字体颜色
			// 150代表左间距 100代表上边距 300代表宽度 300代表高度
			g.drawImage(logo, 150, 100, 300, 300, null);
			// 602 和 244代表条形码的宽度和高度，此值已经是打印纸最大限度
			g.drawImage(supplierOrderNo, 0, 550, 602, 244, null);
			// g.drawImage(pid, 0, 900, 602, 244, null);
			g.dispose();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return d;
	}

	/**
	 * TMS查询批次列表操作
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public String getTrackList(TMSTrack tmsTrack,
			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {

		return tmsService.getTrackList(tmsTrack, userInfo.getSupplierNo());
	}

	/**
	 * TMS查询批次明细
	 * */
	@RequestMapping(value = "/{taskBatchNo}/{importType}/detail", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getTrackDetail(
			@PathVariable("taskBatchNo") String taskBatchNo,
			@PathVariable("importType") String importType,
			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {

		return tmsService.getTrackDetail(taskBatchNo, userInfo.getSopUserNo(),importType);
	}

	// /**
	// * TMS查询批次明细
	// * */
	// @RequestMapping(value = "/{taskBatchNo}/detailExport", produces =
	// "text/html;charset=UTF-8")
	// @ResponseBody
	// public String getTrackDetail(@PathVariable("taskBatchNo") String
	// taskBatchNo,@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo){
	//
	// return tmsService.getTrackDetail(taskBatchNo,userInfo.getSopUserNo());
	// }

	/**
	 * TMS查询发票信息
	 * */
	@RequestMapping(value = "/{taskBatch}/logisticData", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getLogisticData(
			@PathVariable("taskBatch") String taskBatchNo,
			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {

		return tmsService.getLogisticData(taskBatchNo,
				userInfo.getAccountName());
	}

	/**
	 * TMS查询发票信息
	 * */
	@RequestMapping(value = "/{taskBatch}/shipingCartonsData", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getShipingCartonsData(
			@PathVariable("taskBatch") String taskBatchNo,
			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {

		return tmsService.getShipingCartonsData(taskBatchNo,
				userInfo.getAccountName());
	}


	@RequestMapping(value = "/PrintZonyzoomItalyInvoice", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String PrintZonyzoomItalyInvoice()  {

		try{
			Configuration cfg = new Configuration();
			StringWriter writer = new StringWriter();
			Map<String,Object> context = new HashMap<String,Object>();
	        String path = this.getClass().getClassLoader().getResource("fapiaoTemplate/").getPath();
	        cfg.setObjectWrapper(new DefaultObjectWrapper());
	        cfg.setDirectoryForTemplateLoading(new File(path));
	        Template template = cfg.getTemplate("monnier.ftl");
	        template.process(context, writer);
	        return writer.toString();
		}catch(Exception e){

		}
		return null;
	}

	  public static String txt2String(File file){
	        StringBuilder result = new StringBuilder();
	        try{
	            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
	            String s = null;
	            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
	                result.append(System.lineSeparator()+s);
	            }
	            br.close();
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	        return result.toString();
	    }
	/**
	 * TMS查询发票信息
	 * */
	@RequestMapping(value = "/ctaskBatchExport/{taskBatchNo}/{importType}/{masterTrackNo}", produces = "text/html;charset=UTF-8")
	public String ctaskBatchExport(
			@PathVariable("taskBatchNo") String taskBatchNo,
			@PathVariable("importType") String importType,
			@PathVariable("masterTrackNo") String masterTrackNo,
			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo,
			HttpServletResponse response) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String type = null;
		response.setContentType("application/x-download");// 设置为下载application/x-download
		try {

			if ("1".equals(importType)) {
				type = " cross border";
			}
			if ("2".equals(importType)) {
				type = " general trade";
			}
			String json = "{\"TaskNo\":\"" + taskBatchNo + "\"}";
			logger.info(taskBatchNo + "查询批次明细传参：" + json);
			String result = HttpUtil45.operateData("post", "json",
					ApiServiceUrlConfig.getSearchTaskOrderDetail(),
					new OutTimeConfig(1000 * 10 * 6, 1000 * 10 * 6,
							1000 * 10 * 6), null, json, null, null);
			logger.info(taskBatchNo + "查询批次明细返回结果：" + result);
			if (result != null) {
				JSONObject jsonObject = JSON.parseObject(result);
				Object s = jsonObject.get("TaskOrderDetailList");
				if (s != null) {
					JSONArray array = JSONArray.parseArray(s.toString());
					List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
					Iterator<Object> it = array.iterator();
					while (it.hasNext()) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						JSONObject ob = (JSONObject) it.next();
						map.put("FedExInternationalTrackNo",
								ob.getString("FedExInternationalTrackNo"));
						map.put("FedExDomesticTrackNo",
								ob.getString("FedExDomesticTrackNo"));
						map.put("SkuName", ob.getString("SkuName"));
						map.put("RecordPrice", ob.getString("RecordPrice"));
						map.put("Qty", ob.getString("Qty"));
						map.put("Weight", ob.getString("Weight"));
						map.put("ReceivingUser", ob.getString("ReceivingUser"));
						map.put("StartSendArea", ob.getString("StartSendArea"));
						map.put("ReceivingUserIDCard",
								ob.getString("ReceivingUserIDCard"));
						map.put("ReceivingMobile",
								ob.getString("ReceivingMobile"));
						map.put("ReceivingAddress",
								ob.getString("ReceivingAddress"));
						map.put("ReceivingPostCode",
								ob.getString("ReceivingPostCode"));
						map.put("OrderNo", ob.getString("OrderNo"));
						map.put("OutBoundTime", ob.getString("OutBoundTime"));
						map.put("SkuNo", ob.getString("SkuNo"));
						map.put("ReceivingCompanyName",
								ob.getString("ReceivingCompanyName"));
						map.put("ReceivingMobile1",
								ob.getString("ReceivingMobile1"));
						map.put("ReceivingCity", ob.getString("ReceivingCity"));
						map.put("ReceivingArea", ob.getString("ReceivingArea"));
						map.put("SpecialProcess",
								ob.getString("SpecialProcess"));
						map.put("AffirmsValue", ob.getString("AffirmsValue"));
						listMap.add(map);
					}

					String[] headers = { "FDX国际子运单号", "FDX国内运单号", "品名",
							"货物申明价值", "件数", "重量（Kg）", "收件人中文名", "货物始发地",
							"证件号码", "客户电话", "客户地址", "邮编", "订单号", "发货日期",
							"商品货号", "SKU编码", "收件人公司名称", "收件人手机", "收件人城市",
							"收件人区县", "特别处理", "价值(RMB)" };
					String[] columns = { "FedExInternationalTrackNo",
							"FedExDomesticTrackNo", "SkuName", "RecordPrice",
							"Qty", "Weight", "ReceivingUser", "StartSendArea",
							"ReceivingUserIDCard", "ReceivingMobile",
							"ReceivingAddress", "ReceivingPostCode", "OrderNo",
							"OutBoundTime", "SupplierSkuNo", "SkuNo",
							"ReceivingCompanyName", "ReceivingMobile1",
							"ReceivingCity", "ReceivingArea", "SpecialProcess",
							"AffirmsValue" };

					String fileName = "shipping notice"
							+ format.format(new Date()) + "-" + masterTrackNo
							+ type;
					response.addHeader(
							"Content-Disposition",
							"attachment;filename="
									+ new String(fileName.getBytes("gb2312"),
											"ISO8859-1") + ".xls");

					OutputStream out = response.getOutputStream();

					ExportExcelUtils.exportExcel1(fileName, headers, columns,
							listMap, out, "");
					out.close();
				}
			}

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;

	}

}

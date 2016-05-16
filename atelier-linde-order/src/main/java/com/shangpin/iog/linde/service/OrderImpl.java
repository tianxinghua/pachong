package com.shangpin.iog.linde.service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.csvreader.CsvReader;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.linde.dto.Item;
import com.shangpin.iog.service.SkuPriceService;

@Component
public class OrderImpl extends AbsOrderService {

	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String supplierNo = null;
	private static String path = null;
	private static String filePath = null;

	private static String smtpHost = null;
	private static String from = null;
	private static String fromUserPassword = null;
	private static String to = null;
	private static String subject = null;
	private static String messageText = null;
	private static String messageType = null;
	@Autowired
	SkuPriceService skuPriceService;

	@Autowired
	com.shangpin.iog.service.OrderService productOrderService;

	private static Map<String, String> map = new HashMap<String, String>();
	static {
		if (null == bdl) {
			bdl = ResourceBundle.getBundle("conf");
		}
		supplierId = bdl.getString("supplierId");
		supplierNo = bdl.getString("supplierNo");
		path = bdl.getString("path");
		filePath = bdl.getString("filePath");
		fromUserPassword = bdl.getString("fromUserPassword");
		from = bdl.getString("from");
		to = bdl.getString("to");
		subject = bdl.getString("subject");
		messageText = bdl.getString("messageText");
		messageType = bdl.getString("messageType");
		smtpHost = bdl.getString("smtpHost");
		
		List<Item> list = null;
		try {
			list = readLocalCSV(Item.class);
		} catch (Exception e1) {
		}
		for (Item item : list) {
			map.put(item.getBarCode(), item.getId());
		}

	}

	public void loopExecute() {
		this.checkoutOrderFromWMS(supplierNo, supplierId, true);
	}

	public void confirmOrder() {
		this.confirmOrder(supplierId);

	}

	/**
	 * 锁库存
	 */
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setExcState("0");
		orderDTO.setStatus(OrderStatus.PLACED);
	}

	/**
	 * 推送订单
	 */
	@Override
	public void handleConfirmOrder(final OrderDTO orderDTO) {
		orderDTO.setExcState("0");
		orderDTO.setStatus(OrderStatus.CONFIRMED);
	}

	/**
	 * 解除库存锁
	 */
	@Override
	public void handleCancelOrder(final ReturnOrderDTO deleteOrder) {
		deleteOrder.setExcState("0");
		// 超过一天 不需要在做处理 订单状态改为其它状体
		deleteOrder.setStatus(OrderStatus.CANCELLED);
	}

	/**
	 * 获取真正的供货商编号
	 *
	 * @param skuMap
	 *            key skuNo ,value supplerSkuNo
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public void getSupplierSkuId(Map<String, String> skuMap)
			throws ServiceException {

	}

	/*
	 * detail数据格式： skuId:数量,skuId:数量
	 */

	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		// TODO Auto-generated method stub
		deleteOrder.setExcState("0");
		deleteOrder.setStatus(OrderStatus.REFUNDED);
	}

	@Override
	public void handleEmail(OrderDTO orderDTO) {
	}

	public static <T> T fillDTO(T t, List<String> data) {
		try {
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < data.size(); i++) {
				fields[i].setAccessible(true);
				fields[i].set(t, data.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	public static <T> List<T> readLocalCSV(Class<T> clazz) throws Exception {
		String rowString = null;
		List<T> dtoList = new ArrayList<T>();
		// Set<T> dtoSet = new HashSet<T>();
		String[] split = null;
		List<String> colValueList = null;
		CsvReader cr = null;
		// 解析csv文件
		cr = new CsvReader(new FileReader(filePath));
		System.out.println("创建cr对象成功");
		// 得到列名集合
		cr.readRecord();
		int a = 0;
		while (cr.readRecord()) {
			a++;
			rowString = cr.getRawRecord();
			if (StringUtils.isNotBlank(rowString)) {
				split = rowString.split(" ");
				colValueList = Arrays.asList(split);
				T t = fillDTO(clazz.newInstance(), colValueList);
				dtoList.add(t);
			}
			System.out.println(a);
		}
		cr.close();
		return dtoList;
	}
	public void sendEmailToSupplier1() {

		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date beginDate = new Date();
		System.out.println(dft.format(beginDate));
		String endTime = dft.format(beginDate);
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		
		int week = date.get(Calendar.DAY_OF_WEEK);
		//如果为2，则为星期一，订单要查询从周五下午14::00到周一9:00的订单
		if(week==2){
			date.set(Calendar.HOUR, date.get(Calendar.HOUR) - 67);
		}else{
			date.set(Calendar.HOUR, date.get(Calendar.HOUR) - 19);
		}
		
		System.out.println(dft.format(date.getTime()));
		String startTime = dft.format(date.getTime());
		getOrderList(startTime,endTime);
	}
	public void sendEmailToSupplier2() {

		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date beginDate = new Date();
		System.out.println(dft.format(beginDate));
		String endTime = dft.format(beginDate);
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		date.set(Calendar.HOUR, date.get(Calendar.HOUR) - 5);
		System.out.println(dft.format(date.getTime()));
		String startTime = dft.format(date.getTime());
		getOrderList(startTime,endTime);
	}
	public void getOrderList(String startTime,String endTime){
		List<OrderDTO> listOrder = null;
		try {
			listOrder = productOrderService
					.getOrderBySupplierIdAndOrderStatusAndTime(supplierId,
							"confirmed", startTime, endTime);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		StringBuffer str = new StringBuffer();
		if(listOrder!=null&&!listOrder.isEmpty()){
			for (OrderDTO dto : listOrder) {
				// skuId:qty
				String detail = dto.getDetail();
				// 2220080-2014876494045
				String skuId = detail.split(":", -1)[0];
				String barCode = skuId.split("-")[1];
				if (barCode.startsWith("20")) {
					barCode = barCode.replaceFirst("20", "00").substring(0,
							barCode.length() - 1);
				}
				if (barCode.startsWith("21")) {
					barCode = barCode.replaceFirst("21", "90").substring(0,
							barCode.length() - 1);
				}
				String qty = "00001";
				String vid = skuId.split("-")[0];
				String id = map.get(barCode);
				str.append(barCode).append(" ").append(qty).append(" ").append(vid)
						.append(" ").append(id).append("\r\n");
			}
			messageText= "orders in the attachment";
			final String path = readLine(str.toString());
			
			Thread t = new Thread(	 new Runnable() {
				@Override
				public void run() {
					try {
						System.out.println("email");
						SendMail.sendGroupMailWithFile(smtpHost, from, fromUserPassword,to,subject,messageText,messageType, new File(path));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			t.start();
		}
	}
	private static String readLine(String content) {

		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH");
		Date beginDate = new Date();
		String date = dft.format(beginDate);
		date = date.replace("-", "").replace(" ", "");
		System.out.println(date);
		String relaPath = path + date + ".ate";
		File file = new File(relaPath);
		FileWriter fwriter = null;
		try {
			fwriter = new FileWriter(file);
			fwriter.write(content);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				fwriter.flush();
				fwriter.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return relaPath;
	}

}

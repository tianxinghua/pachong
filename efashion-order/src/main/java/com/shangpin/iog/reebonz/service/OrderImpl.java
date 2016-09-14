package com.shangpin.iog.reebonz.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.efashion.dto.Item;
import com.shangpin.iog.efashion.dto.RequestObject;
import com.shangpin.iog.efashion.dto.Result;
import com.shangpin.iog.efashion.dto.ReturnObject;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.service.SkuPriceService;

@Component
public class OrderImpl extends AbsOrderService {
	private static Logger loggerError = Logger.getLogger("error");
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = null;
	private static String supplierNo = null;
	private static String cancelUrl = null;
	private static String placeUrl = null;

	private static String smtpHost = null;
	private static String from = null;
	private static String fromUserPassword = null;
	private static String to = null;
	private static String subject = null;
	private static String messageText = null;
	private static String messageType = null;
	private static String isPurchaseExp = null;

	@Autowired
	SkuPriceService skuPriceService;
	static {
		if (null == bdl) {
			bdl = ResourceBundle.getBundle("conf");
		}
		supplierId = bdl.getString("supplierId");
		supplierNo = bdl.getString("supplierNo");
		cancelUrl = bdl.getString("cancelUrl");
		placeUrl = bdl.getString("placeUrl");

		smtpHost = bdl.getString("smtpHost");
		from = bdl.getString("from");
		fromUserPassword = bdl.getString("fromUserPassword");
		to = bdl.getString("to");
		subject = bdl.getString("subject");
		messageText = bdl.getString("messageText");
		messageType = bdl.getString("messageType");
		isPurchaseExp = bdl.getString("isPurchaseExp");
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
		orderDTO.setStatus(OrderStatus.PLACED);
	}

	/**
	 * 推送订单
	 */
	@Override
	public void handleConfirmOrder(final OrderDTO orderDTO) {

		String orderId = orderDTO.getSpOrderId();

		if (orderId.startsWith("20160206") || orderId.startsWith("20160207")
				|| orderId.startsWith("20160208")
				|| orderId.startsWith("20160209")) {
			orderDTO.setStatus(OrderStatus.CONFIRMED);
		} else {
			String rtnData = pushOrder(orderDTO,placeUrl,false);
			logger.info("rtnData====" + rtnData);
			System.out.println("rtnData====" + rtnData);
			String[] data = rtnData.split("\\|");
			String code = data[0];
			String res = data[1];
			if ("400".equals(code)) {
				System.out.println("result===我是else 200");
				Result result = new Gson().fromJson(res, Result.class);
				System.out.println("result==="+result.toString());
				if ("400".equals(result.getReqCode())) {
					System.out.println("返回结果：" + result.getResult());
					if ("StoreCode not valid".equals(result.getResult())) {
						orderDTO.setExcDesc(res);
						orderDTO.setExcState("0");
						orderDTO.setStatus(OrderStatus.NOHANDLE);
						loggerError.info(result.getResult());
						Thread t = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									SendMail.sendMessage(
											smtpHost,
											from,
											fromUserPassword,
											to,
											subject,
											"推送efashion订单"
													+ orderDTO.getSpOrderId()
													+ "出现错误,已置为不做处理，原因："
													+ orderDTO.getExcDesc(),
											messageType);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
						t.start();
					} else if ("Order number just placed".equals(result
							.getResult())) {
						orderDTO.setExcDesc(res);
						orderDTO.setExcState("0");
						orderDTO.setStatus(OrderStatus.NOHANDLE);
						Thread t = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									SendMail.sendMessage(
											smtpHost,
											from,
											fromUserPassword,
											to,
											subject,
											"推送efashion订单"
													+ orderDTO.getSpOrderId()
													+ "出现错误,已置为不做处理，原因："
													+ orderDTO.getExcDesc(),
											messageType);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
						t.start();
					} else if (result.getResult().startsWith("Quantity")) {
						System.out.println("isPurchaseExp"+isPurchaseExp);
						if ("yes".equals(isPurchaseExp)) {
							orderDTO.setExcDesc(result.getResult());
							String reResult = setPurchaseOrderExc(orderDTO);
							System.out.println(reResult);
							if ("-1".equals(reResult)) {
								orderDTO.setStatus(OrderStatus.NOHANDLE);
							} else if ("1".equals(reResult)) {
								orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
							} else if ("0".equals(reResult)) {
								orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
							}
						} else {
							orderDTO.setStatus(OrderStatus.NOHANDLE);
						}
					} else {
						loggerError.info(res);
						if (res.length() > 200) {
							res = res.substring(0, 200);
						}
						orderDTO.setExcDesc(res);
						orderDTO.setExcState("0");
						// 供应商返回信息有误，暂时设置成不处理
						orderDTO.setStatus(OrderStatus.NOHANDLE);
					}

				}
			} else if ("200".equals(code)) {
				System.out.println("result===我是else 200");
				ReturnObject obj = new Gson().fromJson(res, ReturnObject.class);
				if (obj != null) {
					Result r = obj.getResults();
					if (r != null) {
						if ("200".equals(r.getReqCode())) {
							orderDTO.setExcState("0");
							orderDTO.setSupplierOrderNo(r.getDescription());
							orderDTO.setStatus(OrderStatus.CONFIRMED);
						} else if ("400".equals(r.getReqCode())) {

							orderDTO.setExcState("0");
							orderDTO.setExcDesc(r.getDescription());
							logger.info("返回结果：" + r.getDescription());
							// 供应商返回信息有误，暂时设置成不处理
							if (r.getDescription().indexOf("Quantity") > 0) {
								if ("yes".equals(isPurchaseExp)) {
									String reResult = setPurchaseOrderExc(orderDTO);
									if ("-1".equals(reResult)) {
										orderDTO.setStatus(OrderStatus.NOHANDLE);
									} else if ("1".equals(reResult)) {
										orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
									} else if ("0".equals(reResult)) {
										orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
									}
								} else {
									orderDTO.setStatus(OrderStatus.NOHANDLE);
								}
							} else {
								orderDTO.setStatus(OrderStatus.NOHANDLE);
							}
						}
					}
				}
			} else {
				System.out.println("result===我是else");
				loggerError.info(res);
				if (res.length() > 200) {
					res = res.substring(0, 200);
				}
				orderDTO.setExcDesc(res);
				orderDTO.setExcState("0");
				orderDTO.setStatus(OrderStatus.NOHANDLE);
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							SendMail.sendMessage(
									smtpHost,
									from,
									fromUserPassword,
									to,
									subject,
									"推送efashion订单" + orderDTO.getSpOrderId()
											+ "出现错误,已置为不做处理，原因："
											+ orderDTO.getExcDesc(),
									messageType);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				t.start();

			}
		}
	}

	private String pushOrder(OrderDTO orderDTO,String url,boolean flag) {

		System.out.println("order info:"+orderDTO.toString());
		String json = getJsonData(orderDTO,flag);
		logger.info("推送的数据：" + json);
		System.out.println("推送的数据：" + json);
		HttpResponse response = null;
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = httpClientBuilder.build();
		HttpPost httpPost = new HttpPost(url);
		StringEntity entity = new StringEntity(json, "utf-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		Map map = new HashMap();
		map.put("order", json);
		Iterable<? extends NameValuePair> nvs = map2NameValuePair(map);
		httpPost.setEntity(new UrlEncodedFormEntity(nvs, Charset
				.forName("UTF-8")));
		String str = null;
		try {
			response = httpClient.execute(httpPost);
			str = EntityUtils.toString(response.getEntity());
		} catch (Exception e) {

			loggerError.info("推送订单返回的响应response:" + response + str);
			loggerError.info(e.getMessage());
			e.printStackTrace();
		}

		return response.getStatusLine().getStatusCode() + "|" + str;
	}

	private static Iterable<? extends NameValuePair> map2NameValuePair(
			Map<String, String> param) {
		Iterator<Entry<String, String>> kvs = param.entrySet().iterator();
		List<NameValuePair> nvs = new ArrayList<NameValuePair>(param.size());
		while (kvs.hasNext()) {
			Entry<String, String> kv = kvs.next();
			NameValuePair nv = new BasicNameValuePair(kv.getKey(),
					kv.getValue());
			nvs.add(nv);
		}
		return nvs;
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
		final OrderDTO orderDTO = new OrderDTO();
		orderDTO.setSpOrderId(deleteOrder.getSpOrderId());
		orderDTO.setDetail(deleteOrder.getDetail());
		orderDTO.setCreateTime(deleteOrder.getCreateTime());
		orderDTO.setPurchasePriceDetail(null);
		String orderId = orderDTO.getSpOrderId();

			String rtnData = pushOrder(orderDTO,cancelUrl,true);
			logger.info("rtnData====" + rtnData);
			System.out.println("rtnData====" + rtnData);
			String[] data = rtnData.split("\\|");
			String code = data[0];
			String res = data[1];
			if ("400".equals(code)) {
				System.out.println("result===我是else 200");
				Result result = new Gson().fromJson(res, Result.class);
				System.out.println("result==="+result.toString());
				if ("400".equals(result.getReqCode())) {
					System.out.println("返回结果：" + result.getResult());
					loggerError.info(result.getResult());
				}
			} else if ("200".equals(code)) {
				System.out.println("result===我是else 200");
				ReturnObject obj = new Gson().fromJson(res, ReturnObject.class);
				if (obj != null) {
					Result r = obj.getResults();
					if (r != null) {
						if ("200".equals(r.getReqCode())) {
							orderDTO.setExcState("0");
							orderDTO.setSupplierOrderNo(r.getDescription());
							orderDTO.setStatus(OrderStatus.CONFIRMED);
						} else if ("400".equals(r.getReqCode())) {

							orderDTO.setExcState("0");
							orderDTO.setExcDesc(r.getDescription());
							logger.info("返回结果：" + r.getDescription());
							// 供应商返回信息有误，暂时设置成不处理
							if (r.getDescription().indexOf("Quantity") > 0) {
								if ("yes".equals(isPurchaseExp)) {
									String reResult = setPurchaseOrderExc(orderDTO);
									if ("-1".equals(reResult)) {
										orderDTO.setStatus(OrderStatus.NOHANDLE);
									} else if ("1".equals(reResult)) {
										orderDTO.setStatus(OrderStatus.PURCHASE_EXP_SUCCESS);
									} else if ("0".equals(reResult)) {
										orderDTO.setStatus(OrderStatus.PURCHASE_EXP_ERROR);
									}
								} else {
									orderDTO.setStatus(OrderStatus.NOHANDLE);
								}
							} else {
								orderDTO.setStatus(OrderStatus.NOHANDLE);
							}
						}
					}
				}
			} else {
				System.out.println("result===我是else");
				loggerError.info(res);
				if (res.length() > 200) {
					res = res.substring(0, 200);
				}
				orderDTO.setExcDesc(res);
				orderDTO.setExcState("0");
				orderDTO.setStatus(OrderStatus.NOHANDLE);
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							SendMail.sendMessage(
									smtpHost,
									from,
									fromUserPassword,
									to,
									subject,
									"推送efashion订单" + orderDTO.getSpOrderId()
											+ "出现错误,已置为不做处理，原因："
											+ orderDTO.getExcDesc(),
									messageType);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				t.start();

		}
	}

	@Override
	public void handleEmail(OrderDTO orderDTO) {

	}

	private String getJsonData(OrderDTO orderDTO,boolean flag) {

		JSONObject array = null;
		try {
			RequestObject obj = new RequestObject();
			obj.setOrder_number(orderDTO.getSpOrderId());
			obj.setItems_count("1");
			SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
			obj.setDate(time.format(orderDTO.getCreateTime()));
			Item item = new Item();

			String detail = orderDTO.getDetail();
			String skuNo = null;
			String num = null;
			if (detail != null) {
				skuNo = detail.split(":")[0];
				num = detail.split(":")[1];
			}
			// item.getSku_id()+"|"+item.getProduct_reference()+"|"+item.getColor_reference()+"|"+size
//			item.setSku_id(skuNo.split("\\|")[0]);
//			item.setColor_reference(skuNo.split("\\|")[2]);
			item.setProduct(skuNo.split("-")[0]);
			item.setQuantity(num);
			String size = skuNo.split("-")[1];
			if ("A".equals(size)) {
				size = null;
			}
			item.setSize(size);
			if(orderDTO.getPurchasePriceDetail()!=null){
				BigDecimal priceInt = new BigDecimal(
						orderDTO.getPurchasePriceDetail());
				String price = priceInt.divide(new BigDecimal(1.05), 2)
						.setScale(2, BigDecimal.ROUND_HALF_UP).toString();

				item.setPurchase_price(price);
			}
			if(flag){
				item.setPurchase_price("1");
			}
			Item[] i = { item };
			obj.setItems(i);
			array = JSONObject.fromObject(obj);
		} catch (Exception ex) {

		}
		if (array != null) {
			return array.toString();
		} else {
			return null;
		}

	}

	public static void main(String[] args) {
		OrderImpl ompl = new OrderImpl();
		ReturnOrderDTO orderDTO = new ReturnOrderDTO();
		OrderDTO orderDTO1 = new OrderDTO();
		String d = "5731b9622b33300afbc3b4b6-40:1";
		orderDTO.setDetail(d);
		orderDTO.setSpOrderId("201609011112111");
		orderDTO.setCreateTime(new Date());
		orderDTO1.setDetail(d);
		orderDTO1.setSpOrderId("201609011112111");
		orderDTO1.setCreateTime(new Date());
		orderDTO1.setPurchasePriceDetail("1.11");
//		orderDTO.setSpPurchaseDetailNo("CGD2016090100392");
		ompl.handleConfirmOrder(orderDTO1);//(orderDTO);
		ompl.handleRefundlOrder(orderDTO);//(orderDTO);
	}
}

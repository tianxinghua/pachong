package com.shangpin.api.airshop.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.dto.CBatchTaskDetail;
import com.shangpin.api.airshop.dto.Product;
import com.shangpin.api.airshop.dto.PurchaseOrderDetail;
import com.shangpin.api.airshop.dto.ReturnInOutBound;
import com.shangpin.api.airshop.dto.ReturnWaybillTaskOrderDetail;
import com.shangpin.api.airshop.dto.TMSTrack;
import com.shangpin.api.airshop.dto.WaybillTaskOrderDetail;
import com.shangpin.api.airshop.dto.base.ResponseContentOne;
import com.shangpin.api.airshop.util.HttpClientUtil;
import com.shangpin.api.airshop.util.HttpUtil45;
import com.shangpin.api.airshop.util.OutTimeConfig;
import com.shangpin.common.utils.FastJsonUtil;
@Service
public class TMSService {
	private static Logger logger = LoggerFactory.getLogger(TMSService.class);
	@Autowired
	private PurOrderService purOrderService;
	public String saveInAndOutBound(String sopUserNo,
			String supplierOrderNos,String purchases, String opUser) {
		
		//supplierOrderNos以逗号分隔 ，purchases以|分隔
		String [] arr = supplierOrderNos.split(",",-1);
		String [] arrPur = purchases.split("\\|");
		for(int i=0;i<arr.length;i++){
			String supplierOrderNo = arr[i];
			String purchase = arrPur[i].split("_")[0];
			String type= null;
			Map<String, String> params = new HashMap<String, String>();
			params.put("SupplierOrderNo",supplierOrderNo );
			params.put("OpUser",opUser );
			logger.info("入库参数："+params.toString());
			String result = HttpClientUtil.doPost(ApiServiceUrlConfig.getSaveInboundWayBill() , params);
			logger.info(supplierOrderNo+"入库返回结果："+result);
			String resu = HttpClientUtil.doPost(ApiServiceUrlConfig.getSaveOutboundWayBill() , params);
			logger.info(supplierOrderNo+"出库返回结果："+resu);
			ReturnInOutBound returnInOutBound = FastJsonUtil.deserializeString2Obj(resu,ReturnInOutBound.class);
			//出库成功，则更改采购单状态为代发货
			if("true".equals(returnInOutBound.getSuccessed())){
				//出库成功，进行采购单状态修改
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();
					Map<String, String> map = new HashMap<String, String>();
					map.put("SopUserNo", sopUserNo);
					map.put("SopPurchaseOrderDetailNo", purchase);
					map.put("IsStock", type);
					list.add(map);
				ResponseContentOne content = purOrderService.checkStock(list, type);
				logger.info(purchase+"修改采购单状态返回结果："+content.getMsg()+content.getContent());
			}
		}
		return FastJsonUtil
				.serialize2String(ResponseContentOne.successResp("success"));
	}

	public String createTrack(String supplierNo, String sopUserNo, String supplierOrders,String sopPurchaseNo,
			String opUser) {
		
		supplierOrders = supplierOrders.replace(",","|");
		Map<String, String> params = new HashMap<String, String>();
		params.put("OpUser",opUser);
		params.put("SupplierOrderNos",supplierOrders);
		logger.info(supplierOrders+"TMS创建批次传递参数:" + params);
		if(supplierOrders.isEmpty()){
			return FastJsonUtil
					.serialize2String(ResponseContentOne.errorResp(""));
		}
		String result = HttpClientUtil.doPost(ApiServiceUrlConfig.getCreateTrack() , params);
		logger.info(supplierOrders+"TMS创建批次完成返回结果:" + result);
		ReturnCreateTask returnInOutBound = FastJsonUtil.deserializeString2Obj(result,ReturnCreateTask.class);
		if("true".equals(returnInOutBound.getSuccessed())){
			//tms创建物流成功，通知sop采购单状态为完成
		
//			String str = "{\"SupplierNo\":\""+supplierNo+"\",\"PurchaseOrderNos\":\""+sopPurchaseNo+"\"}";
//			logger.info("tms创建物流成功，通知sop采购单状态为完成传递的参数:" + str);
//			String sopResult = HttpClientUtil.doPost(ApiServiceUrlConfig.getModifyPurchaseOrderStatus() , str);
//			logger.info(supplierOrders+"修改采购单状态为完成返回结果:" + sopResult);
			return FastJsonUtil
					.serialize2String(ResponseContentOne.successResp(returnInOutBound.getWaybillTaskList().get(0).getTaskBatchNo()));
		}else{
			return FastJsonUtil
					.serialize2String(ResponseContentOne.errorResp(returnInOutBound.getMessage()));
		}
	}

	public String getTrackList(TMSTrack tmsTrack,String SupplierNo) {
		// TODO Auto-generated method stub
		String endTime = tmsTrack.getUpdateTimeEnd();
		String startTime = tmsTrack.getUpdateTimeBegin();
		
		try {
			if(endTime!=null&&!StringUtils.isEmpty(endTime)){
				endTime = endTime.split("-")[2] + "-" +endTime.split("-")[1] + "-" +endTime.split("-")[0] + " 23:59:59";	
				endTime =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime).getTime()+"";
				endTime = "/Date("+endTime+"+0800)/";
			}else{
				endTime = null;
			}
			if(startTime!=null&&!StringUtils.isEmpty(startTime)){
				startTime = startTime.split("-")[2] + "-" +startTime.split("-")[1] + "-" +startTime.split("-")[0] + " 00:00:00";
				startTime =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime).getTime()+"";
				startTime = "/Date("+startTime+"+0800)/";
			}else{
				startTime = null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("MasterTrackNo",tmsTrack.getMasterNo());
		params.put("SearchETime",endTime);
		params.put("SearchSTime",startTime);
		params.put("TaskBatchNo",tmsTrack.getTaskBatchNo());
		if(tmsTrack.getStatus().equals("5")){
			params.put("TaskStatus",null);
		}else{
			params.put("TaskStatus",tmsTrack.getStatus());
		}
		
		params.put("SupplierNo",SupplierNo);
		logger.info("查询批次传递的参数："+params.toString());
		String result = HttpClientUtil.doPost(ApiServiceUrlConfig.getGetTrack() , params);
		ReturnWayBillTask returnInOutBound = FastJsonUtil.deserializeString2Obj(result,ReturnWayBillTask.class);
		logger.info("查询批次返回的结果："+returnInOutBound.getSuccessed());
		if("true".equals(returnInOutBound.getSuccessed())){
			return FastJsonUtil.serialize2StringEmpty(ResponseContentOne.successResp(returnInOutBound));
		}else{
			return FastJsonUtil
					.serialize2String(ResponseContentOne.errorRespWithApi(returnInOutBound.getMessage()));
		}
	}

	public String getTrackDetail(String taskBatchNo,String supplierNo,String importType) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("CTaskBatchNo",taskBatchNo);
		
		String json = "{\"CTaskBatchNo\":\""+taskBatchNo+"\"}";
		logger.info(taskBatchNo+"查询批次明细传参："+json);
		String result = HttpUtil45.operateData("post","json", ApiServiceUrlConfig.getGetTrackDetail(), new OutTimeConfig(1000*10*6,1000*10*6,1000*10*6), null, json, null,null);
		logger.info(taskBatchNo+"查询批次明细返回结果："+result);
//		String result = HttpClientUtil.doPost(ApiServiceUrlConfig.getGetTrackDetail() , json);
		ReturnWaybillTaskOrderDetail returnInOutBound = FastJsonUtil.deserializeString2Obj(result,ReturnWaybillTaskOrderDetail.class);
		if("true".equals(returnInOutBound.getSuccessed())){
			//根据批次号得到批次下所有的orderNo
			List<WaybillTaskOrderDetail> list = returnInOutBound.getWaybillTaskOrderDetailList();
			if(list!=null&&!list.isEmpty()){
				List<CBatchTaskDetail> taskList = new ArrayList<CBatchTaskDetail>();
				for(WaybillTaskOrderDetail way : list){
					//根据orderNo获取采购单信息
					String orderNo = way.getOrderNo();
					String supplierOrderNo = way.getSupplierOrderNO();
					CBatchTaskDetail detail = new CBatchTaskDetail();
					detail.setOrderNo(orderNo);
					detail.setQty(way.getOrderProductCount());
					String status = way.getIsRequestSuccess();
					if("0".equals(status)){
						detail.setStatus("nohandle");
					}else if("1".equals(status)||"2".equals(status)){
						detail.setStatus("success");
					}else{
						detail.setStatus("fail");
					}
					detail.setLogisticsOrderNo(way.getTrackNo());
					detail.setRequestTimes(way.getRequestTimes());
					String path = way.getPlaneSinglePath();
					if(path!=null&&!path.isEmpty()){
						path = way.getPlaneSinglePath().replace("\\","/");
						detail.setServiceUrl(ApiServiceUrlConfig.getIp()+"/wayprint/print?path="+path);
					}else{
						detail.setServiceUrl(null);
					}
					
					detail.setSupplierOrderNo(supplierOrderNo);
					String porderDetailResult = purOrderService.getCGDDetailByOrderNo(orderNo,supplierOrderNo,supplierNo);
					logger.info("根据supplierOrderNo:"+supplierOrderNo+"和orderNo:"+orderNo+"获取采购单信息返回结果："+porderDetailResult);
					JSONArray jsonObject = JSON.parseArray(porderDetailResult);
					if(jsonObject!=null&&!jsonObject.isEmpty()){
						PurchaseOrderDetail porderDetai = FastJsonUtil.deserializeString2Obj(jsonObject.get(0).toString(),PurchaseOrderDetail.class);
						if(porderDetai!=null){
							//根据supplierSkuNo和supplierId获取原始货号
							String jsonProduct = null;
							try {
								jsonProduct = product(supplierNo, URLEncoder.encode(porderDetai.getSupplierSkuNo(),"UTF-8"));
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if(StringUtils.isEmpty(json) || "[]".equals(json)){
								return null;
							}
							List<Product> products = FastJsonUtil.deserializeString2ObjectList(jsonProduct, Product.class);
							if(products!=null&&products.size()>0){
								detail.setProductModel(products.get(0).getProductCode());
							}
							detail.setBrandName(porderDetai.getBrandNo());
							
							detail.setProductName(porderDetai.getProductName());
							
							detail.setSopPurchaseOrderNo(porderDetai.getSopPurchaseOrderNo());
							detail.setSupplierSkuNo(porderDetai.getSupplierSkuNo());
							detail.setSkuPrice(porderDetai.getSkuPrice());
							detail.setImportType(importType);
							if(importType!=null&&"3".equals(importType)){
								detail.setServiceUrl(ApiServiceUrlConfig.getIp()+"/wayprint/printDeliveryNo?deliveryNo="+way.getTrackNo());
							}
							
						}
					}
					taskList.add(detail);
				}
				return FastJsonUtil.serialize2StringEmpty(ResponseContentOne.successResp(taskList));
			}else{
				return FastJsonUtil
						.serialize2String(ResponseContentOne.errorParam());
			}
			
		}else{
			return FastJsonUtil
					.serialize2String(ResponseContentOne.errorRespWithApi(returnInOutBound.getMessage()));
		}
	}
	public String product(String supplierNo, String supplierSkuNo){
		try{
			String url=ApiServiceUrlConfig.getProductUri();
			Map<String,String> request = new HashMap<>();
			request.put("supplierId",supplierNo);
			request.put("skuId",supplierSkuNo);
			return HttpClientUtil.doPost(url, request);// String.class);
		}catch(Exception e){
			
		}
		return null;
	}
//	public InputStream print(String supplierNo, String path)  {
//		InputStream in = getFile(path);
//		try {
//			byte[] picB = IOUtils.toByteArray(in);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		StringBuffer   out   =   new   StringBuffer(); 
//        byte[]   b   =   new   byte[4096]; 
//        try {
//			for   (int   n;   (n   =   in.read(b))   !=   -1;)   { 
//			        out.append(new   String(b,   0,   n)); 
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		String urlStr = "http://127.0.0.1:8080/print/printFile";
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("content", out.toString());
////		String json = HttpUtil45.operateData("post","txt", urlStr, new OutTimeConfig(), map, null, null, null);
////		if("{\"error\":\"发生异常错误\"}".equals(json)){
////			return "local print service no run";
////		}
//		return in;
//	}
//	public static InputStream getFile(String path){
//	    FTPClient ftpClient = new FTPClient();
//	    String hostName = "192.168.20.219";
//	    String userName = "erp@ftp";
//	    String password = "erp@ftp";
//	    path = "/FedExShipmentLabels/15/02/03/TCI201502030002_O20150130046690_M794629620485.txt";
//	    try {
//	      ftpClient.connect(hostName, 21);
//	      ftpClient.setControlEncoding("UTF-8");
//	      ftpClient.login(userName, password);
//	      ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//	      InputStream inStream =ftpClient.retrieveFileStream(path);;
//	      ftpClient.quit();
//	      return  inStream;
//	    } catch (Exception e) {
//	      e.printStackTrace();
//	    } 
//	    return null;
//	  }
//	 public static String file2String(File file) { 
//         StringBuffer sb = new StringBuffer(); 
//         try { 
//                 LineNumberReader reader = new LineNumberReader(new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))); 
//                 String line; 
//                 while ((line = reader.readLine()) != null) { 
//                         sb.append(line).append(System.getProperty("line.separator")); 
//                 } 
//         } catch (Exception e) { 
//         } 
//         return sb.toString(); 
// }

	public String getLogisticData(String taskBatchNo, String sopUserNo) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("CTaskBatchNo",taskBatchNo);
		params.put("OpUser",sopUserNo);
		FaPiao a = new FaPiao();
		List<List> all = new ArrayList<List>();
		logger.info(taskBatchNo+"打印发票传递参数："+params.toString());
		String result = HttpClientUtil.doPost(ApiServiceUrlConfig.getGetLogisticData() , params);
		logger.info(taskBatchNo+"打印发票返回结果："+result);
		JSONObject jsonObject = JSON.parseObject(result);
		if(jsonObject.get("Successed").equals(true)){ 
			
			JSONObject jj = JSON.parseObject(jsonObject.get("Invoice")+"");
			
			Invoice invoice = FastJsonUtil.deserializeString2Obj(jsonObject.get("Invoice")+"", Invoice.class);
			JSONArray arr = JSON.parseArray(jj.get("ProductList")+"");
			int count = arr.size();
			//总页数
			int page = 0;
			//第一页最多的商品个数
			int firstPage = 6;
			//除了第一页，其他页最多的商品个数
			int pageSize = 8;
			//一般贸易和跨境电商发票不一样 ， 2代表一般贸易
			if(invoice.getImportType().equals("2")){
				firstPage = 6;
			}else if(invoice.getImportType().equals("1")){
				firstPage = 1;
			}
			if(count>firstPage){
				page = (count-firstPage)/pageSize ;
				page = page +1;
				List<Object> l = new ArrayList<Object>();
				//发票第一页限制商品
				for(int i=0;i<firstPage;i++){
					
					l.add(arr.get(i));
				}
				all.add(l);
				for(int i=0;i<page;i++){
					l = new ArrayList<Object>(); 
					for(int j=(firstPage+i*pageSize);j<(firstPage+(i+1)*pageSize);j++){
						if(j>=count){
							break;
						}
						l.add(arr.get(j));
					}
					all.add(l);
				}
				a.setList(all);
				a.setPage(all.size());
				a.setInvoice(invoice);
			}else{
//				JSONArray arr = JSON.parseArray(jj.get("ProductList")+"");
				all.add(arr);
				a.setList(all);
				a.setPage(all.size());
				a.setInvoice(invoice);
			}
			
			return  FastJsonUtil.serialize2StringEmpty(ResponseContentOne.successResp(a));
		}
		return  FastJsonUtil.serialize2StringEmpty(ResponseContentOne.errorResp(jsonObject.get("Message")+""));
	}

	public String getShipingCartonsData(String taskBatchNo, String accountName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("TaskBatchNo",taskBatchNo);
		FaPiao a = new FaPiao();
		List<List> all = new ArrayList<List>();
		List<List> allDetail = new ArrayList<List>();
		
		logger.info(taskBatchNo+"打印发票传递参数："+params.toString());
		String result = HttpClientUtil.doPost(ApiServiceUrlConfig.getPrintBoxInfo() , params);
		logger.info(taskBatchNo+"打印发票返回结果："+result);
		JSONObject jsonObject = JSON.parseObject(result);
		if(jsonObject.get("Successed").equals(true)){ 
			
			JSONObject jj = JSON.parseObject(jsonObject.get("Result")+"");
			JSONArray arr = JSON.parseArray(jj.get("BoxInfoList")+"");
			JSONArray arrDetail = JSON.parseArray(jj.get("BoxInfoDetailList")+"");
			if(jj.get("SendTime")!=null){
				String sendTime = jj.get("SendTime").toString();
				a.setSendTime(sendTime);
			}
			int count = arr.size();
			//总页数
			int page = 0;
			//第一页最多的商品个数
			int firstPage = 11;
			//除了第一页，其他页最多的商品个数
			int pageSize = 15;
			//一般贸易和跨境电商发票不一样 ， 2代表一般贸易
			if(count>firstPage){
				page = (count-firstPage)/pageSize ;
				page = page +1;
				List<Object> l = new ArrayList<Object>();
				List<Object> detailList = new ArrayList<Object>();
				//发票第一页限制商品
				for(int i=0;i<firstPage;i++){
					l.add(arr.get(i));
					
					detailList.add(arrDetail.get(i));	
				}
				all.add(l);
				allDetail.add(detailList);
				for(int i=0;i<page;i++){
					l = new ArrayList<Object>(); 
					detailList = new ArrayList<Object>(); 
					for(int j=(firstPage+i*pageSize);j<(firstPage+(i+1)*pageSize);j++){
						if(j>=count){
							break;
						}
						l.add(arr.get(j));
						detailList.add(arrDetail.get(j));
					}
					all.add(l);
					allDetail.add(detailList);
				}
				a.setList(all);
				a.setPage(all.size());
				a.setDetailList(allDetail);
			}else{
				all.add(arr);
				allDetail.add(arrDetail);
				a.setList(all);
				a.setDetailList(allDetail);
				a.setPage(all.size());
			}
			
			return  FastJsonUtil.serialize2StringEmpty(ResponseContentOne.successResp(a));
		}
		return  FastJsonUtil.serialize2StringEmpty(ResponseContentOne.errorResp(jsonObject.get("Message")+""));
	} 
}

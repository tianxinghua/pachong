package com.shangpin.api.airshop.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.dto.*;
import com.shangpin.api.airshop.dto.base.ResponseContentOne;
import com.shangpin.api.airshop.dto.direct.DirectDeliverOrder;
import com.shangpin.api.airshop.dto.direct.DirectDeliveryOrderRS;
import com.shangpin.api.airshop.dto.direct.SupplierSegments;
import com.shangpin.api.airshop.dto.request.PurchaseRequest;
import com.shangpin.api.airshop.service.FindOrderService;
import com.shangpin.api.airshop.service.PurOrderService;
import com.shangpin.api.airshop.util.Constants;
import com.shangpin.api.airshop.util.DateFormat;
import com.shangpin.api.airshop.util.ExportExcelUtils;
import com.shangpin.api.airshop.util.HttpClientUtil;
import com.shangpin.common.utils.DateUtil;
import com.shangpin.common.utils.FastJsonUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 17:46 2018/10/8
 * @Description: 真直发（又名直邮） 供应商品 controller
 */


@RestController
@RequestMapping("/directDeliveryPurchase")
@SessionAttributes({Constants.SESSION_USER}) //添加到session作用域
public class DirectDeliveryOrderController {

    private static Logger log = LoggerFactory.getLogger(PurOrderController.class);

    @Autowired
    private PurOrderService purOrderService;

    @Autowired
    private FindOrderService findOrderService;


    /**
     *  第一步采购单查询列表
     * @param purchase
     *            请求参数实体
     * @return
     */
    @RequestMapping(value = "/findDirectPorderbyPage", method = {RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    public String findDirectPorderbyPage(PurchaseRequest purchase, HttpServletRequest request, boolean flag) {
        long startTime = new Date().getTime();
        String pageIndex = purchase.getPageIndex();
        String pageSize = purchase.getPageSize();
        String detailStatus = (String) purchase.getDetailStatus();
        String isStock = (String) purchase.getIsStock();
        if (StringUtils.isEmpty(pageIndex) || StringUtils.isEmpty(pageSize)) {
            return FastJsonUtil.serialize2String(ResponseContentOne.errorParam());
        }
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(Constants.SESSION_USER);
        String userNo = userInfo.getSopUserNo();
        String supplierNo = userInfo.getSupplierNo();

        if(flag){
            purchase.setDetailStatus(null);
            purchase.setIsDoStock("0");
        }else{
            purchase.setDetailStatus(this.convert(detailStatus));
        }

        purchase.setIsStock(this.convert(isStock));
        purchase.setSopUserNo(userNo);
        purchase.setSupplierNo(supplierNo);
        if(purchase.getImportType()==null){
            purchase.setImportType("0");
        }
        purchase.setUpdateTimeBegin(DateFormat.TimeFormatChangeToString(purchase.getUpdateTimeBegin(), "dd-MM-yyyy", "yyyy-MM-dd"));
        purchase.setUpdateTimeEnd(DateFormat.TimeFormatChangeToString(purchase.getUpdateTimeEnd(), "dd-MM-yyyy", "yyyy-MM-dd"));
        if (purchase.getUpdateTimeBegin()==null||purchase.getUpdateTimeEnd()==null) {
            purchase.setUpdateTimeBegin("");
            purchase.setUpdateTimeEnd("");
        }
        ResponseContentOne<PurchaseOrders> result = purOrderService.findDirectPorderbyPage(purchase);
        if (null == result) {
            return FastJsonUtil.serialize2String(ResponseContentOne.errorResp("返回数据为空"));
        }
        PurchaseOrders pOrders = result.getContent();
        if(pOrders!=null){
            List<PurchaseOrderDetail> list = pOrders.getPurchaseOrderDetails();
            if(list!=null&&!list.isEmpty()){
                for (int i = 0; i < list.size(); i++) {
                    PurchaseOrderDetail detail = list.get(i);

                    //请求商品 原始链接
                    String content = "{\"supplierId\":\""+userNo+"\",\"skuId\":\""+detail.getSupplierSkuNo()+"\"}";
                    try{
                        log.info("【请求原始品牌链接 url：{} param:{}】",ApiServiceUrlConfig.getQueryHubProduct(),content);
                        String productUrl = HttpClientUtil.doPostForJson(ApiServiceUrlConfig.getQueryHubProduct(), content);
                        log.info("【请求原始品牌返回结果：{} 】",productUrl);
                        if(productUrl==null){
                            productUrl="";
                        }
                        detail.setProductUrl(productUrl);
                    }catch(Exception e){
                        log.error("【请求原始品牌链接失败 url:{}  param:{} errorMsg:{} 】",ApiServiceUrlConfig.getQueryHubProduct(),content,e.getMessage());
                        e.printStackTrace();
                    }

                    // 请求商品原始 图片链接
                    String picUrl = detail.getPicUrl();
                    if(StringUtils.isEmpty(picUrl)){
                        try{
                            picUrl = getPicUrl(detail.getSkuNo());
                        }catch(Exception ex){
                            log.error("【请求商品图片失败 skuNo:{} errorMsg:{} 】 ",detail.getSkuNo(),ex.getMessage());
                        }
                        if(picUrl!=null){
                            detail.setPicUrl(picUrl);
                        }else{
                            detail.setPicUrl("");
                        }
                    }
                    //请求TMSURL
                    String urlTMS = null;
                    try {
                        urlTMS = ApiServiceUrlConfig.getIp()+"/wayprint/printPIDAndSupplierOrderNo?supplierOrderNo="+detail.getSupplierOrderNo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    detail.setTmsUrl(urlTMS);
                }
            }else{
                pOrders.setPurchaseOrderDetails(new ArrayList<PurchaseOrderDetail>());
            }
        }
        log.info("查询订单列表总耗时："+(new Date().getTime()-startTime));
        return FastJsonUtil.serialize2String(ResponseContentOne.successResp(pOrders));
    }



    /**
     *  第二步采购单查询列表
     * @param purchase
     *            请求参数实体
     * @return
     */
    @RequestMapping(value = "/findDirectPorderStockedbyPage", method = {RequestMethod.GET, RequestMethod.POST }, produces = "text/html;charset=UTF-8")
    public String findDirectPorderStockedbyPage(PurchaseRequest purchase, HttpServletRequest request, boolean flag) {
        long startTime = new Date().getTime();
        String pageIndex = purchase.getPageIndex();
        String pageSize = purchase.getPageSize();
        String detailStatus = (String) purchase.getDetailStatus();
        String isStock = (String) purchase.getIsStock();
        if (StringUtils.isEmpty(pageIndex) || StringUtils.isEmpty(pageSize)) {
            return FastJsonUtil.serialize2String(ResponseContentOne.errorParam());
        }
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(Constants.SESSION_USER);
        String userNo = userInfo.getSopUserNo();
        String supplierNo = userInfo.getSupplierNo();

        if(flag){
            purchase.setDetailStatus(null);
            purchase.setIsDoStock("0");
        }else{
            purchase.setDetailStatus(this.convert(detailStatus));
        }

        purchase.setIsStock(this.convert(isStock));
        purchase.setSopUserNo(userNo);
        purchase.setSupplierNo(supplierNo);
        if(purchase.getImportType()==null){
            purchase.setImportType("0");
        }
        purchase.setUpdateTimeBegin(DateFormat.TimeFormatChangeToString(purchase.getUpdateTimeBegin(), "dd-MM-yyyy", "yyyy-MM-dd"));
        purchase.setUpdateTimeEnd(DateFormat.TimeFormatChangeToString(purchase.getUpdateTimeEnd(), "dd-MM-yyyy", "yyyy-MM-dd"));
        if (purchase.getUpdateTimeBegin()==null||purchase.getUpdateTimeEnd()==null) {
            purchase.setUpdateTimeBegin("");
            purchase.setUpdateTimeEnd("");
        }
        ResponseContentOne<PurchaseOrders> result = purOrderService.findDirectPorderStockedbyPage(purchase);
        if (null == result) {
            return FastJsonUtil.serialize2String(ResponseContentOne.errorResp("返回数据为空"));
        }
        PurchaseOrders pOrders = result.getContent();
        if(pOrders!=null){
            List<PurchaseOrderDetail> list = pOrders.getPurchaseOrderDetails();
            if(list!=null&&!list.isEmpty()){
                for (int i = 0; i < list.size(); i++) {
                    PurchaseOrderDetail detail = list.get(i);

                    //请求商品 原始链接
                    String content = "{\"supplierId\":\""+userNo+"\",\"skuId\":\""+detail.getSupplierSkuNo()+"\"}";
                    try{
                        log.info("【请求原始品牌链接 url：{} param:{}】",ApiServiceUrlConfig.getQueryHubProduct(),content);
                        String productUrl = HttpClientUtil.doPostForJson(ApiServiceUrlConfig.getQueryHubProduct(), content);
                        log.info("【请求原始品牌返回结果：{} 】",productUrl);
                        if(productUrl==null){
                            productUrl="";
                        }
                        detail.setProductUrl(productUrl);
                    }catch(Exception e){
                        log.error("【请求原始品牌链接失败 url:{}  param:{} errorMsg:{} 】",ApiServiceUrlConfig.getQueryHubProduct(),content,e.getMessage());
                        e.printStackTrace();
                    }

                    // 请求商品原始 图片链接
                    String picUrl = detail.getPicUrl();
                    if(StringUtils.isEmpty(picUrl)){
                        try{
                            picUrl = getPicUrl(detail.getSkuNo());
                        }catch(Exception ex){
                            log.error("【请求商品图片失败 skuNo:{} errorMsg:{} 】 ",detail.getSkuNo(),ex.getMessage());
                        }
                        if(picUrl!=null){
                            detail.setPicUrl(picUrl);
                        }else{
                            detail.setPicUrl("");
                        }
                    }
                    //请求TMSURL
                    String urlTMS = null;
                    try {
                        urlTMS = ApiServiceUrlConfig.getIp()+"/wayprint/printPIDAndSupplierOrderNo?supplierOrderNo="+detail.getSupplierOrderNo();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    detail.setTmsUrl(urlTMS);
                }
            }else{
                pOrders.setPurchaseOrderDetails(new ArrayList<PurchaseOrderDetail>());
            }
        }
        log.info("查询订单列表总耗时："+(new Date().getTime()-startTime));
        return FastJsonUtil.serialize2String(ResponseContentOne.successResp(pOrders));
    }



    /**
     *  第一步 采购单 确认库存 接口
     * @param sopPurchaseOrderDetailNoAndStockFlags  (批量，pruchaseOrderInfo 是多个采购详情编号 以 | 分割 每个单元以 sopPurchaseOrderDetailNo_stockFlag 组成
     * 	                         其中 sopPurchaseOrderDetailNo:采购详情单号  stockFlag:有无库存标志 1:有库存 2:无库存 0:待确认库存 )
     * @return
     */
    @RequestMapping(value = "/modifyDirectPorderStock", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseContentOne modifyDirectPorderStock(String sopPurchaseOrderDetailNoAndStockFlags,@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
        if(userInfo==null){
            return ResponseContentOne.errorResp("3","");
        }

        if (StringUtils.isEmpty(sopPurchaseOrderDetailNoAndStockFlags)) {
            return ResponseContentOne.errorParam();
        }
        String userNo = userInfo.getSopUserNo();
        String[] info = sopPurchaseOrderDetailNoAndStockFlags.trim().split("\\|");
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        for (int i = 0; i < info.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            String[] str = info[i].trim().split("\\_");
            if (str.length < 2 || StringUtils.isEmpty(str[0])) {
                return ResponseContentOne.errorParam();
            }
            String purchaseNo = str[0];
            String stu = str[1];
            map.put("SopUserNo", userNo);
            map.put("SopPurchaseOrderDetailNo", purchaseNo);
            map.put("IsStock", stu);
            list.add(map);
        }
        ResponseContentOne content = purOrderService.modifyDirectPorderStock(list);
        return content;
    }


    /**
     *  第二步 初始化 采购单 发货页面数据
     * @param sopPurchaseOrderNo  采购订单单号
     * @return
     */
    @RequestMapping(value = "/findDirectPorderTransInfo", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseContentOne findDirectPorderTransInfo(String sopPurchaseOrderNo,@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {

        if(userInfo==null){
            return ResponseContentOne.errorResp("3","");
        }

        log.info("【真直发 发货页面初始化 入参：  url {}  sopPurchaseOrderNo {} ","findDirectPorderTransInfo",sopPurchaseOrderNo);
        if (StringUtils.isEmpty(sopPurchaseOrderNo)) {
            return ResponseContentOne.errorParam();
        }
        String userNo = userInfo.getSopUserNo();
        String supplierNo = userInfo.getSupplierNo();
        Map<String, String> map = new HashMap();
        map.put("SopUserNo", userNo);
        map.put("SopPurchaseOrderNo", sopPurchaseOrderNo);
        map.put("SupplierNo", supplierNo);
        ResponseContentOne content = purOrderService.findDirectPorderTransInfo(map);
        return content;
    }


    /**
     *  第二步 保存 发货页面输入数据
     * @param logisticsName  物流公司名称
     * @param logisticsCompanyNo 物流公司编号
     * @param logisticsOrderNo  物流单号
     * @param sopPurchaseOrderDetailNo  发货明细单号
     * @param remark  备注
     * @return
     */
    @RequestMapping(value = "/createDirectDeliveryOrder", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseContentOne createDirectDeliveryOrder(String logisticsName,String logisticsCompanyNo,String logisticsOrderNo,
                                                        String sopPurchaseOrderDetailNo,String remark, @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
        if(userInfo==null){
            return ResponseContentOne.errorResp("3","");
        }

        if(StringUtils.isEmpty(logisticsName)||StringUtils.isEmpty(sopPurchaseOrderDetailNo)){
            return ResponseContentOne.errorParam();
        }

        log.info("【 真直发 保存发货信息 入参：  logisticsName: {} ","logisticsOrderNo: {} sopPurchaseOrderDetailNo: {} remark: {}",logisticsName,logisticsOrderNo,sopPurchaseOrderDetailNo);
        if (StringUtils.isEmpty(logisticsName)||StringUtils.isEmpty(sopPurchaseOrderDetailNo)) {
            return ResponseContentOne.errorParam();
        }
        String userNo = userInfo.getSopUserNo();
        Map<String, Object> map = new HashMap();
        map.put("SopUserNo", userNo);
        map.put("LogisticsName", logisticsName);
        map.put("LogisticsCompanyNo", logisticsCompanyNo);
        map.put("LogisticsOrderNo", logisticsOrderNo);
        List<String> sopPurchaseOrderDetailNos = new ArrayList<>();
        sopPurchaseOrderDetailNos.add(sopPurchaseOrderDetailNo);
        map.put("SopPurchaseOrderDetailNos", sopPurchaseOrderDetailNos);
        //发货时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateDeliver = dateFormat.format(new Date());
        map.put("DateDeliver", dateDeliver);

        map.put("Remark", remark);
        ResponseContentOne content = purOrderService.createDirectDeliveryOrder(map);
        return content;
    }


    /***
     *  第三步 发货单列表查询
     *
     * @param deliveryOrdersRQ 页面请求参数
     * @return
     */
    @RequestMapping(value = "/findDirectDorderbyPage",  method = { RequestMethod.GET, RequestMethod.POST },produces = "text/html;charset=UTF-8")
    public String findDirectDorderbyPage(@Valid final DeliveryOrdersRQ deliveryOrdersRQ,
                       @ModelAttribute(Constants.SESSION_USER) UserInfo uInfo) {
        if(uInfo==null){
            return FastJsonUtil.serialize2StringEmpty(ResponseContentOne.errorResp("3",""));
        }
        String userNo = uInfo.getSopUserNo();
        deliveryOrdersRQ.setSopUserNo(uInfo.getSopUserNo());

        log.info("【直邮 发货列表页面 请求参数 deliveryOrdersRQ: {} 】",FastJsonUtil.serialize2StringEmpty(deliveryOrdersRQ));

        if (StringUtils.isEmpty(deliveryOrdersRQ.getStart()) || StringUtils.isEmpty(deliveryOrdersRQ.getLength())) {
            return FastJsonUtil.serialize2String(ResponseContentOne.errorParam());
        }
        // 请求数据
        ResponseContentOne< DirectDeliveryOrderRS > res =findOrderService.findDirectDorderbyPage(deliveryOrdersRQ);
        List<DirectDeliverOrder> deliverOrder = res.getContent().getDeliverOrder();
        int deliverOrderSize = deliverOrder.size();
        for (int i = 0; i < deliverOrderSize ; i++) {
            DirectDeliverOrder detail = deliverOrder.get(i);
            detail.setDateDeliver(DateFormat.TimeFormatChangeToString(detail.getDateDeliver(), "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy HH:mm:ss") );
            String content = "{\"supplierId\":\""+userNo+"\",\"skuId\":\""+detail.getSupplierSkuNo()+"\"}";
            try{
                log.info("【直邮发货列表请求原始品牌链接参数 userNo：{} skuId:{}】",userNo,detail.getSupplierSkuNo());
                String productUrl = HttpClientUtil.doPostForJson(ApiServiceUrlConfig.getQueryHubProduct(), content);
                log.info("【直邮发货列表请求原始品牌返回结果：{} 】",productUrl);
                if(productUrl==null){
                    productUrl="";
                }
                detail.setProductUrl(productUrl);
            }catch(Exception e){
                log.error("【直邮发货列表请求原始品牌链接失败 url:{}  param:{} errorMsg:{} 】",ApiServiceUrlConfig.getQueryHubProduct(),content,e.getMessage());
                e.printStackTrace();
            }
            String picUrl = detail.getPicUrl();
            if(StringUtils.isEmpty(picUrl)) {
                try {
                    picUrl = getPicUrl(detail.getSkuNo());
                } catch (Exception ex) {
                    log.error("【直邮发货列表请求商品图片失败 skuNo:{} errorMsg:{} 】 ", detail.getSkuNo(), ex.getMessage());
                }
                if (picUrl != null) {
                    detail.setPicUrl(picUrl);
                } else {
                    detail.setPicUrl("");
                }
            }
        }
        return FastJsonUtil.serialize2StringEmpty(res);
    }


    /**
     *  第三步 获取物流公司信息数据
     * @return
     */
    @RequestMapping(value = "/findDirectSupplierLogisticsCompany", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseContentOne findDirectSupplierLogisticsCompany(@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
        if(userInfo==null){
            return ResponseContentOne.errorResp("3","");
        }
        String userNo = userInfo.getSopUserNo();
        Map<String, Object> map = new HashMap();
        map.put("SopUserNo", userNo);
        ResponseContentOne content = purOrderService.findDirectSupplierLogisticsCompany(map);
        return content;
    }

    /**
     *  第三步 采购单录入国内段物流信息
     * @param sopDeliverOrderNo 发货单编号
     * @param logisticsOrderNo 物流单号
     * @param logisticsName 物流公司名称
     * @param logisticsCompanyNo 物流公司编号
     * @param userInfo
     * @return
     */
    @RequestMapping(value = "/entryDirectDorderDomesticLogistics", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseContentOne entryDirectDorderDomesticLogistics(String sopDeliverOrderNo , String logisticsOrderNo,
                                                                 String logisticsName,String logisticsCompanyNo,
                                                                 @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {
        if(userInfo==null){
            return ResponseContentOne.errorResp("3","");
        }
        if(StringUtils.isEmpty(logisticsName)||StringUtils.isEmpty(sopDeliverOrderNo)){
            return ResponseContentOne.errorParam();
        }
        String userNo = userInfo.getSopUserNo();
        Map<String, Object> map = new HashMap();
        map.put("SopUserNo", userNo);
        map.put("SopDeliverOrderNo", sopDeliverOrderNo);
        map.put("LogisticsOrderNo", logisticsOrderNo);
        map.put("LogisticsName", logisticsName);
        map.put("LogisticsCompanyNo", logisticsCompanyNo);
        ResponseContentOne content = purOrderService.entryDirectDorderDomesticLogistics(map);
        return content;
    }

    /**
     * 发货单详情
     * @param sopDeliverOrderNo 发货单号
     * @return
     */
    @RequestMapping(value = "/{sopDeliverOrderNo}/detail")
    public ResponseContentOne findDirectDorderDetail(@PathVariable("sopDeliverOrderNo")String sopDeliverOrderNo, @ModelAttribute(Constants.SESSION_USER) UserInfo user) {
        if(user==null){
            return ResponseContentOne.errorResp("3","");
        }
        ResponseContentOne<PurchaseOrderDetail> result = findOrderService.findDirectDorderDetail(sopDeliverOrderNo,user.getSopUserNo(),user.getSupplierNo());
        PurchaseOrderDetail detail = result.getContent();

        String picUrl = detail.getPicUrl();
        if(StringUtils.isEmpty(picUrl)){
            try{
                picUrl = getPicUrl(detail.getSkuNo());
            }catch(Exception ex){
            }
            if(picUrl!=null){
                detail.setPicUrl(picUrl);
            }else{
                detail.setPicUrl("");
            }
        }
        return result;
    }

    /**
     * 发货单详情
     * @param purchaseOrderNo 采购订单号
     * @param user
     * @return
     */
    @RequestMapping(value = "/{purchaseOrderNo}/findDirectPorderDetail")
    public ResponseContentOne findDirectPorderDetail(@PathVariable("purchaseOrderNo") String purchaseOrderNo, @ModelAttribute(Constants.SESSION_USER) UserInfo user) {
        if(user==null){
            return ResponseContentOne.errorResp("3","");
        }
        ResponseContentOne<PurchaseOrderDetail> result = findOrderService.findDirectPorderDetail(purchaseOrderNo,user.getSopUserNo());
        PurchaseOrderDetail detail = result.getContent();
        String picUrl = detail.getPicUrl();
        if(StringUtils.isEmpty(picUrl)){
            try{
                picUrl = getPicUrl(detail.getSkuNo());
            }catch(Exception ex){
            }
            if(picUrl!=null){
                detail.setPicUrl(picUrl);
            }else{
                detail.setPicUrl("");
            }
        }

        return result;
    }

    /**
     * 查询直发供应商 物流段信息
     * @return
     */
    @RequestMapping(value = "/findSupplierSegments")
    public ResponseContentOne<SupplierSegments> findSupplierSegments(@ModelAttribute(Constants.SESSION_USER) UserInfo user) {
        if(user==null){
            return ResponseContentOne.errorResp("3","");
        }
        ResponseContentOne<SupplierSegments> result = findOrderService.findSupplierSegments(user);
        return result;
    }

    /**
     * 组装请求参数（接口所需的格式）
     *
     * @param params
     * @return
     */
    private String[] convert(String params) {
        if (StringUtils.isEmpty(params)) {
            return new String[] {};
        }
        if (params.indexOf("_") > -1) {
            return params.split("\\_");
        }
        return new String[] { params };
    }


    private String getPicUrl(String  sku) throws Exception{
        String url = null;
        String requestPicUrl = ApiServiceUrlConfig.getPicHost()+"/ListingCatalog/getPicListBySkuNoList?skuNoList="+sku;
        log.info("【请求商品图片 skuPicUrl:{} 】",requestPicUrl);
        String json = HttpClientUtil.doGet(requestPicUrl);
        PictureObj obj = new Gson().fromJson(json, PictureObj.class);
        if(obj!=null){
            if(obj.getContent()!=null){
                if(obj.getContent().getList()!=null&&!obj.getContent().getList().isEmpty()){
                    url = obj.getContent().getList().get(0).getPicUrl();
                }
            }
        }
        log.info("【请求商品图片 skuPicUrl:{}  result {}】",requestPicUrl,url);
        return url;
    }

    /***
     * 直邮第一步 采购单库存标记数据导出
     *
     * @param purchase
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/inventoryExport")
    public String porderCheck(PurchaseRequest purchase, HttpServletRequest request, boolean flag,HttpServletResponse response) throws Exception {
        long startTime = new Date().getTime();
        String pageIndex = purchase.getPageIndex();
        String pageSize = purchase.getPageSize();
        String detailStatus = (String) purchase.getDetailStatus();
        String isStock = (String) purchase.getIsStock();
        if (StringUtils.isEmpty(pageIndex) || StringUtils.isEmpty(pageSize)) {
            return FastJsonUtil.serialize2String(ResponseContentOne.errorParam());
        }
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(Constants.SESSION_USER);
        String userNo = userInfo.getSopUserNo();
        String supplierNo = userInfo.getSupplierNo();

        if(flag){
            purchase.setDetailStatus(null);
            purchase.setIsDoStock("0");
        }else{
            purchase.setDetailStatus(this.convert(detailStatus));
        }

        purchase.setIsStock(this.convert(isStock));
        purchase.setSopUserNo(userNo);
        purchase.setSupplierNo(supplierNo);
        if(purchase.getImportType()==null){
            purchase.setImportType("0");
        }
        purchase.setUpdateTimeBegin(DateFormat.TimeFormatChangeToString(purchase.getUpdateTimeBegin(), "dd-MM-yyyy", "yyyy-MM-dd"));
        purchase.setUpdateTimeEnd(DateFormat.TimeFormatChangeToString(purchase.getUpdateTimeEnd(), "dd-MM-yyyy", "yyyy-MM-dd"));
        if (purchase.getUpdateTimeBegin()==null||purchase.getUpdateTimeEnd()==null) {
            purchase.setUpdateTimeBegin("");
            purchase.setUpdateTimeEnd("");
        }
        ResponseContentOne<PurchaseOrders> purchaseOrdersResult = purOrderService.findDirectPorderbyPage(purchase);
        log.info("查询导出订单列表总耗时："+(new Date().getTime()-startTime));
        response.setContentType("application/x-download");// 设置为下载application/x-download
        OutputStream out = response.getOutputStream();
        String fileName = "Stock Check (1st time)";
        if (null != purchaseOrdersResult) {
            PurchaseOrders pOrders = purchaseOrdersResult.getContent();
            //采购单列表
            List<PurchaseOrderDetail> purchaseOrderDetails = pOrders.getPurchaseOrderDetails();
            List<HashMap<String, Object>> result = converPurchaseOrderDetailsToExcelData(purchaseOrderDetails);
            if (purchase.getUpdateTimeBegin()!=null&&purchase.getUpdateTimeEnd()!=null&&!purchase.getUpdateTimeEnd().equals("")&&!purchase.getUpdateTimeBegin().equals("")) {
                fileName+= DateUtil.date2String(DateUtil.stringToDate(purchase.getUpdateTimeBegin(), "yyyy-MM-dd"), "MM-dd-yyyy")+" to "+DateUtil.date2String(DateUtil.stringToDate(purchase.getUpdateTimeEnd(), "yyyy-MM-dd"), "MM-dd-yyyy");
            }else{
                fileName+="All";
            }
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(fileName.getBytes("gb2312"), "ISO8859-1")
                    + ".xls");
            if (result != null && result.size() > 0) {
                String[] headers = {"Brand", "Order Code",  "Supplier SKU", "Item Code",
                                    "Bar Code","PID",  "Item Name", "Size", "Price",
                                    "Shangpin Code", "Date", "Qty", "Stock","sopPurchaseOrderDetailNo","Deadline" };

                String[] columns = { "brandName", "sopPurchaseOrderNo" , "supplierSkuNo", "productModel",
                        "barCode","pid",  "itemName", "size", "skuPrice",
                        "supplierOrderNo", "createTime", "qty", "stock","sopPurchaseOrderDetailNo","latestConfirmTime"};
                ExportExcelUtils.exportExcel(fileName, headers, columns, result,
                        out, "");
                out.close();
                return null;
            }
        }
        List<HashMap<String, Object>> result2 = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> result1=new HashMap<String, Object>();
        result1.put("oops", "Export failed. Please try again later.");
        result2.add(result1);
        String[] headers = { "Oops" };
        String[] columns = { "oops" };
        ExportExcelUtils.exportExcel(fileName, headers, columns, result2,
                out, "");
        out.close();
        return null;
    }


    private List<HashMap<String, Object>> converPurchaseOrderDetailsToExcelData(List<PurchaseOrderDetail> importRS){
        List<HashMap<String, Object>> excelDataMap = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<importRS.size();i++){

            HashMap<String, Object> result=new HashMap<String, Object>();
            result.put("brandName", importRS.get(i).getBrandName());
            result.put("sopPurchaseOrderNo", importRS.get(i).getSopPurchaseOrderNo());
            result.put("supplierSkuNo", importRS.get(i).getSupplierSkuNo());

            result.put("productModel", importRS.get(i).getProductModel()== null?"":importRS.get(i).getProductModel());
            result.put("barCode", importRS.get(i).getBarCode());
            result.put("pid", importRS.get(i).getPid());

            String itemName = importRS.get(i).getProductName()== null?"":importRS.get(i).getProductName();
            itemName += " ";
            itemName +=importRS.get(i).getColor()== null?"":importRS.get(i).getColor();
            result.put("itemName", itemName);

            result.put("size", importRS.get(i).getSize()== null?"":importRS.get(i).getSize());
            result.put("skuPrice", importRS.get(i).getSkuPrice()== null?"":importRS.get(i).getSkuPrice());
            result.put("supplierOrderNo", importRS.get(i).getSupplierOrderNo()== null?"":importRS.get(i).getSupplierOrderNo());

            result.put("createTime",importRS.get(i).getCreateTime() == null?"":importRS.get(i).getCreateTime());
            result.put("qty",importRS.get(i).getQty() == null?"":importRS.get(i).getQty());
            result.put("stock",importRS.get(i).getIsStock() == null?"0":importRS.get(i).getIsStock());

            result.put("sopPurchaseOrderDetailNo",importRS.get(i).getSopPurchaseOrderDetailNo() == null?"":importRS.get(i).getSopPurchaseOrderDetailNo());
            result.put("latestConfirmTime",importRS.get(i).getLatestConfirmTime() == null?"":importRS.get(i).getLatestConfirmTime());
            excelDataMap.add(result);
        }
        return excelDataMap;
    }

    /***
     * 直邮第一步 采购单库存标记数据导入
     *
     * @param uploadfile Excel
     * @param userInfo 用户登录信息
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/inventoryImport")
    public String inventoryImport(
            @RequestParam(value = "uploadfile", required = true) MultipartFile uploadfile,
            @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) throws Exception {
        InputStream is = uploadfile.getInputStream();
        // InputStream is = new FileInputStream("d:\\77.xls");
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);

        String userNo = userInfo.getSopUserNo();
        //请求入参
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                Map<String, String> map = new HashMap<String, String>();
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                map.put("SopUserNo", userNo);
                map.put("SopPurchaseOrderDetailNo", getValue(hssfRow.getCell(13)));
                try {
                    int temStock = new Float(Float
                            .parseFloat(getValue(hssfRow.getCell(12))))
                            .intValue();
                    if(temStock==0){ //如果当前商品的库存为 0（待确认）
                        continue;
                    }
                    map.put("IsStock", temStock+"");
                } catch (Exception e) {
                    map.put("IsStock", "0");
                }
                list.add(map);
            }

        }
        ResponseContentOne content = purOrderService.modifyDirectPorderStock(list);
        return FastJsonUtil.serialize2String(content);
    }



    @SuppressWarnings("static-access")
    private String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            // 返回字符串类型的值
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }


    /**
     * 取消采购单查询列表
     *
     * @param purchase
     *            请求参数实体
     * @return
     */
    @RequestMapping(value = "/findDirectPorderCanceledbyPage", produces = "text/html;charset=UTF-8", method = {
            RequestMethod.GET, RequestMethod.POST })
    public String findDirectPorderCanceledbyPage(PurchaseRequest purchase,HttpServletRequest request,boolean flag) {
        long startTime = new Date().getTime();
        String pageIndex = purchase.getPageIndex();
        String pageSize = purchase.getPageSize();
        String detailStatus = (String) purchase.getDetailStatus();
        String isStock = (String) purchase.getIsStock();
        if (StringUtils.isEmpty(pageIndex) || StringUtils.isEmpty(pageSize)) {
            return FastJsonUtil.serialize2String(ResponseContentOne
                    .errorParam());
        }
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(Constants.SESSION_USER);
        String userNo = userInfo.getSopUserNo();
        String supplierNo = userInfo.getSupplierNo();

        if(flag){
            purchase.setDetailStatus(null);
            purchase.setIsDoStock("0");
        }else{
            purchase.setDetailStatus(this.convert(detailStatus));
        }

        purchase.setIsStock(this.convert(isStock));
        purchase.setSopUserNo(userNo);
        purchase.setSupplierNo(supplierNo);
        if(purchase.getImportType()==null){
            purchase.setImportType("0");
        }
        purchase.setUpdateTimeBegin(DateFormat.TimeFormatChangeToString(purchase.getUpdateTimeBegin(), "dd-MM-yyyy", "yyyy-MM-dd"));
        purchase.setUpdateTimeEnd(DateFormat.TimeFormatChangeToString(purchase.getUpdateTimeEnd(), "dd-MM-yyyy", "yyyy-MM-dd"));
        if (purchase.getUpdateTimeBegin()==null||purchase.getUpdateTimeEnd()==null) {
            purchase.setUpdateTimeBegin("");
            purchase.setUpdateTimeEnd("");
        }
        PurchaseOrders pOrders = purOrderService.findDirectPorderCanceledbyPage(purchase);
        if (null == pOrders) {
            return FastJsonUtil.serialize2String(ResponseContentOne
                    .errorResp("返回数据为空"));
        }
        //http://tms0.shangpin.com/Delivery/OrderManage/GetPrintDataBySupplierOrderNo?opType=0&supplierOrderNo=2016051208191
        if(pOrders!=null){
            List<PurchaseOrderDetail> list = pOrders.getPurchaseOrderDetails();
            if(list!=null&&!list.isEmpty()){
                for(PurchaseOrderDetail detail : list){
                    try{
                        String content = "{\"supplierId\":\""+userNo+"\",\"skuId\":\""+detail.getSupplierSkuNo()+"\"}";
                        log.info("请求原始品牌链接参数："+userNo+",skuId:"+detail.getSupplierSkuNo());
                        String productUrl = HttpClientUtil.doPostForJson(ApiServiceUrlConfig.getQueryHubProduct(), content);
                        log.info("请求原始品牌链接参数："+userNo+",skuId:"+detail.getSupplierSkuNo()+",返回结果："+productUrl);
                        detail.setProductUrl(productUrl);
                    }catch(Exception e){
                    }

                    String urlTMS = ApiServiceUrlConfig.getIp()+"/wayprint/printPIDAndSupplierOrderNo?supplierOrderNo="+detail.getSupplierOrderNo();
                    detail.setTmsUrl(urlTMS);
                }
            }else{
                pOrders.setPurchaseOrderDetails(new ArrayList<PurchaseOrderDetail>());
            }
        }
        ResponseContentOne<PurchaseOrders> result = ResponseContentOne
                .successResp(pOrders);
        log.info("查询订单列表总耗时："+(new Date().getTime()-startTime));
        return FastJsonUtil.serialize2StringEmpty(result);
    }


    /***
     * 直邮第三步 发货单数据导出
     *
     * @param deliveryOrdersRQ
     * @param uInfo
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/dorderPageExport")
    public String dorderPageExport(@Valid final DeliveryOrdersRQ deliveryOrdersRQ,
                                   @ModelAttribute(Constants.SESSION_USER) UserInfo uInfo,HttpServletResponse response) throws Exception {
        if(uInfo==null){
            return FastJsonUtil.serialize2StringEmpty(ResponseContentOne.errorResp("3",""));
        }
        deliveryOrdersRQ.setSopUserNo(uInfo.getSopUserNo());

        log.info("【直邮 发货列表页面 请求参数 deliveryOrdersRQ: {} 】",FastJsonUtil.serialize2StringEmpty(deliveryOrdersRQ));

        if (StringUtils.isEmpty(deliveryOrdersRQ.getStart()) || StringUtils.isEmpty(deliveryOrdersRQ.getLength())) {
            return FastJsonUtil.serialize2String(ResponseContentOne.errorParam());
        }
        // 请求数据
        ResponseContentOne< DirectDeliveryOrderRS > res =findOrderService.findDirectDorderbyPage(deliveryOrdersRQ);

        response.setContentType("application/x-download");// 设置为下载application/x-download
        OutputStream out = response.getOutputStream();
        String fileName = "Parcel In Transit (1st time)";
        if (null != res&&res.getContent()!=null) {
            List<DirectDeliverOrder> deliverOrder = res.getContent().getDeliverOrder();
            //发货单列表
            List<HashMap<String, Object>> result = converDirectDeliverOrderToExcelData(deliverOrder);
            if (deliveryOrdersRQ.getUpdateTimeBegin()!=null&&deliveryOrdersRQ.getUpdateTimeEnd()!=null&&!deliveryOrdersRQ.getUpdateTimeEnd().equals("")&&!deliveryOrdersRQ.getUpdateTimeBegin().equals("")) {
                fileName+= DateUtil.date2String(DateUtil.stringToDate(deliveryOrdersRQ.getUpdateTimeBegin(), "yyyy-MM-dd"), "MM-dd-yyyy")+" to "+DateUtil.date2String(DateUtil.stringToDate(deliveryOrdersRQ.getUpdateTimeEnd(), "yyyy-MM-dd"), "MM-dd-yyyy");
            }else{
                fileName+="All";
            }
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(fileName.getBytes("gb2312"), "ISO8859-1")
                    + ".xls");
            if (result != null && result.size() > 0) {
                String[] headers = {"Order Code",  "SupplierOrderNo", "Brand", "Category1",
                        "Category","Material",  "Qty", "Price",
                        "Tracking Number1", "Tracking Number2"};

                String[] columns = { "sopPurchaseOrderNo", "supplierOrderNo" , "brandName", "categoryName",
                        "categoryNameEn","material",  "deliveryCount", "skuPrice","overseasLogisticsNO", "logisticsOrderNo"};
                ExportExcelUtils.exportExcel(fileName, headers, columns, result,
                        out, "");
                out.close();
                return null;
            }
        }
        List<HashMap<String, Object>> result2 = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> result1=new HashMap<String, Object>();
        result1.put("oops", "Export failed. Please try again later.");
        result2.add(result1);
        String[] headers = { "Oops" };
        String[] columns = { "oops" };
        ExportExcelUtils.exportExcel(fileName, headers, columns, result2,
                out, "");
        out.close();
        return null;
    }

    /**
     * 将发货单数据转换为excel 导出数据
     * @param deliverOrder
     * @return
     */
    private List<HashMap<String, Object>> converDirectDeliverOrderToExcelData(List<DirectDeliverOrder> deliverOrder) {


        List<HashMap<String, Object>> excelDataMap = new ArrayList<HashMap<String, Object>>();
        for(int i=0;i<deliverOrder.size();i++){
            DirectDeliverOrder directDeliverOrder = deliverOrder.get(i);

            HashMap<String, Object> result=new HashMap<String, Object>();
            result.put("sopPurchaseOrderNo", directDeliverOrder.getSopPurchaseOrderNo());
            result.put("supplierOrderNo", directDeliverOrder.getSupplierOrderNo());
            result.put("brandName", directDeliverOrder.getBrandName());

            result.put("categoryName",directDeliverOrder.getCategoryName() == null?"":directDeliverOrder.getCategoryName());
            result.put("categoryNameEn",directDeliverOrder.getCategoryNameEn() == null?"":directDeliverOrder.getCategoryNameEn());
            result.put("material",directDeliverOrder.getMaterial() == null?"":directDeliverOrder.getMaterial());
            result.put("deliveryCount",directDeliverOrder.getDeliveryCount()+"" == null?"":directDeliverOrder.getDeliveryCount()+"");
            result.put("skuPrice",directDeliverOrder.getSkuPrice()+"" == null?"":directDeliverOrder.getSkuPrice());
            result.put("overseasLogisticsNO",directDeliverOrder.getOverseasLogisticsNO()+"" == null?"":directDeliverOrder.getOverseasLogisticsNO());
            result.put("logisticsOrderNo",directDeliverOrder.getLogisticsOrderNo()+"" == null?"":directDeliverOrder.getLogisticsOrderNo());
            excelDataMap.add(result);
        }
        return excelDataMap;
    }


    public static void main(String[] args) {
        String json = "{\"code\":\"0\",\"msg\":\"\",\"content\":{\"list\":[{\"skuNo\":\"31062943002\",\"picUrl\":\"https://pic3.shangpin.com/f/p/18/10/09/20181009135803313866-400-400.jpg\"},{\"skuNo\":\"31062943002\",\"picUrl\":\"https://pic6.shangpin.com/f/p/18/10/09/20181009135805094717-400-400.jpg\"},{\"skuNo\":\"31062943002\",\"picUrl\":\"https://pic1.shangpin.com/f/p/18/10/09/20181009135806813682-400-400.jpg\"},{\"skuNo\":\"31062943002\",\"picUrl\":\"https://pic6.shangpin.com/f/p/18/10/09/20181009135808469202-400-400.jpg\"},{\"skuNo\":\"31062943002\",\"picUrl\":\"https://pic6.shangpin.com/f/p/18/10/09/20181009135810282057-400-400.jpg\"}]}}" ;
        log.info(" 请求商品 skuPicUrl : "+json);
        PictureObj obj = new Gson().fromJson(json, PictureObj.class);
        if(obj!=null){
            if(obj.getContent()!=null){
                if(obj.getContent().getList()!=null&&!obj.getContent().getList().isEmpty()){
                  String   url = obj.getContent().getList().get(0).getPicUrl();
                    System.out.println(url);
                }
            }
        }
    }

}

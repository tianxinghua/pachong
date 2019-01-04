package com.shangpin.api.airshop.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.barcodelib.barcode.Linear;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.dto.TMSTrack;
import com.shangpin.api.airshop.dto.UserInfo;
import com.shangpin.api.airshop.service.TMSFZFService;
import com.shangpin.api.airshop.service.TMSService;
import com.shangpin.api.airshop.util.Constants;
import com.shangpin.api.airshop.util.ExportExcelUtils;
import com.shangpin.api.airshop.util.HttpUtil45;
import com.shangpin.api.airshop.util.OutTimeConfig;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@RestController
@RequestMapping("/waybillFZF")
@SessionAttributes(Constants.SESSION_USER)
public class TMSFZFController {
	private static Logger logger = LoggerFactory.getLogger(TMSFZFController.class);
	@Autowired
	private TMSFZFService tmsfzfService;

	/**
	 * TMS查询批次列表操作
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public String getTrackList(TMSTrack tmsTrack,
			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {

		return tmsfzfService.getTrackList(tmsTrack, userInfo.getSupplierNo());
	}
	/**
	 * TMSFZF查询批次明细
	 * */
	@RequestMapping(value = "/{taskBatchNo}/{importType}/detail", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getTrackDetail(
			@PathVariable("taskBatchNo") String taskBatchNo,
			@PathVariable("importType") String importType,
			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {

		return tmsfzfService.getTrackDetail(taskBatchNo, userInfo.getSopUserNo(),importType);
	}
	/**
	 * TMSFZF创建批次操作
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

		return tmsfzfService.createTrack(userInfo.getSupplierNo(),
				userInfo.getSopUserNo(), supplierOrders, sopPurchaseOrderNo,
				opUser);
	}
}

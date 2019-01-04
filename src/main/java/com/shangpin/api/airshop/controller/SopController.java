package com.shangpin.api.airshop.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.shangpin.api.airshop.dto.DeliverAddress;
import com.shangpin.api.airshop.dto.UserInfo;
import com.shangpin.api.airshop.supplier.service.SupplierService;
import com.shangpin.api.airshop.util.Constants;

@RestController
@RequestMapping("/sop")
@SessionAttributes(Constants.SESSION_USER)
public class SopController {
	private static Logger logger = LoggerFactory.getLogger(SopController.class);
	  @Autowired
	    SupplierService supplierService;
//	  @Autowired
//	  TMSService tmsService;
	
	/**TMS出入库操作
	 * @param sopUserNo
	 * @param supplierSkuNo
	 * @param productModel
	 * @param skuNo
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
//	@RequestMapping(value="/inAndOutBound")
//	@ResponseBody
//	public String saveInAndOutBound(@RequestParam(value="supplierOrderNo",defaultValue="") String supplierOrderNo
//			,@RequestParam(value="opUser",defaultValue="") String opUser
//			,@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo){
//		
////			return tmsService.saveInAndOutBound(userInfo.getSopUserNo(), supplierOrderNo, opUser);
//	}
//	/**TMS出入库操作
//	 * @param sopUserNo
//	 * @param supplierSkuNo
//	 * @param productModel
//	 * @param skuNo
//	 * @param pageIndex
//	 * @param pageSize
//	 * @return
//	 */
//	@RequestMapping(value="/createTrack")
//	@ResponseBody
//	public String createTrack(@RequestParam(value="supplierOrders",defaultValue="") String supplierOrders
//			,@RequestParam(value="opUser",defaultValue="") String opUser
//			,@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo){
////		tmsService.createTrack(userInfo.getSopUserNo(), supplierOrders, opUser)
//			return null;
//	}
//	
//	/**TMS出入库操作
//	 * @param sopUserNo
//	 * @param supplierSkuNo
//	 * @param productModel
//	 * @param skuNo
//	 * @param pageIndex
//	 * @param pageSize
//	 * @return
//	 */
////	@RequestMapping(value="/list")
////	@ResponseBody
////	public String getTrackList(TMSTrack tmsTrack
////			,@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo){
////		
////			 return tmsService.getTrackList(tmsTrack);
////	}
//	
//	@RequestMapping(value = "/{taskBatchNo}/detail", produces = "text/html;charset=UTF-8")
//	@ResponseBody
//	public String getTrackDetail(@PathVariable("taskBatchNo") String taskBatchNo,
//			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo){
//		
//			 return tmsService.getTrackDetail(taskBatchNo,userInfo.getSupplierNo());
//	}
    /**
     * 获取供应商信息
     * @param captcha 验证码
     */
    @RequestMapping(value = "/getSupplierInfo", method = RequestMethod.GET)
    public String getSupplierInfo(HttpServletRequest request) throws Exception {
    	UserInfo userInfo = (UserInfo) request.getSession().getAttribute(Constants.SESSION_USER);
        //不抛出异常 验证通过
    	String json = supplierService.getSupplierInfo(userInfo.getSopUserNo()); 
        return json;
    }
    
    /**
     * 获取返货地址
     * @param captcha 验证码
     */
    @RequestMapping(value = "/getReturnAddress", method = RequestMethod.GET)
    public String getReturnAddress(HttpServletRequest request) throws Exception {
    	UserInfo userInfo = (UserInfo) request.getSession().getAttribute(Constants.SESSION_USER);
        //不抛出异常 验证通过
    	String json = supplierService.getReturnAddress(userInfo.getSupplierNo()); 
        return json;
    }
    
    /**
     * 获取发货地址
     * @param captcha 验证码 
     */
    @RequestMapping(value = "/getSupplierDeliverAddress", method = RequestMethod.GET)
    public String getSupplierDeliverAddress(HttpServletRequest request) throws Exception {
    	UserInfo userInfo = (UserInfo) request.getSession().getAttribute(Constants.SESSION_USER);
        //不抛出异常 验证通过
    	String json = supplierService.getSupplierDeliverAddress(userInfo.getSopUserNo()); 
        return json;
    }
    
    /**
     * 修改发货地址
     * @param captcha 验证码
     */
    @RequestMapping(value = "/modifySupplierDeliverAddress", method = RequestMethod.GET)
    public String modifySupplierDeliverAddress(HttpServletRequest request) throws Exception {
    	UserInfo userInfo = (UserInfo) request.getSession().getAttribute(Constants.SESSION_USER);
//        //不抛出异常 验证通过
    	DeliverAddress deliver = new DeliverAddress();
//    	SopDeliverAddressEntity
//    	DeliverAddress
//    	UpdateTime
//    	UpdateUserName
//    	SopUserNo
//    	String json = supplierService.modifySupplierDeliverAddress(userInfo.getSopUserNo()); 
        return null;
    }
}

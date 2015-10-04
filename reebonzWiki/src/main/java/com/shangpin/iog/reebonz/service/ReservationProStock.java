package com.shangpin.iog.reebonz.service;

import net.sf.json.JSONObject;

import org.springframework.dao.DuplicateKeyException;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.reebonz.dto.Items;
import com.shangpin.iog.reebonz.dto.ResponseObject;
import com.shangpin.iog.reebonz.util.MyJsonUtil;

public class ReservationProStock {
	
	/*
	 * order_id - Order Id. (mandatory)
	2. order_site - Order Site. (mandatory)
	3. data - List of products to reserve. (mandatory)
	 * */
	    public void lockStock(String order_id,String order_site,String data) throws ServiceException {
	    	
	    	String json = MyJsonUtil.lockStock(order_id,order_site,data);
	    	if(json!=null){
				ResponseObject obj = new Gson().fromJson(json, ResponseObject.class);
				if(obj!=null){
					String result = obj.getReturn_code();
					if("1".equals(result)){
						String reservationId = obj.getReservation_id();
						System.out.println("锁库存success"+reservationId);
					}else if("-1".equals(result)){
						System.out.println("库存不足");
					}else if("0".equals(result)){
						System.out.println("库存被锁");
					}
				}
			}
	    }
	    
	    /*
	     *  1. reservation_id - Reservation id. (mandatory)
			2. order_id - Order id. (mandatory)
			3. user_id - Order id. (optional)
			4. confirmation_code - deducted" (for confirmation) "voided" (for reversal)
	     * */
	    public void unlockStock(String reservation_id,String order_id,String user_id,String confirmation_code) throws ServiceException {
	    	
	    	String json = MyJsonUtil.unlockStock(reservation_id,order_id,user_id,confirmation_code);
	    	if(json!=null){
				ResponseObject obj = new Gson().fromJson(json, ResponseObject.class);
				if(obj!=null){
					String result = obj.getReturn_code();
					if("1".equals(result)){
						System.out.println("锁库存success");
					}else if("-1".equals(result)){
						System.out.println("库存不足");
					}
				}
			}
	    }

}

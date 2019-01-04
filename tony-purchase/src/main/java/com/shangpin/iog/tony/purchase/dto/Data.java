package com.shangpin.iog.tony.purchase.dto;

import com.google.gson.Gson;
import com.shangpin.iog.ice.dto.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Data {
	
	private String message;

	public static void main(String[] args) {
		Gson gson = new Gson();
		 String rtnData ="{\"status\":\"ko\",\"messages\":[\"Quota exceeded\"]}";
//       String rtnData = "{\"status\":\"ok\",\"data\":{\"message\":\"order successfully updated\"}}";
        ReturnDataDTO returnDataDTO = gson.fromJson(rtnData,ReturnDataDTO.class);
       
        
        
        if ("ko".equals(returnDataDTO.getStatus())){
        	String mes = returnDataDTO.getMessages().toString();
        	System.out.println(mes);
        	if("[Quota exceeded]".equals(returnDataDTO.getMessages())){
        		 System.out.println(returnDataDTO.getMessages());
        	}else{
        		 System.out.println(returnDataDTO.getMessages());
        	}
//
        }
	}
}

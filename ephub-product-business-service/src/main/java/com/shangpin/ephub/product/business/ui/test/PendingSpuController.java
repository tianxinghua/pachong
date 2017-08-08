package com.shangpin.ephub.product.business.ui.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title:PendingProductController </p>
 * <p>Description: 待处理页面</p>
 * <p>Company: www.shangpin.com</p>
 * @author lubaijiang
 * @date 2016年12月21日 下午5:15:48
 *
 */
@RestController
@RequestMapping("/hub-spu-pending")
@Slf4j
public class PendingSpuController {

    @RequestMapping(value="/notOnShelfList",method=RequestMethod.POST)
    public HubResponse<?> pendingList(@RequestBody PendingQuryDto pendingQuryDto){
    	Page pgae = new Page();
    	pgae.setTotal(10);
    	List<JSONObject> list = new ArrayList<JSONObject>();
    	
    	JSONObject json = new JSONObject();
    	json.put("spuName","test");
    	json.put("brandName","TODS");
    	json.put("supplierSpuNo","123456");
    	json.put("spuModel","123456");
    	json.put("errorDesc","errorDesc");
    	list.add(json);
    	pgae.setList(list);
        return HubResponse.successResp(pgae);
    }
}

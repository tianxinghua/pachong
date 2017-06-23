package com.shangpin.ephub.client.data.mysql.studio.spusupplierextend.gateway;


import com.shangpin.ephub.client.data.mysql.studio.spusupplierextend.dto.SlotSpuSupplierExtendQueryDto;
import com.shangpin.ephub.client.data.mysql.studio.spusupplierextend.result.HubSlotSpuSupplierExtend;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * <p>Title: HubSupplierPriceGateWay</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年4月6日 上午11:02:36
 *
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSlotSpuSupplierExtendGateWay {

	@RequestMapping(value = "/hub-slot-spu-supplier/select-by-query", method = RequestMethod.POST,consumes = "application/json")
	public List<HubSlotSpuSupplierExtend> selectByQuery(@RequestBody SlotSpuSupplierExtendQueryDto priceQueryDto);
	
	@RequestMapping(value = "/hub-slot-spu-supplier/count-by-query", method = RequestMethod.POST,consumes = "application/json")
	public int countByQuery(@RequestBody SlotSpuSupplierExtendQueryDto priceQueryDto);
}

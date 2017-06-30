package com.shangpin.ephub.client.data.mysql.studio.spusupplierunion.gateway;

import com.shangpin.ephub.client.data.mysql.studio.spusupplierunion.dto.SlotSpuSupplier;
import com.shangpin.ephub.client.data.mysql.studio.spusupplierunion.dto.SpuSupplierQueryDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by loyalty on 17/6/30.
 */
@FeignClient("ephub-data-mysql-service")
public interface SpuSupplierUnionGateWay {

    @RequestMapping(value="/spu-supplier-search/list", method = RequestMethod.POST,consumes = "application/json")
    public List<SlotSpuSupplier> listByQuery(@RequestBody SpuSupplierQueryDto queryDto);

    @RequestMapping(value="/spu-supplier-search/count-by-query", method = RequestMethod.POST,consumes = "application/json")
    public int countByQuery(@RequestBody SpuSupplierQueryDto queryDto)  ;

}

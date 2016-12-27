package com.shangpin.ephub.client.data.mysql.service.gateway;

import com.shangpin.ephub.client.data.mysql.service.dto.SpuPendingAuditVO;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuWithCriteriaDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 *
 * 待审核处理
 *
 */
@FeignClient("ephub-data-mysql-service")
public interface PengdingToHubGateWay {

	@RequestMapping(value = "/pending-hub/audit", method = RequestMethod.POST,consumes = "application/json")
    public boolean auditPending(@RequestBody SpuPendingAuditVO auditVO);
	

	

}

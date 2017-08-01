package com.shangpin.ephub.client.data.mysql.spu.gateway;

import com.shangpin.ephub.client.data.mysql.spu.dto.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
    spupending 无法处理的原因
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSpuPendingNohandleReasonGateWay {

	@RequestMapping(value = "/hub-spu-pending-nohandle/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSpuPendingNohandleReasonCriteriaDto criteria);

	@RequestMapping(value = "/hub-spu-pending-nohandle/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSpuPendingNohandleReasonCriteriaDto criteria);

	@RequestMapping(value = "/hub-spu-pending-nohandle/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long id );

	@RequestMapping(value = "/hub-spu-pending-nohandle/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubSpuPendingNohandleReasonDto objDto);

	@RequestMapping(value = "/hub-spu-pending-nohandle/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubSpuPendingNohandleReasonDto objDto);

	@RequestMapping(value = "/hub-spu-pending-nohandle/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSpuPendingNohandleReasonDto> selectByCriteriaWithRowbounds(@RequestBody HubSpuPendingNohandleReasonCriteriaWithRowBoundsDto criteriaWithRowBounds);

	@RequestMapping(value = "/hub-spu-pending-nohandle/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSpuPendingNohandleReasonDto> selectByCriteria(@RequestBody HubSpuPendingNohandleReasonCriteriaDto criteria);

	@RequestMapping(value = "/hub-spu-pending-nohandle/select-by-primary-key/{id}", method = RequestMethod.POST,consumes = "application/json")
    public HubSpuPendingNohandleReasonDto selectByPrimaryKey(@PathVariable(name = "id") Long id);
	
	@RequestMapping(value = "/hub-spu-pending-nohandle/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSpuPendingNohandleReasonWithCriteriaDto withCriteriaDto);
	
	@RequestMapping(value = "/hub-spu-pending-nohandle/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSpuPendingNohandleReasonWithCriteriaDto withCriteriaDto);
	
	@RequestMapping(value = "/hub-spu-pending-nohandle/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSpuPendingNohandleReasonDto objDto);
	
	@RequestMapping(value = "/hub-spu-pending-nohandle/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSpuPendingNohandleReasonDto objDto);

    @RequestMapping(value = "/hub-spu-pending-nohandle/count-distinct-brandno-and-spumodel-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countDistinctBrandNoAndSpuModelByCriteria(@RequestBody HubSpuPendingNohandleReasonCriteriaDto criteria);
}

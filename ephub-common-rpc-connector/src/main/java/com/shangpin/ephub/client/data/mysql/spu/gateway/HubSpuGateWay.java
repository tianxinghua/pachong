package com.shangpin.ephub.client.data.mysql.spu.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuWithCriteriaDto;
/**
 * <p>Title:HubSpuController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:18:04
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSpuGateWay {

	@RequestMapping(value = "/hub-spu/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-spu/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-spu/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long spuId);
	
	@RequestMapping(value = "/hub-spu/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubSpuDto hubSpu);
	
	@RequestMapping(value = "/hub-spu/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubSpuDto hubSpu);
	
	@RequestMapping(value = "/hub-spu/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSpuDto> selectByCriteriaWithRowbounds(@RequestBody HubSpuCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-spu/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSpuDto> selectByCriteria(@RequestBody HubSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-spu/select-by-primary-key/{spuId}", method = RequestMethod.POST,consumes = "application/json")
    public HubSpuDto selectByPrimaryKey(@PathVariable Long spuId);
	
	@RequestMapping(value = "/hub-spu/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSpuWithCriteriaDto hubSpuWithCriteria);
	
	@RequestMapping(value = "/hub-spu/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSpuWithCriteriaDto hubSpuWithCriteria);
	
	@RequestMapping(value = "/hub-spu/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSpuDto hubSpu);
	
	@RequestMapping(value = "/hub-spu/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSpuDto hubSpu);

    @RequestMapping(value = "/hub-spu/get-max-spu-no", method = RequestMethod.POST,consumes = "application/json")
	public String getMaxSpuNo();


    @RequestMapping(value = "/hub-spu/get-spuno", method = RequestMethod.POST,consumes = "application/json")
    public String getSpuNo();
}

package com.shangpin.ephub.client.data.mysql.material.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicDto;
import com.shangpin.ephub.client.data.mysql.material.dto.HubMaterialDicWithCriteriaDto;

/**
 * <p>Title:HubMaterialDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月15日 下午3:02:13
 */
@FeignClient("ephub-data-mysql-service")
public interface HubMaterialDicGateWay {

	
	@RequestMapping(value = "/hub-material-dic/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubMaterialDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-material-dic/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubMaterialDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-material-dic/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long materialDicId);
	
	@RequestMapping(value = "/hub-material-dic/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubMaterialDicDto hubMaterialDic);
	
	@RequestMapping(value = "/hub-material-dic/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubMaterialDicDto hubMaterialDic);
	
	@RequestMapping(value = "/hub-material-dic/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubMaterialDicDto> selectByCriteriaWithRowbounds(@RequestBody HubMaterialDicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-material-dic/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubMaterialDicDto> selectByCriteria(@RequestBody HubMaterialDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-material-dic/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubMaterialDicDto selectByPrimaryKey(Long materialDicId);
	
	@RequestMapping(value = "/hub-material-dic/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubMaterialDicWithCriteriaDto hubMaterialDicWithCriteria);
	
	@RequestMapping(value = "/hub-material-dic/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubMaterialDicWithCriteriaDto hubMaterialDicWithCriteria);
	
	@RequestMapping(value = "/hub-material-dic/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubMaterialDicDto hubMaterialDic);
	
	@RequestMapping(value = "/hub-material-dic/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubMaterialDicDto hubMaterialDic);
}

package com.shangpin.ephub.client.data.mysql.season.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicWithCriteriaDto;
/**
 * <p>Title:HubSeasonDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:37:10
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSeasonDicGateWay {

	
	@RequestMapping(value = "/hub-season-dic/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSeasonDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-season-dic/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSeasonDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-season-dic/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long seasonDicId);
	
	@RequestMapping(value = "/hub-season-dic/insert", method = RequestMethod.POST,consumes = "application/json")
    public int insert(@RequestBody HubSeasonDicDto hubSeasonDic);
	
	@RequestMapping(value = "/hub-season-dic/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public int insertSelective(@RequestBody HubSeasonDicDto hubSeasonDic);
	
	@RequestMapping(value = "/hub-season-dic/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSeasonDicDto> selectByCriteriaWithRowbounds(@RequestBody HubSeasonDicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-season-dic/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSeasonDicDto> selectByCriteria(@RequestBody HubSeasonDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-season-dic/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubSeasonDicDto selectByPrimaryKey(Long seasonDicId);
	
	@RequestMapping(value = "/hub-season-dic/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSeasonDicWithCriteriaDto hubSeasonDicWithCriteria);
	
	@RequestMapping(value = "/hub-season-dic/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSeasonDicWithCriteriaDto hubSeasonDicWithCriteria);
	
	@RequestMapping(value = "/hub-season-dic/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSeasonDicDto hubSeasonDic);
	
	@RequestMapping(value = "/hub-season-dic/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSeasonDicDto hubSeasonDic);
}

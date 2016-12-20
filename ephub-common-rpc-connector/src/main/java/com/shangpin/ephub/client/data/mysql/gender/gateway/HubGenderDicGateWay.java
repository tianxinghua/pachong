package com.shangpin.ephub.client.data.mysql.gender.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicDto;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicWithCriteriaDto;
/**
 * <p>Title:HubGenderDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午4:30:46
 */
@FeignClient("ephub-data-mysql-service")
public interface HubGenderDicGateWay {

	
	@RequestMapping(value = "/hub-gender-dic/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubGenderDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-gender-dic/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubGenderDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-gender-dic/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long genderDicId);
	
	@RequestMapping(value = "/hub-gender-dic/insert", method = RequestMethod.POST,consumes = "application/json")
    public int insert(@RequestBody HubGenderDicDto hubGenderDic);
	
	@RequestMapping(value = "/hub-gender-dic/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public int insertSelective(@RequestBody HubGenderDicDto hubGenderDic);
	
	@RequestMapping(value = "/hub-gender-dic/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubGenderDicDto> selectByCriteriaWithRowbounds(@RequestBody HubGenderDicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-gender-dic/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubGenderDicDto> selectByCriteria(@RequestBody HubGenderDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-gender-dic/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubGenderDicDto selectByPrimaryKey(Long genderDicId);
	
	@RequestMapping(value = "/hub-gender-dic/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubGenderDicWithCriteriaDto hubGenderDicWithCriteria);
	
	@RequestMapping(value = "/hub-gender-dic/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubGenderDicWithCriteriaDto hubGenderDicWithCriteria);
	
	@RequestMapping(value = "/hub-gender-dic/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubGenderDicDto hubGenderDic);
	
	@RequestMapping(value = "/hub-gender-dic/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubGenderDicDto hubGenderDic);
}

package com.shangpin.ephub.client.data.mysql.gender.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
public interface HubGenderDicController {

	
	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubGenderDicCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubGenderDicCriteriaDto criteria);
	
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long genderDicId);
	
	@RequestMapping(value = "/insert")
    public int insert(@RequestBody HubGenderDicDto hubGenderDic);
	
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(@RequestBody HubGenderDicDto hubGenderDic);
	
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubGenderDicDto> selectByCriteriaWithRowbounds(@RequestBody HubGenderDicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/select-by-criteria")
    public List<HubGenderDicDto> selectByCriteria(@RequestBody HubGenderDicCriteriaDto criteria);
	
	@RequestMapping(value = "/select-by-primary-key")
    public HubGenderDicDto selectByPrimaryKey(Long genderDicId);
	
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubGenderDicWithCriteriaDto hubGenderDicWithCriteria);
	
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubGenderDicWithCriteriaDto hubGenderDicWithCriteria);
	
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubGenderDicDto hubGenderDic);
	
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubGenderDicDto hubGenderDic);
}

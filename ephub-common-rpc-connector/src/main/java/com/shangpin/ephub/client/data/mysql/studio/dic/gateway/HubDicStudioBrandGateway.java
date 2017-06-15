package com.shangpin.ephub.client.data.mysql.studio.dic.gateway;

import java.util.List;

import com.shangpin.ephub.client.data.mysql.studio.dic.dto.HubDicStudioBrandCriteriaDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.studio.dic.dto.HubDicStudioBrandCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.studio.dic.dto.HubDicStudioBrandDto;
import com.shangpin.ephub.client.data.mysql.studio.dic.dto.HubDicStudioBrandWithCriteriaDto;
/**
 * <p>Title:HubDicStudioBrandDtoGateway.java </p>
 * <p>Description: EPHUB品牌字典接口网关</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午1:46:35
 */
@FeignClient("ephub-data-mysql-service")
public interface HubDicStudioBrandGateway {

	@RequestMapping(value = "/hub-dic-studio-brand/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubDicStudioBrandCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-dic-studio-brand/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubDicStudioBrandCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-dic-studio-brand/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long brandDicId);
	
	@RequestMapping(value = "/hub-dic-studio-brand/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubDicStudioBrandDto HubDicStudioBrandDto);
	
	@RequestMapping(value = "/hub-dic-studio-brand/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubDicStudioBrandDto HubDicStudioBrandDto);
	
	@RequestMapping(value = "/hub-dic-studio-brand/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubDicStudioBrandDto> selectByCriteriaWithRowbounds(@RequestBody HubDicStudioBrandCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-dic-studio-brand/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubDicStudioBrandDto> selectByCriteria(@RequestBody HubDicStudioBrandCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-dic-studio-brand/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubDicStudioBrandDto selectByPrimaryKey(Long brandDicId);
	
	@RequestMapping(value = "/hub-dic-studio-brand/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubDicStudioBrandWithCriteriaDto HubDicStudioBrandDtoWithCriteria);
	
	@RequestMapping(value = "/hub-dic-studio-brand/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubDicStudioBrandWithCriteriaDto HubDicStudioBrandDtoWithCriteria);
	
	@RequestMapping(value = "/hub-dic-studio-brand/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubDicStudioBrandDto HubDicStudioBrandDto);
	
	@RequestMapping(value = "/hub-dic-studio-brand/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubDicStudioBrandDto HubDicStudioBrandDto);
}

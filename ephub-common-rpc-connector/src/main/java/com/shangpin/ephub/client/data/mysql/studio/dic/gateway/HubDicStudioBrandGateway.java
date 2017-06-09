package com.shangpin.ephub.client.data.mysql.studio.dic.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicWithCriteriaDto;
/**
 * <p>Title:HubBrandDicDtoGateway.java </p>
 * <p>Description: EPHUB品牌字典接口网关</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午1:46:35
 */
@FeignClient("ephub-data-mysql-service")
public interface HubDicStudioBrandGateway {

	@RequestMapping(value = "/hub-dic-studio-brand/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubBrandDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-dic-studio-brand/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubBrandDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-dic-studio-brand/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long brandDicId);
	
	@RequestMapping(value = "/hub-dic-studio-brand/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubBrandDicDto HubBrandDicDto);
	
	@RequestMapping(value = "/hub-dic-studio-brand/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubBrandDicDto HubBrandDicDto);
	
	@RequestMapping(value = "/hub-dic-studio-brand/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubBrandDicDto> selectByCriteriaWithRowbounds(@RequestBody HubBrandDicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-dic-studio-brand/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubBrandDicDto> selectByCriteria(@RequestBody HubBrandDicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-dic-studio-brand/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubBrandDicDto selectByPrimaryKey(Long brandDicId);
	
	@RequestMapping(value = "/hub-dic-studio-brand/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubBrandDicWithCriteriaDto HubBrandDicDtoWithCriteria);
	
	@RequestMapping(value = "/hub-dic-studio-brand/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubBrandDicWithCriteriaDto HubBrandDicDtoWithCriteria);
	
	@RequestMapping(value = "/hub-dic-studio-brand/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubBrandDicDto HubBrandDicDto);
	
	@RequestMapping(value = "/hub-dic-studio-brand/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubBrandDicDto HubBrandDicDto);
}

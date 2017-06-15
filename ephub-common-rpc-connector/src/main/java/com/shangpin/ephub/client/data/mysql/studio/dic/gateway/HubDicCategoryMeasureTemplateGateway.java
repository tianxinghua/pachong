package com.shangpin.ephub.client.data.mysql.studio.dic.gateway;

import java.util.List;

import com.shangpin.ephub.client.data.mysql.studio.dic.dto.HubDicCategoryMeasureTemplateCriteriaDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.studio.dic.dto.HubDicCategoryMeasureTemplateCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.studio.dic.dto.HubDicCategoryMeasureTemplateDto;
import com.shangpin.ephub.client.data.mysql.studio.dic.dto.HubDicCategoryMeasureTemplateWithCriteriaDto;
/**
 * <p>Title:HubDicCategoryMeasureTemplateDtoGateway.java </p>
 * <p>Description: EPHUB品牌字典接口网关</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午1:46:35
 */
@FeignClient("ephub-data-mysql-service")
public interface HubDicCategoryMeasureTemplateGateway {

	@RequestMapping(value = "/hub-dic-category-measure-template/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubDicCategoryMeasureTemplateCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-dic-category-measure-template/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubDicCategoryMeasureTemplateCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-dic-category-measure-template/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long brandDicId);
	
	@RequestMapping(value = "/hub-dic-category-measure-template/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubDicCategoryMeasureTemplateDto HubDicCategoryMeasureTemplateDto);
	
	@RequestMapping(value = "/hub-dic-category-measure-template/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubDicCategoryMeasureTemplateDto HubDicCategoryMeasureTemplateDto);
	
	@RequestMapping(value = "/hub-dic-category-measure-template/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubDicCategoryMeasureTemplateDto> selectByCriteriaWithRowbounds(@RequestBody HubDicCategoryMeasureTemplateCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-dic-category-measure-template/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubDicCategoryMeasureTemplateDto> selectByCriteria(@RequestBody HubDicCategoryMeasureTemplateCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-dic-category-measure-template/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubDicCategoryMeasureTemplateDto selectByPrimaryKey(Long brandDicId);
	
	@RequestMapping(value = "/hub-dic-category-measure-template/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubDicCategoryMeasureTemplateWithCriteriaDto HubDicCategoryMeasureTemplateDtoWithCriteria);
	
	@RequestMapping(value = "/hub-dic-category-measure-template/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubDicCategoryMeasureTemplateWithCriteriaDto HubDicCategoryMeasureTemplateDtoWithCriteria);
	
	@RequestMapping(value = "/hub-dic-category-measure-template/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubDicCategoryMeasureTemplateDto HubDicCategoryMeasureTemplateDto);
	
	@RequestMapping(value = "/hub-dic-category-measure-template/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubDicCategoryMeasureTemplateDto HubDicCategoryMeasureTemplateDto);
}

package com.shangpin.ephub.client.data.studio.match.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.studio.match.dto.StudioMatchSpuCriteriaDto;
import com.shangpin.ephub.client.data.studio.match.dto.StudioMatchSpuCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.studio.match.dto.StudioMatchSpuDto;
/**
 * 
 * <p>Title: StudioMatchSpuGateWay</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月6日 下午12:10:08
 *
 */
@FeignClient("studio-data-mysql-service")
public interface StudioMatchSpuGateWay {

	
	@RequestMapping(value = "/studio-match-spu/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody StudioMatchSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-match-spu/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody StudioMatchSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-match-spu/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long skuId);
	
	@RequestMapping(value = "/studio-match-spu/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody StudioMatchSpuDto hubSku);
	
	@RequestMapping(value = "/studio-match-spu/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody StudioMatchSpuDto hubSku);
	
	@RequestMapping(value = "/studio-match-spu/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioMatchSpuDto> selectByCriteriaWithRowbounds(@RequestBody StudioMatchSpuCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/studio-match-spu/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<StudioMatchSpuDto> selectByCriteria(@RequestBody StudioMatchSpuCriteriaDto criteria);
	
	@RequestMapping(value = "/studio-match-spu/select-by-primary-key/{skuId}", method = RequestMethod.POST,consumes = "application/json")
    public StudioMatchSpuDto selectByPrimaryKey(@PathVariable("skuId") Long skuId);
	
	@RequestMapping(value = "/studio-match-spu/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody StudioMatchSpuCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-match-spu/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody StudioMatchSpuCriteriaDto hubSkuWithCriteria);
	
	@RequestMapping(value = "/studio-match-spu/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody StudioMatchSpuDto hubSku);
	
	@RequestMapping(value = "/studio-match-spu/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody StudioMatchSpuDto hubSku);

}

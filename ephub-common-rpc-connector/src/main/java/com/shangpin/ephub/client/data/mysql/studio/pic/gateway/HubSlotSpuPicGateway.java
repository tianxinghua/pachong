package com.shangpin.ephub.client.data.mysql.studio.pic.gateway;

import java.util.List;

import com.shangpin.ephub.client.data.mysql.studio.pic.dto.HubSlotSpuPicCriteriaDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.data.mysql.studio.pic.dto.HubSlotSpuPicCriteriaWithRowBoundsDto;
import com.shangpin.ephub.client.data.mysql.studio.pic.dto.HubSlotSpuPicDto;
import com.shangpin.ephub.client.data.mysql.studio.pic.dto.HubSlotSpuPicWithCriteriaDto;
/**
 * <p>Title:HubSlotSpuPicDtoGateway.java </p>
 * <p>Description: EPHUB品牌字典接口网关</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午1:46:35
 */
@FeignClient("ephub-data-mysql-service")
public interface HubSlotSpuPicGateway {

	@RequestMapping(value = "/hub-slot-spu-pic/count-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int countByCriteria(@RequestBody HubSlotSpuPicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-slot-spu-pic/delete-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByCriteria(@RequestBody HubSlotSpuPicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-slot-spu-pic/delete-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int deleteByPrimaryKey(Long brandDicId);
	
	@RequestMapping(value = "/hub-slot-spu-pic/insert", method = RequestMethod.POST,consumes = "application/json")
    public Long insert(@RequestBody HubSlotSpuPicDto HubSlotSpuPicDto);
	
	@RequestMapping(value = "/hub-slot-spu-pic/insert-selective", method = RequestMethod.POST,consumes = "application/json")
    public Long insertSelective(@RequestBody HubSlotSpuPicDto HubSlotSpuPicDto);
	
	@RequestMapping(value = "/hub-slot-spu-pic/select-by-criteria-with-rowbounds", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSlotSpuPicDto> selectByCriteriaWithRowbounds(@RequestBody HubSlotSpuPicCriteriaWithRowBoundsDto criteriaWithRowBounds);
	
	@RequestMapping(value = "/hub-slot-spu-pic/select-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public List<HubSlotSpuPicDto> selectByCriteria(@RequestBody HubSlotSpuPicCriteriaDto criteria);
	
	@RequestMapping(value = "/hub-slot-spu-pic/select-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public HubSlotSpuPicDto selectByPrimaryKey(Long brandDicId);
	
	@RequestMapping(value = "/hub-slot-spu-pic/update-by-criteria-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteriaSelective(@RequestBody HubSlotSpuPicWithCriteriaDto HubSlotSpuPicDtoWithCriteria);
	
	@RequestMapping(value = "/hub-slot-spu-pic/update-by-criteria", method = RequestMethod.POST,consumes = "application/json")
    public int updateByCriteria(@RequestBody HubSlotSpuPicWithCriteriaDto HubSlotSpuPicDtoWithCriteria);
	
	@RequestMapping(value = "/hub-slot-spu-pic/update-by-primary-key-selective", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKeySelective(@RequestBody HubSlotSpuPicDto HubSlotSpuPicDto);
	
	@RequestMapping(value = "/hub-slot-spu-pic/update-by-primary-key", method = RequestMethod.POST,consumes = "application/json")
    public int updateByPrimaryKey(@RequestBody HubSlotSpuPicDto HubSlotSpuPicDto);
}
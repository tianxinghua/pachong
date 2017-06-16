package com.shangpin.ephub.data.mysql.slot.pic.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.shangpin.ephub.data.mysql.slot.pic.bean.HubSlotSpuPicCriteriaWithRowBounds;
import com.shangpin.ephub.data.mysql.slot.pic.bean.HubSlotSpuPicWithCriteria;
import com.shangpin.ephub.data.mysql.slot.pic.po.HubSlotSpuPic;
import com.shangpin.ephub.data.mysql.slot.pic.po.HubSlotSpuPicCriteria;
import com.shangpin.ephub.data.mysql.slot.pic.service.HubSlotSpuPicService;

@RestController
@RequestMapping("/hub-slot-spu-pic")
public class HubSlotSpuPicController {
	@Autowired
	private HubSlotSpuPicService hubSlotSpuPicService;

	@RequestMapping(value = "/count-by-criteria")
    public int countByCriteria(@RequestBody HubSlotSpuPicCriteria criteria){
    	return hubSlotSpuPicService.countByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByCriteria(@RequestBody HubSlotSpuPicCriteria criteria){
    	return hubSlotSpuPicService.deleteByCriteria(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long skuId){
    	return hubSlotSpuPicService.deleteByPrimaryKey(skuId);
    }
	@RequestMapping(value = "/insert")
    public Long  insert(@RequestBody HubSlotSpuPic hubSlotSpuPic){

    	 hubSlotSpuPicService.insert(hubSlotSpuPic);
		return hubSlotSpuPic.getSlotSpuPicId();
    }
	@RequestMapping(value = "/insert-selective")
    public Long insertSelective(@RequestBody HubSlotSpuPic hubSlotSpuPic){
    	 hubSlotSpuPicService.insertSelective(hubSlotSpuPic);
		return hubSlotSpuPic.getSlotSpuPicId();
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubSlotSpuPic> selectByCriteriaWithRowbounds(@RequestBody HubSlotSpuPicCriteriaWithRowBounds criteriaWithRowBounds){
    	return hubSlotSpuPicService.selectByCriteriaWithRowbounds(criteriaWithRowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubSlotSpuPic> selectByCriteria(@RequestBody HubSlotSpuPicCriteria criteria){
    	return hubSlotSpuPicService.selectByCriteria(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key/{skuId}")
    public HubSlotSpuPic selectByPrimaryKey(@PathVariable(value = "skuId") Long skuId){
    	return hubSlotSpuPicService.selectByPrimaryKey(skuId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByCriteriaSelective(@RequestBody HubSlotSpuPicWithCriteria HubSlotSpuPicWithCriteria){
    	return hubSlotSpuPicService.updateByCriteriaSelective(HubSlotSpuPicWithCriteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByCriteria(@RequestBody HubSlotSpuPicWithCriteria HubSlotSpuPicWithCriteria){
    	return hubSlotSpuPicService.updateByCriteria(HubSlotSpuPicWithCriteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(@RequestBody HubSlotSpuPic HubSlotSpuPic){
    	return hubSlotSpuPicService.updateByPrimaryKeySelective(HubSlotSpuPic);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(@RequestBody HubSlotSpuPic HubSlotSpuPic){
    	return hubSlotSpuPicService.updateByPrimaryKey(HubSlotSpuPic);
    }
}

package com.shangpin.ephub.data.mysql.dictionary.brand.controller;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.data.mysql.dictionary.brand.mapper.HubBrandDicMapper;
import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubBrandDic;
import com.shangpin.ephub.data.mysql.dictionary.brand.po.HubBrandDicCriteria;

/**
 * <p>Title:HubBrandDicController.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月13日 下午3:37:23
 */
@RestController
@RequestMapping(value = "/hub-brand-dic")
public class HubBrandDicController {
	
	@Autowired
	private HubBrandDicMapper hubBrandDicMapper;
	@RequestMapping(value = "/count-by-criteria")
    public int countByExample(HubBrandDicCriteria criteria){
    	return hubBrandDicMapper.countByExample(criteria);
    }
	@RequestMapping(value = "/delete-by-criteria")
    public int deleteByExample(HubBrandDicCriteria criteria){
    	return hubBrandDicMapper.deleteByExample(criteria);
    }
	@RequestMapping(value = "/delete-by-primary-key")
    public int deleteByPrimaryKey(Long brandDicId){
    	return hubBrandDicMapper.deleteByPrimaryKey(brandDicId);
    }
	@RequestMapping(value = "/insert")
    public int insert(HubBrandDic hubBrandDic){
    	return hubBrandDicMapper.insert(hubBrandDic);
    }
	@RequestMapping(value = "/insert-selective")
    public int insertSelective(HubBrandDic hubBrandDic){
    	return hubBrandDicMapper.insertSelective(hubBrandDic);
    }
	@RequestMapping(value = "/select-by-criteria-with-rowbounds")
    public List<HubBrandDic> selectByExampleWithRowbounds(HubBrandDicCriteria criteria, RowBounds rowBounds){
    	return hubBrandDicMapper.selectByExampleWithRowbounds(criteria, rowBounds);
    }
	@RequestMapping(value = "/select-by-criteria")
    public List<HubBrandDic> selectByExample(HubBrandDicCriteria criteria){
    	return hubBrandDicMapper.selectByExample(criteria);
    }
	@RequestMapping(value = "/select-by-primary-key")
    public HubBrandDic selectByPrimaryKey(Long brandDicId){
    	return hubBrandDicMapper.selectByPrimaryKey(brandDicId);
    }
	@RequestMapping(value = "/update-by-criteria-selective")
    public int updateByExampleSelective(@Param("record") HubBrandDic hubBrandDic, @Param("example") HubBrandDicCriteria criteria){
    	return hubBrandDicMapper.updateByExample(hubBrandDic, criteria);
    }
	@RequestMapping(value = "/update-by-criteria")
    public int updateByExample(@Param("record") HubBrandDic hubBrandDic, @Param("example") HubBrandDicCriteria criteria){
    	return hubBrandDicMapper.updateByExample(hubBrandDic, criteria);
    }
	@RequestMapping(value = "/update-by-primary-key-selective")
    public int updateByPrimaryKeySelective(HubBrandDic hubBrandDic){
    	return hubBrandDicMapper.updateByPrimaryKeySelective(hubBrandDic);
    }
	@RequestMapping(value = "/update-by-primary-key")
    public int updateByPrimaryKey(HubBrandDic hubBrandDic){
    	return hubBrandDicMapper.updateByPrimaryKey(hubBrandDic);
    }
}

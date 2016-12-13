package com.shangpin.ephub.data.mysql.task.spuimport.mapper;

import com.shangpin.ephub.data.mysql.task.spuimport.po.HubSpuImportTask;
import com.shangpin.ephub.data.mysql.task.spuimport.po.HubSpuImportTaskCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubBrandDicMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月13日 下午2:12:01
 */
@Mapper
public interface HubSpuImportTaskMapper {
    int countByExample(HubSpuImportTaskCriteria example);

    int deleteByExample(HubSpuImportTaskCriteria example);

    int deleteByPrimaryKey(Long spuImportTaskId);

    int insert(HubSpuImportTask record);

    int insertSelective(HubSpuImportTask record);

    List<HubSpuImportTask> selectByExampleWithRowbounds(HubSpuImportTaskCriteria example, RowBounds rowBounds);

    List<HubSpuImportTask> selectByExample(HubSpuImportTaskCriteria example);

    HubSpuImportTask selectByPrimaryKey(Long spuImportTaskId);

    int updateByExampleSelective(@Param("record") HubSpuImportTask record, @Param("example") HubSpuImportTaskCriteria example);

    int updateByExample(@Param("record") HubSpuImportTask record, @Param("example") HubSpuImportTaskCriteria example);

    int updateByPrimaryKeySelective(HubSpuImportTask record);

    int updateByPrimaryKey(HubSpuImportTask record);
}
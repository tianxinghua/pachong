package com.shangpin.ephub.data.mysql.modifyDataStatus.state.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ModifyDataStatusMapper {

    void updateStatus();
    
    void updatePicStatus();
    
    void updateNewPicStatus();
    
}

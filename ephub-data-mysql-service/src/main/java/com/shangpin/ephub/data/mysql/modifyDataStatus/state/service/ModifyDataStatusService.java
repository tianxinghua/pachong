package com.shangpin.ephub.data.mysql.modifyDataStatus.state.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.modifyDataStatus.state.mapper.ModifyDataStatusMapper;

@Service
public class ModifyDataStatusService{

	@Autowired
    private ModifyDataStatusMapper modifyDataStatusMapper;

    public int updateStatus(){
        return modifyDataStatusMapper.updateStatus();
    }
    
    public int updatePicStatus(){
        return modifyDataStatusMapper.updatePicStatus();
    }
}

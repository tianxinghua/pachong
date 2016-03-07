package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.ColorContrastDTO;
import com.shangpin.iog.dto.MaterialContrastDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by fanhaiying on 15/6/15.
 */
public interface MaterialContrastFindService {
        /**
     * 获取材质信息
     * @return  颜色列表
     * @throws ServiceException
     */
    List<MaterialContrastDTO> findAll() throws ServiceException, SQLException;

}

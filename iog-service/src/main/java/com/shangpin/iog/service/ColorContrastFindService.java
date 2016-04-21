package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.ColorContrastDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by huxia on 15/6/15.
 */
public interface ColorContrastFindService {
        /**
     * 获取颜色信息
     * @return  颜色列表
     * @throws ServiceException
     */
    List<ColorContrastDTO> findAll() throws ServiceException, SQLException;

}

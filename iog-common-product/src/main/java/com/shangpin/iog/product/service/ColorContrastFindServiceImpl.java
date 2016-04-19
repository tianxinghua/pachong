package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.ColorContrastDTO;
import com.shangpin.iog.product.dao.ColorContrastMapper;
import com.shangpin.iog.service.ColorContrastFindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by huxia on 15/6/15.
 */
@Service("colorContrastFindService")
public class ColorContrastFindServiceImpl implements ColorContrastFindService {


    @Autowired
    ColorContrastMapper colorContrastDAO;

    @Override
    public List<ColorContrastDTO> findAll() throws ServiceException {

        List<ColorContrastDTO> ColorContrastDTOList = colorContrastDAO.findAll();
        return ColorContrastDTOList;
    }
}
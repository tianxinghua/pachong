package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.ColorContrastDTO;
import com.shangpin.iog.dto.MaterialContrastDTO;
import com.shangpin.iog.product.dao.ColorContrastMapper;
import com.shangpin.iog.product.dao.MaterialContrastMapper;
import com.shangpin.iog.service.ColorContrastFindService;
import com.shangpin.iog.service.MaterialContrastFindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by fanhaiying on 15/6/15.
 */
@Service("materialContrastFindService")
public class MaterialContrastFindServiceImpl implements MaterialContrastFindService {


    @Autowired
    MaterialContrastMapper MaterialContrastDAO;

    @Override
    public List<MaterialContrastDTO> findAll() throws ServiceException {

        List<MaterialContrastDTO>MaterialContrastDTOList = MaterialContrastDAO.findAll();
        return MaterialContrastDTOList;
    }
}
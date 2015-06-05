package com.shangpin.iog.apennine.service.impl;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.apennine.utils.ApennineHttpUtil;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ApennineProductService;
import org.springframework.beans.factory.annotation.Autowired;
import com.shangpin.iog.service.ProductFetchService;

import java.util.List;

/**
 * Created by sunny on 2015/6/5.
 */
public class ApennineProductServiceImpl implements ApennineProductService{
    @Autowired
    ProductFetchService fetchService;
    @Override
    public long insertSKU(SkuDTO dto) throws ServiceException {
        return 0;
    }

    @Override
    public int insertSKU(List<SkuDTO> skuDTOList) throws ServiceException {
        fetchService.saveSKU(skuDTOList);
        return 0;
    }

    @Override
    public long insertSPU(SpuDTO dto) throws ServiceException {
        return 0;
    }
    @Override
    public int insertSPU(List<SpuDTO>spuDTOList)throws ServiceException{
        fetchService.saveSPU(spuDTOList);
        return 0;
    }
    @Override
    public long insertPicture(ProductPictureDTO dto)throws ServiceException{
        return 0;
    }
    @Override
    public int insertPicture(List<ProductPictureDTO> pictureDTOList)throws ServiceException{
        fetchService.savePicture(pictureDTOList);
        return 0;
    }
}

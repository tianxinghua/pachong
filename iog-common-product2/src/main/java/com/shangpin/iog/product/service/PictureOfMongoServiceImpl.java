package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.PictureDTO;
import com.shangpin.iog.mongodao.PictureDAO;
import com.shangpin.iog.mongodomain.ProductPicture;
import com.shangpin.iog.service.PictureOfMongoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by lizhongren on 2015/12/7.
 */
@Service
public class PictureOfMongoServiceImpl implements PictureOfMongoService{

    @Autowired
    PictureDAO pictureDAO;

    @Override
    public boolean isHavePic(String supplierId, String picUrl) {
        try {
            if(pictureDAO.findDistinctProductPictureBySupplierIdAndPicUrl(supplierId, picUrl).size()>0){
                return true;
            }else{
                return false;
            }
        } catch (ServiceException e) {
            return false;
        }
    }

	@Override
	public void removePicBySupplierId(String supplierId)
			throws ServiceException {
		// TODO Auto-generated method stub
		
	}


}

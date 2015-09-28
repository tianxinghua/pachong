package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.product.dao.ReturnOrderMapper;
import com.shangpin.iog.service.ReturnOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by sunny on 2015/9/19.
 */
@Service
public class ReturnOrderServiceImpl implements ReturnOrderService{
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private  final  static String UPDATE_ERROR = "更新退单状态失败";
    @Autowired
    ReturnOrderMapper returnOrderDao;
    @Override
    public void saveOrder(ReturnOrderDTO returnOrderDTO) throws ServiceException {
        try{
            returnOrderDao.save(returnOrderDTO);
        }catch (Exception e){
            logger.error(UPDATE_ERROR+ e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void updateReturnOrderStatus(Map<String, String> statusMap) throws ServiceException {
        try {
            returnOrderDao.updateReturnOrderStatus(statusMap);
        } catch (Exception e) {
            logger.error(UPDATE_ERROR + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void updateReturnOrderMsg(Map<String, String> statusMap) throws ServiceException {
        try {
            returnOrderDao.updateReturnOrderMsg(statusMap);
        } catch (Exception e) {
            logger.error("更新退单信息失败" + e.getMessage());
            e.printStackTrace();
        }

    }
}

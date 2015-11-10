package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.common.utils.InVoke;
import com.shangpin.iog.dto.EventProductDTO;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.mongodao.PictureDAO;
import com.shangpin.iog.mongodomain.ProductPicture;
import com.shangpin.iog.product.dao.EventProductMapper;
import com.shangpin.iog.product.dao.ProductPictureMapper;
import com.shangpin.iog.product.dao.SkuMapper;
import com.shangpin.iog.product.dao.SpuMapper;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by loyalty on 15/6/4.
 */
@Service
public class EventProductServiceImpl implements EventProductService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static String  REPEAT_MESSAGE="数据插入失败键重复";

    @Autowired
    EventProductMapper eventDAO;

    @Override
    public void saveEventProduct(EventProductDTO eventDTO) throws ServiceException {
        try {
        	eventDAO.save(eventDTO);
        } catch (Exception e) {
        	if(e instanceof DuplicateKeyException)
        		throw new ServiceMessageException(REPEAT_MESSAGE);
        	throw new ServiceMessageException("数据插入失败"+e.getMessage());
        }

    }

	@Override
	public String selectEventIdBySku(String skuNo,String supplierId) throws ServiceException {
		
		EventProductDTO event =null;
		 try {
			 event = eventDAO.selectEventIdBySku(skuNo,supplierId);
	        } catch (Exception e) {
	        	if(e instanceof DuplicateKeyException)
	        		throw new ServiceMessageException(REPEAT_MESSAGE);
	        	throw new ServiceMessageException("数据插入失败"+e.getMessage());
	        }
		 if(event!=null){
			 return event.getEventId();
		 }else{
			 return null;
		 }
		
	}
	@Override
	public EventProductDTO selectEventProductDTOBySku(String skuNo,String supplierId) throws ServiceException {
		
		EventProductDTO event =null;
		 try {
			 event = eventDAO.selectEventIdBySku(skuNo,supplierId);
	        } catch (Exception e) {
	        	if(e instanceof DuplicateKeyException)
	        		throw new ServiceMessageException(REPEAT_MESSAGE);
	        	throw new ServiceMessageException("数据插入失败"+e.getMessage());
	        }
		return event;
	}

	@Override
	public EventProductDTO checkEventSku(String supplierId,String sku) throws ServiceMessageException {
		EventProductDTO event =null;
		 try {
			 event = eventDAO.checkEventSku(supplierId,sku);
	        } catch (Exception e) {
	        	if(e instanceof DuplicateKeyException)
	        		throw new ServiceMessageException(REPEAT_MESSAGE);
	        	throw new ServiceMessageException("数据插入失败"+e.getMessage());
	        }
		 return event;
	}

	@Override
	public void updateEventDate(EventProductDTO dto) throws ServiceMessageException{
		EventProductDTO event =null;
		 try {
			 event = eventDAO.updateEvent(dto);
	        } catch (Exception e) {
	        	throw new ServiceMessageException("数据插入失败"+e.getMessage());
	        }
	}

	@Override
	public List<EventProductDTO> selectEventList() throws ServiceMessageException {
		List<EventProductDTO> event =null;
		 try {
			 event = eventDAO.selectEventList();
	        } catch (Exception e) {
	        	throw new ServiceMessageException("数据插入失败"+e.getMessage());
	        }
		 return event;
	}
}

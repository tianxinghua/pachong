package com.shangpin.iog.product.service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.common.utils.InVoke;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SkuRelationDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.mongodao.PictureDAO;
import com.shangpin.iog.mongodomain.ProductPicture;
import com.shangpin.iog.product.dao.ProductPictureMapper;
import com.shangpin.iog.product.dao.ProductsMapper;
import com.shangpin.iog.product.dao.SkuMapper;
import com.shangpin.iog.product.dao.SkuRelationHKMapper;
import com.shangpin.iog.product.dao.SkuRelationMapper;
import com.shangpin.iog.product.dao.SpuMapper;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

/**
 * Created by loyalty on 15/6/4.
 */
@Service
public  class ProductFetchServiceImpl implements ProductFetchService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static String  REPEAT_MESSAGE="数据插入失败键重复";

    @Autowired
    SkuMapper skuDAO;
    @Autowired
    SkuRelationHKMapper skuRelationHKDAO;
    
    @Autowired
    SkuRelationMapper skuRelationDAO;
    @Autowired
	ProductsMapper productDAO;
    
    @Autowired
    SpuMapper spuDAO;

    @Autowired
    ProductPictureMapper productPictureMapper;

    @Autowired
    PictureDAO pictureDAO;
    
    @Autowired
    ProductsMapper productsMapper;
    

    @Autowired
    ProductSearchService productSearchService;
    
    @Override
    public Map<String,String> findPictureBySupplierIdAndSpuId(String supplierId, String spuId){
    	
    	Map<String,String> map  =   null;
    	List<ProductPicture> spuPictureList = null;
    	try {
    		spuPictureList = pictureDAO
					.findDistinctProductPictureBySupplierIdAndSpuIdAndSkuIdNull(
							supplierId, spuId);
    		if(spuPictureList!=null){
    			map = new HashMap<String,String>()	;
    			for(ProductPicture p :spuPictureList){
    				map.put(p.getPicUrl(),p.getSpuId());
    			}
    		}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
    	return map;
    }

    @Override
    public void saveSPU(List<SpuDTO> spuDTOList) throws ServiceException {
        try {
            spuDAO.saveList(spuDTOList);
        } catch (Exception e) {
        	if(e instanceof DuplicateKeyException)
        		throw new ServiceMessageException(REPEAT_MESSAGE);
            throw new ServiceMessageException("数据插入失败"+e.getMessage());
        }
    }

    @Override
    public void saveSPU(SpuDTO spuDTO) throws ServiceException {
        try {
            spuDAO.save(spuDTO);
        } catch (Exception e) {
        	if(e instanceof DuplicateKeyException)
        		throw new ServiceMessageException(REPEAT_MESSAGE);
        	throw new ServiceMessageException("数据插入失败"+e.getMessage());
        }

    }

    @Override
    public void saveSKU(List<SkuDTO> skuDTOList) throws ServiceException {

        try {

            skuDAO.saveList(skuDTOList);
        } catch (Exception e) {
        	if(e instanceof DuplicateKeyException)
        		throw new ServiceMessageException(REPEAT_MESSAGE);
            throw new ServiceMessageException("数据插入失败"+e.getMessage());
        }

    }

    @Override
    public void saveSKU(SkuDTO skuDTO) throws ServiceException {

        try {
            if(StringUtils.isBlank(skuDTO.getNewMarketPrice())) skuDTO.setNewMarketPrice(skuDTO.getMarketPrice());
            if(StringUtils.isBlank(skuDTO.getNewSalePrice())) skuDTO.setNewSalePrice(skuDTO.getSalePrice());
            if(StringUtils.isBlank(skuDTO.getNewSupplierPrice())) skuDTO.setNewSupplierPrice(skuDTO.getSupplierPrice());
            skuDTO.setLastTime(new Date());
            skuDAO.save(skuDTO);
        } catch ( Exception e) {
        	if(e instanceof DuplicateKeyException)
        	throw new ServiceMessageException(REPEAT_MESSAGE);
            throw new ServiceMessageException("数据插入失败"+e.getMessage());
        }
    }
    @Override
	public void update(SkuDTO sku) {
		// TODO Auto-generated method stub
    	 try {
			skuDAO.update(sku);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    @Override
    public void updatePriceAndStock(SkuDTO skuDTO) throws ServiceException {
        try {
            if(null==skuDTO.getUpdateTime()) skuDTO.setUpdateTime(new Date());
            skuDAO.updatePriceAndStock(skuDTO);
        } catch ( Exception e) {

            throw new ServiceMessageException("数据更新失败"+e.getMessage());
        }
    }
    public List<SkuRelationDTO> selectRelationFromHKBySupplierId(String supplier){
		return skuRelationHKDAO.selectRelationFromHKBySupplierId(supplier);
	}
    @Override
    public void updateMaterial(SpuDTO spuDTO) throws ServiceException {
        try {
            if(null==spuDTO.getLastTime()) spuDTO.setLastTime(new Date());

            spuDAO.updateMaterial(spuDTO);
        } catch ( Exception e) {

            throw new ServiceMessageException("数据更新失败"+e.getMessage());
        }
    }
    @Override
    public void savePicture(ProductPictureDTO pictureDTO) throws ServiceException {
        try {
            productPictureMapper.save(pictureDTO);
        } catch (Exception e) {
        	if(e instanceof DuplicateKeyException)
        		throw new ServiceMessageException(REPEAT_MESSAGE);
        	throw new ServiceMessageException("数据插入失败"+e.getMessage());
        }

    }

    @Override
    public void savePicture(List<ProductPictureDTO> pictureDTOList) throws ServiceException {
        try {
            productPictureMapper.saveList(pictureDTOList);
        } catch (Exception e) {
        	if(e instanceof DuplicateKeyException)
        		throw new ServiceMessageException(REPEAT_MESSAGE);
            throw new ServiceMessageException("数据插入失败"+e.getMessage());
        }

    }

    
    @Override
    public void savePictureForMongo(ProductPictureDTO productPictureDTO) throws ServiceException {
        ProductPicture productPicture = new ProductPicture();
        if(null!=productPictureDTO) {
            try {
                InVoke.setValue(productPictureDTO,productPicture,null,null);
                pictureDAO.save(productPicture);
            } catch (Exception e) {
            	if(e instanceof DuplicateKeyException)
            		throw new ServiceMessageException(REPEAT_MESSAGE);
                throw new ServiceMessageException("数据插入失败"+e.getMessage());
            }
        }
    }
    public Map<String,String> findPictureBySupplierIdAndSkuId(String supplierId, String skuId){
     	
    	Map<String,String> map  =   null;
    	List<ProductPicture> spuPictureList = null;
    	try {
    		spuPictureList = pictureDAO
					.findDistinctProductPictureBySupplierIdAndSkuId(
							supplierId, skuId);
    		if(spuPictureList!=null){
    			map = new HashMap<String,String>()	;
    			for(ProductPicture p :spuPictureList){
    				map.put(p.getPicUrl(),p.getSkuId());
    			}
    		}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
    	return map;
    }
	@Override
	public void savePicture(String supplierId, String spuId, String skuId,
			Collection<String> picUrl) {
		Map map = null;
		ProductPictureDTO dto = null;
		if(spuId!=null){
			map = findPictureBySupplierIdAndSpuId(supplierId, spuId);
		}else if(skuId!=null){
			map = findPictureBySupplierIdAndSkuId(supplierId, skuId);
		}
		for(String pic:picUrl){
			if(map==null||!map.containsKey(pic)){

				dto = new ProductPictureDTO();
				dto.setPicUrl(pic);
				dto.setSupplierId(supplierId);
				dto.setId(UUIDGenerator.getUUID());
				if(spuId!=null){
					dto.setSpuId(spuId);
				}else{
					dto.setSkuId(skuId);
				}
				try {
					savePictureForMongo(dto);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public List<ProductDTO> selectAllSpu() throws ServiceException {
		
		List<ProductDTO>  list = productDAO.selectAllSpu();
		return list;
	}
	
	@Override
	public List<ProductDTO>  selectAllSku() throws ServiceException {
		
		List<ProductDTO>  list = productDAO.selectAllSku();
		return list;
		
	}

	@Override
	public List<ProductDTO> selectSpuByDay() throws ServiceException {
		
		List<ProductDTO>  list = productDAO.selectSpuByDay();
		
		return list;
		
	}
	
	@Override
	public List<ProductDTO> selectSkuByDay() throws ServiceException {
		List<ProductDTO>  list = productDAO.selectSkuByDay();
		return list;
	}
	@Override
	public List<SkuRelationDTO> selectAllRelation()  throws ServiceException{
		return skuRelationHKDAO.selectAllRelation();
	}
	@Override
	public SkuDTO findSKUBySupplierIdAndSkuId(String supplierId, String spuId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SkuDTO findSupplierPrice(String supplierId, String skuId)
			throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SpuDTO findSPUBySupplierIdAndSpuId(String supplierId, String spuId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void saveSkuRelation(SkuRelationDTO sku) throws ServiceException {
		try {
			skuRelationDAO.save(sku);
		} catch ( Exception e) {
        	if(e instanceof DuplicateKeyException)
        	throw new ServiceMessageException(REPEAT_MESSAGE);
            throw new ServiceMessageException("数据插入失败"+e.getMessage());
        }
	}

	@Override
	public List<SkuRelationDTO> selectRelationDayFromHK()
			throws ServiceException {
		return skuRelationHKDAO.selectRalationDayFromHK();
	}

	@Override
	public List<String> saveAndCheckPicture(String supplierId, String id,
			Collection<String> picUrl, String flag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateSpuOrSkuMemoAndTime(String supplierId, String id,
			String memo, String flag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateSkuListMemo(String supplierId, List<String> idList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateSpuListMemo(List<SpuDTO> spuList) {
		// TODO Auto-generated method stub
		
	}

	public List<String> saveAndCheckPictureForSteFaniamode(String supplierId,
			String id, Collection<String> picUrl, String flag) {
		return null;
	}
	@Override
	public List<ProductDTO> findSkuBySupplierId(String supplier){
		return productsMapper.findProductBySupplierId(supplier);
	}	
	
	@Override
	public List<ProductDTO> findProductByDate(String startDate, String endDate){
		return productsMapper.findProductByDate(startDate,endDate);
	}

	@Override
	public String findBarCodeBySupplierIdAndSkuId(String supplierId,
			String skuId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> findPictureBySupplierIdAndSkuIdOrSpuId(
			String supplierId, String skuId, String spuId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void updateSpSkuIdBySupplier(String supplierId,
			String supplierSkuId, String spSkuId, String skuStatus,
			String spProductCode) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

	
}

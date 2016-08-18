package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.common.utils.InVoke;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.PictureDTO;
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
import com.shangpin.iog.product.dao.SkuRelationMapper;
import com.shangpin.iog.product.dao.SpuMapper;
import com.shangpin.iog.service.ProductFetchService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by loyalty on 15/6/4.
 */
@Service
public class ProductFetchServiceImpl implements ProductFetchService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static String  REPEAT_MESSAGE="数据插入失败键重复";

    @Autowired
    SkuMapper skuDAO;


    @Autowired
    SkuRelationMapper skuRelationDAO;
    @Autowired
    SpuMapper spuDAO;

    @Autowired
    ProductPictureMapper productPictureMapper;

    @Autowired
    PictureDAO pictureDAO;
    @Autowired
    ProductsMapper productsMapper;
    
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
            skuDAO.save(skuDTO);
        } catch ( Exception e) {
        	if(e instanceof DuplicateKeyException)
        	throw new ServiceMessageException(REPEAT_MESSAGE);
            throw new ServiceMessageException("数据插入失败"+e.getMessage());
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
    @Override
    public void updateMaterial(SpuDTO spuDTO) throws ServiceException {
        try {
//            if(null==spuDTO.getLastTime()) spuDTO.setLastTime(new Date());
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
	public SkuDTO findSupplierPrice(String supplierId, String skuId)
			throws ServiceException {
		
		SkuDTO sku = null;
		try {
			sku = skuDAO.findSupplierPrice(supplierId, skuId);
		} catch (Exception e) {
            logger.error("获取失败 "+e.getMessage());
			e.printStackTrace();
		}
		return sku;
	}
	

	@Override
	public SkuDTO findSKUBySupplierIdAndSkuId(String supplierId, String skuId) {
		SkuDTO sku = null;
		try {
			sku = skuDAO.findSKUBySupplierAndSkuId(supplierId, skuId);
		} catch (Exception e) {
            logger.error("获取失败 "+e.getMessage());
			e.printStackTrace();
		}
		return sku;
	}
	
	@Override
	public SpuDTO findSPUBySupplierIdAndSpuId(String supplierId, String spuId) {
		SpuDTO spu = null;
		try {
			spu = spuDAO.findSPUBySupplierAndSpuId(supplierId, spuId);
		} catch (Exception e) {
            logger.error("获取失败 "+e.getMessage());
			e.printStackTrace();
		}
		return spu;
	}
	
	@Override
	public List<ProductDTO> selectSkuByDay() throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductDTO> selectSpuByDay() throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductDTO> selectAllSku() throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductDTO> selectAllSpu() throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List selectAllRelation() throws ServiceException {
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
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void updateSpuOrSkuMemoAndTime(String supplierId,String id,String memo,String flag) {
		if (flag.equals("spu")) {
			spuDAO.updateSpuMemo(supplierId, id, memo, new Date());
		}else{
			skuDAO.updateSkuMemo(supplierId, id, memo, new Date());
		}
	}
	  public void updateSkuListMemo(String supplierId, List<String> idList){
		  skuDAO.updateSkuMemoList(idList, supplierId);
	  }
	  
	  public void updateSpuListMemo(List<SpuDTO> spuList){
	      spuDAO.updateSpuMemoList(spuList);
	  }
	
	
	@Override
	public List<String> saveAndCheckPicture(String supplierId, String id,
			Collection<String> picUrl,String flag) {
		List<String> imageList = new ArrayList<String>();
		Map map = null;
		ProductPictureDTO dto = null;
		if (flag.equals("spu")) {
			map = findPictureBySupplierIdAndSpuId(supplierId, id);
		}else if(flag.equals("sku")){
			map = findPictureBySupplierIdAndSkuId(supplierId, id);
		}
		for(String pic:picUrl){
			if(map==null||!map.containsKey(pic)){
				imageList.add(pic);
				dto = new ProductPictureDTO();
				dto.setPicUrl(pic);
				dto.setSupplierId(supplierId);
				dto.setId(UUIDGenerator.getUUID());
				if (flag.equals("spu")){
					dto.setSpuId(id);
				}else{
					dto.setSkuId(id);
				}
				try {
					savePictureForMongo(dto);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		}
		return imageList;
	}
	 public List<String> saveAndCheckPictureForSteFaniamode(String supplierId, String id, Collection<String> picUrl, String flag) {
	    List<String> imageList = new ArrayList();
	    Map map = null;
	    ProductPictureDTO dto = null;
	    if (flag.equals("spu")) {
	      map = findPictureBySupplierIdAndSpuId(supplierId, id);
	    } else if (flag.equals("sku")) {
	      map = findPictureBySupplierIdAndSkuId(supplierId, id);
	    }
	    if ((map == null) || (map.size() <= 0)) {
	      for (String pic : picUrl)
	      {
	        imageList.add(pic);
	        dto = new ProductPictureDTO();
	        dto.setPicUrl(pic);
	        dto.setSupplierId(supplierId);
	        dto.setId(UUIDGenerator.getUUID());
	        if (flag.equals("spu")) {
	          dto.setSpuId(id);
	        } else {
	          dto.setSkuId(id);
	        }
	        try
	        {
	          savePictureForMongo(dto);
	        }
	        catch (ServiceException e)
	        {
	          e.printStackTrace();
	        }
	      }
	    }
	    return imageList;
	  }

	public void updateMongoTest(String id) {
		try {
			pictureDAO.removePicture(id);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public String findBarCodeBySupplierIdAndSkuId(String supplierId,
			String skuno){
		
		return skuDAO.findBarCodeBySkuId(supplierId,skuno);
	}
	@Override
    public Map<String,String> findPictureBySupplierIdAndSkuIdOrSpuId(String supplierId, String skuId,String spuId){
     	
    	Map<String,String> map  =   null;
    	List<ProductPicture> pictureList = null;
    	try {
    		if (skuId!=null) {
    			pictureList = pictureDAO.findDistinctProductPictureBySupplierIdAndSkuId(supplierId, skuId);
			}else{
				pictureList = pictureDAO.findDistinctProductPictureBySupplierIdAndSkuId(supplierId, spuId);
				
			}
    		if(pictureList!=null){
    			String key = "";
    			map = new HashMap<String,String>()	;
    			for(ProductPicture p :pictureList){
    				key = p.getSkuId()==null?p.getSpuId():p.getSkuId();
    				
    				if (map.containsKey(key)) {
    					map.put(key, map.get(key)+","+p.getPicUrl());
					}else{
						map.put(key, p.getPicUrl());
					}
    			}
    		}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
    	return map;
    }

	@Override
	public List<ProductDTO> findSkuBySupplierId(String supplier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductDTO> findProductByDate(String startDate, String endDate) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void updateSpSkuIdBySupplier(String supplierId,String supplierSkuId,String spSkuId,String skuStatus) throws ServiceException{
		skuDAO.updateSpSkuIdBySupplier(supplierId, supplierSkuId, spSkuId,skuStatus); 
	}
	

}

package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.common.utils.InVoke;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.PictureDTO;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.mongodao.PictureDAO;
import com.shangpin.iog.mongodomain.ProductPicture;
import com.shangpin.iog.product.dao.ProductPictureMapper;
import com.shangpin.iog.product.dao.ProductsMapper;
import com.shangpin.iog.product.dao.SkuMapper;
import com.shangpin.iog.product.dao.SpuMapper;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

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
	ProductsMapper productDAO;
    
    @Autowired
    SpuMapper spuDAO;

    @Autowired
    ProductPictureMapper productPictureMapper;

    @Autowired
    PictureDAO pictureDAO;
    

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
	public void saveAllSpuFromHK() throws ServiceException {
		
		List<ProductDTO>  list = productDAO.selectAllSpu();
		if(list!=null){
			for(ProductDTO pro:list){
				SpuDTO spu = new SpuDTO();
				spu.setBrandId(pro.getBrandId());
				spu.setId(UUIDGenerator.getUUID());
				spu.setBrandName(pro.getBrandName());
				spu.setCategoryGender(pro.getCategoryGender());
				spu.setCategoryId(pro.getCategoryId());
				spu.setCategoryName(pro.getCategoryName());
				spu.setMaterial(pro.getMaterial());
				spu.setProductOrigin(pro.getProductOrigin());
				spu.setSeasonId(pro.getSeasonId());
				spu.setSeasonName(pro.getSeasonName());
				spu.setSpuId(pro.getSpuId());
				spu.setSpuName(pro.getSpuName());
				spu.setSubCategoryId(pro.getSubCategoryId());
				spu.setSubCategoryName(pro.getSubCategoryName());
				spu.setSupplierId(pro.getSupplierId());
				try {
					spuDAO.save(spu);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void  saveAllSkuFromHK() throws ServiceException {
		
		List<ProductDTO>  list = productDAO.selectAllSku();
		if(list!=null){
			for(ProductDTO pro:list){
				SkuDTO sku = new SkuDTO();
				sku.setBarcode(pro.getBarcode());
				sku.setColor(pro.getColor());
				sku.setId(UUIDGenerator.getUUID());
				sku.setMarketPrice(pro.getMarketPrice());
				sku.setProductCode(pro.getProductCode());
				sku.setProductDescription(pro.getProductDescription());
				sku.setProductName(pro.getProductName());
				sku.setProductSize(pro.getSize());
				sku.setSaleCurrency(pro.getSaleCurrency());
				sku.setSalePrice(pro.getSalePrice());
				sku.setSkuId(pro.getSkuId());
				sku.setSpuId(pro.getSpuId());
				sku.setStock(pro.getStock());
				sku.setSupplierId(pro.getSupplierId());
				sku.setSupplierPrice(pro.getSupplierPrice());
				sku.setNewMarketPrice(pro.getNewMarketPrice());
				sku.setNewSalePrice(pro.getNewSalePrice());
				sku.setNewSupplierPrice(pro.getNewSupplierPrice());
				try {
					skuDAO.save(sku);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	public void saveSpuDayFromHK() throws ServiceException {
		
		List<ProductDTO>  list = productDAO.selectSpuByDay();
		
		if(list!=null){   
			for(ProductDTO pro:list){
				SpuDTO spu = new SpuDTO();
				spu.setBrandId(pro.getBrandId());
				spu.setId(UUIDGenerator.getUUID());
				spu.setBrandName(pro.getBrandName());
				spu.setCategoryGender(pro.getCategoryGender());
				spu.setCategoryId(pro.getCategoryId());
				spu.setCategoryName(pro.getCategoryName());
				spu.setMaterial(pro.getMaterial());
				spu.setProductOrigin(pro.getProductOrigin());
				spu.setSeasonId(pro.getSeasonId());
				spu.setSeasonName(pro.getSeasonName());
				spu.setSpuId(pro.getSpuId());
				spu.setSpuName(pro.getSpuName());
				spu.setSubCategoryId(pro.getSubCategoryId());
				spu.setSubCategoryName(pro.getSubCategoryName());
				spu.setSupplierId(pro.getSupplierId());
				try {
					spuDAO.save(spu);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	public void saveSkuDayFromHK() throws ServiceException {
		
		List<ProductDTO>  list = productDAO.selectSkuByDay();
		if(list!=null){
			for(ProductDTO pro:list){
				SkuDTO sku = new SkuDTO();
				sku.setId(UUIDGenerator.getUUID());
				sku.setBarcode(pro.getBarcode());
				sku.setColor(pro.getColor());
				sku.setMarketPrice(pro.getMarketPrice());
				sku.setProductCode(pro.getProductCode());
				sku.setProductDescription(pro.getProductDescription());
				sku.setProductName(pro.getProductName());
				sku.setProductSize(pro.getSize());
				sku.setSaleCurrency(pro.getSaleCurrency());
				sku.setSalePrice(pro.getSalePrice());
				sku.setSkuId(pro.getSkuId());
				sku.setSpuId(pro.getSpuId());
				sku.setStock(pro.getStock());
				sku.setSupplierId(pro.getSupplierId());
				sku.setSupplierPrice(pro.getSupplierPrice());
				sku.setNewMarketPrice(pro.getNewMarketPrice());
				sku.setNewSalePrice(pro.getNewSalePrice());
				sku.setNewSupplierPrice(pro.getNewSupplierPrice());
				try {
					skuDAO.save(sku);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

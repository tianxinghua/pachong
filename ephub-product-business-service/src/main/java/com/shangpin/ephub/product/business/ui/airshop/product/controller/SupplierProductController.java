package com.shangpin.ephub.product.business.ui.airshop.product.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.shangpin.ephub.client.data.airshop.supplier.product.dto.HubSupplierProductRequestWithPage;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.product.business.common.mapp.hubSupplierValueMapping.HubSupplierValueMappingService;
import com.shangpin.ephub.product.business.common.supplier.product.HubSupplierProductService;
import com.shangpin.ephub.product.business.common.supplier.sku.HubSupplierSkuService;
import com.shangpin.ephub.product.business.common.supplier.spu.HubSupplierSpuService;
import com.shangpin.ephub.product.business.conf.stream.source.task.sender.TaskStreamSender;
import com.shangpin.ephub.product.business.rest.gms.service.SupplierService;
import com.shangpin.ephub.product.business.rest.supplier.controller.SupplierController;
import com.shangpin.ephub.product.business.ui.airshop.product.dto.PageResponseDTO;
import com.shangpin.ephub.product.business.ui.airshop.product.dto.ProductDTO;
import com.shangpin.ephub.product.business.ui.airshop.product.dto.ProductDetailResponseDTO;
import com.shangpin.ephub.product.business.ui.airshop.product.dto.ProductDetailSkuResponseDTO;
import com.shangpin.ephub.product.business.ui.airshop.product.dto.ResponseDTO;
import com.shangpin.ephub.product.business.ui.airshop.product.dto.SkuProductDTO;
import com.shangpin.ephub.product.business.ui.airshop.product.dto.SpuProductDTO;
import com.shangpin.ephub.product.business.ui.airshop.product.dto.SupplierProduct;
import com.shangpin.ephub.product.business.ui.airshop.product.dto.UUIDGenerator;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title:PendingProductController </p>
 * <p>Description: 待处理页面</p>
 * <p>Company: www.shangpin.com</p>
 * @author lubaijiang
 * @date 2016年12月21日 下午5:15:48
 *
 */
@RestController
@RequestMapping("/supplier-product")
@Slf4j
public class SupplierProductController {
	
	@Autowired
	RestTemplate rest;
	@Autowired
	HubSupplierValueMappingService hubSupplierValueMappingService;
	@Autowired
	TaskStreamSender productImportTaskStreamSender;
	@Autowired
	private HubSupplierProductService hubSupplierProductService;
	@Autowired
	private HubSupplierSkuService hubSupplierSkuService;
	@Autowired
	private HubSupplierSpuService hubSupplierSpuService;
	@Autowired
	HubSpuPendingPicGateWay hubSpuPendingPicGateWay;
	@Autowired
	HubSupplierSkuGateWay hubSupplierSkuGateWay;
	@Autowired
	HubSupplierSpuGateWay hubSupplierSpuGateWay;
	@RequestMapping(value="/selectSupplierProductInfo",method=RequestMethod.POST)
    public String selectProductDetail(@RequestBody SkuProductDTO skuQuryDto){
    	log.info("airshop查询品牌方原始链接请求参数：{}",skuQuryDto);
    	HubSupplierSkuCriteriaDto criteria = new HubSupplierSkuCriteriaDto();
    	criteria.createCriteria().andSupplierIdEqualTo(skuQuryDto.getSupplierId()).andSupplierSkuNoEqualTo(skuQuryDto.getSkuId());
    	List<HubSupplierSkuDto> listSku = hubSupplierSkuGateWay.selectByCriteria(criteria);
		if(listSku!=null&&!listSku.isEmpty()){
			Long spuId = listSku.get(0).getSupplierSpuId();
			HubSupplierSpuDto spuDto = hubSupplierSpuGateWay.selectByPrimaryKey(spuId);
			if(spuDto!=null){
				return spuDto.getProductUrl();
			}
		}
        return null;
    }
	
    @RequestMapping(value="/selectSupplierProduct",method=RequestMethod.POST)
    public PageResponseDTO pendingList(@RequestBody HubSupplierProductRequestWithPage pendingQuryDto){
    	log.info("airshop查询请求参数：{}",pendingQuryDto);
    	PageResponseDTO pendingProducts = new PageResponseDTO();
    	int total = hubSupplierProductService.count(pendingQuryDto);
    	if(total>0){
    		List<ProductDTO> list = hubSupplierProductService.selectProductList(pendingQuryDto);
        	pendingProducts.setList(list);
        	pendingProducts.setCount(total);	
    	}
        return pendingProducts;
    }

    
    @RequestMapping(value="/detail/{supplierSpuId}",method=RequestMethod.GET)
    public ProductDetailResponseDTO detail(@PathVariable("supplierSpuId") Long supplierSpuId){
    	log.info("airshop查询详情接受到参数：{}",supplierSpuId);
    	HubSupplierSpuDto spu = hubSupplierSpuService.selectHubSupplierSpuById(supplierSpuId);
    	ProductDetailResponseDTO productDetail = new ProductDetailResponseDTO();
    	if(spu!=null){
    		productDetail.setBrand(spu.getSupplierBrandname());
    		productDetail.setCategory(spu.getSupplierCategoryname());
    		productDetail.setColour(spu.getSupplierSpuColor());
    		productDetail.setDescription(spu.getSupplierSpuDesc());
    		productDetail.setGender(spu.getSupplierGender());
    		productDetail.setMadeIn(spu.getSupplierOrigin());
    		productDetail.setMaterial(spu.getSupplierMaterial());
    		if(spu.getSupplierSeasonname()!=null){
    			String yearSeason = spu.getSupplierSeasonname();
    			if(yearSeason.split("-").length>1){
    				productDetail.setSeason(yearSeason.split("-")[1]);
    				productDetail.setYear(yearSeason.split("-")[0]);
    			}
    		}
    		
    		productDetail.setSpuName(spu.getSupplierSpuName());
    		productDetail.setSupplierSpuId(spu.getSupplierSpuId());
    		productDetail.setProductCode(spu.getSupplierSpuModel());
    		HubSpuPendingPicCriteriaDto criteriaPic = new HubSpuPendingPicCriteriaDto();
    		criteriaPic.createCriteria().andSupplierIdEqualTo(spu.getSupplierId()).andSupplierSpuIdEqualTo(supplierSpuId);
    		List<HubSpuPendingPicDto> picList = hubSpuPendingPicGateWay.selectByCriteria(criteriaPic);
    		
    		List<String> pics = new ArrayList<String>();
    		if(picList!=null&&picList.size()>1){
    			for(HubSpuPendingPicDto pic:picList){
    				pics.add(pic.getPicUrl());	
    			}
    		}
    		productDetail.setImages(pics);
    		
    		List<HubSupplierSkuDto> listSku = hubSupplierSkuService.selectListSkuBySpuId(supplierSpuId);
        	if(listSku!=null){
        		List<ProductDetailSkuResponseDTO> skus = new ArrayList<ProductDetailSkuResponseDTO>();
        		for(HubSupplierSkuDto sku:listSku){
        			ProductDetailSkuResponseDTO response = new ProductDetailSkuResponseDTO();
        			response.setBarcode(sku.getSupplierBarcode());
        			response.setCurrency(sku.getMarketPriceCurrencyorg());
        			response.setMarketPrice(sku.getMarketPrice()+"");
        			response.setSkuId(sku.getSupplierSkuNo());
        			response.setShangpinSKU(sku.getSpSkuNo());
        			response.setSize(sku.getSupplierSkuSize());
        			response.setSizeClass(sku.getSupplierSkuSizeType());
        			response.setSupplyPrice(sku.getSupplyPrice()+"");
        			response.setSupplierSkuId(sku.getSupplierSkuId());
        			productDetail.setMarketPrice(sku.getMarketPrice()+"");
        			productDetail.setSupplyPrice(sku.getSupplyPrice()+"");
        			productDetail.setCurrency(sku.getMarketPriceCurrencyorg());
        			skus.add(response);
        		}
        		productDetail.setSkus(skus);
        	}
    	}
    	return productDetail;
    }
    

    @RequestMapping(value="/add/{supplierId}",method=RequestMethod.POST)
    public ResponseDTO add(@PathVariable("supplierId") String supplierId,@RequestBody SpuProductDTO product){
    	log.info(supplierId+"airshop新增接受到参数：{}",product);
    	HubSupplierSpuDto supplierSpu = convertProduct2HubSupplierSpu(supplierId,product);
    	supplierSpu.setCreateTime(new Date());
    	Long supplierSpuId = hubSupplierSpuService.insert(supplierSpu);
    	List<SkuProductDTO> skus = product.getList();
    	if(skus!=null){
    		for(SkuProductDTO sku:skus){
    			HubSupplierSkuDto supplierSku = convertProduct2HubSupplierSku(supplierId,sku,product);	
    			supplierSku.setSupplierSpuId(supplierSpuId);
    			hubSupplierSkuService.insertSku(supplierSku);
    		}
    	}
    	ResponseDTO response = new ResponseDTO();
    	response.setResponseCode(1);
    	return response;
    }

    @RequestMapping(value="/update/{supplierId}",method=RequestMethod.POST)
    public ResponseDTO update(@PathVariable("supplierId") String supplierId,@RequestBody SpuProductDTO product){
    	log.info(supplierId+"airshop更新接受到参数：{}",product);
    	HubSupplierSpuDto supplierSpu = convertProduct2HubSupplierSpu(supplierId,product);
    	Long supplierSpuId = hubSupplierSpuService.insert(supplierSpu);
    	List<SkuProductDTO> skus = product.getList();
    	if(skus!=null){
    		for(SkuProductDTO sku:skus){
    			HubSupplierSkuDto supplierSku = convertProduct2HubSupplierSku(supplierId,sku,product);	
    			supplierSku.setSupplierSpuId(supplierSpuId);
    			hubSupplierSkuService.insertSku(supplierSku);
    		}
    	}
    	ResponseDTO response = new ResponseDTO();
    	response.setResponseCode(1);
    	return response;
    }
    
    @RequestMapping(value="/batchSave/{supplierId}",method=RequestMethod.POST)
    public ResponseDTO batchAdd(@PathVariable("supplierId") String supplierId,@RequestBody List<ProductDTO> products){
    	log.info(supplierId+"airshop批量新增接受到参数：{}",products.size());
    	SimpleDateFormat simTemp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	if(products!=null){
    		SupplierProduct supp = new SupplierProduct();
			supp.setMessageType("json");
			supp.setSupplierName("smets");
			supp.setSupplierId(supplierId);
			List<HubSupplierValueMappingDto> valueMap = hubSupplierValueMappingService.getHubSupplierValueMappingByTypeAndSupplierId((byte)5, supplierId);
			if(valueMap!=null&&valueMap.size()>0){
				supp.setSupplierNo(valueMap.get(0).getHubValNo());	
			}
    		for(ProductDTO product:products){
    			supp.setMessageId(UUIDGenerator.getUUID());
    			supp.setMessageDate(simTemp.format(new Date()));
    			supp.setData(JSONObject.toJSONString(product));
    			JSONObject supplierDto = rest.postForEntity("http://api-queue.ephub.spidc1.com/message/api/original-product",supp, JSONObject.class).getBody();
    			log.info(supp.getSupplierName()+"=="+supplierDto.toString());
    		}
    	}
    	ResponseDTO response = new ResponseDTO();
    	response.setResponseCode(1);
    	return response;
    }
    
	private HubSupplierSkuDto convertProduct2HubSupplierSku(String supplierId,SkuProductDTO sku,SpuProductDTO product) {
		HubSupplierSkuDto hubSupplierSkuDto = new HubSupplierSkuDto();
		
		int stock = 0;
		if(StringUtils.isBlank(sku.getStock())){
			stock = 0;
		}else{
			if(Integer.parseInt(sku.getStock().trim())<0){
				stock = 0;
			}
			stock = Integer.parseInt(sku.getStock().trim());
		}
		hubSupplierSkuDto.setSupplierId(supplierId);
		hubSupplierSkuDto.setStock(stock);
		String sizeAndType = sku.getSize();
		String size = null;
		String sizeType = null;
		if(sizeAndType!=null&&sizeAndType.split("-").length>1){
			size = sizeAndType.split("-")[1];
			sizeType = sizeAndType.split("-")[0];
		}
		hubSupplierSkuDto.setSupplierSkuSize(size);
		hubSupplierSkuDto.setSupplierSkuSizeType(sizeType);
		hubSupplierSkuDto.setSupplierSkuNo(sku.getSkuId());
		hubSupplierSkuDto.setSupplierBarcode(sku.getBarcode());
		if(StringUtils.isNotBlank(product.getMarketPrice())){
			hubSupplierSkuDto.setMarketPrice(new BigDecimal(product.getMarketPrice()));	
		}
		if(StringUtils.isNotBlank(product.getSupplierPrice())){
			hubSupplierSkuDto.setSupplyPrice(new BigDecimal(product.getSupplierPrice()));
		}
		if(StringUtils.isNotBlank(product.getSaleCurrency())){
			hubSupplierSkuDto.setMarketPriceCurrencyorg(product.getSaleCurrency());
		}
		return hubSupplierSkuDto;
	}


	private HubSupplierSpuDto convertProduct2HubSupplierSpu(String supplierId,SpuProductDTO product) {
		HubSupplierSpuDto supplierSpu = new HubSupplierSpuDto();
		supplierSpu.setSupplierBrandname(product.getBrandName());
		supplierSpu.setSupplierCategoryname(product.getCategoryName());
		supplierSpu.setSupplierGender(product.getCategoryGender());
		supplierSpu.setSupplierSpuNo(product.getProductCode()+product.getColor());
		supplierSpu.setSupplierId(supplierId);
		supplierSpu.setSupplierMaterial(product.getMaterial());
		supplierSpu.setSupplierOrigin(product.getProductOrigin());
		supplierSpu.setSupplierSeasonname(product.getSeasonName());
		supplierSpu.setSupplierSpuColor(product.getColor());
		supplierSpu.setSupplierSpuDesc(product.getProductDescription());
		supplierSpu.setSupplierSpuModel(product.getProductCode());
		supplierSpu.setSupplierSpuName(product.getProductName());
		supplierSpu.setUpdateTime(new Date());
		return supplierSpu;
	}
}

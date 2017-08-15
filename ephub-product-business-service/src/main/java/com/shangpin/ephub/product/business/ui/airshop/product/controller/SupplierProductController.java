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

import com.google.gson.Gson;
import com.shangpin.ephub.client.data.airshop.supplier.product.dto.HubSupplierProductRequestWithPage;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.product.business.common.supplier.product.HubSupplierProductService;
import com.shangpin.ephub.product.business.common.supplier.sku.HubSupplierSkuService;
import com.shangpin.ephub.product.business.common.supplier.spu.HubSupplierSpuService;
import com.shangpin.ephub.product.business.conf.stream.source.task.sender.TaskStreamSender;
import com.shangpin.ephub.product.business.ui.airshop.product.dto.PageResponseDTO;
import com.shangpin.ephub.product.business.ui.airshop.product.dto.ProductDTO;
import com.shangpin.ephub.product.business.ui.airshop.product.dto.ProductDetailResponseDTO;
import com.shangpin.ephub.product.business.ui.airshop.product.dto.ProductDetailSkuResponseDTO;
import com.shangpin.ephub.product.business.ui.airshop.product.dto.SkuProductDTO;
import com.shangpin.ephub.product.business.ui.airshop.product.dto.SpuProductDTO;

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
	    TaskStreamSender productImportTaskStreamSender;
	@Autowired
	private HubSupplierProductService hubSupplierProductService;
	@Autowired
	private HubSupplierSkuService hubSupplierSkuService;
	@Autowired
	private HubSupplierSpuService hubSupplierSpuService;
	@Autowired
	HubSpuPendingPicGateWay hubSpuPendingPicGateWay;

    @RequestMapping(value="/selectSupplierProduct",method=RequestMethod.POST)
    public PageResponseDTO pendingList(@RequestBody HubSupplierProductRequestWithPage pendingQuryDto){
    	log.info("请求参数：{}",pendingQuryDto);
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
    		productDetail.setSeason(spu.getSupplierSeasonname());
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
    public void add(@PathVariable("supplierId") String supplierId,@RequestBody SpuProductDTO product){
    	
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
    }

    @RequestMapping(value="/update/{supplierId}",method=RequestMethod.POST)
    public void update(@PathVariable("supplierId") String supplierId,@RequestBody SpuProductDTO product){
    	
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
    }
    
    private static String dateFormat = "yyyy-MM-dd HH:mm:ss";
    @RequestMapping(value="/batchSave/{supplierId}",method=RequestMethod.POST)
    public void batchAdd(@PathVariable("supplierId") String supplierId,@RequestBody List<ProductDTO> products){
    	
    	Gson gson = new Gson();
    	if(products!=null){
    		for(ProductDTO product:products){
		       SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmmssSSS");
               Date date = new Date();
               String taskNo = sim.format(date);
               Task productImportTask = new Task();
               productImportTask.setMessageDate(new SimpleDateFormat(dateFormat).format(new Date()));
               productImportTask.setMessageId(UUID.randomUUID().toString());
               productImportTask.setTaskNo(taskNo);
               productImportTask.setType(18);
               productImportTask.setData(gson.toJson(product));
               log.info("推送任务的参数：{}",productImportTask);
             	productImportTaskStreamSender.hubProductImportTaskStream(productImportTask, null);
    		}
    	}
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
		
		hubSupplierSkuDto.setStock(stock);
		hubSupplierSkuDto.setSupplierSkuSize(sku.getSize());
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
		supplierSpu.setSupplierId(supplierId);
		supplierSpu.setSupplierMaterial(product.getMaterial());
		supplierSpu.setSupplierOrigin(product.getProductOrigin());
		supplierSpu.setSupplierSeasonname(product.getSeasonName());
		supplierSpu.setSupplierSpuColor(product.getColor());
		supplierSpu.setSupplierSpuDesc(product.getProductDescription());
		supplierSpu.setSupplierSpuModel(product.getProductCode());
		supplierSpu.setSupplierSpuName(product.getProductName());
		supplierSpu.setSupplierSpuNo(product.getSpuId());
		supplierSpu.setUpdateTime(new Date());
		return supplierSpu;
	}
}

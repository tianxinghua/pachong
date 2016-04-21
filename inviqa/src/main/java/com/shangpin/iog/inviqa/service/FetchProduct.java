package com.shangpin.iog.inviqa.service;

/**
 * Created by wang on 2015/9/21.
 */

import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;

import net.sf.json.JSONArray;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Token;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuthService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.inviqa.dto.API;
import com.shangpin.iog.inviqa.dto.Product;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

/**
 * Created by 赵根春 on 2015/12/25.
 */
@Component("inviqa")
public class FetchProduct {

	@Autowired
	ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;
	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String MAGENTO_API_KEY = null;
    private static String MAGENTO_API_SECRET = null;
    private static String MAGENTO_REST_API_URL = null;
    private static String token = null;
    private static String secret = null;
	private static String supplierId;
	private Map<String,SkuDTO> skuDTOMap = new HashedMap();
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		
		MAGENTO_API_KEY = bdl.getString("MAGENTO_API_KEY");
		MAGENTO_API_SECRET = bdl.getString("MAGENTO_API_SECRET");
		MAGENTO_REST_API_URL = bdl.getString("MAGENTO_REST_API_URL");
		token = bdl.getString("token");
		secret = bdl.getString("secret");
	}
    
    
	private int sum = 0;
	private int update = 0;
	private int save = 0;
	private int excSum = 0;
	private int j = 0,tempPage=0;
	public  void getProduct(OAuthService service,Token accessToken,int page){
		try{
			OAuthRequest request = new OAuthRequest(Verb.GET,
					MAGENTO_REST_API_URL+"product?limit=100&page="+page,
					service);
			service.signRequest(accessToken, request);
			Response response = request.send();
			String json = response.getBody();
			System.out.println(json);
			if(!json.isEmpty()){
				List<Product> retList = new Gson().fromJson(json,  
		                new TypeToken<List<Product>>() {  
		                }.getType());  
				logger.info("拉取的数量："+retList.size());
				System.out.println("拉取的数量："+retList.size());
				j=0;
				if(retList.size()==100){
					page++;
					sum += 100;
					System.out.println("已拉取的数量："+sum);
					logger.info("已拉取的数量："+sum);
					messMappingAndSave(retList);
					System.out.println("save success");
					getProduct(service,accessToken,page);
				}else{
					messMappingAndSave(retList);
				}
			}
		}catch(Exception ex){
			excSum+=1;
			tempPage = page;
			if(tempPage==page){
				j++;
			}
			logger.info("第"+excSum+"次发生异常,异常原因："+ex.getMessage());
			System.out.println("第"+excSum+"次发生异常,异常原因："+ex.getMessage());
			getProduct(service,accessToken,page);
			if(j==4){
				System.out.println("第四次发生异常"+ex.getMessage());
				ex.printStackTrace();
			}
		}
		
	}
	/**
	 * fetch product and save into db
	 */
	public void fetchProductAndSave() {
		int day = 90;
		Date startDate,endDate= new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
		//获取原有的SKU 仅仅包含价格和库存
		
		try {
			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		OAuthService service = new ServiceBuilder().provider(API.class)
				.apiKey(MAGENTO_API_KEY).apiSecret(MAGENTO_API_SECRET).build();
		Token accessToken = new Token(token,
				secret);
		getProduct(service,accessToken,1);
		logger.info("总共拉取总数："+sum);
		logger.info("总共save总数："+save);
		logger.info("总共update总数："+update);
		logger.info("总共发生异常次数："+excSum);
		System.out.println("总共拉取总数："+sum);
		System.out.println("总共save总数："+save);
		System.out.println("总共update总数："+update);
		System.out.println("总共发生异常次数："+excSum);
		//更新网站不再给信息的老数据
		for(Iterator<Map.Entry<String,SkuDTO>> itor = skuDTOMap.entrySet().iterator();itor.hasNext(); ){
			 Map.Entry<String,SkuDTO> entry =  itor.next();
			if(!"0".equals(entry.getValue().getStock())){//更新不为0的数据 使其库存为0
				entry.getValue().setStock("0");
				try {
					productFetchService.updatePriceAndStock(entry.getValue());
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//  , ]
	
	
	/**
	 * message mapping and save into DB
	 */
	private void messMappingAndSave(List<Product> array) {

		if (array != null) {
			for (Product item : array) {
				if(item.getSkuId()!=null){
					SpuDTO spu = new SpuDTO();
					try {
						spu.setId(UUIDGenerator.getUUID());
						spu.setSupplierId(supplierId);
						spu.setSpuId(item.getSpuId());
						spu.setSpuName(item.getProductName());
						spu.setCategoryName(item.getCategoryName());
						spu.setSubCategoryName(item.getSubCategoryName());
						spu.setBrandName(item.getBrandName());
						spu.setSpuName(item.getProductName());
						if(item.getMaterial()!=null){
							if(item.getMaterial().toLowerCase().indexOf("no data")==-1){
								spu.setMaterial(item.getMaterial());
							}
						}
						if(item.getSeasonName()!=null){
							if(item.getSeasonName().toLowerCase().indexOf("no data")==-1){
								spu.setSeasonId(item.getSeasonName());
								spu.setSeasonName(item.getSeasonName());
							}
						}
						
						if(item.getProductOrigin()!=null){
							if(item.getProductOrigin().toLowerCase().indexOf("no data")==-1){
								spu.setProductOrigin(item.getProductOrigin());
							}
						}
						spu.setCategoryGender(item.getCategoryGender());
						productFetchService.saveSPU(spu);
					} catch (Exception e) {
						try {
							productFetchService.updateMaterial(spu);
						} catch (ServiceException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					
					String image = item.getImages();
					if (StringUtils.isNotBlank(image)) {
						String images[] = image.split("\\|\\|");
						productFetchService.savePicture(supplierId, null,
								item.getSkuId(), Arrays.asList(images));
					}

					SkuDTO sku = new SkuDTO();
					try {
						sku.setId(UUIDGenerator.getUUID());
						sku.setSupplierId(supplierId);
						sku.setSpuId(item.getSpuId());
						String proSize = item.getSize();
						if ("no".equals(proSize.toLowerCase())) {
							sku.setProductSize("A");
						} else {
							sku.setProductSize(proSize);
						}
						sku.setSkuId(item.getSkuId());
						sku.setStock(item.getStock());
						sku.setSalePrice(item.getSalePrice());
						sku.setMarketPrice(item.getMarketPrice());
						if(item.getSupplierPrice()!=null){
							if(item.getSupplierPrice().toLowerCase().indexOf("no data")==-1){
								sku.setSupplierPrice(item.getSupplierPrice());
							}
						}
						if(item.getBarcode()!=null){
							if(item.getBarcode().toLowerCase().indexOf("no data")==-1){
								sku.setBarcode(item.getBarcode());
							}
						}
						sku.setColor(item.getColor());
						sku.setProductName(item.getProductName());
						sku.setProductDescription(item.getProductDescription());
						sku.setProductCode(item.getProductCode());
						sku.setSaleCurrency(item.getSaleCurrency());
						if(skuDTOMap.containsKey(sku.getSkuId())){
							skuDTOMap.remove(sku.getSkuId());
						}
						
						productFetchService.saveSKU(sku);
						save++;
					} catch (ServiceException e) {
						if (e.getMessage().equals("数据插入失败键重复")) {
							try {
								productFetchService.updatePriceAndStock(sku);
								update++;		
							} catch (ServiceException e1) {
								e1.printStackTrace();
							}
						} else {
							e.printStackTrace();
						}

					}
				}
			}
		}
	}
}

package com.shangpin.iog.dante5.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dante5.dto.Item;
import com.shangpin.iog.dante5.dto.Rss;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

/**
 *
 */
@Component("dante5")
public class FetchProduct {
    final Logger logger = Logger.getLogger(this.getClass());
    private static Logger logMongo = Logger.getLogger("mongodb");
    @Autowired
    ProductFetchService productFetchService;
    @Autowired
	ProductSearchService productSearchService;

    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static int day;

    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        day = Integer.valueOf(bdl.getString("day"));
    }

    public void fetchProductAndSave(String url) {
        try {
        	Date startDate,endDate= new Date();
			startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
			//获取原有的SKU 仅仅包含价格和库存
			Map<String,SkuDTO> skuDTOMap = new HashedMap();
			try {
				skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
        	
            Map<String, String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = new OutTimeConfig(1000*60, 1000*60*20,1000*60*20);
//            timeConfig.confRequestOutTime(600000);
//            timeConfig.confSocketOutTime(600000);
            String result = HttpUtil45.get(url, timeConfig, null);
            HttpUtil45.closePool();

            System.out.println("result : " + result);

            if (result == null || "".equals(result)) {
                return;
            }

            XmlParserCreator parserCreator = new XmlParserCreator() {
                @Override
                public XmlPullParser createParser() {
                    try {
                        return XmlPullParserFactory.newInstance().newPullParser();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };

            GsonXml gsonXml = new GsonXmlBuilder()
                    .setSameNameLists(true)
                    .setXmlParserCreator(parserCreator)
                    .create();

            Rss rss = gsonXml.fromXml(result, Rss.class);

            if (rss == null || rss.getChannel() == null) {
                return;
            }

            int count = 0;
            for (Item item : rss.getChannel().getItem()) {
                if (item == null) {
                    continue;
                }

                //库存为0不进行入库
                if (item.getAvailability() == null || "".equals(item.getAvailability().trim()) || "0".equals(item.getAvailability().trim())) {
                    continue;
                }

                System.out.println("count : " + ++count);

                String skuId = item.getId() + item.getSize(); //接口中g:id是spuId,对应不同尺码
                String spuId = item.getId();
                SkuDTO sku = new SkuDTO();

                //SKU 必填
                sku.setId(UUIDGenerator.getUUID());
                sku.setSupplierId(supplierId);
                sku.setSkuId(skuId);
                sku.setSpuId(spuId);
                String marketPrice = item.getMarketPrice();
                if (marketPrice != null && !"".equals(marketPrice)) {
                    marketPrice = marketPrice.replace("EUR", "").replace(",", ".").trim();
                }
                String supplierPrice = item.getSupplierPrice();
                if (supplierPrice != null && !"".equals(supplierPrice)) {
                    supplierPrice = supplierPrice.replace("EUR", "").replace(",", ".").trim();
                }

                sku.setSaleCurrency("EUR");
                sku.setMarketPrice(marketPrice);
                sku.setSupplierPrice(supplierPrice);
                sku.setColor(item.getColor());
                sku.setProductSize(item.getSize());
                sku.setStock(item.getAvailability());

                //SKU 选填
                sku.setProductName(item.getTitle());
                sku.setProductDescription(item.getDescription());
                sku.setProductCode(item.getMpn());

//                System.out.println("sku : " + sku);

                try {
                	
                	if(skuDTOMap.containsKey(sku.getSkuId())){
						skuDTOMap.remove(sku.getSkuId());
					}
                	
                    productFetchService.saveSKU(sku);
                } catch (ServiceException e) {
                    try {
                        if (e.getMessage().equals("数据插入失败键重复")) {
                            //更新价格和库存
                            productFetchService.updatePriceAndStock(sku);
                        } else {
                            e.printStackTrace();
                        }
                    } catch (ServiceException e1) {
                        e1.printStackTrace();
                    }
                }

                //保存图片
                if (item.getImageLinks() != null) {
                	 productFetchService.savePicture(supplierId, spuId, null, item.getImageLinks().getLinks());
                }

                //保存SPU
                SpuDTO spu = new SpuDTO();
                //SPU 必填
                spu.setId(UUIDGenerator.getUUID());
                spu.setSpuId(spuId);
                spu.setSupplierId(supplierId);
                spu.setCategoryName(item.getProductType());
                spu.setBrandName(item.getBrand());
                spu.setMaterial(item.getComposition());
                spu.setSeasonName(item.getCustomLabel0());

                //SPU选填
                spu.setCategoryGender(item.getGender());

                try {
                    productFetchService.saveSPU(spu);
                } catch (ServiceException e) {
                    try {
                        if (e.getMessage().equals("数据插入失败键重复")) {
                            //更新价格和库存
                            productFetchService.updateMaterial(spu);
                        } else {
                            e.printStackTrace();
                        }
                    } catch (ServiceException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
            
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

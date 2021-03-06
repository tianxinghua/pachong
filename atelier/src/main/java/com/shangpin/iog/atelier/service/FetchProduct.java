package com.shangpin.iog.atelier.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.atelier.common.MyStringUtil;
import com.shangpin.iog.atelier.common.WS_Sito_P15;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;

/**
 * Created by wangyuzhi on 2015/9/30
 */
@Component("atelier")
public class FetchProduct {

    private final Logger logger = Logger.getLogger("info");
//    private final String supplierId = "2015093001426";
    private static String supplierId;
    private WS_Sito_P15 atelier = new WS_Sito_P15();
    private static ResourceBundle bdl=null;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }
    @Autowired
    ProductFetchService productFetchService;
    /**
     * 主处理
     */
    public void fetchProductAndSave() {
        //fetch product
        atelier.fetchProduct();
        //save into DB
        messMappingAndSave(atelier.getAllItemsStr().split("\\n"));

    }


    /**
     * save items into DB
     * **/
    private void messMappingAndSave(String[] items) {
//        String stocks = atelier.getAllAvailabilityStr(); 
        String[] skus = atelier.getAllAvailabilityStr().split("\\n");
        String pictrues = atelier.getAllImageStr();

        //将所有的itemid,item存入map集合
        Map<String,String> itemIdMap = new HashMap<String,String>();
        for (String item : items) {
        	String[] fields = item.split(";");
        	String itemid = fields[0];
        	itemIdMap.put(itemid, item);
        }
        //遍历availability的skus保存sku信息
        for (int i = 1; i < skus.length; i++) {
        	String[] skuDetails = skus[i].split(";");
        	String itemid = skuDetails[0];
        	if (itemIdMap.containsKey(itemid)) {
        		String itemDetail = itemIdMap.get(itemid);
        		String[] fields = itemDetail.split(";");
        		SkuDTO sku = new SkuDTO();
        		try {
        			sku.setId(UUIDGenerator.getUUID());
        			sku.setSupplierId(supplierId);
        			sku.setSpuId(itemid);
        			//sku.setSkuId(skuId);
        			sku.setProductSize(skuDetails[1]);
        			sku.setMarketPrice(fields[16]);
        			sku.setSalePrice(fields[16]);
        			sku.setSupplierPrice(fields[16]);
        			sku.setColor(fields[10]);
        			sku.setProductDescription(fields[15]);
        			sku.setSaleCurrency("");
        			String stock = skuDetails[2];
        			String barCode = skuDetails[5];
        			sku.setStock(stock);
        			//skuid+barcode
        			sku.setSkuId(itemid+"-"+barCode);
        			sku.setBarcode(barCode);
        			sku.setProductCode(fields[0]);
        			// sku.setProductName(fields[14]);
        			productFetchService.saveSKU(sku);
        			
        			String skuPic = null;
        			if(pictrues.contains(itemid)){
        				skuPic = pictrues.substring(pictrues.indexOf(itemid),pictrues.lastIndexOf(itemid)+16);
        			}
        			if (StringUtils.isNotBlank(skuPic)) {
        				String[] picArray = MyStringUtil.getPicUrl(itemid,skuPic);
//                             List<String> picUrlList = Arrays.asList(picArray);
        				for (int x = 1; x < picArray.length; x++) {
        					ProductPictureDTO dto = new ProductPictureDTO();
        					dto.setPicUrl(picArray[x].split(";")[0]);
        					dto.setSupplierId(supplierId);
        					dto.setId(UUIDGenerator.getUUID());
        					dto.setSkuId(itemid+"-"+barCode);
        					try {
        						productFetchService.savePictureForMongo(dto);
        					} catch (ServiceException e) {
        						e.printStackTrace();
        					}
        				}
        			}
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
			}
        }
        //遍历item保存spu信息
        for (String item : items) {
        	 String[] fields = item.split(";");
        	 SpuDTO spu = new SpuDTO();
           try {
               spu.setId(UUIDGenerator.getUUID());
               spu.setSupplierId(supplierId);
               spu.setSpuId(fields[0]);
               spu.setBrandName(fields[2]);
               spu.setCategoryName(fields[8]);
               //spu.setSpuName(fields[0]);
               spu.setSeasonId(fields[6]);
               spu.setMaterial(fields[11]);
               if (StringUtils.isNotBlank(fields[42])&&StringUtils.isBlank(fields[11])) {
            	   spu.setMaterial(fields[11]);
               }
               spu.setCategoryGender(fields[5]);
               spu.setProductOrigin(fields[40]);
               productFetchService.saveSPU(spu);
           } catch (ServiceException e) {
               e.printStackTrace();
           }
        }
//        int a = 0;
//        for (String item : items) {
//            a++;
//        	String[] fields = item.split(";");
//            String skuId = fields[0];
//            
//            SkuDTO sku = new SkuDTO();
//            try {
//                sku.setId(UUIDGenerator.getUUID());
//                sku.setSupplierId(supplierId);
//                sku.setSpuId(skuId);
//                //sku.setSkuId(skuId);
//                sku.setProductSize(fields[34]);
//                sku.setMarketPrice(fields[16]);
//                sku.setSalePrice(fields[16]);
//                sku.setSupplierPrice(fields[16]);
//                sku.setColor(fields[10]);
//                sku.setProductDescription(fields[15]);
//                String stock = "0";
//                String barCode = "";
//                if(stocks.contains(skuId)){
//                    int index = stocks.indexOf(skuId);
//                    stock = MyStringUtil.getStockBySkuId(stocks.substring(index,index+20));
//                    barCode = MyStringUtil.getBarcodeBySkuId(stocks.substring(index,index+50));
//                }
//                sku.setStock(stock);
//                //skuid+barcode
//                sku.setSkuId(skuId+"-"+barCode);
//                sku.setBarcode(barCode);
//                sku.setProductCode(fields[0]);
//               // sku.setProductName(fields[14]);
//                productFetchService.saveSKU(sku);
//
//                String skuPic = null;
//                if(pictrues.contains(skuId)){
//                    skuPic = pictrues.substring(pictrues.indexOf(skuId),pictrues.lastIndexOf(skuId)+16);
//                }
//                if (StringUtils.isNotBlank(skuPic)) {
//                    String[] picArray = MyStringUtil.getPicUrl(skuId,skuPic);
////                            List<String> picUrlList = Arrays.asList(picArray);
//                    for (int i = 1; i < picArray.length; i++) {
//                        ProductPictureDTO dto = new ProductPictureDTO();
//                        dto.setPicUrl(picArray[i].split(";")[0]);
//                        dto.setSupplierId(supplierId);
//                        dto.setId(UUIDGenerator.getUUID());
//                        dto.setSkuId(skuId+"-"+barCode);
//                        try {
//                            productFetchService.savePictureForMongo(dto);
//                        } catch (ServiceException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            } catch (ServiceException e) {
//                try {
//                    if (e.getMessage().equals("数据插入失败键重复")) {
//                        //更新价格和库存
//                        productFetchService.updatePriceAndStock(sku);
//                    } else {
//                        e.printStackTrace();
//                    }
//
//                } catch (ServiceException e1) {
//                    e1.printStackTrace();
//                }
//            }
//
//            SpuDTO spu = new SpuDTO();
//            try {
//                spu.setId(UUIDGenerator.getUUID());
//                spu.setSupplierId(supplierId);
//                spu.setSpuId(skuId);
//                spu.setBrandName(fields[2]);
//                spu.setCategoryName(fields[8]);
//                //spu.setSpuName(fields[0]);
//                spu.setSeasonId(fields[6]);
//                spu.setMaterial(fields[11]);
//                spu.setCategoryGender(fields[5]);
//                spu.setProductOrigin(fields[40]);
//                productFetchService.saveSPU(spu);
//            } catch (ServiceException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void testPost(){
        //new FetchProduct().returnRe();
        System.out.println("-----------------------------------------------------------------------");
        //new FetchProduct().test();
        // new FetchProduct().getAllPricelistMarketplace();
/*        String s = new FetchProduct().readAtelier();
        System.out.println(s);
        System.out.println(s.split("\\n").length);*/
        String kk = HttpUtil45.post("http://109.168.12.42/ws_sito_P15/ws_sito_p15.asmx/GetAllImageMarketplace",
                new OutTimeConfig(1000*60*1,1000*60*1,1000*60*1));
        System.out.println("return kk = " + kk);
        /*kk = kk.replaceAll("&lt;","");
        kk = kk.replaceAll("&gt;","");
        kk = kk.replaceAll("&amp;","");
        String[] arr = kk.split("\\n");
        System.out.println(arr.length);
        for(String str:arr){
            System.out.print(str.split(";").length + " ");
            if (str.split(";").length == 50){
                System.out.println(str);
            }
        }
        System.out.println();
        System.out.println("-------------------------------sex--------------------------------");
        for(String str:arr){
            if (str.split(";").length>=48){
                System.out.print(str.split(";")[5]+" ");
            }
        }
        System.out.println();
        System.out.println("--------------------------------price-------------------------------");
        for(String str:arr){
            if (str.split(";").length>=48){
                System.out.print(str.split(";")[16]+" ");
            }
        }
        System.out.println();
        System.out.println("--------------------------------date-------------------------------");
        for(String str:arr){
            if (str.split(";").length>=48){
                System.out.print(str.split(";")[35]+" ");
            }
        }*/
    }

    /**
     * test
     * */
    public static void main(String[] args) throws IOException {
/*        String kk = "263816;FW15;Givenchy;BB05253004;001;Woman;Fall Winter;Bags;Shoulder Bags;Pandora Mini;Black;;;;Mini black washed leather Pandora bag:     detachable leather shoulder strap one leather handle  double closure with zipper  inside pocket with zipper cotton cloth lining;Givenchy: Black Washed Leather Mini Pandora Bag;792;792;;792;;NO;1;Blacks and greys;;0;Height;Width;Depth;Shoulder strap;Handles;;;;;01/01/1998 00:00:00;;;0;;;;;;Completa;NC;Main;Bags";
        String[] array = kk.split(";");
        System.out.println("   " + array.length);*/
    	new FetchProduct().testPost();
    }
}

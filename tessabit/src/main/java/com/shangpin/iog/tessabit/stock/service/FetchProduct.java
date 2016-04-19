package com.shangpin.iog.tessabit.stock.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.tessabit.stock.common.Constant;
import com.shangpin.iog.tessabit.stock.common.MyFtpClient;
import com.shangpin.iog.tessabit.stock.common.StringUtil;
import com.shangpin.iog.tessabit.stock.dto.Item;
import com.shangpin.iog.tessabit.stock.dto.Items;
import com.shangpin.iog.tessabit.stock.dto.Picture;
import com.shangpin.iog.tessabit.stock.dto.Product;
import com.shangpin.iog.tessabit.stock.dto.Products;
import com.shangpin.iog.tessabit.stock.dto.Riferimenti;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sun.net.ftp.FtpClient;

import javax.xml.bind.JAXBException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by wangyuzhi on 2015/9/10.
 */
@Component("tessabit")
public class FetchProduct {

    final Logger logger = Logger.getLogger("info");

    private static Logger loggerError = Logger.getLogger("error");
    @Autowired
    ProductFetchService productFetchService;
    /**
     * 主处理
     */
    public void fetchProductAndSave() {
        //拉取FTP文件
        logger.info("downLoad ftpFile begin......");
        //
        new MyFtpClient().downLoad("0",Constant.PICTURE_FILE);
        new MyFtpClient().downLoad("1",Constant.SERVER_FILE);
        logger.info("downLoad ftpFile end......");
        //入库处理
        logger.info("save products into DB begin......");
        Products products = null;
        Map<String,String> pictureMap = null;
        try {
        	//将FTP拉取到的图片xml文件转换成模型数据
        	pictureMap = getPictureMap();
            // 将FTP拉取到的数据xml文件转换成模型数据--delete from SKU where SUPPLIER_ID = '2015091701503';
        	//--delete from SPU where SUPPLIER_ID = '2015091701503';
        	
            products = ObjectXMLUtil.xml2Obj(Products.class, new File(Constant.LOCAL_FILE));
            System.out.println(products.getProducts().size());
        } catch(  JAXBException e  )  {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //映射数据并保存
        messMappingAndSave(products,pictureMap);
        logger.info("save products into DB end......");

    }
    private static Map<String,String> getPictureMap() {
    	 Map<String,String> map = new HashMap<String,String>();
			 try {
				
				 Picture p= ObjectXMLUtil.xml2Obj(Picture.class,new File(Constant.LOCAL_PICTURE));
				 List<Riferimenti> list = p.getList();
				 for(Riferimenti r:list){
					 if(map.containsKey(r.getRF_RECORD_ID())){
						 map.put(r.getRF_RECORD_ID(),map.get(r.getRF_RECORD_ID())+","+r.getRIFERIMENTO());
					 }else{
						 map.put(r.getRF_RECORD_ID(), r.getRIFERIMENTO());
					 }
				 }
				 System.out.println("s");
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 return map;
		}
    /**
     * 映射数据并保存
     */
    private void messMappingAndSave(Products products,Map<String,String> pictureMap) {
        List<Product> productList = products.getProducts();
        int i =0,j=0;
        for(Product product:productList){
            SpuDTO spu = new SpuDTO();

            Items items = product.getItems();
            if(null==items){//判断SKU
                continue;
            }

            List<Item> itemList = items.getItems();
            if(null==itemList) continue;
            String skuId = "";

            for(Item item:itemList){
                SkuDTO sku  = new SkuDTO();
                try {
                    sku.setId(UUIDGenerator.getUUID());
                    sku.setSupplierId(Constant.SUPPLIER_ID);

                    sku.setSpuId(product.getProductId());
                    skuId = item.getItem_id();
                    sku.setSkuId(StringUtil.convertStr(skuId));
                    sku.setProductSize(StringUtil.convertStr(item.getItem_size()));
                    sku.setMarketPrice(item.getMarket_price());
                    sku.setSalePrice(item.getSell_price());
                    sku.setSupplierPrice(item.getSupply_price());
                    sku.setColor(item.getColor());
                    sku.setProductDescription(item.getDescription());
                    if("0".equals(item.getStock())){
                        j++;
                        loggerError.error("库存为0的sku=" + skuId);
                    }
                    sku.setStock(item.getStock());
                    sku.setBarcode(item.getBarcode());
                    sku.setProductCode(product.getProducer_id());
                    productFetchService.saveSKU(sku);
                } catch (ServiceException e) {
                    try {
                        if(e.getMessage().equals("数据插入失败键重复")){
                            //更新价格和库存
                            productFetchService.updatePriceAndStock(sku);
                        } else{
                            e.printStackTrace();
                        }

                    } catch (ServiceException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            try {
                spu.setId(UUIDGenerator.getUUID());
                spu.setSupplierId(Constant.SUPPLIER_ID);
                spu.setSpuId(product.getProductId());
                spu.setBrandName(product.getProduct_brand());
                spu.setCategoryName(product.getCategory());
                spu.setSpuName(product.getProduct_name());
                spu.setSeasonId(product.getSeason_code());
                spu.setMaterial(product.getProduct_material());
                spu.setCategoryGender(product.getGender());
                productFetchService.saveSPU(spu);
                if(pictureMap!=null){
                    String proId = product.getProductId();
                    if(pictureMap.containsKey(proId)&&pictureMap.get(proId)!=null){
                    	String [] arr = pictureMap.get(proId).split(",");
                    	for(String picUrl :arr){
                            ProductPictureDTO dto  = new ProductPictureDTO();
                            dto.setPicUrl("http://partners.tessabit.biz/partner/"+picUrl);
                            dto.setSupplierId(Constant.SUPPLIER_ID);
                            dto.setId(UUIDGenerator.getUUID());
                            dto.setSpuId(proId);
                            try {
                                productFetchService.savePictureForMongo(dto);
                            } catch (ServiceException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            
        }
        loggerError.error("库存为0的总数是=" + j+ "--");
        loggerError.error("无图片的总数是---" +i + "----");
    }

    /**
     * test
     * @param args
     */
    public static void main(String[] args){
        new FetchProduct().fetchProductAndSave();
    }

}


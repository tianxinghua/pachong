package com.shangpin.iog.theclutcher.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.ResourceBundle;

import javax.xml.bind.JAXBException;

import com.shangpin.iog.theclutcher.dao.ImageLinks;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.theclutcher.Startup;
import com.shangpin.iog.theclutcher.dao.Item;
import com.shangpin.iog.theclutcher.dao.Rss;
import com.shangpin.iog.theclutcher.utils.DownloadFileFromNet;
import com.shangpin.iog.theclutcher.utils.UNZIPFile;
import com.shangpin.iog.theclutcher.utils.XMLUtil;

@Component("theclutcher")
public class FetchProduct {

	private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ResourceBundle bdl=null;
    private static String supplierId = "";
    
    private static String localPathDefault = ""; //
    
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        localPathDefault = bdl.getString("local.filePath");
    }
    
    @Autowired
    ProductFetchService productFetchService;
    
	public void fetchProductAndSave(String urlStr,String fileName){
		
		String localPath = "";//存放下载的zip文件的本地目录
		try {
			localPath = URLDecoder.decode((Startup.class.getClassLoader().getResource("").getFile()), "utf-8");
		} catch (UnsupportedEncodingException e) {
			localPath = localPathDefault;
			loggerError.info(e.getMessage());
			e.printStackTrace();
		}
		try {
			//下载
			File zipFile = DownloadFileFromNet.downLoad(urlStr, fileName, localPath);
//			//解压
			File xmlFile = UNZIPFile.unZipFile(zipFile,localPath);//(new File("C:\\Users\\suny\\git\\iog\\theclutcher\\bin\\feedShangping.zip"), localPath);
//			//读取文件
			String result = DownloadFileFromNet.file2Striing(xmlFile);//(new File("C:\\Users\\suny\\git\\iog\\theclutcher\\bin\\feedShangping.xml"));//+"</item></channel></rss>";

			//字符串转对象
			try {
				
				Rss rss = XMLUtil.gsonXml2Obj(Rss.class, result);
				
				if (rss == null || rss.getChannel() == null) {
	                return;
	            }
	            int count = 0;
				String markerPrice ="",supplier_price ="";
				for (Item item : rss.getChannel().getItem()) {
	                if (item == null) {
	                    continue;
	                }

	                //库存为0不进行入库
	                if (item.getAvailability() == null || "".equals(item.getAvailability().trim()) || "0".equals(item.getAvailability().trim())) {
	                    continue;
	                }

	                System.out.println("count : " + ++count);
	                
	                String size = item.getSize();
					if(StringUtils.isNotBlank(size)){
						if(size.indexOf("½")>0){
							size = size.substring(0, size.indexOf("½")-1)+".5";
						}
					}else {
						size ="";
					}


	                String skuId = item.getId() +"-"+ size; //接口中g:id是spuId,对应不同尺码
	                String spuId = item.getId();
	                SkuDTO sku = new SkuDTO();

	                //SKU 必填
	                sku.setId(UUIDGenerator.getUUID());
	                sku.setSupplierId(supplierId);
	                sku.setSkuId(skuId);
	                sku.setSpuId(spuId);
					markerPrice = item.getItalianRetailPrice();
	                if (markerPrice != null && !"".equals(markerPrice)) {
						markerPrice = markerPrice.replace("€", "").trim();
						sku.setMarketPrice(markerPrice);
	                }
	                sku.setSaleCurrency("EUR");

					supplier_price= item.getDiscountToShangpin();
					if (supplier_price != null && !"".equals(supplier_price)) {
						supplier_price = supplier_price.replace("€", "").trim();
						sku.setSupplierPrice(supplier_price);
					}
	                sku.setColor(item.getColor());
	                sku.setProductSize(size);
	                sku.setStock(item.getAvailability());

	                //SKU 选填
	                sku.setProductName(item.getTitle());
	                sku.setProductDescription(item.getDescription());
	                sku.setProductCode(item.getMpn().replaceAll("," , "."));
	                
	                System.out.println("sku : " + sku);

	                try {
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
	                if (item.getImage_links() != null) {
	                    for (String  imageUrl: item.getImage_links().getLinks()) {
	                        if (imageUrl != null ) {
	                            ProductPictureDTO dto = new ProductPictureDTO();
	                            dto.setPicUrl(imageUrl);
	                            dto.setSupplierId(supplierId);
	                            dto.setId(UUIDGenerator.getUUID());
	                            dto.setSkuId(skuId);
	                            try {
	                                productFetchService.savePictureForMongo(dto);
	                            } catch (ServiceException e) {
	                                e.printStackTrace();
	                            }
	                        }
	                    }
	                }

	                //保存SPU
	                SpuDTO spu = new SpuDTO();
	                //SPU 必填
	                spu.setId(UUIDGenerator.getUUID());
	                spu.setSpuId(spuId);
	                spu.setSupplierId(supplierId);
	                spu.setCategoryName(item.getProduct_type());
	                spu.setBrandName(item.getBrand());
	                spu.setMaterial(item.getMaterial());
					spu.setSeasonName(item.getSeason());
	                spu.setCategoryGender(item.getGender());

	                try {
	                    productFetchService.saveSPU(spu);
	                } catch (ServiceException e) {
	                    e.printStackTrace();
	                }
	            }
				
			} catch (Exception e) {
				loggerError.info(e.getMessage());
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			loggerError.info(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		String result="<rss xmlns:g=\"http://base.google.com/ns/1.0\" version=\"2.0\">\n" +
				"  <channel>\n" +
				"    <title>theclutcher</title>\n" +
				"    <link>http://www.theclutcher.com</link>\n" +
				"    <description>All products for theclutcher</description>\n" +
				"    <item>\n" +
				"      <title>Cantarelli GREY wool suit</title>\n" +
				"      <link>https://www.theclutcher.com/en-US/product/2146?adwS=shanping&amp;adwC=shanping&amp;nat=HK</link>\n" +
				"      <description>GREY PINSTRIPED WOOL SINGLE-BREASTED SUIT. JACKET: THREE BUTTONS STYLE WITH NOTCHED LAPELS, TWO FLAP POCKETS AND BREAST POCKET, DOUBLE REAR VENT. TROUSERS: OBLIQUE LATERAL POCKETS AND BUTTONED WELT POCKETS ON THE BACK.</description>\n" +
				"      <material>composition: 100% wool, lining 100% viscose  MODEL size: 50</material>\n" +
				"      <made />\n" +
				"      <id>2146</id>\n" +
				"      <mpn>208-32253191-11667</mpn>\n" +
				"      <brand>Cantarelli</brand>\n" +
				"      <product_type>Abbigliamento</product_type>\n" +
				"      <gender>male</gender>\n" +
				"      <color>Grigio</color>\n" +
				"      <size>IT 46</size>\n" +
				"      <availability>1</availability>\n" +
				"      <season>autunno_inverno_2013_2014</season>\n" +
				"      <italianRetailPrice>€ 898.00</italianRetailPrice>\n" +
				"      <discountToShangpin>€ 518.14</discountToShangpin>\n" +
				"      <image_links>\n" +
				"        <image_link>https://barcestorage.blob.core.windows.net/product/2146/original/0590c774-c150-474f-bdff-ff21ba672990.jpg</image_link>\n" +
				"        <image_link>https://barcestorage.blob.core.windows.net/product/2146/original/2a23d481-a4a0-42a9-bbb6-58091b17a656.jpg</image_link>\n" +
				"        <image_link>https://barcestorage.blob.core.windows.net/product/2146/original/1c193e1b-6465-45fd-879c-65b65a06d781.jpg</image_link>\n" +
				"        <image_link>https://barcestorage.blob.core.windows.net/product/2146/original/edb995f4-e11e-40e7-a144-c442f4a43115.jpg</image_link>\n" +
				"      </image_links>\n" +
				"    </item>\n" +
				"   </channel>\n" +
				"</rss>";

		try {
			Rss rss = XMLUtil.gsonXml2Obj(Rss.class, result);
			Item item = null;
			try {
				item = rss.getChannel().getItem().get(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (String  imageUrl: item.getImage_links().getLinks()) {
				if (imageUrl != null ) {
					ProductPictureDTO dto = new ProductPictureDTO();
					dto.setPicUrl(imageUrl);
					dto.setSupplierId(supplierId);
					dto.setId(UUIDGenerator.getUUID());

				}
			}
			System.out.println("");
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
    
	
}
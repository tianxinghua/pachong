package com.shangpin.iog.lungo.util;

import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("lungolivigno")
public class FetchProduct {
	private static Logger log = Logger.getLogger("info");

	@Autowired
	ProductFetchService productFetchService;

	private List<String> getCategory(String uri) {
		List<String> list = new ArrayList<String>();
		try {
			HttpResponse response = HttpUtils.get(uri);
			if (response.getStatus()==200) {
				String htmlContent = response.getResponse();
				Document doc = Jsoup.parse(htmlContent);
				Elements ele1 = doc.select("div[class=container group]");
				Elements categorys = ele1.select("#controls").select("a");
				for (Element category : categorys) {
					if (!uri.isEmpty()) {
						uri = ((Element) category.select("a").get(0)).ownText();
					}
					list.add(uri);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void fetchProduct(String url) throws Exception {
	
	//分类
	List<String> categoryList = getCategory(url);
  
    String htmlContent = "";
    Document doc = null;
    int num = 0;
    
    for(String uri:categoryList){
          HttpResponse response = HttpUtils.get("http://www.lungolivignofashion.com/collection/#"+uri);
          if (response.getStatus() == 200) {
            htmlContent = response.getResponse();
            doc = Jsoup.parse(htmlContent);
            Elements pages = doc.select("#main");
            Elements page = pages.select("div.box");
        	for (Element category : page) {
        		
        		Elements image = category.select("div[class=image]");
        		Elements images= image.select("div[class=img_eventi]");
				if (!uri.isEmpty()) {
					uri = ((Element) category.select("a").get(0)).ownText();
				}
			}
          }
    }
//            if (null != page) {
//              maxPage = Integer.valueOf(((Element)page.select("a").get(0)).ownText());
//            }
//            for (int i = 1; i <= maxPage.intValue(); ++i)
//            {
////              response = HttpUtils.get("www.farfetch.com" + visitUrl + "?page=" + i);
//
//              if (response.getStatus() == 200) {
//                htmlContent = response.getResponse();
//                doc = Jsoup.parse(htmlContent);
//                Elements as = doc.select("div[class=listing-item-image]").select("a");
//                for (Element a : as) {
//                  ++num;
//                  System.out.println(num);
//                  log.info(num + "*" + a.attr("href"));
//                  LinkQueue.addAllSkuUrl(a.attr("href") + "|" + category);

//                    all = LinkQueue.allSkuUrlDeQueue();
//                    visitUrl = all.split("\\|")[0];
//                    category = all.split("\\|")[1];
//                      response = HttpUtils.get("www.farfetch.com" + visitUrl);
                      
//                        for (Iterator i$ = doc.select("a[class=relative js-video-thumb]").iterator(); i$.hasNext(); ) { ele = (Element)i$.next();
////                          img = img + ele.select("img").attr("src") + ";";
//                        }

//                        Element ele;
//                        brandName = ((Element)doc.select("a[data-tstid=Label_ItemBrand]").get(0)).ownText();
//                        for (i$ = doc.select("div[data-tstid=Content_Composition&Care]").select("dd").iterator(); i$.hasNext(); ) { ele = (Element)i$.next();
//                          if (ele.ownText().contains(">")) {
//                            matrial = matrial + ele.ownText().split(">")[1] + ";";
//                          }
//                        }
//                        skuName = ((Element)((Element)doc.select("h1[class=detail-brand detail-name]").get(0)).select("span").get(0)).ownText();
//                        description = ((Element)doc.select("p[itemprop=description]").get(0)).ownText();
//                        skuId = ((Element)doc.select("span[itemprop=sku]").get(0)).ownText();
//                        gender = ((Element)doc.select("#divBreadCrumbInformation").select("a").get(1)).ownText();

//                        SkuDTO sku = new SkuDTO();
//                        SpuDTO spu = new SpuDTO();
//                        spu.setId(UUIDGenerator.getUUID());
//                        sku.setId(UUIDGenerator.getUUID());
//                        spu.setBrandName(brandName);
//                        spu.setSpuId(skuId);
//                        spu.setCategoryGender(gender);
//                        spu.setCategoryName(category);
//                        spu.setMaterial(matrial);
//                        spu.setSupplierId("201601281522");
//
//                        sku.setSupplierId("201601281522");
//                        sku.setSpuId(skuId);
//                        sku.setSkuId(skuId);
//                        sku.setSaleCurrency("www.farfetch.com" + visitUrl);
//
//                        sku.setProductName(skuName);
//                        sku.setProductDescription(description);
//                        sku.setStock("1");
//                        System.out.println("save sku");
//                        this.productFetchService.saveSKU(sku);
//                        System.out.println("save spu");
//                        this.productFetchService.saveSPU(spu);
//                        System.out.println("save spic");
//                        this.productFetchService.savePicture("201601281522", null, skuId, Arrays.asList(img.split(";")));
    }

	// }

	public static void main(String[] args) {
		String[] feeds = { "www.farfetch.com/cn/shopping/women/smets/items.aspx?q=smets" };
	}
}
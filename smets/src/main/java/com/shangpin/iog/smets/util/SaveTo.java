package com.shangpin.iog.smets.util;

import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.smets.queue.PicQueue;
import com.shangpin.iog.smets.queue.SkuQueue;
import com.shangpin.iog.smets.queue.SpuQueue;

public class SaveTo extends Thread{
	private String visitUrl1;
	private int i;
	private int num;
	private String category;
	private String supplierId;
	private ProductFetchService productFetchService;
	
	
	public SaveTo(String visitUrl1, int i, int num, String category,
			String supplierId, ProductFetchService productFetchService) {
		super();
		this.visitUrl1 = visitUrl1;
		this.i = i;
		this.num = num;
		this.category = category;
		this.supplierId = supplierId;
		this.productFetchService = productFetchService;
	}


	@Override
	public void run() {
		
		//具体页码
		HttpResponse response1 = null;
		try {
			response1 = HttpUtils.get("www.farfetch.com"+visitUrl1+"?page="+i);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		System.out.println("www.farfetch.com"+visitUrl1+"?page="+i);
		if(response1.getStatus()==200){
			String htmlContentSku = response1.getResponse();
			Document skudoc = Jsoup.parse(htmlContentSku);
			Elements as = skudoc.select("div[class=listing-item-image]").select("a");
			for (Element a : as) {
				num++;
				System.out.println(num+"*"+a.attr("href"));
					try {
						response1 = HttpUtils.get("www.farfetch.com"+a.attr("href"));
						if(response1.getStatus()==200){
							String img = "";
							String matrial = "";
							String brandName = "";
							String description = "";
							String skuName = "";
							String gender = "";
							String skuId = "";
							
							String htmlContentdet = response1.getResponse();
							Document docdet = Jsoup.parse(htmlContentdet);
							for(Element ele:docdet.select("a[class=relative js-video-thumb]")){
								img += ele.select("img").attr("src")+";";
							}
							brandName = docdet.select("a[data-tstid=Label_ItemBrand]").get(0).ownText();
							for(Element ele:docdet.select("div[data-tstid=Content_Composition&Care]").select("dd")){
								if (ele.ownText().contains(">")) {
									matrial +=ele.ownText().split(">")[1]+";";
								}
							}
							skuName = docdet.select("h1[class=detail-brand detail-name]").get(0).select("span").get(0).ownText();
							description = docdet.select("p[itemprop=description]").get(0).ownText();
							skuId = docdet.select("span[itemprop=sku]").get(0).ownText();
							gender = docdet.select("#divBreadCrumbInformation").select("a").get(1).ownText();
							//保存到数据库
							SkuDTO sku = new SkuDTO();
							SpuDTO spu = new SpuDTO();
							spu.setId(UUIDGenerator.getUUID());
							sku.setId(UUIDGenerator.getUUID());
							spu.setBrandName(brandName);
							spu.setSpuId(skuId);
							spu.setCategoryGender(gender);
							spu.setCategoryName(category);
							spu.setMaterial(matrial);
							spu.setSupplierId(supplierId);
							
							
							sku.setSupplierId(supplierId);
							sku.setSpuId(skuId);
							sku.setSkuId(skuId);
							sku.setSaleCurrency("www.farfetch.com"+a.attr("href"));
							sku.setProductName(skuName);
							sku.setProductDescription(description);
							sku.setStock("1");
							//sku 入队
							System.out.println("sku入队~~~~~~~~~~~~~~~~");
							SkuQueue.addUnvisitedSku(sku);
							//spu 入队
							System.out.println("spu入队~~~~~~~~~~~~~~~~~");
							SpuQueue.addUnvisitedSpu(spu);
							//pic 入队
							System.out.println("pic入队~~~~~~~~~~~~~~~~~~~");
							PicQueue.addUnvisitedUrl(skuId+"^"+img);
							img = "";
							skuId = "";
							brandName = "";
							description = "";
							matrial = "";
							skuName = "";
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
//				}
				
				
				
			}
		}
	
	}
	public static void main(String[] args) {
		String a = "asdasd^ssss";
		System.out.println(a.split("\\^")[0]);
	}
}

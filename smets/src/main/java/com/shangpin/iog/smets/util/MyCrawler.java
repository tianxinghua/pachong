package com.shangpin.iog.smets.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
@Component("mycrawler")
public class MyCrawler {
	private static Logger log = Logger.getLogger("info");
	// feed
	//  www.farfetch.com/cn/shopping/women/smets/items.aspx?q=smets
	//	www.farfetch.com/cn/shopping/men/smets/items.aspx?q=smets
	@Autowired
	ProductFetchService productFetchService;
	private LinkedList<String> initCrawlerWithSeeds(String[] feeds){
		LinkedList<String> relist = new LinkedList<String>();
		for (String feed : feeds) {
			//访问feed 获取品类url
			LinkedList<String> list = getSubUrl(feed);
			relist.addAll(list);
		}
		return relist;
	}
	private LinkedList<String> getSubUrl(String uri){
		LinkedList<String> list = new LinkedList<String>();
		try {
			HttpResponse response = HttpUtils.get(uri);
			if (response.getStatus()==200) {
				String htmlContent = response.getResponse();
				Document doc = Jsoup.parse(htmlContent);
				Elements ele1 = doc.select("#lp-filters");
				Elements categorys = ele1.select("li[class=facet-item tree-item]");
				for (Element category : categorys) {
					 uri = category.select("a").get(0).attr("href");
					 list.add(uri+"|"+category.select("a").get(0).attr("title"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public void crawling(String[] feeds){
		LinkedList<String> linkedList = initCrawlerWithSeeds(feeds);
		int num = 0;
		
		for (String string : linkedList) {
				String all1 = string;
				String visitUrl1 = all1.split("\\|")[0];
				String category = all1.split("\\|")[1];
				
				//1、解析url 获取性别 品类
				//2、访问url，分页获取所有产品，解析
				try {
					HttpResponse response = HttpUtils.get("www.farfetch.com"+visitUrl1);
					System.out.println("www.farfetch.com"+visitUrl1);
					if (response.getStatus()==200) {
						String htmlContentPage = response.getResponse();
						Document docpage = Jsoup.parse(htmlContentPage);
						//获取页码
						Elements pages = docpage.select("li[class=pagination-item float-left]");
						Integer maxPage = 1;
						for(int i = pages.size()-1;i>=1;i--){
							Element page = pages.get(i);
							if (page.select("a").size()!=0) {
								if (StringUtils.isNotBlank(page.select("a").get(0).ownText())) {
									maxPage = Integer.valueOf(page.select("a").get(0).ownText());
									break;
								}
							}
						}
						for (int i = 1; i <=maxPage; i++) {
							
							//具体页码
							HttpResponse response1 = HttpUtils.get("www.farfetch.com"+visitUrl1+"?page="+i);
							System.out.println("www.farfetch.com"+visitUrl1+"?page="+i);
							if(response1.getStatus()==200){
								String htmlContentSku = response1.getResponse();
								Document skudoc = Jsoup.parse(htmlContentSku);
								Elements as = skudoc.select("div[class=listing-item-image]").select("a");
								for (Element a : as) {
									num++;
									System.out.println(num+"*"+a.attr("href"));
									log.info(num+"*"+a.attr("href"));
					
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
//											for(Element ele:doc.select("#detailSizeDropdown").select("#SizesInformation_SizeDesc")){
//												size += ele.attr("data-sizeid")+";";
//											}
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
												spu.setSupplierId("201601291741");
												
												
												sku.setSupplierId("201601291741");
												sku.setSpuId(skuId);
												sku.setSkuId(skuId);
												sku.setSaleCurrency("www.farfetch.com"+a.attr("href"));
//											sku.setProductSize(size);
												sku.setProductName(skuName);
												sku.setProductDescription(description);
												sku.setStock("1");
												System.out.println("save sku");
												productFetchService.saveSKU(sku);
												System.out.println("save spu");
												productFetchService.saveSPU(spu);
												System.out.println("save spic");
												productFetchService.savePicture("201601281522", null, skuId, Arrays.asList(img.split(";")));
												//
												img = "";
//											 size = "";
												skuId = "";
												brandName = "";
												description = "";
												matrial = "";
												skuName = "";
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
										
//									}
									
									
									
								}
							}
						}
					}
				} catch (Exception e) {
					log.info("++++++++++++++++++++++++++++++++"+num);
					e.printStackTrace();
				}
			}
	//=============================
	}
	public void test(String str){
		String visitUrl = "";
		String category = "";
		String all = "";
		String htmlContent = "";
		Document doc = null;
		int num = 0;
		
		String img = "";
//		String size = "";
		String skuId = "";
		String brandName = "";
		String description = "";
		String matrial = "";
		String skuName = "";
		String gender = "";
		try {
			visitUrl = str.split("\\|")[0];
			category = str.split("\\|")[1];
			HttpResponse response = HttpUtils.get("www.farfetch.com"+visitUrl);
			System.out.println("www.farfetch.com"+visitUrl);
			if (response.getStatus()==200) {
				htmlContent = response.getResponse();
				doc = Jsoup.parse(htmlContent);
				//获取页码
				Elements pages = doc.select("li[class=pagination-item float-left]");
				Integer maxPage = 1;
				for(int i = pages.size()-1;i>=1;i--){
					Element page = pages.get(i);
					if (page.select("a").size()!=0) {
						if (StringUtils.isNotBlank(page.select("a").get(0).ownText())) {
							maxPage = Integer.valueOf(page.select("a").get(0).ownText());
							break;
						}
					}
				}
				for (int i = 1; i <=maxPage; i++) {
					
					//具体页码
					response = HttpUtils.get("www.farfetch.com"+visitUrl+"?page="+i);
					System.out.println("www.farfetch.com"+visitUrl+"?page="+i);
					if(response.getStatus()==200){
						htmlContent = response.getResponse();
						doc = Jsoup.parse(htmlContent);
						Elements as = doc.select("div[class=listing-item-image]").select("a");
						for (Element a : as) {
							num++;
							System.out.println(num+"*"+a.attr("href"));
							log.info(num+"*"+a.attr("href"));
//							LinkQueue.addAllSkuUrl(a.attr("href")+"|"+category);
							
							//==============================================
							
//						response = HttpUtils.get("www.farfetch.com"+a.attr("href"));
//						htmlContent = response.getResponse();
//						doc = Jsoup.parse(htmlContent);
//						System.out.println(doc.select("#divBreadCrumbInformation").select("a").get(1).ownText());
							
							
							
//							while(!LinkQueue.allSkuUrllsEmpty()){
								all = a.attr("href")+"|"+category;
								visitUrl = all.split("\\|")[0];
								category = all.split("\\|")[1];
//							HttpResponse response = null;
								try {
									response = HttpUtils.get("www.farfetch.com"+visitUrl);
									if(response.getStatus()==200){
										htmlContent = response.getResponse();
										doc = Jsoup.parse(htmlContent);
										for(Element ele:doc.select("a[class=relative js-video-thumb]")){
											img += ele.select("img").attr("src")+";";
										}
//									for(Element ele:doc.select("#detailSizeDropdown").select("#SizesInformation_SizeDesc")){
//										size += ele.attr("data-sizeid")+";";
//									}
										brandName = doc.select("a[data-tstid=Label_ItemBrand]").get(0).ownText();
										for(Element ele:doc.select("div[data-tstid=Content_Composition&Care]").select("dd")){
											if (ele.ownText().contains(">")) {
												matrial +=ele.ownText().split(">")[1]+";";
											}
										}
										skuName = doc.select("h1[class=detail-brand detail-name]").get(0).select("span").get(0).ownText();
										description = doc.select("p[itemprop=description]").get(0).ownText();
										skuId = doc.select("span[itemprop=sku]").get(0).ownText();
										gender = doc.select("#divBreadCrumbInformation").select("a").get(1).ownText();
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
										spu.setSupplierId("201601281522");
										
										
										sku.setSupplierId("201601281522");
										sku.setSpuId(skuId);
										sku.setSkuId(skuId);
										sku.setSaleCurrency("www.farfetch.com"+visitUrl);
//									sku.setProductSize(size);
										sku.setProductName(skuName);
										sku.setProductDescription(description);
										sku.setStock("1");
										System.out.println("save sku");
										productFetchService.saveSKU(sku);
										System.out.println("save spu");
										productFetchService.saveSPU(spu);
										System.out.println("save spic");
										productFetchService.savePicture("201601281522", null, skuId, Arrays.asList(img.split(";")));
										//
										img = "";
//									 size = "";
										skuId = "";
										brandName = "";
										description = "";
										matrial = "";
										skuName = "";
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								
//							}
							
							
							
						}
					}
				}
			}
		} catch (Exception e) {
			log.info("++++++++++++++++++++++++++++++++"+num);
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		
		
//		HttpResponse response = null;
//		try {
//			response = HttpUtils.get("http://www.farfetch.com/cn/shopping/men/smets/items.aspx?q=%2520smets&category=135976");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if (response.getStatus()==200) {
//			String htmlContent = response.getResponse();
//			Document doc = Jsoup.parse(htmlContent);
//			Elements pages = doc.select("li[class=pagination-item float-left]");
//			Integer maxPage = 1;
//			for(int i = pages.size()-1;i>=1;i--){
//				Element page = pages.get(i);
//				if (page.select("a").size()!=0) {
//					if (StringUtils.isNotBlank(page.select("a").get(0).ownText())) {
//						maxPage = Integer.valueOf(page.select("a").get(0).ownText());
//						break;
//					}
//				}
//			}
//			System.out.println(maxPage);
//		}
		String[] feeds = new String[]{"www.farfetch.com/cn/shopping/women/smets/items.aspx?q=smets","http://www.farfetch.com/cn/shopping/men/smets/items.aspx?q=smets"};
		new MyCrawler().crawling(feeds);
		
	}
	
}

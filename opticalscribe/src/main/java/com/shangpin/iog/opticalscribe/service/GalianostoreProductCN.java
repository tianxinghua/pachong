package com.shangpin.iog.opticalscribe.service;

/**
 * Created by zhaogenchun on 2015/11/26.
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.iog.opticalscribe.dto.GucciProductDTO;
import com.shangpin.iog.pavinGroup.util.HttpResponse;
import com.shangpin.iog.pavinGroup.util.HttpUtils;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;

/**
 * Created by wanner on 2018/06/28 conf.properties 配置信息如下： d
 */
@Component("GalianostoreProductCN")
public class GalianostoreProductCN {


	@Autowired
	ProductFetchService productFetchService;

	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String uri; //
	private static String path;
	// 所有品类相对路径以及名称 如：
	// women&&新作：RE(BELLE)&&/zh/ca/women/handbags/new,women&&手提包&波士顿包&&/zh/ca/women/handbags/top-handles
	private static List<String> genderAndNameAndCategoryUris = new ArrayList<>();

	private static OutputStreamWriter out = null;
	static String splitSign = ",";
	static {
		 if (null == bdl)
		 bdl = ResourceBundle.getBundle("conf");
		 path = bdl.getString("path");
	}

	ExecutorService exe = new ThreadPoolExecutor(10, 10, 500,
			TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(100),
			new ThreadPoolExecutor.CallerRunsPolicy());

	public void getUrlList() throws Exception {

		System.out.println("要下载的url：" + uri);
		System.out.println("文件保存目录：" + path);
		try {
			out = new OutputStreamWriter(new FileOutputStream(path, true),
					"gb2312");
		} catch (IOException e) {
			e.printStackTrace();
		}

		StringBuffer buffer = new StringBuffer("gender" + splitSign + "brand"
				+ splitSign +

				"category" + splitSign + "SPU" + splitSign +

				"productModel" + splitSign + "season" + splitSign +

				"material" + splitSign + "color" + splitSign +

				"size" + splitSign + "proName" + splitSign +

				"市场价" + splitSign + "售价" + splitSign +

				"qty" + splitSign + "made" + splitSign +

				"desc" + splitSign + "pics" + splitSign +

				"detailLink" + splitSign

		).append("\r\n");
		out.write(buffer.toString());
		try {
			solveCategoryProjectEelements("https://www.galianostore.com/en/man/categories/clothing");
			exe.shutdown();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 处理 品类 下的 商品列表信息
	 *
	 * @param sex
	 *            性别
	 * @param categoryName
	 *            品类名称
	 * @param categoryUrl
	 *            品类url
	 */
	public List<GucciProductDTO> solveCategoryProjectEelements(String url) {
		List<GucciProductDTO> returnList = null;
		// 请求分页的参数时 ni
		try {
			HttpResponse response = HttpUtils.get(url);
			if (response.getStatus() == 200) {
				String htmlContent = response.getResponse();

				Document doc = Jsoup.parse(htmlContent);
				// 获取当前页的 商品数据
				Elements content = doc.select("#content-block");
				Elements pages = content.select("div.accordeon");
				Elements categoryEles = pages.select("li");
				if (categoryEles != null && !categoryEles.isEmpty()) {
					returnList = new ArrayList<>();
					for (Element ele : categoryEles) {
						String categoryName = ele.select("a").text();

						String categoryUrl = ele.select("a").attr("href");

						response = HttpUtils.get(categoryUrl);
						if (response.getStatus() == 200) {
							htmlContent = response.getResponse();

							doc = Jsoup.parse(htmlContent);

							content = doc.select("#content-block");
							Elements contentEles = content.select(
									"div.information-blocks").select("div.row");
							Elements productEles = contentEles
									.select("div.row").select("div.shop-grid")
									.select("div.grid-view")
									.select("div.col-md-3");

							Elements pagesEle = contentEles.select(
									"div.page-selector")
									.select("div.pages-box");
							
							for (Element product : productEles) {
								
								Elements productImag = product
										.select("div.product-image");
								String productDetailUrl = productImag.select(
										"a").attr("href");
								List<GucciProductDTO> productList = fetchProducts(productDetailUrl);
								
								String season = null;
								Elements eleSeason = product.select("div.season");
								if(eleSeason!=null&&!eleSeason.isEmpty()){
									season = eleSeason.text();	
								}
								for(GucciProductDTO productDto : productList){
									exportExcel(productDto,categoryName,season);
								}
								
							}
//							if (pagesEle != null && !pagesEle.isEmpty()) {
//								String nextUrl = pagesEle.select("li").last()
//										.select("a").attr("href");
//								if(nextUrl!=null){
//									solveCategoryProjectEelements(nextUrl);
//								}
//							}

						}
					}
				}
			}
		} catch (Exception e) {

		}
		return returnList;
	}

	/**
	 * 根据商品列表 elements 获取保存商品数据
	 * 
	 * @param sex
	 *            性别
	 * @param categoryName
	 *            列表名称
	 * @param elements
	 *            商品列表
	 */
	public List<GucciProductDTO> fetchProducts(String url) {
		
		List<GucciProductDTO> returnList = new ArrayList<>();
		
		try {
			HttpResponse response = HttpUtils.get(url);
			if (response.getStatus() == 200) {
				GucciProductDTO product = new GucciProductDTO();
				String htmlContent = response.getResponse();
				Document doc = Jsoup.parse(htmlContent);

				Elements content = doc.select("#content-block");
				Elements pages = content.select("div.information-blocks")
						.select("div.row");
				Elements categoryEles = pages.select("div.col-sm-6")
						.select("div.information-entry")
						.select("div.product-detail-box");

				String productName = categoryEles.select("h1").text();
				
				String brandName = categoryEles.select("h3").text();
				
				Elements pricesEle = categoryEles.select("div.price").select(
						"div.detail-info-entry");
				String price = pricesEle.select("div.prev").text();
				String salePrice = pricesEle.select("div.c").text();

				Elements productDescEles = categoryEles.select(
						"div.product-description").select(
						"div.detail-info-entry");
				String madeIn = null;
				String material = null;
				String color = null;
				String productDesc = productDescEles.html();
				String[] productDescArr = productDesc.split("<br>");
				for (String desc : productDescArr) {
					if(desc.contains("Made in")){
						madeIn = desc.replace("Made in","").replace("-","").trim();
					}
					if(desc.contains("Material")){
						material = desc.replace("Material:","").replace("-","").trim();
					}
					if(desc.contains("Color")){
						color = desc.replace("Color:","").replace("-","").trim();
					}
				}
				productDesc = productDescEles.text().replace(",",".");
				Elements productCodeEle = categoryEles.select(
						"div.tags-selector").select("div.detail-info-entry");
				String productCode = productCodeEle.text();


				Elements productPicEle = pages.select("div.col-sm-6")
						.select("div.information-entry")
						.select("div.product-preview-box")
						.select("div.paddings-container");

				StringBuffer picUrls = new StringBuffer();
				for (Element pic : productPicEle) {
					String picUrl = pic.select("img").attr("src");
					picUrls.append(picUrl).append("|");
				}
				product.setUrl(url);
				product.setPicUrls(picUrls.toString());
				product.setProductname(productName);
				if(price!=null){
					product.setPrice(price.replace("€","").trim());
				}
				if(brandName!=null){
					if(brandName.contains("In stock")){
						product.setQty("1");
					}
					brandName = brandName.substring(0,brandName.indexOf("Availability"));
				}
				product.setBrand(brandName);
				if(productCode!=null){
					String spuNo = productCode.replace("Product code", "").replace(":","").trim();
					product.setProductCode(spuNo);
					product.setSpuNo(spuNo);
				}
				product.setMaterial(material);
				product.setDescript(productDesc);
				product.setMade(madeIn);
				product.setColorCode(color);
				product.setItemprice(price);
				product.setItemsaleprice(salePrice);
				product.setPicUrls(picUrls.toString());
				
				Elements sizeEles = categoryEles.select("div.size-selector")
						.select("div.detail-info-entry");
				sizeEles = sizeEles.select("#size_box").select("a.size_item");
				if(sizeEles!=null&&!sizeEles.isEmpty()){
					for (Element sizeEle : sizeEles) {
						product.setSize(sizeEle.text());
						returnList.add(product);
					}
				}else{
					returnList.add(product);
				}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnList;
	}

	
	/**
	 * 导出单个商品信息到csv 文件（追加）
	 * 
	 * @param dto
	 *            商品信息DTO
	 */
	private static void exportExcel(GucciProductDTO dto,String categoryName,String season) {
		// 继续追加
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append(dto.getSex()).append(splitSign);
			buffer.append(dto.getBrand()).append(splitSign);

			buffer.append(categoryName).append(splitSign);
			buffer.append(dto.getSpuNo()).append(splitSign);

			buffer.append(dto.getBarCode()).append(splitSign);
			buffer.append(season).append(splitSign);

			buffer.append(dto.getMaterial()).append(splitSign);
			buffer.append(dto.getColorCode()).append(splitSign);

			buffer.append(dto.getSize()).append(splitSign);
			buffer.append(dto.getProductname()).append(splitSign);

			buffer.append(dto.getItemprice()).append(splitSign);
			buffer.append(dto.getItemsaleprice()).append(splitSign);

			buffer.append(dto.getQty()).append(splitSign);
			buffer.append(dto.getMade()).append(splitSign);

			buffer.append(dto.getDescript()).append(splitSign);
			buffer.append(dto.getPicUrls()).append(splitSign);

			buffer.append(dto.getUrl());

			buffer.append("\r\n");
			out.write(buffer.toString());
			System.out.println(buffer.toString());
			logger.info(buffer.toString());
			out.flush();
		} catch (Exception e) {
		}
	}

}
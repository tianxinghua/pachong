package com.tessbit.service;

/**
 * Created by zhaogenchun on 2015/11/26.
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import com.tessbit.util.HttpResponse;
import com.tessbit.util.HttpUtils;

/**
 * Created by 赵根春 on 2015/9/25.
 */
@Component("opticalscribeThread1")
public class WangFetchProduct1 {
	@Autowired
	ProductFetchService productFetchService;

	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String uri;
	private static String sex;
	private static String path;
	private static int pageStart;
	private static int pageEnd;
	private static OutputStreamWriter  out= null;
	static String splitSign = ",";
	private static String fileName;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		uri = bdl.getString("uri");
		sex = bdl.getString("gender");
		path = bdl.getString("path");
		if(!bdl.getString("pageStart").isEmpty()){
			pageStart = Integer.parseInt(bdl.getString("pageStart"));
		}
		if(!bdl.getString("pageEnd").isEmpty()){
			pageEnd = Integer.parseInt(bdl.getString("pageEnd"));
		}
	}
	
	class SaveThread extends Thread{
		private int pageNo;
		public SaveThread(int pageNo) {
			this.pageNo = pageNo;
		}
		@Override
		public void run() {
			try {
				fetch(pageNo);
			} catch (Exception e) {
				logger.warn(Thread.currentThread().getName() + "处理出错", e);
			}
		}
	}
	class UpdateThread extends Thread{
		private Element html;
		public UpdateThread(Element html) {
			this.html = html;
		}
		@Override
		public void run() {
			try {
				save(html);
			} catch (Exception e) {
				logger.warn(Thread.currentThread().getName() + "处理出错", e);
			}
		}
	}
	class fetchThread extends Thread{
		private int i;
		public fetchThread(int i) {
			this.i = i;
		}
		@Override
		public void run() {
			try {
				fetch(i);
			} catch (Exception e) {
				logger.warn(Thread.currentThread().getName() + "处理出错", e);
			}
		}
	}
//	ExecutorService exe=Executors.newFixedThreadPool(500);//相当于跑4遍
	ExecutorService exe = new ThreadPoolExecutor(10,10, 500, TimeUnit.MILLISECONDS,
										new ArrayBlockingQueue<Runnable>(100),new ThreadPoolExecutor.CallerRunsPolicy());
	private void save(Element category){
		Elements picUrl1 = category.select("div");
		Elements picUrl2 = picUrl1.select("a");
		if(StringUtils.isBlank(picUrl2.attr("href"))){
			return;
		}
		String proUrl ="http://www.farfetch.com"+ picUrl2.attr("href");
		Map<String,String> map = getProductUrl(proUrl);
		
		String brand = category.select("h5[itemprop=brand]").text();
		String productName = category.select("p[itemprop=name]").text();
		String price = category.select("span[itemprop=price]").text();
		Pattern pattern = Pattern.compile("[^0-9]");
		Matcher matcher = pattern.matcher(price);
		price = matcher.replaceAll("");
		Product pro = null;
		pro = new Product();
		pro.setBrand(brand);
		pro.setMaterl(map.get("materl"));
		pro.setProductCode(map.get("productCode"));
		pro.setDescript(map.get("desc"));
		pro.setProductname(productName);
		pro.setUrl(map.get("picUrl"));
		pro.setBarCode(map.get("barCode"));
		pro.setMade(map.get("made"));
		pro.setSize(map.get("size"));
		pro.setColorCode(map.get("colorCode"));
		pro.setPrice(price);
		messMappingAndSave(pro);
	}
	
	private void fetch(int i) throws Exception{
		HttpResponse response = HttpUtils.get(uri+"&page="+i);
		
		if (response.getStatus()==200) {
			String htmlContent = response.getResponse();
			Document doc = Jsoup.parse(htmlContent);
			Elements categorys = doc.select("div.listing-flexbox");
			Elements category1 = categorys.select("div .baseline").select(".col9").select(".col-md-8").select("section").select("article");
			int j=0;
			for (Element category : category1) {
				j++;
				System.out.println("第"+i+"页："+",第"+j+"条数据");
				exe.execute(new UpdateThread(category));
			}
			
		}
		
	}
	public  void getUrlList() throws Exception {
		System.out.println("要下载的url："+uri);
		System.out.println("文件保存目录："+path);
		try {
			out = new OutputStreamWriter(new FileOutputStream(path, true),"gb2312");
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuffer buffer = new StringBuffer("图片" + splitSign).append("\r\n");
		out.write(buffer.toString());
		String page = null;
		int pageCount = 0;
		try {
			HttpResponse response = HttpUtils.get(uri);
			if (response.getStatus()==200) {
				String htmlContent = response.getResponse();
				Document doc = Jsoup.parse(htmlContent);
				Elements pageEle  = doc.select(".number");
				page = pageEle.text();
				System.out.println("总页数："+page);
			}
			if(page!=null){
				pageCount = Integer.parseInt(page);
			}else{
				pageCount = pageEnd;
			}
			if(pageStart==0){
				pageStart = 1;
			}
			for(int i=pageStart;i<=pageCount;i++){
//				exe1.execute(new fetchThread(i));
				fetch(i);
			}
			
			exe.shutdown();
			while (!exe.awaitTermination(60, TimeUnit.SECONDS)) {

			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
//	public static void main(String[] args) {
//		getProductUrl("https://www.farfetch.com/cn/shopping/women/gucci-icon-signature-chain-wallet-item-11924450.aspx?storeid=9270&from=listing&tglmdl=1&rnkdmnly=1&ffref=lp_pic_5_1_");
//	}
	private static Map<String,String> getProductUrl(String url) {
		Map<String,String> map = new HashMap<String,String>();
		String  materal = null;
		String  desc= null;
		String  barCode= null;
		String productCode = null;
		String colorCode = null;
		String made = null;
		String size = null;
		StringBuffer pic = new StringBuffer();
		try {
			HttpResponse response = HttpUtils.get(url);
			if (response.getStatus()==200) {
				String htmlContent = response.getResponse();
				Document doc = Jsoup.parse(htmlContent);
				Elements categorys = doc.select("div .accordion").select(".accordion-xl").select(".product-detail");
				Elements categoryPics = doc.select(".sliderProduct").select(".js-sliderProductFull").select("li");
				Elements categorySize1 = doc.select("div .accordion-item"); 
				for (Element ele : categorySize1) {
					
					Elements trkEle = ele.select("div").eq(1);
					
					String trk = trkEle.attr("trk");
					if("233".equals(trk)){
						Elements categorySize =  ele.select("div").eq(2);;
						if(categorySize!=null&&!categorySize.isEmpty()){
							Element category4 = categorySize.select("dd").get(0);
							if(category4!=null){
								size = category4.text();
								size = size.replace(",",".");
							}
						}
						break;
					} 
				}
				
			
				int i=0;
				if(categoryPics!=null&&categoryPics.size()>0){
					for(Element categoryPic:categoryPics){
						i++;
						String pic1 = categoryPic.select("img").attr("data-fullsrc");
						pic.append(pic1).append("|");
						if(i==5){
							break;
						}
					}
				}
				
				if(categorys!=null&&categorys.size()>0){
						Elements category1  = categorys.select("p[itemprop=description]");
						
						
						
						
						if(category1!=null){
							desc = category1.text();
							desc = desc.replace(",",".");
								
						}
						Elements category2  = categorys.select("span[itemprop=sku]");
						if(category2!=null){
							barCode = category2.text();	
						}
						
						Elements category3 = categorys.select("p[data-tstid=designerStyleId]").select("span");
						if(category3!=null){
							productCode = category3.text();
						}
						//<span itemprop="color">32140</span>
						Elements category6 = categorys.select("span[itemprop=color]");
						if(category6!=null){
							colorCode = category6.text();	
						}
						
						Elements category4 = categorys.select("span[data-tstid=MadeInLabel]");
						if(category4!=null){
							made = category4.text();
						}
						Elements category5 = categorys.select("div .product-detail-dl");
						if(category5!=null){
							String ss = category5.text();
							int index = ss.indexOf("洗涤说明");
							//成分
							if(index<0){
								materal = ss.substring(0);
							}else{
								materal = ss.substring(0,ss.indexOf("洗涤说明"));
							}
						}
					
						map.put("productCode", productCode);
						if(StringUtils.isBlank(barCode)){
							System.out.println(barCode);
						}
						map.put("barCode", barCode);
						map.put("desc", desc);
						map.put("materl", materal);
						map.put("picUrl", pic.toString());
						map.put("made", made);
						map.put("size", size);
						map.put("colorCode",colorCode);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * fetch product and save into db
	 */
	/**
	 * message mapping and save into DB
	 */

	public void messMappingAndSave(Product item) {

		exportExvel(item);
//			SpuDTO spu = new SpuDTO();
//			try {
//				i=i+1;
//				spu.setId(UUIDGenerator.getUUID());
//				spu.setSupplierId(supplierId);
//				spu.setSpuId(item.getBarCode());
//				spu.setCategoryGender(sex);
//				spu.setBrandName(item.getBrand());
//				spu.setMaterial(item.getMaterl());
//				spu.setProductOrigin(item.getMade());
//				spu.setSpuName(item.getProductname());
//				spu.setPicUrl(item.getUrl());
//				productFetchService.saveSPU(spu);
//			} catch (Exception e) {
//				try {
//					productFetchService.updateMaterial(spu);
//				} catch (ServiceException e1) {
//					e1.printStackTrace();
//				}
//			}
//			SkuDTO sku = new SkuDTO();
//			try {
//				sku.setId(UUIDGenerator.getUUID());
//				sku.setSupplierId(supplierId);
//				sku.setSpuId(item.getBarCode());
//				sku.setSkuId(item.getBarCode());
//				sku.setMarketPrice(item.getPrice());
//				sku.setProductCode(item.getProductCode());
//				sku.setProductName(item.getProductname());
//				sku.setColor(item.getDescript());
//				sku.setBarcode(item.getBarCode());
//				sku.setMemo(item.getMemo());
//				productFetchService.saveSKU(sku);
//
//			} catch (ServiceException e) {
//				if (e.getMessage().equals("数据插入失败键重复")) {
//					try {
//						productFetchService.updatePriceAndStock(sku);
//					} catch (ServiceException e1) {
//						e1.printStackTrace();
//					}
//				} else {
//					e.printStackTrace();
//				}
//			}
	}


	private static void exportExvel(Product dto){
		//此处设置为true即可追加
		//继续追加

//		   InputStream in = new BufferedInputStream(new ByteArrayInputStream(buffer.toString().getBytes("gb2312")));
//		   File file = new File(path);
//		   FileOutputStream out = new FileOutputStream(file);
//            byte[] data = new byte[1024];
//            int len = 0;
//            while (-1 != (len=in.read(data, 0, data.length))) {
//                out.write(data, 0, len);
//            }
//            out.flush();
//            out.close();
//
//

		StringBuffer buffer  = new StringBuffer();
		try {
			//supplierId 供货商

			String brandName = dto.getBrand();
			buffer.append(brandName).append(splitSign);
			// 货号
			buffer.append(
					null == dto.getProductCode() ? "" : dto
							.getProductCode().replaceAll(",", " ")).append(
					splitSign);
			// 供应商SKUID
			// 产品名称
			String productName = dto.getProductname();

			buffer.append(productName).append(splitSign);

			buffer.append(dto.getBarCode()).append(
					splitSign);
			buffer.append(dto.getColorCode()).append(
					splitSign);

			// 获取材质
			String material = dto.getMaterl();
			if (StringUtils.isBlank(material)) {
				material = "";
			} else {

				material = material.replaceAll(splitSign, " ")
						.replaceAll("\\r", "").replaceAll("\\n", "");
			}

			buffer.append(material).append(splitSign);
			buffer.append(dto.getSize()).append(
					splitSign);
			// 获取产地
			String productOrigin = dto.getMade();

			buffer.append(productOrigin).append(splitSign);
			// 欧洲习惯 第一个先看 男女
			buffer.append(
					sex).append(splitSign);
			// 新的价格
			String newMarketPrice = dto.getPrice();
			buffer.append(newMarketPrice).append(splitSign);
			buffer.append(dto.getDescript()).append(
					splitSign);

			buffer.append(dto.getUrl());
			buffer.append("\r\n");
			out.write(buffer.toString());
			out.flush();
		} catch (Exception e) {
		}
	}
}
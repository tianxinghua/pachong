package com.shangpin.iog.opticalscribe.service;

/**
 * Created by zhaogenchun on 2015/11/26.
 */

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.shangpin.iog.opticalscribe.dto.GucciProductDTO;
import com.shangpin.iog.opticalscribe.dto.RequestFailProductDTO;
import com.shangpin.iog.opticalscribe.tools.CSVUtilsSolveRepeatData;
import com.shangpin.iog.pavinGroup.util.HttpResponse;
import com.shangpin.iog.pavinGroup.util.HttpUtils;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.commons.httpclient.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanner 2018/06/28

 conf.properties 配置信息如下：

 # gucci 意大利
 uri-it=https://www.gucci.com
 #拉取数据存放路径
 path-it=D://GUCCI/gucci-it.csv
 #去重数据存放路径
 destFilePath-it=D://GUCCI/gucci-it-solved.csv
 # false:拉取数据  true:处理拉取完的数据（去重）
 flag-it=false

 #women
 it-genderAndNameAndCategoryUri1=women&&Nuovi Arrivi: RE(BELLE)&&/it/it/ca/women/handbags/new-rebelle-c-women-handbags-new
 it-genderAndNameAndCategoryUri2=women&&Borse a Mano e Bauletti&&/it/it/ca/women/handbags/top-handles-boston-bags-c-women-handbags-top-handles-and-boston-bags
 it-genderAndNameAndCategoryUri3=women&&Borse Shopping&&/it/it/ca/women/handbags/womens-totes-c-women-handbags-totes
 it-genderAndNameAndCategoryUri4=women&&Borse a Spalla&&/it/it/ca/women/handbags/womens-shoulder-bags-c-women-handbags-shoulder-bags
 it-genderAndNameAndCategoryUri5=women&&Borse a Tracolla&&/it/it/ca/women/handbags/womens-crossbody-bags-c-women-handbags-crossbody-bags
 it-genderAndNameAndCategoryUri6=women&&Marsupi&&/it/it/ca/women/handbags/belt-bags-c-women-handbags-belt
 it-genderAndNameAndCategoryUri7=women&&Zaini&&/it/it/ca/women/handbags/womens-backpacks-c-women-handbags-backpacks
 it-genderAndNameAndCategoryUri8=women&&Borse Mini&&/it/it/ca/women/handbags/womens-mini-bags-c-women-handbags-mini-bags
 it-genderAndNameAndCategoryUri9=women&&Valigeria e Borse Lifestyle&&/it/it/ca/women/womens-lifestyle-bags-luggage-c-women-accessories-lifestyle-bags-and-luggage

 it-genderAndNameAndCategoryUri10=women&&Felpe e T-Shirt&&/it/it/ca/women/womens-ready-to-wear/womens-sweatshirts-t-shirts-c-women-ready-to-wear-new-sweatshirts

 it-genderAndNameAndCategoryUri11=women&&Mocassini&&/it/it/ca/women/womens-shoes/womens-moccasins-loafers-c-women-shoes-moccasins-and-loafers
 it-genderAndNameAndCategoryUri12=women&&Ballerine&&/it/it/ca/women/womens-shoes/womens-ballerinas-c-women-shoes-ballerinas
 it-genderAndNameAndCategoryUri13=women&&Slippers e Mules&&/it/it/ca/women/womens-shoes/womens-slippers-mules-c-women-shoes-slides-mules

 it-genderAndNameAndCategoryUri14=women&&Portafogli e Piccola Pelletteria&&/it/it/ca/women/womens-accessories/womens-wallets-small-accessories-c-women-accessories-wallets
 it-genderAndNameAndCategoryUri15=women&&Cinture&&/it/it/ca/women/womens-accessories/womens-belts-c-women-accessories-belts

 #men
 it-genderAndNameAndCategoryUri21=men&&Nuovi Arrivi: Gucci Print&&/it/it/ca/men/mens-bags/new-gucci-print-c-men-bags-new
 it-genderAndNameAndCategoryUri22=men&&Borse a Tracolla&&/it/it/ca/men/mens-bags/mens-messengers-bags-c-men-bags-messenger-bags
 it-genderAndNameAndCategoryUri23=men&&Zaini&&/it/it/ca/men/mens-bags/mens-backpacks-c-men-bags-backpacks
 it-genderAndNameAndCategoryUri24=men&&Borse Shopping&&/it/it/ca/men/mens-bags/mens-totes-c-men-bags-totes
 it-genderAndNameAndCategoryUri25=men&&Marsupi&&/it/it/ca/men/mens-bags/mens-belt-bags-c-men-bags-belt
 it-genderAndNameAndCategoryUri26=men&&Portadocumenti&&/it/it/ca/men/mens-bags/mens-portfolios-c-men-bags-portfolios
 it-genderAndNameAndCategoryUri27=men&&Borse Business&&/it/it/ca/men/mens-bags/mens-briefcases-c-men-bags-briefcases
 it-genderAndNameAndCategoryUri28=men&&Valigie e Borse Viaggio&&/it/it/ca/men/mens-bags/mens-suitcases-duffle-bags-c-men-bags-suitcases-duffle-bags
 it-genderAndNameAndCategoryUri29=men&&Valigeria&&/it/it/ca/men/mens-bags/mens-luggage-c-men-bags-trolleys

 it-genderAndNameAndCategoryUri30=men&&Camicie&&/it/it/ca/men/mens-ready-to-wear/mens-shirts-c-men-readytowear-shirts
 it-genderAndNameAndCategoryUri31=men&&Felpe&&/it/it/ca/men/mens-ready-to-wear/mens-sweatshirts-c-men-ready-to-wear-new-sweatshirts
 it-genderAndNameAndCategoryUri32=men&&T-Shirt e Polo&&/it/it/ca/men/mens-ready-to-wear/mens-t-shirts-polos-c-men-readytowear-tshirts-and-polos

 it-genderAndNameAndCategoryUri33=men&&Mocassini&&/it/it/ca/men/mens-shoes/mens-moccasins-loafers-c-men-shoes-moccasins-and-loafers
 it-genderAndNameAndCategoryUri34=men&&Stringate&&/it/it/ca/men/mens-shoes/mens-lace-ups-c-men-shoes-lace-ups
 it-genderAndNameAndCategoryUri35=men&&Sandali & Sliders&&/it/it/ca/men/mens-shoes/mens-sandals-slides-c-men-shoes-sandals

 it-genderAndNameAndCategoryUri36=men&&Portafogli e Piccola Pelletteria&&/it/it/ca/men/mens-accessories/mens-wallets-small-accessories-c-men-accessories-wallets
 it-genderAndNameAndCategoryUri37=men&&Cinture&&/it/it/ca/men/mens-accessories/mens-belts-c-men-accessories-belts

 */
//@Component("gucciProductIT")
public class GucciProductIT {

	//有库存
	private static final String IN_STOCK = "1";
	//无库存
	private static final String NO_STOCK = "0";

	private static String supplierId="2018070502014";
	private static String supplierNo="S0000963";

	// 商品barCodeMap  key:barCode value:null
	private static HashMap<String, String> barCodeMap= null;

	//商品失败请求信息
    private static List<RequestFailProductDTO> failList = null;


	@Autowired
	ProductFetchService productFetchService;

	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String uri;
	private static String path;
	private static String destFilePath;
	private static String flag;
    // 所有品类相对路径以及名称 如： women&&新作：RE(BELLE)&&/zh/ca/women/handbags/new,women&&手提包&波士顿包&&/zh/ca/women/handbags/top-handles
    private static List<String> genderAndNameAndCategoryUris = new ArrayList<>();
	private static OutputStreamWriter  out= null;
	static String splitSign = ",";


	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		    uri = bdl.getString("uri-it");
            path = bdl.getString("path-it");
			destFilePath = bdl.getString("destFilePath-it");
			flag = bdl.getString("flag-it");

        Enumeration<String> keys = bdl.getKeys();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            if(key.startsWith("it-genderAndNameAndCategoryUri")){
                String keyValue = null;
                try {
                    String value = bdl.getString(key);
                    keyValue = new String(value.getBytes("ISO-8859-1"), "GBK");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                genderAndNameAndCategoryUris.add(keyValue);
            }
        }

	}

	ExecutorService exe = new ThreadPoolExecutor(10,10, 500, TimeUnit.MILLISECONDS,
			new ArrayBlockingQueue<Runnable>(100),new ThreadPoolExecutor.CallerRunsPolicy());

	public  void getUrlList() throws Exception {
		//根据flag 判断是处理数据还是 拉取数据
		if("true".equals(flag)){  //处理拉取的重复商品数据
			System.out.println("=============it-处理重复数据开始===============");
			logger.info("=============it-处理重复数据开始===============");
			CSVUtilsSolveRepeatData.solveFinalProductData(path,destFilePath);
			return;
		}
		System.out.println("要下载的url："+uri);
		System.out.println("文件保存目录："+path);
		try {
			out = new OutputStreamWriter(new FileOutputStream(path, true),"gb2312");
		} catch (IOException e) {
			e.printStackTrace();
		}

        StringBuffer buffer = new StringBuffer(
				"gender" + splitSign +
				"brand" + splitSign +

				"category" + splitSign +
				"SPU" + splitSign +

				"productModel" + splitSign +
				"season" + splitSign +

				"material"+ splitSign +
				"color"+ splitSign +

				"size" + splitSign +
				"proName" + splitSign +

				"国外市场价" + splitSign +
				"国内市场价" + splitSign +

				"qty" + splitSign +
				"qtyDesc" + splitSign +


				"made" + splitSign +

				"desc" + splitSign +
				"pics" + splitSign +

				"detailLink" + splitSign +
                "measurement" + splitSign+
                "supplierId" + splitSign+
                "supplierNo" + splitSign+
                "supplierSkuNo" + splitSign
        ).append("\r\n");
		out.write(buffer.toString());

		try {

			//每次调用都初始化 barCodeMap 以及 failList
			barCodeMap = new HashMap<String,String>();
            failList = new ArrayList<>();

			int productModeNumber = 0;

            //校验配置信息 是否符合格式要求
            int size = genderAndNameAndCategoryUris.size();
            for (int i = 0; i < size; i++) {

                String[] sexAnduriAndName = genderAndNameAndCategoryUris.get(i).split("&&");
                if(sexAnduriAndName.length!=3) {
                    logger.info(" genderAndCategoryUri 不符合格式 ：" + genderAndNameAndCategoryUris.get(i));
                    System.out.println(" genderAndCategoryUri 不符合格式 ：" + genderAndNameAndCategoryUris.get(i));
                    throw new RuntimeException("配置文件 genderAndCategoryUri 不符合格式："+genderAndNameAndCategoryUris.get(i));
                }
            }

            for (int i = 0; i < size; i++) {
                if(i>0){
                    String[] temsexAnduriAndName = genderAndNameAndCategoryUris.get(i-1).split("&&");

                    System.out.println("it拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
                    System.out.println("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
                    System.out.println("=====================================================================");

                    logger.info("it拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
                    logger.info("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
                    logger.info("=====================================================================");
					productModeNumber = barCodeMap.size();
                }

                String[] sexAnduriAndName = genderAndNameAndCategoryUris.get(i).split("&&");
                //拉取 品类信息
                String categoryUrl =  sexAnduriAndName[2];
                categoryUrl = uri + categoryUrl;
				System.out.println("============it开始拉取："+sexAnduriAndName[0]+":"+sexAnduriAndName[1]+":"+categoryUrl+"=========================================================");
				logger.info("============it开始拉取："+sexAnduriAndName[0]+":"+sexAnduriAndName[1]+":"+categoryUrl+"=========================================================");
                solveCategoryProjectEelements(sexAnduriAndName[0],sexAnduriAndName[1],categoryUrl);

            }
            String[] temsexAnduriAndName = genderAndNameAndCategoryUris.get(size-1).split("&&");
            System.out.println();
            System.out.println("it拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
            System.out.println("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
            System.out.println("共拉取 ："+barCodeMap.size()+"款商品数据");
            System.out.println("=====================================================================");


            logger.info("it拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
            logger.info("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
            logger.info("所有品类 ："+barCodeMap.size()+"款商品数据");
            logger.info("=====================================================================");

            logger.error("=====================================================================================");
            logger.error("==========IT重新请求失败的商品及品类信息===================================================");
            int failSize = failList.size();
            for (int i = 0; i <failSize ; i++) {
                RequestFailProductDTO failProduct = failList.get(i);
                String flag = failProduct.getFlag();
                if("1".equals(flag)){
                    solveCategoryProjectEelements(failProduct.getGender(),failProduct.getCategoryName(),failProduct.getUrl());
                }else if("0".equals(flag)){
                    //不校验 productModel 是否存在
                    savePre(failProduct.getGender(),failProduct.getCategoryName(),failProduct.getUrl());
                }
            }
            logger.info("=====================================================================================");
            logger.info("==========IT请求失败的商品及品类信息 结束===================================================");
			exe.shutdown();
			out.close();
			barCodeMap = null;
			failList = null;
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	/**
	 * 处理 品类 下的 商品列表信息
	 *
	 * @param sex 性别
	 * @param categoryName  品类名称
	 * @param categoryUrl  品类url
	 */
	public void solveCategoryProjectEelements(String sex,String categoryName,String categoryUrl) {

		//请求分页的参数时 ni
		if(categoryUrl==null||"".equals(categoryUrl)){
			return;
		}
		try {
			Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
			Header[] headers = new Header[1];
			headers[0] = header;
			HttpResponse response = HttpUtils.get(categoryUrl,headers);

			if (response.getStatus() == 200) {
				String htmlContent = response.getResponse();

				Document doc = Jsoup.parse(htmlContent);

				//获取当前页的 商品数据 div
				/**
				 * <div class="content search-result new-4-cols-layout"
					  data-module="ajaxPagination"
					  data-category-code="women-handbags-shoulder-bags"
					  data-cta-details="[{&#034;pageNumber&#034;:0,&#034;buttonType&#034;:2,&#034;buttonTitle&#034;:&#034;Visualizza tutto&#034;}]"
					  data-start-page="0"
					  data-items-per-page="36"
					  data-total-items="190"
					  data-total-pages="6"
					  data-component-name="PGEU4Cols"
					  data-promo-invisible="true"
					  data-bg-large-is-repeat="true"
					  data-bg-medium-is-repeat="true"
					  data-bg-small-is-repeat="true"
					  data-has-backgrounds="false"
					  data-has-shoppable="false"
					  data-has-large-backgrounds="false"
					  data-has-medium-backgrounds="false"
					  data-has-small-backgrounds="false"
					  data-number-of-promos="0"
					  data-promos-positions="[]"  >
				 */

				Element productDivElement = doc.select("div[data-module=ajaxPagination]").first();
				Elements productArticleElements = productDivElement.select("article");

				//商品详情urls 集合
				List<String> productDetailUrls = new ArrayList<>();
				//获取初始页数据
				for (Element productArticleElement: productArticleElements) {
					Element aElement = productArticleElement.select("a").first();
					if(aElement==null){
						continue;
					}
					String productDetailUri = aElement.attr("href");
					if(productDetailUri!=null&&!"".equals(productDetailUri)){
						String productDetailUrl = uri +productDetailUri;
						productDetailUrls.add(productDetailUrl);
					}
				}

				//分页加载剩余数据
				//查看当前品类 商品页码数
				String dataTotalPagesStr = productDivElement.attr("data-total-pages");
				if(dataTotalPagesStr==null||"1".equals(dataTotalPagesStr)){  //当期数据只有一页 即 上面初始页数据 ，直接返回
					//处理品类所有的商品列表数据
					fetchProducts(sex,categoryName,productDetailUrls);
					return ;
				}
				//类别Code categoryCode
				String categoryCode = productDivElement.attr("data-category-code");

				Integer pageNumber = Integer.parseInt(dataTotalPagesStr);
				//请求地址demo: https://www.gucci.com/it/it/c/productgrid?categoryCode=women-handbags-top-handles-and-boston-bags&show=Page&page=1
				String currentBasePageUrl = uri + "/it/it/c/productgrid"+"?categoryCode="+categoryCode+"&show=Page&page=";
                /**
                 * 注意 分页json返回的数据 的 "productLink":"/pr/women/handbags/womens-shoulder-bags/gg-marmont-embroidered-velvet-mini-bag-p-446744K4DST5668",
                 * 都是以 /pr 开头 多以在 前面追加 /it/it
                 */
				for (int i = 1; i < pageNumber; i++) { //获取下一页 ，从第1页开始获取数据  初始页数据:page = 0
					currentBasePageUrl+=i;

                    String  productsJsonStr = "";
                    HttpResponse temResponse = HttpUtils.get(currentBasePageUrl,headers);
					if(temResponse.getStatus()==200){
                        productsJsonStr = temResponse.getResponse();
                    }else{
                        System.err.println("请求商品品类 分页地址出错 categoryUrl： "+categoryUrl+" currentBasePageUrl:"+currentBasePageUrl);
                        logger.info("请求商品品类 分页地址出错 categoryUrl： "+categoryUrl+" currentBasePageUrl:"+currentBasePageUrl);
                        return;
                    }

					JsonElement je = new JsonParser().parse(productsJsonStr);
					JsonArray products = je.getAsJsonObject().get("products").getAsJsonObject().get("items").getAsJsonArray();
					for (int j = 0; j < products.size(); j++) {
                        JsonElement jsonElement = products.get(j);
                        String productDetailUri = jsonElement.getAsJsonObject().get("productLink").toString();
                        // 解决 /pr/women/handbags/womens-shoulder-bags/gg-marmont-mini-bag-p-446744K4D2T6433
                        if(productDetailUri.contains("\"")){
                            productDetailUri = productDetailUri.replace("\"","");
                        }
                        if(productDetailUri.contains("\'")){
                            productDetailUri = productDetailUri.replace("\'","");
                        }

                        if(productDetailUri.startsWith("/pr")){
                            productDetailUri = "/it/it"+productDetailUri;
                        }
						String productDetailUrl = uri + productDetailUri;
						productDetailUrls.add(productDetailUrl);
                    }

				}
				//处理品类所有的商品列表数据
				fetchProducts(sex,categoryName,productDetailUrls);
			}else{
			    //添加到 失败 请求中
                failList.add(new RequestFailProductDTO(sex, categoryName, categoryUrl, "1"));
                System.err.println("请求商品品类 地址出错  "+categoryUrl);
                logger.error("请求商品品类 地址出错categoryUrl {}  "+categoryUrl);
                return;
			}
		}catch (Exception e){
				e.printStackTrace();
		}
	}

	/**
	 * 遍历商品列表数据 数据并保存
	 * @param sex 性别
	 * @param categoryName 列表名称
	 * @param productDetailUrls 商品列表Url
	 */
	public void fetchProducts(String sex,String categoryName,List<String> productDetailUrls){
		if(productDetailUrls.size()>0){
			for (String productDetailUrl:productDetailUrls) {
				savePre(sex,categoryName,productDetailUrl);
			}
		}
	}

	/**
	 * 获取某一 style 商品信息（单个或多个size）
	 * @param url
	 * @return
	 */
	private  List<GucciProductDTO> getProductUrlForProject(String sex, String categoryName, String url) {
		List<GucciProductDTO> products = new ArrayList<>();

		GucciProductDTO product = new GucciProductDTO();
		//材质
		String  material = "";
		//商品描述
		String  desc= "";
		//商品名称
		String productName="";
		//供应商 spuNo
		String spuNo = "";
		//货号
		String  barCode= "";
		//品牌
		String brand = "GUCCI";
		//颜色 名称
		String colorCode = "";
		//商品 尺寸大小描述
		StringBuffer sizeDesc=new StringBuffer();
		//产地
		String made = "意大利制造";
		//市场价
		String itemPrice="";
		//销售价
		String salePrice="";
		//库存
		String qty="";
		//库存描述信息
        String qtyDesc = "";
		//图片信息
		StringBuffer picSrcs = new StringBuffer();  //多张图片 链接Str  用|分割
		String season = "2018春夏";


		try {
			Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
			Header[] headers = new Header[1];
			headers[0] = header;

			HttpResponse response = HttpUtils.get(url,headers);

			if (response.getStatus()==200) {

				String htmlContent = response.getResponse();
				Document doc = Jsoup.parse(htmlContent);

				/** 商品名称
				 <input type="hidden" class="gucciProductName" value="Borsa a mano Nymphaea misura piccola">
				 */
				Element proNameElement  = doc.select("input.gucciProductName").first();
				if(proNameElement!=null){
					productName = proNameElement.attr("value").replaceAll("\r|\n", "");
				}

				/**
				 *  商品货号

				 <input type="hidden" class="gucciProductCode" value="453767DVU0G1000">
				 */
				Element barCodeElement  = doc.select("div.product-info-wrapper").select("div.style-number-title").select("span").first();
				if(barCodeElement!=null){
					barCode = barCodeElement.text().substring(2); //款式barCode 去除前 两个 &nbsp;&lrm;
				}
				/** 商品价格
				 * <input type="hidden" class="gucciProductPrice" value="1690.0">
				 */
				//获取商品市场价
				Element itemPriceElement  = doc.select("input.gucciProductPrice").first();
				if(itemPriceElement!=null){
					itemPrice = itemPriceElement.attr("value");
				}

				//商品售价
				salePrice = itemPrice;

				//处理商品图片信息 7张图片
				/**
				 *
				 <picture>
				 <source>...</source>
				 <source data-image-size="standard-retina" srcset="//media.gucci.com/style/DarkGray_Center_0_0_1200x1200/1473184822/453767_DVU0G_1000_007_076_0000_Light-Borsa-a-mano-Nymphaea-misura-piccola.jpg" media="(min-width: 1024px) and (-webkit-min-device-pixel-ratio: 2), (min-width: 1024px) and (min-resolution: 192dpi)">
				 <source>...</source>
				 </picture>
				 */
				Elements imgsElements = doc.select("picture").select("source[data-image-size=standard-retina]");
				int i = 0;
				for(Element img : imgsElements) {
					String imgUrl = "https:"+ img.attr("srcset");
					if(i==0){
						picSrcs.append(imgUrl);
					}else{
						picSrcs.append("|").append(imgUrl);
					}
					i++;
				}

				//商品描述 spice-product-detail
				/**
				 <div class="accordion-drawer" id="product-details" style="">
				 <div class="product-detail columnize-by-2">
				 <p>
				 Ispirata dai miti greci e latini ricchi di riferimenti a fiori acquatici e ninfe con perle tra i capelli, la borsa a mano Nymphaea è caratterizzata dal doppio manico in bambù, distintivo della Maison, impreziosito da perle color crema ai lati. Realizzato in morbida pelle nera, questo modello misura piccola si distingue per la naturale lucentezza e l’aspetto flessibile.
				 </p>
				 <ul>
				 <li>Pelle nera</li>
				 <li>Finiture color oro</li>
				 <li>Cerniera interna e tasche per smartphone</li>
				 <li>Doppio manico in bambù con perle, altezza (luce) 8cm</li>
				 <li>Tracolla staccabile, altezza (luce) 49cm</li>
				 <li>Misura piccola: L27cm x A22cm x P13cm</li>
				 <li>Fodera in microfibra effetto camoscio</li>
				 <li>Made in Italy</li>
				 </ul>
				 </div>
				 <div class="product-detail-2">
				 </div>
				 </div>
				 */
				Element descriptElement  = doc.select("div#product-details").select("p").first();
				if(descriptElement!=null){
					desc = descriptElement.text();
					desc = desc.replace(",",".").replaceAll("\r|\n", "");
				}

				//季节
				if(desc.contains("Primavera Estate")){
					season = "春夏";
				}else if(desc.contains("Pre-Fall")){
					season = "早秋";
				}else if(desc.contains("Primavera")){
					season = "春季";
				}else if(desc.contains("Estate")){
					season = "夏季";
				}else if(desc.contains("Autunno")){
					season = "秋季";
				}else if(desc.contains("Inverno")){
					season = "冬季";
				}else if(desc.contains("Autunno Inverno")){
					season = "秋冬季";
				}

				//商品其他信息 如 颜色、产地、省份、尺码大小
				Elements otherInfoElements  = doc.select("div#product-details").select("ul").select("li");

				for (Element otherInfoElement:otherInfoElements) {
					/**
					 *  1.处理其他信息规则 含有 % 的为材质(% 优先级高)
                     *  2.信息 包含有 materiale 的 前面为颜色 ，后面为材质
					 */
					String text = otherInfoElement.text();

					if(text!=null){
                        text = text.replace(",",".");
                        //处理 颜色 和 材质 信息
						if(text.contains("%")){
							//100% cotone
							material = text;
						}else if(text.contains("materiale")){
                            String[] colorAndMateriales = text.split("materiale");
                            //只取第一个颜色信息
                            colorCode = colorAndMateriales[0];
                            // material 为空或者 "" 进行赋值
                            if(material==null||"".equals(material)){
                                material = colorAndMateriales[colorAndMateriales.length-1];
                            }
						}
                        // 处理包类产品 尺寸大小信息
						// ".*[0-9]cm.*"  判断字符串 中是否包含 3cm 形式的字符串

                        if(text.contains("Misura media")||text.matches(".*[0-9]cm.*")){
                            sizeDesc.append(" "+text);
                        }
					}
				}
				if(colorCode==null||"".equals(colorCode)){
					colorCode= otherInfoElements.eq(0).text();
					if("".equals(material)||null==material){
						material = otherInfoElements.eq(0).text();
					}
				}

                colorCode = colorCode.replace(",",".");
                material = material.replace(",",".");

                //设置 spuNo
                spuNo = barCode;

				product.setUrl(url);
				product.setMaterial(material);
				product.setProductname(productName);
				product.setPrice(itemPrice);
				product.setBrand(brand);
				product.setProductCode("");
				product.setDescript(desc);
				product.setBarCode(barCode);
				product.setMade(made);
				product.setSex(sex);
				product.setSeason(season);
				product.setColorCode(colorCode);
				product.setSizeDesc(sizeDesc.toString());
				product.setItemprice(itemPrice);
				product.setItemsaleprice(salePrice);

				product.setSpuNo(spuNo);
				product.setCategory(categoryName);
				product.setPicUrls(picSrcs.toString().replace("\"",""));


				/**
				 *   处理商品 的尺码 以及 库存信息
				 <form action="/it/it/cart/ajax/add" class="add-to-shopping-bag-form" id="product-detail-add-to-shopping-bag-form">
				 <input name="sku" type="hidden" value="747804" />
				 <input class="personalized" name="personalized" type="hidden" value="false" />
				 <input class="monogramStyle" name="monogramStyle" type="hidden" value="" />
				 <input class="monogram" name="monogram" type="hidden" value="" />
				 <input name="position" type="hidden" value="1" />
				 <input name="listName" type="hidden" value="PGEU4Cols" />
				 <input name="categoryPath" type="hidden" value="Women/Womens-Ready-to-Wear/Coats" />
				 <input class="CSRFToken" name="CSRFToken" type="hidden" value="fca2c19f-3364-4fe3-ba86-c32d8015b53f" />

				 <div class="sizes">
				 <div class="size-dropdown " data-module="productSizes" >
				 <div class="content-select basic-content-select">
				 <select name="size" class="custom size-select _disable-mobile "   aria-label="Taglia">
				 <option value="-1" selected="selected">Taglia</option>
				 <option value="808313659" data-copy="36" data-copy-device="36&nbsp;" data-available="true" class="" >
				 36&nbsp;
				 </option>
				 <option value="808305674" data-copy="38" data-copy-device="38&nbsp;" data-available="true" class="" >
				 38&nbsp;
				 </option>
				 <option value="808225409" data-copy="40" data-copy-device="40&nbsp;" data-available="true" class="" >
				 40&nbsp;
				 </option>
				 <option value="808305927" data-copy="42" data-copy-device="42&nbsp;" data-available="true" class="" >
				 42&nbsp;
				 </option>
				 <option value="808305928" data-copy="44" data-copy-device="44&nbsp;" data-available="true" class="" >
				 44&nbsp;
				 </option>
				 <option value="808310044" data-copy="46" data-copy-device="46&nbsp;" data-available="true" class="" >
				 46&nbsp;
				 </option>
				 <option value="808313400" data-copy="48" data-copy-device="48&nbsp;Ricerca nei negozi" data-available="false" class="sizes-item-value-disabled" >
				 48&nbsp;
				 </option>
				 </select>

				 */
				//判断当前包页面有没有尺码信息  有分成多个 product 没有 尺码为均码
				//   .spice-dropdown-pdp-size-box
				Elements temSizeElements = doc.select("select[name=size]");
				if(temSizeElements!=null&&temSizeElements.size()>0){
					Elements sizeElements =temSizeElements.first().select("option");

					for (Element sizeElement:sizeElements ) {
						String pcode = sizeElement.attr("value");
						String classValue = sizeElement.attr("class");
						String sizeContent = sizeElement.attr("data-copy");

						if(sizeContent!=null&&!"".equals(sizeContent)){
                            if(pcode==null||"-1".equals(pcode)||"Taglia".equals(sizeContent)){  //跳过尺码提示语选项
                                continue;
                            }
                            //处理特殊字符均码
							if(sizeContent.startsWith("U")&&sizeContent.length()==2){
								sizeContent = "U";
							}
						}else{
						    continue;
                        }

						//如果<option> 标签上含有 class="sizes-item-value-disabled" 为无库存
						if("sizes-item-value-disabled".equals(classValue)){
							qty = NO_STOCK;
                            qtyDesc = "售罄";
						}else{
							//获取库存数据
							//
							//获取 pcode 参数value
							Map<String, String> checkProductInfo = getProductQtyInfo(pcode,url);
							String temQty = checkProductInfo.get("qty");
                            qtyDesc = checkProductInfo.get("qtyDesc");

							if(temQty!=null){
								qty = temQty;
							}else{ //获取库存失败 库存为0
								qty = NO_STOCK;
                                qtyDesc = "售罄";
							}
						}

						product.setSize(sizeContent);
						product.setQty(qty);
						product.setQtyDesc(qtyDesc);
						products.add(GucciProductDTO.copyProductData(product));
						/**
						 * 获取每一个尺码都休息 0.5s
						 */
						//Thread.sleep(500);
					}
				}else{
					//处理 详情其他信息
					product.setQty(qty);
					product.setSize("U");
					products.add(product);
				}
			}else{
                failList.add(new RequestFailProductDTO(sex,categoryName,url,"0"));
                System.err.println("it-getProductUrlForProject请求商品url 失败"+sex +":"+categoryName+":"+url);
                logger.error("it-getProductUrlForProject请求商品url 失败"+sex +":"+categoryName+":"+url);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return products;
	}

	/**
	 *  尺码 code
	 * @param pcode 商品尺码code
	 * @param url  商品详情URL
	 * @return
	 */
	public  Map<String,String> getProductQtyInfo(String pcode,String url){

		//库存结果集
		HashMap<String, String> mapDate = new HashMap<>();

		//  /it/it/p/ajax/product-detail-shipping.ajax?pcode=808354516

		String checkProductQtyUrl = uri + "/it/it/p/ajax/product-detail-shipping.ajax?pcode="+pcode;

		try {
			getProductQtyUnit(checkProductQtyUrl,mapDate);
		} catch (Exception e) {
			logger.info("=========尝试再次获取=====");
			try {
				getProductQtyUnit(checkProductQtyUrl,mapDate);
			} catch (Exception ee) {
				//获取不到库存 默认为0
				mapDate.put("qty",NO_STOCK);
				mapDate.put("qtyDesc","售罄");

				logger.info("=========第二次：获取库存失败 ===url: "+url +"pcode:"+pcode);
				ee.printStackTrace();
				System.out.println();
				System.err.println("=========第二次：获取库存失败 ===url: "+url +"pcode:"+pcode);
			}
		}
		return mapDate;
	}


	/**
	 * 获取商品库存信息
	 * @param checkProductUrl
	 * @throws Exception
	 */
	public void getProductQtyUnit(String checkProductUrl,Map<String,String> mapDate) throws Exception{
		Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
		Header[] headers = new Header[1];
		headers[0] = header;
		HttpResponse checkResponse = HttpUtils.get(checkProductUrl,headers);
		if (checkResponse.getStatus()==200) {
			/**
			 <p class="title">DISPONIBILE	</p>
			 <div class="subtitle">Il prodotto è disponibile per la spedizione. La consegna è stimata in 1-2 giorni lavorativi.</div>
			 */
			String responseHtml = checkResponse.getResponse();
			if(responseHtml==null||"".equals(responseHtml)){
				//没有库存
				mapDate.put("qty",NO_STOCK);
                mapDate.put("qtyDesc","售罄");
				return;
			}
			Document doc = Jsoup.parse(responseHtml);

			String qtyStr = doc.select("p.title").first().text();
			String qtyStrDesc = doc.select("div.subtitle").first().text();
			// DISPONIBILE SOLO PER PRE-ORDINE | DISPONIBILE  | DISPONIBILE IN 1-2 SETTIMANE

			if(qtyStr!=null){
				if(qtyStr.contains("PRE-ORDINE")){ //预售
					mapDate.put("qty",NO_STOCK);
					mapDate.put("qtyDesc","预售");
				}else if(qtyStr.contains("DISPONIBILE")){
					if(qtyStrDesc.contains("1-2")){
						mapDate.put("qty",IN_STOCK);
						mapDate.put("qtyDesc","有货");
					}else{
						mapDate.put("qty",NO_STOCK);
						mapDate.put("qtyDesc","发货时长超过两天");
					}
				}
			}else{
				// 售罄
				mapDate.put("qty",NO_STOCK);
                mapDate.put("qtyDesc","售罄");
			}
		}else{
			mapDate.put("qty",NO_STOCK);
            mapDate.put("qtyDesc","售罄");
		}
	}


	/**
	 * 访问商品所有style url，并输出到csv文件
	 * @param productDetailUrl 商品详情url
	 */
	private void savePre(String sex,String categoryName,String productDetailUrl){
		if(StringUtils.isBlank(productDetailUrl)){
			return;
		}
		try {
			Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
			Header[] headers = new Header[1];
			headers[0] = header;

			HttpResponse response = HttpUtils.get(productDetailUrl,headers);

            if (response.getStatus()==200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
                /**
                 * 判断当前商品是否含有多个style

                 <div id="style-selector-overlay" class="overlay style-selector-overlay" data-module="overlay">
                 <div class="overlay-wrapper style-selector-overlay-wrapper">
                 <div class="overlay-content-wrapper style-selector-overlay-content-wrapper">
                 <section class="overlay-content style-selector-overlay-content">
                 <button class="overlay-close overlay-close-button-first" type="button">Close Overlay</button>
                 <div class="style-selector-carousel">
                 <div class="container-carousel-style-selector" data-module="slickCarousel">
                 <div class="carousel-inner">
                 <div 	data-code="453767DVU0G1000"
                 data-url="/it/it/pr/women/handbags/womens-totes/nymphaea-small-top-handle-bag-p-453767DVU0G1000?position=1&listName=VariationOverlay"
                 data-id="3"
                 data-title="Borsa a mano Nymphaea misura piccola"
                 data-color-material="pelle nera"
                 data-image="yellow"
                 data-price="€ 1.690"
                 data-full-price=""
                 class="carousel-slide">
                 <img alt="Borsa a mano Nymphaea misura piccola"
                 data-src="//media.gucci.com/style/White_Center_0_0_365x245/1473184819/453767_DVU0G_1000_001_076_0000_Light.jpg"
                 data-src_medium="//media.gucci.com/style/White_Center_0_0_320x180/1473184819/453767_DVU0G_1000_001_076_0000_Light.jpg"
                 data-src_medium_retina="//media.gucci.com/style/White_Center_0_0_640x360/1473184819/453767_DVU0G_1000_001_076_0000_Light.jpg"
                 data-src_small="//media.gucci.com/style/White_Center_0_0_365x245/1473184819/453767_DVU0G_1000_001_076_0000_Light.jpg"
                 data-src_small_retina="//media.gucci.com/style/White_Center_0_0_730x490/1473184819/453767_DVU0G_1000_001_076_0000_Light.jpg"
                 data-src_standard="//media.gucci.com/style/White_Center_0_0_250x170/1473184819/453767_DVU0G_1000_001_076_0000_Light.jpg"
                 data-src_standard_retina="//media.gucci.com/style/White_Center_0_0_500x340/1473184819/453767_DVU0G_1000_001_076_0000_Light.jpg"
                 src="//media.gucci.com/style/White_Center_0_0_250x170/1473184819/453767_DVU0G_1000_001_076_0000_Light.jpg"
                 />
                 */
                Elements styleElements = doc.select("div#style-selector-overlay")
                        .select("section").select("div.style-selector-carousel")
                        .select("div.container-carousel-style-selector").select("div").select("div").select("div").first().select("div");
                //.select("class[aria-live=polite]").select("div.slick-track").select("div");
                //商品多款和单款都放在一起
                if(styleElements!=null&&styleElements.size()>0){
                    for (Element element: styleElements) {
                        String productDetailLink = element.attr("data-url");
                        if(productDetailLink==null||"".equals(productDetailLink)){
                            continue;
                        }
                        //商品barCode
                        String productSpu = element.attr("data-code");
                        //保存并处理商品数据
                        productDetailLink = uri + productDetailLink;
                        saveAndSolveRepeatSpu(sex,categoryName,productDetailLink,productSpu);
                    }
                }
            }else{
                failList.add(new RequestFailProductDTO(sex,categoryName,productDetailUrl,"0"));
                System.err.println("it-savePre请求商品url 失败"+sex +":"+categoryName+":"+productDetailUrl);
                logger.error("it-savePre请求商品url 失败"+sex +":"+categoryName+":"+productDetailUrl);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

    /**
     * 保存并处理重复商品数据 时机：获取具体某一style 商品数据 前（判断是否拉取过） 后（将处理过的spu 存入skuMap ）
     * @param sex 性别
     * @param categoryName 品类名称
     * @param productDetailLink 商品详情绝对路径
     * @param barCode 商品货号 如:524239JCIAT9578 (不带空格)
     */
	public void saveAndSolveRepeatSpu(String sex,String categoryName,String productDetailLink,String barCode){

		//校验之前是否已经存储过该SPU
		if(barCodeMap.containsKey(barCode)){ //之前处理过
			return;
		}
		//获取商品详情页数据
		saveByProductDetailLink( sex, categoryName, productDetailLink);
		/**
		 * 将商品的 barCode 加入到 barCodeMap 中
		 */
        barCodeMap.put(barCode,null);
		System.out.print(" "+barCodeMap.size());
	}

	/**
	 * 访问商品 某一style 获取该 style 所有 size ，并输出到csv文件
	 * @param proUrl 商品详情页url
	 */
	private void saveByProductDetailLink(String sex,String categoryName,String proUrl)  {

		//获取商品详情页数据，多个商品信息是同一个 barCode 只是size 不同
		List<GucciProductDTO> pros = getProductUrlForProject(sex,categoryName,proUrl);
		for (GucciProductDTO pro:pros) {
			exportExcel(pro);
		}
		/**
		 * 拉取商品某一个style 就sleep 10s
		 */
		/*try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
	}

	/**
	 * 导出单个商品信息到csv 文件（追加）
	 * @param dto 商品信息DTO
	 */
	private static void exportExcel(GucciProductDTO dto) {
		//继续追加
		StringBuffer buffer = new StringBuffer();
		try {
			buffer.append(dto.getSex()).append(splitSign);
			buffer.append(dto.getBrand()).append(splitSign);

			buffer.append(dto.getCategory()).append(splitSign);
			buffer.append(dto.getSpuNo()).append(splitSign);

			buffer.append(dto.getBarCode()).append(splitSign);
			buffer.append((dto.getSeason()==null||"".equals(dto.getSeason()))?"2018春夏":dto.getSeason()).append(splitSign);

			buffer.append(dto.getMaterial()).append(splitSign);
			buffer.append(dto.getColorCode()).append(splitSign);

			buffer.append(dto.getSize()).append(splitSign);
			buffer.append(dto.getProductname()).append(splitSign);

			buffer.append(dto.getItemprice()).append(splitSign);
			buffer.append(dto.getItemsaleprice()).append(splitSign);

			buffer.append(dto.getQty()).append(splitSign);
			buffer.append(dto.getQtyDesc()).append(splitSign);


			buffer.append(dto.getMade()).append(splitSign);

			buffer.append(dto.getSizeDesc()+"。"+dto.getDescript()).append(splitSign);
			buffer.append(dto.getPicUrls()).append(splitSign);

			buffer.append(dto.getUrl()).append(splitSign);

            buffer.append(dto.getSizeDesc()).append(splitSign);

            buffer.append(supplierId).append(splitSign);
            buffer.append(supplierNo).append(splitSign);
            buffer.append(splitSign);
			buffer.append("\r\n");
			out.write(buffer.toString());
			logger.info(buffer.toString());
			out.flush();
		} catch (Exception e) {
		}
	}




	public static void main(String[] args) throws IOException {

		//每次调用都初始化 barCodeMap 以及 failList
		barCodeMap = new HashMap<String,String>();
		failList = new ArrayList<>();


		System.out.println("要下载的url："+uri);
		System.out.println("文件保存目录："+path);
		try {
			out = new OutputStreamWriter(new FileOutputStream(path, true),"gb2312");
		} catch (IOException e) {
			e.printStackTrace();
		}


		String barCodeArr = "493117 X3P54 1082,\n" +
				"492347 X3P55 1082,\n" +
				"492347 X9Z30 2130,\n" +
				"493117 X3P56 7550,\n" +
				"492347 X3P10 5261,\n" +
				"492347 X3N19 5237,\n" +
				"492347 X9S62 1082,\n" +
				"457095 X5L89 1948,\n" +
				"492347 X9Y38 7550,\n" +
				"492347 X9S57 7550,\n" +
				"492347 X9S63 1082,\n" +
				"492347 X9Y30 1082,\n" +
				"469307 X9D35 9230,\n" +
				"515838 X9S55 6527,\n" +
				"515838 X9S55 6527,\n" +
				"493117 X3M44 7550,\n" +
				"492347 X3L83 6310,\n" +
				"535917 X9Z22 9218,\n" +
				"535921 X9Z25 2035,\n" +
				"535918 X9Z23 1082,\n" +
				"539086 X3Q17 9025,\n" +
				"469250 X903B 6527,\n" +
				"517139 X3M10 9247,\n" +
				"469250 X9Y46 9025,\n" +
				"479477 X9Y42 1082,\n" +
				"515013 X9Y54 9025,\n" +
				"469250 X9Y52 1082,\n" +
				"489677 X9N16 5681,\n" +
				"469250 X9S46 9150,\n" +
				"489677 X9N17 4033,\n" +
				"469250 X3L92 9101,\n" +
				"469250 X3P08 5864,\n" +
				"469250 X9S42 9150,\n" +
				"469250 X3N43 5237,\n" +
				"469250 X9S59 1082,\n" +
				"475532 X3M49 9169,\n" +
				"489677 X3L79 9101,\n" +
				"469250 X3P71 6310,\n" +
				"469250 X3P73 3663,\n" +
				"469250 X3P74 5247,\n" +
				"469250 X3P70 4649,\n" +
				"469250 X3P72 4205,\n" +
				"469250 X3P75 7548,\n" +
				"469250 X3P76 9025,\n" +
				"469250 X3P77 1082,\n" +
				"469250 X3P78 4725,\n" +
				"489677 X9P73 5253,\n" +
				"489677 X3L80 1082,\n" +
				"469251 X9Y51 1082,\n" +
				"469251 X3P58 7550,\n" +
				"469251 X999A 2130,\n" +
				"475374 X3P60 1082,\n" +
				"469251 X3L57 9264,\n" +
				"505527 X9P84 1672,\n" +
				"526524 X9V99 4925,\n" +
				"489718 X9F88 1146,\n" +
				"472245 X5S83 1082,\n" +
				"472245 X9H56 1146,\n" +
				"472245 X9H56 7113";

		String[] split = barCodeArr.replace("\n","").split(",");
		int length  =  split.length;
		GucciProductIT gucciProductIT = new GucciProductIT();
		for (int i = 0; i <length ; i++) {
			String barCode = split[i];
			String productUrl = "https://www.gucci.com/it/en_gb/pr/women/womens-ready-to-wear/womens-sweatshirts-t-shirts/oversize-sweatshirt-with-gucci-cities-p-"+ barCode.replace(" ","")+"?position=1&listName=SearchResultGridComponent";
			gucciProductIT.savePre("women","Sweatshirts & T-Shirts",productUrl);
		}
		out.close();
		barCodeMap = null;
		failList = null;
	}


}
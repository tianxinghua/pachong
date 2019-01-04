package com.shangpin.iog.opticalscribe.service;

/**
 * Created by zhaogenchun on 2015/11/26.
 */

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shangpin.iog.opticalscribe.dto.FarfetchBrandNameDTO;
import com.shangpin.iog.opticalscribe.dto.FarfetchCategoryDTO;
import com.shangpin.iog.opticalscribe.dto.FarfetchProductDTO;
import com.shangpin.iog.opticalscribe.tools.DownloadAndReadCSV;
import com.shangpin.iog.pavinGroup.util.HttpResponse;
import com.shangpin.iog.pavinGroup.util.HttpUtils;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.commons.httpclient.Header;
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
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * conf.properties 配置

 rootUrl=https://www.farfetch.cn
 projectUrl=men&&https://www.farfetch.cn/cn/designers/men,women&&https://www.farfetch.cn/cn/designers/women,kids&&https://www.farfetch.cn/cn/designers/kids
 # brandNameFile: 性别,品牌名称 csv文件 每行有两个数据 如：women,gucci 其中品牌名称和官网名称保持一致（可忽略大小写）
 brandNameFile=E:/FARFETCH/farfetch-brand.csv
 path=E:/FARFETCH/farfetch.csv
 # 获取分页数据 分页数据 失败后 重复请求次数
 repeatRequestNumber=7


 */
//@Component("farfetchFetchProductImpl")
public class FarfetchFetchProductImpl {
	@Autowired
	ProductFetchService productFetchService;

	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");

    private static Logger loggerError = Logger.getLogger("error");

	private static ResourceBundle bdl = null;

	//网站根路径（网站的链接为相对路径，用于拼接）
	private static String rootUrl;
	//具体商品url,用于直接爬去商品列表信息，projectUrl 已指定好 性别、品牌、类别、颜色
    private static String projectUrl;

	private static String path;
	private static OutputStreamWriter  out= null;
	static String splitSign = ",";
	private static String brandNameFile="";
	//拉取商品数量计数器
    private static Integer productCount = 0;

	// 所有所需要拉取的性别品牌信息
    private static String allSexBrandStr = "";

    private static Integer  repeatRequestNumber = 5;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");

        rootUrl = bdl.getString("rootUrl");
        projectUrl = bdl.getString("projectUrl");

		path = bdl.getString("path");
        brandNameFile = bdl.getString("brandNameFile");
        String repeatRequestNumberStr = bdl.getString("repeatRequestNumber");
        if(repeatRequestNumberStr!=null&&!"".equals(repeatRequestNumberStr)){
            repeatRequestNumber = Integer.parseInt(repeatRequestNumberStr);
        }

        /**
         * TODO 判断当前品牌是否 需要拉取 拼接所有需要拉取的 性别品牌 str
         */
        List<FarfetchBrandNameDTO> brandList = null;
        try {
            brandList = DownloadAndReadCSV.readLocalCSV(brandNameFile, FarfetchBrandNameDTO.class, ",", "gbk");
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuffer sexBrandBuffer = new StringBuffer();
        sexBrandBuffer.append(",");
        int size = brandList.size();
        for (int i = 0; i < size; i++) {
            FarfetchBrandNameDTO farfetchBrandNameDTO = brandList.get(i);
            sexBrandBuffer.append(farfetchBrandNameDTO.getSex()+"&&"+farfetchBrandNameDTO.getBrandName()).append(",");
        }
        allSexBrandStr = sexBrandBuffer.toString().toLowerCase();

	}

    /**
     * 每一个品牌开一个线程
     */
    class BrandThread extends Thread{
        private String gender;  //性别
        private String brandName; //品牌名称
        private String brandUrl;    //品牌url
        public BrandThread(String gender,String brandName,String brandUrl) {
            this.gender = gender;
            this.brandName = brandName;
            this.brandUrl = brandUrl;
        }
        @Override
        public void run() {
            try {
                fetchBrandHomePageByBrand(gender, brandName, brandUrl);
            } catch (Exception e) {
                logger.warn(Thread.currentThread().getName() + "处理出错", e);
            }
        }
    }

    /**
     * 拉取数据时 每一页开启一个线程
     */
    class PageThread extends Thread{
        private int i;
        private String gender;  //性别
        private String brandName; //品牌名称
        private String categoryName; //类别名称
        private String parentCategory; //类别名称
        private String colorName;    //颜色名称
        private String brandCategoryColorPageUrl;    //品牌url
        public PageThread(String gender,String brandName,String parentCategory,String categoryName,String colorName,String brandCategoryColorPageUrl) {
            this.gender = gender;
            this.brandName = brandName;
            this.categoryName = categoryName;
            this.parentCategory = parentCategory;
            this.colorName = colorName;
            this.brandCategoryColorPageUrl = brandCategoryColorPageUrl;
        }
        @Override
        public void run() {
            try {
                fetchProudctPageByBrandAndColorUrl( gender,  brandName, parentCategory,  categoryName,  colorName,  brandCategoryColorPageUrl);
            } catch (Exception e) {
                logger.warn(Thread.currentThread().getName() + "处理出错", e);
            }
        }
    }

    //	ExecutorService exe=Executors.newFixedThreadPool(500);//相当于跑4遍
    ExecutorService exe = new ThreadPoolExecutor(10,10, 500, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(100),new ThreadPoolExecutor.CallerRunsPolicy());



	/**
	 * 开始 抓取 声称文件头
	 *
	 * 拉取过程
	 * 1.初始化csv文件
	 * 2.拉取网站首页性别全部品牌首页信息Elements（女士、男士、儿童） for
	 * 3.拉取性别-品牌列表信息  for
	 * 4.拉取性别-品牌列表-类别列表信息 for
	 * 5.拉取性别-品牌列表-某一品牌-类别列表信息 for
	 * 6.拉取性别-品牌列表-某一品牌-类别列表-某一类别-颜色列表 for
	 * 7.拉取性别-品牌列表-某一品牌-类别列表-某一类别-颜色列表-某一颜色-分页商品列表 for
	 * 8.拉取性别-品牌列表-某一品牌-类别列表-某一类别-颜色列表-某一颜色-商品列表-某一商品信息 封装存入 csv 文件
	 *
	 * @throws Exception
	 */
	public void getUrlList() throws Exception {
        productCount = 0;

		System.out.println("要下载的url："+rootUrl);
		System.out.println("文件保存目录："+path);
		//初始化csv 文件头行
		try {
			//此处设置为true即可在生成文件追加数据
			out = new OutputStreamWriter(new FileOutputStream(path, true),"gb2312");
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuffer buffer = new StringBuffer("gender" + splitSign +
                "brand" + splitSign +

                "parentCategory" + splitSign +
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

		//请求网站首页
		try {

            String[] sexAndBrandUrlArr = projectUrl.split(",");
            for (int i = 0; i < sexAndBrandUrlArr.length; i++) {
                String[] sexAndBrandUrl = sexAndBrandUrlArr[i].split("&&");
                fetchGenderBrandsByGender(sexAndBrandUrl[0],sexAndBrandUrl[1]);
                System.out.println(""+sexAndBrandUrl[0]+":"+sexAndBrandUrl[1]);
            }
            System.out.println("共有："+productCount+" 个品牌匹配！ ");
            /**
             * TODO 临时添加
             */
            //fetchProudctPageByBrandAndColorUrlPages("women","","","包袋", "","https://www.farfetch.cn/cn/shopping/women/bags-purses-1/items.aspx");
            exe.shutdown();
            while (!exe.awaitTermination(60, TimeUnit.SECONDS)) {
            }
            System.out.println(" 任务执行完毕 ");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 3.获取所有某一性别下品牌列表信息 遍历
	 * @param genderStr 性别
	 * @param genderBrandsUrl 性别to品牌 url
	 */
	private void fetchGenderBrandsByGender(String genderStr,String genderBrandsUrl){
		if(genderBrandsUrl==null){
            System.out.println("性别url genderBrandsUrl 为空，return 不做处理");
            loggerError.error("性别url genderBrandsUrl 为空，return 不做处理");
			return;
		}

		//拉取性别下的信息
		//1.请求性别首页信息
		Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
		Header[] headers = new Header[1];
		headers[0] = header;
		try {
		    //拼接完整路径
			HttpResponse response = HttpUtils.get(genderBrandsUrl,headers);
			//请求成功 获取性别下 品牌列表信息
			if (response.getStatus()==200) {
				String htmlContent = response.getResponse();
				Document doc = Jsoup.parse(htmlContent);

				//获取某一性别下所有的品牌 Element 信息
				Elements brandElements = doc.select("li.designersResultsBrands");
				for (Element brandElement:brandElements) {
					/**
					 *   <li class="force-ltr designersResultsBrands">
					 *         <a data-href="#" href="/cn/shopping/kids/adidas-kids/items.aspx"
					 *                     class="open-persistent-tooltip" data-gender="19018">
					 *                     Adidas Kids
					 *         </a>
					 *   </li>
					 */
                    Element brandInfo = brandElement.select("a").first();
                    //为空直接返回
                    if(brandInfo==null){
                        continue;
                    }
                    //获取品牌名称
                    String brandName = brandInfo.text();
                    //System.out.println(genderStr +","+ brandName);
                    //out.write(genderStr +","+ brandName+"\r\n");

                    String currentSexAndBrandName = ","+ genderStr +"&&"+ brandName +",";
                    currentSexAndBrandName = currentSexAndBrandName.toLowerCase();
                    if(!allSexBrandStr.contains(currentSexAndBrandName)){
                        continue;
                    }
					//获取品牌首页url
					String brandUrl = brandInfo.attr("href");
					if(brandUrl==null||"".equals(brandUrl)){
						System.out.println("----品牌brandUrl为空----");
						logger.info("----品牌brandUrl为空----");
						continue;
					}
                    System.out.println(genderStr +","+ brandName);
					brandUrl = rootUrl+brandUrl;
					//获取该品牌下的 品类列表信息
					//fetchBrandHomePageByBrand(genderStr, brandName, brandUrl);
                    exe.execute(new BrandThread(genderStr, brandName, brandUrl)); //开启品牌线程
                    productCount++;
				}
			}
		} catch (Exception e) {
            System.out.println("请求品牌列表信息 -- genderBrandsUrl=>>"+genderBrandsUrl+" 出现异常："+e.getMessage());
			logger.info("请求品牌列表信息 -- genderBrandsUrl=>>"+genderBrandsUrl+" 出现异常："+e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 解析品牌Element 请求品牌首页获取品类信息列表 遍历
	 * @param genderStr 性别名称 women men childs
	 * @param brandName 品牌名称
     * @param brandUrl 品牌请求url https://www.farfetch.cn/cn/shopping/women/balenciaga/items.aspx?category=135971&colour=0
	 */
	private void fetchBrandHomePageByBrand(String genderStr,String brandName,String brandUrl){
		if(brandUrl==null){
		    return;
        }
        System.out.println("-------开始拉取 品牌："+brandName+" "+genderStr+" "+brandUrl);
        logger.info("-------开始拉取 品牌："+brandName+" "+genderStr+" "+brandUrl);
        //拉取性别下的信息
		//1.请求性别首页信息
		Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
		Header[] headers = new Header[1];
		headers[0] = header;
		try {
            //拼接完整路径
			HttpResponse response = HttpUtils.get(brandUrl,headers);
			//请求成功 获取性别url 列表（女士、男士、儿童）
			if (response.getStatus()==200) {
				String htmlContent = response.getResponse();
				Document doc = Jsoup.parse(htmlContent);
				//获取所有的品牌 Element 信息
				// 商品类别 注意分为两种情况： 有的页面是含有 品类信息的 有的页面中不直接显示出来， 需要从<script> 标签中进行解析
                Elements categroyElements = doc.select("ul#lp-filters");
                if(categroyElements.size()>0){
                    categroyElements = categroyElements.select("li[data-lp-name=category]");
                    if(categroyElements!=null&&categroyElements.size()>0){
                        categroyElements = categroyElements.select("ul.tree").first().select("li.tree-item");
                    }
                }
                if(categroyElements!=null&&categroyElements.size()>0){

                    List<FarfetchCategoryDTO> farfetchCategoryDTOList  = new ArrayList<>();
                    for (Element categroyElement:categroyElements) {
                        //拉取该品类下的 颜色类别信息
                        getMinimalCategoryByElement(null, categroyElement , farfetchCategoryDTOList);
                    }
                    int categorySize = farfetchCategoryDTOList.size();

                    for (int i = 0; i <categorySize ; i++) {
                        FarfetchCategoryDTO farfetchCategoryDTO = farfetchCategoryDTOList.get(i);
                        String categoryUrl = brandUrl + "?category="+farfetchCategoryDTO.getCategoryId();
                        fetchColorHomePageByBrandUrl(genderStr,brandName,farfetchCategoryDTO.getParentCategoryName(),farfetchCategoryDTO.getCategoryName(),categoryUrl);
                    }
                }else{
                    /**
                     * 从页面中的<script> </script> 标签中获取 商品品类 列表信息
                     */
                    //从<script> 中的json 获取 商品的 图片地址信息  window['__initialState_slice-pdp__'] = {"config":{
                    Elements scripts = doc.select("script");
                    for(Element script : scripts) {
                        if (script.html().contains("window['__initialState_slice-listing__']")) { //注意这里一定是html(), 而不是text()
                            String str = script.html().replace(";", ""); //这里是为了解决 无法多行匹配的问题
                            String jsonStr = str.replace("window['__initialState_slice-listing__'] = ","").trim();
                            JsonElement je = new JsonParser().parse(jsonStr);
                            JsonObject asJsonObject = je.getAsJsonObject();
                            //JsonArray categoryArray = asJsonObject.get("listing").getAsJsonObject().get("facets").getAsJsonObject().get("category").getAsJsonObject().get("values").getAsJsonArray().get("children").getAsJsonArray();
                            JsonArray valueArray = asJsonObject.get("listing").getAsJsonObject().get("facets").getAsJsonObject().get("category").getAsJsonObject().get("values").getAsJsonArray();
                            int valueSize = valueArray.size();
                            List<FarfetchCategoryDTO> farfetchCategoryDTOList  = new ArrayList<>();
                            for (int i = 0; i < valueSize ; i++) {
                                JsonObject categoryJsonObject = valueArray.get(i).getAsJsonObject();
                                getMinimalCategoryByEJsonObject("",categoryJsonObject,farfetchCategoryDTOList);
                            }
                            int categorySize = farfetchCategoryDTOList.size();
                            for (int i = 0; i < categorySize ; i++) {
                                FarfetchCategoryDTO farfetchCategoryDTO = farfetchCategoryDTOList.get(i);
                                //System.out.println(farfetchCategoryDTO.getCategoryName()+":"+farfetchCategoryDTO.getCategoryId()+":"+farfetchCategoryDTO.getParentCategoryName());
                                String categoryUrl = brandUrl + "?category="+farfetchCategoryDTO.getCategoryId();
                                fetchColorHomePageByBrandUrl(genderStr,brandName,farfetchCategoryDTO.getParentCategoryName(),farfetchCategoryDTO.getCategoryName(),categoryUrl);
                            }
                        }
                    }
                }
			}else{
				System.out.println("--- 请求品牌地址失败 brand:"+brandUrl);
                loggerError.error("--- 请求品牌地址失败 brand:"+brandUrl);
			}
		} catch (Exception e) {
            loggerError.error("请求品牌首页 brandUrl=>>"+brandUrl+"获取类别列表 出现异常："+e.getMessage());
			e.printStackTrace();
		}
	}

    /**
     * 获取商品最小品类信息
     * @param parentCategoryName 父品类名称
     * @param parentElement  传入 最大 类别 li 标签 如：
     *                       <li class="facet-item tree-item  active" role="treeitem" data-tstid="Action_ListingTreeFilter" data-index="0">
     *                           <a id="category-135967" class="tree-title  no-underline primary" href="/cn/shopping/women/atlantic-stars/clothing-1/items.aspx" title="服装" data-lp-value="135967">
     *                               服装 <span class="glyphs tree-item-icon icon-next"></span> </a>
     *                           <ul class="facet-content tree-content subtree js-accordion" data-index="2" style="">.. <ul/>
     *                       </li>
     * @param farfetchCategoryDTOList 存放所有 最小品类List 不可为空
     * @return
     */
	public void getMinimalCategoryByElement(String parentCategoryName,Element parentElement,List<FarfetchCategoryDTO> farfetchCategoryDTOList){
	    if(farfetchCategoryDTOList ==null){
            System.out.println(" farfetchCategoryDTOList 为空");
	        return;
        }
        Elements aElements = parentElement.select("a");
        if(aElements==null||aElements.size()==0){ //没有 a 标签
            return;
        }
        Element firstA = aElements.first();
        //获取品类 categoryId
        String categoryId = firstA.attr("data-lp-value");
        //获取类别名称
        String categoryName = firstA.attr("title");

        Elements ulElements = parentElement.select("ul");
        if(ulElements!=null&&ulElements.size()>0){ //说明还有 子级品类
            Elements liElements = ulElements.first().children();
            for (Element liElement: liElements) {
                String temParentCategoryName = "";
                if(parentCategoryName==null||"".equals(parentCategoryName)){
                    temParentCategoryName = categoryName;
                }else{
                    temParentCategoryName = parentCategoryName +"-"+categoryName;
                }
                getMinimalCategoryByElement(temParentCategoryName, liElement,farfetchCategoryDTOList);
            }
        }else{ //没有子品类信息
            FarfetchCategoryDTO farfetchCategoryDTO = new FarfetchCategoryDTO(parentCategoryName,categoryName,categoryId);
            farfetchCategoryDTOList.add(farfetchCategoryDTO);
        }
    }


    /**
     * 获取商品最小品类信息
     * @param parentCategoryName 父品类名称
     * @param parentJsonObject  父品类 JsonObject
     * @param farfetchCategoryDTOList 存放所有 最小品类List 不可为空
     * @return
     */
    public void getMinimalCategoryByEJsonObject(String parentCategoryName,JsonObject parentJsonObject,List<FarfetchCategoryDTO> farfetchCategoryDTOList){
        if(farfetchCategoryDTOList ==null){
            System.out.println(" farfetchCategoryDTOList 为空");
            return;
        }

        //获取品类 categoryId
        String categoryId = parentJsonObject.get("value").toString().replace("\"","");
        //获取类别名称
        String categoryName = parentJsonObject.get("description").toString().replace("\"","");

        JsonElement children = parentJsonObject.get("children");
        if(children!=null){ //说明还有 子级品类
            JsonArray asJsonArray = children.getAsJsonArray();
            int size = asJsonArray.size();
            for (int i = 0; i < size ; i++) {
                JsonObject asJsonObject = asJsonArray.get(i).getAsJsonObject();
                String temParentCategoryName = "";
                if(parentCategoryName==null||"".equals(parentCategoryName)){
                    temParentCategoryName = categoryName;
                }else{
                    temParentCategoryName = parentCategoryName +"-"+categoryName;
                }
                getMinimalCategoryByEJsonObject(temParentCategoryName, asJsonObject,farfetchCategoryDTOList);
            }
        }else{ //没有子品类信息
            FarfetchCategoryDTO farfetchCategoryDTO = new FarfetchCategoryDTO(parentCategoryName,categoryName,categoryId);
            farfetchCategoryDTOList.add(farfetchCategoryDTO);
        }
    }



    /**
     * 获取品类下某一品类下颜色列表信息 遍历
     * @param genderStr 性别
     * @param brandName 品牌名称
     * @param parentCategoryName 父品类名称
     * @param categoryName 父级品类名称
     * @param categoryUrl 品类url https://www.farfetch.cn/cn/shopping/women/balenciaga/items.aspx?category=135971
     */
    private  void fetchColorHomePageByBrandUrl(String genderStr, String brandName,String parentCategoryName,String categoryName,String categoryUrl){

        //拉取颜色列表下的信息
        //1.请求性别首页信息
        Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
        Header[] headers = new Header[1];
        headers[0] = header;
        try {
            //拼接完整路径
            HttpResponse response = HttpUtils.get(categoryUrl,headers);
            //请求成功 获取到该类别下有的颜色列表
            if (response.getStatus()==200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
                //获取所有该品类下有货颜色 Element 信息  获取方式1：
				Elements colorElements1 = doc.select("ul#lp-filters").select("li[data-lp-name=colour]").select("ul").select("li.facet-item");
                if(colorElements1.size()>0){
                    for (Element colorElement:colorElements1) {
                        /**
                         <li class="facet-item  " role="treeitem" data-tstid="Div_ListingFilterOption">
                         <a id="colour-5" class="ffInput js-lp-multiple-option  ffInput--disabled no-underline primary" title="棕色" data-lp-value="5">
                         <span class="ffInput__checkbox"></span> 棕色
                         </a>
                         </li>
                         */
                        //获取品牌下颜色列表信息（其实颜色是写死的，有的类别下只有其中几种颜色的商品）
                        String aClass = colorElement.attr("class");
                        if(aClass!=null&&aClass.contains("hide")){ //默认值color=0 查询全部
                            continue;
                        }
                        Element aElments = colorElement.select("a").first();
                        String classStr = aElments.attr("class");
                        if(classStr!=null&&classStr.contains("ffInput--disabled")){  //该颜色无货
                            continue;
                        }
                        String colorValue = aElments.attr("data-lp-value");
                        String colorName = aElments.attr("title");
                        String categoryAndColorUrl = categoryUrl + "&colour="+colorValue;
                        fetchProudctPageByBrandAndColorUrlPages( genderStr,  brandName, parentCategoryName, categoryName,colorName, categoryAndColorUrl);
                    }
                }else{
                    /**
                     * 从页面中的<script> </script> 标签中获取 商品颜色 列表信息
                     */
                    //从<script> 中的json 获取 商品的 图片地址信息  window['__initialState_slice-pdp__'] = {"config":{
                    Elements scripts = doc.select("script");
                    for(Element script : scripts) {
                        if (script.html().contains("window['__initialState_slice-listing__']")) { //注意这里一定是html(), 而不是text()
                            String str = script.html().replace(";", ""); //这里是为了解决 无法多行匹配的问题
                            String jsonStr = str.replace("window['__initialState_slice-listing__'] = ","").trim();
                            JsonElement je = new JsonParser().parse(jsonStr);
                            JsonObject asJsonObject = je.getAsJsonObject();
                            //JsonArray categoryArray = asJsonObject.get("listing").getAsJsonObject().get("facets").getAsJsonObject().get("category").getAsJsonObject().get("values").getAsJsonArray().get("children").getAsJsonArray();
                            JsonArray valueArray = asJsonObject.get("listing").getAsJsonObject().get("facets").getAsJsonObject().get("colour").getAsJsonObject().get("values").getAsJsonArray();
                            int valueSize = valueArray.size();
                            for (int i = 0; i < valueSize ; i++) {
                                JsonObject colorObject = valueArray.get(i).getAsJsonObject();
                                //颜色ID
                                String colorId  = colorObject.get("value").toString().replace("\"","");
                                //颜色名称
                                String colorName = colorObject.get("description").toString().replace("\"","");;
                                //该品类 颜色下的商品数量
                                String colorCount = colorObject.get("count").toString().replace("\"","");;
                                if(colorCount==null||"".equals(colorCount)){
                                    continue;
                                }
                                if(colorCount!=null&&Integer.parseInt(colorCount)==0){
                                    continue;
                                }
                                String categoryAndColorUrl = categoryUrl + "&colour="+colorId;
                                fetchProudctPageByBrandAndColorUrlPages( genderStr,  brandName,parentCategoryName, categoryName,colorName, categoryAndColorUrl);
                            }
                        }
                    }
                }
            }else{
                System.out.println("--- 请求品类地址失败 categoryUrl:"+categoryUrl);
                loggerError.error("--- 请求品类地址失败 categoryUrl:"+categoryUrl);
            }
        } catch (Exception e) {
            loggerError.error("---请求类别 categoryUrl ："+categoryUrl+"获取颜色列表出现异常："+e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 获取品牌 品类 颜色 下的商品列表信息
     * @param genderStr 性别
     * @param brandName 品牌名称
     * @param categoryName 品类名称
     * @param colorName 颜色名称
     * @param categoryAndColorUrl 请求路径 https://www.farfetch.cn/cn/shopping/women/balenciaga/items.aspx?category=135971&colour=0
     */
    private  void fetchProudctPageByBrandAndColorUrlPages(String genderStr, String brandName,String parentCategoryName, String categoryName, String colorName, String categoryAndColorUrl) {

        //拉取颜色列表下的信息
        //1.请求性别首页信息
        Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
        Header[] headers = new Header[1];
        headers[0] = header;
        try {
            boolean flag = true;
            int count = 0;
            while(flag){
                count ++;
                if(count == repeatRequestNumber){
                    System.out.println("--- 第"+repeatRequestNumber+"次获取商品列表 分页数 失败"+genderStr+":"+brandName+":"+parentCategoryName+":"+categoryName+":"+colorName+" categoryAndColorUrl:"+categoryAndColorUrl);
                    loggerError.error("--- 第"+repeatRequestNumber+"次获取商品列表 分页数 失败"+genderStr+":"+brandName+":"+parentCategoryName+":"+categoryName+":"+colorName+" categoryAndColorUrl:"+categoryAndColorUrl);
                    break;
                }
                //拼接完整路径
                HttpResponse response = HttpUtils.get(categoryAndColorUrl,headers);
                //请求成功 获取到该类别下有的颜色列表
                if (response.getStatus()==200) {
                    String htmlContent = response.getResponse();
                    Document doc = Jsoup.parse(htmlContent);
                    /**
                     * 获取商品总页  <span class="js-lp-pagination-all" data-tstid="paginationTotal">90</span>
                     */
                    Element paginationElement = doc.select("span[data-tstid=paginationTotal]").first();
                    //定义商品起始页
                    Integer pageNum  = 1;
                    if(paginationElement!=null){
                        String pageNumText = paginationElement.text();
                        if(pageNumText ==null||"".equals(pageNumText)){
                            continue;
                        }
                        pageNum = Integer.parseInt(pageNumText);
                        flag = false;
                        for (int i = 1; i <= pageNum; i++) {
                            String temCategoryAndColorUrl = "";
                            //添加分页参数
                            if(categoryAndColorUrl.contains("?")){
                                temCategoryAndColorUrl = categoryAndColorUrl + "&page="+i;
                            }else{
                                temCategoryAndColorUrl = categoryAndColorUrl + "?page="+i;
                            }
                            System.out.println();
                            System.out.println(" "+genderStr+": "+brandName+": "+categoryName+" :"+colorName+" 共："+pageNum+" 页数据");
                            System.out.println("开始拉取第"+i+"页数据： "+temCategoryAndColorUrl);
                            logger.info(" "+genderStr+": "+brandName+": "+categoryName+" :"+colorName+" 共："+pageNum+" 页数据");
                            logger.info(("开始拉取第"+i+"页数据： "+temCategoryAndColorUrl));
                            fetchProudctPageByBrandAndColorUrl( genderStr,  brandName, parentCategoryName,  categoryName,  colorName,  temCategoryAndColorUrl);
                            //exe.execute(new PageThread(genderStr,  brandName, parentCategoryName, categoryName,  colorName,  temCategoryAndColorUrl)); //开启分页线程
                        }
                    }else{
                        //System.out.println("--获取商品总页数失败--重新请求");
                        //logger.info("--获取商品总页数失败--重新请求");
                        continue;
                    }
                }else{
                    System.out.println("--- 请求商品列表地址失败 categoryAndColorUrl:"+categoryAndColorUrl);
                    loggerError.error("--- 请求商品列表地址失败 categoryAndColorUrl:"+categoryAndColorUrl);
                }
            }
        } catch (Exception e) {
            loggerError.error("请求商品列表地址失败url categoryAndColorUrl ："+categoryAndColorUrl+"获取颜色列表出现异常："+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取品牌 品类 颜色 下的商品列表信息
     * @param genderStr 性别
     * @param brandName 品牌名称
     * @param categoryName 品类名称
     * @param colorName 颜色名称
     * @param categoryAndColorUrl 请求路径 https://www.farfetch.cn/cn/shopping/women/balenciaga/items.aspx?category=135971&colour=0
     */
    private  void fetchProudctPageByBrandAndColorUrl(String genderStr, String brandName,String parentCategoryName, String categoryName, String colorName, String categoryAndColorUrl) {
        //拉取颜色分页列表下的信息
        //1.请求性别首页信息
        Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
        Header[] headers = new Header[1];
        headers[0] = header;
        try {

            boolean flag = true;
            int count = 0;
            while(flag){
                count ++;
                if(count == repeatRequestNumber){
                    System.out.println("--- 第"+repeatRequestNumber+"次获取 商品分页列表数据 失败 "+genderStr+":"+brandName+":"+parentCategoryName+":"+categoryName+":"+colorName+" categoryAndColorUrl:"+categoryAndColorUrl);
                    loggerError.error("--- 第"+repeatRequestNumber+"次获取 商品分页列表数据 失败 "+genderStr+":"+brandName+":"+parentCategoryName+":"+categoryName+":"+colorName+" categoryAndColorUrl:"+categoryAndColorUrl);
                    break;
                }
                //拼接完整路径
                HttpResponse response = HttpUtils.get(categoryAndColorUrl,headers);
                //请求成功 获取到该类别下有的颜色列表
                if (response.getStatus()==200) {
                    String htmlContent = response.getResponse();
                    Document doc = Jsoup.parse(htmlContent);

                    //获取所有该品类下有货颜色 Element 信息
                    //新版本获取方式 一行显示四个商品 <section class="listing-items js-listing-items listing-item-ab" data-tstid="Div_listingItems">
                    Elements productElements = doc.select("section[data-tstid=Div_listingItems]").select("article");

                    if (productElements.size() > 0) {
                        for (Element prouductElement : productElements) {
                            /**
                             * 获取品牌名称
                             *<h5 itemprop="brand" class="listing-item-content-brand force-ltr" data-tstid="Label_ListingDesignerName">Wandler</h5>
                             */
                            Elements brandNameElements = prouductElement.select("h5[data-tstid=Label_ListingDesignerName]");
                            if(brandNameElements!=null&&brandNameElements.size()>0){
                                brandName = brandNameElements.first().text();
                            }
                            /**
                             <a target="_blank" href="/cn/shopping/women/alexander-mcqueen---item-13060405.aspx?storeid=9445&amp;from=1" data-ffref="lp_pic_1_7" itemprop="url" class="listing-item-link no-underline">
                             </a>
                             */
                            Element aElement = prouductElement.select("a").first();
                            String productDetailUrl = aElement.attr("href");
                            productDetailUrl = rootUrl + productDetailUrl;
                            getProductInfoAndExport(genderStr, brandName,parentCategoryName, categoryName, colorName, productDetailUrl);
                        }
                        //请求成功跳出while 循环
                        flag = false;
                    } else {
                        //System.out.println("-- 获取新版本 商品列表数据 分页失败--重新请求");
                        //logger.info("-- 获取新版本 商品列表数据 分页失败--重新请求");
                        continue;
                    }
                    //旧版本便利商品 列表方式
                    //旧版本获取方式 一行显示三个商品
//                    Elements prouductElements2 = doc.select("div[data-test=product-card-list]").select("article");
//                    if(prouductElements2.size()>0){
//                        for (Element prouductElement:prouductElements2) {
//                            /**
//                             <article class="fb8548 _60906b" itemprop="itemListElement" itemscope="" itemtype="http://schema.org/Product" itemid="/cn/shopping/women/3x1-w2-mason--item-12902159.aspx?storeid=9270">
//                             <div class="_095029"><a itemprop="url" href="/cn/shopping/women/3x1-w2-mason--item-12902159.aspx?storeid=9270" aria-label="W2 Mason弹性棉质牛仔短裤" data-ffref="lp_pic_1_3_" target="_blank">
//                             <span class="_69638b"><span class="_774407 "><img itemprop="image" class="_820068 _882e61" alt="W2 Mason弹性棉质牛仔短裤" src="https://cdn-images.farfetch-contents.com/12/90/21/59/12902159_13306758_300.jpg">
//                             </span></span></a></div><a class="_4fb98b _60906b _68f151" href="/cn/shopping/women/3x1-w2-mason--item-12902159.aspx?storeid=9270" data-test="productInformationSection" data-ffref="lp_dn_1_3_" target="_blank"><div class="_8e0ff4
//                             _01aef7 c8299d"><h5 class="_2fa38d" itemprop="brand" data-test="productDesignerName">3X1</h5><p itemprop="name" class="_3461b5" data-test="productDescription">W2 Mason弹性棉质牛仔短裤</p></div><div class="_58aa34 b448d3 fcd899" itemprop="offers" itemscope="" itemtype="http://schema.org/Offer"><span class="b36ba5" data-test="price" itemprop="price" content="3263">¥3,263</span><meta itemprop="priceCurrency" content="CNY"></div><div class="c7f8f7"><h5 class="_181362" data-test="productSizesTitle">可选尺码：</h5><div class="_6beb96" data-test="productSizes"><span>XS</span><span>S</span></div></div></a><span class="_3fe023"><a href="/cn/shopping/women/similarity/items.aspx?productid=12902159" data-test="see-similar-label" data-ffref="lp_ss"><span>查看类似商品</span></a></span><button class="_4593db " aria-label="wishlist button add" data-test="wishlist-button-add">
//                             <span class="fc849e"></span></button>
//                             </article>
//                             */
//                            String productDetailUrl = prouductElement.attr("itemid");
//                            productDetailUrl = rootUrl + productDetailUrl;
//                            getProductInfoAndExport( genderStr,  brandName,  categoryName,  colorName,  productDetailUrl);
//                        }
//                    }
                }else{
                    System.out.println("--- 请求商品列表地址失败 categoryAndColorUrl:"+categoryAndColorUrl);
                    loggerError.error("--- 请求商品列表地址失败 categoryAndColorUrl:"+categoryAndColorUrl);
                }
            }
        } catch (Exception e) {
            loggerError.error("请求商品列表地址失败url categoryAndColorUrl ："+categoryAndColorUrl+"获取颜色列表出现异常："+e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取品类下某一品类下颜色列表信息 遍历
     * @param genderStr 性别
     * @param brandName 品牌名称
     * @param categoryName 品类名称
     * @param colorName 颜色名称
     * @param productDetailUrl 商品详情url
     */
    private  void getProductInfoAndExport(String genderStr, String brandName, String parentCategoryName, String categoryName, String colorName, String productDetailUrl) {
        List<FarfetchProductDTO> products = getProductUrl(genderStr,  brandName, parentCategoryName, categoryName,  colorName,  productDetailUrl);
        if(products ==null){
            System.out.println(" !!获取商品信息失败- "+genderStr+":"+brandName+":"+categoryName+":"+colorName+":"+productDetailUrl+" ");
            loggerError.error(" !!获取商品信息失败- "+genderStr+":"+brandName+":"+categoryName+":"+colorName+":"+productDetailUrl+" ");
            return;
        }
        int size = products.size();
        for (int i = 0; i < size; i++) {
            exportExcel(products.get(i));
        }
        productCount ++;
        System.out.print(productCount+" ");
    }

    /**
     * 根据 商品url 获取商品信息
     * @param genderStr 性别
     * @param brandName 品牌名称
     * @param categoryName 品类名称
     * @param colorName 颜色名称
     * @param productDetailUrl 商品详情url
     * @return
     */
    private  List<FarfetchProductDTO> getProductUrl(String genderStr, String brandName, String parentCategoryName, String categoryName, String colorName, String productDetailUrl) {
        List<FarfetchProductDTO> products = null;
        //商品列表信息 （其中尺码 以及价格 信息不同）
        FarfetchProductDTO product = new FarfetchProductDTO();
        try {
            boolean flag = true;
            int count = 0;
            while(flag) {
                count++;
                if (count == repeatRequestNumber) {
                    System.out.println("--- 第" + repeatRequestNumber + "次获取 商品详情 数据失败 productDetailUrl:" + productDetailUrl);
                    loggerError.error("--- 第" + repeatRequestNumber + "次获取 商品详情 数据失败 productDetailUrl:" + productDetailUrl);
                    break;
                }
                Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
                Header[] headers = new Header[1];
                headers[0] = header;
                HttpResponse response = HttpUtils.get(productDetailUrl, headers);
                if (response.getStatus() == 200) {
                    String htmlContent = response.getResponse();
                    Document doc = Jsoup.parse(htmlContent);
                    product.setUrl(productDetailUrl);
                    product = getProductInfoFromDoc(doc, product);
                    product.setUrl(productDetailUrl);
                    product.setSex(genderStr);
                    product.setBrand(brandName);
                    product.setCategory(categoryName);
                    product.setParentCategory(parentCategoryName);
                    product.setColorName(colorName);
                    //获取商品尺码 库存以及价格信息
                    products = getProductSizeListByDoc(doc, product);
                    flag =false;
                }
            }
        } catch (Exception e) {
            loggerError.error(e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    /**
     * 根据商品详情页doc 获取商品尺码 库存 市场销售价格信息
     * @param doc
     * @param product
     * @return list 每个商品的尺码、价格、库存信息(售罄尺码不显示，获取的产品都是有库存的)
     */
    private List<FarfetchProductDTO> getProductSizeListByDoc(Document doc, FarfetchProductDTO product) {
        List<FarfetchProductDTO> products = new ArrayList<>();
        //判断 该商品是否是 均码 含有该标签 说明是均码 没有则获取尺寸列表信息
        //<span class="_02b25e" data-tstid="oneSizeLabel">均码</span>
        Element sizeSpanElement = doc.select("span[data-tstid=oneSizeLabel]").first();
        if(sizeSpanElement!=null){ //商品为均码 产品销售市场价走默认值
            product.setSize("U");
            product.setQty("1");
            products.add(product);
        }else{ //获取产品尺码列表信息
            //获取默认 市场销售价
            String temItemsaleprice = product.getItemsaleprice();
            Pattern pattern = Pattern.compile("[^0-9]");
            /**
             <select id="dropdown" aria-label="Sizes dropdown" class="a0736c ">
                <option selected="" disabled="" value="0">选择尺码 - Jeans (Waist) - 请注意，以下尺码价格会有些许差异，是由于商品来自不同的Farfetch合作伙伴</option>
                <option value="19">24 WAIST  - 最后1件</option><option value="20">25 WAIST  - 最后1件</option>
                <option value="21">26 WAIST  </option>
                <option value="22">27 WAIST - ¥2,537 - 最后1件</option>
                <option value="23">28 WAIST - ¥2,428 </option>
                <option value="24">29 WAIST - ¥2,428 </option>
                <option value="25">30 WAIST - ¥2,615 - 最后1件</option>
             </select>
             */

            Elements optionElements = doc.select("select#dropdown").select("option");
            for (Element optionElement:optionElements) {
                String value = optionElement.attr("value");
                if("0".equals(value)||"default".equals(value)){  //跳过 尺码选择提示
                    continue;
                }
                String sizeInfoStr = optionElement.text().replace("- 最后1件","").trim();
                if(sizeInfoStr.contains("选择尺码")){
                    continue;
                }
                sizeInfoStr = sizeInfoStr.replace(",",".");
                if(sizeInfoStr.contains("-")){ //走特定价格
                    String[] sizeSplit = sizeInfoStr.split("-");
                    String size = sizeSplit[0].trim();
                    product.setSize(size);
                    Matcher temlistMatcher = pattern.matcher(sizeSplit[1]);
                    String privateItemsaleprice = temlistMatcher.replaceAll("");
                    product.setItemsaleprice(privateItemsaleprice);
                }else{ //不包含 - 走默认价格
                    product.setSize(sizeInfoStr);
                    product.setItemsaleprice(temItemsaleprice);
                }
                // 拷贝商品实体信息 保存
                products.add(FarfetchProductDTO.copyProductData(product));
            }
        }
        return products;
    }

    /**
     * 根据商品详情页 doc 获取商品信息(获取 尺码 库存 价格信息)
     * @param doc
     * @param product
     * @return
     */
    private  FarfetchProductDTO getProductInfoFromDoc(Document doc, FarfetchProductDTO product) {
        //String url;
        String material=""; //材质
        String productName="";
        //String brand="";

        String productCode="";
        String barCode="";
        String descript="";
        String made="";
        //String sex="";
        //String size ="";
        String sizeDesc ="";
        String colorCode ="";
        String itemprice ="";
        String itemdiscountA ="";
        String itemsaleprice ="";
        //String spuNo ="";
        //String category;
        StringBuffer picUrls = new StringBuffer();
        //String qty ="";
        //String qtyDesc ="";
        String season ="2018春夏";
        String otherInfos ="";
        String washMethod = "";

        //模特尺码信息
        String modelMeasurements = "";

        //成分
        try {
            material = doc.select("dl[data-tstid=productComposition]").text().replace(",",".");
            //获取商品名称
            Element productNameElement = doc.select("div#slice-pdp").select("span[itemprop=name]").first();
            //System.out.println(product.getUrl());
            productName = productNameElement.text();

            //获取商品品牌特定编号 即：productCode 、barCode
            // 1.商品货号 data-tstid="designerStyleId"  也是 品牌特定编号    <p data-tstid="designerStyleId">品牌特定编号: 460085QKJ09</p>
            Element barCodeElement  = doc.select("p[data-tstid=designerStyleId]").first();
            if(barCodeElement!=null){
                barCode = barCodeElement.text();
                barCode = barCode.replace("\"","");
                barCode = barCode.split(":")[1].trim();
            }
            productCode = barCode;

            //商品描述 data-tstid="fullDescription"
            Element descriptElement  = doc.select("p[data-tstid=fullDescription]").first();
            if(descriptElement!=null){
                descript = descriptElement.text().replace(",",".");
            }

            //获取商品产地  made = doc.select("p[data-tstid=madeIn]").first().text().replace(",",".");
            Element madeElement  = doc.select("p[data-tstid=madeIn]").first();
            if(madeElement!=null){
                made = madeElement.text().replace(",",".");
            }

            //商品产地 <p data-tstid="madeIn">意大利制造</p>
            Element madeInElement = doc.select("p[data-tstid=madeIn]").first();
            if(madeInElement!=null){
                made = madeInElement.text();
            }

            //商品详情颜色编号  <p data-tstid="designerColor">颜色: 9014</p>
            Element colorCodeElement = doc.select("p[data-tstid=designerColor]").first();
            if(colorCodeElement!=null){
                colorCode = colorCodeElement.text().split(":")[1].trim();
            }

            //产品 尺寸描述
//            Elements sizeElements = doc.select("div[data-tstid=measurementsModule]");
//            if(sizeElements!=null&&sizeElements.size()>0){
//                String sizeTitleDesc = "";
//                String productMeasurementDesc = "";
//                Elements temDescElments = sizeElements.first().select("p");
//                if(temDescElments!=null&&temDescElments.size()>0){
//                    sizeTitleDesc = temDescElments.first().text().replace(",",".");
//                }
//                //商品尺码 data-tstid="productMeasurements" TODO 商品的尺码信息 隐藏在<script> 标签的json 中
//                if(productMeasurementDesc!=null&&!"".equals(productMeasurementDesc)){
//                    sizeDesc = sizeTitleDesc + productMeasurementDesc;
//                }
//            }

            //产品 供价 折扣 销售价 信息
            /**
             <span>
             <del data-tstid="priceInfo-original" class="_3dc7f0 eda00d">¥16,287</del>
             <span class="_348541" aria-hidden="true">|</span>
             <span class="ab951c" data-tstid="priceInfo-discount" dir="LTR">50% 折扣</span>
             <strong class="d9c839 _7af6ec _62e534" data-tstid="priceInfo-onsale">¥8,144</strong>
             <small class="_840e33 eda00d" data-tstid="dutiesInformation" dir="LTR">(<!-- -->价格已包含关税<!-- -->)</small>
             </span>

             没有折扣：
             <span>
             <strong data-tstid="priceInfo-original" class="d9c839 _62e534">¥18,317</strong>
             <small class="_840e33 eda00d" data-tstid="dutiesInformation" dir="LTR">(<!-- -->价格已包含关税<!-- -->)</small>
             </span>
             */
            //Element priceElement = doc.select("div[data-tstid=productOffer]").select("div[data-tstid=priceInfo-priceInfo]").select("span").first();
            Elements priceElement = doc.select("div[data-tstid=productOffer]").select("div[data-tstid=priceInfo-priceInfo]").select("span");
            Pattern pattern = Pattern.compile("[^0-9]");

            //折扣（可能没有）
            Elements discountElements = priceElement.select("span[data-tstid=priceInfo-discount]");
            if(discountElements!=null&&discountElements.size()>0){
                itemdiscountA = discountElements.first().text();
            }
            //市场销售价(可能没有)
            Elements salePriceElements = priceElement.select("strong[data-tstid=priceInfo-onsale]");
            if(salePriceElements!=null&&salePriceElements.size()>0){
                String temSalePrice = salePriceElements.first().text();
                Matcher listMatcher = pattern.matcher(temSalePrice);
                itemsaleprice = listMatcher.replaceAll("");
            }

            //供价 del 标签是 有折扣的 strong 标签的商品没有折扣
            Elements supplyPriceElementDels = priceElement.select("del[data-tstid=priceInfo-original]");
            Elements supplyPriceElementStrs = priceElement.select("strong[data-tstid=priceInfo-original]");
            if(supplyPriceElementDels!=null&&supplyPriceElementDels.size()>0){
                String temSupplyPrice = supplyPriceElementDels.first().text();
                Matcher listMatcher = pattern.matcher(temSupplyPrice);
                itemprice = listMatcher.replaceAll("");
            }else if(supplyPriceElementStrs!=null&&supplyPriceElementStrs.size()>0){ //有折扣的商品 供价和销售价是同一个价格
                String temSupplyPrice = supplyPriceElementStrs.first().text();
                Matcher listMatcher = pattern.matcher(temSupplyPrice);
                itemprice = listMatcher.replaceAll("");
                itemsaleprice = itemprice;
            }


            //洗涤说明
            //doc.select("div[data-tstid=productDetails]").select("div.be0930").select("dl._9b60a7").eq(1).select("dd._13f28d").text();
            Element washTemElement = doc.select("div[data-tstid=productDetails]").select("div.be0930").select("dl._9b60a7").eq(1).first();
            if(washTemElement!=null){
                washMethod = washTemElement.select("dd._13f28d").text();
            }

            //从<script> 中的json 获取 商品的 图片地址信息  window['__initialState_slice-pdp__'] = {"config":{
            Elements scripts = doc.select("script");
            int j = 0;
            for(Element script : scripts)
                if (script.html().contains("window['__initialState_slice-pdp__']")) { //注意这里一定是html(), 而不是text()
                    // 处理商品图片信息
                    String str = script.html().replace(";", ""); //这里是为了解决 无法多行匹配的问题
                    String jsonStr = str.replace("window['__initialState_slice-pdp__'] =", "").trim();
                    JsonElement je = new JsonParser().parse(jsonStr);
                    JsonObject asJsonObject = je.getAsJsonObject();
                    JsonArray imageArray = asJsonObject.get("productViewModel").getAsJsonObject().get("images").getAsJsonObject().get("main").getAsJsonArray();
                    for (int i = 0; i < imageArray.size(); i++) {
                        JsonElement jsonElement = imageArray.get(i);
                        JsonElement imgUrl = jsonElement.getAsJsonObject().get("large");
                        if (i == 0) {
                            picUrls.append(imgUrl);
                        } else {
                            picUrls.append("|").append(imgUrl);
                        }
                        j++;
                    }

                    //处理商品尺码描述信息
                    /**
                     "measurements":{
                     "defaultMeasurement":0,
                     "defaultSize":30,
                     "available":Array[2],
                     "extraMeasurements":Array[0],
                     "sizes":Object{...},
                     "sizeDescription":{"30":"8.5"},
                     "modelMeasurements":Object{...},
                     "modelIsWearing":null,
                     "friendlyScaleName":"UK",
                     "isOneSize":false,
                     "category":"鞋履",
                     "fittingInformation":null,
                     "modelHeight":null
                     },
                     */
                    JsonElement jsonElement = asJsonObject.get("productViewModel").getAsJsonObject().get("measurements");
                    if (jsonElement != null&&!"null".equals(jsonElement.toString())) {
                        JsonObject measurementsObject = jsonElement.getAsJsonObject();
                        String defaultSize = measurementsObject.get("defaultSize").toString();
                        if (!"0".equals(defaultSize)) {
                            StringBuffer sizeBuffer = new StringBuffer();
                            String friendlyScaleName = measurementsObject.get("friendlyScaleName").toString();
                            String sizeDescTitle = measurementsObject.get("sizeDescription").getAsJsonObject().get(defaultSize).toString();
                            sizeBuffer.append(sizeDescTitle+" ").append(friendlyScaleName).append("尺寸信息如下：");

                            JsonObject sizes = measurementsObject.get("sizes").getAsJsonObject().get(defaultSize).getAsJsonObject();
                            Set<Map.Entry<String, JsonElement>> entries = sizes.entrySet();
                            for (Map.Entry<String, JsonElement> size : entries) {
                                String sizeName = size.getKey();
                                sizeBuffer.append(sizeName);
                                JsonArray asJsonArray = size.getValue().getAsJsonArray();
                                int two = asJsonArray.size();
                                for (int i = 0; i < two; i++) {
                                    String sizeValue = asJsonArray.get(i).toString();
                                    if (sizeValue.contains("厘米")) {
                                        sizeBuffer.append(":").append(sizeValue);
                                    }
                                    if (sizeValue.contains("英寸")) {
                                        sizeBuffer.append("|").append(sizeValue);
                                    }
                                }
                                sizeBuffer.append(";");
                            }
                            sizeDesc = sizeBuffer.toString().replace("\"", "").replace(",",".");

                        }else{
                            //System.out.println("没有商品尺码信息");
                        }
                    } else {  //没有商品尺码信息
                        //System.out.println("没有商品测量 measurements 信息");
                    }

                    // 处理商品 品牌特点编号 颜色特定编号 洗涤说明信息
                    if(barCode==null||"".equals(barCode)){
                        JsonObject designerDetails = asJsonObject.get("productViewModel").getAsJsonObject().get("designerDetails").getAsJsonObject();
                        // 品牌特定编号
                        barCode = designerDetails.get("designerStyleId").toString().replace("\"", "");
                        productCode = barCode;
                        //颜色
                        colorCode = designerDetails.get("designerColour").toString().replace("\"", "");
                    }

                    //处理洗涤说明信息
                    if(washMethod==null||"".equals(washMethod)){
                        JsonArray asJsonArray = asJsonObject.get("productViewModel").getAsJsonObject().get("care").getAsJsonArray();
                        int size = asJsonArray.size();
                        for (int i = 0; i <size ; i++) {
                            washMethod += asJsonArray.get(i).getAsJsonObject().get("value").toString().replace("\"", "");
                        }
                    }
                }



            //模特资料 data-tstid="modelMeasurements"
            Element modelMeasurementsElement = doc.select("div[data-tstid=modelMeasurements]").select("table").first();
            if(modelMeasurementsElement!=null){
                modelMeasurements = modelMeasurementsElement.text();
            }

            /**
             <p class="_41db0e d927f9" data-tstid="merchandiseTagDetailsTab">新季</p>
             */
            Elements seasonElement = doc.select("p[data-tstid=merchandiseTagDetailsTab]");
            if(seasonElement!=null){
                String seasonStr = seasonElement.text();
                if(!"".equals(seasonStr)){
                    season = seasonElement.text();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        product.setMaterial(material.replace(",","."));
        product.setProductName(productName);
        product.setProductCode(productCode);
        product.setBarCode(barCode);
        product.setDescript(descript);
        product.setMade(made);
        product.setSizeDesc(sizeDesc);
        product.setColorCode(colorCode);
        product.setItemprice(itemprice);
        product.setItemdiscountA(itemdiscountA);
        product.setItemsaleprice(itemsaleprice);
        product.setPicUrls(picUrls.toString().replace("\"",""));
        product.setSeason(season);
        product.setOtherInfos(otherInfos);
        product.setModelMeasurements(modelMeasurements);
        product.setWashMethod(washMethod);
        return product;
    }

	/**
	 * 导出单个商品信息到csv 文件（追加）
	 * @param dto 商品信息DTO
	 */
	private  void exportExcel(FarfetchProductDTO dto){
		//继续追加
		StringBuffer buffer  = new StringBuffer();
		try {

            buffer.append(dto.getSex()).append(splitSign);
            buffer.append(dto.getBrand()).append(splitSign);
            buffer.append(dto.getParentCategory()).append(splitSign);

            /**
             * 儿童品类生成 加上父品类名称 以区分男女
             */
            String sex = dto.getSex();
            if("kids".equals(sex)){
                buffer.append(dto.getParentCategory()).append(" "+dto.getCategory()).append(splitSign);
            }else{
                buffer.append(dto.getCategory()).append(splitSign);
            }

            String barCode = dto.getBarCode()+" "+dto.getColorCode();
            buffer.append(barCode).append(splitSign);
            buffer.append(barCode).append(splitSign);
            buffer.append(dto.getSeason()).append(splitSign);

            buffer.append(dto.getMaterial()).append(splitSign);
            buffer.append(dto.getColorName()).append(splitSign);

            buffer.append(dto.getSize()).append(splitSign);
            buffer.append(dto.getProductName()).append(splitSign);

            buffer.append(dto.getItemprice()).append(splitSign);
            buffer.append(dto.getItemsaleprice()).append(splitSign);

            buffer.append("0").append(splitSign);
            buffer.append("0").append(splitSign);


            buffer.append(dto.getMade()).append(splitSign);
            buffer.append(dto.getDescript()).append(splitSign);

            //buffer.append(dto.getWashMethod()).append(splitSign);
            //buffer.append(dto.getModelMeasurements()).append(splitSign);

            buffer.append(dto.getPicUrls()).append(splitSign);
            buffer.append(dto.getUrl()).append(splitSign);

            buffer.append(dto.getSizeDesc()).append(splitSign);
            buffer.append("").append(splitSign);
            buffer.append("").append(splitSign);
            buffer.append("").append(splitSign);
            buffer.append("\r\n");
			out.write(buffer.toString());
			out.flush();
		} catch (Exception e) {
		}
	}


    public static void main(String[] args) throws Exception {
	    new FarfetchFetchProductImpl().
        fetchBrandHomePageByBrand("women","ag jeans","https://www.farfetch.cn/cn/shopping/women/ag-jeans/items.aspx");
    }
}
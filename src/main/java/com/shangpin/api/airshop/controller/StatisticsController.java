package com.shangpin.api.airshop.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
//import org.yaml.snakeyaml.reader.StreamReader;

import com.itextpdf.text.pdf.BaseFont;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.dto.DaySalesAmounts;
import com.shangpin.api.airshop.dto.SalesDay;
import com.shangpin.api.airshop.dto.SalesMonth;
import com.shangpin.api.airshop.dto.SalesMonthBrand;
import com.shangpin.api.airshop.dto.SalesMonthCategory;
import com.shangpin.api.airshop.dto.SalesMonthPerformance;
import com.shangpin.api.airshop.dto.SalesMonthWeek;
import com.shangpin.api.airshop.dto.UserInfo;
import com.shangpin.api.airshop.dto.base.ResponseContentList;
import com.shangpin.api.airshop.dto.base.ResponseContentOne;
import com.shangpin.api.airshop.service.base.BaseService;
import com.shangpin.api.airshop.supplier.service.ProductService;
import com.shangpin.api.airshop.util.Constants;
import com.shangpin.common.utils.DateUtil;

/**
 * 返货
 * Created by ZRS on 2016/1/14.
 */
@RestController
@RequestMapping("/sale")
@SessionAttributes(Constants.SESSION_USER)
public class StatisticsController {

    @Autowired
    BaseService baseService;
    @Autowired
    ProductService productService;

    /*@RequestMapping(value="/gf")
    private String gf(HttpServletRequest request) throws Exception{
    	String aa=getClass().getClassLoader().getResource("pdfTemplate/SIMSUN.TTC").getFile().substring(1);
    	return aa;
    	
    	InputStream inStream =getClass().getClassLoader().getResourceAsStream("pdfTemplate/monthlyReportTemplate.ftl");
    	ByteArrayOutputStream outSteam = new ByteArrayOutputStream(); 
    	byte[] buffer = new byte[1024];  
    	int len = -1;  
    	 while ((len = inStream.read(buffer)) != -1) {  
             outSteam.write(buffer, 0, len);  
         } 
    	 outSteam.close();  
    	 inStream.close(); 
        String remess = new String(outSteam.toByteArray());  
    	return remess;
    }*/
    
    
    /**根据文件路径获取文件二进制流
     * @param path
     * @return
     * @throws Exception
     */
    private byte[] getFileBytes(String path)throws Exception{
    	InputStream inStream =getClass().getClassLoader().getResourceAsStream(path);
    	ByteArrayOutputStream outSteam = new ByteArrayOutputStream(); 
    	byte[] buffer = new byte[1024];  
    	int len = -1;  
    	 while ((len = inStream.read(buffer)) != -1) {  
             outSteam.write(buffer, 0, len);  
         } 
    	 outSteam.close();  
    	 inStream.close(); 
    	 return buffer;
    }
    
    private String getFileByte(String path)throws Exception{
    	InputStream inStream =getClass().getClassLoader().getResourceAsStream(path);
    	InputStreamReader reader = new InputStreamReader(inStream);
    	BufferedReader bufferedReader = new BufferedReader(reader);
    	StringBuffer sb = new StringBuffer();
    	String lineTxt = null;
        while((lineTxt = bufferedReader.readLine()) != null){
        	sb.append(lineTxt);
        }
        reader.close();
        return sb.toString();
    }
    
    /**导出pdf文件
     * @param pdfContent
     * @param pdfCreateDate
     * @param userInfo
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/getSupplierReportPDF",method={RequestMethod.GET,RequestMethod.POST})
    private void getSupplierReportPDF(@RequestParam(name="pdfContent",required=true) String pdfContent
    		,@RequestParam(name="pdfCreateDate",required=true) String pdfCreateDate
    		,@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo
    		,HttpServletResponse response) throws Exception{
    	response.setContentType("application/x-download");
    	OutputStream out = response.getOutputStream();
    	
    	//读取pdf模板文件
    	String pdfTemplateContent=getFileByte("pdfTemplate/monthlyReportTemplate.ftl");
    	pdfTemplateContent=pdfTemplateContent.format(pdfTemplateContent, pdfContent).replace("Export", "");
    	
    	ITextRenderer renderer = new ITextRenderer();
    	renderer.setDocumentFromString(pdfTemplateContent);//读取模板文件
    	ITextFontResolver fontResolver = renderer.getFontResolver();  
    	fontResolver.addFont("/data/www/8082/SIMSUN.TTC", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
    	//fontResolver.addFont(getClass().getClassLoader().getResource("pdfTemplate/SIMSUN.TTC").getPath().substring(5), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
    	renderer.layout();
    	renderer.createPDF(out);
    	
    	
    	//设置文档保存的文件名 
    	String fileName ="Monthly Report"+(pdfCreateDate==null?"":"_"+pdfCreateDate);
        response.addHeader("Content-disposition", "attachment;filename="+  new String(fileName.getBytes("gb2312"), "ISO8859-1") +".pdf"); 
       //设置类型 
        //response.setContentType("application/pdf");
        response.setContentType("application/x-download");
    	out.close();
    	
    }
    
    private DecimalFormat df = new DecimalFormat("##0.00");

    @RequestMapping(value = "/day")
    public ResponseContentOne<SalesDay> getOrderList(
            @RequestParam(value = "start", required = false) String start,
            @RequestParam(value = "end", required = false) String end,
            @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) throws UnsupportedEncodingException {

        //做些操作 DT
        if (!StringUtils.isEmpty(start) && StringUtils.isEmpty(end)) {
            end = "01/01/2100";
        }
        if (!StringUtils.isEmpty(end) && StringUtils.isEmpty(start)) {
            start = "01/01/1900";
        }

        if(StringUtils.isEmpty(start) && StringUtils.isEmpty(end)){
            return ResponseContentOne.errorParam();
        }

        //组装参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("DateStart", start);
        paramMap.put("DateEnd", end);
        paramMap.put("SopUserNo", userInfo.getSopUserNo());

        //请求数据
        ResponseContentOne<SalesDay> responseContentOne = baseService.requestAPI4One(ApiServiceUrlConfig.getSalesByDayUrl(), paramMap, SalesDay.class);

        if ("0".equals(responseContentOne.getCode())) {
            SalesDay salesDay = responseContentOne.getContent();

            //计算比率
            double productQty = 0d;
            double outStockQty = 0d;
            double cancelQty = 0d;
            double deliveryQty = 0d;
            if (!StringUtils.isEmpty(salesDay.getProductQty())) {
                productQty = Double.valueOf(salesDay.getProductQty());
            }
            if (productQty != 0d) {
                if (!StringUtils.isEmpty(salesDay.getOutStockQty())) {
                    outStockQty = Double.valueOf(salesDay.getOutStockQty());
                }
                if (!StringUtils.isEmpty(salesDay.getCancelQty())) {
                    cancelQty = Double.valueOf(salesDay.getCancelQty());
                }
                if (!StringUtils.isEmpty(salesDay.getDeliveryQty())) {
                    deliveryQty = Double.valueOf(salesDay.getDeliveryQty());
                }

                salesDay.setOutStockQtyRate(df.format(outStockQty / productQty * 100d) + "%");
                salesDay.setCancelQtyRate(df.format(cancelQty / productQty * 100d) + "%");
                salesDay.setDeliveryQtyRate(df.format(deliveryQty / productQty * 100d) + "%");
            }

            //计算横纵标
            List<DaySalesAmounts> list = salesDay.getDaySalesAmounts();
            List<String> X = new ArrayList<>();
            List<String> Y = new ArrayList<>();
            String maxValue = null;
            for (DaySalesAmounts daySalesAmounts : list) {
                Date date = DateUtil.stringToDate(daySalesAmounts.getStatisticDate(),"yyyy-MM-dd");
                X.add(DateUtil.date2String(date,"MM/dd/yyyy"));
                if(maxValue==null){
                	maxValue = daySalesAmounts.getTotalDaySaleAmount();
                }
                String temp = daySalesAmounts.getTotalDaySaleAmount();
                if(temp!=null&&maxValue!=null&&!maxValue.isEmpty()&&!temp.isEmpty()){
                	 if(Double.parseDouble(temp)>Double.parseDouble(maxValue)){
                		 maxValue = temp;
                     }
                }
                Y.add(temp);
            }
            salesDay.setX(X);
            salesDay.setY(Y);
	        if(maxValue!=null&&!maxValue.isEmpty()){
	        	double maxVal =  Double.parseDouble(maxValue);
	        	if(maxVal==0){
	        		salesDay.setScaleStepWidth("100");
	                salesDay.setScaleSteps("10");
	        	}else{
	        		String index = ((int)maxVal/10)+"";
	        		int m = Integer.parseInt(index.substring(0,1))+1;
	        		int leng = index.length()-1;
	        		int mmm = (int) (Math.pow(10,leng) * m); 
	        			salesDay.setScaleStepWidth(mmm+"");
		                salesDay.setScaleSteps("10");
	        		
	        	}
	        }else{
	        	salesDay.setScaleStepWidth("100");
                salesDay.setScaleSteps("10");
	        }
            
        }
        return responseContentOne;
    }

    @RequestMapping(value = "/month")
    public ResponseContentOne<SalesMonth> getOrderItem(@RequestParam("month") String month,
           @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) throws UnsupportedEncodingException {

        String[] split = month.split("/");
        if(split.length < 2){
            return ResponseContentOne.errorParam();
        }

        //组装参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("DateMonth", split[1]+ split[0]);
        paramMap.put("SopUserNo", userInfo.getSopUserNo());

        //请求数据
        ResponseContentOne<SalesMonth> responseContentOne = baseService.requestAPI4One(ApiServiceUrlConfig.getSalesByMonthUrl(), paramMap, SalesMonth.class);

        ResponseContentList<SalesMonthCategory> salesMonthCategoryList = baseService.requestAPI(ApiServiceUrlConfig.getSalesByMonthCategoryUrl(), paramMap, SalesMonthCategory.class);
        ResponseContentOne<SalesMonthPerformance> salesMonthPerformanceOne = baseService.requestAPI4One(ApiServiceUrlConfig.getSalesByMonthPerformanceUrl(), paramMap, SalesMonthPerformance.class);
        ResponseContentList<SalesMonthWeek> salesMonthWeekList = baseService.requestAPI(ApiServiceUrlConfig.getSalesByMonthWeekUrl(), paramMap, SalesMonthWeek.class);

        paramMap.put("Top", 15);
        paramMap.put("isAllBrand", 1);
        ResponseContentList<SalesMonthBrand> salesMonthBrandList = baseService.requestAPI(ApiServiceUrlConfig.getSalesByMonthBrandUrl(), paramMap, SalesMonthBrand.class);

        //上月
        String yesterMonth = DateUtil.date2String(DateUtil.addMonth(DateUtil.stringToDate(month,"MM/yyyy"),-1),"yyyyMM");
        paramMap.put("DateMonth", yesterMonth);
        ResponseContentOne<SalesMonth> yesterResponse = baseService.requestAPI4One(ApiServiceUrlConfig.getSalesByMonthUrl(), paramMap, SalesMonth.class);

        //组装参数
        if("0".equals(responseContentOne.getCode())){
            SalesMonth salesMonth = responseContentOne.getContent();

            if("0".equals(salesMonthCategoryList.getCode())){
                //品类追加汇总
                List<SalesMonthCategory> content = salesMonthCategoryList.getContent();
                double salesAmounts = 0;
                int sealesQtyts= 0;
                double buyAmounts=0;
                int salesItems =0;
                //汇总
                for (SalesMonthCategory salesMonthCategory : content) {
                    double salesAount = Double.valueOf(salesMonthCategory.getSalesAmount());
                    int sealesQty = Integer.valueOf(salesMonthCategory.getSalesQty());
                    double buyAmount = Double.valueOf(salesMonthCategory.getBuyAmount());
                    int salesItem = Integer.valueOf(salesMonthCategory.getSalesItems());
                    salesAmounts += salesAount;
                    sealesQtyts += sealesQty;
                    buyAmounts += buyAmount;
                    salesItems += salesItem;
                    
                    double price =0;
                    if(sealesQty!=0)
                    {
                    	price=buyAmount/sealesQty;
                    }
                    salesMonthCategory.setPrice(df.format(price));
                }
                SalesMonthCategory salesMonthCategory = new SalesMonthCategory();
                salesMonthCategory.setSecondaryCategory("<b>Total</b>");
                salesMonthCategory.setSalesAmount(df.format(salesAmounts));
                salesMonthCategory.setSalesQty(sealesQtyts+"");
                salesMonthCategory.setBuyAmount(df.format(buyAmounts));
                salesMonthCategory.setSalesItems(salesItems+"");
                content.add(salesMonthCategory);
                salesMonth.setSalesMonthCategoryList(content);

                //再计算
                for (SalesMonthCategory category : content) {
                    double salesAmount = Double.valueOf(category.getSalesAmount());
                    double amountRate=0;
                    if(salesAmounts!=0)
                    {
                    	amountRate = salesAmount / salesAmounts;
                    }
                    category.setSalesRate(df.format(amountRate*100d)+"%");
                }
                //总额单价
                double price =0;
                if(sealesQtyts!=0)
                {
                	price=buyAmounts/sealesQtyts;
                }
                salesMonthCategory.setPrice(df.format(price));

            }
            if("0".equals(salesMonthPerformanceOne.getCode())){

                //转换级别
                SalesMonthPerformance salesMonthPerformance = salesMonthPerformanceOne.getContent();
                salesMonthPerformance.setFulfilmentRate(getRank(salesMonthPerformance.getFulfilmentRateRanking(),salesMonthPerformance.getTotalFulfilmentRateRanking()));
                salesMonthPerformance.setSalesRate(getRank(salesMonthPerformance.getSalesRanking(),salesMonthPerformance.getTotalSalesRanking()));
                salesMonthPerformance.setValidItemsRate(getRank(salesMonthPerformance.getValidItemsRanking(),salesMonthPerformance.getTotalValidItemsRanking()));
                salesMonthPerformance.setReturnRate(getRank(salesMonthPerformance.getReturnRateRanking(),salesMonthPerformance.getTotalReturnRateRanking()));

                salesMonth.setSalesMonthPerformance(salesMonthPerformance);
            }
            if("0".equals(salesMonthWeekList.getCode())){
                salesMonth.setSalesMonthWeekList(salesMonthWeekList.getContent());
            }
            if("0".equals(salesMonthBrandList.getCode())){
                salesMonth.setSalesMonthBrand(salesMonthBrandList.getContent());
            }
            String yesterTotalRevenue = "";
            salesMonth.setYesterTotalRevenue("");
            salesMonth.setRevenueRate("");
            if ("0".equals(yesterResponse.getCode()) && yesterResponse.getContent() != null) {
                yesterTotalRevenue = yesterResponse.getContent().getTotalRevenue();
                salesMonth.setYesterTotalRevenue(yesterTotalRevenue);
            }
            String totalRevenue = salesMonth.getTotalRevenue();
            double rate = 0d;

            if(!StringUtils.isEmpty(yesterTotalRevenue) && !StringUtils.isEmpty(totalRevenue)){
            	if(Double.valueOf(yesterTotalRevenue)!=0)
            	{
            		 rate = (Double.valueOf(totalRevenue) - Double.valueOf(yesterTotalRevenue))/ Double.valueOf(yesterTotalRevenue) ;
            	}
            }
           salesMonth.setRevenueRate(df.format(rate*100d)+"%");
        }

        return responseContentOne;
    }

    @RequestMapping(value = "/month/brand")
    public ResponseContentList<SalesMonthBrand> getOrderItem(@RequestParam("month") String month,@RequestParam(value = "top",required = false) Integer top,
           @ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) throws UnsupportedEncodingException {

        //默认15条
        top = top == null?15:top;

        String[] split = month.split("/");
        month = split[1]+ split[0];

        //组装参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("SopUserNo", userInfo.getSopUserNo());
        paramMap.put("DateMonth", month);
        paramMap.put("Top", top);
        paramMap.put("isAllBrand", 2);
        ResponseContentList<SalesMonthBrand> responseContentList = baseService.requestAPI(ApiServiceUrlConfig.getSalesByMonthBrandUrl(), paramMap, SalesMonthBrand.class);

        //DT
        if("0".equals(responseContentList.getCode())){
            int i= 1;
            for (SalesMonthBrand salesMonthBrand : responseContentList.getContent()) {
                salesMonthBrand.setSalesQty("" + i++);
                salesMonthBrand.setSalesAmount("");
            }
        }

        return responseContentList;

    }

    //粗略计算
    private String getRank(String a, String b){
        if(StringUtils.isEmpty(b) || "0".equals(b)){
            return "C";
        }
        double aa = Double.valueOf(a);
        double bb = Double.valueOf(b);

        double i = aa/ bb;
        if(i <= 0.25){
            return "A";
        }
        if(i <= 0.75){
            return "B";
        }
        return "C";

    }

}

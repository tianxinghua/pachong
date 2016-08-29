package com.shangpin.iog.webcontainer.front.controller;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import net.sf.json.JSONObject;

import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.json.JsonUtil;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.OrderDetailDTO;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.ProductSearchDTO;
import com.shangpin.iog.dto.SpecialSkuDTO;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.service.OrderDetailService;
import com.shangpin.iog.service.OrderService;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.service.SpecialSkuService;
import com.shangpin.iog.service.SupplierService;
import com.shangpin.iog.webcontainer.front.strategy.NameGenContext;
import com.shangpin.iog.webcontainer.front.util.DowmImage;
import com.shangpin.iog.webcontainer.front.util.NewSavePic;
import com.shangpin.iog.webcontainer.front.util.ReadExcel;
import com.shangpin.iog.webcontainer.front.util.queue.PicQueue;


/**
 */
@Controller
@RequestMapping("/download")
public class FileDownloadController {
	private static ResourceBundle bdl = null;
	private static String pcode = null;
	private static String pcodecolor = null;
	private static String skuwithoutsize = null;
	private static String efashion = null;
	private static String pavin = null;
	private static String downloadpath = null;
	private static String pictmpdownloadpath = null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		pcode = bdl.getString("pcode");
		pcodecolor = bdl.getString("pcodecolor");
		skuwithoutsize = bdl.getString("skuwithoutsize");
		efashion = bdl.getString("efashion");
		pavin = bdl.getString("pavin");
		downloadpath = bdl.getString("downloadpath");
		pictmpdownloadpath = bdl.getString("pictmpdownloadpath");
	}
	private Logger log = LoggerFactory.getLogger(FileDownloadController.class) ;
	@Autowired
	ProductFetchService pfs;
    @Autowired
    ProductSearchService productService;

    @Autowired
    SpecialSkuService specialSkuService;
    
    @Autowired
    SupplierService supplierService;
    
    @Autowired
    OrderService orderService;
    
    @Autowired
    OrderDetailService orderDetailService;
    
    @RequestMapping(value = "view")
    public ModelAndView viewPage() throws Exception {
        ModelAndView mv = new ModelAndView("iog");
        List<SupplierDTO> supplierDTOList = supplierService.findAllWithAvailable();
        List<String> bus = productService.findAllBus();

        mv.addObject("supplierDTOList",supplierDTOList);
        mv.addObject("BUs", bus);
        return mv;
    }


    @RequestMapping(value = "code")
    public void setCode(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String code =   request.getParameter("code");
        System.out.println("code = " +code );
        log.error("code =" + code);

        OutTimeConfig timeConfig = OutTimeConfig.defaultOutTimeConfig();
        timeConfig.confRequestOutTime(1000*60);
        timeConfig.confSocketOutTime(1000*60);
        
        //An access token is returned as a JSON response along with the time (in seconds) till expiration.
        String application_id = "qwmmx12wu7ug39a97uter3dz29jbij3j";
        String shared_secret = "TqMSdN6-LkCFA0n7g7DWuQ";
        Map<String,String> map = new HashMap<>();
        map.put("grant_type","authorization_code");
        map.put("code",code);
        map.put("redirect_uri","https://49.213.13.167:8443/iog/download/code");
        String kk = HttpUtil45.postAuth("https://api.channeladvisor.com/oauth2/token", map, timeConfig,application_id,shared_secret);
        System.out.println("kk = "  + kk);
        log.error(kk);
        
        PrintWriter out = response.getWriter();
        out.println("GET ACCESS TOKEN SUCCESSFUL");
        out.println("code=="+code);
        out.println("token=="+kk);
    }
    

    @RequestMapping(value = "csv")
    public void downloadCsv(
                         HttpServletResponse response,
                         String queryJson) throws Exception {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        StringBuffer productBuffer =null;
        try {

        	response.reset();

            response.setContentType("text/csv;charset=gb2312");

            ProductSearchDTO productSearchDTO = (ProductSearchDTO) JsonUtil.getObject4JsonString(queryJson, ProductSearchDTO.class);

            if(null==productSearchDTO) productSearchDTO = new ProductSearchDTO();

            String supplier = null;
            if(!StringUtils.isEmpty(productSearchDTO.getSupplier()) && !productSearchDTO.getSupplier().equals("-1")){
            	supplier = productSearchDTO.getSupplier();
            }
            Date startDate  =null;
            if(!StringUtils.isEmpty(productSearchDTO.getStartDate())){
                startDate =  DateTimeUtil.convertFormat(productSearchDTO.getStartDate(),"yyyy-MM-dd HH:mm:ss");
            }

            Date endDate = null;
            if(!StringUtils.isEmpty(productSearchDTO.getEndDate())){
                endDate= DateTimeUtil.convertFormat(productSearchDTO.getEndDate(), "yyyy-MM-dd HH:mm:ss");
            }
            
            Integer pageIndex = -1;
            if(null !=productSearchDTO.getPageIndex()){
            	pageIndex = productSearchDTO.getPageIndex();
            }
            
            Integer pageSize = -1;
            if(null != productSearchDTO.getPageSize()){
            	pageSize = productSearchDTO.getPageSize();
            }
            
            if (productSearchDTO.getFlag().equals("same")) {//导出功能
            	productBuffer =productService.exportProduct(supplier,startDate,endDate,productSearchDTO.getPageIndex(),productSearchDTO.getPageSize(),productSearchDTO.getFlag());
            	response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode(null==productSearchDTO.getSupplier()?"All":productSearchDTO.getSupplierName()+ "_product" + System.currentTimeMillis() + ".csv", "UTF-8"));
			}else if(productSearchDTO.getFlag().equals("order")){
				
				productBuffer =orderService.exportOrder(supplier,startDate,endDate,pageIndex,pageSize,productSearchDTO.getFlag());
				response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode(null==productSearchDTO.getSupplier()?"All":productSearchDTO.getSupplierName()+ "_order" + System.currentTimeMillis() + ".csv", "UTF-8"));
				
//			}else if(productSearchDTO.getFlag().equals("ep_regular")){//按条件导出
//				productBuffer =productService.exportProduct(supplier,startDate,endDate,productSearchDTO.getPageIndex(),productSearchDTO.getPageSize(),productSearchDTO.getFlag());
//            	response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode(null==productSearchDTO.getSupplier()?"All":productSearchDTO.getSupplierName()+ "_product" + System.currentTimeMillis() + ".csv", "UTF-8"));

			
			}else if(productSearchDTO.getFlag().equals("ep_rule")){//按条件导出
				productBuffer =productService.exportProductByEpRule(supplier,startDate,endDate,productSearchDTO.getPageIndex(),productSearchDTO.getPageSize());
            	response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode(null==productSearchDTO.getSupplier()?"All":productSearchDTO.getSupplierName()+ "_product" + System.currentTimeMillis() + ".csv", "UTF-8"));
			
			}
			else if(productSearchDTO.getFlag().equals("report")){//报表导出
				productBuffer =productService.exportReportProduct(supplier,startDate,endDate,productSearchDTO.getPageIndex(),productSearchDTO.getPageSize());
            	response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode(null==productSearchDTO.getSupplier()?"All":productSearchDTO.getSupplierName()+ "_product" + System.currentTimeMillis() + ".csv", "UTF-8"));
			
			}
			else{//价格变化导出
				productBuffer =productService.exportDiffProduct(productSearchDTO.getSupplier(),startDate,endDate,productSearchDTO.getPageIndex(),productSearchDTO.getPageSize(),productSearchDTO.getFlag());
				response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode(null==productSearchDTO.getSupplier()?"All":productSearchDTO.getSupplierName()+ "_product" + System.currentTimeMillis() + ".csv", "UTF-8"));
			}

            

            

//            System.out.print("kk ----------------- " + productBuffer.toString());
            in = new BufferedInputStream(new ByteArrayInputStream(productBuffer.toString().getBytes("gb2312")));

            out = new BufferedOutputStream(response.getOutputStream());
            byte[] data = new byte[1024];
            int len = 0;
            while (-1 != (len=in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }

//            response.getOutputStream().write(productBuffer.toString().getBytes("gb2312"));
//            response.getOutputStream().flush();
//            response.getOutputStream().close();
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }



    }
    
    
    @RequestMapping(value = "orders")
    @ResponseBody
    public ModelAndView queryOrders(HttpServletRequest request,
                         HttpServletResponse response,
                         String queryJson){
    	ModelAndView modelAndView = new ModelAndView();
//    	Map<String, String> nameMap = new HashMap<String, String>();
//    	nameMap.put("placed", "下订单成功");
//    	nameMap.put("payed", "支付");
//    	nameMap.put("cancelled", "取消成功");
//    	nameMap.put("confirmed", "支付成功");
//    	nameMap.put("nohandle", "超时不处理");
//    	nameMap.put("waitplaced", "待下订单");
//    	nameMap.put("waitcancel", "待取消");
//    	nameMap.put("refunded", "退款成功");
//    	nameMap.put("waitrefund", "待退款");
//    	nameMap.put("purexpsuc", "采购异常Suc");
//    	nameMap.put("purexperr", "采购异常Err");
//    	nameMap.put("shipped", "shipped");
//    	nameMap.put("should purExp", "应该采购异常");
//    	
//    	List<OrderDTO> orderList = null;
//    	try{
//    		String page = request.getParameter("page");
//    		String rows = request.getParameter("rows");
//    		System.out.println(page+rows);
//    		ProductSearchDTO productSearchDTO = (ProductSearchDTO) JsonUtil.getObject4JsonString(queryJson, ProductSearchDTO.class);
//            if(null==productSearchDTO) productSearchDTO = new ProductSearchDTO();
//            String supplier = null;
//            if(!StringUtils.isEmpty(productSearchDTO.getSupplier()) && !productSearchDTO.getSupplier().equals("-1")){
//            	supplier = productSearchDTO.getSupplier();
//            }
//            Date startDate  =null;
//            if(!StringUtils.isEmpty(productSearchDTO.getStartDate())){
//                startDate =  DateTimeUtil.convertFormat(productSearchDTO.getStartDate(),"yyyy-MM-dd HH:mm:ss");
//            }
//            Date endDate = null;
//            if(!StringUtils.isEmpty(productSearchDTO.getEndDate())){
//                endDate= DateTimeUtil.convertFormat(productSearchDTO.getEndDate(), "yyyy-MM-dd HH:mm:ss");
//            }        
//            Integer pageIndex = -1;
//            if(null !=productSearchDTO.getPageIndex()){
//            	pageIndex = productSearchDTO.getPageIndex();
//            }        
//            Integer pageSize = -1;
//            if(null != productSearchDTO.getPageSize()){
//            	pageSize = productSearchDTO.getPageSize();
//            }
//            
//            
//            if(pageIndex != null && pageSize != null && pageIndex != -1 && pageSize != -1){			
//    			orderList = orderService.getOrderBySupplierIdAndTime(supplier, startDate, endDate, pageIndex, pageSize);
//    						
//    		}else{			
//    			orderList = orderService.getOrderBySupplierIdAndTime(supplier, startDate, endDate,1,100);
//    						
//    		}	     
//            for (OrderDTO orderDTO : orderList) {
//            	if(nameMap.containsKey(orderDTO.getStatus().toLowerCase())){
//            		orderDTO.setStatus(nameMap.get(orderDTO.getStatus().toLowerCase()));
//            	}				
//			}
    	
            modelAndView.addObject("supplierId", request.getParameter("supplierId"));
    		modelAndView.setViewName("orders");
//    		
//    	}catch(Exception ex){
//    		log.error(ex.getMessage());
//    		ex.printStackTrace();
//    	}        
//    	
    	modelAndView.setViewName("orders");
		return modelAndView;
    }
    
    
    
    
    @RequestMapping(value = "orderPage")
    @ResponseBody
    public String queryOrdersPage(HttpServletRequest request,
                         HttpServletResponse response){
    	
    	Map<String, String> nameMap = new HashMap<String, String>();
    	nameMap.put("placed", "下订单成功");
    	nameMap.put("payed", "支付");
    	nameMap.put("cancelled", "取消成功");
    	nameMap.put("confirmed", "支付成功");
    	nameMap.put("nohandle", "超时不处理");
    	nameMap.put("waitplaced", "待下订单");
    	nameMap.put("waitcancel", "待取消");
    	nameMap.put("refunded", "退款成功");
    	nameMap.put("waitrefund", "待退款");
    	nameMap.put("purexpsuc", "采购异常Suc");
    	nameMap.put("purexperr", "采购异常Err");
    	nameMap.put("shipped", "shipped");
    	nameMap.put("should purExp", "应该采购异常");
    	ModelAndView modelAndView = new ModelAndView();
    
    	try{
    		String page = request.getParameter("page");
    		String rows = request.getParameter("rows");
    		String supplier = request.getParameter("supplierId");
    		System.out.println(page+rows);
            int pageIndex1 = Integer.parseInt(page);
    		int pageSize1 = Integer.parseInt(rows);
    		List<OrderDetailDTO> orderList = null;
            orderList = orderDetailService.getOrderBySupplierIdAndTime(supplier, null, null, (pageIndex1-1)*pageSize1,pageSize1 );	
            int total = orderDetailService.getOrderTotalBySupplierIdAndTime(supplier, null, null);
            
            for (OrderDetailDTO orderDTO : orderList) {
            	if(nameMap.containsKey(orderDTO.getStatus().toLowerCase())){
            		orderDTO.setStatus(nameMap.get(orderDTO.getStatus().toLowerCase()));
            	}				
			}
            modelAndView.addObject("orderList", orderList);
    		modelAndView.setViewName("orders");
    		 JSONObject result = new JSONObject();  
    	      result.put("rows", orderList);  
             result.put("total",total);
             return result.toString();//这个就是你在ajax成功的时候返回的数据，我在那边进行了一个对象封装
    	}catch(Exception ex){
    		log.error(ex.getMessage());
    		ex.printStackTrace();
    	}        
    	return null;
    }
    
    
    
    @RequestMapping(value = "downLoadPicture")
    public void dowmLoadPic(HttpServletResponse response, String queryJson){
    	ProductSearchDTO productSearchDTO = (ProductSearchDTO) JsonUtil.getObject4JsonString(queryJson, ProductSearchDTO.class);
    	BufferedInputStream in = null;
    	BufferedOutputStream out = null;
    	if(null==productSearchDTO) productSearchDTO = new ProductSearchDTO();

        String supplier = null;
        if(!StringUtils.isEmpty(productSearchDTO.getSupplier()) && !productSearchDTO.getSupplier().equals("-1")){
        	supplier = productSearchDTO.getSupplier();
        }
        Date startDate  =null;
        if(!StringUtils.isEmpty(productSearchDTO.getStartDate())){
            startDate =  DateTimeUtil.convertFormat(productSearchDTO.getStartDate(),"yyyy-MM-dd HH:mm:ss");
        }

        Date endDate = null;
        if(!StringUtils.isEmpty(productSearchDTO.getEndDate())){
            endDate= DateTimeUtil.convertFormat(productSearchDTO.getEndDate(), "yyyy-MM-dd HH:mm:ss");
        }
        
        Integer pageIndex = productSearchDTO.getPageIndex();
        
        Integer pageSize = productSearchDTO.getPageSize();
        Map<String,List<File>> nameMap = null;
        ZipFile zipfile = null;
        try {
        	//要下载的文件列表
        	List<ProductDTO> pList = productService.findPicName(supplier, startDate, endDate, pageIndex, pageSize);
        	
        	NameGenContext context = new NameGenContext(supplier);
        	nameMap = context.operate(pList);
        	for (Entry<String, List<File>> productDTO : nameMap.entrySet()) {
        		log.error(productDTO.getKey());
			}
        	zipfile = new ZipFile(new File(new Date().getTime()+""));
        	ArrayList<File> filesToAdd = new ArrayList<File>();
    		//供应商pic的文件夹
    		File dir = new File(downloadpath+productSearchDTO.getSupplierName()+"/");
    		String key = "";
    		if (dir.isDirectory()) {
    			File[] files = dir.listFiles();
    			for (File file : files) {
    				//TODO  替换filename中的转义字符
    				if (nameMap.containsKey(file.getName().split("_")[0])) {
    					key = file.getName().split("_")[0];
    					if(null==nameMap.get(key)){
    						nameMap.put(key, new ArrayList<File>());
    					}
    					nameMap.get(key).add(file);
					}
    			}
			}
    		//添加map中要下载的文件
    		for (Entry<String, List<File>> entry : nameMap.entrySet()) {
    			if (entry.getValue()!=null&&entry.getValue().size()>0) {
    				filesToAdd.addAll(entry.getValue());
				}
    		}
    		
			ZipParameters parameters = new ZipParameters();  
		    parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		    parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);  
			zipfile.addFiles(filesToAdd, parameters);
			response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode("picture"+new Date().getTime()+".zip", "UTF-8"));

			in = new BufferedInputStream(new FileInputStream(zipfile.getFile()));

            out = new BufferedOutputStream(response.getOutputStream());
            byte[] data = new byte[1048576];
            int len = 0;
            while (-1 != (len=in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				in.close();
				out.close();
				zipfile.getFile().delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    @RequestMapping("uploadFileAndDown")
    public void uploadFileAndDown(@RequestParam(value = "uploadFile", required = false) MultipartFile file, HttpServletRequest request,HttpServletResponse response){
    	BufferedInputStream in = null;
    	BufferedOutputStream out = null;
    	String path = request.getSession().getServletContext().getRealPath("");  
    	
    	String parameter = request.getParameter("threadnum");
    	ThreadPoolExecutor executor = null;
    	if (parameter.equals("")||parameter.contains("-")) {
    		executor = new ThreadPoolExecutor(3, 15, 300, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(6),new ThreadPoolExecutor.CallerRunsPolicy());
		}else{
			executor = new ThreadPoolExecutor(Integer.valueOf(parameter), Integer.valueOf(parameter),0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
		}
    	PicQueue picQueue = new PicQueue();
        NewSavePic newSavePic = new NewSavePic(picQueue,executor);
        
    	String fileName = file.getOriginalFilename();  
        File targetFile = new File(path, fileName);  
        //保存  
        try {  
        	if (!targetFile.exists()) {
        		targetFile.mkdirs(); 
        		targetFile.createNewFile();
			}
            file.transferTo(targetFile);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        
        
        String filePath = newSavePic.saveImg(targetFile,picQueue);
        log.error(targetFile.getName()+"下载路径为+++++++++++++++++++++++++++++++++"+filePath);
    	delay(executor);
    	
    	//重新下载失败的
		 String failUrl = "";
		 String[] split = null;
		 Map<String,Integer> recordMap = new HashMap<String, Integer>();
		 while(executor.getActiveCount()>0||!picQueue.unVisitedUrlsEmpty()){
			 if (picQueue.unVisitedUrlsEmpty()&&executor.getActiveCount()>=0) {
				 log.error("============================================都为空=======================================================");
				try {
	 				Thread.sleep(1000*15);
	 			} catch (InterruptedException e) {
	 				e.printStackTrace();
	 			}
				continue;
			 }
			 failUrl = picQueue.unVisitEdUrlDeQueue();
			 if (recordMap.containsKey(failUrl)) {
				 if (recordMap.get(failUrl)>10) {
					continue;
				 }
				 recordMap.put(failUrl, recordMap.get(failUrl)+1);
			 }else{
				 recordMap.put(failUrl, 1);
			 }
			 split = failUrl.split(";");
			 executor.execute(new DowmImage(split[0],split[2],split[1],picQueue));
		 }
		 delay(executor);
    	
        ZipFile zipfile = null;
        
        try {
        	log.error("下载路径为+++++++++++++++++++++++++++++++++"+filePath);
        	long time = new Date().getTime();
			zipfile = new ZipFile(time+"");

			ZipParameters parameters = new ZipParameters();  
		    parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		    parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);  
			zipfile.addFolder(filePath, parameters);
			response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode("picture"+time+".zip", "UTF-8"));

			in = new BufferedInputStream(new FileInputStream (zipfile.getFile()));

            out = new BufferedOutputStream(response.getOutputStream());
            byte[] data = new byte[2048];
            int len = 0;
            while (-1 != (len=in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				in.close();
				out.close();
				zipfile.getFile().delete();
				targetFile.delete();
			    File delfiledir = new File(filePath);
	            for (File b : delfiledir.listFiles()) {
					b.delete();
				}
	            delfiledir.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    
    
    @RequestMapping("uploadPreSaleFileAndDown")
    public String uploadPreSaleFileAndDown(@RequestParam(value = "uploadPreSaleFile", required = false) MultipartFile file, HttpServletRequest request,HttpServletResponse response,Model model){
    	BufferedInputStream in = null;
    	BufferedOutputStream out = null;
    	String path = request.getSession().getServletContext().getRealPath("");  
    	
    	String fileName = file.getOriginalFilename();  
        File targetFile = new File(path, fileName);  
        //保存  
        try {  
        	if (!targetFile.exists()) {
        		targetFile.mkdirs(); 
        		targetFile.createNewFile();
			}
            file.transferTo(targetFile);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        List<SpecialSkuDTO> list = null;
        try {
        	list = ReadExcel.readXlsx(path+"/"+fileName);
        	System.out.println(list.size());
        	targetFile.delete();
		} catch (Exception e) {
			e.printStackTrace(); 
		}
      
        	for(SpecialSkuDTO spec:list){
	    		  try {
	    			  specialSkuService.saveDTO(spec);
	    		  } catch (ServiceMessageException e) {
	    				e.printStackTrace(); 
	    		  }
        	}
			
		
        List<SupplierDTO> availableSupplierDTOList = null ;
		try {
			availableSupplierDTOList = supplierService.findAllWithAvailable();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        model.addAttribute("supplierDTOList", availableSupplierDTOList);
        model.addAttribute("resultMessage", "save success");
        return "iog";
    }
    
    @RequestMapping("deletePreSaleFile")
    public String deletePreSaleFile(@RequestParam(value = "deletePreSaleFile", required = false) MultipartFile file, HttpServletRequest request,HttpServletResponse response,Model model){
    	BufferedInputStream in = null;
    	BufferedOutputStream out = null;
    	String path = request.getSession().getServletContext().getRealPath("");  
    	
    	String fileName = file.getOriginalFilename();  
        File targetFile = new File(path, fileName);  
        //保存  
        try {  
        	if (!targetFile.exists()) {
        		targetFile.mkdirs(); 
        		targetFile.createNewFile();
			}
            file.transferTo(targetFile);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        List<SpecialSkuDTO> list = null;
        try {
        	list = ReadExcel.readXlsx(path+"/"+fileName);
        	System.out.println(list.size());
        	targetFile.delete();
		} catch (Exception e) {
			e.printStackTrace(); 
		}
			specialSkuService.deleteSkuBySupplierId(list);
        List<SupplierDTO> availableSupplierDTOList = null ;
		try {
			availableSupplierDTOList = supplierService.findAllWithAvailable();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        model.addAttribute("supplierDTOList", availableSupplierDTOList);
        model.addAttribute("resultMessage", "save success");
        return "iog";
    }
    
    @RequestMapping(value = "OnlineDownLoad")
    public void dowmLoadPicOnline(HttpServletResponse response,HttpServletRequest request, String queryJson){
    	//获取要下载的产品
    	List<ProductDTO> productList = getDownProductList(queryJson);
    	if (null==productList||productList.size()<1) {
			return;
		}
    	//遍历 获取要下载的图片 map<spskuid,url1,url2>
    	Map<String,String> imgMap = getMongoPic(productList);

		log.error("imgMap message ="+imgMap.toString());
    	
    	//下载保存图片
    	BufferedInputStream in = null;
    	BufferedOutputStream out = null;
    	String path = request.getSession().getServletContext().getRealPath("");  
    	ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 15, 300, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(6),new ThreadPoolExecutor.CallerRunsPolicy());
//    	String dirPath = "F:/usr/local/picturetem/"+new Date().getTime();
    	String dirPath =pictmpdownloadpath + +new Date().getTime();   //  /usr/local
		File f1 = new File(dirPath);
		if (!f1.exists()) {
			f1.mkdirs();
		}
		int a = 0;
		PicQueue picQueue = new PicQueue();
		for (Entry<String, String> entry : imgMap.entrySet()) {
			System.out.println("++++"+a+"++++++");
			a++;
			String[] ingArr = entry.getValue().split(",");
			int i = 0;
			for (String img : ingArr) {
				if (org.apache.commons.lang.StringUtils.isNotBlank(img)) {
					try {
						i++;
						File f = new File(dirPath+"/"+entry.getKey()+" ("+i+").jpg");
						if (f.exists()) {
							continue;
						}
						executor.execute(new DowmImage(img.trim(),entry.getKey()+" ("+i+").jpg",dirPath,picQueue));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		delay(executor);
		//重新下载失败的
		 String failUrl = "";
		 String[] split = null;
		 Map<String,Integer> recordMap = new HashMap<String, Integer>();
		 while(executor.getActiveCount()>0||!picQueue.unVisitedUrlsEmpty()){
			 if (picQueue.unVisitedUrlsEmpty()&&executor.getActiveCount()>=0) {
				 System.out.println("============================================都为空=======================================================");
				try {
	 				Thread.sleep(1000*15);
	 			} catch (InterruptedException e) {
	 				e.printStackTrace();
	 			}
				continue;
			 }
			 failUrl = picQueue.unVisitEdUrlDeQueue();
			 if (recordMap.containsKey(failUrl)) {
				 if (recordMap.get(failUrl)>10) {
					continue;
				 }
				 recordMap.put(failUrl, recordMap.get(failUrl)+1);
			 }else{
				 recordMap.put(failUrl, 1);
			 }
			 split = failUrl.split(";");
			 executor.execute(new DowmImage(split[0],split[2],split[1],picQueue));
//			try {
// 				Thread.sleep(500);
// 			} catch (InterruptedException e) {
// 				e.printStackTrace();
// 			}
		 }
		 delay(executor);
		
	       ZipFile zipfile = null;
	        
	        try {
	        	log.error("下载路径为+++++++++++++++++++++++++++++++++"+dirPath);
	        	long time = new Date().getTime();
				zipfile = new ZipFile(time+"");

				ZipParameters parameters = new ZipParameters();  
			    parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			    parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);  
				zipfile.addFolder(dirPath, parameters);
				response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode("picture"+time+".zip", "UTF-8"));

				in = new BufferedInputStream(new FileInputStream (zipfile.getFile()));

	            out = new BufferedOutputStream(response.getOutputStream());
	            byte[] data = new byte[2048];
	            int len = 0;
	            while (-1 != (len=in.read(data, 0, data.length))) {
	                out.write(data, 0, len);
	            }
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					in.close();
					out.close();
					zipfile.getFile().delete();
				    File delfiledir = new File(dirPath);
		            for (File b : delfiledir.listFiles()) {
						b.delete();
					}
		            delfiledir.delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		 
		 
		
		
    }
    private void delay(ThreadPoolExecutor executor){
    	 while(true){
         	if(executor.getActiveCount()==0){
         		log.error("线程活动数为0");
         		break;
         	}
         	try {
 				Thread.sleep(1000*30);
 			} catch (InterruptedException e) {
 				e.printStackTrace();
 			}
         }
    }
    private Map<String,String> getMongoPic(List<ProductDTO> productList){
    	Map<String,String> imgMap = new HashMap<String,String>();
    	Map<String,String> idMap = new HashMap<String,String>();

		Map<String,String > skuspuMap = new HashMap<>();

		for (ProductDTO productDTO : productList) {
			skuspuMap.put(productDTO.getSkuId(),productDTO.getSpuId());
		}
		log.error("skuspuMap = " + skuspuMap.toString());
    	
    	Map<String, String> findMap = null;
		String sku="",spu="";
    	for (ProductDTO productDTO : productList) {
    		
    		//TODO 如果spskuid为空跳过
    		idMap.put(productDTO.getSpuId(), productDTO.getSpSkuId());
    		idMap.put(productDTO.getSkuId(), productDTO.getSpSkuId());
    		findMap = pfs.findPictureBySupplierIdAndSkuIdOrSpuId(productDTO.getSupplierId(), productDTO.getSkuId(),null);
			if (null==findMap||findMap.size()<1) {
				findMap =pfs.findPictureBySupplierIdAndSkuIdOrSpuId(productDTO.getSupplierId(), null,productDTO.getSpuId());
			}
			if (null!=findMap&&findMap.size()>0) {
				//换成尚品内部的SKU编号 现停 换成其它的 20160819
//				for (Entry<String, String> m : findMap.entrySet()) {
//					imgMap.put(idMap.get(m.getKey()), m.getValue());
//				}

				for (Entry<String, String> m : findMap.entrySet()) {

					if(skuspuMap.containsKey(m.getKey())){//sku
						try {
//							spu = URLEncoder.encode(skuspuMap.get(m.getKey()),"utf-8");
							spu = getBASE64(skuspuMap.get(m.getKey()));
						} catch (Exception e) {
							log.error("转码失败");
							e.printStackTrace();
						}
					}else{//spu

						try {
							spu = getBASE64(m.getKey());
						} catch (Exception e) {
							log.error("转码失败");
							e.printStackTrace();
						}

					}
					imgMap.put("SPID"+productDTO.getSupplierId()+"-"+spu, m.getValue());
				}

			}
		}

    	return imgMap;
    	
    }


	public static String getBASE64(String s) {
		if (s == null) return null;
		return (new sun.misc.BASE64Encoder()).encode( s.getBytes() );
	}

	private List<ProductDTO> getDownProductList(String queryJson){

    	ProductSearchDTO productSearchDTO = (ProductSearchDTO) JsonUtil.getObject4JsonString(queryJson, ProductSearchDTO.class);
    	if(null==productSearchDTO) productSearchDTO = new ProductSearchDTO();

        String supplier = null;
        if(!StringUtils.isEmpty(productSearchDTO.getSupplier()) && !productSearchDTO.getSupplier().equals("-1")){
        	supplier = productSearchDTO.getSupplier();
        }
        Date startDate  =null;
        if(!StringUtils.isEmpty(productSearchDTO.getStartDate())){
            startDate =  DateTimeUtil.convertFormat(productSearchDTO.getStartDate(),"yyyy-MM-dd HH:mm:ss");
        }

        Date endDate = null;
        if(!StringUtils.isEmpty(productSearchDTO.getEndDate())){
            endDate= DateTimeUtil.convertFormat(productSearchDTO.getEndDate(), "yyyy-MM-dd HH:mm:ss");
        }
        
        Integer pageIndex = productSearchDTO.getPageIndex();
        
        Integer pageSize = productSearchDTO.getPageSize();
        List<ProductDTO> pList = productService.findPicName(supplier, startDate, endDate, pageIndex, pageSize);
        return pList;
    }

}

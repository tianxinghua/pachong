/**
 * 
 */
package com.shangpin.iog.webcontainer.front.controller;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadPoolExecutor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.json.JsonUtil;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.ProductSearchDTO;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.service.OrderService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.service.SupplierService;
import com.shangpin.iog.webcontainer.front.strategy.NameGenContext;
import com.shangpin.iog.webcontainer.front.strategy.PcodeAsName;
import com.shangpin.iog.webcontainer.front.util.SavePic;


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
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		pcode = bdl.getString("pcode");
		pcodecolor = bdl.getString("pcodecolor");
		skuwithoutsize = bdl.getString("skuwithoutsize");
		efashion = bdl.getString("efashion");
		pavin = bdl.getString("pavin");
	}
	private Logger log = LoggerFactory.getLogger(FileDownloadController.class) ;
	
    @Autowired
    ProductSearchService productService;
    
    @Autowired
    SupplierService supplierService;
    
    @Autowired
    OrderService orderService;

    @RequestMapping(value = "view")
    public ModelAndView viewPage() throws Exception {
        ModelAndView mv = new ModelAndView("iog");
        List<SupplierDTO> supplierDTOList = supplierService.findAllWithAvailable();

        mv.addObject("supplierDTOList",supplierDTOList);
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
            
            if (productSearchDTO.getFlag().equals("same")) {
            	productBuffer =productService.exportProduct(supplier,startDate,endDate,productSearchDTO.getPageIndex(),productSearchDTO.getPageSize(),productSearchDTO.getFlag());
            	response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode(null==productSearchDTO.getSupplier()?"All":productSearchDTO.getSupplierName()+ "_product" + System.currentTimeMillis() + ".csv", "UTF-8"));
			}else if(productSearchDTO.getFlag().equals("order")){
				
				productBuffer =orderService.exportOrder(supplier,startDate,endDate,pageIndex,pageSize,productSearchDTO.getFlag());
				response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode(null==productSearchDTO.getSupplier()?"All":productSearchDTO.getSupplierName()+ "_order" + System.currentTimeMillis() + ".csv", "UTF-8"));
				
			}else if(productSearchDTO.getFlag().equals("ep_regular")){//按条件导出
				productBuffer =productService.exportProduct(supplier,startDate,endDate,productSearchDTO.getPageIndex(),productSearchDTO.getPageSize(),productSearchDTO.getFlag());
            	response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode(null==productSearchDTO.getSupplier()?"All":productSearchDTO.getSupplierName()+ "_product" + System.currentTimeMillis() + ".csv", "UTF-8"));

			
			}else if(productSearchDTO.getFlag().equals("ep_rule")){
				productBuffer =productService.exportProductByEpRule(supplier,startDate,endDate,productSearchDTO.getPageIndex(),productSearchDTO.getPageSize());
            	response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode(null==productSearchDTO.getSupplier()?"All":productSearchDTO.getSupplierName()+ "_product" + System.currentTimeMillis() + ".csv", "UTF-8"));
			
			}			
			else{
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
    public ModelAndView queryOrders(
                         HttpServletResponse response,
                         String queryJson){
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
    	ModelAndView modelAndView = new ModelAndView();
    	List<OrderDTO> orderList = null;
    	try{
    		
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
            
            
            if(pageIndex != null && pageSize != null && pageIndex != -1 && pageSize != -1){			
    			orderList = orderService.getOrderBySupplierIdAndTime(supplier, startDate, endDate, pageIndex, pageSize);
    						
    		}else{			
    			orderList = orderService.getOrderBySupplierIdAndTime(supplier, startDate, endDate);
    						
    		}	     
            for (OrderDTO orderDTO : orderList) {
				orderDTO.setStatus(nameMap.get(orderDTO.getStatus().toLowerCase()));
			}
            modelAndView.addObject("orderList", orderList);
    		modelAndView.setViewName("orders");
    		
    	}catch(Exception ex){
    		log.error(ex.getMessage());
    		ex.printStackTrace();
    	}        
		return modelAndView;
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
        	//TODO 获取dto按条件拼接图片名称
        	
        	
        	NameGenContext context = new NameGenContext(supplier);
        	nameMap = context.operate(pList);
        	
        	zipfile = new ZipFile(new File(new Date().getTime()+""));
        	ArrayList<File> filesToAdd = new ArrayList<File>();
    		//供应商pic的文件夹
    		//TODO 具体位置待定
    		File dir = new File("/mnt/nfs/"+productSearchDTO.getSupplierName()+"/");
    		String key = "";
    		if (dir.isDirectory()) {
    			File[] files = dir.listFiles();
    			for (File file : files) {
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
        SavePic savePic = new SavePic();
        String filePath = savePic.saveImg(targetFile);
        ThreadPoolExecutor executor = savePic.getExecutor();
        while(true){
        	if(executor.getActiveCount()==0){
        		break;
        	}
        	try {
				Thread.sleep(1000*30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        ZipFile zipfile = null;
        
        try {
			zipfile = new ZipFile(new File(new Date().getTime()+""));

			ZipParameters parameters = new ZipParameters();  
		    parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
		    parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);  
			zipfile.addFolder(filePath, parameters);
			response.setHeader("Content-Disposition", "attachment;filename="+java.net.URLEncoder.encode("picture"+new Date().getTime()+".zip", "UTF-8"));

			in = new BufferedInputStream(new FileInputStream (zipfile.getFile()));

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
 
    
    
}

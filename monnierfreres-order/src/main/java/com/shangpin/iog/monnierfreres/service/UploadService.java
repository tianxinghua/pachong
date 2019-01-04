package com.shangpin.iog.monnierfreres.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.dto.OrderDetailDTO;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.monnierfreres.utils.FTPUtils;
import com.shangpin.iog.service.OrderDetailService;
import com.shangpin.iog.service.ProductSearchService;

@Component
public class UploadService {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("info");
    private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");
    
    private static  String supplierId = null;
    
    private static String  startDate=null,endDate=null;
    private static final String YYYY_MMDD_HH = "yyyy-MM-dd HH:mm:ss";
    private static String  startDateOfUpload=null,endDateOfUpload=null; 
    
    private static String confirmOrderSavePath = null;
    
    private static String ip = null;
    private static int port = 21;
    private static String usrName = null;
    private static String password = null;
    private static String remotePath = null;
    
    private static ResourceBundle bdl=null;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        startDateOfUpload = bdl.getString("startDateOfUpload");
        endDateOfUpload = bdl.getString("endDateOfUpload");
        confirmOrderSavePath = bdl.getString("confirmOrderSavePath");
        supplierId = bdl.getString("supplierId");
        
        ip = bdl.getString("ip");
        port = Integer.parseInt(bdl.getString("port"));
        usrName = bdl.getString("usrName");
        password  = bdl.getString("password");
        remotePath = bdl.getString("remotePath"); 
    }
    
    @Autowired
    ProductSearchService productSearchService;
    
    @Autowired
    OrderDetailService orderDetailService;

	/**
	 * 推送订单
	 */
	public void uploadConfirmOrder(){
		try {
			
			initDate("dateOfUpload.ini");			
			logger.info("=========开始时间"+startDate+"到结束时间"+endDate+"的订单开始保存本地=========");
			
			String fileName = "S"+DateTimeUtil.convertFormat(DateTimeUtil.convertFormat(endDate,YYYY_MMDD_HH),"yyyyMMddHHmm").substring(2); 
			String DATCMD = DateTimeUtil.convertFormat(DateTimeUtil.convertFormat(startDate,YYYY_MMDD_HH),"yyyyMMdd");
			String sep = ";";
			List<OrderDetailDTO> orderDetails = orderDetailService.getOrderBySupplierIdAndOrderStatusAndUpdateTime(supplierId, OrderStatus.CONFIRMED, startDate, endDate);
			logger.info("该时段查到的采购单的数量是======"+orderDetails.size()); 
			if(null != orderDetails && orderDetails.size()>0){
				StringBuffer buffer = new StringBuffer();
				buffer.append("REFCMD;DATCMD;DATLIV;INSCMD;CODCLI;CODTRP;REFCMD;NOMCLI;NOMSTE;ADRES1;ADRES2;VILNOM;CODPST;CODPAY;MNTTOT;CODPRD;QTECMD;PRXUNT;PRPTYP;NLGLIV;NUMLOT;INSLI1;INSLI2;INSCD2;RELNOM;ADREL1;ADREL2;ADREL3;VILREL;RELPST;RELPAY;CMPAD1;CMPAD2;CMPAD3;TELFIX;TELMOB;CODREL;MAILAD;LOTACH;DISOR;DISSOV;FACNOM;ADREF1;ADREF2;ADREF3;VILFAC;FACPST;FACPAY;MCTRBS;MSGCD1;MSGCD2;MSGCD3;MSGCD4");
				buffer.append("\r\n");
				for (OrderDetailDTO orderDTO:orderDetails){
		        	try {
		        		
						ProductDTO product = productSearchService.findProductForOrder(supplierId,orderDTO.getSupplierSku());
						buffer.append(fileName).append(sep).append(DATCMD).append(sep).append("").append(sep).append("").append(sep).append("Shangpin").append(sep)
						.append("8300").append(sep).append("COMMANDE SHANGPIN").append(sep).append("SHANGPIN").append(sep).append("GENERTEC ITALIA").append(sep)
						.append("VIA GIACOMO LEOPARDI").append(sep).append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep)
						.append(product.getProductCode()).append(sep).append(orderDTO.getQuantity()).append(sep).append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep)
						.append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep)
						.append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep)
						.append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep)
						.append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep).append("").append(sep); 
						
		        	}catch(Exception ex){
		        		ex.printStackTrace();
		    			loggerError.error(ex); 
		        	}
				}
				//TODO 保存订单
				logger.info("开始保存订单"); 
				String filePath = confirmOrderSavePath+File.separator+fileName+".csv";
				System.out.println(filePath); 
				save(filePath,buffer); 
				//TODO 上传订单
				logger.info("开始上传订单"); 
				FTPUtils.uploadFile(ip, port, usrName, password, remotePath, filePath);
				
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
			loggerError.error(e); 
		}
	}
	
	/**
	 * 推送取消的订单
	 */
	public void uploadCancelledOrder(){
		
	}
	
	private  static void initDate(String  fileName) {
        Date tempDate = new Date();

        endDate = com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(tempDate, YYYY_MMDD_HH);

        String lastDate=getLastGrapDate(fileName);
        startDate= org.apache.commons.lang.StringUtils.isNotEmpty(lastDate) ? lastDate: com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(DateUtils.addDays(tempDate, -180), YYYY_MMDD_HH);

        Date tmpDate =  DateTimeUtil.getAppointDayFromSpecifiedDay(DateTimeUtil.convertFormat(startDate,YYYY_MMDD_HH),-10,"S");
        startDate = DateTimeUtil.convertFormat(tmpDate,YYYY_MMDD_HH) ;
        
        if(StringUtils.isNotBlank(startDateOfUpload)){
        	startDate =startDateOfUpload;
        }
        
        if(StringUtils.isNotBlank(endDateOfUpload)){
        	endDate = endDateOfUpload;
        }else{
        	writeGrapDate(endDate,fileName);
        }
    }

    private static File getConfFile(String fileName) throws IOException {
        String realPath = UploadService.class.getClassLoader().getResource("").getFile();
        realPath= URLDecoder.decode(realPath, "utf-8");
        File df = new File(realPath+fileName);//"date.ini"
        if(!df.exists()){
            df.createNewFile();
        }
        return df;
    }
    private static String getLastGrapDate(String fileName){
        File df;
        String dstr=null;
        try {
            df = getConfFile(fileName);
            try(BufferedReader br = new BufferedReader(new FileReader(df))){
                dstr=br.readLine();
            }
        } catch (IOException e) {
        	loggerError.error("读取日期配置文件错误");
        }
        return dstr;
    }

    private static void writeGrapDate(String date,String fileName){
    	int i=0;
    	boolean boo = true;
    	while(boo && i<100){
    		File df;
            try {
                df = getConfFile(fileName);
                try(BufferedWriter bw = new BufferedWriter(new FileWriter(df))){
                    bw.write(date);
                }
                boo = false;
            } catch (IOException e) {
            	loggerError.error("写入日期配置文件错误 "+i); 
                boo = true;
                try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
            }finally{
            	i++;
            }
    	}
        
    }
    
    public void save(String path,StringBuffer buffer){
    	try {
    		File file = new File(path);
    		if (!file.exists()) {
    			try {
    				file.getParentFile().mkdirs();
    				file.createNewFile();
    				
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    		FileWriter fwriter = null;
    		try {
    			fwriter = new FileWriter(path);
    			fwriter.write(buffer.toString());
    		} catch (IOException ex) {
    			ex.printStackTrace();
    		} finally {
    			try {
    				fwriter.flush();
    				fwriter.close();
    			} catch (IOException ex) {
    				ex.printStackTrace();
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
			loggerError.error(e);
		}
    	
    }
    
    public static void main(String[] args) {
//    	String fileName = "S"+DateTimeUtil.convertFormat(DateTimeUtil.convertFormat("2016-07-11 22:00:45",YYYY_MMDD_HH),"yyyyMMddHHmm").substring(2);
//    	System.out.println(fileName);
    	FTPUtils.uploadFile(ip, port, usrName, password, "/out/commandes", confirmOrderSavePath+File.separator+"ddd"+".csv");
	}
	
}

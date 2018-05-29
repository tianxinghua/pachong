package com.shangpin.ephub.product.business.ui.pending.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.product.business.service.pending.PendingService;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuModelMsgVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuModelVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuPendingAuditQueryVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuPendingAuditVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuPendingVO;
import com.shangpin.ephub.response.HubResponse;

@RestController
@RequestMapping("/pending-audit")
@Slf4j
public class PendingAuditController {
	
	@Autowired
	PendingService pendingService;


	@RequestMapping(value="query-spumodel",method=RequestMethod.POST)
	public HubResponse<?> querySpuModel(@RequestBody SpuPendingAuditQueryVO queryVO){

		SpuModelMsgVO spuModel = pendingService.getSpuModel(queryVO);

		return HubResponse.successResp(spuModel);
	}


	@RequestMapping(value="query-spupending",method=RequestMethod.POST)
	public HubResponse<?> querySpuPending(@RequestBody SpuModelVO queryVO){

		List<SpuPendingVO> spuPendingVOList = pendingService.getSpuPendingByBrandNoAndSpuModel(queryVO.getBrandNo(), queryVO.getSpuModel());
		return HubResponse.successResp(spuPendingVOList);
	}


	@RequestMapping(value="audit-spupendig",method=RequestMethod.POST)
	public HubResponse<?> querySpuPending(@RequestBody SpuPendingAuditVO auditVO){
		log.info("待审核请求参数：{}",auditVO);
		List<SpuPendingAuditVO> auditList = new ArrayList<>();
		auditList.add(auditVO);
//		try {
//			if(!pendingService.audit(auditVO)){
//				return HubResponse.errorResp(auditVO.getMemo());
//			}
//		} catch (Exception e) {
////			e.printStackTrace();
//			log.error("待审核失败 ：" + " reason :" +  e.getMessage(),e);
//			return HubResponse.errorResp(e.getMessage());
//		}
//		return HubResponse.successResp(true);
		return pendingService.batchAudit(auditList);
	}

	
	@RequestMapping(value="/batch-audit-spupendig",method=RequestMethod.POST)
	public HubResponse<?> batchAuditSpuPending(@RequestBody List<SpuPendingAuditVO> auditList){

		return pendingService.batchAudit(auditList);
	}
//		try {
//	    	ExecutorService exe = new ThreadPoolExecutor(10,10, 500, TimeUnit.MILLISECONDS,
//					new ArrayBlockingQueue<Runnable>(100),new ThreadPoolExecutor.CallerRunsPolicy());
//	    	
//	    		    	
//	    	
//			StringBuilder result = new StringBuilder();
//			if(auditList!=null&&!auditList.isEmpty()){
//				for(SpuPendingAuditVO auditVO:auditList){
//					if(!pendingService.audit(auditVO)){
//						result.append(auditVO.getMemo());
//					}
//				}
//			}
//			return HubResponse.errorResp(result);
//		} catch (Exception e) {
//			log.error("待审核失败 ：" + " reason :" +  e.getMessage(),e);
//			return HubResponse.errorResp(e.getMessage());
//		}
//	}
	
	
	public static void moreThread() {  
	    try {   
	    	ExecutorService exe = new ThreadPoolExecutor(10,10, 500, TimeUnit.MILLISECONDS,
					new ArrayBlockingQueue<Runnable>(100),new ThreadPoolExecutor.CallerRunsPolicy());
	        int threadNum = 0;  
	        for (int i = 0; i < 10; i++) {  
	            threadNum++;  
	              
	            final int currentThreadNum = threadNum;  
	            exe.execute(new Runnable() {  
	                @Override  
	                public void run() {  
	                    try {  
	                        System.out.println("子线程[" + currentThreadNum + "]开启");  
	                        Thread.sleep(1000*10);  
	                    } catch (InterruptedException e) {  
	                        e.printStackTrace();  
	                    }finally{  
	                        System.out.println("子线程[" + currentThreadNum + "]结束");  
	                    }  
	                }  
	            });    
	        }  
	          
	        exe.shutdown();  
	        while(true){  
	            if(exe.isTerminated()){  
	                System.out.println("所有的子线程都结束了！");  
	                break;  
	            }  
	        }  
	          
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }finally{  
	        System.out.println("主线程结束");  
	    }  
	}  
	
}

package com.shangpin.ephub.data.schedule.service.product;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.data.schedule.service.mail.SendMailService;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title: ProductPullDataService</p>
 * <p>Description: 检测产品拉去是否正常 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年7月19日 上午11:49:43
 *
 */
@Service("productPullDataServiceImpl")
@Slf4j
public class ProductPullDataService {
	
	private static final int MAX_TIMEVALUE = 24;
	private static final String LINE_BREAK = "<br>";
	
	@Autowired
	private HubSupplierValueMappingGateWay supplierValueMappingGateWay;	
    @Autowired
    private HubSupplierSkuGateWay supplierSkuGateWay;
    @Autowired
    private SendMailService sendMailService;
    
    public void productCheck(){
    	List<HubSupplierValueMappingDto> list = findAllSupplier();
    	if(CollectionUtils.isNotEmpty(list)){
    		boolean haveSupplier = false;
    		StringBuffer buffer = new StringBuffer();
    		buffer.append("以下供应商已经超过"+MAX_TIMEVALUE+"小时没有拉取产品了，请检查原因：").append(LINE_BREAK);
    		for(HubSupplierValueMappingDto dto : list){
    			log.info("开始检测供应商："+dto.getHubVal()); 
    			boolean result = findSupplierLastPullTime(dto.getSupplierId());
    			if(!result){
    				haveSupplier = true;
    				buffer.append("供应商 "+dto.getHubVal()+" 编号 "+dto.getSupplierId()).append(LINE_BREAK);
    			}
    		}
    		if(haveSupplier){
    			sendMailService.sendMail("EPHUB拉去产品检测", buffer.toString()); 
    		}
    	}
    }

    /**
     * 检测该供应商{@link MAX_TIMEVALUE}小时之内是否拉去过产品，是返回true，否则返回false
     * @param supplierId
     * @return
     */
    public boolean findSupplierLastPullTime(String supplierId){
    	Date lastPullTime = new Date(new Date().getTime() - MAX_TIMEVALUE*1000*60*60);
        HubSupplierSkuCriteriaDto criteria = new HubSupplierSkuCriteriaDto();
        criteria.setPageNo(1);
        criteria.setPageSize(1); 
        criteria.setFields("last_pull_time");
        criteria.createCriteria().andSupplierIdEqualTo(supplierId).andLastPullTimeGreaterThanOrEqualTo(lastPullTime);
        List<HubSupplierSkuDto> skuDtos = supplierSkuGateWay.selectByCriteria(criteria);
        if(CollectionUtils.isNotEmpty(skuDtos)){
        	return true;
        }else{
        	return false;
        }
    }
    
    /**
     * 获取所有的供应商门户编号
     * @return
     */
    public List<HubSupplierValueMappingDto> findAllSupplier(){
    	HubSupplierValueMappingCriteriaDto criteria = new HubSupplierValueMappingCriteriaDto();
    	criteria.setFields("supplier_id,hub_val");
    	criteria.setOrderByClause("hub_val");
    	criteria.setPageNo(1); 
    	criteria.setPageSize(1000); 
    	criteria.createCriteria().andHubValTypeEqualTo((byte)5).andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
		return supplierValueMappingGateWay.selectByCriteria(criteria);
    }
}

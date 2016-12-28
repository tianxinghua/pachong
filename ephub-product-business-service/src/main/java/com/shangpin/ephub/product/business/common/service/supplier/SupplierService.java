package com.shangpin.ephub.product.business.common.service.supplier;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.common.dto.SupplierDTO;
import com.shangpin.ephub.product.business.common.enumeration.GlobalConstant;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title:SupplierService </p>
 * <p>Description: 通过供应商编号获取供应商信息的service</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月27日 下午2:29:21
 *
 */
@Component
@Slf4j
public class SupplierService {
	
	@Autowired
    private IShangpinRedis shangpinRedis;
	@Autowired
	private RestTemplate httpClient;

	/**
     * 通过供货商编号查询供货商信息,失败返回null
     * @param supplierNo
     * @return
     */
    public SupplierDTO getSupplier(String supplierNo) {
    	try {
    		//先获取缓存中的数据
            String supplierMsg = shangpinRedis.get(GlobalConstant.REDIS_ORDER_SUPPLIER_KEY+"_"+supplierNo);
            if(!StringUtils.isEmpty(supplierMsg)){
            	return JsonUtil.deserialize(supplierMsg, SupplierDTO.class);
            }else{
            	//调用接口获取供货商信息
                Map<String, String> paraMap = new HashMap<>();
                paraMap.put("supplierNo", supplierNo);
                String url = "http://scm.shangpin.com/scms/Supplier/GetSupplierInfoListByNo?supplierNo="+supplierNo;
                SupplierDTO supplierDto = httpClient.getForEntity(url, SupplierDTO.class).getBody();
                try {
                	//缓存到redis中
                    shangpinRedis.setex(GlobalConstant.REDIS_ORDER_SUPPLIER_KEY+"_"+supplierNo,1000*60*5,JsonUtil.serialize(supplierDto));
    			} catch (Exception e) {
    				log.error(supplierNo+"缓存供应商信息到redis时出错，错误信息为："+e.getMessage(),e); 
    			}
                return supplierDto;            
            }
		} catch (Exception e) {
			log.error(supplierNo+"通过supplierNo获取supplierName时出错，错误信息为："+e.getMessage(),e); 
			return null;
		}
        

    }
}

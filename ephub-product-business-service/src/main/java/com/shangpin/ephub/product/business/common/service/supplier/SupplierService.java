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
import com.shangpin.ephub.product.business.conf.rpc.ApiAddressProperties;

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
	@Autowired
	private ApiAddressProperties apiAddress;

	/**
     * 通过供货商编号查询供货商信息,失败返回null
     * @param supplierNo
     * @return
     */
    public SupplierDTO getSupplier(String supplierNo) {
    	try {
    		if(StringUtils.isEmpty(supplierNo)){
    			log.error("通过供应商编号查询供应商信息时，请传入有效的编号");
    			return null;
    		}
            String supplierMsg = getScmsSupplierInfoByReids(supplierNo);
            if(!StringUtils.isEmpty(supplierMsg)){
            	return JsonUtil.deserialize(supplierMsg, SupplierDTO.class);
            }else{
                SupplierDTO supplierDto = getScmsSupplierInfoByApi(supplierNo);
                setScmsSupplierInfoToRedis(supplierNo,supplierDto);
                return supplierDto;            
            }
		} catch (Exception e) {
			log.error(supplierNo+"通过supplierNo获取supplierName时出错，错误信息为："+e.getMessage(),e); 
			return null;
		}
    }

    /**
     * 通过api查找供应商信息
     * @param supplierNo
     * @return
     * @throws Exception
     */
	private SupplierDTO getScmsSupplierInfoByApi(String supplierNo) throws Exception {
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("supplierNo", supplierNo);
		String url = apiAddress.getScmsSupplierInfoUrl()+supplierNo;
		String reSupplierMsg = httpClient.getForObject(url, String.class);
		SupplierDTO supplierDto = JsonUtil.deserialize2(reSupplierMsg, SupplierDTO.class);
		return supplierDto;
	}
    /**
     * 将供应商信息缓存到reids
     * @param supplierNo
     * @param supplierDto
     */
    public void setScmsSupplierInfoToRedis(String supplierNo,SupplierDTO supplierDto){
    	try {
        	//缓存到redis中
            shangpinRedis.setex(GlobalConstant.REDIS_ORDER_SUPPLIER_KEY+"_"+supplierNo,1000*60*5,JsonUtil.serialize(supplierDto));
		} catch (Exception e) {
			log.error(supplierNo+"缓存供应商信息到redis时出错，错误信息为："+e.getMessage(),e); 
		}
    }
    /**
     * 从reids中取供应商信息
     * @param supplierNo
     * @return
     */
    public String getScmsSupplierInfoByReids(String supplierNo){
    	try {
    		return shangpinRedis.get(GlobalConstant.REDIS_ORDER_SUPPLIER_KEY+"_"+supplierNo);
		} catch (Exception e) {
			log.error("通过redis获取供应商时异常："+e.getMessage(),e); 
			return "";
		}
    }
}

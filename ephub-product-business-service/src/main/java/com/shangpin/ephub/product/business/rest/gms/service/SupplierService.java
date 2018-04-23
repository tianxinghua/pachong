package com.shangpin.ephub.product.business.rest.gms.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.product.business.rest.gms.dto.SupplierDTO;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.common.enumeration.GlobalConstant;
import com.shangpin.ephub.product.business.conf.rpc.ApiAddressProperties;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title:SupplierInHubService </p>
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
		
		try{
			long start_supplierService = System.currentTimeMillis();
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("supplierNo", supplierNo);
			String url = apiAddress.getScmsSupplierInfoUrl()+supplierNo;
			log.info("supplierNo:"+supplierNo);
			String reSupplierMsg = httpClient.getForObject(url, String.class);
			log.info("reSupplierMsg:"+reSupplierMsg);
			SupplierDTO supplierDto = JsonUtil.deserialize2(reSupplierMsg, SupplierDTO.class);
			log.info("--->请求供应商名称接口耗时{}",System.currentTimeMillis() - start_supplierService);
			return supplierDto;
		}catch(Exception e){
			e.getStackTrace();
			throw e;
		}
		
	}
    /**
     * 将供应商信息缓存到reids
     * @param supplierNo
     * @param supplierDto
     */
    public void setScmsSupplierInfoToRedis(String supplierNo,SupplierDTO supplierDto){
    	try {
        	//缓存到redis中
            shangpinRedis.setex(GlobalConstant.REDIS_ORDER_SUPPLIER_KEY+"_"+supplierNo,60*60*24*7,JsonUtil.serialize(supplierDto));
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

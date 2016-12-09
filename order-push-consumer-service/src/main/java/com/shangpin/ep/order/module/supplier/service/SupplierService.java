package com.shangpin.ep.order.module.supplier.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ep.order.module.supplier.bean.SupplierDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:SupplierService.java </p>
 * <p>Description: 供货商信息获取业务逻辑实现</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月21日 上午12:06:53
 */
@Service
@Slf4j
public class SupplierService {
	
	private static final String PREFIX = "EP_ORDER_SERVICE:";
	
    @Autowired
    private IShangpinRedis shangpinRedis;
	/**
	 * @param supplierNo 前台系统提供的供货商编号
	 * @return 供货商信息
	 */
	public SupplierDTO getSupplierDTO(String supplierNo){
		if (StringUtils.isBlank(supplierNo)) {
			return null;
		}
		String supplier = null;
		try {
			supplier = shangpinRedis.get(PREFIX+supplierNo);
		} catch (Throwable e) {
			log.error("系统从redis缓存中查询编号为{}所对应的供货商信息时发生异常，系统将自动切换到通过调用接口获取供货商信息。请检查RedisCluster集群是否工作正常！", supplierNo);
			e.printStackTrace();
		}
		if (StringUtils.isNotBlank(supplier)) {
			return JSON.parseObject(supplier, SupplierDTO.class);
		} else {
			log.info("系统将通过调用接口获取编号为{}的供货商信息", supplierNo);
			SupplierDTO supplierDTO = null;
			try {
				supplierDTO = querySupplierDTOFromRpc(supplierNo);
			} catch (Throwable ee) {
				log.error("系统通过RPC接口获取编号为"+supplierNo+"的供货商信息时发生异常，系统将读取配置文件的方式获取供货商信息！", ee);
				ee.printStackTrace();
			}
			if (supplierDTO == null) {
				 //TODO 如何读取配置文件？？？？？？
				return null;
			} else {
				try {
					shangpinRedis.set(PREFIX+supplierNo, JSON.toJSONString(supplierDTO));
				} catch (Throwable eee) {//缓存不可用不要让程序停在这，而是继续执行！
					log.error("系统往redis缓存中缓存激活码【{}】所对应策略编号时发生异常，请检查RedisCluster集群是否工作正常！", supplierNo);
					eee.printStackTrace();
				}
				return supplierDTO;
			}
		}
		
		
	}
	/**
	 * 通过调接口的方式获取指定编号供货商的信息
	 * @param supplierNo 供货商编号
	 * @return 供货商信息
	 */
	private SupplierDTO querySupplierDTOFromRpc(String supplierNo) {
		// TODO 如何调接口？？？？？？？
		return null;
	}
	
}

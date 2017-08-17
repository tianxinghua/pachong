package com.shangpin.ephub.data.mysql.product.supplier.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.data.mysql.product.supplier.bean.HubSupplierProductRequestWithPage;
import com.shangpin.ephub.data.mysql.product.supplier.mapper.HubSupplierProductMapper;
import com.shangpin.ephub.data.mysql.product.supplier.po.HubSupplierProduct;
import com.shangpin.ephub.data.mysql.sku.supplier.mapper.HubSupplierSkuMapper;
import com.shangpin.ephub.data.mysql.sku.supplier.po.HubSupplierSku;
import com.shangpin.ephub.data.mysql.spu.supplier.mapper.HubSupplierSpuMapper;
import com.shangpin.ephub.data.mysql.spu.supplier.po.HubSupplierSpu;

/**
 * <p>Title:HubSkuService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月14日 下午5:09:16
 */
@Service
public class HubSupplierProductService {

	@Autowired
	private HubSupplierProductMapper hubSkuMapper;
	@Autowired
	private HubSupplierSkuMapper hubSupplierSkuMapper;
	@Autowired
	private HubSupplierSpuMapper hubSupplierSpuMapper;

	public List<HubSupplierProduct> selectHubSupplierProductByPage(HubSupplierProductRequestWithPage request) {
		return hubSkuMapper.selectHubSupplierProductByPage(request);
	}
	
	public int count(HubSupplierProductRequestWithPage request) {
		return hubSkuMapper.count(request);
	}

	public Long insert(HubSupplierProduct product) {
		HubSupplierSpu spu = new HubSupplierSpu();
		BeanUtils.copyProperties(product, spu);
		hubSupplierSpuMapper.insertSelective(spu);
		Long supplierSpuId = spu.getSupplierSpuId();
		if(supplierSpuId!=null){
			HubSupplierSku sku = new HubSupplierSku();
			sku.setSupplierSpuId(supplierSpuId);
			BeanUtils.copyProperties(product, sku);
		    hubSupplierSkuMapper.insertSelective(sku);
		    return sku.getSupplierSkuId();
		}
		return null;
	}
}

package com.shangpin.iog.product.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ShangPin.SOP.Entity.Api.Product.SopProductSkuIce;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPage;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPageQuery;
import ShangPin.SOP.Entity.Api.Product.SopSkuIce;
import ShangPin.SOP.Servant.OpenApiServantPrx;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.IcePrxHelper;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月10日
 */
public abstract class AbsUpdateProductStock {
	static Logger logger = LoggerFactory.getLogger(AbsUpdateProductStock.class);
	static int pageIndex=1;
	static int pageSize=50;
	/**
	 * 抓取供应商库存数据 
	 * @param skuNo 供应商的每个产品的唯一编号：sku
	 * @see #grabProduct() 抓取主站SKU
	 * @return 每个sku对应的库存数
	 * @throws ServiceException 
	 */
	public abstract Map<String,Integer> grabStock(Collection<String> skuNo) throws ServiceException;
	/**
	 * 抓取主站商品SKU信息，等待更新库存<br/>
	 * @see #updateProduct(String, Map) 更新库存
	 * @return
	 * @throws Exception 
	 */
	private Collection<String> grabProduct(String supplier,String start,String end) throws Exception{
		OpenApiServantPrx servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
		boolean hasNext=true;
		Set<String> skuIds = new HashSet<String>();
		while(hasNext){
			SopProductSkuPageQuery query = new SopProductSkuPageQuery(start,end,pageIndex,pageSize);
			SopProductSkuPage products = servant.FindCommodityInfoPage(supplier, query);
			List<SopProductSkuIce> skus = products.SopProductSkuIces;
			for (SopProductSkuIce sku : skus) {
				List<SopSkuIce> skuIces = sku.SopSkuIces;
				for (SopSkuIce ice : skuIces) {
					skuIds.add(ice.SkuNo);
				}
			}
			pageIndex++;
			hasNext=products.Total>pageSize;
		}
		return skuIds;
	}
	/**
	 * 更新主站库存
	 * @param supplier 供应商id
	 * @param start 主站产品数据时间开始 yyyy-MM-dd HH:mm
	 * @param end 主站产品数据时间结束 yyyy-MM-dd HH:mm
	 * @see {@link #grabStock(List) 抓取库存 }
	 * @return 更新失败数
	 * @throws Exception 
	 */
	public int updateProductStock(String supplier,String start,String end) throws Exception{
		Collection<String> skuNoSet=grabProduct(supplier, start, end);
		//拿库存
		Map<String, Integer> stoks=grabStock(skuNoSet);
		OpenApiServantPrx servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
		int failCount=0;
		if(stoks!=null && stoks.size()>0){
			Iterator<Entry<String, Integer>> iter=stoks.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Integer> entry = iter.next();
				Boolean result = servant.UpdateStock(supplier, entry.getKey(), entry.getValue());
				if(!result){
					failCount++;
					logger.warn("更新SKU：{}，库存量：{}失败",entry.getKey(),entry.getValue());
				}				
			}
		}
		return failCount;
	}
}

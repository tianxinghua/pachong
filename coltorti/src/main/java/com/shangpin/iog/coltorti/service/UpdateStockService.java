/**
 * 
 */
package com.shangpin.iog.coltorti.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.product.service.AbsUpdateProductStock;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月9日
 */
public class UpdateStockService extends AbsUpdateProductStock{
	static Logger logger = LoggerFactory.getLogger(UpdateStockService.class);

	public Map<String, Integer> grabStock(Collection<String> skuNos) throws ServiceException{
		Map<String, Integer> skuStock= new HashMap<>(skuNos.size());
		for (Iterator<String> iterator = skuNos.iterator(); iterator
				.hasNext();) {
			String skuNo = iterator.next();
			int scalarIdx=skuNo.lastIndexOf("#");//尺寸标志开始位置
			String productId=skuNo.substring(0, skuNo.lastIndexOf("-"));//产品id
			String recordId=skuNo.substring(0,scalarIdx);//记录id
			String scalarNo=skuNo.substring(scalarIdx+1);//尺寸编号
			Map<String, Map<String, Integer>> stoks=null;
			try{
				stoks=ColtortiStockService.getStock(productId, recordId);
			}catch(ServiceException e){
				if(ColtortiUtil.isTokenExpire(e)){
					ColtortiTokenService.initToken();
					stoks=ColtortiStockService.getStock(productId, recordId);
				}else{
					continue;								
				}
			}
			if(stoks!=null && stoks.size()>0){
				int quantity=stoks.get(recordId).get(scalarNo);
				skuStock.put(skuNo, quantity);
			}
		}
		return skuStock;
	}
	
	public static void main(String[] args) {
		String skuNo="152790FCR000002-MIDBL#m";
		String recordId=skuNo.substring(0, skuNo.lastIndexOf("#"));
		String scalarNo=skuNo.substring(skuNo.lastIndexOf("#")+1);
		String productId=skuNo.substring(0, skuNo.lastIndexOf("-"));
		System.out.println(recordId+","+scalarNo+","+productId);
	}
}

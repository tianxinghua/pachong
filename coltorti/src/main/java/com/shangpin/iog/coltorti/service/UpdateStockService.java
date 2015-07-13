/**
 * 
 */
package com.shangpin.iog.coltorti.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月9日
 */
public class UpdateStockService extends AbsUpdateProductStock{
	static Logger logger = LoggerFactory.getLogger(UpdateStockService.class);

	public Map<String, Integer> grabStock(Collection<String> skuNos) throws ServiceException{
		Map<String, Integer> skuStock= new HashMap<>(skuNos.size());
		Set<String> productIdSet=new HashSet<>();
		String productId=null,recordId=null,scalarNo=null;
		for (Iterator<String> iterator = skuNos.iterator(); iterator
				.hasNext();) {
			String skuNo = iterator.next();
			try{
				int scalarIdx=skuNo.lastIndexOf("#");//尺寸标志开始位置
				productId=skuNo.substring(0, skuNo.lastIndexOf("-"));//产品id
				recordId=skuNo.substring(0,scalarIdx);//记录id
				scalarNo=skuNo.substring(scalarIdx+1);//尺寸编号
			}catch(Exception e){
				logger.error(skuNo+"截取失败.",e);
				skuStock.put(skuNo, 0);
				continue;
			}
			Map<String, Map<String, Integer>> stoks=null;
			try{
				if(!productIdSet.contains(productId)){
					//logger.warn("拉取productId:{}的库存",productId);
					stoks=ColtortiStockService.getStock(productId, null);
					productIdSet.add(productId);
					if(stoks!=null && stoks.size()>0){
						if(stoks.get(recordId)!=null){
							Integer qnt=stoks.get(recordId).get(scalarNo);
							int quantity=qnt==null?0:qnt;
							skuStock.put(skuNo, quantity);
						}else{							
							skuStock.put(skuNo, 0);
						}
					}else{
						skuStock.put(skuNo, 0);						
					}
					logger.warn(skuNo+",拉取库存成功:"+new Gson().toJson(stoks));
				}
			}catch(ServiceException e){
				logger.error("拉取skuNO{}库存失败{}",skuNo,e.getMessage());
				if(ColtortiUtil.isTokenExpire(e)){
					ColtortiTokenService.initToken();
					stoks=ColtortiStockService.getStock(productId, recordId);
				}else{
					continue;						
				}
			}
		}
		return skuStock;
	}
	
	/*
	public static void main(String[] args) {
		String skuNo="152790FCR000002-MIDBL#m";
		String recordId=skuNo.substring(0, skuNo.lastIndexOf("#"));
		String scalarNo=skuNo.substring(skuNo.lastIndexOf("#")+1);
		String productId=skuNo.substring(0, skuNo.lastIndexOf("-"));
		System.out.println(recordId+","+scalarNo+","+productId);
	}*/
}

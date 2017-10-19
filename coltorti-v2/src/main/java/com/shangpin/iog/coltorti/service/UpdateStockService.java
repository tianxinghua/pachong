/**
 * 
 */
package com.shangpin.iog.coltorti.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.coltorti.dto.ColtortiSkuDto;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月9日
 */
@Component("updateStockService")
public class UpdateStockService extends AbsUpdateProductStock{
	static Logger logger = LoggerFactory.getLogger(UpdateStockService.class);

	public Map<String, String> grabStock(Collection<String> skuNos) throws ServiceException{

		Map<String, String> skuStock= new HashMap<>(skuNos.size());
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
				skuStock.put(skuNo, "0");
				continue;
			}
			Map<String, List<ColtortiSkuDto>> stoks=null;
			try{
				if(!productIdSet.contains(productId)){
					//获取库存
					stoks=ColtortiStockService.getStock(productId, null);
					if(stoks!=null && stoks.size()>0){
						for(Entry<String, List<ColtortiSkuDto>> entry : stoks.entrySet()){
							;
							List<ColtortiSkuDto> list = entry.getValue();
							if(null != list && list.size() >0){
								for(ColtortiSkuDto skuDto : list){
									skuStock.put(entry.getKey()+"#"+skuDto.getSizeId(), String.valueOf(skuDto.getStock()));
								}
							}
							
						}
					}else{
						skuStock.put(skuNo, "0");
					}
					productIdSet.add(productId);
					logger.warn(skuNo+",拉取库存成功:"+new Gson().toJson(stoks));
				}
			}catch(ServiceException e){
				logger.error("拉取skuNO{}库存失败{}",skuNo,e.getMessage());
				if(ColtortiUtil.isTokenExpire(e)){
					try{
						ColtortiTokenService.initToken();
					}catch(Exception e1){
						skuStock.put(skuNo, "0");
						continue;
					}
				}else{
					continue;						
				}
			}catch(Exception ex){
				ex.printStackTrace();
				skuStock.put(skuNo, "0");
				logger.error("拉取skuNO{}库存失败{}",skuNo,ex.getMessage());
				continue;
			}
		}
		return skuStock;
	}
	

	public static void main(String[] args) {
//		String skuNo="152790FCR000002-MIDBL#m";
//		String recordId=skuNo.substring(0, skuNo.lastIndexOf("#"));
//		String scalarNo=skuNo.substring(skuNo.lastIndexOf("#")+1);
//		String productId=skuNo.substring(0, skuNo.lastIndexOf("-"));
//		System.out.println(recordId+","+scalarNo+","+productId);
		UpdateStockService updateStockService = new UpdateStockService();
		Collection<String> skuList= new ArrayList<>();
		skuList.add("151400FPH000002-NERO#1");
		skuList.add("151400FPH000003-BLMAR#1");
		skuList.add("152008ACR000001-1566#6");

		try {
			updateStockService.grabStock(skuList);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}


}

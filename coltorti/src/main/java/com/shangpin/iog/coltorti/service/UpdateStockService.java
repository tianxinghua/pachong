/**
 * 
 */
package com.shangpin.iog.coltorti.service;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.coltorti.dto.ColtortiProduct;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import org.springframework.stereotype.Component;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月9日
 */
@Component("updateStockService")
public class UpdateStockService extends AbsUpdateProductStock{
	static Logger logger = LoggerFactory.getLogger(UpdateStockService.class);
	private static ReentrantLock lock = new ReentrantLock();


	public Map<String, String> grabStock(Collection<String> skuNos) throws ServiceException{


		Map<String, String> skuStock= new HashMap<>(skuNos.size());
		Set<String> productIdSet=new HashSet<>();
		String productId=null,recordId=null,scalarNo=null;

		String price ="";
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
			Map<String, Map<String, Integer>> stoks=null;
			List<ColtortiProduct>  coltortiProductList = null;
			Map<String,String>  recordMap = null;
			try{
				if(!productIdSet.contains(productId)){
					//获取库存
					stoks=ColtortiStockService.getStock(productId, null);
					//获取价格
					try {
						coltortiProductList =ColtortiProductService.findSingleProduct(productId,null);
						if(null!=coltortiProductList&&coltortiProductList.size()>0){
                            recordMap = new HashMap<>();
                            for(ColtortiProduct product:coltortiProductList){
                                recordMap.put(product.getSkuId(),product.getPrice().toString());
                            }
                        }
					} catch (Exception e) {  //发生错误  不做处理
						e.printStackTrace();
					}

					if(stoks!=null && stoks.size()>0){
						Set<Map.Entry<String, Map<String, Integer>>> entrySet = stoks.entrySet();
						for (Map.Entry<String, Map<String, Integer>> entry : entrySet) {
							String rcdId=entry.getKey();
							Map<String,Integer> kvs=entry.getValue();
							Set<Map.Entry<String, Integer>> kvEntrySet = kvs.entrySet();
							price = "";
							for (Map.Entry<String, Integer> entry2 : kvEntrySet) {
								String scalar=entry2.getKey();
								Integer qnt=entry2.getValue();
								int quantity=qnt==null?0:qnt;

								if(sopMarketPriceMap.containsKey(skuNo)){  //SOP 上的价格
									if(null!=recordMap&&recordMap.containsKey(recordId)){    //拉取的新价格

										if(StringUtils.isNotBlank(sopMarketPriceMap.get(skuNo))&&StringUtils.isNotBlank(recordMap.get(recordId))){
											if(!sopMarketPriceMap.get(skuNo).equals(recordMap.get(recordId))){
												// 格式  为 新价格 老价格

												price="|"+ recordMap.get(recordId) +","+ sopMarketPriceMap.get(skuNo);
												System.out.println("price change = " + skuNo +"　" + price );
											}
										}

									}
								}
								skuStock.put(rcdId+"#"+scalar, String.valueOf(quantity)+price);
							}
						}
						productIdSet.add(productId);
					}else{
						skuStock.put(skuNo, "0");
					}

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
		skuList.add("151431DAB000003-303#3");

		try {
			updateStockService.grabStock(skuList);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}


}

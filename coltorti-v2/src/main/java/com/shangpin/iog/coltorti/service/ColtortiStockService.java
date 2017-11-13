/**
 *
 */
package com.shangpin.iog.coltorti.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.coltorti.conf.ApiURL;
import com.shangpin.iog.coltorti.dto.ColtortiSkuDto;
import com.shangpin.iog.coltorti.dto.ColtortiStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

/**
 * @description
 * @author 陈小峰
 * <br/>2015年6月9日
 */
public class ColtortiStockService {
	static Logger logger =LoggerFactory.getLogger(ColtortiStockService.class);
	/**
	 * 返回 产品的记录id：sku集合
	 * @param productId 货号
	 * @param recordId 未拆分尺码前的spu no，如果是拆分后的一般是skuId的'#'号前面部分<br/>
	 * 务必排除该‘#’号及后面部分
	 * @return 
	 * @throws ServiceException 没有数据，或者token过期了
	 */
	public static Map<String, List<ColtortiSkuDto>> getStock(String productId,String recordId) throws ServiceException{
		Map<String,String> param=ColtortiUtil.getCommonParam(0,0);
		if(productId!=null) param.put("product_id", productId);
		if(recordId!=null) param.put("id", recordId);
		String body=HttpUtil45.get(ColtortiUtil.paramGetUrl(ApiURL.STOCK,param),new OutTimeConfig(1000*10*10,1000*10*10,1000*10*10),null);
		ColtortiUtil.check(body);
		Gson gson = new Gson();
		Map<String,ColtortiStock> mp=null;
		try{
			mp=gson.fromJson(body, new TypeToken<Map<String,ColtortiStock>>(){}.getType());
		}catch(Exception e){
			logger.error("http请求结果转换库存失败，productId:{}返回数据{}",productId,body);
		}
		Map<String,List<ColtortiSkuDto>> rtnScalar=null;
		if(mp!=null && mp.size()>0){
			/**
			 * 是产品id：sku集合
			 */
			rtnScalar=new HashMap<String, List<ColtortiSkuDto>>();
			Iterator<Entry<String, ColtortiStock>> it=mp.entrySet().iterator();
			while(it.hasNext()){//不同产品 ，如果有recordId那么有一个
				Entry<String, ColtortiStock> etry=it.next();
				String supplierSpuNo=etry.getKey();//唯一记录id
				ColtortiStock stock=etry.getValue();//每个仓库的库存
				if(null == stock.getTotal() && 0 == stock.getTotal())
					continue;
				Map<String,Map<String,Integer>> sizes=stock.getSizes();
				if(sizes!=null && sizes.size()>0){
					List<ColtortiSkuDto> skuDtos = new ArrayList<ColtortiSkuDto>();
					for(Entry<String, Map<String, Integer>> entry : sizes.entrySet()){
						String sizeId= entry.getKey();
						for(Entry<String,Integer> entry1 : entry.getValue().entrySet()){
							ColtortiSkuDto skuDto = new ColtortiSkuDto();
							skuDto.setSizeId(sizeId);
							skuDto.setSize(entry1.getKey());
							skuDto.setStock(entry1.getValue());
							skuDtos.add(skuDto);
						}
					}
					rtnScalar.put(supplierSpuNo, skuDtos);
				}
			}
		}

		return rtnScalar;
	}
}

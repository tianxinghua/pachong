/**
 *
 */
package com.shangpin.iog.coltorti.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.json.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.coltorti.conf.ApiURL;
import com.shangpin.iog.coltorti.dto.ColtortiStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;

/**
 * @description
 * @author 陈小峰
 * <br/>2015年6月9日
 */
public class ColtortiStockService {
	static Logger logger =LoggerFactory.getLogger(ColtortiStockService.class);
	/**
	 * 返回 产品的记录id：尺码：数量<br/>库存信息
	 * @param productId 货号，相当spu id<br/>一个product id包含多个sku<br/>
	 * @param recordId 未拆分尺码前的skuId，如果是拆分后的一般是skuId的'#'号前面部分<br/>
	 * 务必排除该‘#’号及后面部分
	 * @return 返回 每个sku不同尺码对应的数量；<br/>产品的记录id：（尺码：数量）<br/> 
	 * @throws ServiceException 没有数据，或者token过期了
	 */
	public static Map<String, Map<String, Integer>> getStock(String productId,String recordId) throws ServiceException{
		Map<String,String> param=ColtortiUtil.getCommonParam(0,0);
		if(productId!=null) param.put("product_id", productId);
		if(recordId!=null) param.put("id", recordId);
		//Date startDate= new  Date();
		//logger.error("{}@{},http start+++++++",Thread.currentThread().getName(),productId);
		String body=HttpUtil45.get(ColtortiUtil.paramGetUrl(ApiURL.STOCK,param),new OutTimeConfig(10000,10000,10000),null);

		//logger.error("{}@{},http end---------",Thread.currentThread().getName(),productId);
		/*System.out.println("productId =" + productId);
		System.out.println("recordId =" + recordId);
		System.out.println("  抓取库存需要的时间 =" + String.valueOf(System.currentTimeMillis()-startDate.getTime()));*/
//		logger.info("request stock result:\r\n"+body);
//		                          System.out.println("request stock result:\r\n"+body);
		ColtortiUtil.check(body);
		Gson gson = new Gson();
		Map<String,List<ColtortiStock>> mp=null;
		try{
			mp=gson.fromJson(body, new TypeToken<Map<String,List<ColtortiStock>>>(){}.getType());
		}catch(Exception e){
			logger.error("http请求结果转换库存失败，productId:{}返回数据{}",productId,body);
		}
		Map<String,Map<String,Integer>> rtnScalar=null;
		if(mp!=null && mp.size()>0){
			Iterator<Entry<String, List<ColtortiStock>>> it=mp.entrySet().iterator();
			//是产品id：（尺码：数量）
			rtnScalar=new HashMap<String, Map<String,Integer>>();
			while(it.hasNext()){//不同产品 ，如果有recordId那么有一个
				Entry<String, List<ColtortiStock>> etry=it.next();
				String skuid=etry.getKey();//唯一记录id（可能是skuid，可能不是）
				List<ColtortiStock> stock=etry.getValue();//每个仓库的库存
				Map<String, Integer> scalarDetail=rtnScalar.get(skuid);
				if(scalarDetail==null){
					scalarDetail=new HashMap<String, Integer>();
					rtnScalar.put(skuid, scalarDetail);
				}
				for (ColtortiStock s : stock) {//不同仓库
					if("0".equals(s.getTotal()) || StringUtils.isBlank(s.getTotal()))
						continue;
					Map<String,Map<String,String>> scalars=s.getScalars();
					if(scalars!=null && scalars.size()>0){
						Set<String> ks=scalars.keySet();
						for (String unk : ks) {
							Map<String,String> sizes=scalars.get(unk);
							if(sizes!=null){
								Set<String> sks=sizes.keySet();
								for (String sk : sks) {//不同尺码
									String sizeNum=sizes.get(sk);
									Integer sizeValue=scalarDetail.get(unk);
									if(sizeValue==null){
										sizeValue=0;
									}
									Integer v=Integer.parseInt(sizeNum);
									Integer newNum=(v<0?0:v)+sizeValue;
									scalarDetail.put(unk, newNum);
								}
							}
						}
					}
				}
			}
		}

		return rtnScalar;
	}
	public static void main(String[] args) throws ServiceException {
		String[] x={"151431DAB000003-303"
		};
		for (String record : x) {
			try{
				Map<String, Map<String, Integer>> stok = getStock("162481FBS000020",null);
				System.out.println(new Gson().toJson(stok));
			}catch(Exception e){
				if(e instanceof ServiceException){
					if(ColtortiUtil.isTokenExpire((ServiceException) e)){
						try {
							ColtortiTokenService.initToken();
						} catch (ServiceException e1) {
							logger.error("拉库存更新token错误",e1);
						}
					}
				}
			}

		}
	}
}

/**
 * 
 */
package com.shangpin.iog.coach.service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.coach.conf.ApiURL;
import com.shangpin.iog.coach.convert.ProductConvert;
import com.shangpin.iog.coach.dto.Attributes;
import com.shangpin.iog.coach.dto.Product;
import com.shangpin.iog.coach.dto.Stock;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月4日
 */
public class ProductService {
	static Logger logger =LoggerFactory.getLogger(ProductService.class);
	static final Map<String,Field[]> classField = new HashMap<>();
	/**
	 * 
	 * @param page
	 * @param size
	 * @return
	 * @throws ServiceException
	 */
	public static String requestAttribute(int page,int size) throws ServiceException{
		Map<String,String> param = CommonUtil.getCommonParam(page,size);
		String url=CommonUtil.paramGetUrl(ApiURL.ATTRIBUTES,param);
		String body=HttpUtils.get(url);
		CommonUtil.check(body);
		Gson gson = new Gson();
		logger.info("request attribute result:\r\n"+body);
		Map<String,Attributes> attriMap=gson.fromJson(body, new TypeToken<Map<String,Attributes>>(){}.getType());
		logger.info("getAttribute result:\r\n"+gson.toJson(attriMap));
		return body;
	}
	/**
	 * 获取产品，但无库存
	 * @param page
	 * @param size
	 * @param productId
	 * @return
	 * @throws ServiceException
	 */
	public static List<Product> findProduct(int page,int size,String productId) throws ServiceException{
		Map<String,String> param=CommonUtil.getCommonParam(page,size);
		param.put("fields", "id,name,product_id,variant,description,price,scalars,"
				+ "ms5_group,ms5_subgroup,ms5_category,brand,season,images,"
				+ "macro_category,group,family,line,subgroup,category,attributes,updated_at");
		if(productId!=null) param.put("product_id", productId);
		param.put("since_updated_at", "2015-06-01");
		param.put("until_updated_at", DateTimeUtil.getShortCurrentDate());
		String body=HttpUtils.get(CommonUtil.paramGetUrl(ApiURL.PRODUCT,param));
		CommonUtil.check(body);
		JsonObject jo =new JsonParser().parse(body).getAsJsonObject();
		logger.info("request product result:\r\n"+body);
		Set<Entry<String,JsonElement>> ks=jo.entrySet();
		List<Product> pros = new ArrayList<>(ks.size()); 
		for (Entry<String, JsonElement> entry : ks) {
			JsonElement je=entry.getValue();
			JsonObject jop=je.getAsJsonObject();
			try {
				Product p=toObj(jop,Product.class);
				p.setSkuId(entry.getKey());
				pros.add(p);
			} catch (InstantiationException | IllegalAccessException e) {
				logger.warn("convert product fail Json："+jop.toString());
			}
		}
		//logger.info(new Gson().toJson(pros));
		return pros;
	}
	/**
	 * 根据产品的尺码，拆分成sku，并返回一个新的product集合<br/>
	 * 新的product 的sku是,原sku+#+尺码编号，如：151828DGN000001-MULTI#M<br/>
	 * 新的product会加上库存量
	 * @param pros
	 * @throws ServiceException
	 */
	public static List<Product> product2sku(List<Product> pros) throws ServiceException{
		List<Product> newProducts = new ArrayList<>(pros.size());
		for (Iterator<Product> iterator = pros.iterator(); iterator.hasNext();) {
			Product prd = iterator.next();
			String pid=prd.getProductId();
			Map<String,String> scalars=prd.getScalars();
			Map<String,Map<String,Integer>> stocks=null;
			String scalarkey="";
			//如果只有一个尺码那么不用去取?
			if(scalars!=null && scalars.size()>0){
				stocks=getStock(pid,prd.getSkuId());
				scalarkey=scalars.entrySet().iterator().next().getValue();
			}
			if(stocks!=null && stocks.size()>0){
				Map<String, Integer> scalar=stocks.get(prd.getSkuId());
				if(scalar!=null){
					Set<String> smlx=scalar.keySet();
					for (String key : smlx) {
						Integer size=scalar.get(key);
						newProducts.add(convertProduct(prd, key,size));
					}
				}else{
					newProducts.add(convertProduct(prd, scalarkey,0));
				}
			}else{
				newProducts.add(convertProduct(prd, scalarkey,0));
			}
		}
		return newProducts;
	}
	/**
	 *  没有库存数据情况 默认为0
	 * @param prd
	 * @param scalarKey
	 * @param stock
	 * @return
	 */
	private static Product convertProduct(Product prd,String scalarKey,Integer stock) {
		Product newp = new Product();
		try {
			BeanUtils.copyProperties(newp,prd);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		newp.setStock(stock);
		newp.setSkuId(prd.getSkuId()+"-"+scalarKey);
		newp.setScalars(null);
		return newp;
	}
	
	private static <T> T toObj(JsonObject jo,Class<T> cls) throws InstantiationException, IllegalAccessException{
		Set<Entry<String,JsonElement>> ks=jo.entrySet();
		T p=cls.newInstance();
		Gson g = new Gson();
		for (Entry<String, JsonElement> entry : ks) {
			JsonElement je=entry.getValue();
			String key = entry.getKey();
			if(je.isJsonArray()){
				JsonArray ja=je.getAsJsonArray();
				if(ja.size()==0)
					continue;
			}
			String pro=remove_(key);
			Class<?> type = getProType(cls,pro);
			if(type!=null){
				try {
					//Object obj=JSONObject.parseObject(js1, type);
					//TypeToken.get(TypeToken.get(type).getType());
					Object obj=g.fromJson(je, TypeToken.get(type).getType());
					BeanUtils.setProperty(p, pro,obj);
				} catch (JsonSyntaxException | InvocationTargetException e) {
					logger.error("set product pro fail:"+je.toString());
				}
			}
		}
		return p;
	}
	
	/**
	 * 将中划线方式转为骆驼命名规则
	 * @param key
	 * @return
	 */
	private static String remove_(String key) {
		String regex="_\\S";
		Pattern pat = Pattern.compile(regex);  
		StringBuffer sb = new StringBuffer();
		String tmpStr=key;
		if(tmpStr.startsWith("_"))
			tmpStr=tmpStr.substring(1);
		if(tmpStr.endsWith("_"))
			tmpStr=tmpStr.substring(0, tmpStr.length()-1);
		Matcher matcher = pat.matcher(tmpStr);
		int start=0;
		while (matcher.find()) {
			int s=matcher.start();int e=matcher.end();
			String temp = tmpStr.substring(s+1,e);//.toUpperCase();
			sb.append(tmpStr.substring(start, s)).append(temp.toUpperCase());
		    start=e;
		}
		sb.append(tmpStr.substring(start));
		return sb.toString();
	}
	
	/**
	 * @param class1
	 * @param pro
	 * @return
	 */
	private static Class<?> getProType(Class<?> class1, String pro) {
		
		try {
			Field[] fds=classField.get(class1.getName());
			if(fds==null){
				fds=class1.getDeclaredFields();
				classField.put(class1.getName(), fds);
			}
			
			for (Field m : fds) {				
				if(m.getName().equals(pro) &&
						!(Modifier.isStatic(m.getModifiers()) &&
								Modifier.isFinal(m.getModifiers())))
					return m.getType();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 返回 产品的记录id：（尺码：数量）<br/>库存信息
	 * @param productId 货好，相当spu id<br/>一个product id包含多个sku<br/>
	 * @return 返回 产品的记录id：（尺码：数量）<br/> 
	 * @throws ServiceException
	 */
	public static Map<String, Map<String, Integer>> getStock(String productId,String recordId) throws ServiceException{
		Map<String,String> param=CommonUtil.getCommonParam(1,10);
		if(productId!=null) param.put("product_id", productId);
		if(recordId!=null) param.put("id", recordId);
		String body=HttpUtils.get(CommonUtil.paramGetUrl(ApiURL.STOCK,param));
		try{
			CommonUtil.check(body);
		}catch(ServiceException e){
			if(CommonUtil.isNotResultError(e)){
				return null;
			}
		}
		//logger.info("request stock result:\r\n"+body);
		Gson gson = new Gson();
		Map<String,List<Stock>> mp=gson.fromJson(body, new TypeToken<Map<String,List<Stock>>>(){}.getType());
		Map<String,Map<String,Integer>> rtnScalar=null;
		if(mp!=null && mp.size()>0){
			Set<Map.Entry<String,List<Stock>>> eset=mp.entrySet();
			Iterator<Entry<String, List<Stock>>> it=eset.iterator();
			//是产品id：（尺码：数量）
			rtnScalar=new HashMap<String, Map<String,Integer>>();
			while(it.hasNext()){//不同产品
				Entry<String, List<Stock>> etry=it.next();
				String skuid=etry.getKey();//唯一记录id（可能是skuid，可能不是）
				List<Stock> stock=etry.getValue();//每个仓库的库存
				Map<String, Integer> scalarDetail=rtnScalar.get(skuid);
				if(scalarDetail==null){
					scalarDetail=new HashMap<String, Integer>();
					rtnScalar.put(skuid, scalarDetail);
				}
				for (Stock s : stock) {//不同仓库
					Map<String,Map<String,String>> scalars=s.getScalars();
					if(scalars!=null && scalars.size()>0){
						Set<String> ks=scalars.keySet();
						for (String unk : ks) {
							Map<String,String> sizes=scalars.get(unk);
							if(sizes!=null){
								Set<String> sks=sizes.keySet();
								for (String sk : sks) {//不同尺码
									String sizeNum=sizes.get(sk);
									Integer sizeValue=scalarDetail.get(sk);
									if(sizeValue==null){
										sizeValue=0;
									}
									Integer v=Integer.parseInt(sizeNum);
									Integer newNum=(v<0?0:v)+sizeValue;
									scalarDetail.put(sk, newNum);
								}
							}							
						}
					}
				}
			}
		}
		if(rtnScalar!=null){
			logger.info("new stocks result："+gson.toJson(rtnScalar));
		}
		return rtnScalar;
	}
	
	public static void main(String[] args) throws ServiceException, IOException {
		//requestAttribute(1, 100);
		//findProduct(1,40,"152790AAV000001");
		//getStock("152790AAV000001","152790AAV000001-PINxRU");//"152790FCR000005-SADMA"
		List<Product> ps=product2sku(findProduct(1, 4, null));
		logger.info("-----new products -----\r\n"+new Gson().toJson(ps));
		List<SkuDTO> skus=new ArrayList<>(ps.size());
		List<SpuDTO> spus=new ArrayList<>(ps.size());
		Map<String,Set<ProductPictureDTO>> mpccs=new HashMap<String, Set<ProductPictureDTO>>();
		for (Product product : ps) {
			SkuDTO sk=ProductConvert.product2sku(product);
			SpuDTO spu = ProductConvert.product2spu(product);
			skus.add(sk);
			spus.add(spu);
			Set<ProductPictureDTO> ppcs=ProductConvert.productPic(product);
			mpccs.put(product.getSkuId(), ppcs);
		}
		logger.info("-----after convert skus-----\r\n"+new Gson().toJson(skus));
		logger.info("-----after convert spus-----\r\n"+new Gson().toJson(spus));
		logger.info("-----after convert image-----\r\n"+new Gson().toJson(mpccs));
		System.in.read();
	}
	
	
}

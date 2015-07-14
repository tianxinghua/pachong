/**
 * 
 */
package com.shangpin.iog.coltorti.service;

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
import com.shangpin.iog.coltorti.conf.ApiURL;
import com.shangpin.iog.coltorti.dto.ColtortiAttributes;
import com.shangpin.iog.coltorti.dto.ColtortiProduct;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月4日
 */
public class ColtortiProductService{
	static Logger logger =LoggerFactory.getLogger(ColtortiProductService.class);
	static final Map<String,Field[]> classField = new HashMap<>();
	private static final int defaultSize=500; 
	private static Map<String,Map<String,Map<String,Integer>>> stocks=new HashMap<>();
	static int cnt=0;
	/**
	 * 
	 * @param page
	 * @param size
	 * @return
	 * @throws ServiceException
	 */
	public static String requestAttribute(int page,int size) throws ServiceException{
		Map<String,String> param = ColtortiUtil.getCommonParam(page,size);
		String url=ColtortiUtil.paramGetUrl(ApiURL.ATTRIBUTES,param);
		String body=HttpUtil45.get(url,null,null);
		ColtortiUtil.check(body);
		Gson gson = new Gson();
		Map<String,ColtortiAttributes> attriMap=gson.fromJson(body, new TypeToken<Map<String,ColtortiAttributes>>(){}.getType());
		logger.info("getAttribute result:\r\n"+gson.toJson(attriMap));
		return body;
	}
	/**
	 * 获取供应商产品数据
	 * @param start 供应商数据更新开始时间段
	 * @param end 供应商数据更新结束时间段
	 * @return
	 * @throws ServiceException
	 */
	public static List<ColtortiProduct> findProduct(String start,String end) throws ServiceException{
		return hasMore(start, end, null, null);
	}
	 
	/**
	 * 获取供应商产品数据
	 * @param productId 相当spuId，一个productId下有多个产品
	 * @return
	 * @throws ServiceException
	 */
	public static List<ColtortiProduct> findProductByProductId(String productId) throws ServiceException{
		return hasMore(null, null, productId, null);
	}
	/**
	 * 获取记录id指定的唯一产品id
	 * @param recordId 相当skuid，供应商的产品记录id
	 * @return 最多只有一个产品
	 * @throws ServiceException
	 */
	public static List<ColtortiProduct> findProductBySkuId(String recordId) throws ServiceException{
		return hasMore(null,null,null,recordId);
	}
	/**
	 * 分页默认一次一页
	 * @param dateStart
	 * @param dateEnd
	 * @param productId
	 * @param recordId
	 * @return
	 * @throws ServiceException
	 */
	private static List<ColtortiProduct> hasMore(String dateStart,
			String dateEnd,String productId,String recordId) throws ServiceException{
		List<ColtortiProduct> rs= new ArrayList<>(defaultSize);
		boolean hasMore=true;
		int pg=1;
		while(hasMore){
			List<ColtortiProduct> r1=null;
			try{
					r1=findProduct(pg,defaultSize,dateStart,dateEnd,productId,recordId);
			}catch(ServiceException e){
				if(ColtortiUtil.isTokenExpire(e)){//如果是过期的话重新获取token
					logger.warn(e.getMessage());
					ColtortiTokenService.initToken();
					continue;
				}else if(ColtortiUtil.isNoResultError(e)){
					hasMore=false;
					continue;
				}else
					throw e;
			}
			rs.addAll(r1);
			if(r1.size()<defaultSize)
				hasMore=false;
			pg++;
		}
		stocks.clear();
		return rs;
	}
	
	/**
	 * 获取产品，但无库存
	 * @param page 页码 无则为0
	 * @param size 页大小 无则为0
	 * @param dateStart 供应商数据更新开始时间段
	 * @param dateEnd 供应商数据更新结束时间段
	 * @param productId 相当spuid
	 * @param recordId 相当skuid
	 * @return
	 * @throws ServiceException
	 */
	public static List<ColtortiProduct> findProduct(int page,int size,String dateStart,
			String dateEnd,String productId,String recordId) throws ServiceException{
		Map<String,String> param=ColtortiUtil.getCommonParam(page,size);
		param.put("fields", "id,name,product_id,variant,description,price,scalars,"
				+ "ms5_group,ms5_subgroup,ms5_category,brand,season,images,alternative_ids,"
				+ "macro_category,group,family,line,subgroup,category,attributes,updated_at");
		if(productId!=null) param.put("product_id", productId);
		if(dateStart!=null)param.put("since_updated_at", dateStart);
		if(dateEnd!=null)param.put("until_updated_at", dateEnd);
		if(recordId!=null)param.put("id", recordId);
		String body=HttpUtil45.get(ColtortiUtil.paramGetUrl(ApiURL.PRODUCT,param),null,null);
		//logger.error(body);
		ColtortiUtil.check(body);
		JsonObject jo =new JsonParser().parse(body).getAsJsonObject();
		Set<Entry<String,JsonElement>> ks=jo.entrySet();
		List<ColtortiProduct> pros = new ArrayList<>(ks.size()); 
		for (Entry<String, JsonElement> entry : ks) {
			JsonElement je=entry.getValue();
			try {
				JsonObject jop=je.getAsJsonObject();
				ColtortiProduct p=toObj(jop,ColtortiProduct.class);
				p.setSkuId(entry.getKey());//skuId就是recordId暂时的..#@see convertProduct
				//获取库存
				setStock(entry.getKey(), p);
				pros.add(p);
			} catch (InstantiationException | IllegalAccessException e) {
				logger.warn("convert product fail Json："+je.toString());
			}
		}
		return pros;
	}
	/**
	 * @param entry
	 * @param productId
	 * @throws ServiceException
	 */
	private static void setStock(String recordId,ColtortiProduct p) {
		try{
			String productId=p.getProductId();
			Map<String,Integer> stockMap =null;
			if(!stocks.containsKey(productId)){
				Map<String, Map<String, Integer>> tmap = ColtortiStockService.getStock(productId, null);
				stockMap= tmap.get(recordId);
				stocks.put(productId, tmap);
				cnt++;
			}else{
				stockMap=stocks.get(productId).get(recordId);
System.out.println(productId+"存在,不存在的数:"+cnt);				
			}
			p.setSizeStockMap(stockMap);
		}catch(Exception e){
			if(e instanceof ServiceException){
				if(ColtortiUtil.isTokenExpire((ServiceException) e)){
					try {
						ColtortiTokenService.initToken();
					} catch (ServiceException e1) {
						logger.error("拉库存更新token错误",e1);
					}
				}
			}else{
				logger.error("拉库存错误",e);				
			}
		}
	}
	
	/**
	 * 根据产品的尺码，拆分成sku，并返回一个新的product集合<br/>
	 * 新的product 的sku是,原sku+#+尺码编号，如：151828DGN000001-MULTI#M<br/>
	 * 新的product会加上库存量
	 * @param pros
	 * @throws ServiceException
	 */
	public static List<ColtortiProduct> divideSku4Size(List<ColtortiProduct> pros) throws ServiceException{
		List<ColtortiProduct> newProducts = new ArrayList<>(pros.size());
		for (Iterator<ColtortiProduct> iterator = pros.iterator(); iterator.hasNext();) {
			ColtortiProduct prd = iterator.next();
			Map<String,String> scalars=prd.getScalars();
			if(scalars!=null && scalars.size()>0){
				//stocks=null;//getStock(pid,prd.getSkuId());
				Set<String> scks=scalars.keySet();
				for (String sck : scks) {
					String sml=scalars.get(sck);//尺码字符
					//TODO 尺码key;sck;
					ColtortiProduct pt = convertProduct(prd,sck);
					pt.setStock(prd.getSizeStockMap()==null?0:prd.getSizeStockMap().get(sck));
					pt.setSizeKeyValue(sck+"#"+sml);
					newProducts.add(pt);
				}
			}
			
		}
		return newProducts;
	}
	/**
	 *  没有库存数据情况 默认为0
	 * @param prd
	 * @param scalarCode
	 * @param stock
	 * @return
	 */
	private static ColtortiProduct convertProduct(ColtortiProduct prd,String scalarCode) {
		ColtortiProduct newp = new ColtortiProduct();
		try {
			BeanUtils.copyProperties(newp,prd);
		} catch (IllegalAccessException | InvocationTargetException e) {
			return null;
		}
		newp.setSkuId(prd.getSkuId()+"#"+scalarCode);
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
	/*
	 * 返回 产品的记录id：尺码：数量<br/>库存信息
	 * @param productId 货号，相当spu id<br/>一个product id包含多个sku<br/>
	 * @param recordId 未拆分尺码前的skuId，如果是拆分后的一般是skuId的'#'号前面部分<br/>
	 * 务必排除该‘#’号及后面部分
	 * @return 返回 每个sku不同尺码对应的数量；<br/>产品的记录id：（尺码：数量）<br/> 
	 * @throws ServiceException
	 *
	public static Map<String, Map<String, Integer>> getStock(String productId,String recordId) throws ServiceException{
		Map<String,String> param=ColtortiUtil.getCommonParam(0,0);
		if(productId!=null) param.put("product_id", productId);
		if(recordId!=null) param.put("id", recordId);
		String body=HttpUtils.get(ColtortiUtil.paramGetUrl(ApiURL.STOCK,param));
		try{
			ColtortiUtil.check(body);
		}catch(ServiceException e){
			if(ColtortiUtil.isNoResultError(e)){
				return null;
			}
		}
		//logger.info("request stock result:\r\n"+body);
		Gson gson = new Gson();
		Map<String,List<ColtortiStock>> mp=gson.fromJson(body, new TypeToken<Map<String,List<ColtortiStock>>>(){}.getType());
		Map<String,Map<String,Integer>> rtnScalar=null;
		if(mp!=null && mp.size()>0){
			Set<Map.Entry<String,List<ColtortiStock>>> eset=mp.entrySet();
			Iterator<Entry<String, List<ColtortiStock>>> it=eset.iterator();
			//是产品id：（尺码：数量）
			rtnScalar=new HashMap<String, Map<String,Integer>>();
			while(it.hasNext()){//不同产品
				Entry<String, List<ColtortiStock>> etry=it.next();
				String skuid=etry.getKey();//唯一记录id（可能是skuid，可能不是）
				List<ColtortiStock> stock=etry.getValue();//每个仓库的库存
				Map<String, Integer> scalarDetail=rtnScalar.get(skuid);
				if(scalarDetail==null){
					scalarDetail=new HashMap<String, Integer>();
					rtnScalar.put(skuid, scalarDetail);
				}
				for (ColtortiStock s : stock) {//不同仓库
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
	}*/
	
	/*public static void main(String[] args) throws ServiceException, IOException {
		//requestAttribute(1, 100);
		//findProduct(1,40,"152790AAV000001");
		//getStock("152790AAV000001","152790AAV000001-PINxRU");//"152790FCR000005-SADMA"
		List<ColtortiProduct> ps=divideSku4Size(findProductByProductId(null));
		logger.info("-----new products -----\r\n"+new Gson().toJson(ps));
		List<SkuDTO> skus=new ArrayList<>(ps.size());
		List<SpuDTO> spus=new ArrayList<>(ps.size());
		Map<String,Set<ProductPictureDTO>> mpccs=new HashMap<String, Set<ProductPictureDTO>>();
		for (ColtortiProduct product : ps) {
			SkuDTO sk=ColtortiProductConvert.product2sku(product);
			SpuDTO spu = ColtortiProductConvert.product2spu(product);
			skus.add(sk);
			spus.add(spu);
			Set<ProductPictureDTO> ppcs=ColtortiProductConvert.productPic(product);
			mpccs.put(product.getSkuId(), ppcs);
		}
		logger.info("-----after convert skus-----\r\n"+new Gson().toJson(skus));
		logger.info("-----after convert spus-----\r\n"+new Gson().toJson(spus));
		logger.info("-----after convert image-----\r\n"+new Gson().toJson(mpccs));
		System.in.read();
	}
	*/
	
}

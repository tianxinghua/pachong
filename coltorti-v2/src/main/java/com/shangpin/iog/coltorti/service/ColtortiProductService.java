/**
 * 
 */
package com.shangpin.iog.coltorti.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.coltorti.conf.ApiURL;
import com.shangpin.iog.coltorti.dto.ColtortiProduct;
import com.shangpin.iog.coltorti.dto.ColtortiSkuDto;
import com.shangpin.iog.coltorti.dto.attribute.DiscountDto;
import com.shangpin.iog.coltorti.service.attribute.AttributeService;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.product.AbsSaveProduct;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月4日
 */
@Component
public class ColtortiProductService extends AbsSaveProduct{
	
	public static int day;
	public static String supplierId = null;
	private static ResourceBundle bdl = null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		day = Integer.valueOf(bdl.getString("day"));
		
		supplierId = bdl.getString("supplierId");
	}
	static Logger logger =LoggerFactory.getLogger(ColtortiProductService.class);
	static final Map<String,Field[]> classField = new HashMap<>();
	private static final int defaultSize=500; 
	private static Map<String,Map<String,List<ColtortiSkuDto>>> stocks=new HashMap<>();
	static int cnt=0;
	
	private static Gson gson = new Gson();
	
	/**
	 * 
	 * @param page
	 * @param size
	 * @return
	 * @throws ServiceException
	 */
//	public static String requestAttribute(int page,int size) throws ServiceException{
//		Map<String,String> param = ColtortiUtil.getCommonParam(page,size);
//		String url=ColtortiUtil.paramGetUrl(ApiURL.ATTRIBUTES,param);
//		String body=HttpUtil45.get(url,null,null);
//		ColtortiUtil.check(body);
//		Gson gson = new Gson();
//		Map<String,ColtortiAttributes> attriMap=gson.fromJson(body, new TypeToken<Map<String,ColtortiAttributes>>(){}.getType());
//		logger.info("getAttribute result:\r\n"+gson.toJson(attriMap));
//		return body;
//	}
	/**
	 * 获取供应商产品数据
	 * @param start 供应商数据更新开始时间段
	 * @param end 供应商数据更新结束时间段
	 * @return
	 * @throws ServiceException
	 */
	public  List<ColtortiProduct> findProduct(String start,String end) throws Exception{
		return hasMore(start, end, null, null);
	}
	 
	public static void main(String[] args) {
		try {
			new ColtortiProductService().findProductByProductId("181716UIM000001");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 获取供应商产品数据
	 * @param productId 相当spuId，一个productId下有多个产品
	 * @return
	 * @throws ServiceException
	 */
	public  List<ColtortiProduct> findProductByProductId(String productId) throws Exception{
		return hasMore(null, null, productId, null);
	}
	/**
	 * 获取记录id指定的唯一产品id
	 * @param recordId 相当skuid，供应商的产品记录id
	 * @return 最多只有一个产品
	 * @throws ServiceException
	 */
	public  List<ColtortiProduct> findProductBySkuId(String recordId) throws Exception{
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
	private  List<ColtortiProduct> hasMore(String dateStart,
			String dateEnd,String productId,String recordId) throws Exception{
		List<ColtortiProduct> rs= new ArrayList<>();
		/**
		 * 获取所有季节信息
		 */
		Map<String, String> seasons = AttributeService.getAllSeasons();
		/**
		 * 获取所有品牌信息
		 */
		Map<String, String> brands = AttributeService.getAllBrands();
		/**
		 * 获取所有品类
		 */
		Map<String, String> categories = AttributeService.getAllCategories();
		/**
		 * 性别字典
		 */
		Map<String, String> genders = AttributeService.getAllGenders();
		boolean hasMore=true;
		int pg=1;
		while(hasMore){
			List<ColtortiProduct> r1=null;
			try{
				System.out.println("pg==============="+pg);
				r1=findProduct(seasons, brands, categories, genders, pg,defaultSize,dateStart,dateEnd,productId,recordId);
				System.out.println("r1.size==============="+r1.size()); 
			}catch(ServiceException e){
				if(ColtortiUtil.isTokenExpire(e)){//如果是过期的话重新获取token
					logger.warn(e.getMessage());
					try{
						ColtortiTokenService.initToken();
					}catch(Exception e1){
					}
					continue;
				}else if(ColtortiUtil.isNoResultError(e)){
//					hasMore=false;
					continue;

				}else{
					continue;
//					throw e;
				}

			}
			rs.addAll(r1);
			if(r1.size()<defaultSize)
				hasMore=false;
			pg++;
		}
		System.out.println("总的pg==============="+pg);
		
		stocks.clear();
		return rs;
	}
	
	/**
	 * 获取产品，但无库存
	 * @param seasons 季节字典
	 * @param brands 品牌字典
	 * @param categories 品类字典
	 * @param genders 性别字典
	 * @param page 页码 无则为0
	 * @param size 页大小 无则为0
	 * @param dateStart 供应商数据更新开始时间段
	 * @param dateEnd 供应商数据更新结束时间段
	 * @param productId 相当spuid
	 * @param recordId 相当skuid
	 * @return
	 * @throws ServiceException
	 */
	public  List<ColtortiProduct> findProduct(Map<String, String> seasons, Map<String, String> brands, Map<String, String> categories, Map<String, String> genders, int page,int size,String dateStart,
			String dateEnd,String productId,String recordId) throws Exception{
		Map<String,String> param=ColtortiUtil.getCommonParam(page,size);
		/*
		 * param.put("fields", "id,name,product_id,variant,description,price,discount_rate,scalars,"
				+ "ms5_group,ms5_subgroup,ms5_category,brand,season,images,alternative_ids,"
				+ "macro_category,group,family,line,subgroup,category,attributes,updated_at");
		 */
		if(productId!=null) param.put("product_id", productId);
		if(dateStart!=null)param.put("since_updated_at", dateStart);
		if(dateEnd!=null)param.put("until_updated_at", dateEnd);
		if(recordId!=null)param.put("id", recordId);
		logger.warn("拉去链接=========="+ColtortiUtil.paramGetUrl(ApiURL.PRODUCT,param)); 
		String body=HttpUtil45.get(ColtortiUtil.paramGetUrl(ApiURL.PRODUCT,param),new OutTimeConfig(1000*10*10,1000*10*10,1000*10*10),null);
		
		ColtortiUtil.check(body);
		JsonObject jo =new JsonParser().parse(body).getAsJsonObject();
		Set<Entry<String,JsonElement>> ks=jo.entrySet();
		List<ColtortiProduct> pros = new ArrayList<>(ks.size()); 
		for (Entry<String, JsonElement> entry : ks) {
			JsonElement je=entry.getValue();
			try {
				JsonObject jop=je.getAsJsonObject();
				ColtortiProduct p=toObj(jop,ColtortiProduct.class);
				pros.add(p);
				String supplierSpuNo = entry.getKey();
				p.setSupplierSpuNo(supplierSpuNo); 
				/**
				 * 查找库存
				 */
				setStock(supplierSpuNo, p);
				/**
				 * 设置季节名称、品牌名称、品类名称
				 */
				p.setSeasonName(seasons.get(p.getSeason_id()));
				p.setBrandName(brands.get(p.getBrand_id()));
				p.setCategoryName(categories.get(p.getGroup_id()));  
				p.setGenderName(genders.get(p.getFamily_id()));  
				/**
				 * 设置其他属性
				 */
				p.setAttributes(AttributeService.getAttributeBySpuNo(supplierSpuNo));
				DiscountDto dis = AttributeService.getDiscount(supplierSpuNo);
				if(dis!=null&&dis.getYour_price()!=null){
					try{
						BigDecimal yourPrice = new BigDecimal(dis.getYour_price());
						dis.setYour_price(yourPrice.divide(new BigDecimal(1.22),2, BigDecimal.ROUND_HALF_UP).toString());	
					}catch(Exception e){
						logger.info("价格错误："+e.getMessage());
						continue;
					}
				}
				p.setDiscount(dis);
				logger.warn("发送的数据============"+gson.toJson(p)); 
				supp.setData(gson.toJson(p));
				pushMessage(gson.toJson(supp));
				
			} catch (Exception e) {
				logger.warn("convert product fail Json："+je.toString());
			}
		}
		return pros;
	}
	/**
	 * @param recordId
	 * @param p
	 * @throws ServiceException
	 */
	private static void setStock(String recordId,ColtortiProduct p) {
		try{
			String productId=p.getProduct_id();
			List<ColtortiSkuDto> coltortiSkus =null;
			if(!stocks.containsKey(productId)){
				Map<String, List<ColtortiSkuDto>> tmap = ColtortiStockService.getStock(productId, null);
				coltortiSkus= tmap.get(recordId);
				stocks.put(productId, tmap);
				cnt++;
			}else{
				coltortiSkus=stocks.get(productId).get(recordId);
				System.out.println(productId+"存在,不存在的数:"+cnt);				
			}
			p.setColtortiSkus(coltortiSkus);
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
//			String pro=remove_(key);
			Class<?> type = getProType(cls,key);
			if(type!=null){
				try {
					Object obj=g.fromJson(je, TypeToken.get(type).getType());
					BeanUtils.setProperty(p, key,obj);
				} catch (JsonSyntaxException | InvocationTargetException e) {
					logger.error("set product pro fail:"+je.toString());
				}
			}
		}
		return p;
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
	 * 抓取数据
	 * @param dateStart 
	 * @param dateEnd
	 */
	public Map<String,Object> fetchProductAndSave(String dateStart,String dateEnd){
		try {
			logger.warn("抓取数据开始，开始时间：{},结束时间:{}",dateStart,dateEnd);
			System.out.println("抓取数据开始，开始时间：" + dateStart +" 结束时间:"+dateEnd);
			List<ColtortiProduct> coltorProds=findProduct(dateStart, dateEnd);
			logger.warn("抓取数据成功，抓取到{}条.",coltorProds.size());
			System.out.println("抓取数据成功，抓取到{}条."+coltorProds.size());
//			for(ColtortiProduct ColtortiProduct : coltorProds){
//				
//				Gson gson = new Gson();
//				supp.setData(gson.toJson(ColtortiProduct));
//				pushMessage(gson.toJson(supp));
//			}

		} catch (Exception e) {
			logger.error("抓取Coltorti数据失败。",e);
		}
		return null;
	}
	@Override
	public Map<String, Object> fetchProductAndSave() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

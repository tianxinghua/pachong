/**
 * 
 */
package com.shangpin.test;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shangpin.iog.coltorti.dto.ColtortiProduct;
import com.shangpin.iog.common.utils.DateTimeUtil;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月4日
 */

public class AnyTest {
	static String json="{ \"152790FPG000001-BLACK\": { \"name\": \"P.FOGLIO SIGNATURE CROSSGRAIN COIN WALLE\", \"product_id\": \"152790FPG000001\", \"variant\": { \"BLACK\": \"BLACK\" }, \"notes\": null, \"description\": \"100%LE\", \"price\": 0, \"scalars\": { \"1\": \"U\" }, \"alternative_ids\": [ \"152790FPG000001\", \"74922\" ], \"ms5_group\": { \"F\": \"ACCESSORI UOMO\" }, \"ms5_subgroup\": { \"FSLG\": \"PICCOLA PELLETTERIA\" }, \"ms5_category\": { \"PG\": \"PORTAFOGLI\" }, \"brand\": { \"BRA790\": \"COACH\" }, \"season\": { \"152\": \"2015 / AUTUNNO - INVERNO\" }, \"family\": { \"U\": \"UOMO\" }, \"line\": { \"ACC\": \"ACCESSORI\" }, \"images\": [ [], [] ], \"discount_category\": { \"SC-ACC\": \"ACEESSORI\" }, \"conai_category\": [], \"macro_category\": { \"0000000002\": \"UOMO\" }, \"group\": { \"UBORS\": \"BORSE UOMO\" }, \"subgroup\": [], \"category\": {\"yoyo\":\"你好\"}, \"attributes\": { \"000001\": { \"description\": \"Supercolore\", \"values\": { \"000010\": \"Nero\" } }, \"000003\": { \"description\": \"Supercomposizione\", \"values\": { \"000007\": \"Pelle\" } } }, \"updated_at\": \"2015-05-13T13:11:36Z\" }, \"152790FCR000005-SADMA\": { \"name\": \"CINTURA DRESS SCULTPED C TEXTURED LEATHE\", \"product_id\": \"152790FCR000005\", \"variant\": { \"SADMA\": \"SADDLE MAHOGANY\" }, \"notes\": null, \"description\": \"100%LE\", \"price\": 0, \"scalars\": { \"1\": \"U\" }, \"alternative_ids\": [ \"152790FCR000005\", \"66721\" ], \"ms5_group\": { \"F\": \"ACCESSORI UOMO\" }, \"ms5_subgroup\": { \"FSLG\": \"PICCOLA PELLETTERIA\" }, \"ms5_category\": { \"CR\": \"CINTURA\" }, \"brand\": { \"BRA790\": \"COACH\" }, \"season\": { \"152\": \"2015 / AUTUNNO - INVERNO\" }, \"family\": { \"U\": \"UOMO\" }, \"line\": { \"ACC\": \"ACCESSORI\" }, \"images\": [ [], [] ], \"discount_category\": { \"SC-ACC\": \"ACEESSORI\" }, \"conai_category\": [], \"macro_category\": { \"0000000002\": \"UOMO\" }, \"group\": { \"UACC\": \"ACCESSORI UOMO\" }, \"subgroup\": { \"SCINT\": \"CINTURE\" }, \"category\": [], \"attributes\": [], \"updated_at\": \"2015-05-13T13:11:36Z\" }, \"152790FCR000005-BLKBL\": { \"name\": \"CINTURA DRESS SCULTPED C TEXTURED LEATHE\", \"product_id\": \"152790FCR000005\", \"variant\": { \"BLKBL\": \"BLACK BLACK\" }, \"notes\": null, \"description\": \"100%LE\", \"price\": 0, \"scalars\": { \"1\": \"U\" }, \"alternative_ids\": [ \"152790FCR000005\", \"66721\" ], \"ms5_group\": { \"F\": \"ACCESSORI UOMO\" }, \"ms5_subgroup\": { \"FSLG\": \"PICCOLA PELLETTERIA\" }, \"ms5_category\": { \"CR\": \"CINTURA\" }, \"brand\": { \"BRA790\": \"COACH\" }, \"season\": { \"152\": \"2015 / AUTUNNO - INVERNO\" }, \"family\": { \"U\": \"UOMO\" }, \"line\": { \"ACC\": \"ACCESSORI\" }, \"images\": [ [], [] ], \"discount_category\": { \"SC-ACC\": \"ACEESSORI\" }, \"conai_category\": [], \"macro_category\": { \"0000000002\": \"UOMO\" }, \"group\": { \"UACC\": \"ACCESSORI UOMO\" }, \"subgroup\": { \"SCINT\": \"CINTURE\" }, \"category\": [], \"attributes\": { \"000001\": { \"description\": \"Supercolore\", \"values\": { \"000010\": \"Nero\" } }, \"000003\": { \"description\": \"Supercomposizione\", \"values\": { \"000007\": \"Pelle\" } } }, \"updated_at\": \"2015-05-13T13:11:36Z\" }, \"152790FCR000004-MAHMA\": { \"name\": \"CINTURA DRESS WESTON CROSSGRAIN SIGNATUR\", \"product_id\": \"152790FCR000004\", \"variant\": { \"MAHMA\": \"MAHOGANY MAHOGANY\" }, \"notes\": null, \"description\": \"100%LE\", \"price\": 0, \"scalars\": { \"1\": \"U\" }, \"alternative_ids\": [ \"152790FCR000004\", \"63333\" ], \"ms5_group\": { \"F\": \"ACCESSORI UOMO\" }, \"ms5_subgroup\": { \"FSLG\": \"PICCOLA PELLETTERIA\" }, \"ms5_category\": { \"CR\": \"CINTURA\" }, \"brand\": { \"BRA790\": \"COACH\" }, \"season\": { \"152\": \"2015 / AUTUNNO - INVERNO\" }, \"family\": { \"U\": \"UOMO\" }, \"line\": { \"ACC\": \"ACCESSORI\" }, \"images\": [ [], [] ], \"discount_category\": { \"SC-ACC\": \"ACEESSORI\" }, \"conai_category\": [], \"macro_category\": { \"0000000002\": \"UOMO\" }, \"group\": { \"UACC\": \"ACCESSORI UOMO\" }, \"subgroup\": { \"SCINT\": \"CINTURE\" }, \"category\": [], \"attributes\": [], \"updated_at\": \"2015-05-13T13:11:36Z\" }, \"152790FCR000004-BLKBL\": { \"name\": \"CINTURA DRESS WESTON CROSSGRAIN SIGNATUR\", \"product_id\": \"152790FCR000004\", \"variant\": { \"BLKBL\": \"BLACK BLACK\" }, \"notes\": null, \"description\": \"100%LE\", \"price\": 0, \"scalars\": { \"1\": \"U\" }, \"alternative_ids\": [ \"152790FCR000004\", \"63333\" ], \"ms5_group\": { \"F\": \"ACCESSORI UOMO\" }, \"ms5_subgroup\": { \"FSLG\": \"PICCOLA PELLETTERIA\" }, \"ms5_category\": { \"CR\": \"CINTURA\" }, \"brand\": { \"BRA790\": \"COACH\" }, \"season\": { \"152\": \"2015 / AUTUNNO - INVERNO\" }, \"family\": { \"U\": \"UOMO\" }, \"line\": { \"ACC\": \"ACCESSORI\" }, \"images\": [ [], [] ], \"discount_category\": { \"SC-ACC\": \"ACEESSORI\" }, \"conai_category\": [], \"macro_category\": { \"0000000002\": \"UOMO\" }, \"group\": { \"UACC\": \"ACCESSORI UOMO\" }, \"subgroup\": { \"SCINT\": \"CINTURE\" }, \"category\": [], \"attributes\": { \"000001\": { \"description\": \"Supercolore\", \"values\": { \"000010\": \"Nero\" } }, \"000003\": { \"description\": \"Supercomposizione\", \"values\": { \"000007\": \"Pelle\" } } }, \"updated_at\": \"2015-05-13T13:11:36Z\" }, \"152790FCR000003-CHARC\": { \"name\": \"CINTURA DRESS WESTON SIGNATURE CTS/REVER\", \"product_id\": \"152790FCR000003\", \"variant\": { \"CHARC\": \"CHARCOAL\" }, \"notes\": null, \"description\": \"100%LE\", \"price\": 0, \"scalars\": { \"1\": \"U\" }, \"alternative_ids\": [ \"152790FCR000003\", \"63265\" ], \"ms5_group\": { \"F\": \"ACCESSORI UOMO\" }, \"ms5_subgroup\": { \"FSLG\": \"PICCOLA PELLETTERIA\" }, \"ms5_category\": { \"CR\": \"CINTURA\" }, \"brand\": { \"BRA790\": \"COACH\" }, \"season\": { \"152\": \"2015 / AUTUNNO - INVERNO\" }, \"family\": { \"U\": \"UOMO\" }, \"line\": { \"ACC\": \"ACCESSORI\" }, \"images\": [ [], [] ], \"discount_category\": { \"SC-ACC\": \"ACEESSORI\" }, \"conai_category\": [], \"macro_category\": { \"0000000002\": \"UOMO\" }, \"group\": { \"UACC\": \"ACCESSORI UOMO\" }, \"subgroup\": { \"SCINT\": \"CINTURE\" }, \"category\": [], \"attributes\": [], \"updated_at\": \"2015-05-13T13:11:36Z\" }, \"152790FCR000002-MIDBL\": { \"name\": \"CINTURA DRESS WESTON CROSSGRAIN TEXTURED\", \"product_id\": \"152790FCR000002\", \"variant\": { \"MIDBL\": \"MIDNIGHT BLACK\" }, \"notes\": null, \"description\": \"100%LE\", \"price\": 0, \"scalars\": { \"1\": \"U\" }, \"alternative_ids\": [ \"152790FCR000002\", \"62772\" ], \"ms5_group\": { \"F\": \"ACCESSORI UOMO\" }, \"ms5_subgroup\": { \"FSLG\": \"PICCOLA PELLETTERIA\" }, \"ms5_category\": { \"CR\": \"CINTURA\" }, \"brand\": { \"BRA790\": \"COACH\" }, \"season\": { \"152\": \"2015 / AUTUNNO - INVERNO\" }, \"family\": { \"U\": \"UOMO\" }, \"line\": { \"ACC\": \"ACCESSORI\" }, \"images\": [ [], [] ], \"discount_category\": { \"SC-ACC\": \"ACEESSORI\" }, \"conai_category\": [], \"macro_category\": { \"0000000002\": \"UOMO\" }, \"group\": { \"UACC\": \"ACCESSORI UOMO\" }, \"subgroup\": { \"SCINT\": \"CINTURE\" }, \"category\": [], \"attributes\": [], \"updated_at\": \"2015-05-13T13:11:36Z\" }, \"152790FCR000002-BLMAH\": { \"name\": \"CINTURA DRESS WESTON CROSSGRAIN TEXTURED\", \"product_id\": \"152790FCR000002\", \"variant\": { \"BLMAH\": \"BLACK MAHOGANY\" }, \"notes\": null, \"description\": \"100%LE\", \"price\": 0, \"scalars\": { \"1\": \"U\" }, \"alternative_ids\": [ \"152790FCR000002\", \"62772\" ], \"ms5_group\": { \"F\": \"ACCESSORI UOMO\" }, \"ms5_subgroup\": { \"FSLG\": \"PICCOLA PELLETTERIA\" }, \"ms5_category\": { \"CR\": \"CINTURA\" }, \"brand\": { \"BRA790\": \"COACH\" }, \"season\": { \"152\": \"2015 / AUTUNNO - INVERNO\" }, \"family\": { \"U\": \"UOMO\" }, \"line\": { \"ACC\": \"ACCESSORI\" }, \"images\": [ [], [] ], \"discount_category\": { \"SC-ACC\": \"ACEESSORI\" }, \"conai_category\": [], \"macro_category\": { \"0000000002\": \"UOMO\" }, \"group\": { \"UACC\": \"ACCESSORI UOMO\" }, \"subgroup\": { \"SCINT\": \"CINTURE\" }, \"category\": [], \"attributes\": [], \"updated_at\": \"2015-05-13T13:11:36Z\" }, \"152790FCR000001-FABLK\": { \"name\": \"CINTURA BLEECKER CTS/REVERSIBILE BELT\", \"product_id\": \"152790FCR000001\", \"variant\": { \"FABLK\": \"FAWN BLACK\" }, \"notes\": null, \"description\": \"100%LE\", \"price\": 0, \"scalars\": { \"1\": \"U\" }, \"alternative_ids\": [ \"152790FCR000001\", \"69900\" ], \"ms5_group\": { \"F\": \"ACCESSORI UOMO\" }, \"ms5_subgroup\": { \"FSLG\": \"PICCOLA PELLETTERIA\" }, \"ms5_category\": { \"CR\": \"CINTURA\" }, \"brand\": { \"BRA790\": \"COACH\" }, \"season\": { \"152\": \"2015 / AUTUNNO - INVERNO\" }, \"family\": { \"U\": \"UOMO\" }, \"line\": { \"ACC\": \"ACCESSORI\" }, \"images\": [ [], [] ], \"discount_category\": { \"SC-ACC\": \"ACEESSORI\" }, \"conai_category\": [], \"macro_category\": { \"0000000002\": \"UOMO\" }, \"group\": { \"UACC\": \"ACCESSORI UOMO\" }, \"subgroup\": { \"SCINT\": \"CINTURE\" }, \"category\": [], \"attributes\": [], \"updated_at\": \"2015-05-13T13:11:36Z\" }, \"152790FBS000010-CHARC\": { \"name\": \"BRIEFCASE HUDSON BAG SIGNATURE\", \"product_id\": \"152790FBS000010\", \"variant\": { \"CHARC\": \"CHARCOAL\" }, \"notes\": null, \"description\": \"80%PVC 10%PL 10%CO\", \"price\": 0, \"scalars\": { \"1\": \"U\" }, \"alternative_ids\": [ \"152790FBS000010\", \"71711\" ], \"ms5_group\": { \"F\": \"ACCESSORI UOMO\" }, \"ms5_subgroup\": { \"FBOR\": \"PELLETTERIA\" }, \"ms5_category\": { \"BS\": \"BORSE\" }, \"brand\": { \"BRA790\": \"COACH\" }, \"season\": { \"152\": \"2015 / AUTUNNO - INVERNO\" }, \"family\": { \"U\": \"UOMO\" }, \"line\": { \"ACC\": \"ACCESSORI\" }, \"images\": [ [], [] ], \"discount_category\": { \"SC-ACC\": \"ACEESSORI\" }, \"conai_category\": [], \"macro_category\": { \"0000000002\": \"UOMO\" }, \"group\": { \"UBORS\": \"BORSE UOMO\" }, \"subgroup\": { \"SBORS\": \"TOTE\" }, \"category\": [], \"attributes\": [], \"updated_at\": \"2015-05-13T13:11:36Z\" } }";
	static final Map<String,Field[]> classField = new HashMap<>();
	@Test
	public void testXmlBean(){
		Date de=DateTimeUtil.convertDateFormat(new Date(), "yyyyMMdd");
		Date ds=DateUtils.addDays(de, -1);
		System.out.println(de+"|"+ds);
		String isoFmt="yyyy-MM-dd'T'HH:mm:ss'Z'";
		System.out.println(DateTimeUtil.convertFormat(de,isoFmt)); 
		System.out.println(DateTimeUtil.convertFormat(ds,isoFmt)); 
	}
	@Test
	public void testJson(){
		/*Gson gson = new Gson();
		try{
		Map<String,Product> mp=gson.fromJson(json, new TypeToken<Map<String,Product>>(){}.getType());
		System.out.println("-------------products:"+gson.toJson(mp));
		}catch(Exception e){
			e.printStackTrace();
		}*/
		JSONObject jo = JSONObject.fromObject(json);
		@SuppressWarnings("unchecked")
		Set<String> keys=jo.keySet();
		for (String key : keys) {
			JSONObject jpro=jo.getJSONObject(key);
			try{
				String cate=jpro.getString("category");
				System.out.println(cate);
				//JSONObject job=jpro.getJSONObject("category");
				//System.out.println("category Object:"+job.toString());
			}catch(Exception e){
				JSONArray ja=jpro.getJSONArray("category");
				System.out.println("category Array:"+ja.toString());
			}
		}
	}
	
	@Test
	public void testGson() throws InstantiationException, IllegalAccessException{
		JsonObject jo =new JsonParser().parse(json).getAsJsonObject();
		Set<Entry<String,JsonElement>> ks=jo.entrySet();
		Gson g = new Gson();
		for (Entry<String, JsonElement> entry : ks) {
			JsonElement je=entry.getValue();
			JsonObject jop=je.getAsJsonObject();
			ColtortiProduct p=toObj(jop,ColtortiProduct.class);
			System.out.println(g.toJson(p));
		}
		
	}
	
	private <T> T toObj(JsonObject jo,Class<T> cls) throws InstantiationException, IllegalAccessException{
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
			if("attributes".equals(key))
				System.out.println(key);
			String pro=remove_(key);
			
			Class<?> type = getProType(cls,pro);
			if(type!=null)
				setProductValue(p,pro,g.fromJson(je, type));
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
	 * @param p
	 * @param pro
	 * @param value
	 */
	private void setProductValue(Object p, String pro, Object value) {
		try {
			BeanUtils.setProperty(p, pro, value);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
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
				System.out.println("no cache...");
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
	
	@Test
	public void testSameDate(){
		Date ds=null;Date de=null;
		String YYYY_MMDD_HH="yyyyMMddHH";
		ds=DateTimeUtil.parse("2015050312",YYYY_MMDD_HH);
		de=DateTimeUtil.parse("201505031214",YYYY_MMDD_HH);
		System.out.println(ds.equals(de));
	}
	
	public static void main(String[] args) {
		String path=AnyTest.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		String realPath = AnyTest.class.getClassLoader().getResource("").getFile();
		try {
			path= URLDecoder.decode(path,"utf-8");
			realPath= URLDecoder.decode(realPath,"utf-8");
			System.out.println(path);//jar文件路径|classpath根目录
			System.out.println(realPath);//jar目录|classpath根目录
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}

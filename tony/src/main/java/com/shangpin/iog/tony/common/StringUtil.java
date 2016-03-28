package com.shangpin.iog.tony.common;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyuzhi on 2015/9/10.
 */
public class StringUtil {

    /**
     * test
     * @param args
     */
    public static void main(String[] args) {


    }



    /**
     *get tony Category Name by id
     */
    public static String getCategoryID(String categoryId){
        return categoryId.substring(4,28);
    }

    /**
     *get Material from desc
     */
    public static String getMaterial(String desc){
        String material = "";
        if (desc == null){
            return material;
        }
        String[] descArr = desc.split("<br>");
        for (String s: descArr){
            if (s.contains("%,")){
                material = s;
            } else if ( s.contains("Leather")){
                System.out.println("=============================================");
                material = s;
            } else  if (s.contains("leather")){
                material = "leather";
            } else if (s.contains("Nylon")){
                material = s;
            } else if (s.contains("brass")){
                material = s;
            } else if (s.contains("PVC")){
                material = "PVC";
            } else if (s.contains("fox")||s.contains("calfskin")){
                material = s;
            } else if (s.contains("Sheared fabric")){
                material = "Sheared fabric";
            } else if (s.contains("GG fabric")){
                material = "GG fabric";
            } else if (s.contains("Elaphe snakeskin")){
                material = "Elaphe snakeskin";
            }
        }
        return material;
    }

    /**
     *get SKU ID
     */
    public static String getSkuID(Object skuId){
        return skuId.toString().substring(1,25);
    }

    /**
     *get Product Size
     */
    public static String getProductSize(String skuId){
        if (!skuId.contains("_") || !skuId.contains("-"))
            return "";
        return skuId.split("_")[1].split("-")[1];
    }

    /**
     *get spu id
     */
    public static String getSpuId(String skuId){
        if (skuId.contains("_")){
            return skuId.split("_")[0];
        } else if (skuId.contains("-")){
            return skuId.split("-")[0];
        }
        return skuId;
    }

    /**
     *get Product Size
     */
    public static String getProductCode(String skuId){
        if (!skuId.contains("-"))
            return skuId;
        return skuId.split("-")[0];
    }
    /**
     *get tony Category Name by id
     */
    public static String getCategoryNameByID(List<String> categoryIdList, Map<String,String> categoryMap){
    	try{
            String categoryName ="";
            for(String categoryId:categoryIdList){
                categoryName = categoryName+ categoryMap.get(categoryId)+"-";
            }
            return categoryName.substring(0,categoryName.length()-1);
    	}catch(Exception ec){
    		ec.printStackTrace();
    	}
    	
    	return "";
    }

    public static Map<String,String> getCategoryMap(String categoryJson){
        Map<String,String> categoryMap  = new HashMap<String,String>();
        try{

            JSONObject jsonObj = JSONObject.fromObject(categoryJson);
            JSONArray arr = jsonObj.getJSONObject("data").getJSONArray("categories");
            for(int i=0;i<arr.size();i++){
                JSONObject object = arr.getJSONObject(i);
                categoryMap.put(object.getJSONObject("_id").getString("$id"),object.getString("name"));
            }

        }catch(Exception ec){
            ec.printStackTrace();
        }

        return categoryMap;
    }


    public static String getCategory(JSONArray arr){

        try{
            if(null!=arr&&arr.size()>0){
                JSONObject object = arr.getJSONObject(0);
                return object.getJSONObject("_id").getString("$id");

            }

        }catch(Exception ec){
            ec.printStackTrace();
        }

        return "";
    }

    /**
     *获取Tony单品数量
     */
    public static String getTonyQty(String itemId,String json){

        String s = json.substring(json.indexOf(itemId));
        String s1 = s.substring(s.indexOf("\"season\""), s.indexOf("\"stock_price\""));;
        return s1.split(",")[1].replaceAll("\"","").replaceAll("qty:","");
    }
    /**
     *using getEvents get item's stock
     */
    public static String getStockById(String item){
        return item.split("\"qty\":")[1].split("}")[0];
    }

    /**
     *获取单品数量和价格
     */
    public static String getStockAndSupplyPrice(String item){

        System.out.println("item=="+item);
        String rtnStr = new StringBuffer(item.substring(item.indexOf("<stock>")+7,item.indexOf("</stock>")))
                .append("/")
                .append(item.substring(item.indexOf("<supply_price>")+14, item.indexOf("</supply_price>"))).toString();
        //System.out.println("rtnStr=="+rtnStr);
        return rtnStr;
    }
    /**
     *获取单品数量
     */
    public static String getSubBySub(String str,String begin,String end,int eAdd){
        str = str.substring(str.indexOf(begin)+begin.length(),str.indexOf(end)+eAdd);
        if(str.contains("stock")){
            str = getSubBySub(str,"<stock>","</stock>",0);
        }
        return str;
    }
    /**
     *截取标准产品字符串
     */
    public String getItemsArray(String json){
        return  json.substring(json.indexOf("["), json.length() - 2).replaceAll("\\$", "");
    }

}

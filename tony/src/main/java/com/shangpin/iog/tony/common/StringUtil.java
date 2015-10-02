package com.shangpin.iog.tony.common;

/**
 * Created by wangyuzhi on 2015/9/10.
 */
public class StringUtil {

    /**
     * test
     * @param args
     */
    public static void main(String[] args) {

        String s = "{\"status\":\"ok\",\"data\":{\"categories\":[{\"_id\":{\"$id\":\"55fab44028499f25010041a7\"},\"name\":\"Clutch\",\"master\":true},{\"_id\":{\"$id\":\"55fab45d28499f55050041a7\"},\"name\":\"Valigeria\",\"master\":true},{\"_id\":{\"$id\":\"55fab46428499f55050041a8\"},\"name\":\"Borsa a tracolla\",\"parent\":{\"$id\":\"55fab45d28499f55050041a7\"}},{\"_id\":{\"$id\":\"55fab46e28499f55050041a9\"},\"name\":\"Zaino\",\"parent\":{\"$id\":\"55fab45d28499f55050041a7\"}},{\"_id\":{\"$id\":\"55fab47628499f25010041a8\"},\"name\":\"Borsa a mano\",\"parent\":{\"$id\":\"55fab45d28499f55050041a7\"}},{\"_id\":{\"$id\":\"55fab48428499f53050041a8\"},\"name\":\"Abbigliamento\",\"master\":true},{\"_id\":{\"$id\":\"55fab48a28499f54050041a7\"},\"name\":\"Accessori\",\"parent\":{\"$id\":\"55fab48428499f53050041a8\"}},{\"_id\":{\"$id\":\"55fab48e28499f54050041a8\"},\"name\":\"Cappello\",\"parent\":{\"$id\":\"55fab48a28499f54050041a7\"}},{\"_id\":{\"$id\":\"55fab49928499f57050041a7\"},\"name\":\"Maglia\",\"parent\":{\"$id\":\"55fab48428499f53050041a8\"}},{\"_id\":{\"$id\":\"55fab49e28499f55050041aa\"},\"name\":\"Abito\",\"parent\":{\"$id\":\"55fab48428499f53050041a8\"}},{\"_id\":{\"$id\":\"55fab4a828499f51050041a7\"},\"name\":\"Top\",\"parent\":{\"$id\":\"55fab48428499f53050041a8\"}},{\"_id\":{\"$id\":\"55fab4ae28499f51050041a8\"},\"name\":\"Scarpe\",\"master\":true},{\"_id\":{\"$id\":\"55fab4b528499f52050041a7\"},\"name\":\"Scarpe col tacco\",\"parent\":{\"$id\":\"55fab4ae28499f51050041a8\"}}]}}";
       String id = "55fab44028499f25010041a7";
        //System.out.println(localFile);
        System.out.println("00000000000000000000000000000");
        System.out.println(StringUtil.getCategoryNameByID(id, s));
    }



    /**
     *get tony Category Name by id
     */
    public static String getCategoryID(String categoryId){
        return categoryId.substring(4,28);
    }
        /**
         *get tony Category Name by id
         */
        public static String getCategoryNameByID(String categoryId,String categoryJson){
        String str = categoryJson.substring(categoryJson.indexOf(categoryId),categoryJson.indexOf(categoryId)+50);
        System.out.println("==========str==========");
        System.out.println(str);
        return  str .split(",")[1].replaceAll("\"","").replaceAll("name:","");
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
}

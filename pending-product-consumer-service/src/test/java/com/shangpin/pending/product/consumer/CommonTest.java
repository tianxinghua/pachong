package com.shangpin.pending.product.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shangpin.pending.product.consumer.common.enumeration.SupplierValueMappingType;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by lizhongren on 2017/1/19.
 */
public class CommonTest {

    public static void main(String[] args){
//        String materail="尼龙 con 细节 in 真皮<br />锦纶tta con chiusura a scatto<br />chiusura superiore con cerniera<br />taschino 内部<br />2 manici in 真皮<br />larghezza: 30 cm<br />altezza: 28 cm<br />profoncità: 20 cmposizione del logo: logo inciso sul bottone100%, poliammide, 100%, pvc, 100%, 真皮colore: nero, marroneproduct model: le pliage";
//        if (StringUtils.isNotBlank(materail)) {
//            System.out.println(materail.replaceAll("<br />", "\r\n").replaceAll("<html>", "")
//                    .replaceAll("</html>", "").replaceAll("<br>","\r\n"));
//        }
//        Map<String,String> commonSizeMap = new LinkedHashMap<>();
//        commonSizeMap.put("++",".5");
//        commonSizeMap.put("+",".5");
//        Set<String> sizeSet  = commonSizeMap.keySet();//sizeMap.keySet();
//
//        String size = "32+";
//
//
//        String size1 = "33++";
//
//        String commkey = "";
//        for(String sizeKey:sizeSet){
//            if("++".equals(sizeKey)){
//                commkey = "\\++";
//            }else  if("+".equals(sizeKey)){
//                commkey = "\\+";
//            }
//            if(size.indexOf(sizeKey)>=0){
//                size = size.replaceAll(commkey,commonSizeMap.get(sizeKey));
//                System.out.println("size =" + size );
//            }
//            if(size1.indexOf(sizeKey)>=0){
//                size1 = size1.replaceAll(commkey,commonSizeMap.get(sizeKey));
//                System.out.println("size1 =" + size1 );
//            }
//        }


//        String stockResult ="{\"Stock\":1}";
//        JSONObject obj = JSON.parseObject(stockResult);;
//
//        int num = obj.getIntValue("Stock");
//        System.out.println("SupplierValueMappingType.TYPE_SIZE.getIndex()=" + SupplierValueMappingType.TYPE_SIZE.getIndex())  ;
        CommonTest test = new CommonTest();
        String line = "4509509HVAT 8697";
//        String[] words = line.split("\\s+",-1);
//        System.out.println("words length "+words.length);
//
//        String line4 = "GY6GMZ FSFFZ HWI86";
//        String[] words4 = line4.split("\\s+",-1);
//        System.out.println("words4 length "+words4.length);
//
//        String line1 = "97%  ,ksjd ksd,kdkd     viscose, 3% 弹性纤维ne  离中国";
//        String[] words1 = line1.split("\\s+|\\t| ");
//        test.setValue(words1);
//        System.out.println("words1 length "+words1.length);
//
//        String line2 = "primo tessuto: 100% 棉/ secondo tessuto: 65% 醋酸纤维 我说呀 啊, 35% 铜氨/ terzo tessuto: 88% poliammide, 12% 弹性纤维n";
//        String[] words2 = line2.split("\\s+|\\t| ");
//        test.setValue(words2);
//        System.out.println("words2 length "+words2.length);
//        test.test();
        test.getSysEnv();
    }

    public void setValue(String[] words){
        String material="";
        int i=0;
        StringBuilder builder = new StringBuilder();
        for(String word:words){
            material = "";

            i= word.indexOf(",");
            if(i==0){
                builder.append(",");
                word = word.substring(1);

                builder.append(word);



            }else if(i>0){
                if(i==word.length()-1){
                    word = word.substring(0,word.length()-1);
                    if(this.isChinese(word)) {
                        builder.append(word).append(",");
                    }else{
                        builder.append(" ").append(word).append(",");
                    }

                }else{
                    String[]  wordList = word.split(",");
                    int j=1;
                    for(String wd:wordList){
                         if(j==1){

                             builder.append(" ").append(wd);
                         }else{
                             builder.append(wd);
                         }

                        if(j!=wordList.length){
                            builder.append(",");
                        }
                        j++;
                    }

                }

            }else{
                 if(this.isChinese(word)){
                     builder.append(word);
                 }else{
                     builder.append(" ").append(word);
                 }

            }



        }
        System.out.println("word value=" + builder.toString());
    }


    public  boolean isChinese(String str) {
        String regex = "^[\u4e00-\u9fa5]+$";//其他需要，直接修改正则表达式就好
        return str.matches(regex);
    }

    public void test(){
        String brandMode ="7E1108A1GN F11F0";
        String modelRex = "([0-9A-Z]{6})([A-Z0-9]{5})([0-9]{4})";
        String excludeRex = "[^A-Z0-9]";
        String separator = "$1 $2 $3";
        String  result ="";
        String processed = brandMode.replaceAll(excludeRex, "");
        if (StringUtils.isBlank(processed)) {

        } else {
            if (processed.matches(modelRex)) {
                 result = processed.replaceAll(modelRex, separator);
            } else {

            }
        }
        System.out.println("result  = "+result );
    }

    public  void  getSysEnv(){

        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS","D:\\empolder\\google\\MyTranslation-d51da5164bfd.json");
        Map map = System.getenv();

        Iterator it = map.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry entry = (Map.Entry)it.next();
            System.out.print(entry.getKey()+"=");
            System.out.println(entry.getValue());
        }
        System.out.println("----------------------------------------------");
        Properties properties = System.getProperties();
        Iterator it1 =  properties.entrySet().iterator();
        while(it1.hasNext())
        {
            Map.Entry entry = (Map.Entry)it1.next();
            System.out.print(entry.getKey()+"=");
            System.out.println(entry.getValue());
        }
        String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        System.out.println("GOOGLE_APPLICATION_CREDENTIALS="+ credentialsPath);
        System.out.println("path ="+ System.getenv("path"));
    }
}

package com.shangpin.ephub.client.util;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lizhongren on 2017/1/13.
 */
public class RegexUtil {

    /**
     * @param regex
     * 正则表达式字符串
     * @param str
     * 要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 验证验证输入字母
     *
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean excludeLetter(String str) {
        if(StringUtils.isBlank(str)) return false;
        String regex = "[^A-Za-z]+";
        boolean result =  match(regex, str);
        if(!result){

                //排除PVC
                String regexPvc = "[^A-Za-z]?pvc[^A-Za-z]?";
                result =  match(regexPvc, str.toLowerCase());

        }
        return result;
    }

    /**
     * 品类特殊处理
     * @param category
     * @param val
     * @return 校验成功 返回null ，失败返回错误原因
     */
    public static String specialCategoryMatch(String category,String val) {
    	
    	try{
    		if(StringUtils.isBlank(val)) return "材质为空";
            String regex = "[^A-Za-z]+";
            boolean result =  match(regex, val);
            if(result){
                if(category.startsWith("A01")){  //服装
                    if(val.indexOf("%")<0){
                    	 return "材质缺少百分比";
                    }else{
                        return judgeNum(val);
                    }
                }else{
                	return null;
                }
            }else{
                //排除PVC  聚氯乙烯
//                String regexPvc = "^(pvc)$";
            	String regexPvc = "[^A-Za-z]*pvc[^A-Za-z]*";
                result =  match(regexPvc, val.toLowerCase());
                if(result){
                	return null;
                }else{
                	return "材质中含有英文字符";
                }
            }
    	}catch(Exception e){
    		return "材质校验异常："+e.getMessage();
    	}
    }

    private static String judgeNum(String val){
          double total = 0;
          String tmp = "";
          val =  replaceBlank(val);
          while(val.indexOf("%")>0){
              tmp =  val.substring(0,val.indexOf("%"));
              val = val.substring(val.indexOf("%")+1,val.length());
              total = total + getNum(tmp,4);

          }
          if(total==0){
        	  return "材质百分比错误";
          }else{
              if(0==total%100){
            	  return null;
              }else{
            	  return "材质百分比错误";
              }
          }
    }

    public  static double getNum(String val,int length){
         @SuppressWarnings("unused")
         double num = 0;
         if(length==0){
             return 0;
         }else{

             try {
                 val = val.substring(val.length()-length);
                 return Double.valueOf(val);

             } catch (Exception e) {
                 return getNum(val,length-1);
             }
         }
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
    /**
     * 验证验证输入汉字
     *
     * @param str
     * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean isChinese(String str) {
        String regex = "^[\u4e00-\u9fa5],{0,}$";
        return match(regex, str);
    }

    public static void main(String[] args){
//        System.out.println("1234");
//        System.out.println(RegexUtil.excludeLetter("skdj"));
//        System.out.println(RegexUtil.excludeLetter(" skdj  中国 "));
//        System.out.println(RegexUtil.excludeLetter("  100%  中国 ,93 % "));
        System.out.println(RegexUtil.specialCategoryMatch("A0123"," 棉45% 棉 ,100% 面部，快点看看55%"));
        System.out.println(RegexUtil.specialCategoryMatch("A0123"," 45.5% 棉 ,100% 面部，快点看看54.5%"));
        System.out.println("pvc = "+ RegexUtil.specialCategoryMatch("A"," sdkj pvc kkd"));
        System.out.println("pvc = "+ RegexUtil.specialCategoryMatch("A","PVC"));
        System.out.println("pvc = "+ RegexUtil.specialCategoryMatch("A13B01C01D02","100%pvc"));
    }
}

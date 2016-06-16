package com.shangpin.iog.productweb;

import java.lang.reflect.Method;

/**
 * Created by lizhongren on 2016/3/30.
 */
public class Test {

    public static void main(String[] args){
        String rootUrl = "D:/git/shangpin/iog-order/iog-product-web/src/main/java";
        ClassLoaderUtil networkClassLoader = new ClassLoaderUtil(rootUrl);
        String classname = "com.shangpin.iog.productweb.FetchData";
        try {
            Class cls = networkClassLoader.loadClass(classname);
            Method[] methods = cls.getMethods();
            for(Method methdo :methods){

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

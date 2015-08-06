package com.shangpin.iog.gilt;

import com.shangpin.iog.common.utils.httpclient.HttpUtil;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

/**
 * Created by sunny on 2015/8/5.
 */
public class Startup {

    public  static void main(String[] args) throws  Exception{
      String kk =   HttpUtil45.get("https://api-sandbox.gilt.com/global/skus", new OutTimeConfig(),null,"fb8ea6839b486dba8c5cabb374c03d9d","");
        System.out.println("kk = "+ kk);

       String post  =  HttpUtil45.postAuth("https://api-sandbox.gilt.com/global/skus",null, new OutTimeConfig(),"fb8ea6839b486dba8c5cabb374c03d9d","") ;
        System.out.println("post = "+ post);
        String kkk =  HttpUtil.getData("https://api-sandbox.gilt.com/global/skus",false,true,"fb8ea6839b486dba8c5cabb374c03d9d","");
        System.out.println("kkk = "+ kkk);

    }

}

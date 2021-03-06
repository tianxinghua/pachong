package com.shangpin.iog.onsite.base.common;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

/**
 * Created by Administrator on 2015/9/18.
 */
public class HTTPClient {
    private String URL;
    public HTTPClient(String URL){
        this.URL = URL;
    }
    /**
     * create conection and get json
     */
    public String fetchProductJson(){
        String json = null;
        try{
            long startMili=System.currentTimeMillis();
            json = HttpUtil45.get(URL, new OutTimeConfig(), null);
            long endMili=System.currentTimeMillis();
            System.out.println("source run times is "+(endMili-startMili)+" ms");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            HttpUtil45.closePool();
        }

        return json.substring(json.indexOf("<products>"));
    }
}

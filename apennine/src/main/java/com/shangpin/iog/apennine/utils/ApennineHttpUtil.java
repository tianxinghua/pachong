package com.shangpin.iog.apennine.utils;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.httpclient.HttpUtil;
import com.shangpin.iog.dto.ApennineProductDTO;
import com.shangpin.iog.dto.SpuDTO;
import org.apache.commons.httpclient.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunny on 2015/6/2.
 */
public class ApennineHttpUtil {
    @Autowired
    HttpUtil httpUtilService;
    private ApennineProductDTO getObjectByJsonString(String jsonStr) {
        ApennineProductDTO obj =null;
        Gson gson = new Gson();
        try {
            obj=gson.fromJson(jsonStr, ApennineProductDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
    /**
     * JSON发序列化为Java对象集合
     * @param jsonStr
     * @return
     */

    private  List<ApennineProductDTO>  getObjectsByJsonString(String jsonStr){
        Gson gson = new Gson();
        List<ApennineProductDTO> objs = new ArrayList<ApennineProductDTO>();
        try {
            objs = gson.fromJson(jsonStr, new TypeToken<List<ApennineProductDTO>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objs;
    }
    private  List<ApennineProductDTO>getProductsByUrl(String url){
        List<ApennineProductDTO>list=new ArrayList<>();
        try {
            String jsonStr=httpUtilService.getData(url,false);
            list=this.getObjectsByJsonString(jsonStr);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return list;
    }
    private  List<ApennineProductDTO>getProductsByUrlAndParam(String url,NameValuePair[] data){
        List<ApennineProductDTO>list=new ArrayList<>();
        try {
            String jsonStr=httpUtilService.postData(url, data,false);
            list=this.getObjectsByJsonString(jsonStr);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return list;
    }
    private List<SpuDTO> formatToSpu(List<ApennineProductDTO>list){
        List<SpuDTO>spuList=new ArrayList<>();
        SpuDTO spuDTO=new SpuDTO();
        for (int i = 0;i<list.size();i++){
            ApennineProductDTO dto=list.get(i);
            spuDTO.setSeasonId(dto.getCat1());
            spuDTO.setBrandName(dto.getCat());
            spuDTO.setSpuId(dto.getScode());
            spuList.add(spuDTO);
        }
        return spuList;
    }
}

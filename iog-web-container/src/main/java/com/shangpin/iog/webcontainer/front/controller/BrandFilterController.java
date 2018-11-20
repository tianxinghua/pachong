/**
 * 
 */
package com.shangpin.iog.webcontainer.front.controller;

import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.webcontainer.front.dto.ApiResponseBody;
import com.shangpin.iog.webcontainer.front.dto.BrandFilterDTO;
import com.shangpin.iog.webcontainer.front.dto.DataPageContent;
import com.shangpin.iog.webcontainer.front.util.HttpUtil45;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description 
 * @author myw
 * <br/>2018年11月5日
 */
@Controller
@RequestMapping("/download")
public class BrandFilterController {

	private static ResourceBundle bdl = null;
	private static String host;
	private static String deleteSupplier;
	private static OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		host = bdl.getString("host");
		deleteSupplier = bdl.getString("deleteSupplier");
	}

    //跳转到查询hub_filter的页面
	@RequestMapping("gotoBrandFilter")
	@ResponseBody
	public ModelAndView gotoBrandFilter(HttpServletResponse response){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("brandFilter");
		return modelAndView;
	}
	//将查询的数据返回到hub_filter的页面
	@RequestMapping("queryBrandFilter")
	@ResponseBody
	public String queryBrandFilter(HttpServletRequest request, HttpServletResponse response, BrandFilterDTO brandFilterDTO){
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        int pageIndex1 = Integer.parseInt(page);
        int pageSize1 = Integer.parseInt(rows);
        brandFilterDTO.setStartRow((pageIndex1-1)*pageSize1);
        brandFilterDTO.setPageSize(pageSize1);
		String jsonStr = JSONObject.fromObject(brandFilterDTO).toString();
		try {
			//根据条件查询总条数
			String totalJsonStr = HttpUtil45.operateData("post", "json", "http://192.168.3.175:8003/brandFilter/brandFilterTotalByCondition", timeConfig, null, jsonStr, null, null);
			//条件分页查询
			String resultJsonStr = HttpUtil45.operateData("post", "json", "http://192.168.3.175:8003/brandFilter/brandFilterList", timeConfig, null, jsonStr, null, null);
			JSONObject totalJsonObject = JSONObject.fromObject(totalJsonStr);
			int total  = totalJsonObject.getInt("content");
			JSONObject resultJsonObject = JSONObject.fromObject(resultJsonStr);
			Map<String,Class> keyMapConfig= new HashMap<>();
			keyMapConfig.put("content", BrandFilterDTO.class);
            ApiResponseBody apiResponseBody = (ApiResponseBody) JSONObject.toBean(resultJsonObject, ApiResponseBody.class, keyMapConfig);
            List<BrandFilterDTO> content = apiResponseBody.getContent();
            JSONObject result = new JSONObject();
            result.put("rows", content);
            result.put("total",total);
            return result.toString();
            //return content;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

    //修改处理状态并返回到hub_filter的页面
    @RequestMapping("updateType")
	@ResponseBody
    public String updateType(HttpServletResponse response, BrandFilterDTO brandFilterDTO){
        String jsonStr = JSONObject.fromObject(brandFilterDTO).toString();
        try {
            String resultJsonStr = HttpUtil45.operateData("post", "json", "http://192.168.3.175:8003/brandFilter/updateBrandFilter", timeConfig, null, jsonStr, null, null);
            JSONObject resultJsonObject = JSONObject.fromObject(resultJsonStr);
            String reustlMsg = resultJsonObject.getString("content");
            if("修改成功".equals(reustlMsg)){
                return "success";
            }
            return "error";
        }catch (Exception e){
            return "error";
        }

    }
}

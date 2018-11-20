/**
 * 
 */
package com.shangpin.iog.webcontainer.front.controller;

import com.alibaba.fastjson.JSON;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.webcontainer.front.dto.ApiResponseBody;
import com.shangpin.iog.webcontainer.front.dto.BrandFilterDTO;
import com.shangpin.iog.webcontainer.front.dto.HubSupplierValueMappingDto;
import com.shangpin.iog.webcontainer.front.dto.SupplierChannelPicDto;
import com.shangpin.iog.webcontainer.front.util.HttpUtil45;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @description 渠道和供应商编号的维护
 * @author myw
 * <br/>2018年11月12日
 */
@Controller
@RequestMapping("/download")
public class ChannelManageController {

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
	@RequestMapping("gotoChannelManage")
	@ResponseBody
	public ModelAndView gotoBrandFilter(HttpServletResponse response){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("channelManage");
		return modelAndView;
	}
	//将查询渠道的数据并且根据所有的供应商编号来设置页面的默认选中标志，返回到channelManage的页面
	@RequestMapping("queryByChannel")
	@ResponseBody
	public List<HubSupplierValueMappingDto> queryByChannel(HttpServletResponse response, SupplierChannelPicDto supplierChannelPicDto){
		String jsonStr = JSONObject.fromObject(supplierChannelPicDto).toString();
        //System.out.println(channelName);
		try {
		    //先查询所有的供应商编号
            String resultJsonStrVal = HttpUtil45.operateData("post", "json", "http://192.168.3.175:8003/channelManage/querySupplierIdList", timeConfig, null, "", null, null);
            JSONObject resultJsonObjectVal = JSONObject.fromObject(resultJsonStrVal);
            String contentJsonVal = resultJsonObjectVal.getString("content");
            List<HubSupplierValueMappingDto> listVal = JSON.parseArray(contentJsonVal, HubSupplierValueMappingDto.class);

            //查询条件无渠道名称时则不查询渠道表
            if(null != supplierChannelPicDto.getChannel() && !"".equals(supplierChannelPicDto.getChannel())){
                //查询该渠道的信息
                String resultJsonStrChannel = HttpUtil45.operateData("post", "json", "http://192.168.3.175:8003/channelManage/queryByChannelList", timeConfig, null, jsonStr, null, null);
                JSONObject resultJsonChannel = JSONObject.fromObject(resultJsonStrChannel);
                String contentJsonChannel = resultJsonChannel.getString("content");
                List<SupplierChannelPicDto> listChannel = JSON.parseArray(contentJsonChannel, SupplierChannelPicDto.class);
                if(null != listChannel && listChannel.size()>0){
                    for (HubSupplierValueMappingDto hsv: listVal) {
                        for (SupplierChannelPicDto scp: listChannel) {
                            if(scp.getSupplierId().equals(hsv.getSupplierId())){
                                hsv.setFlag("1"); //供应商编号相同时设置选中标志
                            }
                        }
                    }
                }
            }

            return listVal;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

    //保存数据并将处理状态并返回到channelManage的页面
    @RequestMapping("saveChannel")
	@ResponseBody
    public String saveChannel(HttpServletResponse response,SupplierChannelPicDto supplierChannelPicDto, String list ){
        //list = list.replace("[","").replace("]","");
        List<SupplierChannelPicDto> listStr = JSON.parseArray(list, SupplierChannelPicDto.class);
        try {
            //删除该渠道下的所有数据
            String jsonStrChannel = JSONObject.fromObject(supplierChannelPicDto).toString();
            HttpUtil45.operateData("post", "json", "http://192.168.3.175:8003/channelManage/deleteByChannel", timeConfig, null, jsonStrChannel, null, null);

            //执行插入操作
            for (SupplierChannelPicDto scp: listStr) {
                String jsonStr = JSONObject.fromObject(scp).toString();
                String resultJsonStr = HttpUtil45.operateData("post", "json", "http://192.168.3.175:8003/channelManage/addChannel", timeConfig, null, jsonStr, null, null);
            }
                return "success";
        }catch (Exception e){
            return "error";
        }

    }
}

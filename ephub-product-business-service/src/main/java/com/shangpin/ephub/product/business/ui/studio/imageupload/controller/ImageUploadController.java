package com.shangpin.ephub.product.business.ui.studio.imageupload.controller;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.studio.common.operation.dto.OperationQuery;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.StudioSlotVo;
import com.shangpin.ephub.product.business.ui.studio.common.pictrue.dto.UploadQuery;
import com.shangpin.ephub.product.business.ui.studio.imageupload.service.ImageUploadService;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title: ImageUploadController</p>
 * <p>Description: 图片上传页面所有的api </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月27日 上午11:07:49
 *
 */
@RestController
@RequestMapping("/api/airstudio/image")
@Slf4j
public class ImageUploadController {
	
	@Autowired
	private ImageUploadService imageUploadService;
	
	/**
	 * 图片上传页面
	 * @param request
	 * @return 返回失败的图片名称
	 */
	@RequestMapping(value="/upload",method = RequestMethod.POST)
	public HubResponse<?> upload(@RequestBody Map<String, List<String>> urlMaps){
		try {
			log.info("========开始上传图片==========="); 
			boolean result = true;
			for(Entry<String,List<String>> entry : urlMaps.entrySet()){
				if(!imageUploadService.add(entry.getKey(), entry.getValue())){
					result = false;
				}
			}
			if(result){
				return HubResponse.successResp("");
			}else{
				return HubResponse.errorResp("上传图片失败，请检查图片名称是否为barcode");
			}
		} catch (Exception e) {
			log.error("上传图片时异常："+e.getMessage(),e);
		}
		return HubResponse.errorResp("调用接口异常");
	}
	
	@RequestMapping(value = "/list" ,method = RequestMethod.POST)
	public HubResponse<?> slotList(@RequestBody OperationQuery operationQuery){
		List<StudioSlotVo> list = imageUploadService.list(operationQuery);
		if(null != list){
			return HubResponse.successResp(list);
		}else{
			return HubResponse.errorResp("error");
		}
	} 
	
	@RequestMapping(value = "/detail", method = RequestMethod.POST)
	public HubResponse<?> detail(@RequestBody String slotNo){
		log.info("批次号："+slotNo);
		return HubResponse.successResp(imageUploadService.slotDetail(slotNo));
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HubResponse<?> add(@RequestBody UploadQuery uploadQuery){
		log.info("添加图片参数："+JsonUtil.serialize(uploadQuery)); 
		if(StringUtils.isEmpty(uploadQuery.getSlotNoSpuId())){
			return HubResponse.errorResp("请先扫码");
		}
		if(imageUploadService.add(uploadQuery.getSlotNoSpuId(), uploadQuery.getUrls())){
			return HubResponse.successResp("全部上传成功。");
		}else{
			return HubResponse.errorResp("上传失败。");
		}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public HubResponse<?> deleteSlotSpuPic(@RequestBody String spPicUrl){
		boolean bool = imageUploadService.deleteSlotSpuPic(spPicUrl);
		log.info("图片删除返回结果=========="+bool); 
		if(bool){
			return HubResponse.successResp("删除成功。");
		}else{
			return HubResponse.errorResp("删除失败。");
		}
	}
	
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	public HubResponse<?> confirm(@RequestBody String slotNo){
		log.info("confirm=========="+slotNo);
		return imageUploadService.confirm(slotNo); 
	}
	
	@RequestMapping(value = "/modification", method = RequestMethod.POST)
	public HubResponse<?> modification(@RequestBody String barcode){
		List<String> list = imageUploadService.findPictures(barcode);
		if(null != list){
			return HubResponse.successResp(list);
		}else{
			return HubResponse.errorResp("modification error");
		}
		
	}

}

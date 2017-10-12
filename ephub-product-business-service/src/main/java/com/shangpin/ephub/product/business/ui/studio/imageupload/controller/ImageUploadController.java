package com.shangpin.ephub.product.business.ui.studio.imageupload.controller;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.studio.common.operation.dto.OperationQuery;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.StudioSlotVo;
import com.shangpin.ephub.product.business.ui.studio.common.pictrue.dto.UploadQuery;
import com.shangpin.ephub.product.business.ui.studio.common.pictrue.service.PictureService;
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
	@Autowired
	private PictureService pictureService;
	
	/**
	 * 图片上传页面
	 * @param request
	 * @return 返回失败的图片名称
	 */
	@RequestMapping(value="/upload",method = RequestMethod.POST)
	public HubResponse<?> upload(HttpServletRequest request){
		try {
			log.info("========开始上传图片==========="); 
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if(isMultipart){
				MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)request;
				Map<String, MultipartFile> maps = mreq.getFileMap();
				if(null != maps && maps.size()>0){
					/**
					 * 记录上传失败的url，并返回
					 */
					List<String> failList = Lists.newArrayList();
					Map<String,List<String>> urlMaps = Maps.newHashMap();
					String fileName, extension, fdfsURL, barcode = "";
					for(MultipartFile file : maps.values()){
						fileName = file.getOriginalFilename();
						log.info("上传图片："+fileName);
						extension = pictureService.getExtension(fileName);
						fdfsURL = pictureService.uploadPic(file.getBytes(), extension);
						barcode = pictureService.getBarCode(fileName);
						if(urlMaps.containsKey(barcode)){
							urlMaps.get(barcode).add(fdfsURL);
						}else{
							List<String> urls = Lists.newArrayList();
							urls.add(fdfsURL);
							urlMaps.put(barcode, urls);
						}
					}
					for(Entry<String,List<String>> entry : urlMaps.entrySet()){
						List<String> list = imageUploadService.add(entry.getKey(), entry.getValue());
						if(list.size() > 0){
							failList.addAll(list);
						}
					}
					if(failList.size() > 0){
						return HubResponse.errorResp(failList);
					}
				}
			}else{
				log.error("This request is not Multipart!"); 
				return HubResponse.errorResp("This request is not Multipart!");
			}
			return HubResponse.successResp("");
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
		List<String> list = imageUploadService.add(uploadQuery.getSlotNoSpuId(), uploadQuery.getUrls());
		log.info("上传失败的图片："+JsonUtil.serialize(list)); 
		if(list.size() == 0){
			return HubResponse.successResp("全部上传成功。");
		}else{
			return HubResponse.errorResp(list);
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

package com.shangpin.ephub.product.business.ui.studio.defective.controller;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicDto;
import com.shangpin.ephub.product.business.ui.studio.defective.dto.DefectiveQuery;
import com.shangpin.ephub.product.business.ui.studio.defective.service.DefectiveProductService;
import com.shangpin.ephub.product.business.ui.studio.defective.vo.DefectiveProductVo;
import com.shangpin.ephub.product.business.ui.studio.picture.PictureService;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title: DefectiveProductController</p>
 * <p>Description: 残次品记录页面所有接口 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月9日 下午6:11:36
 *
 */
@RestController
@RequestMapping("/defective-product")
@Slf4j
public class DefectiveProductController {
	
	@Autowired
	private DefectiveProductService defectiveProductService;
	@Autowired
	private PictureService pictureService;
	
	@RequestMapping(value="/list",method = RequestMethod.POST)
	public HubResponse<?> list(@RequestBody DefectiveQuery defectiveQuery){
		DefectiveProductVo productVo = defectiveProductService.list(defectiveQuery);
		if(null != productVo){
			return HubResponse.successResp(productVo);
		}else{
			return HubResponse.errorResp("调用接口异常。");
		}
	}

	@RequestMapping(value="/add",method = RequestMethod.POST)
	public HubResponse<?> add(@RequestBody String slotNoSpuId, HttpServletRequest request){
		try {
			StudioSlotDefectiveSpuDto defctiveSouDot = defectiveProductService.add(slotNoSpuId);
			if(null != defctiveSouDot){
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if(isMultipart){
					MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)request;
					Map<String, MultipartFile> maps = mreq.getFileMap();
					if(null != maps && maps.size()>0){
						for(Entry<String,MultipartFile> entry : maps.entrySet()){
							String fileName = entry.getKey();
							MultipartFile file = entry.getValue();
							String extension = pictureService.getExtension(fileName);
							Long studioSlotDefectiveSpuPicId = defectiveProductService.insert(defctiveSouDot, extension);
							String fdfsURL = pictureService.uploadPic(file.getBytes(), studioSlotDefectiveSpuPicId, extension);
							defectiveProductService.update(studioSlotDefectiveSpuPicId, fdfsURL);
						}
					}
				}else{
					log.error("This request is not Multipart!"); 
					return HubResponse.errorResp("This request is not Multipart!");
				}
			}else{
				return HubResponse.errorResp("残次品入库时服务异常。");
			}
			return HubResponse.successResp("");
		} catch (Exception e) {
			log.error("添加残次品时异常："+e.getMessage(),e);
		}
		return HubResponse.errorResp("调用接口异常");
	}
	
	@RequestMapping(value="/modification", method = RequestMethod.POST)
	public HubResponse<?> modify(@RequestBody Long studioSlotDefectiveSpuId, HttpServletRequest request){
		try {
			StudioSlotDefectiveSpuDto defctiveSouDot = defectiveProductService.selectByPrimarykey(studioSlotDefectiveSpuId);
			if(null != defctiveSouDot){
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if(isMultipart){
					MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)request;
					Map<String, MultipartFile> maps = mreq.getFileMap();
					if(null != maps && maps.size()>0){
						for(Entry<String,MultipartFile> entry : maps.entrySet()){
							String fileName = entry.getKey();
							if(!defectiveProductService.hasDefectiveSpuPic(fileName)){
								MultipartFile file = entry.getValue();
								String extension = pictureService.getExtension(fileName);
								Long studioSlotDefectiveSpuPicId = defectiveProductService.insert(defctiveSouDot, extension);
								String fdfsURL = pictureService.uploadPic(file.getBytes(), studioSlotDefectiveSpuPicId, extension);
								defectiveProductService.update(studioSlotDefectiveSpuPicId, fdfsURL);
							}
						}
					}
				}else{
					log.error("This request is not Multipart!"); 
					return HubResponse.errorResp("This request is not Multipart!");
				}
			}
		} catch (Exception e) {
			log.error("残次品页面修改图片发生异常："+e.getMessage(),e); 
		}
		return null;
	}
	
	@RequestMapping(value="/detail", method = RequestMethod.POST)
	public HubResponse<?> detail(@RequestBody String studioSlotDefectiveSpuId){
		List<StudioSlotDefectiveSpuPicDto> list = defectiveProductService.selectDefectivePic(studioSlotDefectiveSpuId);
		if(CollectionUtils.isNotEmpty(list)){
			return HubResponse.successResp(list);
		}else{
			return HubResponse.errorResp("查找详情图片失败"); 
		}
	}
	
	@RequestMapping(value="/delete-defective-pic", method = RequestMethod.POST)
	public HubResponse<?> deleteDefectivePic(@RequestBody String spPicUrl){
		boolean bool = defectiveProductService.deleteDefectivePic(spPicUrl);
		if(bool){
			return HubResponse.successResp("");
		}else{
			return HubResponse.errorResp("删图失败");
		}
	}
}

package com.shangpin.ephub.product.business.ui.studio.defective.controller;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.esotericsoftware.minlog.Log;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuDto;
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
	public HubResponse<?> list(DefectiveQuery defectiveQuery){
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
			Log.error("添加残次品时异常："+e.getMessage(),e);
		}
		return HubResponse.errorResp("调用接口异常");
	}
	
	@RequestMapping(value="/modification", method = RequestMethod.POST)
	public HubResponse<?> modify(){
		return null;
	}
	
	@RequestMapping(value="/detail", method = RequestMethod.POST)
	public HubResponse<?> detail(){
		return null;
	}
}

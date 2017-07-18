package com.shangpin.ephub.product.business.ui.studio.defective.controller;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicDto;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.ui.studio.common.pictrue.dto.UploadQuery;
import com.shangpin.ephub.product.business.ui.studio.common.pictrue.service.PictureService;
import com.shangpin.ephub.product.business.ui.studio.defective.dto.DefectiveQuery;
import com.shangpin.ephub.product.business.ui.studio.defective.service.DefectiveProductService;
import com.shangpin.ephub.product.business.ui.studio.defective.vo.DefectiveProductVo;
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
@RequestMapping("/api/airstudio/defective-product")
@Slf4j
public class DefectiveProductController {
	
	@Autowired
	private DefectiveProductService defectiveProductService;
	@Autowired
	private PictureService pictureService;
	
	@RequestMapping(value="/list",method = RequestMethod.POST)
	public HubResponse<?> list(@RequestBody DefectiveQuery defectiveQuery){
		List<DefectiveProductVo> productVo = defectiveProductService.list(defectiveQuery);
		if(null != productVo){
			return HubResponse.successResp(productVo);
		}else{
			return HubResponse.errorResp("调用接口异常。");
		}
	}
	
	@RequestMapping(value="/add",method = RequestMethod.POST)
	public HubResponse<?> add(@RequestBody UploadQuery uploadQuery){
		try {
			log.info("添加残次品接受到的数据："+JsonUtil.serialize(uploadQuery)); 
			StudioSlotDefectiveSpuDto defctiveSouDot = defectiveProductService.add(uploadQuery.getSlotNoSpuId());
			if(null != defctiveSouDot){
				if(CollectionUtils.isNotEmpty(uploadQuery.getUrls())){
					for(String spPicUrl : uploadQuery.getUrls()){
						if(!defectiveProductService.hasDefectiveSpuPic(spPicUrl)){
							String extension = pictureService.getExtension(spPicUrl);
							defectiveProductService.insert(defctiveSouDot, spPicUrl, extension);
						}
					}
				}
				return HubResponse.successResp("添加成功");
			}
		} catch (Exception e) {
			log.error("残次品页面修改图片发生异常："+e.getMessage(),e); 
		}
		return HubResponse.errorResp("添加失败");
	}
	
	@RequestMapping(value="/detail", method = RequestMethod.POST)
	public HubResponse<?> detail(@RequestBody String studioSlotDefectiveSpuId){
		List<StudioSlotDefectiveSpuPicDto> list = defectiveProductService.selectDefectivePic(studioSlotDefectiveSpuId);
		return HubResponse.successResp(list);
	}
	
	@RequestMapping(value="/delete-defective-pic", method = RequestMethod.POST)
	public HubResponse<?> deleteDefectivePic(@RequestBody String spPicUrl){
		boolean bool = defectiveProductService.deleteDefectivePic(spPicUrl);
		log.info("残次品图片删除返回结果=========="+bool); 
		if(bool){
			return HubResponse.successResp("删除成功");
		}else{
			return HubResponse.errorResp("删图失败");
		}
	}
}

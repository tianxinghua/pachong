package com.shangpin.ephub.product.business.common.service.check.property;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.enumeration.MaterialState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuGenderState;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.util.RegexUtil;
import com.shangpin.ephub.product.business.common.hubDic.material.dto.MaterialDTO;
import com.shangpin.ephub.product.business.common.hubDic.material.service.HubMaterialDicService;
import com.shangpin.ephub.product.business.common.service.check.CommonCheckBase;

/**
 * Created by lizhongren on 2017/3/3. 单个具体的实现类
 */
@Component
public class MaterialCheck extends CommonCheckBase {
	static Map<String, String> materialStaticMap = null;
	@Autowired
	HubMaterialDicService hubMaterialDicService;

	@Override
	protected String checkValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) {
		
		if(hubSpuPendingIsExist.getMaterialState()==MaterialState.HANDLED.getIndex()){
    		return null;
    	}
		// 校验材质
		hubSpuPendingIsExist.setHubMaterial(spuPendingDto.getHubMaterial());
		if (StringUtils.isNotBlank(spuPendingDto.getHubMaterial())) {
			if (RegexUtil.excludeLetter(spuPendingDto.getHubMaterial())) {
				hubSpuPendingIsExist.setMaterialState(MaterialState.HANDLED.getIndex());
				return null;
			}
		}
		hubSpuPendingIsExist.setMaterialState(MaterialState.UNHANDLED.getIndex());
		return "材质校验失败";
	}

	@Override
	protected boolean convertValue(HubSpuPendingDto spuPendingDto) throws Exception {
		return replaceMaterial(spuPendingDto);
	}

	protected boolean replaceMaterial(HubSpuPendingDto hubSpuPending) {
		Map<String, String> materialMap = this.getMaterialMap();
		Set<String> materialSet = materialMap.keySet();
		String supplierMaterial = "";
		if (StringUtils.isNotBlank(hubSpuPending.getHubMaterial())) {

			for (String material : materialSet) {
				if (StringUtils.isBlank(material))
					continue;

				supplierMaterial = hubSpuPending.getHubMaterial().toLowerCase();
				if (supplierMaterial.indexOf(material.toLowerCase()) >= 0) {

					hubSpuPending.setHubMaterial(supplierMaterial.replaceAll(material.toLowerCase(), materialMap.get(material)));
				}

			}
			hubSpuPending.setHubMaterial(hubSpuPending.getHubMaterial());

			if (!RegexUtil.excludeLetter(hubSpuPending.getHubMaterial())) {
				hubSpuPending.setMaterialState(InfoState.IMPERFECT.getIndex());
				// 材质含有英文 返回false
				return false;
			} else {
				hubSpuPending.setMaterialState(InfoState.PERFECT.getIndex());
				return true;
			}

		} else {
			hubSpuPending.setMaterialState(InfoState.IMPERFECT.getIndex());
			// 无材质 返回false
			return false;
		}
	}

	/**
	 * 材质获取 做替换
	 *
	 *
	 * @return
	 */
	protected Map<String, String> getMaterialMap() {
		if (null == materialStaticMap) {
			materialStaticMap = new LinkedHashMap<>();
			List<MaterialDTO> materialDTOS = hubMaterialDicService.getMaterialMapping();
			for (MaterialDTO dto : materialDTOS) {

				materialStaticMap.put(dto.getSupplierMaterial(), dto.getHubMaterial());
			}
		} else {
			if (isNeedHandle()) {
				List<MaterialDTO> materialDTOS = hubMaterialDicService.getMaterialMapping();
				for (MaterialDTO dto : materialDTOS) {
					materialStaticMap.put(dto.getSupplierMaterial(), dto.getHubMaterial());
				}
			}
		}
		return materialStaticMap;
	}
}

package com.shangpin.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

public class HandSpuDataThread implements Runnable {

	private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger.getLogger("info");
	private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");
	@Autowired
	ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;
	private String supplierId;
	private List<SpuDTO> spuList;
	
	public HandSpuDataThread(String supplierId, List<SpuDTO> spuList,ProductFetchService productFetchService,ProductSearchService productSearchService) {
		this.supplierId = supplierId;
		this.spuList = spuList;
		this.productFetchService =productFetchService ;
		this.productSearchService = productSearchService;
	}

	@Override
	public void run() {
		saveSPU(spuList,supplierId);
	}
	/**
	 * 保存spu
	 * @param spuList
	 */
	public void saveSPU(List<SpuDTO> spuList,String supplierId) {
		System.out.println("====保存spu:"+spuList.size());
		updateSPUMemo(supplierId, spuList);
		
		for (SpuDTO spuDTO : spuList) {
			try {
				productFetchService.saveSPU(spuDTO);
			} catch (ServiceException e) {
				try {
					productFetchService.updateMaterial(spuDTO);
				} catch (ServiceException e1) {
					loggerError.info("spu更新材质信息失败");
				}
			}
		}
		System.out.println("+++++++++++++保存spu结束+++++++++++++");
		loggerInfo.info("保存spu结束");
	}
	private void updateSPUMemo(String supplierId,List<SpuDTO> spuList){
		List<SpuDTO> updateSpu = productSearchService.findpartSpuListBySupplier(supplierId);
		loggerInfo.info("获取到更新spu数"+updateSpu.size());
		Map<String,SpuDTO> spuMap = new HashMap<String,SpuDTO>();
		
		List<SpuDTO> reSpuList = new ArrayList<SpuDTO>();
		for (SpuDTO spu : updateSpu) {
			try {
				spuMap.put(spu.getSpuId(), spu);
			} catch (Exception e) {
				loggerError.error(e); 
			}			
		}
		String memo = "";
		for (SpuDTO spuDTO : spuList) {
			try {
				if (spuMap.containsKey(spuDTO.getSpuId())) {
					memo = conpareSpu(spuDTO, spuMap.get(spuDTO.getSpuId()));
					if (StringUtils.isNotEmpty(memo)) {
						spuDTO.setMemo(memo);
						reSpuList.add(spuDTO);
					}
				}
			} catch (Exception e) {
				loggerError.error(e); 
			}
			
		}
		if (reSpuList.size()>0&&reSpuList!=null) {
			productFetchService.updateSpuListMemo(reSpuList);
		}
	}
	//检查spu的   made_in ,material ,season 是否变化
	private String conpareSpu(SpuDTO spuDTO,SpuDTO nespuDTO){
		StringBuffer memo = new StringBuffer();
		String localeString = new Date().toLocaleString();
		if (StringUtils.isNotBlank(spuDTO.getSeasonName())) {
			if (!spuDTO.getSeasonName().equals(nespuDTO.getSeasonName())) {
				memo.append(localeString+"季节改变").append(";");
			}
		}
		if (StringUtils.isNotBlank(spuDTO.getMaterial())) {
			if (!spuDTO.getMaterial().equals(nespuDTO.getMaterial())) {
				memo.append(localeString+"材质改变").append(";");
			}
		}
		if (StringUtils.isNotBlank(spuDTO.getProductOrigin())) {
			if (!spuDTO.getProductOrigin().equals(nespuDTO.getProductOrigin())) {
				memo.append(localeString+"产地改变").append(";");
			}
		}
		return memo.toString();
	}
	
}

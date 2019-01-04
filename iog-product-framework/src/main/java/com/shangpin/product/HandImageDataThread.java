package com.shangpin.product;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.ImageUtils;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.service.SkuPriceService;
public class HandImageDataThread implements Runnable {

	private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger.getLogger("info");
	private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");
	@Autowired
	ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;
	@Autowired
	SkuPriceService skuPriceService;
	
	private String supplierId;

	private String flag;

	private String picpath;

	private Map<String, List<String>> imageMap;
	
	public HandImageDataThread(String supplierId, String flag, String picpath, Map<String, List<String>> imageMap,ProductFetchService productFetchService,ProductSearchService productSearchService,SkuPriceService skuPriceService) {
		
		this.supplierId = supplierId;
		this.flag = flag;
		this.picpath = picpath;
		this.imageMap = imageMap;
		this.productFetchService = productFetchService;
		this.productSearchService = productSearchService;
		this.skuPriceService = skuPriceService;
	}

	@Override
	public void run() {
		saveImage(imageMap, flag, supplierId, picpath);
	}
	/**
	 * 保存图片,检查并处理图片变化
	 * @param imageMap
	 * @param flag sku,spu
	 * @param supplierId
	 * @param picpath 如果为空就不下载图片
	 */
	public void saveImage(Map<String,List<String>> imageMap,String flag,String supplierId, String picpath) {
		loggerInfo.info("开始处理图片");
		if(imageMap==null||imageMap.size()<=0){
			return;
		}
		System.out.println("开始处理图片"+imageMap.size());
		List<String> list = null;
		String imagePath = "";
		String memo = "";
		String id = "";
		String imgname = "";
		String result = "";
		Map<String, List<String>> downMap = new HashMap<String, List<String>>();
		for (Entry<String, List<String>> entry : imageMap.entrySet()) {
			try {
				id = entry.getKey().split(";")[0];
				//正常使用
//				System.out.println("===="+id+"==="+entry.getValue());
				list = productFetchService.saveAndCheckPicture(supplierId,id, entry.getValue(), flag);
				// 仅仅stefaniamode采取
//				list = productFetchService.saveAndCheckPictureForSteFaniamode(supplierId,id, entry.getValue(), flag);
				if (list.size()>0) {
					productFetchService.updateSpuOrSkuMemoAndTime(supplierId, id,  new Date().toLocaleString()+"图片变化", flag);
					//存新增的的图片到map
					imgname = entry.getKey().split(";")[1];
					downMap.put(id+";"+imgname, list);
				}
			} catch (Exception e) {
				loggerError.error(e); 
			}
			
		}
		
		if (StringUtils.isNotBlank(picpath)&&downMap.size()>0) {
			loggerInfo.info("开始保存图片"+downMap.size());
			System.out.println("开始保存图片"+downMap.size());
			for (Entry<String, List<String>> e : downMap.entrySet()) {
				try {
					int n = 1;
					for (String url : e.getValue()) {
						try {
							if (StringUtils.isNotEmpty(url)) {
								id = e.getKey().split(";")[0];
								imgname = e.getKey().split(";")[1]+" ("+n+++").jpg";
								imagePath = picpath+imgname;
								imagePath = ImageUtils.downImage(url, picpath,imgname);
								result = ImageUtils.checkImageSize(imagePath);
								if (!result.equals("")) {
									if (memo.contains(result)) {
										memo = memo.replace(result, result+"1");
									}else{
										memo +=" "+result;
									}
								}
							}
						} catch (Exception e2) {
							loggerError.error(e2); 
						}
						
					}
					if (StringUtils.isNotBlank(memo)) {
						productFetchService.updateSpuOrSkuMemoAndTime(supplierId, id,  new Date().toLocaleString()+memo, flag);
					}
					memo = "";
				} catch (Exception e2) {
					loggerError.error(e2); 
				}
				
			}
		}
		System.out.println("保存图片结束");
		loggerInfo.info("保存图片结束");
	}
}

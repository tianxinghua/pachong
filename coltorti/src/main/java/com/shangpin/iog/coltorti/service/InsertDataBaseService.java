/**
 * 
 */
package com.shangpin.iog.coltorti.service;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月9日
 */
public class InsertDataBaseService {
	private static Logger logger = LoggerFactory.getLogger(InsertDataBaseService.class);
	private ProductFetchService pfs;
	/**O
	 * 
	 */
	public InsertDataBaseService(ProductFetchService fetchServ) {
		super();
		this.pfs=fetchServ;
	}

	public void insertSku(Collection<SkuDTO> skus){
		int failCnt=0;int total=skus.size();
		logger.info("-----开始保存SKU-----");
		for (SkuDTO sk : skus) {
			try{
				pfs.saveSKU(sk);
			}catch(Exception e){
				failCnt++;
				logger.error("保存sku:{}失败,错误信息：{},",new Gson().toJson(sk),e.getMessage());
			}
		}
		logger.info("-----SkU保存结束，spu总数：{},成功数{}",total,total-failCnt);
	}
	public void insertSpu(Collection<SpuDTO> spus){
		int failCnt=0;int total=spus.size();
		logger.info("-----开始保存SPU-----");
		for (SpuDTO spu : spus) {
			try{
				pfs.saveSPU(spu);
			}catch(Exception e){
				failCnt++;
				logger.error("保存spu:{}失败,错误信息：{},",new Gson().toJson(spu),e.getMessage());
			}
		}
		logger.info("-----SPU保存结束，spu总数：{},成功数{}",total,total-failCnt);
	}
	public void insertSkuPic(Collection<ProductPictureDTO> skuPics){
		int failCnt=0;int total=skuPics.size();
		logger.info("-----开始保存SKUPIC-----");
		for (ProductPictureDTO pic : skuPics) {
			try{
				pfs.savePicture(pic);
			}catch(Exception e){
				failCnt++;
				logger.error("保存SKUPIC:{}失败,错误信息：{},",new Gson().toJson(pic),e.getMessage());
			}
		}
		logger.info("-----SKUPIC保存结束，SKUPIC总数：{},成功数{}",total,total-failCnt);
	}
	
}

/**
 * 
 */
package com.shangpin.iog.coltorti.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.coltorti.convert.ColtortiProductConvert;
import com.shangpin.iog.coltorti.dto.ColtortiProduct;
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
	/**
	 * 抓取数据
	 * @param dateStart 
	 * @param dateEnd
	 */
	public void grabProduct(String dateStart,String dateEnd){
		try {
			logger.info("抓取数据开始，开始时间：{},结束时间:{}",dateStart,dateEnd);
			List<ColtortiProduct> coltorProds=ColtortiProductService.findProduct(dateStart, dateEnd);
			logger.info("抓取数据成功，抓取到{}条.",coltorProds.size());
			//logger.info("抓取数据成功，抓取到{}条,数据如下：\r\n{}",coltorProds.size(),new Gson().toJson(coltorProds));
			//拆分spu
			Set<SpuDTO> spus=new HashSet<>(coltorProds.size());
			for (ColtortiProduct product : coltorProds) {
				SpuDTO spu = ColtortiProductConvert.product2spu(product);
				spus.add(spu);
			}
			//拆分sku和图片
			coltorProds=ColtortiProductService.divideSku4Size(coltorProds);
			Set<SkuDTO> skus=new HashSet<>(coltorProds.size());
			Map<String,Set<ProductPictureDTO>> productPics=new HashMap<String, Set<ProductPictureDTO>>();
			for (ColtortiProduct product : coltorProds) {
				SkuDTO sk=ColtortiProductConvert.product2sku(product);
				skus.add(sk);
				Set<ProductPictureDTO> ppcs=ColtortiProductConvert.productPic(product);
				productPics.put(product.getSkuId(), ppcs);
			}
			//开始保存
			if(CollectionUtils.isNotEmpty(skus)) {
				logger.info("-----开始保存SKU-----总数：{}",skus.size());
				int failCnt=insertSku(skus);
				logger.info("-----SKU保存结束，sku总数：{},成功数{}",skus.size(),skus.size()-failCnt);
			}
			if(CollectionUtils.isNotEmpty(spus)){
				logger.info("-----开始保存SPU-----总数：{}",spus.size());
				int failCnt = insertSpu(spus);
				logger.info("-----SPU保存结束，spu总数：{},成功数{}",spus.size(),spus.size()-failCnt);
			}
			logger.info("-----开始保存SKUPIC-----");
			Set<String> picSku=productPics.keySet();
			int failCnt=0;int total=0;
			for (String sku : picSku) {
				Set<ProductPictureDTO> pcs=productPics.get(sku);
				if(CollectionUtils.isNotEmpty(pcs)){
					total+=pcs.size();
					failCnt+=insertSkuPic(pcs);
				}
			}
			logger.info("-----SKUPIC保存结束，SKUPIC总数：{},成功数{}",total,total-failCnt);
		} catch (ServiceException e) {
			logger.error("抓取Coltorti数据失败。",e);
		}
	}
	
	
	private int insertSku(Collection<SkuDTO> skus){
		int failCnt=0;
		for (SkuDTO sk : skus) {
			try{
				pfs.saveSKU(sk);
			}catch(Exception e){
				failCnt++;
				if(e.getClass().equals(DuplicateKeyException.class)){
					continue;
				}
				if(failCnt==10){
					e.printStackTrace();
					logger.error("保存sku:{}失败,错误信息：{},",new Gson().toJson(sk),e.getMessage());
				}
			}
		}
		return failCnt;
	}
	private int insertSpu(Collection<SpuDTO> spus){
		int failCnt=0;
		for (SpuDTO spu : spus) {
			try{
				pfs.saveSPU(spu);
			}catch(Exception e){
				failCnt++;
				if(e.getClass().equals(DuplicateKeyException.class)){
					continue;
				}
				logger.error("保存spu:{}失败,错误信息：{},",new Gson().toJson(spu),e.getMessage());
			}
		}
		return failCnt;
	}
	private int insertSkuPic(Collection<ProductPictureDTO> skuPics){
		int failCnt=0;
		for (ProductPictureDTO pic : skuPics) {
			try{
				pfs.savePicture(pic);
			}catch(Exception e){
				failCnt++;
				if(e.getClass().equals(DuplicateKeyException.class)){
					continue;
				}
				logger.error("保存SKU:{} PIC:{}失败,错误信息：{}",new Object[]{pic.getSkuId(),pic.getPicUrl(),e.getMessage()});
			}
		}
		return failCnt;
	}
	
}

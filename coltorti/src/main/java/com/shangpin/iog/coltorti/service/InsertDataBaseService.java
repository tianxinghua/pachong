/**
 * 
 */
package com.shangpin.iog.coltorti.service;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.coltorti.convert.ColtortiProductConvert;
import com.shangpin.iog.coltorti.dto.ColtortiProduct;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.product.service.ProductSearchServiceImpl;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年6月9日
 */
public class InsertDataBaseService {
	private static Logger logger = LoggerFactory.getLogger(InsertDataBaseService.class);
	public static int day;
	private static ResourceBundle bdl = null;
	private ProductFetchService pfs;
	private ProductSearchService productSearchService;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		day = Integer.valueOf(bdl.getString("day"));
	}
	/**O
	 * 
	 */
	
	public InsertDataBaseService(ProductFetchService fetchServ,ProductSearchService productSearchService) {
		super();
		this.pfs=fetchServ;
		this.productSearchService=productSearchService;
	}
	public InsertDataBaseService() {
		super();
	}
	/**
	 * 抓取数据
	 * @param dateStart 
	 * @param dateEnd
	 */
	public void grabProduct(String dateStart,String dateEnd){
		try {
			Date startDate,endDate= new Date();
			startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
			//获取原有的SKU 仅仅包含价格和库存
//			Map<String,SkuDTO> skuDTOMap = new HashedMap();
//			try {    
//				skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
//			} catch (ServiceException e) {
//				e.printStackTrace();
//			}
			
			logger.info("抓取数据开始，开始时间：{},结束时间:{}",dateStart,dateEnd);
			System.out.println("抓取数据开始，开始时间：" + dateStart +" 结束时间:"+dateEnd);
			List<ColtortiProduct> coltorProds=ColtortiProductService.findProduct(dateStart, dateEnd);
			logger.info("抓取数据成功，抓取到{}条.",coltorProds.size());
			System.out.println("抓取数据成功，抓取到{}条."+coltorProds.size());
			//logger.warn("抓取数据成功，抓取到{}条,数据如下：\r\n{}",coltorProds.size(),new Gson().toJson(coltorProds));
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
				
//				if(skuDTOMap.containsKey(sk.getSkuId())){
//					skuDTOMap.remove(sk.getSkuId());
//				}
				
				skus.add(sk);
				Set<ProductPictureDTO> ppcs=ColtortiProductConvert.productPic(product);
				productPics.put(product.getSkuId(), ppcs);
			}
			logger.warn("抓取到sku:\r\n"+new Gson().toJson(skus));
			//开始保存
			if(CollectionUtils.isNotEmpty(spus)){
				logger.info("-----开始保存SPU-----总数：{}",spus.size());
				int failCnt = insertSpu(spus);
				logger.info("-----SPU保存结束，spu总数：{},成功数{}",spus.size(),spus.size()-failCnt);
			}


			if(CollectionUtils.isNotEmpty(skus)) {
				logger.info("-----开始保存SKU-----总数：{}",skus.size());
				int failCnt=insertSku(skus,productPics);
				logger.info("-----SKU保存结束，sku总数：{},成功数{}",skus.size(),skus.size()-failCnt);
			}

//			logger.info("-----开始保存SKUPIC-----");
//			Set<String> picSku=productPics.keySet();
//			int failCnt=0;int total=0;
//			for (String sku : picSku) {
//				Set<ProductPictureDTO> pcs=productPics.get(sku);
//				if(CollectionUtils.isNotEmpty(pcs)){
//					total+=pcs.size();
//					failCnt+=insertSkuPic(pcs);
//				}
//			}
//			logger.info("-----SKUPIC保存结束，SKUPIC总数：{},成功数{}",total,total-failCnt);
			
	    	//更新网站不再给信息的老数据
//			for(Iterator<Map.Entry<String,SkuDTO>> itor = skuDTOMap.entrySet().iterator();itor.hasNext(); ){
//				 Map.Entry<String,SkuDTO> entry =  itor.next();
//				if(!"0".equals(entry.getValue().getStock())){//更新不为0的数据 使其库存为0
//					entry.getValue().setStock("0");
//					try {
//						pfs.updatePriceAndStock(entry.getValue());
//					} catch (ServiceException e) {
//						e.printStackTrace();
//					}
//				}
//			}
		} catch (ServiceException e) {
			logger.error("抓取Coltorti数据失败。",e);
		}
	}
	
	
	private int insertSku(Collection<SkuDTO> skus,Map<String,Set<ProductPictureDTO>> picMap){
		int failCnt=0;
		logger.warn("sku 数量 ：" + skus.size());
		int i =0,j=0;
		for (SkuDTO sk : skus) {
			
			try{
				sk.setMarketPrice(sk.getSupplierPrice());
				pfs.saveSKU(sk);
//				if(null!=picMap){
//					logger.info("-----开始保存SKUPIC-----");
//					Set<ProductPictureDTO> pcs=picMap.get(sk.getSkuId());
//					if(CollectionUtils.isNotEmpty(pcs)){
//						insertSkuPic(pcs);
//					}
//				}


//			logger.info("-----SKUPIC保存结束，SKUPIC总数：{},成功数{}",total,total-failCnt);


			}catch(Exception e){
				failCnt++;

				if(e.getMessage().equals("数据插入失败键重复")) {
					try {
						pfs.updatePriceAndStock(sk);
					} catch (ServiceException e1) {
						e1.printStackTrace();
						logger.error("保存sku:{}失败,错误信息：{},",new Gson().toJson(sk),e.getMessage());
					}
				}
//				if(e.getClass().equals(DuplicateKeyException.class)){
//					continue;
//				}
				if(failCnt==10){
					e.printStackTrace();
					logger.error("保存sku:{}失败,错误信息：{},",new Gson().toJson(sk),e.getMessage());
				}
			}

			if(null!=picMap){
                Set<ProductPictureDTO> pcs=picMap.get(sk.getSkuId());

				if(null!=pcs&&pcs.size()>0){
					i++;
					logger.warn("sku = " + sk.getSkuId() + " 图片数量：" + pcs.size());
					List<String> picUrlList = new ArrayList<>();
					for(ProductPictureDTO productPictureDTO:pcs){
						picUrlList.add(productPictureDTO.getPicUrl());
					}
					pfs.savePicture(ColtortiUtil.productSupplierId, null, sk.getSkuId(),picUrlList );
				}else{
					logger.warn("sku = " + sk.getSkuId() + " 无图片" );
					j++;
				}

			}

		}
		logger.warn("有图片sku总数为:"+i);
		logger.warn("无有图片sku总数为:"+j);
		return failCnt;
	}
	private int insertSpu(Collection<SpuDTO> spus){
		int failCnt=0;
		for (SpuDTO spu : spus) {
			try{
				pfs.saveSPU(spu);
			}catch(Exception e){
				try {
					pfs.updateMaterial(spu);
				} catch (ServiceException e1) {
					e1.printStackTrace();
				}
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
				pfs.savePicture(ColtortiUtil.productSupplierId, null, pic.getSkuId(), Arrays.asList(pic.getPicUrl()));
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

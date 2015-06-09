package com.shangpin.iog;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.coltorti.convert.ColtortiProductConvert;
import com.shangpin.iog.coltorti.dto.ColtortiProduct;
import com.shangpin.iog.coltorti.service.ColtortiProductService;
import com.shangpin.iog.coltorti.service.InsertDataBaseService;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;

public class Startup {
	private static Logger logger = LoggerFactory.getLogger(Startup.class);
	private static ApplicationContext factory;

	private static void loadSpringContext() {

		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	/**
	 * 抓取指定天数据，可传入两参数，开始时间，结束时间以：yyyy-MM-dd形式<br/>
	 * 若没有则取前一天到今天1天的数据
	 * @param args
	 */
	public static void main(String[] args) {
		// 加载spring
		loadSpringContext();
		String dateStart="",dateEnd="";
		if(args.length>1){
			dateStart=args[0];dateEnd=args[1];
		}else{
			dateStart=DateTimeUtil.getbeforeDay(1);
			dateEnd=DateTimeUtil.getShortCurrentDate();
		}
		Map<String,ProductFetchService> fetchsrvs=factory.getBeansOfType(ProductFetchService.class);
		ProductFetchService pfs=null;
		if(fetchsrvs!=null && fetchsrvs.size()>0)
			pfs=fetchsrvs.entrySet().iterator().next().getValue();
		if(pfs==null) return ;
		try {
			logger.info("抓取Coltorti数据开始，开始时间：{},结束时间:{}",dateStart,dateEnd);
			List<ColtortiProduct> coltorProds=ColtortiProductService.findProduct(dateStart, dateEnd);
			logger.info("抓取Coltorti数据成功，抓取到{},数据如下：\r\n{}",coltorProds.size(),new Gson().toJson(coltorProds));
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
			InsertDataBaseService dataSrv= new InsertDataBaseService(pfs);
			if(CollectionUtils.isNotEmpty(skus)) dataSrv.insertSku(skus);
			if(CollectionUtils.isNotEmpty(spus)) dataSrv.insertSpu(spus);
			
			Set<String> picSku=productPics.keySet();
			for (String sku : picSku) {
				Set<ProductPictureDTO> pcs=productPics.get(sku);
				if(CollectionUtils.isNotEmpty(pcs)){
					dataSrv.insertSkuPic(pcs);
				}
			}
		} catch (ServiceException e) {
			logger.error("抓取Coltorti数据失败。",e);
		}
		
	}

}

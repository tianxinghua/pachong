package com.shangpin.iog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	public static void main(String[] args) {

		// 加载spring
		loadSpringContext();
		String dateStart="",dateEnd="";
		if(args.length>1){
			dateStart=args[0];dateEnd=args[1];
		}else{
			dateStart="2015-06-01";
			dateEnd=DateTimeUtil.getShortCurrentDate();
		}
		
		ProductFetchService pfs=factory.getBean(ProductFetchService.class);
		if(pfs==null) return ;
		try {
			logger.info("抓取Coltorti数据开始，开始时间：{},结束时间:{}",dateStart,dateEnd);
			List<ColtortiProduct> coltorProds=ColtortiProductService.findProduct(dateStart, dateEnd);
			logger.info("抓取Coltorti数据成功，抓取到{},数据如下：\r\n{}",coltorProds.size(),new Gson().toJson(coltorProds));
			coltorProds=ColtortiProductService.product2sku(coltorProds);
			List<SkuDTO> skus=new ArrayList<>(coltorProds.size());
			List<SpuDTO> spus=new ArrayList<>(coltorProds.size());
			Map<String,Set<ProductPictureDTO>> productPics=new HashMap<String, Set<ProductPictureDTO>>();
			for (ColtortiProduct product : coltorProds) {
				SkuDTO sk=ColtortiProductConvert.product2sku(product);
				SpuDTO spu = ColtortiProductConvert.product2spu(product);
				skus.add(sk);spus.add(spu);
				pfs.saveSKU(sk);
				pfs.saveSPU(spu);
				Set<ProductPictureDTO> ppcs=ColtortiProductConvert.productPic(product);
				productPics.put(product.getSkuId(), ppcs);
			}
			logger.info("转换spu数：{},sku数{}",spus.size(),skus.size());
			// 保存数据
			pfs.saveSKU(skus);pfs.saveSPU(spus);
			
			Set<String> picSku=productPics.keySet();
			for (String sku : picSku) {
				Set<ProductPictureDTO> pcs=productPics.get(sku);
				List<ProductPictureDTO> imgUrl=new ArrayList<>(pcs.size());
				imgUrl.addAll(pcs);
				pfs.savePicture(imgUrl);
			}
		} catch (ServiceException e) {
			logger.error("抓取Coltorti数据失败。",e);
		}
		
	}

}

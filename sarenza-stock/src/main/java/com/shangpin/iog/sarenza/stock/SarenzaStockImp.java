package com.shangpin.iog.sarenza.stock;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.sarenza.stock.dto.Product;
import com.shangpin.iog.sarenza.util.FtpUtil;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.sop.AbsUpdateProductStock;

/**
 * Created by Administrator on 2015/7/8.
 */
@Component("sarenzaStockImp")
public class SarenzaStockImp extends AbsUpdateProductStock {

	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");

	@Autowired
	private ProductFetchService pfs;

	@Autowired
	ProductSearchService productSearchService;
	private static ApplicationContext factory;

	private static void loadSpringContext() {

		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	private static ResourceBundle bdl = null;
	private static String supplierId;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("sop");
		supplierId = bdl.getString("supplierId");
	}

	@Override
	public Map<String, Integer> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		Map<String, Integer> stock_map = new HashMap<String, Integer>();
		Map<String, String> stock = new HashMap<String, String>();
		List<Product> list = null;
		try {
			logger.info("get supplier file start");
			list = FtpUtil.readLocalCSV(Product.class);
			logger.info("get supplier file end ");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (list != null) {
			logger.info("list size " + list.size());
			for (com.shangpin.iog.sarenza.stock.dto.Product spu : list) {
				stock.put(spu.getVariantId(), spu.getStock());
//				stock.put(spu.getProductId()+"-"+spu.getPCID()+ "-"
//						+ spu.getSupplierSize(),spu.getStock() );
			}

		}
		
//		System.out.println(stock.size());
		int stocktem=0;
		for (String skuno : skuNo) {
			if(stock.containsKey(skuno)){
				stocktem = 0;
				try {
					stocktem = Integer.parseInt(stock.get(skuno).trim());
				} catch (NumberFormatException e) {
					loggerError.error(skuno +" 获取库存失败");
				}
				if(stocktem<0){
					stocktem = 0;
				}
				stock_map.put(skuno, stocktem);
			}else{
				stock_map.put(skuno, 0);
			}
						
		}
		logger.info("stock_map size =" + stock_map.size() );
		logger.info("stock_map map =" + stock_map.toString() );
		return stock_map;
	}

	public static void main(String[] args) throws Exception {
//		// 加载spring
//		loadSpringContext();
//		// //拉取数据
//		 SarenzaStockImp stockImp
//		 =(SarenzaStockImp)factory.getBean("SarenzaStock");
//		// AbsUpdateProductStock grabStockImp = new SpinnakerStockImp();
//		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		 logger.info("pozzilei更新数据库开始");
//		 try {
//		 stockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new
//		 Date()));
//		 } catch (Exception e) {
//		 loggerError.error("pozzilei更新库存失败."+e.getMessage());
//		 e.printStackTrace();
//		 }
//		 logger.info("pozzilei更新数据库结束");
//		 System.exit(0);

	}

}

package com.shangpin.iog.stockMonti.service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ShangPin.SOP.Api.ApiException;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuIce;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPage;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPageQuery;
import ShangPin.SOP.Entity.Api.Product.SopSkuIce;
import ShangPin.SOP.Servant.OpenApiServantPrx;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.DateTimeUtil;
import com.shangpin.ice.ice.IcePrxHelper;
import com.shangpin.iog.dto.SkuRelationDTO;
import com.shangpin.iog.dto.StockUpdateDTO;
import com.shangpin.iog.dto.StockUpdateLimitDTO;
import com.shangpin.iog.service.UpdateStockService;



@Component("stockMontiService")
public class StockMontiService {
	private static Logger logger = Logger.getLogger("info");
	@Autowired
	UpdateStockService updateStockService;
	private static OpenApiServantPrx servant = null;
	private static ResourceBundle bdl=null;
    private static String supplierId;
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        try {
			servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
			
		} catch (Exception e) {
			logger.error("ICE 代理失败");
		}
    }
	
	public void findSupplier(){
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<StockUpdateDTO> list = null;
		try {
			list = updateStockService.getAll();
			if(supplierId!=null){
				for(StockUpdateDTO stockUpdateDTO:list){
					if(stockUpdateDTO.getSupplierId().equals(supplierId)){
		    			Map<String,String> stocks = new HashMap<String,String>();
		    			Collection<String> skuNo = grabProduct(stockUpdateDTO.getSupplierId(), "2015-01-01 00:00", format.format(new Date()), stocks);
		    			updateStock(stockUpdateDTO.getSupplierId(),skuNo,stocks);
					}
				}
			}else{
				for(StockUpdateDTO stockUpdateDTO:list){
					if(null !=stockUpdateDTO && null !=stockUpdateDTO.getUpdateTime()){
						if("1".equals(stockUpdateDTO.getStatus())){
							long diff = new Date().getTime()-stockUpdateDTO.getUpdateTime().getTime();
				    		long hours = diff / (1000 * 60 * 60);
				    		logger.info("供应商："+stockUpdateDTO.getSupplierId()+"未更新时间："+hours);
				    		if(hours >= 2){
				    			Map<String,String> stocks = new HashMap<String,String>();
				    			Collection<String> skuNo = grabProduct(stockUpdateDTO.getSupplierId(), "2015-01-01 00:00", format.format(new Date()), stocks);
				    			updateStock(stockUpdateDTO.getSupplierId(),skuNo,stocks);
				    		}
						}
					}
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private  void updateStock(String supplier,Collection<String> skuNo,Map<String,String> skuSupplier){
		
		int faileCount=0;
		int updateCount=0;

		Map<String,Integer> toUpdateIce=new HashMap<>();
		for(String s:skuNo){
			String spSku = skuSupplier.get(s);
			toUpdateIce.put(spSku, 0);
		}
		Iterator<Entry<String, Integer>> iter=toUpdateIce.entrySet().iterator();
		logger.info(supplier+"待更新的数据总和：--------"+toUpdateIce.size());
		while (iter.hasNext()) {
			Entry<String, Integer> entry = iter.next();
			Boolean result =true;
			int stock = 0;
			for(int i=0;i<2;i++){  //发生错误 允许再执行一次
				try{
					if(entry.getValue()>=0){
						stock = entry.getValue();
					}
					result = servant.UpdateStock(supplier, entry.getKey(), stock);
					if(!result){
						faileCount++;
					}else{
						updateCount++;
					}
				}catch(Exception e){
					result=false;
					faileCount++;
					logger.error(supplier+"更新sku错误："+entry.getKey()+":"+stock+"---"+e.getMessage());
				} 
				if(result){
					i=2;
				}
			}
		}
		logger.info(supplier+"已更新的成功数据总和：--------"+updateCount);
		logger.info(supplier+"已更新的失败数据总和：--------"+faileCount);
	}
	
	/**
	 * 抓取主站商品SKU信息，等待更新库存<br/>
	 * @param supplier 供应商id
	 * @param start 主站数据开始时间
	 * @param end 主站数据结束时间
	 * @param stocks 本地sku编号与ice的sku键值对
	 * @return 供应商skuNo
	 * @throws Exception
	 */
	private Collection<String> grabProduct(String supplier,String start,String end,Map<String,String> stocks) throws Exception{
		int pageIndex=1,pageSize=100;
		
		boolean hasNext=true;
		Set<String> skuIds = new HashSet<String>();

		Date date  = new Date();
		while(hasNext){
			long startDate = System.currentTimeMillis();
			SopProductSkuPageQuery query = new SopProductSkuPageQuery(start,end,pageIndex,pageSize);
			List<SopProductSkuIce> skus = null;

			//如果异常次数超过5次就跳出
			for(int i=0;i<5;i++){
				startDate = System.currentTimeMillis();
				try {
					SopProductSkuPage products = servant.FindCommodityInfoPage(supplier, query);
					logger.warn("通过openAPI 获取第 "+ pageIndex +"页产品信息，信息耗时" + (System.currentTimeMillis() - startDate));
					logger.info("通过openAPI 获取第 "+ pageIndex +"页产品信息，信息耗时" + (System.currentTimeMillis() - startDate));
					skus = products.SopProductSkuIces;
					if(skus!=null){
						i=5;
					}
				} catch (ApiException e1) {
					logger.error("openAPI 获取产品信息出错 -ApiException -  "+e1.Message);
				}  catch (Exception e1) {
					logger.error("openAPI 获取产品信息出错",e1);
					logger.error("openAPI 获取产品信息出错"+e1.getMessage());
				}
			}

			for (SopProductSkuIce sku : skus) {
				List<SopSkuIce> skuIces = sku.SopSkuIces;
				for (SopSkuIce ice : skuIces) {

					if(null!=ice.SkuNo&&!"".equals(ice.SkuNo)&&null!=ice.SupplierSkuNo&&!"".equals(ice.SupplierSkuNo)){
						if(1!=ice.IsDeleted){
							skuIds.add(ice.SupplierSkuNo);
							stocks.put(ice.SupplierSkuNo,ice.SkuNo);
						}
					}
				}
			}
			pageIndex++;
			hasNext=(pageSize==skus.size());

		}
		logger.warn("获取icesku 结束");

		return skuIds;
	}
	
}

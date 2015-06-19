package com.shangpin.iog.apennine.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.apennine.conf.ApiUrl;
import com.shangpin.iog.apennine.utils.ApennineHttpUtil;
public class UpdateApennineStockService extends AbsUpdateProductStock{
	static Logger logger = LoggerFactory.getLogger(UpdateApennineStockService.class);
	static int pageIndex=1;
	static int pageSize=50;
	ApennineHttpUtil ApennineService;
	/*public static void updateStock(String start,String end){
		try {
			OpenApiServantPrx servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
			boolean hasNext=true;
			while(hasNext){
				SopProductSkuPageQuery query = new SopProductSkuPageQuery(start,end,pageIndex,pageSize);
				SopProductSkuPage products = servant.FindCommodityInfoPage("00000003", query);
				List<SopProductSkuIce> skus = products.SopProductSkuIces;
				for (SopProductSkuIce sku : skus) {
					List<SopSkuIce> skuIces = sku.SopSkuIces;
					for (SopSkuIce ice : skuIces) {
						String skuNo=ice.SkuNo;
						int scalarIdx=skuNo.lastIndexOf("#");//尺寸标志开始位置
						String productId=skuNo.substring(0, skuNo.lastIndexOf("-"));//产品id
						String recordId=skuNo.substring(0,scalarIdx);//记录id
						String scalarNo=skuNo.substring(scalarIdx+1);//尺寸编号
						Map<String, Map<String, Integer>> stoks=null;ColtortiStockService.getStock(productId, recordId);
						if(stoks!=null && stoks.size()>0){
							int quantity=stoks.get(recordId).get(scalarNo);
							Boolean result = servant.UpdateStock("00000003", skuNo, quantity);
							if(!result){
								logger.warn("更新SKU：{}，库存量：{}失败",skuNo,quantity);
							}
						}
					}
				}
				pageIndex++;
				hasNext=products.Total>pageSize;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	}*/
	@Override
	/**
	 * 抓取供应商库存数据 
	 * @param skuNo 供应商的每个产品的唯一编号：sku
	 * @see #grabProduct(String, String, String) 抓取主站SKU
	 * @return 每个sku对应的库存数
	 * @throws ServiceException 
	 */
	public Map<String, Integer> grabStock(Collection<String> skuNo)
			throws ServiceException {
		Iterator<String> it = skuNo.iterator();
		Map<String ,Integer>map=new HashMap<>();
		int stock = 0;
		while (it.hasNext()) {
			String skuId = it.next();
			try {
				stock=ApennineService.getHkstockByScode(ApiUrl.STOCK,skuId);
				map.put(skuId, stock);
			} catch (Exception e) {
				logger.error("-------- getHKstockByScode fail--------");
				e.printStackTrace();
			}
		}
		return map;
	}	
	private static Map<String, Integer> grabStock1(Collection<String> skuNo)
			throws ServiceException {
		Iterator<String> it = skuNo.iterator();
		Map<String ,Integer>map=new HashMap<>();
		ApennineHttpUtil ApennineService = new ApennineHttpUtil();
		int stock = 0;
		while (it.hasNext()) {
			String skuId = it.next();
			try {
				stock=ApennineService.getHkstockByScode(ApiUrl.STOCK,skuId);
				map.put(skuId, stock);
			} catch (Exception e) {
				logger.error("-------- getHKstockByScode fail--------");
				e.printStackTrace();
			}
		}
		return map;
	}	
	public static void main(String[] args) throws Exception {
		Map<String,Integer>map=new HashMap<>();
		UpdateApennineStockService stockService = new UpdateApennineStockService() ;
		/*Collection<String> skuNo=stockService.grabProduct("00000003","","");*/
		int stock = stockService.updateProductStock("00000003","","");
		//map=grabStock1(skuNo);
		System.out.println("更新失败 "+stock);//COSEPIAF
	}
}

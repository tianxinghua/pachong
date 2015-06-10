package com.shangpin.iog.apennine.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ShangPin.SOP.Entity.Api.Product.SopProductSkuIce;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPage;
import ShangPin.SOP.Entity.Api.Product.SopProductSkuPageQuery;
import ShangPin.SOP.Entity.Api.Product.SopSkuIce;
import ShangPin.SOP.Servant.OpenApiServantPrx;

import com.shangpin.ice.ice.IcePrxHelper;
public class UpdateApennineStockService {
	static Logger logger = LoggerFactory.getLogger(UpdateApennineStockService.class);
	static int pageIndex=1;
	static int pageSize=50;
	public static void updateStock(String start,String end){
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
						Map<String, Map<String, Integer>> stoks=null;/*ColtortiStockService.getStock(productId, recordId)*/;
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
	}	
}

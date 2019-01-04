package com.shangpin.api.airshop.config;

import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Date:     2016年1月12日 <br/>
 *
 * @author 陈小峰
 * @since JDK 7
 */
@ConfigurationProperties("airshop.api.url")
@Component
public class ApiServiceUrlConfig {
	
	
	@Setter
	private static String bindEmailAndChangePwd;//找回密码设置新密码
	public static String bindEmailAndChangePwd() {
		return host+ bindEmailAndChangePwd;
	}
	@Setter
	private static String setPwdForFind;//找回密码设置新密码
	public static String setPwdForFind() {
		return host+ setPwdForFind;
	}
	
	@Setter
	private static String changePwdAS;//登录后主动修改密码接口
	public static String changePwdAS() {
		return host+ changePwdAS;
	}
	
	@Setter
	private static String changeEmailAS;//登录后主动修改邮箱接口
	public static String changeEmailAS() {
		return host+ changeEmailAS;
	}
	
	@Setter
	private static String checkVerCodeForFind;//找回密码时验证 验证码是否正确
	public static String checkVerCodeForFind() {
		return host+ checkVerCodeForFind;
	}
	
	@Setter
	private static String getVerificationCode;//退货列表
	public static String getVerificationCode() {
		return host+ getVerificationCode;
	}
	@Setter
	private static String changePassword;//退货列表
	public static String changePassword() {
		return host+ changePassword;
	}
	
	
	
	
	@Setter
	private static String returnOrderList;//退货列表
	@Setter
	private static String findStockPage;//获取库存列表
	@Setter
	private static String findStockPageExport;//获取库存导出列表
	@Setter
	private static String updateSkuStock;//更新库存
	@Setter
	private static String findSkuStockImport;//获取库存列表
	
	@Setter
	private static String findNotOnShelfList;//获取库存列表
	
	
	public static String getNotOnShelfList() {
		return hbHost+ findNotOnShelfList;
	}
	
	public static String getReturnOrderList() {
		return host+ returnOrderList;
	}
	public static String getFindStockPage() {
		return host+ findStockPage;
	}

	public static String getFindStockPageExport() {
		return host+findStockPageExport;
	}

	public static String getUpdateSkuStock() {
		return host+updateSkuStock;
	}
	
	public static String getFindSkuStockImport() {
		return host+findSkuStockImport;
	}
	
	
	//tms地址
    @Setter
    private static String TMSUrl;
    public static String getTMSUrl() {
        return TMSUrl;
    }
  //tms地址
    @Setter
    private static String TMS;
    public static String getTMS() {
        return TMS;
    }
    
    //tms入库订单
    @Setter
    private static String saveInboundWayBill;
    public static String getSaveInboundWayBill() {
        return TMSUrl + saveInboundWayBill;
    }
    @Setter
    private static String searchTaskOrderDetail;
    public static String getSearchTaskOrderDetail() {
        return TMSUrl + searchTaskOrderDetail;
    }
    
    //tms出库订单
    @Setter
    private static String saveOutboundWayBill;
    public static String getSaveOutboundWayBill() {
        return TMSUrl + saveOutboundWayBill;
    }
    //tms创建批次物流单号
    @Setter
    private static String createTrack;
    public static String getCreateTrack() {
        return TMSUrl + createTrack;
    }
    //tmsFZF创建批次物流单号
    @Setter
    private static String createTrackFZF;
    public static String getCreateTrackFZF() {
        return TMSUrl + createTrackFZF;
    }
    //tms入库订单
    @Setter
    private static String getTrack;
    public static String getGetTrack() {
        return TMSUrl + getTrack;
    }
    @Setter
    private static String getTrackFZF;
    public static String getGetTrackFZF() {
        return TMSUrl + getTrackFZF;
    }
    @Setter
    private static String getTrackDetail;
    public static String getGetTrackDetail() {
        return TMSUrl + getTrackDetail;
    }
    @Setter
    private static String getTrackDetailFZF;
    public static String getGetTrackDetailFZF() {
        return TMSUrl + getTrackDetailFZF;
    }
    @Setter
    private static String getLogisticData;
    public static String getGetLogisticData() {
        return TMSUrl + getLogisticData;
    }

    @Setter
    private static String getPrintBoxInfo;
    public static String getPrintBoxInfo() {
        return TMSUrl + getPrintBoxInfo;
    }
    
	//dhl地址
    @Setter
    private static String dhlUrl;
    public static String getDhlUrl() {
        return dhlUrl;
    }
    //sop 一般贸易和跨境电商
    @Setter
    private static String countTradeAndCross;
    public static String getCountTradeAndCross() {
		return host + countTradeAndCross;
	}
    
    @Setter
    private static String findSupplierInfo;
    public static String getFindSupplierInfo() {
		return host + findSupplierInfo;
	}
    
    @Setter
    private static String findReturnAddress;
	public static String getFindReturnAddress() {
		return host + findReturnAddress;
	}
	
	 
    @Setter
    private static String findSupplierDeliverAddress;
	public static String getFindSupplierDeliverAddress() {
		return host + findSupplierDeliverAddress;
	}
	@Setter
    private static String findPorderSkuPrice;
	public static String getFindPorderSkuPrice() {
		return host + findPorderSkuPrice;
	}
	@Setter
    private static String modifyPurchasePrint;
	public static String getModifyPurchasePrint() {
		return host + modifyPurchasePrint;
	}
	@Setter
    private static String findPurchaseIsPrint;
	public static String getFindPurchaseIsPrint() {
		return host + findPurchaseIsPrint;
	}
	
	@Setter
    private static String findCGDDetailByOrderNo;
	public static String getFindCGDDetailByOrderNo() {
		return host + findCGDDetailByOrderNo;
	}
	@Setter
	private static String findPurchaseOrderPid;
	public static String getFindPurchaseOrderPid() {
		return host + findPurchaseOrderPid;
	}
//	@Setter
//	private static String modifyPurchaseOrderStatus;
//	public static String getModifyPurchaseOrderStatus() {
//		return host + modifyPurchaseOrderStatus;
//	}
	@Setter
	private static String findProductBrandInfoBySupplierOrder;
	public static String getFindProductBrandInfoBySupplierOrder() {
		return host + findProductBrandInfoBySupplierOrder;
	}
    @Setter
    private static String host;
    @Setter
    private static String login;
    @Setter
    private static String returnList;
    @Setter
    private static String returnOrder;
    @Setter
    private static String returnModifyOrder;
    @Setter
    private static String purchaseListUri;//采购单列表请求uri

    @Setter
    private static String deliverOrderListUri;//发货单列表请求uri
    @Setter
    private static String sorderOrderListUri;//发货单列表请求uri
    @Setter
    private static String findPorderCheckExport;//采购单库存标记数据导入请求uri
    @Setter
    private static String findPorderExportUri;//采购单明细数据导出。
    @Setter
    private static String findPorderImportUri;//采购单库存标记数据导入
    @Setter
    private static String  findSorderExportUri;//供应商已完结采购单导出
    @Setter
    private static String checkStockUri;//检查库存请求uri
    @Setter
    private static String stockUri;//采购单库存标记

    /**
     * 真直发 接口
     */
    @Setter
    private static String findDirectPorderbyPage;//真直发 采购单第一步列表请求uri\
    public static String getFindDirectPorderbyPage() {
        return url(findDirectPorderbyPage);
    }

    @Setter
    private static String findDirectPorderStockedbyPage; //真直发 采购单第二步 带发货列表请求接口
    public static String getFindDirectPorderStockedbyPage() {
        return url(findDirectPorderStockedbyPage);
    }

    @Setter
    private static String modifyDirectPorderStock;//真直发 采购单确认 库存标记
    public static String getModifyDirectPorderStock(){
        return url(modifyDirectPorderStock);
    }

    @Setter
    private static String findDirectPorderTransInfo;//真直发 采购单 发货信息获取接口
    public static String getFindDirectPorderTransInfo() {
        return url(findDirectPorderTransInfo);
    }

    @Setter
    private static String createDirectDeliveryOrder;//保存真直发采购单发货信息接口
    public static String getCreateDirectDeliveryOrder() {
        return url(createDirectDeliveryOrder);
    }

    @Setter
    private static String findDirectDorderbyPage; //真直发 第三步 发货列表数据接口
    public static String getFindDirectDorderbyPage() {
        return url(findDirectDorderbyPage);
    }

    @Setter
    private static String findDirectSupplierLogisticsCompany; //真直发 第三步补录国内段物流时用到的物流公司列表
    public static String getFindDirectSupplierLogisticsCompany() {
        return url(findDirectSupplierLogisticsCompany);
    }

    @Setter
    private static String  entryDirectDorderDomesticLogistics; //真直发 第三步 采购单录入国内段物流信息
    public static String getEntryDirectDorderDomesticLogistics() {
        return url(entryDirectDorderDomesticLogistics);
    }


    @Setter
    private static String findDirectDorderDetail;  //真直发 查询发货单详情
    public static String getFindDirectDorderDetail() {
        return url(findDirectDorderDetail);
    }

    @Setter
    private static String findSupplierSegments; // 查询直发供应商物流段信息
    public static String getFindSupplierSegments() {
        return url(findSupplierSegments) ;
    }

    @Setter
    private static String findDirectPorderDetail; //真直发 查询发货单详情
    public static String getFindDirectPorderDetail() {
        return url(findDirectPorderDetail);
    }

    @Setter
    private static String findDirectPorderCanceledbyPage; //真直发 查询取消采购单列表
    @Setter
    private static String findPorderCanceledbyPage; //代销无库直发、非直发 采购单已取消列表查询接口


    @Setter
    private static String findAllBrand;
    public static String getFindAllBrand() {
        return  url(findAllBrand);
    }



    @Setter
    private static String supplierUrl;
    @Setter
    private static String findPorder;//检查库存请求uri
    @Setter
    private static String findDorder;
    @Setter
    private static String createDeliveryorder;//检查库存请求uri
    @Setter
    private static String productHost;
    @Setter
    private static String productUri;
    @Setter
    private static String salesByDay;
    @Setter
    private static String salesByMonth;
    @Setter
    private static String quitList;
    @Setter
    private static String quitOrder;
    @Setter
    private static String quitModifyOrder;
    @Setter
    private static String salesByMonthPerformance;
    @Setter
    private static String salesByMonthWeek;
    @Setter
    private static String salesByMonthCategory;
    @Setter 
    private static String salesByMonthBrand;
    
    @Setter
    private static String queryBrandProductUrl;
    public static String getBrandProdutUrl(){
    	return hbHost+queryBrandProductUrl;
    }
    @Setter
    private static String batchSaveProduct;
    public static String getBatchSaveProduct() {
        return batchSaveProduct;
    }
    @Setter
    private static String queryProduct;
    public static String getQueryProduct() {
        return queryProduct;
    }
    @Setter
    private static String updateProduct;
    public static String getUpdateProduct() {
        return updateProduct;
    }
    @Setter
    private static String saveProduct;
    public static String getSaveProduct() {
        return saveProduct;
    }
    @Setter
    private static String picHost;
    public static String getPicHost() {
        return picHost;
    }
    @Setter
    private static String findProductById;
    public static String getFindProductById() {
        return findProductById;
    }
    @Setter
    private static String savePicture;
    public static String getSavePicture() {
        return savePicture;
    }
    public static String getProductHost() {
        return productHost;
    }

    public static String getProductUri() {
        return productHost + productUri;
    }

    public static String getLoginUrl() {
        return url(login);
    }

    public static String getReturnListUrl() {
        return url(returnList);
    }

    public static String getReturnOrderUrl() {
        return url(returnOrder);
    }

    public static String getReturnModifyOrder() {
        return url(returnModifyOrder);
    }

    public static String getCheckStockUri() {
        return url(checkStockUri);
    }
    
    public static String getStockUri(){
    	return url(stockUri);
    }

    public static String getPurchaseListUri() {
        return url(purchaseListUri);
    }

    public static String getFindDirectPorderCanceledbyPage() {
        return url(findDirectPorderCanceledbyPage);
    }

    public static String getFindPorderCanceledbyPage() {
        return url(findPorderCanceledbyPage);
    }




    public static String getDeliverOrderListUri() {
        return url(deliverOrderListUri);
    }
    public static String getSorderOrderListUri() {
        return url(sorderOrderListUri);
    }
    public static String getSupplierUrl() {
        return url(supplierUrl);
    }

    private static String url(String uri) {
        return host + uri;
    }

    public static String getFindPorder() {
        return url(findPorder);
    }

    public static String getFindDorder() {
        return url(findDorder);
    }

    public static String getCreateDeliveryorder() {
        return url(createDeliveryorder);
    }

    public static String getSalesByDayUrl() {
        return url(salesByDay);
    }

    public static String getSalesByMonthUrl() {
        return url(salesByMonth);
    }

    public static String getQuitListUrl() {
        return url(quitList);
    }

    public static String getQuitOrderUrl() {
        return url(quitOrder);
    }

    public static String getQuitModifyOrderrUrl() {
        return url(quitModifyOrder);
    }

    public static String getSalesByMonthBrandUrl() {
        return  url(salesByMonthBrand);
    }

    public static String getSalesByMonthPerformanceUrl() {
        return  url(salesByMonthPerformance);
    }

    public static String getSalesByMonthWeekUrl() {
        return  url(salesByMonthWeek);
    }

    public static String getSalesByMonthCategoryUrl() {
        return  url(salesByMonthCategory);
    }
    public static String getFindPorderCheckExportUri() {
        return url(findPorderCheckExport);
    }
    public static String getFindPorderExportUri() {
        return url(findPorderExportUri);
    }
    public static String getFindPorderImportUri() {
        return url(findPorderImportUri);
    }
    public static String getFindSorderExportUri() {
        return url(findSorderExportUri);
    }
	@Setter
    private static String ip;
	public static String getIp() {
		return ip;
	}


	@Setter
    private static String hbHost;
    /* Studio Relevant*/
    private static String hbUrl(String uri) {
        return hbHost + uri;
    }

    public static String getHbProductUri() {
        return hbHost + productUri;
    }


    /* 待拍照商品*/
    @Setter
    private static String studioPendingProducts;
    public static String getStudioPendingProducts() {
        return hbUrl(studioPendingProducts);
    }

    /* 获取供应商已申请的批次列表*/
    @Setter
    private static String studioSupplierSlots;
    public static String getStudioSupplierSlots() {
        return hbUrl(studioSupplierSlots);
    }

    /* 获取批次列表详情*/
    @Setter
    private static String studioSlot;
    public static String getStudioSlot() {
        return hbUrl(studioSlot);
    }
    /*删除供货商商品*/

    @Setter
    private static String delSlotProduct;
    public static String getdelSlotProduct() {
        return hbUrl(delSlotProduct);
    }




    /* 删除批次里面的商品*/
    @Setter
    private static String addSpuSlot;
    public static String getAddSpuSlot() {
        return hbUrl(addSpuSlot);
    }

    /* 删除批次里面的商品*/
    @Setter
    private static String delSpuSlot;
    public static String getdelSpuSlot() {
        return hbUrl(delSpuSlot);
    }

    /*添加物流信息*/
    @Setter
    private static String addLogisticSlot;
    public static String getAddLogisticSlot() {
        return hbUrl(addLogisticSlot);
    }
    /*获取发货单物流信息*/
    @Setter
    private static String slotLogisticInfo;
    public static String getSlotLogisticInfo() {
        return hbUrl(slotLogisticInfo);
    }

    @Setter
    private static String releaseStudioSlot;
    public static String getReleaseStudioSlot(){
        return hbUrl(releaseStudioSlot);
    }

    /* 验证批次，确定能否发货*/
    @Setter
    private static String checkSlot;
    public static String getCheckSlot() {
        return hbUrl(checkSlot);
    }
    /*批次打印*/
    @Setter
    private static String slotPrint;
    public static String getSlotPrint(){
        return hbUrl(slotPrint);
    }
    /* 可申请批次*/
    @Setter
    private static String studioSlots;
    public static String getStudioSlots() {
        return hbUrl(studioSlots);
    }
    /* 可申请批*/
    @Setter
    private static String applySlot;
    public static String getApplySlot() {
        return hbUrl(applySlot);
    }

    /*摄影棚列表*/
    @Setter
    private static String studioList;
    public static String getStudioList() {
        return hbUrl(studioList);
    }

    /*获取返回单*/
    @Setter
    private static String returnSlotList;
    public static String getReturnSlotList(){
        return hbUrl(returnSlotList);
    }

    /*接收返货单*/
    @Setter
    private static String receiveSlot;
    public static String getReceiveSlot(){
        return hbUrl(receiveSlot);
    }

    /*获取返货单详情*/
    @Setter
    private static String receivedSlotInfo;
    public static String getReceivedSlotInfo(){
        return hbUrl(receivedSlotInfo);
    }

    /*获取返货单详情*/
    @Setter
    private static String addProductFromScan;
    public static String getAddProductFromScan(){
        return hbUrl(addProductFromScan);
    }

    /*拣货完成后确认*/
    @Setter
    private static String confirmSlotInfo;
    public static String getConfirmSlotInfo(){
        return hbUrl(confirmSlotInfo);
    }

    /*产品添加*/
    @Setter
    private static String addDefective;
    public static String getAddDefective(){
        return hbUrl(addDefective);
    }

    /*studio 相关上传图片*/
    @Setter
    private static String savePictureForStudio;
    public static String getSavePictureForStudio(){
        return hbUrl(savePictureForStudio);
    }

    @Setter
    private static String defectiveList;
    public static String getDefectiveList(){
        return hbUrl(defectiveList);
    }

    @Setter
    private static String deleteDefective;
    public static String getDeleteDefective(){
        return hbUrl(deleteDefective);
    }
    
    @Setter
    private static String queryHubProduct;
	public static String getQueryHubProduct() {
		return hbUrl(queryHubProduct);
	}
	 @Setter
    private static String queryHubProductDetail;
	public static String getQueryHubProductDetail() {
		return hbUrl(queryHubProductDetail);
	}
	@Setter
	private static String addHubProduct;
	public static String addHubProduct() {
		return hbUrl(addHubProduct);
	}
	@Setter
	private static String updateHubProduct;
	public static String updateHubProduct() {
		return hbUrl(updateHubProduct);
	}

	@Setter
	private static String hubBatchSaveProduct;
	public static String getHubBatchSaveProduct() {
		return hbUrl(hubBatchSaveProduct);
	}

	@Setter
	private static String hubOrderDetailUrl;
	public static String getOrderDetail() {
		return hubOrderDetailUrl;
	}
}

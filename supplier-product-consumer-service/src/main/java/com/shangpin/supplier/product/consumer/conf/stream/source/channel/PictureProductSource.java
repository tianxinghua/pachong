package com.shangpin.supplier.product.consumer.conf.stream.source.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * <p>Title:PictureProductSource.java </p>
 * <p>Description: 供货商商品图片通道配置</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月7日 下午3:24:36
 */
public interface PictureProductSource {

	public String SUPPLIER_PICTURE = "supplierPictureProduct";
	
	public String BRAND_PICTURE = "brandPictureProduct";
	public String stefaniamode_picture = "stefaniamodePictureProduct";

	public String MCLABLES_PICTURE = "mclablesPictureProduct";

	public String ERALDO_PICTURE = "eraldoPictureProduct";

	public String FRMODA_PICTURE = "frmodaPictureProduct";

	public String REEBONZ_PICTURE = "reebonzPictureProduct";
	
	/**
	 * @return 供应商商品通用图片数据流通道组件
	 */
	@Output(value = PictureProductSource.SUPPLIER_PICTURE)
    public MessageChannel supplierPictureProduct();
	
	/**
	 * @return 品牌方通用图片数据流通道组件
	 */
	@Output(value = PictureProductSource.BRAND_PICTURE)
    public MessageChannel brandPictureProduct();
	
	/**
	 * @return stefaniamode通用图片数据流通道组件
	 */
	@Output(value = PictureProductSource.stefaniamode_picture)
    public MessageChannel stefaniamodePictureProduct();

	/**
	 * @return mclables通用图片数据流通道组件
	 */
	@Output(value = PictureProductSource.MCLABLES_PICTURE)
	public MessageChannel mclablesPictureProduct();


	/**
	 * @return eraldo通用图片数据流通道组件
	 */
	@Output(value = PictureProductSource.ERALDO_PICTURE)
	public MessageChannel eraldoPictureProduct();


	/**
	 * @return frmoda通用图片数据流通道组件
	 */
	@Output(value = PictureProductSource.FRMODA_PICTURE)
	public MessageChannel frmodaPictureProduct();


	/**
	 * @return reebonz通用图片数据流通道组件
	 */
	@Output(value = PictureProductSource.REEBONZ_PICTURE)
	public MessageChannel reebonzPictureProduct();
}

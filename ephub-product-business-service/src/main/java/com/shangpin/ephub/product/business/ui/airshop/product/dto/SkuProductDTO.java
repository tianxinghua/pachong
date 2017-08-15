package com.shangpin.ephub.product.business.ui.airshop.product.dto;
import java.io.Serializable;

public class SkuProductDTO  implements Serializable {
	 	
		/**
	 * 
	 */
	private static final long serialVersionUID = 5863785824699629979L;
		private String supplierId;
		private String skuId; //必须
		private String spuId; //必须
	    private String size;
	    private String barcode;//条形码
	    public String getBarcode() {
			return barcode;
		}
		public void setBarcode(String barcode) {
			this.barcode = barcode;
		}
		public String getSpSkuId() {
			return spSkuId;
		}
		public void setSpSkuId(String spSkuId) {
			this.spSkuId = spSkuId;
		}
		public String getSpStatus() {
			return spStatus;
		}
		public void setSpStatus(String spStatus) {
			this.spStatus = spStatus;
		}
		private String stock;
	    private String spSkuId;//尚品sku编号
	    public String getSupplierId() {
			return supplierId;
		}
		public void setSupplierId(String supplierId) {
			this.supplierId = supplierId;
		}
		public String getStock() {
			return stock;
		}
		public void setStock(String stock) {
			this.stock = stock;
		}
		private String status;  //1:unsubmitted   2：editing  3：submitted  4:delete
		private String spStatus;  
	    public String getSize() {
			return size;
		}
		public void setSize(String size) {
			this.size = size;
		}
		//商品状态待审核= 1, //上架 = 2,//待上架 = 3,//审核不通过 = 4,//已下架 = 5
		public String getSkuId() {
			return skuId;
		}
		public void setSkuId(String skuId) {
			this.skuId = skuId;
		}
		public String getSpuId() {
			return spuId;
		}
		public void setSpuId(String spuId) {
			this.spuId = spuId;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
}

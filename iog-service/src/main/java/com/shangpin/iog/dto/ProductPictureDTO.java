package com.shangpin.iog.dto;

import java.io.Serializable;

/**
 * Created by loyalty on 15/6/3.
 * SPU对象
 */
public class ProductPictureDTO implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4890916950574974903L;
	private String id;
    private String supplierId;
    private String skuId;
    private String spuId;
    private String picUrl;
	



    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((picUrl == null) ? 0 : picUrl.hashCode());
		result = prime * result + ((skuId == null) ? 0 : skuId.hashCode());
		result = prime * result + ((spuId == null) ? 0 : spuId.hashCode());
		result = prime * result
				+ ((supplierId == null) ? 0 : supplierId.hashCode());
		return result;
	}
    /**
     * skuId为空的情况看spuId,和picUrl
     * skuId不为空，看skuId,picUrl(若 还有spuId则还比较spuId)
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductPictureDTO other = (ProductPictureDTO) obj;
		/*if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;*/
		if (picUrl == null) {
			if (other.picUrl != null)
				return false;
		} else if (!picUrl.equals(other.picUrl))
			return false;
		
		if (skuId == null) {
			if (other.skuId != null)
				return false;
		} else if (!skuId.equals(other.skuId))
			return false;
		if (spuId == null) {
			if (other.spuId != null)
				return false;
		} else if (!spuId.equals(other.spuId))
			return false;
		if (supplierId == null) {
			if (other.supplierId != null)
				return false;
		} else if (!supplierId.equals(other.supplierId))
			return false;
		return true;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
	public String getSpuId() {
		return spuId;
	}
	public void setSpuId(String spuId) {
		this.spuId = spuId;
	}
    
	public static void main(String[] args) {
		ProductPictureDTO dto1=new ProductPictureDTO();
		ProductPictureDTO dto2=new ProductPictureDTO();
		dto1.setId("a");dto2.setId("b");
		dto1.setSkuId("a");dto2.setSkuId("a");
		//dto1.setSpuId("a");dto2.setSpuId("a");
		dto1.setPicUrl("aa1");dto2.setPicUrl("aa1");
		System.out.println(dto1.equals(dto2));
	}
}

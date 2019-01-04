package com.shangpin.iog.mongodomain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Created by loyalty on 15/6/3.
 * 图片对象
 */
@Document(collection="product-picture")
public class ProductPicture implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4890916950574974903L;
	@Id
	private String id;
    private String supplierId;
	private String spuId;
    private String skuId;
    private String picUrl;
    private String status;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((picUrl == null) ? 0 : picUrl.hashCode());
		result = prime * result + ((spuId == null) ? 0 : spuId.hashCode());
		result = prime * result + ((skuId == null) ? 0 : skuId.hashCode());
		result = prime * result
				+ ((supplierId == null) ? 0 : supplierId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProductPicture other = (ProductPicture) obj;


        if (spuId == null) {
            if (other.spuId != null)
                return false;
        } else if (!spuId.equals(other.spuId))
            return false;

        if (skuId == null) {
            if (other.skuId != null)
                return false;
        } else if (!skuId.equals(other.skuId))
            return false;

        if (supplierId == null) {
            if (other.supplierId != null)
                return false;
        } else if (!supplierId.equals(other.supplierId))
            return false;


        if (picUrl == null) {
            if (other.picUrl != null)
                return false;
        } else if (picUrl.equals(other.picUrl)){
            return true;
        }else  {
            return false;
        }


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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

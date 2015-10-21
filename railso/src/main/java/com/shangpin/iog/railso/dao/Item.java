package com.shangpin.iog.railso.dao;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Item
 * Created by loyalty on 15/6/5.
 */
public class Item {
    String title;
    String link;
    String description;
    String composition; //材质
    @SerializedName("g:id")
    String id;
    @SerializedName("g:mpn")
    String mpn;
    @SerializedName("g:condition")
    String condition;
    @SerializedName("g:image_link")
    String imageLinks;
    @SerializedName("g:additional_image_link")
    List add_images;
    @SerializedName("g:brand")
    String brand;
    @SerializedName("g:product_type")
    String productType;
    @SerializedName("g:google_product_category")//-----------
    String googleProductCategory;
    @SerializedName("g:gender")
    String gender;
    @SerializedName("g:age_group")
    String ageGroup;
    @SerializedName("g:color")
    String color;
    @SerializedName("g:size")
    List size;
    @SerializedName("g:custom_label_0")
    String customLabel0;
    @SerializedName("g:custom_label_2")
    String customLabel2;
    @SerializedName("g:custom_label_3")
    String customLabel3;
    public String getImageLinks() {
		return imageLinks;
	}

	public void setImageLinks(String imageLinks) {
		this.imageLinks = imageLinks;
	}

	public List getAdd_images() {
		List<String> images = new ArrayList<>();
        if (add_images != null && add_images.size() > 0) {
            for (Object picObj : add_images) {
                LinkedTreeMap picMap = (LinkedTreeMap)picObj;
                Object urlObj = picMap.get("$");
                if (urlObj != null) {
                	images.add(urlObj.toString());
                }
            }
        }
		return images;
	}

	public void setAdd_images(List add_images) {
		this.add_images = add_images;
	}

	public List getSize() {
		List<String> sizes = new ArrayList<>();
        if (size != null && size.size() > 0) {
            for (Object picObj : size) {
                LinkedTreeMap picMap = (LinkedTreeMap)picObj;
                Object urlObj = picMap.get("$");
                if (urlObj != null) {
                	sizes.add(urlObj.toString());
                }
            }
        }
        return sizes;
	}

	public void setSize(List size) {
		this.size = size;
	}

	@SerializedName("g:price")
    String price;
    @SerializedName("g:quantity")
    String availability;
    @SerializedName("g:item_group_id")
    String itemGroupId;
    @SerializedName("g:shipping")
    Shipping shipping;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMpn() {
        return mpn;
    }

    public void setMpn(String mpn) {
        this.mpn = mpn;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getGoogleProductCategory() {
        return googleProductCategory;
    }

    public void setGoogleProductCategory(String googleProductCategory) {
        this.googleProductCategory = googleProductCategory;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCustomLabel0() {
        return customLabel0;
    }

    public void setCustomLabel0(String customLabel0) {
        this.customLabel0 = customLabel0;
    }

    public String getCustomLabel2() {
        return customLabel2;
    }

    public void setCustomLabel2(String customLabel2) {
        this.customLabel2 = customLabel2;
    }

    public String getCustomLabel3() {
        return customLabel3;
    }

    public void setCustomLabel3(String customLabel3) {
        this.customLabel3 = customLabel3;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getItemGroupId() {
        return itemGroupId;
    }

    public void setItemGroupId(String itemGroupId) {
        this.itemGroupId = itemGroupId;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

	@Override
	public String toString() {
		return "Item [title=" + title + ", link=" + link + ", description="
				+ description + ", composition=" + composition + ", id=" + id
				+ ", mpn=" + mpn + ", condition=" + condition + ", imageLinks="
				+ imageLinks + ", add_images=" + add_images.toString() + ", brand="
				+ brand + ", productType=" + productType
				+ ", googleProductCategory=" + googleProductCategory
				+ ", gender=" + gender + ", ageGroup=" + ageGroup + ", color="
				+ color + ", size=" + size.toString() + ", customLabel0=" + customLabel0
				+ ", customLabel2=" + customLabel2 + ", customLabel3="
				+ customLabel3 + ", price=" + price + ", availability="
				+ availability + ", itemGroupId=" + itemGroupId + ", shipping="
				+ shipping + "]";
	}

//    @Override
//    public String toString() {
//        return "Item{" +
//                "title='" + title + '\'' +
//                ", link='" + link + '\'' +
//                ", description='" + description + '\'' +
//                ", composition='" + composition + '\'' +
//                ", id='" + id + '\'' +
//                ", mpn='" + mpn + '\'' +
//                ", condition='" + condition + '\'' +
//                ", imageLinks=" + imageLinks +
//                ", brand='" + brand + '\'' +
//                ", productType='" + productType + '\'' +
//                ", googleProductCategory='" + googleProductCategory + '\'' +
//                ", gender='" + gender + '\'' +
//                ", ageGroup='" + ageGroup + '\'' +
//                ", color='" + color + '\'' +
//                ", size='" + size + '\'' +
//                ", customLabel0='" + customLabel0 + '\'' +
//                ", customLabel2='" + customLabel2 + '\'' +
//                ", customLabel3='" + customLabel3 + '\'' +
//                ", price='" + price + '\'' +
//                ", availability='" + availability + '\'' +
//                ", itemGroupId='" + itemGroupId + '\'' +
//                ", shipping=" + shipping +
//                '}';
//    }
    
}

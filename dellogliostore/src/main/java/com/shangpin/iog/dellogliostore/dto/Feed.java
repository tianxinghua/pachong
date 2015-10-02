package com.shangpin.iog.dellogliostore.dto;

import com.google.gson.annotations.SerializedName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 *
 * Created by kelseo on 15/9/25.
 */
@XmlRootElement(name="feed")
@XmlAccessorType(XmlAccessType.NONE)
public class Feed {

    @XmlElement(name="source")
    String source;
    @XmlElement(name="source_url")
    String sourceUrl;
    @XmlElement(name="items")
    SpuItems spuItems;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public SpuItems getSpuItems() {
        return spuItems;
    }

    public void setSpuItems(SpuItems spuItems) {
        this.spuItems = spuItems;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "source='" + source + '\'' +
                ", sourceUrl='" + sourceUrl + '\'' +
                ", spuItems=" + spuItems +
                '}';
    }
}

package com.shangpin.iog.opticalscribe.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@XmlRootElement(name="rss")
public class Rss {
//	@XmlElement(name="channel")
	private Channel channel;
//	@XmlValue
/**
 * 
 * <div class="listing-item-contentSale" itemprop="offerDetails" itemscope="" itemtype="http://data-vocabulary.org/Offer">
    <span class="strike color-medium-grey listing-item-content-price" data-tstid="Label_ListingPrice">¥8,222</span>
    <span class="listing-item-percentOff" data-tstid="Label_PercentageOff">30% 折扣</span>
    <span class="listing-item-content-sale color-red" itemprop="price" data-tstid="Label_ListingSalePrice">¥5,755</span>
</div>
 * 	
 */
//	private String version;
}

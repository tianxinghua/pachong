package com.shangpin.iog.dellogliostore.dto;

/**
 *
 * Created by kelseo on 15/9/25.
 */
public class Photo {

    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "url='" + url + '\'' +
                '}';
    }
}

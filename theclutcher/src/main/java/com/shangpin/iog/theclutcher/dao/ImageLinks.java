package com.shangpin.iog.theclutcher.dao;

import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * Created by kelseo on 15/9/24.
 */
public class ImageLinks {

    @SerializedName("g:image_link")
    List links;

    public List<String> getLinks() {
        List<String> pics = new ArrayList<>();
        if (links != null && links.size() > 0) {
            for (Object picObj : links) {
                LinkedTreeMap picMap = (LinkedTreeMap)picObj;
                Object urlObj = picMap.get("$");
                if (urlObj != null) {
                    pics.add(urlObj.toString());
                }
            }
        }
        return pics;
    }

    public void setLinks(List links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "ImageLinks{" +
                "links=" + links +
                '}';
    }
}

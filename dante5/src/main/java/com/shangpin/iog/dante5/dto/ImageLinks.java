package com.shangpin.iog.dante5.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * Created by kelseo on 15/9/24.
 */
public class ImageLinks {

    @SerializedName("g:image_link")
    List<ImageLink> links;

    @Override
    public String toString() {
        return "ImageLinks{" +
                "links=" + links +
                '}';
    }
}

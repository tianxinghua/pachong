package com.shangpin.iog.dante5.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Channel
 * Created by kelseo on 15/9/18.
 */
public class Channel {

    String title;
    String link;
    String description;
    List<Item> item;


    @Override
    public String toString() {
        return "Channel{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", item=" + item +
                '}';
    }
}

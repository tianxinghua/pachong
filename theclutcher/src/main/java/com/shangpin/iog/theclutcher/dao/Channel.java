package com.shangpin.iog.theclutcher.dao;

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

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

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

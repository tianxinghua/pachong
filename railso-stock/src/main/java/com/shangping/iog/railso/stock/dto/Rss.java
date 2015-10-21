package com.shangping.iog.railso.stock.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Rss
 * Created by kelseo on 15/9/18.
 */
public class Rss {
    Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "Rss{" +
                "channel=" + channel +
                '}';
    }
}

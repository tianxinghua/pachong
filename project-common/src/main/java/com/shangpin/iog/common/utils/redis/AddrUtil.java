package com.shangpin.iog.common.utils.redis;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.JedisShardInfo;

/**
 * Created by loyalty on 15/2/28.
 * 获取地址
 */
public final class AddrUtil {

    private AddrUtil() {
        // Empty
    }

    public static List<JedisShardInfo> getAddresses(String s) {
        if (s == null) {
            throw new NullPointerException("Null host list");
        }
        if (s.trim().equals("")) {
            throw new IllegalArgumentException("No hosts in list:  ``" + s + "''");
        }
        List<JedisShardInfo> addrs = new ArrayList<>();

        for (String hoststuff : s.split("(?:\\s|,)+")) {
            if (hoststuff.equals("")) {
                continue;
            }

            int finalColon = hoststuff.lastIndexOf(':');
            if (finalColon < 1) {
                throw new IllegalArgumentException("Invalid server ``" + hoststuff
                        + "'' in list:  " + s);
            }
            String hostPart = hoststuff.substring(0, finalColon);
            String portNum = hoststuff.substring(finalColon + 1);
            JedisShardInfo jedisShardInfo = new JedisShardInfo(hostPart, Integer.valueOf(portNum));
            addrs.add(jedisShardInfo);
        }
        assert !addrs.isEmpty() : "No addrs found";
        return addrs;
    }
}

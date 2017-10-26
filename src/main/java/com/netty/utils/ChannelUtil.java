package com.netty.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

public class ChannelUtil {

    public static Map<String, Channel> userChannels = new ConcurrentHashMap<String,Channel>(1000);

}

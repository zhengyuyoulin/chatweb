package com.netty.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对用户通道和内部通道的管理
 *
 * @author yang.sun
 * @date 2016/7/12
 */
public class WebSocketChannelManager {

    public static final Map<String, Channel> userChannels;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketChannelManager.class);

    //推送服务器IP、推送服务和8082长连接服务连接channel
    private static final Map<String, Channel> innerChannels;

    static {
        userChannels = new HashMap<String, Channel>();
        innerChannels = new HashMap<String, Channel>();
    }

    public static Channel getUserChannel(String userId) {
        return userChannels.get(userId);
    }

    public static void registerUserChannel(String userId, Channel channel) {
        userChannels.put(userId, channel);
    }

    public static void removeUserChannel(String userId) {
        userChannels.remove(userId);
    }

    public static Channel getInnerChannel(String hostName) {
        return innerChannels.get(hostName);
    }

    public static Channel getOneInnerChannel() {
        try {
            String[] ary = innerChannels.keySet().toArray(new String[innerChannels.size()]);
            return innerChannels.get(ary[new Random().nextInt(ary.length)]);
        } catch (Exception e) {
        	logger.error("get push channel is failed, the inner channel size is" + innerChannels.size() + "  the cause: " + e);
            return null;
        }
    }

    public static void registerInnerChannel(String hostName, Channel channel) {
        innerChannels.put(hostName, channel);
    }

    public static void removeInnerChannel(String hostName) {
        innerChannels.remove(hostName);
    }
}

package com.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cai on 2017/10/16.
 */
public class WebSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    public static void main(String[] args) throws Exception {
        WebSocketServer wss = new WebSocketServer();
        wss.start();
    }

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public void start() throws InterruptedException {

        logger.info("WebSocketServer Starting...");
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap;
        ChannelFuture channelFuture = null;
        try {
            //对外长连接服务器
            bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //如果要求高实时性，有数据就马上发送，该选项设置为true关闭Nagle算法
                    //如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_RCVBUF, 256 * 1024)//接收缓冲器
                    .option(ChannelOption.SO_SNDBUF, 256 * 1024)//发送缓冲器
                    //当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度
                    .option(ChannelOption.SO_BACKLOG, 8192)
                    .childHandler(new WebSocketServerInitializer());
            //绑定端口
            channelFuture = bootstrap.bind(8012);

        } catch (Exception e) {
            logger.error("WebSocket Server Start ERROR", e);
            throw new RuntimeException(e);
        } finally {
            if (null != channelFuture) {
                channelFuture.sync().channel().closeFuture().sync();
            }

            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}

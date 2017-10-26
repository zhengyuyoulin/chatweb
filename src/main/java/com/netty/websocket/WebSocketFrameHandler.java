package com.netty.websocket;

import java.util.Map;

import com.netty.utils.ChannelUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    public WebSocketFrameHandler() {
    }

    @Override
    protected void messageReceived(ChannelHandlerContext context, WebSocketFrame webSocketFrame) throws Exception {
        String string = ((TextWebSocketFrame) webSocketFrame).text();
        System.out.println("read0" + string);
        /*for (String key : ChannelUtil.userChannels.keySet()) {
            if(!key.equals(context.channel().id().asLongText())){
                System.out.println("...");
                ByteBuf byteBuf = webSocketFrame.content();
                ChannelUtil.userChannels.get(key).writeAndFlush(new BinaryWebSocketFrame(byteBuf));
            }
        }*/
        for (Map.Entry<String, Channel> entry : ChannelUtil.userChannels.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(context.channel().id().asLongText());

            if(!entry.getKey().equals(context.channel().id().asLongText())){
                System.out.println("...");
                entry.getValue().writeAndFlush(new TextWebSocketFrame( string.toString()));
            }

        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
        String key = ctx.channel().id().asLongText();
        ChannelUtil.userChannels.put(key, ctx.channel());
    }

    //关闭成功后收到通知的事件
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
        String key = ctx.channel().id().asLongText();
        ChannelUtil.userChannels.remove(key);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught");
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
   }


}

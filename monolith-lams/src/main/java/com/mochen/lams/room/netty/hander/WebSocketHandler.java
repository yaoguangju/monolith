package com.mochen.lams.room.netty.hander;

import cn.hutool.extra.spring.SpringUtil;
import com.mochen.redis.common.manager.RedisManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private final ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("与客户端建立连接，通道开启！");
        //添加到channelGroup通道组
        channelGroup.add(ctx.channel());
        AttributeKey<String> key = AttributeKey.valueOf("user");
        ctx.channel().attr(key).set("1486499999486439426");
        log.info("id,{}",ctx.channel().id().toString());
        RedisManager redisManager = SpringUtil.getBean("redisManager");
        redisManager.setCacheHashMapValue("room:class_1:student_channel_id","1486499999486439426",ctx.channel().id().asLongText());
        channelGroup.find(ctx.channel().id());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("与客户端断开连接，通道关闭！");
        //添加到channelGroup 通道组
        channelGroup.remove(ctx.channel());
        AttributeKey<String> key = AttributeKey.valueOf("user");
        ctx.channel().attr(key).set("1486499999486439426");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 首次连接是FullHttpRequest，处理参数,将数据存入redis中
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            log.info("uri,{}",uri);

        }else if(msg instanceof TextWebSocketFrame){
            //正常的TEXT消息类型
            TextWebSocketFrame frame=(TextWebSocketFrame)msg;
            System.out.println("客户端收到服务器数据：" +frame.text());
            sendAllMessage(frame.text());
        }
        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) {

    }

    private void sendAllMessage(String message){
        //收到信息后，群发给所有channel
        channelGroup.writeAndFlush( new TextWebSocketFrame(message));
    }

    private void sendMessage(String message,String userId){
        //收到信息后，群发给所有channel
        channelGroup.find(this.channelMap.get(userId).id()).writeAndFlush(new TextWebSocketFrame(message));
    }

}

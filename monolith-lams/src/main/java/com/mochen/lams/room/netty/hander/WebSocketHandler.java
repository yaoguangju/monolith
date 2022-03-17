package com.mochen.lams.room.netty.hander;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mochen.lams.room.netty.message.Message;
import com.mochen.redis.common.manager.RedisManager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("与客户端建立连接，通道开启！");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("与客户端断开连接，通道关闭！");
        RedisManager redisManager = SpringUtil.getBean("redisManager");
        //添加到channelGroup 通道组
        AttributeKey<String> uidKey = AttributeKey.valueOf("uid");
        AttributeKey<String> classIdKey = AttributeKey.valueOf("classId");
        AttributeKey<String> typeKey = AttributeKey.valueOf("type");
        String uid = ctx.channel().attr(uidKey).get();
        String classId = ctx.channel().attr(classIdKey).get();
        String type = ctx.channel().attr(typeKey).get();
        if(type.equals("teacher")){
            redisManager.deleteObject("room:"+ classId + ":teacher_id");
        }else {
            // 学生
            redisManager.deleteCacheSet("room:"+ classId + ":student_id",uid);
        }
        channelGroup.remove(ctx.channel());
        channelMap.remove(uid);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 首次连接是FullHttpRequest，处理参数,将数据存入redis中
        if(msg instanceof TextWebSocketFrame){
            //正常的TEXT消息类型
            TextWebSocketFrame frame = (TextWebSocketFrame) msg;
            Message message = JSON.parseObject(frame.text(), Message.class);
            String type = message.getType();
            String content = message.getContent();
            String classId = message.getClassId();

            if(type.equals("connect")){
                JSONObject connectJsonObject = JSONObject.parseObject(content);
                String role = connectJsonObject.get("role").toString();
                String uid = connectJsonObject.get("uid").toString();
                RedisManager redisManager = SpringUtil.getBean("redisManager");

                // 将channel绑定uid属性
                AttributeKey<String> uidKey = AttributeKey.valueOf("uid");
                ctx.channel().attr(uidKey).set(uid);
                AttributeKey<String> classIdKey = AttributeKey.valueOf("classId");
                ctx.channel().attr(classIdKey).set(classId);
                AttributeKey<String> typeKey = AttributeKey.valueOf("type");
                ctx.channel().attr(typeKey).set(type);

                if(role.equals("teacher")){
                    redisManager.setCacheObject("room:"+ classId + ":teacher_id",uid);
                }else {
                    // 学生
                    redisManager.setCacheSet("room:"+ classId + ":student_id",uid);
                }
                channelGroup.add(ctx.channel());
                channelMap.put(uid, ctx.channel());


                Message sendMessage = new Message();
                sendMessage.setContent("连接成功");

                ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(sendMessage)));
            }
            if(type.equals("question")){
                JSONObject questionJsonObject = JSONObject.parseObject(content);
                String questionId = questionJsonObject.get("question_id").toString();
                // 构造消息
                Message sendMessage = new Message();
                sendMessage.setType("question");
                sendMessage.setClassId("1");
                sendMessage.setContent(questionId);
                sendAllStudentMessage(JSON.toJSONString(sendMessage),classId);
            }
        }

        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) {

    }

    // 给所有人发消息
    private void sendAllMessage(String message,String classId){
        RedisManager redisManager = SpringUtil.getBean("redisManager");

        String teacherId = redisManager.getCacheObject("room:" + classId + ":teacher_id");
        channelGroup.find(channelMap.get(teacherId).id()).writeAndFlush(new TextWebSocketFrame(message));

        Set<String> studentIdList = redisManager.getCacheSet("room:" + classId + ":teacher_id");
        studentIdList.forEach(studentId -> {

            channelGroup.find(channelMap.get(studentId).id()).writeAndFlush(new TextWebSocketFrame(message));
        });

    }

    // 给所有学生发消息
    private void sendAllStudentMessage(String message,String classId){
        //收到信息后，发回发送消息的客户端
        RedisManager redisManager = SpringUtil.getBean("redisManager");
        Set<String> studentIdList = redisManager.getCacheSet("room:" + classId + ":student_id");

        studentIdList.forEach(studentId -> {
            channelGroup.find(channelMap.get(studentId).id()).writeAndFlush(new TextWebSocketFrame(message));
        });



    }

    // 给老师发消息
    private void sendTeacherMessage(String message,String classId){
        //收到信息后，发回发送消息的客户端
        RedisManager redisManager = SpringUtil.getBean("redisManager");
        String teacherId = redisManager.getCacheObject("room:" + classId + ":teacher_id");
        channelGroup.find(channelMap.get(teacherId).id()).writeAndFlush(new TextWebSocketFrame(message));
    }

}

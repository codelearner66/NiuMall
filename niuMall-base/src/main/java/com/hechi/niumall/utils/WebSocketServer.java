package com.hechi.niumall.utils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hechi.niumall.entity.Niuinfo;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ccx
 * @date 2022/11/13 21:30
 */
@Slf4j
@Component
@ServerEndpoint("/socket/api/messageService/{userId}")
public class WebSocketServer{
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收userId
     */
    private String userId = "";

    /**
     * @param userId 将userid为用户token 用户连接时解析token获取用户id
     * 连接建立成
     * 功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) throws Exception {
        this.session = session;
        if (userId == null||userId.length() == 0) { return; }
        Claims claims = JwtUtil.parseJWT(userId);
        String subject;
        if (claims != null && (subject = claims.getSubject())!=null) {
            this.userId = subject;
        }else { return; }

        log.info("userId  {}",this.userId);
        if (webSocketMap.containsKey(this.userId)) {
            webSocketMap.remove(this.userId);
            //加入set中
            webSocketMap.put(this.userId, this);
        } else {
            //加入set中
            webSocketMap.put(this.userId, this);
            //在线数加1
            addOnlineCount();
        }
        log.info("用户连接:" + this.userId + ",当前在线人数为:" + getOnlineCount());
        //新建方法获取用户未读消息 并将信息封装发送到前台
        sendMessage("连接成功");
    }

    /**
     * 连接关闭
     * 调用的方法
     */
    @OnClose
    public void onClose() {
        /*
          判断用户会话是否存在 移除会话信息
         */
        if (webSocketMap.containsKey(userId)) {
            //从set中删除
            webSocketMap.remove(userId);
            //  更新在线人数
            subOnlineCount();
        }
        log.info("用户退出:" + userId + ",当前在线人数为:" + getOnlineCount());
    }

    /*
     * 收到客户端消 息后调用的方法
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:" + userId + ",报文:" + message);
        //可以群发消息
        //消息保存到数据库、redis
        //todo 设置消息实体类和与其对应的数据表
        Niuinfo niuinfo=null;
        if(null != message&&message.length() > 0){
            niuinfo=JSON.parseObject(message,Niuinfo.class);
        }
        if (Objects.nonNull(niuinfo)) {
            try {
                niuinfo.setFromid(Long.valueOf(this.userId));
                //传送给对应toUserId用户的websocket
                if (niuinfo.getToid()!=null && webSocketMap.containsKey(niuinfo.getToid().toString())) {
                    webSocketMap.get(niuinfo.getToid().toString()).sendMessage(message);
                } else {
                     //   不在线时信息处理
                    //否则不在这个服务器上，发送到mysql或者redis
                    log.error("请求的userId:" + niuinfo.getToid() + "不在该服务器上");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 实现服务
     * 器主动推送
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     *发送自定
     *义消息
     **/
    public static void sendInfo(String message, String userId) {
        log.info("发送消息到:"+userId+"，报文:"+message);
        if(StringUtils.isNotBlank(userId) && webSocketMap.containsKey(userId)){
            webSocketMap.get(userId).sendMessage(message);
        }else{
            log.error("用户"+userId+",不在线！");
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }
    //更新在线人数 加一
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount += 1;
    }

    //减一
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount -= 1;
    }

    //输出在线人数
    public static synchronized int getOnlineCount() {
        return WebSocketServer.onlineCount;
    }
}

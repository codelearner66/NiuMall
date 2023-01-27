package com.hechi.niumall.utils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hechi.niumall.entity.Niuinfo;
import com.hechi.niumall.service.impl.NiuinfoServiceImpl;
import com.hechi.niumall.vo.NiuinfoVo;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ccx
 * @date 2022/11/13 21:30
 */
@Slf4j
@Component
@ServerEndpoint("/socket/api/messageService/{userId}")
public class WebSocketServer {
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

    private static ApplicationContext applicationContext;
    /**
     * 接收userId
     */
    private String userId = "";

    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketServer.applicationContext = applicationContext;
    }

    /**
     * @param userId 将userid为用户token 用户连接时解析token获取用户id
     *               连接建立成
     *               功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) throws Exception {
        this.session = session;
        if (userId == null || userId.length() == 0) {
            return;
        }
        log.info("接收到的token {}", userId);
        Claims claims = JwtUtil.parseJWT(userId);
        String subject;
        if (claims != null && (subject = claims.getSubject()) != null && !webSocketMap.containsKey(subject)) {
            this.userId = subject;
            log.info("userId  {}", this.userId);
            if (webSocketMap.containsKey(this.userId) && this.userId != null && this.userId.length() != 0) {
                webSocketMap.remove(this.userId);
                //加入set中
                webSocketMap.put(this.userId, this);
            } else {
                if (this.userId != null && this.userId.length() != 0) {
                    //加入set中
                    webSocketMap.put(this.userId, this);
                    //在线数加1
                    addOnlineCount();
                }
            }
        } else {
            return;
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
    public void onMessage(@NotNull String message, Session session) {

        log.info("用户消息:" + userId + ",报文:" + message);

        NiuinfoServiceImpl niuinfoService = applicationContext.getBean(NiuinfoServiceImpl.class);
        //可以群发消息
        Niuinfo niuinfo = null;

        Map<String,String> map = JSON.parseObject(message, Map.class);
        log.info("解析出的map: {},{}",map.keySet(),map.values());
        if ("ping".equals(map.get("ping"))) {
            webSocketMap.get(this.userId).sendMessage("pong");
            return;
        }
        if (null != message && message.length() > 0&&!map.containsKey("ping")) {
            niuinfo = JSON.parseObject(message, Niuinfo.class);
        }
        if (Objects.nonNull(niuinfo)&&!map.containsKey("ping")) {
            niuinfo.setSendtime(new Date());
            try {
                niuinfo.setFromid(Long.valueOf(this.userId));
                niuinfoService.addNewInfo(niuinfo);
                //传送给对应toUserId用户的websocket
                if (niuinfo.getToid() != null && webSocketMap.containsKey(niuinfo.getToid().toString())) {
                    NiuinfoVo niuinfo1 = niuinfoService.getNiuinfo(niuinfo.getFromid(), niuinfo.getToid());
                    webSocketMap.get(niuinfo.getToid().toString()).sendMessage(JSON.toJSONString(niuinfo1));
                } else {
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
     * 发送自定
     * 义消息
     **/
    public static void sendInfo(String message, String userId) {
        log.info("发送消息到:" + userId + "，报文:" + message);
        if (StringUtils.isNotBlank(userId) && webSocketMap.containsKey(userId)) {
            webSocketMap.get(userId).sendMessage(message);
        } else {
            log.error("用户" + userId + ",不在线！");
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
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

package com.y3tu.tool.server.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Component;

import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author y3tu
 */
@ServerEndpoint("/websocket/message")
@Component
@Slf4j
public class MessageWebSocket {
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MessageServer对象
     */
    private static CopyOnWriteArraySet<MessageWebSocket> webSocketSet = new CopyOnWriteArraySet();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        //加入set中
        webSocketSet.add(this);
        //在线数加1
        addOnlineCount();
        log.info("有新窗口开始监听,当前在线人数为" + getOnlineCount());
        try {
            sendMessage("连接成功");
        } catch (Exception e) {
            log.error("websocket IO异常");
        }
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(Object obj) {


    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MessageWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MessageWebSocket.onlineCount--;
    }


}

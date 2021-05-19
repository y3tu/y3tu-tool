package com.y3tu.tool.lowcode.websocket;

import com.y3tu.tool.core.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 消息websocket
 *
 * @author y3tu
 */
@ServerEndpoint("/y3tu-tool-lowcode/websocket/message/{openId}")
@Component
@Slf4j
public class MessageEndPoint {
    private Session session;

    private static CopyOnWriteArraySet<MessageEndPoint> webSockets = new CopyOnWriteArraySet<>();
    private static Map<String, Session> sessionPool = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "openId") String openId) {
        this.session = session;
        webSockets.add(this);
        sessionPool.put(openId, session);
        log.info(openId + "【MessageEndPoint】有新的连接，总数为:" + webSockets.size());
    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);
        log.info("【MessageEndPoint】连接断开，总数为:" + webSockets.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("【MessageEndPoint】收到客户端消息:" + message);
    }


    /**
     * 此为广播消息
     */
    public void sendAllMessage(String message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage(message);
        messageDto.setType(MessageDto.TYPE_COMMON);
        sendAllMessage(messageDto);
    }

    /**
     * 此为广播消息
     */
    public void sendAllMessage(MessageDto messageDto) {
        for (MessageEndPoint webSocket : webSockets) {
            log.info("【MessageEndPoint】广播消息:" + messageDto.getMessage());
            try {
                webSocket.session.getAsyncRemote().sendText(JsonUtil.toJson(messageDto));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 此为单点消息
     */
    public void sendOneMessage(String openId, String message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage(message);
        messageDto.setType(MessageDto.TYPE_COMMON);
        sendObjMessage(openId, messageDto);
    }

    /**
     * 此为单点消息
     */
    public void sendObjMessage(String openId, MessageDto messageDto) {
        Session session = sessionPool.get(openId);
        if (session != null) {
            try {
                session.getAsyncRemote().sendText(JsonUtil.toJson(messageDto));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

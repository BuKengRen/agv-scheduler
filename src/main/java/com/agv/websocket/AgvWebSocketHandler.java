package com.agv.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class AgvWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(AgvWebSocketHandler.class);

    // 存储所有连接的会话
    private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    // 连接建立时
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("WebSocket 连接建立，当前连接数：{}", sessions.size());
    }

    // 收到消息时
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.info("收到客户端消息：{}", message.getPayload());
    }

    // 连接关闭时
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("WebSocket 连接关闭，当前连接数：{}", sessions.size());
    }

    // 广播消息给所有客户端
    public static void broadcast(String message) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                    log.info("推送消息：{}", message);
                } catch (IOException e) {
                    log.error("推送失败", e);
                }
            }
        }
    }
}
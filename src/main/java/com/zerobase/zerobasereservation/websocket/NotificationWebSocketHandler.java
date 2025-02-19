package com.zerobase.zerobasereservation.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class NotificationWebSocketHandler extends TextWebSocketHandler {

        // userId → WebSocketSession을 저장하는 맵
        private static final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            // 클라이언트에서 userId를 요청 파라미터로 전달한다고 가정
            String userId = getUserIdFromSession(session);
            if (userId != null) {
                userSessions.put(userId, session);
            }
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
            // 연결이 종료되면 해당 세션을 제거
            userSessions.values().remove(session);
        }

        public void sendNotification(String userId, String message)
            throws IOException {
            WebSocketSession session = userSessions.get(userId);
            if (session != null && session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        }

        private String getUserIdFromSession(WebSocketSession session) {
            // 요청 URL에서 userId 추출 (예: ws://localhost:8080/notifications?userId=123)
            return session.getUri().getQuery().split("=")[1]; // 간단한 방법 (실제 프로젝트에서는 검증 추가)
        }
    }



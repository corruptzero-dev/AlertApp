package ru.corruptzero.alert.websocket;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.corruptzero.alert.websocket.thread.WebSocketThread;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Getter
@Setter
public class AlertWebSocket extends TextWebSocketHandler {
    private WebSocketSession currentSession;
    private Long timestamp;
    private UUID clientUUID;
    private WebSocketThread thread;
    private Map<UUID, Boolean> clients = new HashMap<>();

    private void sendMessage(WebSocketSession session, TextMessage msg) {
        try {
            session.sendMessage(msg);
        } catch (IOException ex) {
            log.error("Error sending message" + ex.getMessage());
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("WEBSOCKET CONNECTION ESTABLISHED");
        thread = new WebSocketThread(session);
        thread.setInitialTime(System.currentTimeMillis());
        thread.start();
        currentSession = session;
        timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        clientUUID = UUID.randomUUID();
        sendMessage(session, new TextMessage("{\"id\": \"" + clientUUID + "\"" + ", \"message\": \"ping\"}"));
        log.info("Sent message: " + "{\"id\": \"" + clientUUID + "\"" + ", \"message\": \"ping\"}");

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            Thread.sleep(2000);
            thread.setInitialTime(System.currentTimeMillis());
            log.info("Got message: " + message.getPayload());
            if (session.isOpen()) {
                sendMessage(session, new TextMessage("{\"id\": \"" + clientUUID + "\"" + ", \"message\": \"ping\"}"));
                log.info("Sent message: " + "{\"id\": \"" + clientUUID + "\"" + ", \"message\": \"ping\"}");
            }
        } catch (Exception e) {
            log.error("Exception in AlertWebSocket: " + e.getMessage());
        }

    }

}

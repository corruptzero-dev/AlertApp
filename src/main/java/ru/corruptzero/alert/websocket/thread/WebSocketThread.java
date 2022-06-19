package ru.corruptzero.alert.websocket.thread;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@Getter
@Setter
public class WebSocketThread extends Thread {
    private final WebSocketSession session;
    private Boolean isActive;
    private Long initialTime;

    public WebSocketThread(WebSocketSession session) {
        this.session = session;
        this.isActive = false;
    }

    @Override
    public void run() {
        isActive = true;
        initialTime = System.currentTimeMillis();
        while (isActive) {
            try {
                sleep(1000);
                if (System.currentTimeMillis() - initialTime > 5000) {
                    log.error("CLIENT HAVEN'T RESPONDED, SESSION CLOSED!");
                    session.close();
                    isActive = false;
                    break;
                }
            } catch (InterruptedException | IOException e) {
                log.error("Exception in WebSocketThread: " + e.getMessage());
            }

        }
    }
}

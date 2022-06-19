package ru.corruptzero.alert.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import ru.corruptzero.alert.entity.Notification;
import ru.corruptzero.alert.repository.INotificationDAO;
import ru.corruptzero.alert.util.JsonConverter;
import ru.corruptzero.alert.websocket.AlertWebSocket;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class NotificationService implements INotificationService {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    private AlertWebSocket pongWebSocket;
    @Autowired
    private JsonConverter converter;
    @Autowired
    private INotificationDAO repository;

    private java.util.Date date;
    private java.sql.Timestamp timestamp;

    public NotificationService() {
    }

    public Notification getNotificationById(Long id) {
        return repository.getNotificationById(id);
    }

    public void scheduleNotification(Long id, Long scheduleTo, TimeUnit timeUnit) {
        ScheduledFuture<?> schedule = scheduler.schedule(() -> {
            try {
                date = new java.util.Date();
                timestamp = new java.sql.Timestamp(date.getTime());
                pongWebSocket.getCurrentSession().sendMessage(new TextMessage(converter.objToJson(getNotificationById(id))));
                repository.setNotificationLastSent(timestamp, id);
                log.info("SCHEDULED NOTIFICATION SENT!");
            } catch (IOException ex) {
                log.error("Sending notification exception in Notification Service: " + ex.getMessage());
            }
        }, scheduleTo, timeUnit);
        log.info("NOTIFICATION \"" + getNotificationById(id).getTitle() + "\" SCHEDULED TO " + scheduleTo + " " + timeUnit);
    }

    public void sendNotification(Long id) {
        date = new java.util.Date();
        timestamp = new java.sql.Timestamp(date.getTime());
        try {
            pongWebSocket.getCurrentSession().sendMessage(new TextMessage(converter.objToJson(getNotificationById(id))));
            repository.setNotificationLastSent(timestamp, id);
        } catch (IOException ex) {
            log.error("Sending notification exception in Notification Service: " + ex.getMessage());
        }
    }
}

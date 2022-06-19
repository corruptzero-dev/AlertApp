package ru.corruptzero.alert.service;

import ru.corruptzero.alert.entity.Notification;

import java.util.concurrent.TimeUnit;

public interface INotificationService {
    Notification getNotificationById(Long id);
    void scheduleNotification(Long id, Long scheduleTo, TimeUnit timeUnit);
    void sendNotification(Long id);
}

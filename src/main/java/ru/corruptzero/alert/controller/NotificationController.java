package ru.corruptzero.alert.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.corruptzero.alert.entity.Notification;
import ru.corruptzero.alert.entity.Type;
import ru.corruptzero.alert.repository.INotificationDAO;
import ru.corruptzero.alert.service.NotificationService;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/notifications")
@Slf4j
public class NotificationController {

    @Autowired
    NotificationService service;

    @Autowired
    INotificationDAO repo;

    @PostMapping()
    public ResponseEntity<Notification> saveNotification(@RequestParam(required = false) Long id,
                                                         @RequestParam(required = false) Timestamp lastSentAt,
                                                         @RequestParam String title,
                                                         @RequestParam String content,
                                                         @RequestParam Type type){
        Notification notification = new Notification();
        notification.setLastSentAt(Objects.requireNonNullElseGet(lastSentAt, () -> new Timestamp(new Date().getTime())));
        notification.setTitle(title);
        notification.setContent(content) ;
        notification.setType(type);
        repo.save(notification);
        return ResponseEntity.ok(notification);

    }

    @PostMapping("/{id}/send")
    public void sendNotification(@PathVariable Long id,
                                 @RequestParam(required = false) Long scheduleTo,
                                 @RequestParam(required = false) TimeUnit timeUnit){
        if (scheduleTo!=null && timeUnit!=null){
            service.scheduleNotification(id, scheduleTo, timeUnit);
        } else {
            service.sendNotification(id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notification> editNotification(@PathVariable Long id,
                                                         @RequestParam(required = false) Timestamp lastSentAt,
                                                         @RequestParam(required = false) String title,
                                                         @RequestParam(required = false) String content,
                                                         @RequestParam(required = false) Type type){
        Notification notification = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found for this id :: " + id));
        if(lastSentAt!=null) {
            notification.setLastSentAt(lastSentAt);
        } else {
            notification.setLastSentAt(repo.getNotificationById(id).getLastSentAt());
        }
        if(title!=null) notification.setTitle(title);
        if(content!=null) notification.setContent(content) ;
        if(type!=null) notification.setType(type);
        final Notification updatedNotification = repo.save(notification);
        return ResponseEntity.ok(updatedNotification);
    }

}

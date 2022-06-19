package ru.corruptzero.alert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.corruptzero.alert.entity.Notification;

import java.sql.Timestamp;

public interface INotificationDAO extends JpaRepository<Notification, Long> {
    Notification getNotificationById(Long id);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Transactional
    @Query("update Notification n set n.lastSentAt = :timestamp where n.id = :id")
    void setNotificationLastSent(@Param("timestamp") Timestamp timestamp, @Param("id") Long id);
}

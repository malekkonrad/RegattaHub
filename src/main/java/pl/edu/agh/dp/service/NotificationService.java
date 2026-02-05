package pl.edu.agh.dp.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.dp.api.Session;
import pl.edu.agh.dp.api.SessionFactory;
import pl.edu.agh.dp.config.OrmConfig;
import pl.edu.agh.dp.dto.NotificationDto;
import pl.edu.agh.dp.entity.Notification;
import pl.edu.agh.dp.entity.EmailNotification;
import pl.edu.agh.dp.entity.SmsNotification;
import pl.edu.agh.dp.entity.PushNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serwis dla operacji na Notification (TABLE_PER_CLASS inheritance).
 */
@Service
public class NotificationService {

    private final SessionFactory sessionFactory;

    public NotificationService() {
        this.sessionFactory = OrmConfig.getSessionFactory();
    }

    public NotificationDto create(NotificationDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Notification notification = dto.toEntity();
                session.save(notification);
                session.commit();
                return NotificationDto.fromEntity(notification);
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to create notification: " + e.getMessage(), e);
            }
        }
    }

    public Optional<NotificationDto> findById(Long id, String type) {
        try (Session session = sessionFactory.openSession()) {
            Notification notification = null;
            
            if ("EMAIL".equals(type)) {
                notification = session.find(EmailNotification.class, id);
            } else if ("SMS".equals(type)) {
                notification = session.find(SmsNotification.class, id);
            } else if ("PUSH".equals(type)) {
                notification = session.find(PushNotification.class, id);
            }
            
            return Optional.ofNullable(notification).map(NotificationDto::fromEntity);
        }
    }

    public List<NotificationDto> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<NotificationDto> result = new ArrayList<>();
            
            List<EmailNotification> emails = session.findAll(EmailNotification.class);
            List<SmsNotification> sms = session.findAll(SmsNotification.class);
            List<PushNotification> push = session.findAll(PushNotification.class);
            
            emails.forEach(n -> result.add(NotificationDto.fromEntity(n)));
            sms.forEach(n -> result.add(NotificationDto.fromEntity(n)));
            push.forEach(n -> result.add(NotificationDto.fromEntity(n)));
            
            return result;
        }
    }

    public List<NotificationDto> findAllEmails() {
        try (Session session = sessionFactory.openSession()) {
            return session.findAll(EmailNotification.class).stream()
                    .map(NotificationDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    public List<NotificationDto> findAllSms() {
        try (Session session = sessionFactory.openSession()) {
            return session.findAll(SmsNotification.class).stream()
                    .map(NotificationDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    public List<NotificationDto> findAllPush() {
        try (Session session = sessionFactory.openSession()) {
            return session.findAll(PushNotification.class).stream()
                    .map(NotificationDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    public Optional<NotificationDto> update(Long id, NotificationDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Notification updated = dto.toEntity();
                updated.setId(id);
                session.update(updated);
                session.commit();
                return Optional.of(NotificationDto.fromEntity(updated));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to update notification: " + e.getMessage(), e);
            }
        }
    }

    public boolean delete(Long id, String type) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Notification notification = null;
                
                if ("EMAIL".equals(type)) {
                    notification = session.find(EmailNotification.class, id);
                } else if ("SMS".equals(type)) {
                    notification = session.find(SmsNotification.class, id);
                } else if ("PUSH".equals(type)) {
                    notification = session.find(PushNotification.class, id);
                }
                
                if (notification == null) {
                    return false;
                }
                session.delete(notification);
                session.commit();
                return true;
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to delete notification: " + e.getMessage(), e);
            }
        }
    }
}

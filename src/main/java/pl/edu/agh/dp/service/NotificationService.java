package pl.edu.agh.dp.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.dp.api.Session;
import pl.edu.agh.dp.api.SessionFactory;
import pl.edu.agh.dp.config.OrmConfig;
import pl.edu.agh.dp.dto.NotificationDto;
import pl.edu.agh.dp.dto.EmailNotificationDto;
import pl.edu.agh.dp.dto.SmsNotificationDto;
import pl.edu.agh.dp.dto.PushNotificationDto;
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

    public EmailNotificationDto createEmail(EmailNotificationDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                EmailNotification notification = (EmailNotification) dto.toEntity();
                session.save(notification);
                session.commit();
                return EmailNotificationDto.fromEntity(notification);
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to create email notification: " + e.getMessage(), e);
            }
        }
    }

    public SmsNotificationDto createSms(SmsNotificationDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                SmsNotification notification = (SmsNotification) dto.toEntity();
                session.save(notification);
                session.commit();
                return SmsNotificationDto.fromEntity(notification);
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to create SMS notification: " + e.getMessage(), e);
            }
        }
    }

    public PushNotificationDto createPush(PushNotificationDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                PushNotification notification = (PushNotification) dto.toEntity();
                session.save(notification);
                session.commit();
                return PushNotificationDto.fromEntity(notification);
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to create push notification: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Znajduje powiadomienie po ID i automatycznie rzutuje na odpowiednie DTO.
     * Przeszukuje wszystkie typy powiadomień.
     */
    public Optional<NotificationDto> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {

            Notification notification = session.find(Notification.class, id);
            if (notification != null) {
                return Optional.of(NotificationDto.fromEntity(notification));
            }

            return Optional.empty();
        }
    }

    public Optional<EmailNotificationDto> findEmailById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            EmailNotification notification = session.find(EmailNotification.class, id);
            return Optional.ofNullable(notification).map(EmailNotificationDto::fromEntity);
        }
    }

    public Optional<SmsNotificationDto> findSmsById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            SmsNotification notification = session.find(SmsNotification.class, id);
            return Optional.ofNullable(notification).map(SmsNotificationDto::fromEntity);
        }
    }

    public Optional<PushNotificationDto> findPushById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            PushNotification notification = session.find(PushNotification.class, id);
            return Optional.ofNullable(notification).map(PushNotificationDto::fromEntity);
        }
    }

    public List<NotificationDto> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<NotificationDto> result = new ArrayList<>();

            List<Notification> notifications = session.findAll(Notification.class);

            notifications.forEach(notification -> result.add(NotificationDto.fromEntity(notification)));

            return result;
        }
    }

    public List<EmailNotificationDto> findAllEmails() {
        try (Session session = sessionFactory.openSession()) {
            return session.findAll(EmailNotification.class).stream()
                    .map(EmailNotificationDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    public List<SmsNotificationDto> findAllSms() {
        try (Session session = sessionFactory.openSession()) {
            return session.findAll(SmsNotification.class).stream()
                    .map(SmsNotificationDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    public List<PushNotificationDto> findAllPush() {
        try (Session session = sessionFactory.openSession()) {
            return session.findAll(PushNotification.class).stream()
                    .map(PushNotificationDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    public Optional<EmailNotificationDto> updateEmail(Long id, EmailNotificationDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                EmailNotification updated = (EmailNotification) dto.toEntity();
                updated.setId(id);
                session.update(updated);
                session.commit();
                return Optional.of(EmailNotificationDto.fromEntity(updated));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to update email notification: " + e.getMessage(), e);
            }
        }
    }

    public Optional<SmsNotificationDto> updateSms(Long id, SmsNotificationDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                SmsNotification updated = (SmsNotification) dto.toEntity();
                updated.setId(id);
                session.update(updated);
                session.commit();
                return Optional.of(SmsNotificationDto.fromEntity(updated));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to update SMS notification: " + e.getMessage(), e);
            }
        }
    }

    public Optional<PushNotificationDto> updatePush(Long id, PushNotificationDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                PushNotification updated = (PushNotification) dto.toEntity();
                updated.setId(id);
                session.update(updated);
                session.commit();
                return Optional.of(PushNotificationDto.fromEntity(updated));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to update push notification: " + e.getMessage(), e);
            }
        }
    }

    public boolean deleteEmail(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                EmailNotification notification = session.find(EmailNotification.class, id);
                if (notification == null) {
                    return false;
                }
                session.delete(notification);
                session.commit();
                return true;
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to delete email notification: " + e.getMessage(), e);
            }
        }
    }

    public boolean deleteSms(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                SmsNotification notification = session.find(SmsNotification.class, id);
                if (notification == null) {
                    return false;
                }
                session.delete(notification);
                session.commit();
                return true;
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to delete SMS notification: " + e.getMessage(), e);
            }
        }
    }

    public boolean deletePush(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                PushNotification notification = session.find(PushNotification.class, id);
                if (notification == null) {
                    return false;
                }
                session.delete(notification);
                session.commit();
                return true;
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to delete push notification: " + e.getMessage(), e);
            }
        }
    }
}

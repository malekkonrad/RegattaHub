package pl.edu.agh.dp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.agh.dp.entity.Notification;
import pl.edu.agh.dp.entity.EmailNotification;
import pl.edu.agh.dp.entity.SmsNotification;
import pl.edu.agh.dp.entity.PushNotification;

import java.time.LocalDateTime;

/**
 * DTO dla hierarchii Notification (TABLE_PER_CLASS inheritance).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    private Long id;
    private String title;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private Boolean isRead;
    private String status;
    
    // Typ powiadomienia
    private String notificationType; // EMAIL, SMS, PUSH
    
    // Pola EmailNotification
    private String recipientEmail;
    private String subject;
    private String senderEmail;
    private String ccEmails;
    private String bccEmails;
    private Boolean isHtml;
    
    // Pola SmsNotification
    private String phoneNumber;
    private String senderNumber;
    private String carrier;
    private Boolean deliveryReport;
    private Integer messageParts;
    
    // Pola PushNotification
    private String deviceToken;
    private String platform;
    private String category;
    private String actionUrl;
    private String imageUrl;
    private Integer badgeCount;

    public static NotificationDto fromEntity(Notification notification) {
        if (notification == null) return null;

        NotificationDto.NotificationDtoBuilder builder = NotificationDto.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .sentAt(notification.getSentAt())
                .isRead(notification.getIsRead())
                .status(notification.getStatus());

        if (notification instanceof EmailNotification en) {
            builder.notificationType("EMAIL")
                    .recipientEmail(en.getRecipientEmail())
                    .subject(en.getSubject())
                    .senderEmail(en.getSenderEmail())
                    .ccEmails(en.getCcEmails())
                    .bccEmails(en.getBccEmails())
                    .isHtml(en.getIsHtml());
        } else if (notification instanceof SmsNotification sn) {
            builder.notificationType("SMS")
                    .phoneNumber(sn.getPhoneNumber())
                    .senderNumber(sn.getSenderNumber())
                    .carrier(sn.getCarrier())
                    .deliveryReport(sn.getDeliveryReport())
                    .messageParts(sn.getMessageParts());
        } else if (notification instanceof PushNotification pn) {
            builder.notificationType("PUSH")
                    .deviceToken(pn.getDeviceToken())
                    .platform(pn.getPlatform())
                    .category(pn.getCategory())
                    .actionUrl(pn.getActionUrl())
                    .imageUrl(pn.getImageUrl())
                    .badgeCount(pn.getBadgeCount());
        } else {
            builder.notificationType("BASE");
        }

        return builder.build();
    }

    public Notification toEntity() {
        Notification notification;
        
        if ("EMAIL".equals(notificationType)) {
            EmailNotification en = new EmailNotification();
            en.setRecipientEmail(this.recipientEmail);
            en.setSubject(this.subject);
            en.setSenderEmail(this.senderEmail);
            en.setCcEmails(this.ccEmails);
            en.setBccEmails(this.bccEmails);
            en.setIsHtml(this.isHtml);
            notification = en;
        } else if ("SMS".equals(notificationType)) {
            SmsNotification sn = new SmsNotification();
            sn.setPhoneNumber(this.phoneNumber);
            sn.setSenderNumber(this.senderNumber);
            sn.setCarrier(this.carrier);
            sn.setDeliveryReport(this.deliveryReport);
            sn.setMessageParts(this.messageParts);
            notification = sn;
        } else if ("PUSH".equals(notificationType)) {
            PushNotification pn = new PushNotification();
            pn.setDeviceToken(this.deviceToken);
            pn.setPlatform(this.platform);
            pn.setCategory(this.category);
            pn.setActionUrl(this.actionUrl);
            pn.setImageUrl(this.imageUrl);
            pn.setBadgeCount(this.badgeCount);
            notification = pn;
        } else {
            notification = new Notification();
        }

        notification.setId(this.id);
        notification.setTitle(this.title);
        notification.setMessage(this.message);
        notification.setCreatedAt(this.createdAt);
        notification.setSentAt(this.sentAt);
        notification.setIsRead(this.isRead);
        notification.setStatus(this.status);

        return notification;
    }
}

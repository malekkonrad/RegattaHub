package pl.edu.agh.dp.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.edu.agh.dp.entity.Notification;
import pl.edu.agh.dp.entity.EmailNotification;
import pl.edu.agh.dp.entity.SmsNotification;
import pl.edu.agh.dp.entity.PushNotification;

import java.time.LocalDateTime;

/**
 * Bazowe DTO dla hierarchii Notification (TABLE_PER_CLASS inheritance).
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "notificationType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EmailNotificationDto.class, name = "EMAIL"),
        @JsonSubTypes.Type(value = SmsNotificationDto.class, name = "SMS"),
        @JsonSubTypes.Type(value = PushNotificationDto.class, name = "PUSH")
})
public class NotificationDto {

    private Long id;
    private String title;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private Boolean isRead;
    private String status;

    /**
     * Konwertuje encję Notification na odpowiednie DTO w hierarchii.
     */
    public static NotificationDto fromEntity(Notification notification) {
        if (notification == null) return null;

        if (notification instanceof EmailNotification en) {
            return EmailNotificationDto.builder()
                    .id(en.getId())
                    .title(en.getTitle())
                    .message(en.getMessage())
                    .createdAt(en.getCreatedAt())
                    .sentAt(en.getSentAt())
                    .isRead(en.getIsRead())
                    .status(en.getStatus())
                    .recipientEmail(en.getRecipientEmail())
                    .subject(en.getSubject())
                    .senderEmail(en.getSenderEmail())
                    .ccEmails(en.getCcEmails())
                    .bccEmails(en.getBccEmails())
                    .isHtml(en.getIsHtml())
                    .attachments(en.getAttachments())
                    .build();
        } else if (notification instanceof SmsNotification sn) {
            return SmsNotificationDto.builder()
                    .id(sn.getId())
                    .title(sn.getTitle())
                    .message(sn.getMessage())
                    .createdAt(sn.getCreatedAt())
                    .sentAt(sn.getSentAt())
                    .isRead(sn.getIsRead())
                    .status(sn.getStatus())
                    .phoneNumber(sn.getPhoneNumber())
                    .senderNumber(sn.getSenderNumber())
                    .carrier(sn.getCarrier())
                    .deliveryReport(sn.getDeliveryReport())
                    .messageParts(sn.getMessageParts())
                    .build();
        } else if (notification instanceof PushNotification pn) {
            return PushNotificationDto.builder()
                    .id(pn.getId())
                    .title(pn.getTitle())
                    .message(pn.getMessage())
                    .createdAt(pn.getCreatedAt())
                    .sentAt(pn.getSentAt())
                    .isRead(pn.getIsRead())
                    .status(pn.getStatus())
                    .deviceToken(pn.getDeviceToken())
                    .platform(pn.getPlatform())
                    .category(pn.getCategory())
                    .actionUrl(pn.getActionUrl())
                    .imageUrl(pn.getImageUrl())
                    .badgeCount(pn.getBadgeCount())
                    .isSilent(pn.getIsSilent())
                    .timeToLive(pn.getTimeToLive())
                    .build();
        }

        // Bazowy typ (nie powinien występować w praktyce)
        return NotificationDto.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .sentAt(notification.getSentAt())
                .isRead(notification.getIsRead())
                .status(notification.getStatus())
                .build();
    }

    /**
     * Konwertuje DTO na encję. Podklasy nadpisują tę metodę.
     */
    public Notification toEntity() {
        Notification notification = new Notification();
//        notification.setId(this.id);
        notification.setTitle(this.title);
        notification.setMessage(this.message);
        notification.setCreatedAt(this.createdAt);
        notification.setSentAt(this.sentAt);
        notification.setIsRead(this.isRead);
        notification.setStatus(this.status);
        return notification;
    }

    /**
     * Wypełnia wspólne pola encji z DTO.
     */
    protected void fillCommonFields(Notification notification) {
//        notification.setId(this.id);
        notification.setTitle(this.title);
        notification.setMessage(this.message);
        notification.setCreatedAt(this.createdAt);
        notification.setSentAt(this.sentAt);
        notification.setIsRead(this.isRead);
        notification.setStatus(this.status);
    }

    /**
     * Zwraca typ powiadomienia (do serializacji JSON).
     */
    public String getNotificationType() {
        if (this instanceof EmailNotificationDto) return "EMAIL";
        if (this instanceof SmsNotificationDto) return "SMS";
        if (this instanceof PushNotificationDto) return "PUSH";
        return "BASE";
    }
}

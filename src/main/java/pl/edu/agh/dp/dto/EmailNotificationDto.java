package pl.edu.agh.dp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.edu.agh.dp.entity.EmailNotification;
import pl.edu.agh.dp.entity.Notification;

/**
 * DTO dla EmailNotification.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmailNotificationDto extends NotificationDto {

    private String recipientEmail;
    private String subject;
    private String senderEmail;
    private String ccEmails;
    private String bccEmails;
    private Boolean isHtml;
    private String attachments;

    @Override
    public Notification toEntity() {
        EmailNotification entity = new EmailNotification();
        fillCommonFields(entity);
        entity.setRecipientEmail(this.recipientEmail);
        entity.setSubject(this.subject);
        entity.setSenderEmail(this.senderEmail);
        entity.setCcEmails(this.ccEmails);
        entity.setBccEmails(this.bccEmails);
        entity.setIsHtml(this.isHtml);
        entity.setAttachments(this.attachments);
        return entity;
    }

    /**
     * Tworzy DTO z encji EmailNotification.
     */
    public static EmailNotificationDto fromEntity(EmailNotification entity) {
        if (entity == null) return null;
        return EmailNotificationDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .message(entity.getMessage())
                .createdAt(entity.getCreatedAt())
                .sentAt(entity.getSentAt())
                .isRead(entity.getIsRead())
                .status(entity.getStatus())
                .recipientEmail(entity.getRecipientEmail())
                .subject(entity.getSubject())
                .senderEmail(entity.getSenderEmail())
                .ccEmails(entity.getCcEmails())
                .bccEmails(entity.getBccEmails())
                .isHtml(entity.getIsHtml())
                .attachments(entity.getAttachments())
                .build();
    }
}

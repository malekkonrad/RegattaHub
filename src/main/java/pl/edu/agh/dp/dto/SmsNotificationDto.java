package pl.edu.agh.dp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.edu.agh.dp.entity.Notification;
import pl.edu.agh.dp.entity.SmsNotification;

/**
 * DTO dla SmsNotification.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SmsNotificationDto extends NotificationDto {

    private String phoneNumber;
    private String senderNumber;
    private String carrier;
    private Boolean deliveryReport;
    private Integer messageParts;

    @Override
    public Notification toEntity() {
        SmsNotification entity = new SmsNotification();
        fillCommonFields(entity);
        entity.setPhoneNumber(this.phoneNumber);
        entity.setSenderNumber(this.senderNumber);
        entity.setCarrier(this.carrier);
        entity.setDeliveryReport(this.deliveryReport);
        entity.setMessageParts(this.messageParts);
        return entity;
    }

    /**
     * Tworzy DTO z encji SmsNotification.
     */
    public static SmsNotificationDto fromEntity(SmsNotification entity) {
        if (entity == null) return null;
        return SmsNotificationDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .message(entity.getMessage())
                .createdAt(entity.getCreatedAt())
                .sentAt(entity.getSentAt())
                .isRead(entity.getIsRead())
                .status(entity.getStatus())
                .phoneNumber(entity.getPhoneNumber())
                .senderNumber(entity.getSenderNumber())
                .carrier(entity.getCarrier())
                .deliveryReport(entity.getDeliveryReport())
                .messageParts(entity.getMessageParts())
                .build();
    }
}

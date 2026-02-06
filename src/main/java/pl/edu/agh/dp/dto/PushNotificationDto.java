package pl.edu.agh.dp.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.edu.agh.dp.entity.Notification;
import pl.edu.agh.dp.entity.PushNotification;

/**
 * DTO dla PushNotification.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class PushNotificationDto extends NotificationDto {

    private String deviceToken;
    private String platform;
    private String category;
    private String actionUrl;
    private String imageUrl;
    private Integer badgeCount;
    private Boolean isSilent;
    private Integer timeToLive;

    @Override
    public Notification toEntity() {
        PushNotification entity = new PushNotification();
        fillCommonFields(entity);
        entity.setDeviceToken(this.deviceToken);
        entity.setPlatform(this.platform);
        entity.setCategory(this.category);
        entity.setActionUrl(this.actionUrl);
        entity.setImageUrl(this.imageUrl);
        entity.setBadgeCount(this.badgeCount);
        entity.setIsSilent(this.isSilent);
        entity.setTimeToLive(this.timeToLive);
        return entity;
    }

    /**
     * Tworzy DTO z encji PushNotification.
     */
    public static PushNotificationDto fromEntity(PushNotification entity) {
        if (entity == null) return null;
        return PushNotificationDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .message(entity.getMessage())
                .createdAt(entity.getCreatedAt())
                .sentAt(entity.getSentAt())
                .isRead(entity.getIsRead())
                .status(entity.getStatus())
                .deviceToken(entity.getDeviceToken())
                .platform(entity.getPlatform())
                .category(entity.getCategory())
                .actionUrl(entity.getActionUrl())
                .imageUrl(entity.getImageUrl())
                .badgeCount(entity.getBadgeCount())
                .isSilent(entity.getIsSilent())
                .timeToLive(entity.getTimeToLive())
                .build();
    }
}

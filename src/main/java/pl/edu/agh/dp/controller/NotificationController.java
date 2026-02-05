package pl.edu.agh.dp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.dp.dto.ApiResponse;
import pl.edu.agh.dp.dto.NotificationDto;
import pl.edu.agh.dp.service.NotificationService;

import java.util.List;

/**
 * Kontroler REST dla Notification (TABLE_PER_CLASS inheritance).
 * Demonstruje hierarchię: Notification <- EmailNotification, SmsNotification, PushNotification
 */
@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Zarządzanie powiadomieniami (TABLE_PER_CLASS inheritance)")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/")
    @Operation(summary = "Pobierz wszystkie powiadomienia", description = "Zwraca wszystkie typy powiadomień (Email, SMS, Push)")
    public ApiResponse<List<NotificationDto>> getAllNotifications() {
        try {
            List<NotificationDto> notifications = notificationService.findAll();
            return ApiResponse.success(notifications, "Found " + notifications.size() + " notifications");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch notifications: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/email")
    @Operation(summary = "Pobierz powiadomienia email", description = "Zwraca tylko EmailNotification")
    public ApiResponse<List<NotificationDto>> getEmailNotifications() {
        try {
            List<NotificationDto> notifications = notificationService.findAllEmails();
            return ApiResponse.success(notifications, "Found " + notifications.size() + " email notifications");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch email notifications: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/sms")
    @Operation(summary = "Pobierz powiadomienia SMS", description = "Zwraca tylko SmsNotification")
    public ApiResponse<List<NotificationDto>> getSmsNotifications() {
        try {
            List<NotificationDto> notifications = notificationService.findAllSms();
            return ApiResponse.success(notifications, "Found " + notifications.size() + " SMS notifications");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch SMS notifications: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/push")
    @Operation(summary = "Pobierz powiadomienia push", description = "Zwraca tylko PushNotification")
    public ApiResponse<List<NotificationDto>> getPushNotifications() {
        try {
            List<NotificationDto> notifications = notificationService.findAllPush();
            return ApiResponse.success(notifications, "Found " + notifications.size() + " push notifications");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch push notifications: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/{type}/{id}")
    @Operation(summary = "Pobierz powiadomienie po ID i typie", description = "type: EMAIL, SMS, PUSH")
    public ApiResponse<NotificationDto> getNotificationById(@PathVariable String type, @PathVariable Long id) {
        try {
            return notificationService.findById(id, type.toUpperCase())
                    .map(dto -> ApiResponse.success(dto, "Notification found"))
                    .orElse(ApiResponse.notFound("Notification", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch notification: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/")
    @Operation(summary = "Utwórz nowe powiadomienie", description = "Typ określany przez pole notificationType: EMAIL, SMS, PUSH")
    public ApiResponse<NotificationDto> createNotification(@RequestBody NotificationDto dto) {
        try {
            NotificationDto created = notificationService.create(dto);
            return ApiResponse.created(created);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create notification: " + e.getMessage(), 400);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualizuj powiadomienie")
    public ApiResponse<NotificationDto> updateNotification(@PathVariable Long id, @RequestBody NotificationDto dto) {
        try {
            return notificationService.update(id, dto)
                    .map(updated -> ApiResponse.success(updated, "Notification updated successfully"))
                    .orElse(ApiResponse.notFound("Notification", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to update notification: " + e.getMessage(), 400);
        }
    }

    @DeleteMapping("/{type}/{id}")
    @Operation(summary = "Usuń powiadomienie", description = "type: EMAIL, SMS, PUSH")
    public ApiResponse<Void> deleteNotification(@PathVariable String type, @PathVariable Long id) {
        try {
            if (notificationService.delete(id, type.toUpperCase())) {
                return ApiResponse.success(null, "Notification deleted successfully");
            }
            return ApiResponse.notFound("Notification", id);
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete notification: " + e.getMessage(), 500);
        }
    }
}

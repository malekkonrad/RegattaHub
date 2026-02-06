package pl.edu.agh.dp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.dp.dto.ApiResponse;
import pl.edu.agh.dp.dto.NotificationDto;
import pl.edu.agh.dp.dto.EmailNotificationDto;
import pl.edu.agh.dp.dto.SmsNotificationDto;
import pl.edu.agh.dp.dto.PushNotificationDto;
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

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz powiadomienie po ID", description = "Automatycznie zwraca odpowiedni typ DTO (Email, SMS, Push)")
    public ApiResponse<NotificationDto> getNotificationById(@PathVariable Long id) {
        try {
            return notificationService.findById(id)
                    .map(dto -> ApiResponse.success(dto, "Notification found"))
                    .orElse(ApiResponse.notFound("Notification", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch notification: " + e.getMessage(), 500);
        }
    }

    // ==================== EMAIL ENDPOINTS ====================

    @GetMapping("/email")
    @Operation(summary = "Pobierz powiadomienia email", description = "Zwraca tylko EmailNotification")
    public ApiResponse<List<EmailNotificationDto>> getEmailNotifications() {
        try {
            List<EmailNotificationDto> notifications = notificationService.findAllEmails();
            return ApiResponse.success(notifications, "Found " + notifications.size() + " email notifications");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch email notifications: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/email/{id}")
    @Operation(summary = "Pobierz powiadomienie email po ID")
    public ApiResponse<EmailNotificationDto> getEmailById(@PathVariable Long id) {
        try {
            return notificationService.findEmailById(id)
                    .map(dto -> ApiResponse.success(dto, "Email notification found"))
                    .orElse(ApiResponse.notFound("Email notification", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch email notification: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/email")
    @Operation(summary = "Utwórz nowe powiadomienie email")
    public ApiResponse<EmailNotificationDto> createEmailNotification(@RequestBody EmailNotificationDto dto) {
        try {
            EmailNotificationDto created = notificationService.createEmail(dto);
            return ApiResponse.created(created);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create email notification: " + e.getMessage(), 400);
        }
    }

    @PutMapping("/email/{id}")
    @Operation(summary = "Aktualizuj powiadomienie email")
    public ApiResponse<EmailNotificationDto> updateEmailNotification(@PathVariable Long id, @RequestBody EmailNotificationDto dto) {
        try {
            return notificationService.updateEmail(id, dto)
                    .map(updated -> ApiResponse.success(updated, "Email notification updated successfully"))
                    .orElse(ApiResponse.notFound("Email notification", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to update email notification: " + e.getMessage(), 400);
        }
    }

    @DeleteMapping("/email/{id}")
    @Operation(summary = "Usuń powiadomienie email")
    public ApiResponse<Void> deleteEmailNotification(@PathVariable Long id) {
        try {
            if (notificationService.deleteEmail(id)) {
                return ApiResponse.success(null, "Email notification deleted successfully");
            }
            return ApiResponse.notFound("Email notification", id);
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete email notification: " + e.getMessage(), 500);
        }
    }

    // ==================== SMS ENDPOINTS ====================

    @GetMapping("/sms")
    @Operation(summary = "Pobierz powiadomienia SMS", description = "Zwraca tylko SmsNotification")
    public ApiResponse<List<SmsNotificationDto>> getSmsNotifications() {
        try {
            List<SmsNotificationDto> notifications = notificationService.findAllSms();
            return ApiResponse.success(notifications, "Found " + notifications.size() + " SMS notifications");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch SMS notifications: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/sms/{id}")
    @Operation(summary = "Pobierz powiadomienie SMS po ID")
    public ApiResponse<SmsNotificationDto> getSmsById(@PathVariable Long id) {
        try {
            return notificationService.findSmsById(id)
                    .map(dto -> ApiResponse.success(dto, "SMS notification found"))
                    .orElse(ApiResponse.notFound("SMS notification", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch SMS notification: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/sms")
    @Operation(summary = "Utwórz nowe powiadomienie SMS")
    public ApiResponse<SmsNotificationDto> createSmsNotification(@RequestBody SmsNotificationDto dto) {
        try {
            SmsNotificationDto created = notificationService.createSms(dto);
            return ApiResponse.created(created);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create SMS notification: " + e.getMessage(), 400);
        }
    }

    @PutMapping("/sms/{id}")
    @Operation(summary = "Aktualizuj powiadomienie SMS")
    public ApiResponse<SmsNotificationDto> updateSmsNotification(@PathVariable Long id, @RequestBody SmsNotificationDto dto) {
        try {
            return notificationService.updateSms(id, dto)
                    .map(updated -> ApiResponse.success(updated, "SMS notification updated successfully"))
                    .orElse(ApiResponse.notFound("SMS notification", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to update SMS notification: " + e.getMessage(), 400);
        }
    }

    @DeleteMapping("/sms/{id}")
    @Operation(summary = "Usuń powiadomienie SMS")
    public ApiResponse<Void> deleteSmsNotification(@PathVariable Long id) {
        try {
            if (notificationService.deleteSms(id)) {
                return ApiResponse.success(null, "SMS notification deleted successfully");
            }
            return ApiResponse.notFound("SMS notification", id);
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete SMS notification: " + e.getMessage(), 500);
        }
    }

    // ==================== PUSH ENDPOINTS ====================

    @GetMapping("/push")
    @Operation(summary = "Pobierz powiadomienia push", description = "Zwraca tylko PushNotification")
    public ApiResponse<List<PushNotificationDto>> getPushNotifications() {
        try {
            List<PushNotificationDto> notifications = notificationService.findAllPush();
            return ApiResponse.success(notifications, "Found " + notifications.size() + " push notifications");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch push notifications: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/push/{id}")
    @Operation(summary = "Pobierz powiadomienie push po ID")
    public ApiResponse<PushNotificationDto> getPushById(@PathVariable Long id) {
        try {
            return notificationService.findPushById(id)
                    .map(dto -> ApiResponse.success(dto, "Push notification found"))
                    .orElse(ApiResponse.notFound("Push notification", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch push notification: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/push")
    @Operation(summary = "Utwórz nowe powiadomienie push")
    public ApiResponse<PushNotificationDto> createPushNotification(@RequestBody PushNotificationDto dto) {
        try {
            PushNotificationDto created = notificationService.createPush(dto);
            return ApiResponse.created(created);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create push notification: " + e.getMessage(), 400);
        }
    }

    @PutMapping("/push/{id}")
    @Operation(summary = "Aktualizuj powiadomienie push")
    public ApiResponse<PushNotificationDto> updatePushNotification(@PathVariable Long id, @RequestBody PushNotificationDto dto) {
        try {
            return notificationService.updatePush(id, dto)
                    .map(updated -> ApiResponse.success(updated, "Push notification updated successfully"))
                    .orElse(ApiResponse.notFound("Push notification", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to update push notification: " + e.getMessage(), 400);
        }
    }

    @DeleteMapping("/push/{id}")
    @Operation(summary = "Usuń powiadomienie push")
    public ApiResponse<Void> deletePushNotification(@PathVariable Long id) {
        try {
            if (notificationService.deletePush(id)) {
                return ApiResponse.success(null, "Push notification deleted successfully");
            }
            return ApiResponse.notFound("Push notification", id);
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete push notification: " + e.getMessage(), 500);
        }
    }

    // ==================== POLIMORFICZNY ENDPOINT ====================

    @PostMapping("/")
    @Operation(summary = "Utwórz nowe powiadomienie (polimorficznie)", description = "Typ określany przez pole notificationType: EMAIL, SMS, PUSH")
    public ApiResponse<NotificationDto> createNotification(@RequestBody NotificationDto dto) {
        try {
            NotificationDto created = notificationService.create(dto);
            return ApiResponse.created(created);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create notification: " + e.getMessage(), 400);
        }
    }
}

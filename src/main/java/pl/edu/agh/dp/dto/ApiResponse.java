package pl.edu.agh.dp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Generyczna odpowiedź API.
 * Demonstruje wzorzec Response Wrapper dla REST API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private int statusCode;

    /**
     * Tworzy odpowiedź sukcesu z danymi.
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("Operation completed successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .statusCode(200)
                .build();
    }

    /**
     * Tworzy odpowiedź sukcesu z własną wiadomością.
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .statusCode(200)
                .build();
    }

    /**
     * Tworzy odpowiedź sukcesu dla operacji tworzenia.
     */
    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("Resource created successfully")
                .data(data)
                .timestamp(LocalDateTime.now())
                .statusCode(201)
                .build();
    }

    /**
     * Tworzy odpowiedź błędu.
     */
    public static <T> ApiResponse<T> error(String message, int statusCode) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .timestamp(LocalDateTime.now())
                .statusCode(statusCode)
                .build();
    }

    /**
     * Tworzy odpowiedź Not Found.
     */
    public static <T> ApiResponse<T> notFound(String resourceName, Object id) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(resourceName + " with id " + id + " not found")
                .data(null)
                .timestamp(LocalDateTime.now())
                .statusCode(404)
                .build();
    }
}

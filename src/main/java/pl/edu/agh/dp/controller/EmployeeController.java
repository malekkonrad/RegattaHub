package pl.edu.agh.dp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.dp.dto.ApiResponse;
import pl.edu.agh.dp.dto.EmployeeDto;
import pl.edu.agh.dp.service.EmployeeService;

import java.util.List;

/**
 * Kontroler REST dla Employee.
 * 
 * Demonstruje relacje:
 * - Many-to-One (Employee -> Department)
 * - Self-reference (Employee -> Manager)
 * - Many-to-Many (Employee <-> Document)
 */
@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employees", description = "Zarządzanie pracownikami (relacje ManyToOne, Self-reference, ManyToMany)")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    @Operation(summary = "Pobierz wszystkich pracowników")
    public ApiResponse<List<EmployeeDto>> getAllEmployees() {
        try {
            List<EmployeeDto> employees = employeeService.findAll();
            return ApiResponse.success(employees, "Found " + employees.size() + " employees");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch employees: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz pracownika po ID")
    public ApiResponse<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        try {
            return employeeService.findById(id)
                    .map(dto -> ApiResponse.success(dto, "Employee found"))
                    .orElse(ApiResponse.notFound("Employee", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch employee: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/")
    @Operation(summary = "Utwórz nowego pracownika")
    public ApiResponse<EmployeeDto> createEmployee(@RequestBody EmployeeDto dto) {
        try {
            EmployeeDto created = employeeService.create(dto);
            return ApiResponse.created(created);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create employee: " + e.getMessage(), 400);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualizuj pracownika")
    public ApiResponse<EmployeeDto> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto dto) {
        try {
            return employeeService.update(id, dto)
                    .map(updated -> ApiResponse.success(updated, "Employee updated successfully"))
                    .orElse(ApiResponse.notFound("Employee", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to update employee: " + e.getMessage(), 400);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń pracownika")
    public ApiResponse<Void> deleteEmployee(@PathVariable Long id) {
        try {
            if (employeeService.delete(id)) {
                return ApiResponse.success(null, "Employee deleted successfully");
            }
            return ApiResponse.notFound("Employee", id);
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete employee: " + e.getMessage(), 500);
        }
    }

    @PutMapping("/{employeeId}/department/{departmentId}")
    @Operation(summary = "Przypisz pracownika do działu", description = "Demonstruje relację ManyToOne")
    public ApiResponse<EmployeeDto> assignToDepartment(@PathVariable Long employeeId, @PathVariable Long departmentId) {
        try {
            return employeeService.assignToDepartment(employeeId, departmentId)
                    .map(dto -> ApiResponse.success(dto, "Employee assigned to department"))
                    .orElse(ApiResponse.notFound("Employee", employeeId));
        } catch (Exception e) {
            return ApiResponse.error("Failed to assign department: " + e.getMessage(), 400);
        }
    }

    @PutMapping("/{employeeId}/manager/{managerId}")
    @Operation(summary = "Przypisz managera do pracownika", description = "Demonstruje self-reference: ustawia relację @ManyToOne do innego Employee")
    public ApiResponse<EmployeeDto> assignManager(@PathVariable Long employeeId, @PathVariable Long managerId) {
        try {
            return employeeService.assignManager(employeeId, managerId)
                    .map(dto -> ApiResponse.success(dto, "Manager assigned successfully"))
                    .orElse(ApiResponse.notFound("Employee", employeeId));
        } catch (Exception e) {
            return ApiResponse.error("Failed to assign manager: " + e.getMessage(), 400);
        }
    }

    // ==================== SELF-REFERENCE ENDPOINTS ====================

    @GetMapping("/{id}/subordinates")
    @Operation(summary = "Pobierz podwładnych managera", 
               description = "Demonstruje @OneToMany self-reference: pobiera listę Employee, których manager = ten Employee")
    public ApiResponse<List<EmployeeDto>> getSubordinates(@PathVariable Long id) {
        try {
            List<EmployeeDto> subordinates = employeeService.getSubordinates(id);
            return ApiResponse.success(subordinates, "Found " + subordinates.size() + " subordinates");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch subordinates: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/{id}/manager")
    @Operation(summary = "Pobierz managera pracownika", 
               description = "Demonstruje @ManyToOne self-reference: pobiera Employee będącego managerem")
    public ApiResponse<EmployeeDto> getManager(@PathVariable Long id) {
        try {
            return employeeService.getManager(id)
                    .map(dto -> ApiResponse.success(dto, "Manager found"))
                    .orElse(ApiResponse.success(null, "Employee has no manager"));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch manager: " + e.getMessage(), 500);
        }
    }

    @DeleteMapping("/{id}/manager")
    @Operation(summary = "Usuń przypisanie managera", 
               description = "Demonstruje usunięcie self-reference: ustawia manager = null")
    public ApiResponse<EmployeeDto> removeManager(@PathVariable Long id) {
        try {
            return employeeService.removeManager(id)
                    .map(dto -> ApiResponse.success(dto, "Manager removed successfully"))
                    .orElse(ApiResponse.notFound("Employee", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to remove manager: " + e.getMessage(), 400);
        }
    }

    @GetMapping("/{id}/hierarchy")
    @Operation(summary = "Pobierz pracownika z pełną hierarchią", 
               description = "Demonstruje bidirectional self-reference: zwraca pracownika z informacją o managerze i liście podwładnych")
    public ApiResponse<EmployeeDto> getEmployeeWithHierarchy(@PathVariable Long id) {
        try {
            return employeeService.getEmployeeWithHierarchy(id)
                    .map(dto -> ApiResponse.success(dto, "Employee hierarchy loaded"))
                    .orElse(ApiResponse.notFound("Employee", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch employee hierarchy: " + e.getMessage(), 500);
        }
    }

    // ==================== MANY-TO-MANY ENDPOINTS ====================

    @PutMapping("/{employeeId}/documents/{documentId}/grant")
    @Operation(summary = "Przyznaj pracownikowi dostęp do dokumentu",
               description = "Demonstruje dodawanie relacji ManyToMany: pracownik uzyskuje dostęp do dokumentu")
    public ApiResponse<EmployeeDto> grantDocumentAccess(@PathVariable Long employeeId, @PathVariable Long documentId) {
        try {
            return employeeService.grantDocumentAccess(employeeId, documentId)
                    .map(dto -> ApiResponse.success(dto, "Document access granted"))
                    .orElse(ApiResponse.notFound("Employee", employeeId));
        } catch (Exception e) {
            return ApiResponse.error("Failed to grant document access: " + e.getMessage(), 400);
        }
    }

    @DeleteMapping("/{employeeId}/documents/{documentId}/revoke")
    @Operation(summary = "Odbierz pracownikowi dostęp do dokumentu",
               description = "Demonstruje usuwanie relacji ManyToMany: pracownik traci dostęp do dokumentu")
    public ApiResponse<EmployeeDto> revokeDocumentAccess(@PathVariable Long employeeId, @PathVariable Long documentId) {
        try {
            return employeeService.revokeDocumentAccess(employeeId, documentId)
                    .map(dto -> ApiResponse.success(dto, "Document access revoked"))
                    .orElse(ApiResponse.notFound("Employee", employeeId));
        } catch (Exception e) {
            return ApiResponse.error("Failed to revoke document access: " + e.getMessage(), 400);
        }
    }

    @GetMapping("/{id}/documents")
    @Operation(summary = "Pobierz dokumenty dostępne dla pracownika",
               description = "Demonstruje odczyt relacji ManyToMany: lista dokumentów do których pracownik ma dostęp")
    public ApiResponse<List<EmployeeDto.DocumentAccessInfo>> getAccessibleDocuments(@PathVariable Long id) {
        try {
            List<EmployeeDto.DocumentAccessInfo> documents = employeeService.getAccessibleDocuments(id);
            return ApiResponse.success(documents, "Found " + documents.size() + " accessible documents");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch accessible documents: " + e.getMessage(), 500);
        }
    }
}

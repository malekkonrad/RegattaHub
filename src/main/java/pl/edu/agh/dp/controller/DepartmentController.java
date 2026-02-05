package pl.edu.agh.dp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.dp.dto.ApiResponse;
import pl.edu.agh.dp.dto.DepartmentDto;
import pl.edu.agh.dp.service.DepartmentService;

import java.util.List;

/**
 * Kontroler REST dla Department.
 * 
 * Demonstruje relacje:
 * - One-to-Many (Department -> Employees)
 * - Self-reference (Department -> SubDepartments/ParentDepartment)
 */
@RestController
@RequestMapping("/api/departments")
@Tag(name = "Departments", description = "Zarządzanie działami (relacje OneToMany, Self-reference)")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/")
    @Operation(summary = "Pobierz wszystkie działy")
    public ApiResponse<List<DepartmentDto>> getAllDepartments() {
        try {
            List<DepartmentDto> departments = departmentService.findAll();
            return ApiResponse.success(departments, "Found " + departments.size() + " departments");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch departments: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz dział po ID")
    public ApiResponse<DepartmentDto> getDepartmentById(@PathVariable Long id) {
        try {
            return departmentService.findById(id)
                    .map(dto -> ApiResponse.success(dto, "Department found"))
                    .orElse(ApiResponse.notFound("Department", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch department: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/")
    @Operation(summary = "Utwórz nowy dział")
    public ApiResponse<DepartmentDto> createDepartment(@RequestBody DepartmentDto dto) {
        try {
            DepartmentDto created = departmentService.create(dto);
            return ApiResponse.created(created);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create department: " + e.getMessage(), 400);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualizuj dział")
    public ApiResponse<DepartmentDto> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDto dto) {
        try {
            return departmentService.update(id, dto)
                    .map(updated -> ApiResponse.success(updated, "Department updated successfully"))
                    .orElse(ApiResponse.notFound("Department", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to update department: " + e.getMessage(), 400);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń dział")
    public ApiResponse<Void> deleteDepartment(@PathVariable Long id) {
        try {
            if (departmentService.delete(id)) {
                return ApiResponse.success(null, "Department deleted successfully");
            }
            return ApiResponse.notFound("Department", id);
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete department: " + e.getMessage(), 500);
        }
    }

    @PutMapping("/{departmentId}/parent/{parentId}")
    @Operation(summary = "Ustaw dział nadrzędny", description = "Demonstruje self-reference hierarchy")
    public ApiResponse<DepartmentDto> setParentDepartment(@PathVariable Long departmentId, @PathVariable Long parentId) {
        try {
            return departmentService.setParentDepartment(departmentId, parentId)
                    .map(dto -> ApiResponse.success(dto, "Parent department set successfully"))
                    .orElse(ApiResponse.notFound("Department", departmentId));
        } catch (Exception e) {
            return ApiResponse.error("Failed to set parent department: " + e.getMessage(), 400);
        }
    }
}

package pl.edu.agh.dp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.agh.dp.entity.Employee;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO dla encji Employee.
 * Demonstruje mapowanie między encją ORM a obiektem transferowym.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String employeeCode;
    private LocalDate hireDate;
    private BigDecimal salary;
    private String position;
    private Long departmentId;
    private String departmentName;
    private Long managerId;
    private String managerName;
    
    // === SELF-REFERENCE: Lista podwładnych (dla demonstracji) ===
    private List<SubordinateInfo> subordinates;

    /**
     * Konwertuje encję Employee na DTO.
     */
    public static EmployeeDto fromEntity(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeeDtoBuilder builder = EmployeeDto.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .employeeCode(employee.getEmployeeCode())
                .hireDate(employee.getHireDate())
                .salary(employee.getSalary())
                .position(employee.getPosition());

        if (employee.getDepartment() != null) {
            builder.departmentId(employee.getDepartment().getId())
                   .departmentName(employee.getDepartment().getName());
        }

        // === SELF-REFERENCE: Informacje o managerze ===
        try {
            if (employee.getManager() != null) {
                builder.managerId(employee.getManager().getId())
                       .managerName(employee.getManager().getFirstName() + " " + employee.getManager().getLastName());
            }
        } catch (Exception e) {
            System.out.println("Warning: Could not load manager for employee " + employee.getId() + ": " + e.getMessage());
        }

        // === SELF-REFERENCE: Lista podwładnych ===
        try {
            if (employee.getSubordinates() != null && !employee.getSubordinates().isEmpty()) {
                List<SubordinateInfo> subordinateInfos = employee.getSubordinates().stream()
                        .map(sub -> new SubordinateInfo(
                                sub.getId(),
                                sub.getFirstName() + " " + sub.getLastName(),
                                sub.getPosition()))
                        .collect(Collectors.toList());
                builder.subordinates(subordinateInfos);
            }
        } catch (Exception e) {
            System.out.println("Warning: Could not load subordinates for employee " + employee.getId() + ": " + e.getMessage());
        }

        return builder.build();
    }

    /**
     * Konwertuje DTO na nową encję Employee (bez relacji).
     */
    public Employee toEntity() {
        Employee employee = new Employee();
//        employee.setId(this.id);
        employee.setFirstName(this.firstName);
        employee.setLastName(this.lastName);
        employee.setEmail(this.email);
        employee.setPhone(this.phone);
        employee.setEmployeeCode(this.employeeCode);
        employee.setHireDate(this.hireDate);
        employee.setSalary(this.salary);
        employee.setPosition(this.position);
        return employee;
    }

    /**
     * Klasa wewnętrzna do prezentacji podwładnych (SELF-REFERENCE).
     * Uproszczona wersja Employee do unikania rekurencji.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubordinateInfo {
        private Long id;
        private String fullName;
        private String position;
    }
}

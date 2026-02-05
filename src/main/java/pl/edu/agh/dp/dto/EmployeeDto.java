package pl.edu.agh.dp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.agh.dp.entity.Employee;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO dla encji Employee.
 * Demonstruje mapowanie między encją ORM a obiektem transferowym.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

//    private Long id;
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

    /**
     * Konwertuje encję Employee na DTO.
     */
    public static EmployeeDto fromEntity(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeeDtoBuilder builder = EmployeeDto.builder()
//                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .employeeCode(employee.getEmployeeCode())
                .hireDate(employee.getHireDate())
                .salary(employee.getSalary())
                .position(employee.getPosition());

//        if (employee.getDepartment() != null) {
//            builder.departmentId(employee.getDepartment().getId())
//                    .departmentName(employee.getDepartment().getName());
//        }
//
//        if (employee.getManager() != null) {
//            builder.managerId(employee.getManager().getId())
//                    .managerName(employee.getManager().getFirstName() + " " + employee.getManager().getLastName());
//        }

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
}

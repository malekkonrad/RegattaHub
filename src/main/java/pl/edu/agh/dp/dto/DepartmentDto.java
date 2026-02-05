package pl.edu.agh.dp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.agh.dp.entity.Department;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO dla encji Department.
 * Demonstruje mapowanie hierarchii (parent/children) w DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {

    private Long id;
    private String name;
    private String code;
    private String description;
    private Long parentDepartmentId;
    private String parentDepartmentName;
    private int employeeCount;
    private List<DepartmentSummaryDto> subDepartments;

    /**
     * Uproszczone DTO dla poddziałów (unika rekurencji).
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentSummaryDto {
        private Long id;
        private String name;
        private String code;
    }

    /**
     * Konwertuje encję Department na DTO.
     */
    public static DepartmentDto fromEntity(Department department) {
        if (department == null) {
            return null;
        }

        DepartmentDtoBuilder builder = DepartmentDto.builder()
                .id(department.getId())
                .name(department.getName())
                .code(department.getCode())
                .description(department.getDescription());

//        if (department.getParentDepartment() != null) {
//            builder.parentDepartmentId(department.getParentDepartment().getId())
//                    .parentDepartmentName(department.getParentDepartment().getName());
//        }
//
//        if (department.getEmployees() != null) {
//            builder.employeeCount(department.getEmployees().size());
//        }
//
//        if (department.getSubDepartments() != null && !department.getSubDepartments().isEmpty()) {
//            builder.subDepartments(
//                    department.getSubDepartments().stream()
//                            .map(sub -> DepartmentSummaryDto.builder()
//                                    .id(sub.getId())
//                                    .name(sub.getName())
//                                    .code(sub.getCode())
//                                    .build())
//                            .collect(Collectors.toList())
//            );
//        }

        return builder.build();
    }

    /**
     * Konwertuje DTO na nową encję Department (bez relacji).
     */
    public Department toEntity() {
        Department department = new Department();
        department.setId(this.id);
        department.setName(this.name);
        department.setCode(this.code);
        department.setDescription(this.description);
        return department;
    }
}

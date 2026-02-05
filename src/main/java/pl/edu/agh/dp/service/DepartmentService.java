package pl.edu.agh.dp.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.dp.api.Session;
import pl.edu.agh.dp.api.SessionFactory;
import pl.edu.agh.dp.config.OrmConfig;
import pl.edu.agh.dp.dto.DepartmentDto;
import pl.edu.agh.dp.entity.Department;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serwis dla operacji na Department.
 * 
 * Demonstruje:
 * - One-to-Many relationships (employees)
 * - Self-referencing hierarchy (parent/sub departments)
 */
@Service
public class DepartmentService {

    private final SessionFactory sessionFactory;

    public DepartmentService() {
        this.sessionFactory = OrmConfig.getSessionFactory();
    }

    public DepartmentDto create(DepartmentDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Department department = dto.toEntity();
                session.save(department);
                session.commit();
                return DepartmentDto.fromEntity(department);
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to create department: " + e.getMessage(), e);
            }
        }
    }

    public Optional<DepartmentDto> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Department department = session.find(Department.class, id);
            return Optional.ofNullable(department).map(DepartmentDto::fromEntity);
        }
    }

    public List<DepartmentDto> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<Department> departments = session.findAll(Department.class);
            return departments.stream()
                    .map(DepartmentDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    public Optional<DepartmentDto> update(Long id, DepartmentDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Department existing = session.find(Department.class, id);
                if (existing == null) {
                    return Optional.empty();
                }

                existing.setName(dto.getName());
                existing.setCode(dto.getCode());
                existing.setDescription(dto.getDescription());

                session.update(existing);
                session.commit();

                return Optional.of(DepartmentDto.fromEntity(existing));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to update department: " + e.getMessage(), e);
            }
        }
    }

    public boolean delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Department department = session.find(Department.class, id);
                if (department == null) {
                    return false;
                }
                session.delete(department);
                session.commit();
                return true;
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to delete department: " + e.getMessage(), e);
            }
        }
    }

    public Optional<DepartmentDto> setParentDepartment(Long departmentId, Long parentId) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Department department = session.find(Department.class, departmentId);
                if (department == null) {
                    return Optional.empty();
                }

                Department parent = session.find(Department.class, parentId);
                if (parent != null) {
                    department.setParentDepartment(parent);
                    session.update(department);
                }

                session.commit();
                return Optional.of(DepartmentDto.fromEntity(department));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to set parent department: " + e.getMessage(), e);
            }
        }
    }
}

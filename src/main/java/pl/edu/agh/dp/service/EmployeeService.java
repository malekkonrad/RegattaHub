package pl.edu.agh.dp.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.dp.api.Session;
import pl.edu.agh.dp.api.SessionFactory;
import pl.edu.agh.dp.config.OrmConfig;
import pl.edu.agh.dp.dto.EmployeeDto;
import pl.edu.agh.dp.entity.Department;
import pl.edu.agh.dp.entity.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serwis dla operacji na Employee.
 * 
 * Demonstruje:
 * - CRUD operations z użyciem ORM Session
 * - Zarządzanie relacjami (department, manager)
 */
@Service
public class EmployeeService {

    private final SessionFactory sessionFactory;

    public EmployeeService() {
        this.sessionFactory = OrmConfig.getSessionFactory();
    }

    public EmployeeDto create(EmployeeDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Employee employee = dto.toEntity();
                if (dto.getManagerId() != null) {
                    Employee manager = session.find(Employee.class, dto.getManagerId());
                    employee.setManager(manager);
                }
                session.save(employee);
                session.commit();
                return EmployeeDto.fromEntity(employee);
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to create employee: " + e.getMessage(), e);
            }
        }
    }

    public Optional<EmployeeDto> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Employee employee = session.find(Employee.class, id);
            session.load(employee, "manager");
            return Optional.ofNullable(employee).map(EmployeeDto::fromEntity);
        }
    }

    public List<EmployeeDto> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<Employee> employees = session.findAll(Employee.class);
            
            // WORKAROUND: Wymuś załadowanie wszystkich pól w sesji
            // (trigger lazy loading przed zamknięciem sesji)
            List<EmployeeDto> result = new ArrayList<>();
            for (Employee e : employees) {
                session.load(e, "manager");
                result.add(EmployeeDto.fromEntity(e));
            }
            return result;
        }
    }

    public Optional<EmployeeDto> update(Long id, EmployeeDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Employee existing = session.find(Employee.class, id);
                if (existing == null) {
                    return Optional.empty();
                }

                existing.setFirstName(dto.getFirstName());
                existing.setLastName(dto.getLastName());
                existing.setEmail(dto.getEmail());
                existing.setPhone(dto.getPhone());
                existing.setEmployeeCode(dto.getEmployeeCode());
                existing.setHireDate(dto.getHireDate());
                existing.setSalary(dto.getSalary());
                existing.setPosition(dto.getPosition());

                session.update(existing);
                session.commit();

                return Optional.of(EmployeeDto.fromEntity(existing));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to update employee: " + e.getMessage(), e);
            }
        }
    }

    public boolean delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Employee employee = session.find(Employee.class, id);
                if (employee == null) {
                    return false;
                }
                session.delete(employee);
                session.commit();
                return true;
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to delete employee: " + e.getMessage(), e);
            }
        }
    }

    public Optional<EmployeeDto> assignToDepartment(Long employeeId, Long departmentId) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Employee employee = session.find(Employee.class, employeeId);
                if (employee == null) {
                    return Optional.empty();
                }

                Department department = session.find(Department.class, departmentId);
                if (department != null) {
                    employee.setDepartment(department);
                    session.update(employee);
                }

                session.commit();
                return Optional.of(EmployeeDto.fromEntity(employee));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to assign department: " + e.getMessage(), e);
            }
        }
    }

    public Optional<EmployeeDto> assignManager(Long employeeId, Long managerId) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Employee employee = session.find(Employee.class, employeeId);
                if (employee == null) {
                    return Optional.empty();
                }

                Employee manager = session.find(Employee.class, managerId);
                if (manager != null) {
                    System.out.println(manager.getFirstName());
                    employee.setManager(manager);
                    session.update(employee);
                }
                System.out.println(employee.getManager().getId());
                session.commit();
                return Optional.of(EmployeeDto.fromEntity(employee));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to assign manager: " + e.getMessage(), e);
            }
        }
    }

    // ==================== SELF-REFERENCE DEMONSTRATION ====================

    /**
     * Pobiera listę podwładnych danego managera.
     * Demonstruje @OneToMany self-reference: manager -> subordinates
     */
    public List<EmployeeDto> getSubordinates(Long managerId) {
        try (Session session = sessionFactory.openSession()) {
            Employee manager = session.find(Employee.class, managerId);
            if (manager == null || manager.getSubordinates() == null) {
                return List.of();
            }
            return manager.getSubordinates().stream()
                    .map(EmployeeDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Pobiera managera danego pracownika.
     * Demonstruje @ManyToOne self-reference: employee -> manager
     */
    public Optional<EmployeeDto> getManager(Long employeeId) {
        try (Session session = sessionFactory.openSession()) {
            Employee employee = session.find(Employee.class, employeeId);
            session.load(employee, "manager");
            if (employee == null || employee.getManager() == null) {
                return Optional.empty();
            }
            return Optional.of(EmployeeDto.fromEntity(employee.getManager()));
        }
    }

    /**
     * Usuwa przypisanie managera dla pracownika.
     * Demonstruje modyfikację self-reference.
     */
    public Optional<EmployeeDto> removeManager(Long employeeId) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Employee employee = session.find(Employee.class, employeeId);
                session.load(employee, "manager");
                if (employee == null) {
                    return Optional.empty();
                }

                employee.setManager(null);
                session.update(employee);
                session.commit();
                return Optional.of(EmployeeDto.fromEntity(employee));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to remove manager: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Pobiera pełną hierarchię (pracownika z managerem i podwładnymi).
     * Demonstruje bidirectional self-reference w jednym zapytaniu.
     */
    public Optional<EmployeeDto> getEmployeeWithHierarchy(Long employeeId) {
        try (Session session = sessionFactory.openSession()) {
            Employee employee = session.find(Employee.class, employeeId);
            session.load(employee, "manager");
            if (employee == null) {
                return Optional.empty();
            }
            // DTO automatycznie zawiera manager i subordinates
            return Optional.of(EmployeeDto.fromEntity(employee));
        }
    }
}

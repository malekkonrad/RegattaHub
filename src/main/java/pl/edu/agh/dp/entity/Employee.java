package pl.edu.agh.dp.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.dp.api.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Pracownik - samodzielna encja (bez dziedziczenia).
 * 
 * Demonstruje:
 * - Self-reference (manager/subordinates) - relacja do samego siebie
 * - Many-to-One (department) - wiele pracowników w jednym dziale
 * - Many-to-Many (documents) - pracownik ma dostęp do wielu dokumentów
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Employee {

    @Id(autoIncrement = true)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = true)
    private String email;
    @Column(nullable = true)
    private String phone;

    @Column(nullable = false, unique = true)
    private String employeeCode;
    @Column(nullable = true)
    private LocalDate hireDate;
    @Column(nullable = true)
    private BigDecimal salary;
    @Column(nullable = true)
    private String position;

    // ==================== SELF-REFERENCE ====================
    @OneToMany(mappedBy = "manager")
    @JoinColumn(joinColumns = {"subordinates"}, nullable = true)
    private List<Employee> subordinates = new ArrayList<>();

    @ManyToOne(mappedBy = "subordinates")
    @JoinColumn(joinColumns = {"manager"}, nullable = true)
    private Employee manager;

    // ==================== MANY-TO-ONE ====================
    @ManyToOne(mappedBy = "employees")
    @JoinColumn(joinColumns = {"department"}, nullable = true)
    private Department department;

    // ==================== MANY-TO-MANY ====================

    @ManyToMany(mappedBy = "authorizedEmployees")
    @JoinColumn(joinColumns = {"documents"}, nullable = true)
    private List<Document> documents;
}

package pl.edu.agh.dp.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.agh.dp.core.mapping.annotations.*;
import pl.edu.agh.dp.core.mapping.InheritanceType;

import java.time.LocalDate;
import java.util.List;

/**
 * Dokument - klasa bazowa dla hierarchii dziedziczenia SINGLE_TABLE.
 * Document <- Invoice, Report
 * 
 * Demonstruje:
 * - SINGLE_TABLE inheritance
 * - Many-to-Many (authorizedEmployees) - dokument dostępny dla wielu pracowników
 */
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("DOCUMENT")
@Entity
public class Document {
    @Id(autoIncrement = true)
    private Long id;

//    @Column(nullable = true)
    private String title;

//    @Column(nullable = true)
    private LocalDate createdDate;

//    @Column(nullable = true)
    private String createdBy;

//    @Column(nullable = true)
    private String content;

    // ==================== MANY-TO-MANY ====================

    @ManyToMany(mappedBy = "documents")
    @JoinColumn(joinColumns = {"authorizedEmployees"}, nullable = true)
    private List<Employee> authorizedEmployees;
}

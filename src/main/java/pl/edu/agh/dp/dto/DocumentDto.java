package pl.edu.agh.dp.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.edu.agh.dp.entity.Curriculum;
import pl.edu.agh.dp.entity.Document;
import pl.edu.agh.dp.entity.Invoice;
import pl.edu.agh.dp.entity.Report;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Bazowe DTO dla hierarchii Document (SINGLE_TABLE inheritance).
 * Hierarchia: Document <- Report <- Invoice, Document <- Cirriculum
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "documentType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReportDto.class, name = "REPORT"),
        @JsonSubTypes.Type(value = InvoiceDto.class, name = "INVOICE"),
        @JsonSubTypes.Type(value = CurriculumDto.class, name = "CV")
})
public class DocumentDto {

    private Long id;
    private String title;
    private LocalDate createdDate;
    private String createdBy;
    private String content;

    // === MANY-TO-MANY: Pracownicy z dostępem do dokumentu ===
    private List<AuthorizedEmployeeInfo> authorizedEmployees;

    /**
     * Konwertuje encję Document na odpowiednie DTO w hierarchii.
     */
    public static DocumentDto fromEntity(Document document) {
        if (document == null) return null;

        if (document instanceof Invoice inv) {
            return InvoiceDto.builder()
                    .id(inv.getId())
                    .title(inv.getTitle())
                    .createdDate(inv.getCreatedDate())
                    .createdBy(inv.getCreatedBy())
                    .content(inv.getContent())
                    // Report fields
                    .reportType(inv.getReportType())
                    .periodStart(inv.getPeriodStart())
                    .periodEnd(inv.getPeriodEnd())
                    .status(inv.getStatus())
                    // Invoice fields
                    .invoiceNumber(inv.getInvoiceNumber())
                    .issueDate(inv.getIssueDate())
                    .dueDate(inv.getDueDate())
                    .totalAmount(inv.getTotalAmount())
                    .taxAmount(inv.getTaxAmount())
                    .paymentStatus(inv.getPaymentStatus())
                    .build();
        } else if (document instanceof Report rep) {
            return ReportDto.builder()
                    .id(rep.getId())
                    .title(rep.getTitle())
                    .createdDate(rep.getCreatedDate())
                    .createdBy(rep.getCreatedBy())
                    .content(rep.getContent())
                    .reportType(rep.getReportType())
                    .periodStart(rep.getPeriodStart())
                    .periodEnd(rep.getPeriodEnd())
                    .status(rep.getStatus())
                    .build();
        } else if (document instanceof Curriculum cv) {
            return CurriculumDto.builder()
                    .id(cv.getId())
                    .title(cv.getTitle())
                    .createdDate(cv.getCreatedDate())
                    .createdBy(cv.getCreatedBy())
                    .content(cv.getContent())
                    .name(cv.getName())
                    .surname(cv.getSurname())
                    .creationDate(cv.getCreationDate())
                    .build();
        }

        // Bazowy typ
        return DocumentDto.builder()
                .id(document.getId())
                .title(document.getTitle())
                .createdDate(document.getCreatedDate())
                .createdBy(document.getCreatedBy())
                .content(document.getContent())
                .build();
    }

    /**
     * Konwertuje DTO na encję. Podklasy nadpisują tę metodę.
     */
    public Document toEntity() {
        Document document = new Document();
        document.setTitle(this.title);
        document.setCreatedDate(this.createdDate);
        document.setCreatedBy(this.createdBy);
        document.setContent(this.content);
        return document;
    }

    /**
     * Wypełnia wspólne pola encji z DTO.
     */
    protected void fillCommonFields(Document document) {
        document.setTitle(this.title);
        document.setCreatedDate(this.createdDate);
        document.setCreatedBy(this.createdBy);
        document.setContent(this.content);
    }

    /**
     * Zwraca typ dokumentu (do serializacji JSON).
     */
    public String getDocumentType() {
        if (this instanceof InvoiceDto) return "INVOICE";
        if (this instanceof ReportDto) return "REPORT";
        if (this instanceof CurriculumDto) return "CV";
        return "DOCUMENT";
    }

    /**
     * Klasa wewnętrzna do prezentacji uprawnionych pracowników (MANY-TO-MANY).
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorizedEmployeeInfo {
        private Long id;
        private String fullName;
        private String position;
    }

    /**
     * Ładuje listę uprawnionych pracowników do dokumentu.
     */
    public static List<AuthorizedEmployeeInfo> loadAuthorizedEmployees(Document document) {
        if (document == null || document.getAuthorizedEmployees() == null) {
            return null;
        }
        try {
            return document.getAuthorizedEmployees().stream()
                    .map(emp -> new AuthorizedEmployeeInfo(
                            emp.getId(),
                            emp.getFirstName() + " " + emp.getLastName(),
                            emp.getPosition()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return null;
        }
    }
}

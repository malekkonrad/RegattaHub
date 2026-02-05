package pl.edu.agh.dp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.agh.dp.entity.Document;
import pl.edu.agh.dp.entity.Invoice;
import pl.edu.agh.dp.entity.Report;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO dla hierarchii Document (SINGLE_TABLE inheritance).
 * Demonstruje polimorficzne mapowanie DTO.
 * 
 * Hierarchia: Document -> Report -> Invoice
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {

    private Long id;
    private String title;
    private LocalDate createdDate;
    private String createdBy;
    private String content;
    private String documentType; // DOCUMENT, REPORT, INVOICE

    // Pola specyficzne dla Report (i dziedziczone przez Invoice)
    private String reportType;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private String status;

    // Pola specyficzne dla Invoice
    private String invoiceNumber;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private String paymentStatus;

    /**
     * Konwertuje encję Document (lub podklasę) na DTO.
     */
    public static DocumentDto fromEntity(Document document) {
        if (document == null) {
            return null;
        }

        DocumentDtoBuilder builder = DocumentDto.builder()
                .id(document.getId())
                .title(document.getTitle())
                .createdDate(document.getCreatedDate())
                .createdBy(document.getCreatedBy())
                .content(document.getContent());

        if (document instanceof Invoice invoice) {
            builder.documentType("INVOICE")
                    // Report fields (inherited)
                    .reportType(invoice.getReportType())
                    .periodStart(invoice.getPeriodStart())
                    .periodEnd(invoice.getPeriodEnd())
                    .status(invoice.getStatus())
                    // Invoice-specific fields
                    .invoiceNumber(invoice.getInvoiceNumber())
                    .issueDate(invoice.getIssueDate())
                    .dueDate(invoice.getDueDate())
                    .totalAmount(invoice.getTotalAmount())
                    .taxAmount(invoice.getTaxAmount())
                    .paymentStatus(invoice.getPaymentStatus());
        } else if (document instanceof Report report) {
            builder.documentType("REPORT")
                    .reportType(report.getReportType())
                    .periodStart(report.getPeriodStart())
                    .periodEnd(report.getPeriodEnd())
                    .status(report.getStatus());
        } else {
            builder.documentType("DOCUMENT");
        }

        return builder.build();
    }

    /**
     * Konwertuje DTO na odpowiednią encję na podstawie documentType.
     */
    public Document toEntity() {
        Document document;

        switch (documentType != null ? documentType : "DOCUMENT") {
            case "INVOICE" -> {
                Invoice invoice = new Invoice();
                // Report fields (inherited)
                invoice.setReportType(this.reportType);
                invoice.setPeriodStart(this.periodStart);
                invoice.setPeriodEnd(this.periodEnd);
                invoice.setStatus(this.status);
                // Invoice-specific fields
                invoice.setInvoiceNumber(this.invoiceNumber);
                invoice.setIssueDate(this.issueDate);
                invoice.setDueDate(this.dueDate);
                invoice.setTotalAmount(this.totalAmount);
                invoice.setTaxAmount(this.taxAmount);
                invoice.setPaymentStatus(this.paymentStatus);
                document = invoice;
            }
            case "REPORT" -> {
                Report report = new Report();
                report.setReportType(this.reportType);
                report.setPeriodStart(this.periodStart);
                report.setPeriodEnd(this.periodEnd);
                report.setStatus(this.status);
                document = report;
            }
            default -> document = new Document();
        }

        document.setId(this.id);
        document.setTitle(this.title);
        document.setCreatedDate(this.createdDate);
        document.setCreatedBy(this.createdBy);
        document.setContent(this.content);

        return document;
    }
}

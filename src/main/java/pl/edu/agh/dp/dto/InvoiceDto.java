package pl.edu.agh.dp.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.edu.agh.dp.entity.Document;
import pl.edu.agh.dp.entity.Invoice;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO dla Invoice.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class InvoiceDto extends ReportDto {

    private String invoiceNumber;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private String paymentStatus;

    @Override
    public Document toEntity() {
        Invoice entity = new Invoice();
        fillCommonFields(entity);
        fillReportFields(entity);
        entity.setInvoiceNumber(this.invoiceNumber);
        entity.setIssueDate(this.issueDate);
        entity.setDueDate(this.dueDate);
        entity.setTotalAmount(this.totalAmount);
        entity.setTaxAmount(this.taxAmount);
        entity.setPaymentStatus(this.paymentStatus);
        return entity;
    }

    /**
     * Tworzy DTO z encji Invoice.
     */
    public static InvoiceDto fromEntity(Invoice entity) {
        if (entity == null) return null;
        return InvoiceDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .createdDate(entity.getCreatedDate())
                .createdBy(entity.getCreatedBy())
                .content(entity.getContent())
                // Report fields
                .reportType(entity.getReportType())
                .periodStart(entity.getPeriodStart())
                .periodEnd(entity.getPeriodEnd())
                .status(entity.getStatus())
                // Invoice fields
                .invoiceNumber(entity.getInvoiceNumber())
                .issueDate(entity.getIssueDate())
                .dueDate(entity.getDueDate())
                .totalAmount(entity.getTotalAmount())
                .taxAmount(entity.getTaxAmount())
                .paymentStatus(entity.getPaymentStatus())
                .build();
    }
}

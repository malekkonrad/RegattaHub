package pl.edu.agh.dp.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.edu.agh.dp.entity.Document;
import pl.edu.agh.dp.entity.Report;

import java.time.LocalDate;

/**
 * DTO dla Report.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class ReportDto extends DocumentDto {

    private String reportType;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private String status;

    @Override
    public Document toEntity() {
        Report entity = new Report();
        fillCommonFields(entity);
        entity.setReportType(this.reportType);
        entity.setPeriodStart(this.periodStart);
        entity.setPeriodEnd(this.periodEnd);
        entity.setStatus(this.status);
        return entity;
    }

    /**
     * Wypełnia pola specyficzne dla Report w encji.
     */
    protected void fillReportFields(Report entity) {
        entity.setReportType(this.reportType);
        entity.setPeriodStart(this.periodStart);
        entity.setPeriodEnd(this.periodEnd);
        entity.setStatus(this.status);
    }

    /**
     * Tworzy DTO z encji Report.
     */
    public static ReportDto fromEntity(Report entity) {
        if (entity == null) return null;
        return ReportDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .createdDate(entity.getCreatedDate())
                .createdBy(entity.getCreatedBy())
                .content(entity.getContent())
                .reportType(entity.getReportType())
                .periodStart(entity.getPeriodStart())
                .periodEnd(entity.getPeriodEnd())
                .status(entity.getStatus())
                .build();
    }
}

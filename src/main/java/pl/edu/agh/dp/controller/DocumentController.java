package pl.edu.agh.dp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.dp.dto.ApiResponse;
import pl.edu.agh.dp.dto.CurriculumDto;
import pl.edu.agh.dp.dto.DocumentDto;
import pl.edu.agh.dp.dto.InvoiceDto;
import pl.edu.agh.dp.dto.ReportDto;
import pl.edu.agh.dp.service.DocumentService;

import java.math.BigDecimal;
import java.util.List;

/**
 * Kontroler REST dla Document (SINGLE_TABLE inheritance).
 * Demonstruje hierarchię: Document <- Report <- Invoice, Document <- Cirriculum
 */
@RestController
@RequestMapping("/api/documents")
@Tag(name = "Documents", description = "Zarządzanie dokumentami (SINGLE_TABLE inheritance)")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/")
    @Operation(summary = "Pobierz wszystkie dokumenty", description = "Zwraca wszystkie typy dokumentów (Report, Invoice, Cirriculum)")
    public ApiResponse<List<DocumentDto>> getAllDocuments() {
        try {
            List<DocumentDto> documents = documentService.findAll();
            return ApiResponse.success(documents, "Found " + documents.size() + " documents");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch documents: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz dokument po ID", description = "Automatycznie zwraca odpowiedni typ DTO (Report, Invoice, Cirriculum)")
    public ApiResponse<DocumentDto> getDocumentById(@PathVariable Long id) {
        try {
            return documentService.findById(id)
                    .map(dto -> ApiResponse.success(dto, "Document found"))
                    .orElse(ApiResponse.notFound("Document", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch document: " + e.getMessage(), 500);
        }
    }

    // ==================== REPORT ENDPOINTS ====================

    @GetMapping("/report")
    @Operation(summary = "Pobierz raporty", description = "Zwraca tylko Report")
    public ApiResponse<List<ReportDto>> getReports() {
        try {
            List<ReportDto> documents = documentService.findAllReports();
            return ApiResponse.success(documents, "Found " + documents.size() + " reports");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch reports: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/report/{id}")
    @Operation(summary = "Pobierz raport po ID")
    public ApiResponse<ReportDto> getReportById(@PathVariable Long id) {
        try {
            return documentService.findReportById(id)
                    .map(dto -> ApiResponse.success(dto, "Report found"))
                    .orElse(ApiResponse.notFound("Report", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch report: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/report")
    @Operation(summary = "Utwórz nowy raport")
    public ApiResponse<ReportDto> createReport(@RequestBody ReportDto dto) {
        try {
            ReportDto created = documentService.createReport(dto);
            return ApiResponse.created(created);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create report: " + e.getMessage(), 400);
        }
    }

    @PutMapping("/report/{id}")
    @Operation(summary = "Aktualizuj raport")
    public ApiResponse<ReportDto> updateReport(@PathVariable Long id, @RequestBody ReportDto dto) {
        try {
            return documentService.updateReport(id, dto)
                    .map(updated -> ApiResponse.success(updated, "Report updated successfully"))
                    .orElse(ApiResponse.notFound("Report", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to update report: " + e.getMessage(), 400);
        }
    }

    @DeleteMapping("/report/{id}")
    @Operation(summary = "Usuń raport")
    public ApiResponse<Void> deleteReport(@PathVariable Long id) {
        try {
            if (documentService.deleteReport(id)) {
                return ApiResponse.success(null, "Report deleted successfully");
            }
            return ApiResponse.notFound("Report", id);
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete report: " + e.getMessage(), 500);
        }
    }

    // ==================== INVOICE ENDPOINTS ====================

    @GetMapping("/invoice")
    @Operation(summary = "Pobierz faktury", description = "Zwraca tylko Invoice")
    public ApiResponse<List<InvoiceDto>> getInvoices() {
        try {
            List<InvoiceDto> documents = documentService.findAllInvoices();
            return ApiResponse.success(documents, "Found " + documents.size() + " invoices");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch invoices: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/invoice/{id}")
    @Operation(summary = "Pobierz fakturę po ID")
    public ApiResponse<InvoiceDto> getInvoiceById(@PathVariable Long id) {
        try {
            return documentService.findInvoiceById(id)
                    .map(dto -> ApiResponse.success(dto, "Invoice found"))
                    .orElse(ApiResponse.notFound("Invoice", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch invoice: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/invoice")
    @Operation(summary = "Utwórz nową fakturę")
    public ApiResponse<InvoiceDto> createInvoice(@RequestBody InvoiceDto dto) {
        try {
            InvoiceDto created = documentService.createInvoice(dto);
            return ApiResponse.created(created);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create invoice: " + e.getMessage(), 400);
        }
    }

    @PutMapping("/invoice/{id}")
    @Operation(summary = "Aktualizuj fakturę")
    public ApiResponse<InvoiceDto> updateInvoice(@PathVariable Long id, @RequestBody InvoiceDto dto) {
        try {
            return documentService.updateInvoice(id, dto)
                    .map(updated -> ApiResponse.success(updated, "Invoice updated successfully"))
                    .orElse(ApiResponse.notFound("Invoice", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to update invoice: " + e.getMessage(), 400);
        }
    }

    @DeleteMapping("/invoice/{id}")
    @Operation(summary = "Usuń fakturę")
    public ApiResponse<Void> deleteInvoice(@PathVariable Long id) {
        try {
            if (documentService.deleteInvoice(id)) {
                return ApiResponse.success(null, "Invoice deleted successfully");
            }
            return ApiResponse.notFound("Invoice", id);
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete invoice: " + e.getMessage(), 500);
        }
    }

    // ==================== CIRRICULUM (CV) ENDPOINTS ====================

    @GetMapping("/cv")
    @Operation(summary = "Pobierz CV", description = "Zwraca tylko Cirriculum")
    public ApiResponse<List<CurriculumDto>> getCirriculums() {
        try {
            List<CurriculumDto> documents = documentService.findAllCirriculums();
            return ApiResponse.success(documents, "Found " + documents.size() + " CVs");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch CVs: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/cv/{id}")
    @Operation(summary = "Pobierz CV po ID")
    public ApiResponse<CurriculumDto> getCirriculumById(@PathVariable Long id) {
        try {
            return documentService.findCirriculumById(id)
                    .map(dto -> ApiResponse.success(dto, "CV found"))
                    .orElse(ApiResponse.notFound("CV", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch CV: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/cv")
    @Operation(summary = "Utwórz nowe CV")
    public ApiResponse<CurriculumDto> createCirriculum(@RequestBody CurriculumDto dto) {
        try {
            CurriculumDto created = documentService.createCirriculum(dto);
            return ApiResponse.created(created);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create CV: " + e.getMessage(), 400);
        }
    }

    @PutMapping("/cv/{id}")
    @Operation(summary = "Aktualizuj CV")
    public ApiResponse<CurriculumDto> updateCirriculum(@PathVariable Long id, @RequestBody CurriculumDto dto) {
        try {
            return documentService.updateCirriculum(id, dto)
                    .map(updated -> ApiResponse.success(updated, "CV updated successfully"))
                    .orElse(ApiResponse.notFound("CV", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to update CV: " + e.getMessage(), 400);
        }
    }

    @DeleteMapping("/cv/{id}")
    @Operation(summary = "Usuń CV")
    public ApiResponse<Void> deleteCirriculum(@PathVariable Long id) {
        try {
            if (documentService.deleteCirriculum(id)) {
                return ApiResponse.success(null, "CV deleted successfully");
            }
            return ApiResponse.notFound("CV", id);
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete CV: " + e.getMessage(), 500);
        }
    }

    // ==================== FINDER API ENDPOINTS (SINGLE_TABLE INHERITANCE) ====================

    @GetMapping("/finder/by-created-by")
    @Operation(summary = "Wyszukaj dokumenty po autorze",
               description = "Demonstruje Finder eq() na klasie bazowej SINGLE_TABLE - polimorficzne wyszukiwanie wszystkich typów dokumentów")
    public ApiResponse<List<DocumentDto>> findDocumentsByCreatedBy(@RequestParam String createdBy) {
        try {
            List<DocumentDto> documents = documentService.findDocumentsByCreatedBy(createdBy);
            return ApiResponse.success(documents, 
                    "Finder eq() na Document (SINGLE_TABLE): Found " + documents.size() + " documents by '" + createdBy + "'");
        } catch (Exception e) {
            return ApiResponse.error("Failed to find documents: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/finder/by-title-like")
    @Operation(summary = "Wyszukaj dokumenty po fragmencie tytułu",
               description = "Demonstruje Finder like() na klasie bazowej - zwraca wszystkie typy (Report, Invoice, Curriculum)")
    public ApiResponse<List<DocumentDto>> findDocumentsByTitleLike(@RequestParam String titlePattern) {
        try {
            List<DocumentDto> documents = documentService.findDocumentsByTitleLike(titlePattern);
            return ApiResponse.success(documents, 
                    "Finder like() na Document (SINGLE_TABLE): Found " + documents.size() + " documents with title containing '" + titlePattern + "'");
        } catch (Exception e) {
            return ApiResponse.error("Failed to find documents: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/finder/invoice/by-payment-status")
    @Operation(summary = "Wyszukaj faktury po statusie płatności",
               description = "Demonstruje Finder eq() na klasie pochodnej Invoice - wyszukiwanie po polu specyficznym dla klasy dziecka")
    public ApiResponse<List<InvoiceDto>> findInvoicesByPaymentStatus(@RequestParam String paymentStatus) {
        try {
            List<InvoiceDto> invoices = documentService.findInvoicesByPaymentStatus(paymentStatus);
            return ApiResponse.success(invoices, 
                    "Finder eq() na Invoice (SINGLE_TABLE child): Found " + invoices.size() + " invoices with status '" + paymentStatus + "'");
        } catch (Exception e) {
            return ApiResponse.error("Failed to find invoices: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/finder/invoice/by-amount-greater-than")
    @Operation(summary = "Wyszukaj faktury z kwotą większą niż podana",
               description = "Demonstruje Finder gt() + orderDesc() na klasie pochodnej Invoice")
    public ApiResponse<List<InvoiceDto>> findInvoicesByAmountGreaterThan(@RequestParam BigDecimal minAmount) {
        try {
            List<InvoiceDto> invoices = documentService.findInvoicesByAmountGreaterThan(minAmount);
            return ApiResponse.success(invoices, 
                    "Finder gt() na Invoice (SINGLE_TABLE child): Found " + invoices.size() + " invoices with amount > " + minAmount);
        } catch (Exception e) {
            return ApiResponse.error("Failed to find invoices: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/finder/report/by-type-and-status")
    @Operation(summary = "Wyszukaj raporty po typie i statusie",
               description = "Demonstruje Finder z wieloma warunkami eq() na klasie pośredniej Report (zwraca też Invoice polimorficznie)")
    public ApiResponse<List<ReportDto>> findReportsByTypeAndStatus(
            @RequestParam String reportType,
            @RequestParam String status) {
        try {
            List<ReportDto> reports = documentService.findReportsByTypeAndStatus(reportType, status);
            return ApiResponse.success(reports, 
                    "Finder eq()+eq() na Report (SINGLE_TABLE middle): Found " + reports.size() + " reports with type '" + reportType + "' and status '" + status + "'");
        } catch (Exception e) {
            return ApiResponse.error("Failed to find reports: " + e.getMessage(), 500);
        }
    }
}

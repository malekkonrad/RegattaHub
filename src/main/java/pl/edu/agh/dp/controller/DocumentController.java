package pl.edu.agh.dp.controller;

import pl.edu.agh.dp.dto.ApiResponse;
import pl.edu.agh.dp.dto.DocumentDto;
import pl.edu.agh.dp.service.DocumentService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Kontroler REST dla Document i podklas (Invoice, Report).
 * 
 * Demonstruje operacje na hierarchii z SINGLE_TABLE inheritance.
 */
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController() {
        this.documentService = new DocumentService();
    }

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // ==================== CRUD ENDPOINTS ====================

    /**
     * GET /api/documents
     * Pobiera wszystkie dokumenty (wszystkich typów).
     */
    public ApiResponse<List<DocumentDto>> getAllDocuments() {
        try {
            List<DocumentDto> documents = documentService.findAll();
            return ApiResponse.success(documents, "Found " + documents.size() + " documents");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch documents: " + e.getMessage(), 500);
        }
    }

    /**
     * GET /api/documents/{id}
     * Pobiera dokument po ID (polimorficznie).
     */
    public ApiResponse<DocumentDto> getDocumentById(Long id) {
        try {
            return documentService.findById(id)
                    .map(dto -> ApiResponse.success(dto, "Document found (type: " + dto.getDocumentType() + ")"))
                    .orElse(ApiResponse.notFound("Document", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch document: " + e.getMessage(), 500);
        }
    }

    /**
     * POST /api/documents
     * Tworzy nowy dokument (typ określony przez documentType w DTO).
     */
    public ApiResponse<DocumentDto> createDocument(DocumentDto dto) {
        try {
            DocumentDto created = documentService.create(dto);
            return ApiResponse.created(created);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create document: " + e.getMessage(), 400);
        }
    }

    /**
     * PUT /api/documents/{id}
     * Aktualizuje dokument.
     */
    public ApiResponse<DocumentDto> updateDocument(Long id, DocumentDto dto) {
        try {
            return documentService.update(id, dto)
                    .map(updated -> ApiResponse.success(updated, "Document updated successfully"))
                    .orElse(ApiResponse.notFound("Document", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to update document: " + e.getMessage(), 400);
        }
    }

    /**
     * DELETE /api/documents/{id}
     * Usuwa dokument.
     */
    public ApiResponse<Void> deleteDocument(Long id) {
        try {
            if (documentService.delete(id)) {
                return ApiResponse.success(null, "Document deleted successfully");
            }
            return ApiResponse.notFound("Document", id);
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete document: " + e.getMessage(), 500);
        }
    }

    // ==================== TYPE-SPECIFIC ENDPOINTS ====================

    /**
     * GET /api/invoices
     * Pobiera tylko faktury.
     */
    public ApiResponse<List<DocumentDto>> getAllInvoices() {
        try {
            List<DocumentDto> invoices = documentService.findAllInvoices();
            return ApiResponse.success(invoices, "Found " + invoices.size() + " invoices");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch invoices: " + e.getMessage(), 500);
        }
    }

    /**
     * POST /api/invoices
     * Tworzy nową fakturę.
     */
    public ApiResponse<DocumentDto> createInvoice(DocumentDto dto) {
        try {
            DocumentDto created = documentService.createInvoice(dto);
            return ApiResponse.created(created);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create invoice: " + e.getMessage(), 400);
        }
    }

    /**
     * GET /api/invoices/unpaid
     * Pobiera nieopłacone faktury.
     */
    public ApiResponse<List<DocumentDto>> getUnpaidInvoices() {
        try {
            List<DocumentDto> invoices = documentService.findUnpaidInvoices();
            return ApiResponse.success(invoices, "Found " + invoices.size() + " unpaid invoices");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch unpaid invoices: " + e.getMessage(), 500);
        }
    }

    /**
     * PUT /api/invoices/{id}/pay
     * Oznacza fakturę jako opłaconą.
     */
    public ApiResponse<DocumentDto> markInvoiceAsPaid(Long invoiceId) {
        try {
            return documentService.markInvoiceAsPaid(invoiceId)
                    .map(dto -> ApiResponse.success(dto, "Invoice marked as paid"))
                    .orElse(ApiResponse.error("Invoice not found or document is not an invoice", 404));
        } catch (Exception e) {
            return ApiResponse.error("Failed to mark invoice as paid: " + e.getMessage(), 400);
        }
    }

    /**
     * GET /api/reports
     * Pobiera tylko raporty.
     */
    public ApiResponse<List<DocumentDto>> getAllReports() {
        try {
            List<DocumentDto> reports = documentService.findAllReports();
            return ApiResponse.success(reports, "Found " + reports.size() + " reports");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch reports: " + e.getMessage(), 500);
        }
    }

    /**
     * POST /api/reports
     * Tworzy nowy raport.
     */
    public ApiResponse<DocumentDto> createReport(DocumentDto dto) {
        try {
            DocumentDto created = documentService.createReport(dto);
            return ApiResponse.created(created);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create report: " + e.getMessage(), 400);
        }
    }

    // ==================== DEMO SCENARIOS ====================

    /**
     * Demonstruje polimorficzne operacje z SINGLE_TABLE inheritance.
     */
    public void demonstratePolymorphicOperations() {
        System.out.println("=== Document (SINGLE_TABLE Inheritance) Demo ===\n");
        System.out.println("Document <- Report <- Invoice");
        System.out.println("All types stored in single 'document' table with discriminator column\n");

        // Tworzenie różnych typów dokumentów
        System.out.println("1. Creating Invoice...");
        DocumentDto invoice = DocumentDto.builder()
                .title("Faktura VAT 001/2024")
                .createdDate(LocalDate.now())
                .createdBy("System")
                .documentType("INVOICE")
                .invoiceNumber("FV/001/2024")
                .totalAmount(new BigDecimal("1500.00"))
                .taxAmount(new BigDecimal("345.00"))
                .issueDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(30))
                .paymentStatus("PENDING")
                .build();
        ApiResponse<DocumentDto> invoiceResponse = createInvoice(invoice);
        System.out.println("   Created: " + invoiceResponse.getData());

        System.out.println("\n2. Creating Report...");
        DocumentDto report = DocumentDto.builder()
                .title("Raport kwartalny Q1 2024")
                .createdDate(LocalDate.now())
                .createdBy("Manager")
                .content("Podsumowanie wyników za Q1...")
                .documentType("REPORT")
                .reportType("QUARTERLY")
                .periodStart(LocalDate.of(2024, 1, 1))
                .periodEnd(LocalDate.of(2024, 3, 31))
                .status("DRAFT")
                .build();
        ApiResponse<DocumentDto> reportResponse = createReport(report);
        System.out.println("   Created: " + reportResponse.getData());

        System.out.println("\n3. Creating base Document...");
        DocumentDto doc = DocumentDto.builder()
                .title("Notatka wewnętrzna")
                .createdDate(LocalDate.now())
                .createdBy("Admin")
                .content("Treść notatki...")
                .documentType("DOCUMENT")
                .build();
        ApiResponse<DocumentDto> docResponse = createDocument(doc);
        System.out.println("   Created: " + docResponse.getData());

        // Polimorficzne pobieranie wszystkich dokumentów
        System.out.println("\n4. Fetching ALL documents (polymorphic query)...");
        ApiResponse<List<DocumentDto>> allDocs = getAllDocuments();
        System.out.println("   " + allDocs.getMessage());
        allDocs.getData().forEach(d -> 
            System.out.println("   - [" + d.getDocumentType() + "] " + d.getTitle()));

        // Pobieranie tylko faktur
        System.out.println("\n5. Fetching only Invoices...");
        ApiResponse<List<DocumentDto>> invoices = getAllInvoices();
        System.out.println("   " + invoices.getMessage());

        // Pobieranie tylko raportów
        System.out.println("\n6. Fetching only Reports...");
        ApiResponse<List<DocumentDto>> reports = getAllReports();
        System.out.println("   " + reports.getMessage());

        // Operacja specyficzna dla podtypu
        if (invoiceResponse.isSuccess() && invoiceResponse.getData() != null) {
            System.out.println("\n7. Marking invoice as paid (type-specific operation)...");
            ApiResponse<DocumentDto> paidResponse = markInvoiceAsPaid(invoiceResponse.getData().getId());
            System.out.println("   " + paidResponse.getMessage());
            System.out.println("   Payment status: " + paidResponse.getData().getPaymentStatus());
        }

        // Pobieranie po ID - ORM zwraca konkretny podtyp
        if (invoiceResponse.isSuccess() && invoiceResponse.getData() != null) {
            System.out.println("\n8. Fetching by ID (returns concrete subtype)...");
            ApiResponse<DocumentDto> fetchedDoc = getDocumentById(invoiceResponse.getData().getId());
            System.out.println("   " + fetchedDoc.getMessage());
        }

        // Cleanup
        System.out.println("\n9. Cleaning up...");
        if (invoiceResponse.getData() != null) deleteDocument(invoiceResponse.getData().getId());
        if (reportResponse.getData() != null) deleteDocument(reportResponse.getData().getId());
        if (docResponse.getData() != null) deleteDocument(docResponse.getData().getId());
        System.out.println("   Cleanup completed");

        System.out.println("\n=== Demo completed ===");
    }
}

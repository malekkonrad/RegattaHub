package pl.edu.agh.dp.service;

import pl.edu.agh.dp.api.Session;
import pl.edu.agh.dp.api.SessionFactory;
import pl.edu.agh.dp.config.OrmConfig;
import pl.edu.agh.dp.dto.DocumentDto;
import pl.edu.agh.dp.entity.Document;
import pl.edu.agh.dp.entity.Invoice;
import pl.edu.agh.dp.entity.Report;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serwis dla operacji na Document i podklasach (Invoice, Report).
 * 
 * Demonstruje:
 * - SINGLE_TABLE inheritance
 * - Polimorficzne zapytania (findAll zwraca różne typy)
 * - Discriminator value
 */
public class DocumentService {

    private final SessionFactory sessionFactory;

    public DocumentService() {
        this.sessionFactory = OrmConfig.getSessionFactory();
    }

    public DocumentService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Tworzy nowy dokument (lub podtyp).
     * Demonstruje: polimorficzny zapis z SINGLE_TABLE
     */
    public DocumentDto create(DocumentDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Document document = dto.toEntity();
                session.save(document);
                session.commit();

                return DocumentDto.fromEntity(document);
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to create document: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Tworzy fakturę.
     */
    public DocumentDto createInvoice(DocumentDto dto) {
        dto.setDocumentType("INVOICE");
        return create(dto);
    }

    /**
     * Tworzy raport.
     */
    public DocumentDto createReport(DocumentDto dto) {
        dto.setDocumentType("REPORT");
        return create(dto);
    }

    /**
     * Znajduje dokument po ID.
     * Demonstruje: polimorficzne wyszukiwanie - zwraca konkretny podtyp
     */
    public Optional<DocumentDto> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Document document = session.find(Document.class, id);
            return Optional.ofNullable(document).map(DocumentDto::fromEntity);
        }
    }

    /**
     * Pobiera wszystkie dokumenty (wszystkich typów).
     * Demonstruje: polimorficzne findAll z SINGLE_TABLE
     */
    public List<DocumentDto> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<Document> documents = session.findAll(Document.class);
            return documents.stream()
                    .map(DocumentDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Pobiera tylko faktury.
     * Demonstruje: filtrowanie po typie w hierarchii
     */
    public List<DocumentDto> findAllInvoices() {
        try (Session session = sessionFactory.openSession()) {
            List<Invoice> invoices = session.findAll(Invoice.class);
            return invoices.stream()
                    .map(DocumentDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Pobiera tylko raporty.
     */
    public List<DocumentDto> findAllReports() {
        try (Session session = sessionFactory.openSession()) {
            List<Report> reports = session.findAll(Report.class);
            return reports.stream()
                    .map(DocumentDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Aktualizuje dokument.
     */
    public Optional<DocumentDto> update(Long id, DocumentDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Document existing = session.find(Document.class, id);
                if (existing == null) {
                    return Optional.empty();
                }

                // Aktualizuj wspólne pola
                existing.setTitle(dto.getTitle());
                existing.setCreatedDate(dto.getCreatedDate());
                existing.setCreatedBy(dto.getCreatedBy());
                existing.setContent(dto.getContent());

                // Aktualizuj pola specyficzne dla podtypu
                if (existing instanceof Invoice invoice) {
                    // Report fields (inherited)
                    invoice.setReportType(dto.getReportType());
                    invoice.setPeriodStart(dto.getPeriodStart());
                    invoice.setPeriodEnd(dto.getPeriodEnd());
                    invoice.setStatus(dto.getStatus());
                    // Invoice-specific fields
                    invoice.setInvoiceNumber(dto.getInvoiceNumber());
                    invoice.setIssueDate(dto.getIssueDate());
                    invoice.setDueDate(dto.getDueDate());
                    invoice.setTotalAmount(dto.getTotalAmount());
                    invoice.setTaxAmount(dto.getTaxAmount());
                    invoice.setPaymentStatus(dto.getPaymentStatus());
                } else if (existing instanceof Report report) {
                    report.setReportType(dto.getReportType());
                    report.setPeriodStart(dto.getPeriodStart());
                    report.setPeriodEnd(dto.getPeriodEnd());
                    report.setStatus(dto.getStatus());
                }

                session.update(existing);
                session.commit();

                return Optional.of(DocumentDto.fromEntity(existing));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to update document: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Usuwa dokument.
     */
    public boolean delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Document document = session.find(Document.class, id);
                if (document == null) {
                    return false;
                }

                session.delete(document);
                session.commit();
                return true;
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to delete document: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Oznacza fakturę jako opłaconą.
     * Demonstruje: operacja specyficzna dla podtypu
     */
    public Optional<DocumentDto> markInvoiceAsPaid(Long invoiceId) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Document document = session.find(Document.class, invoiceId);
                if (!(document instanceof Invoice invoice)) {
                    return Optional.empty();
                }

                invoice.setPaymentStatus("PAID");
                session.update(invoice);
                session.commit();

                return Optional.of(DocumentDto.fromEntity(invoice));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to mark invoice as paid: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Pobiera nieopłacone faktury.
     */
    public List<DocumentDto> findUnpaidInvoices() {
        try (Session session = sessionFactory.openSession()) {
            List<Invoice> invoices = session.findAll(Invoice.class);
            return invoices.stream()
                    .filter(i -> i.getPaymentStatus() == null || 
                                 "PENDING".equals(i.getPaymentStatus()) ||
                                 !i.getPaymentStatus().equals("PAID"))
                    .map(DocumentDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }
}

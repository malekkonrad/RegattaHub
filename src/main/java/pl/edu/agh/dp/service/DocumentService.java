package pl.edu.agh.dp.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.dp.core.api.Session;
import pl.edu.agh.dp.core.api.SessionFactory;
import pl.edu.agh.dp.config.OrmConfig;
import pl.edu.agh.dp.dto.CurriculumDto;
import pl.edu.agh.dp.dto.DocumentDto;
import pl.edu.agh.dp.dto.InvoiceDto;
import pl.edu.agh.dp.dto.ReportDto;
import pl.edu.agh.dp.entity.Curriculum;
import pl.edu.agh.dp.entity.Document;
import pl.edu.agh.dp.entity.Invoice;
import pl.edu.agh.dp.entity.Report;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serwis dla operacji na Document (SINGLE_TABLE inheritance).
 */
@Service
public class DocumentService {

    private final SessionFactory sessionFactory;

    public DocumentService() {
        this.sessionFactory = OrmConfig.getSessionFactory();
    }

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

    public ReportDto createReport(ReportDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Report document = (Report) dto.toEntity();
                session.save(document);
                session.commit();
                return ReportDto.fromEntity(document);
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to create report: " + e.getMessage(), e);
            }
        }
    }

    public InvoiceDto createInvoice(InvoiceDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Invoice document = (Invoice) dto.toEntity();
                session.save(document);
                session.commit();
                return InvoiceDto.fromEntity(document);
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to create invoice: " + e.getMessage(), e);
            }
        }
    }

    public CurriculumDto createCirriculum(CurriculumDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Curriculum document = (Curriculum) dto.toEntity();
                session.save(document);
                session.commit();
                return CurriculumDto.fromEntity(document);
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to create cirriculum: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Znajduje dokument po ID i automatycznie rzutuje na odpowiednie DTO.
     */
    public Optional<DocumentDto> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Document document = session.find(Document.class, id);
            if (document != null) {
                document.getAuthorizedEmployees().size();
                return Optional.of(DocumentDto.fromEntity(document));
            }
            return Optional.empty();
        }
    }

    public Optional<ReportDto> findReportById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Report document = session.find(Report.class, id);
            return Optional.ofNullable(document).map(ReportDto::fromEntity);
        }
    }

    public Optional<InvoiceDto> findInvoiceById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Invoice document = session.find(Invoice.class, id);
            return Optional.ofNullable(document).map(InvoiceDto::fromEntity);
        }
    }

    public Optional<CurriculumDto> findCirriculumById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Curriculum document = session.find(Curriculum.class, id);
            return Optional.ofNullable(document).map(CurriculumDto::fromEntity);
        }
    }

    public List<DocumentDto> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<Document> documents = session.findAll(Document.class);
            for (Document document : documents) {
                document.getAuthorizedEmployees().size();
            }
            return documents.stream()
                    .map(DocumentDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Pobiera wszystkie raporty (bez faktur) - tylko "czyste" Report.
     */
//    public List<ReportDto> findAllReports() {
//        try (Session session = sessionFactory.openSession()) {
//            return session.findAll(Report.class).stream()
//                    .filter(report -> !(report instanceof Invoice))  // filtruj faktury
//                    .map(ReportDto::fromEntity)
//                    .collect(Collectors.toList());
//        }
//    }

    /**
     * Pobiera wszystkie raporty włącznie z fakturami - zwraca polimorficzne DTO.
     */
    public List<ReportDto> findAllReports() {
        try (Session session = sessionFactory.openSession()) {
            return session.findAll(Report.class).stream()
                    .map(report -> {
                        if (report instanceof Invoice inv) {
                            return InvoiceDto.fromEntity(inv);
                        }
                        return ReportDto.fromEntity(report);
                    })
                    .collect(Collectors.toList());
        }
    }

    public List<InvoiceDto> findAllInvoices() {
        try (Session session = sessionFactory.openSession()) {
            return session.findAll(Invoice.class).stream()
                    .map(InvoiceDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    public List<CurriculumDto> findAllCirriculums() {
        try (Session session = sessionFactory.openSession()) {
            return session.findAll(Curriculum.class).stream()
                    .map(CurriculumDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    public Optional<ReportDto> updateReport(Long id, ReportDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Report updated = (Report) dto.toEntity();
                updated.setId(id);
                session.update(updated);
                session.commit();
                return Optional.of(ReportDto.fromEntity(updated));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to update report: " + e.getMessage(), e);
            }
        }
    }

    public Optional<InvoiceDto> updateInvoice(Long id, InvoiceDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Invoice updated = (Invoice) dto.toEntity();
                updated.setId(id);
                session.update(updated);
                session.commit();
                return Optional.of(InvoiceDto.fromEntity(updated));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to update invoice: " + e.getMessage(), e);
            }
        }
    }

    public Optional<CurriculumDto> updateCirriculum(Long id, CurriculumDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Curriculum updated = (Curriculum) dto.toEntity();
                updated.setId(id);
                session.update(updated);
                session.commit();
                return Optional.of(CurriculumDto.fromEntity(updated));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to update cirriculum: " + e.getMessage(), e);
            }
        }
    }

    public boolean deleteReport(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Report document = session.find(Report.class, id);
                if (document == null) {
                    return false;
                }
                session.delete(document);
                session.commit();
                return true;
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to delete report: " + e.getMessage(), e);
            }
        }
    }

    public boolean deleteInvoice(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Invoice document = session.find(Invoice.class, id);
                if (document == null) {
                    return false;
                }
                session.delete(document);
                session.commit();
                return true;
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to delete invoice: " + e.getMessage(), e);
            }
        }
    }

    public boolean deleteCirriculum(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Curriculum document = session.find(Curriculum.class, id);
                if (document == null) {
                    return false;
                }
                session.delete(document);
                session.commit();
                return true;
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to delete cirriculum: " + e.getMessage(), e);
            }
        }
    }

    // ==================== FINDER API DEMONSTRATION (SINGLE_TABLE INHERITANCE) ====================

    /**
     * Wyszukuje wszystkie dokumenty po autorze (pole z klasy bazowej Document).
     * Demonstruje Finder na klasie bazowej SINGLE_TABLE - zwraca polimorficznie wszystkie typy.
     */
    public List<DocumentDto> findDocumentsByCreatedBy(String createdBy) {
        try (Session session = sessionFactory.openSession()) {
            List<Document> documents = session.finder(Document.class)
                    .eq("createdBy", createdBy)
                    .orderDesc("createdDate")
                    .list();
            
            return documents.stream()
                    .map(DocumentDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Wyszukuje faktury po statusie płatności (pole specyficzne dla Invoice).
     * Demonstruje Finder na klasie pochodnej z warunkiem na polu dziecka.
     */
    public List<InvoiceDto> findInvoicesByPaymentStatus(String paymentStatus) {
        try (Session session = sessionFactory.openSession()) {
            List<Invoice> invoices = session.finder(Invoice.class)
                    .eq("paymentStatus", paymentStatus)
                    .orderDesc("dueDate")
                    .list();
            
            return invoices.stream()
                    .map(InvoiceDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Wyszukuje faktury z kwotą większą niż podana.
     * Demonstruje Finder gt() na klasie pochodnej SINGLE_TABLE.
     */
    public List<InvoiceDto> findInvoicesByAmountGreaterThan(java.math.BigDecimal minAmount) {
        try (Session session = sessionFactory.openSession()) {
            List<Invoice> invoices = session.finder(Invoice.class)
                    .gt("totalAmount", minAmount)
                    .orderDesc("totalAmount")
                    .list();
            
            return invoices.stream()
                    .map(InvoiceDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Wyszukuje raporty po typie i statusie (pola z klasy Report).
     * Demonstruje Finder z wieloma warunkami na klasie pośredniej w hierarchii.
     */
    public List<ReportDto> findReportsByTypeAndStatus(String reportType, String status) {
        try (Session session = sessionFactory.openSession()) {
            List<Report> reports = session.finder(Report.class)
                    .eq("reportType", reportType)
                    .eq("status", status)
                    .orderAsc("periodStart")
                    .list();
            
            return reports.stream()
                    .map(report -> {
                        if (report instanceof Invoice inv) {
                            return InvoiceDto.fromEntity(inv);
                        }
                        return ReportDto.fromEntity(report);
                    })
                    .collect(Collectors.toList());
        }
    }

    /**
     * Wyszukuje dokumenty po tytule (LIKE) - polimorficzne wyszukiwanie.
     * Demonstruje Finder like() na klasie bazowej zwracający wszystkie typy dokumentów.
     */
    public List<DocumentDto> findDocumentsByTitleLike(String titlePattern) {
        try (Session session = sessionFactory.openSession()) {
            List<Document> documents = session.finder(Document.class)
                    .like("title", "%" + titlePattern + "%")
                    .orderAsc("title")
                    .list();
            
            return documents.stream()
                    .map(DocumentDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }
}

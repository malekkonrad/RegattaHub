package pl.edu.agh.dp.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.dp.api.Session;
import pl.edu.agh.dp.api.SessionFactory;
import pl.edu.agh.dp.config.OrmConfig;
import pl.edu.agh.dp.dto.ClientDto;
import pl.edu.agh.dp.entity.Client;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serwis dla operacji na Client.
 * 
 * Demonstruje:
 * - JOINED inheritance (Client extends Person)
 * - Operacje CRUD na encji z dziedziczeniem
 */
@Service
public class ClientService {

    private final SessionFactory sessionFactory;

    public ClientService() {
        this.sessionFactory = OrmConfig.getSessionFactory();
    }

    public ClientService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Tworzy nowego klienta.
     * Demonstruje: zapis encji z JOINED inheritance
     */
    public ClientDto create(ClientDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Client client = dto.toEntity();
                session.save(client);
                session.commit();

                return ClientDto.fromEntity(client);
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to create client: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Znajduje klienta po ID.
     */
    public Optional<ClientDto> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Client client = session.find(Client.class, id);
            return Optional.ofNullable(client).map(ClientDto::fromEntity);
        }
    }

    /**
     * Pobiera wszystkich klientów.
     */
    public List<ClientDto> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<Client> clients = session.findAll(Client.class);
            return clients.stream()
                    .map(ClientDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Aktualizuje klienta.
     */
    public Optional<ClientDto> update(Long id, ClientDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Client existing = session.find(Client.class, id);
                if (existing == null) {
                    return Optional.empty();
                }

                // Pola z Person
                existing.setFirstName(dto.getFirstName());
                existing.setLastName(dto.getLastName());
                existing.setEmail(dto.getEmail());
                existing.setPhone(dto.getPhone());

                // Pola specyficzne dla Client
                existing.setCompanyName(dto.getCompanyName());
                existing.setTaxId(dto.getTaxId());
                existing.setAddress(dto.getAddress());

                session.update(existing);
                session.commit();

                return Optional.of(ClientDto.fromEntity(existing));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to update client: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Usuwa klienta.
     */
    public boolean delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Client client = session.find(Client.class, id);
                if (client == null) {
                    return false;
                }

                session.delete(client);
                session.commit();
                return true;
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to delete client: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Wyszukuje klientów po nazwie firmy.
     * Demonstruje: filtrowanie w pamięci (w prawdziwym ORM byłby Query)
     */
    public List<ClientDto> findByCompanyName(String companyName) {
        try (Session session = sessionFactory.openSession()) {
            List<Client> clients = session.findAll(Client.class);
            return clients.stream()
                    .filter(c -> c.getCompanyName() != null && 
                                 c.getCompanyName().toLowerCase().contains(companyName.toLowerCase()))
                    .map(ClientDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }
}

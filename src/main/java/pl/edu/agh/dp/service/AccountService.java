package pl.edu.agh.dp.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.dp.api.Session;
import pl.edu.agh.dp.api.SessionFactory;
import pl.edu.agh.dp.config.OrmConfig;
import pl.edu.agh.dp.dto.AccountDto;
import pl.edu.agh.dp.entity.Account;
import pl.edu.agh.dp.entity.BankAccount;
import pl.edu.agh.dp.entity.SavingsAccount;
import pl.edu.agh.dp.entity.InvestmentAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serwis dla operacji na Account (JOINED inheritance).
 */
@Service
public class AccountService {

    private final SessionFactory sessionFactory;

    public AccountService() {
        this.sessionFactory = OrmConfig.getSessionFactory();
    }

    public AccountDto create(AccountDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Account account = dto.toEntity();
                session.save(account);
                session.commit();
                return AccountDto.fromEntity(account);
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to create account: " + e.getMessage(), e);
            }
        }
    }

    public Optional<AccountDto> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Account account = session.find(Account.class, id);
            return Optional.ofNullable(account).map(AccountDto::fromEntity);
        }
    }

    public List<AccountDto> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<AccountDto> result = new ArrayList<>();
            
            // Pobierz wszystkie typy kont
            List<BankAccount> bankAccounts = session.findAll(BankAccount.class);
            List<SavingsAccount> savingsAccounts = session.findAll(SavingsAccount.class);
            List<InvestmentAccount> investmentAccounts = session.findAll(InvestmentAccount.class);
            
            bankAccounts.forEach(a -> result.add(AccountDto.fromEntity(a)));
            savingsAccounts.forEach(a -> result.add(AccountDto.fromEntity(a)));
            investmentAccounts.forEach(a -> result.add(AccountDto.fromEntity(a)));
            
            return result;
        }
    }

    public List<AccountDto> findAllBankAccounts() {
        try (Session session = sessionFactory.openSession()) {
            return session.findAll(BankAccount.class).stream()
                    .map(AccountDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    public List<AccountDto> findAllSavingsAccounts() {
        try (Session session = sessionFactory.openSession()) {
            return session.findAll(SavingsAccount.class).stream()
                    .map(AccountDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    public List<AccountDto> findAllInvestmentAccounts() {
        try (Session session = sessionFactory.openSession()) {
            return session.findAll(InvestmentAccount.class).stream()
                    .map(AccountDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    public Optional<AccountDto> update(Long id, AccountDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Account existing = session.find(Account.class, id);
                if (existing == null) {
                    return Optional.empty();
                }
                
                Account updated = dto.toEntity();
                updated.setId(id);
                session.update(updated);
                session.commit();
                return Optional.of(AccountDto.fromEntity(updated));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to update account: " + e.getMessage(), e);
            }
        }
    }

    public boolean delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                Account account = session.find(Account.class, id);
                if (account == null) {
                    return false;
                }
                session.delete(account);
                session.commit();
                return true;
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to delete account: " + e.getMessage(), e);
            }
        }
    }
}

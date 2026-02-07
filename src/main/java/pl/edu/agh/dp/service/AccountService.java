package pl.edu.agh.dp.service;

import org.springframework.stereotype.Service;
import pl.edu.agh.dp.core.api.Session;
import pl.edu.agh.dp.core.api.SessionFactory;
import pl.edu.agh.dp.config.OrmConfig;
import pl.edu.agh.dp.dto.*;
import pl.edu.agh.dp.entity.*;

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

    public BankAccountDto createBank(BankAccountDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                BankAccount account = (BankAccount) dto.toEntity();
                session.save(account);
                session.commit();
                return BankAccountDto.fromEntity(account);
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to create bank account: " + e.getMessage(), e);
            }
        }
    }

    public SavingsAccountDto createSavings(SavingsAccountDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                SavingsAccount account = (SavingsAccount) dto.toEntity();
                session.save(account);
                session.commit();
                return SavingsAccountDto.fromEntity(account);
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to create savings account: " + e.getMessage(), e);
            }
        }
    }

    public InvestmentAccountDto createInvestment(InvestmentAccountDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                InvestmentAccount account = (InvestmentAccount) dto.toEntity();
                session.save(account);
                session.commit();
                return InvestmentAccountDto.fromEntity(account);
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to create investment account: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Znajduje konto po ID i automatycznie rzutuje na odpowiednie DTO.
     * Przeszukuje wszystkie typy kont.
     */
    public Optional<AccountDto> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Account account = session.find(Account.class, id);
            if (account != null) {
                return Optional.of(AccountDto.fromEntity(account));
            }
            return Optional.empty();
        }
    }

    public Optional<BankAccountDto> findBankById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            BankAccount account = session.find(BankAccount.class, id);
            return Optional.ofNullable(account).map(BankAccountDto::fromEntity);
        }
    }

    public Optional<SavingsAccountDto> findSavingsById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            SavingsAccount account = session.find(SavingsAccount.class, id);
            return Optional.ofNullable(account).map(SavingsAccountDto::fromEntity);
        }
    }

    public Optional<InvestmentAccountDto> findInvestmentById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            InvestmentAccount account = session.find(InvestmentAccount.class, id);
            return Optional.ofNullable(account).map(InvestmentAccountDto::fromEntity);
        }
    }

    public List<AccountDto> findAll() {
        try (Session session = sessionFactory.openSession()) {
            List<AccountDto> result = new ArrayList<>();
            
            List<BankAccount> bankAccounts = session.findAll(BankAccount.class);
            List<SavingsAccount> savingsAccounts = session.findAll(SavingsAccount.class);
            List<InvestmentAccount> investmentAccounts = session.findAll(InvestmentAccount.class);
            
            bankAccounts.forEach(a -> result.add(BankAccountDto.fromEntity(a)));
            savingsAccounts.forEach(a -> result.add(SavingsAccountDto.fromEntity(a)));
            investmentAccounts.forEach(a -> result.add(InvestmentAccountDto.fromEntity(a)));
            
            return result;
        }
    }

    public List<BankAccountDto> findAllBankAccounts() {
        try (Session session = sessionFactory.openSession()) {
            return session.findAll(BankAccount.class).stream()
                    .map(BankAccountDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    public List<SavingsAccountDto> findAllSavingsAccounts() {
        try (Session session = sessionFactory.openSession()) {
            return session.findAll(SavingsAccount.class).stream()
                    .map(SavingsAccountDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    public List<InvestmentAccountDto> findAllInvestmentAccounts() {
        try (Session session = sessionFactory.openSession()) {
            return session.findAll(InvestmentAccount.class).stream()
                    .map(InvestmentAccountDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    public Optional<BankAccountDto> updateBank(Long id, BankAccountDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                BankAccount updated = (BankAccount) dto.toEntity();
                updated.setId(id);
                session.update(updated);
                session.commit();
                return Optional.of(BankAccountDto.fromEntity(updated));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to update bank account: " + e.getMessage(), e);
            }
        }
    }

    public Optional<SavingsAccountDto> updateSavings(Long id, SavingsAccountDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                SavingsAccount updated = (SavingsAccount) dto.toEntity();
                updated.setId(id);
                session.update(updated);
                session.commit();
                return Optional.of(SavingsAccountDto.fromEntity(updated));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to update savings account: " + e.getMessage(), e);
            }
        }
    }

    public Optional<InvestmentAccountDto> updateInvestment(Long id, InvestmentAccountDto dto) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                InvestmentAccount updated = (InvestmentAccount) dto.toEntity();
                updated.setId(id);
                session.update(updated);
                session.commit();
                return Optional.of(InvestmentAccountDto.fromEntity(updated));
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to update investment account: " + e.getMessage(), e);
            }
        }
    }

    public boolean deleteBank(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                BankAccount account = session.find(BankAccount.class, id);
                if (account == null) {
                    return false;
                }
                session.delete(account);
                session.commit();
                return true;
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to delete bank account: " + e.getMessage(), e);
            }
        }
    }

    public boolean deleteSavings(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                SavingsAccount account = session.find(SavingsAccount.class, id);
                if (account == null) {
                    return false;
                }
                session.delete(account);
                session.commit();
                return true;
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to delete savings account: " + e.getMessage(), e);
            }
        }
    }

    public boolean deleteInvestment(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.begin();
            try {
                InvestmentAccount account = session.find(InvestmentAccount.class, id);
                if (account == null) {
                    return false;
                }
                session.delete(account);
                session.commit();
                return true;
            } catch (Exception e) {
                session.rollback();
                throw new RuntimeException("Failed to delete investment account: " + e.getMessage(), e);
            }
        }
    }

    // ==================== FINDER API DEMONSTRATION (JOINED INHERITANCE) ====================

    /**
     * Wyszukuje wszystkie konta po walucie (pole z klasy bazowej Account).
     * Demonstruje Finder na klasie bazowej JOINED - zwraca polimorficznie wszystkie typy kont.
     */
    public List<AccountDto> findAccountsByCurrency(String currency) {
        try (Session session = sessionFactory.openSession()) {
            List<Account> accounts = session.finder(Account.class)
                    .eq("currency", currency)
                    .orderAsc("accountName")
                    .list();
            
            return accounts.stream()
                    .map(AccountDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Wyszukuje konta oszczędnościowe z oprocentowaniem większym niż podane.
     * Demonstruje Finder gt() na klasie pochodnej z polem specyficznym (interestRate).
     */
    public List<SavingsAccountDto> findSavingsAccountsByInterestRateGreaterThan(java.math.BigDecimal minRate) {
        try (Session session = sessionFactory.openSession()) {
            List<SavingsAccount> accounts = session.finder(SavingsAccount.class)
                    .gt("interestRate", minRate)
                    .orderDesc("interestRate")
                    .list();
            
            return accounts.stream()
                    .map(SavingsAccountDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Wyszukuje konta z saldem w podanym zakresie.
     * Demonstruje Finder between() na polu z klasy bazowej Account.
     */
    public List<AccountDto> findAccountsByBalanceBetween(java.math.BigDecimal minBalance, java.math.BigDecimal maxBalance) {
        try (Session session = sessionFactory.openSession()) {
            List<Account> accounts = session.finder(Account.class)
                    .between("balance", minBalance, maxBalance)
                    .orderAsc("balance")
                    .list();
            
            return accounts.stream()
                    .map(AccountDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Wyszukuje aktywne konta po walucie i minimalnym saldzie.
     * Demonstruje Finder z wieloma warunkami (eq + gt + eq) na hierarchii JOINED.
     */
    public List<AccountDto> findActiveAccountsByCurrencyAndMinBalance(String currency, java.math.BigDecimal minBalance) {
        try (Session session = sessionFactory.openSession()) {
            List<Account> accounts = session.finder(Account.class)
                    .eq("currency", currency)
                    .gt("balance", minBalance)
                    .eq("isActive", true)
                    .orderDesc("balance")
                    .list();
            
            return accounts.stream()
                    .map(AccountDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Wyszukuje konta bankowe po typie karty.
     * Demonstruje Finder eq() na polu specyficznym klasy pochodnej BankAccount.
     */
    public List<BankAccountDto> findBankAccountsByCardType(String cardType) {
        try (Session session = sessionFactory.openSession()) {
            List<BankAccount> accounts = session.finder(BankAccount.class)
                    .eq("cardType", cardType)
                    .orderAsc("accountName")
                    .list();
            
            return accounts.stream()
                    .map(BankAccountDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }
}

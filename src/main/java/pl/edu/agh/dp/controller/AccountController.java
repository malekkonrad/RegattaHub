package pl.edu.agh.dp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.dp.dto.AccountDto;
import pl.edu.agh.dp.dto.BankAccountDto;
import pl.edu.agh.dp.dto.SavingsAccountDto;
import pl.edu.agh.dp.dto.InvestmentAccountDto;
import pl.edu.agh.dp.dto.ApiResponse;
import pl.edu.agh.dp.service.AccountService;

import java.util.List;

/**
 * Kontroler REST dla Account (JOINED inheritance).
 * Demonstruje hierarchię: Account <- BankAccount, SavingsAccount, InvestmentAccount
 */
@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts", description = "Zarządzanie kontami (JOINED inheritance)")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/")
    @Operation(summary = "Pobierz wszystkie konta", description = "Zwraca wszystkie typy kont (Bank, Savings, Investment)")
    public ApiResponse<List<AccountDto>> getAllAccounts() {
        try {
            List<AccountDto> accounts = accountService.findAll();
            return ApiResponse.success(accounts, "Found " + accounts.size() + " accounts");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch accounts: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz konto po ID", description = "Automatycznie zwraca odpowiedni typ DTO (Bank, Savings, Investment)")
    public ApiResponse<AccountDto> getAccountById(@PathVariable Long id) {
        try {
            return accountService.findById(id)
                    .map(dto -> ApiResponse.success(dto, "Account found"))
                    .orElse(ApiResponse.notFound("Account", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch account: " + e.getMessage(), 500);
        }
    }

    // ==================== BANK ACCOUNT ENDPOINTS ====================

    @GetMapping("/bank")
    @Operation(summary = "Pobierz konta bankowe", description = "Zwraca tylko konta typu BankAccount")
    public ApiResponse<List<BankAccountDto>> getBankAccounts() {
        try {
            List<BankAccountDto> accounts = accountService.findAllBankAccounts();
            return ApiResponse.success(accounts, "Found " + accounts.size() + " bank accounts");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch bank accounts: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/bank/{id}")
    @Operation(summary = "Pobierz konto bankowe po ID")
    public ApiResponse<BankAccountDto> getBankAccountById(@PathVariable Long id) {
        try {
            return accountService.findBankById(id)
                    .map(dto -> ApiResponse.success(dto, "Bank account found"))
                    .orElse(ApiResponse.notFound("Bank account", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch bank account: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/bank")
    @Operation(summary = "Utwórz nowe konto bankowe")
    public ApiResponse<BankAccountDto> createBankAccount(@RequestBody BankAccountDto dto) {
        try {
            BankAccountDto created = accountService.createBank(dto);
            return ApiResponse.created(created);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create bank account: " + e.getMessage(), 400);
        }
    }

    @PutMapping("/bank/{id}")
    @Operation(summary = "Aktualizuj konto bankowe")
    public ApiResponse<BankAccountDto> updateBankAccount(@PathVariable Long id, @RequestBody BankAccountDto dto) {
        try {
            return accountService.updateBank(id, dto)
                    .map(updated -> ApiResponse.success(updated, "Bank account updated successfully"))
                    .orElse(ApiResponse.notFound("Bank account", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to update bank account: " + e.getMessage(), 400);
        }
    }

    @DeleteMapping("/bank/{id}")
    @Operation(summary = "Usuń konto bankowe")
    public ApiResponse<Void> deleteBankAccount(@PathVariable Long id) {
        try {
            if (accountService.deleteBank(id)) {
                return ApiResponse.success(null, "Bank account deleted successfully");
            }
            return ApiResponse.notFound("Bank account", id);
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete bank account: " + e.getMessage(), 500);
        }
    }

    // ==================== SAVINGS ACCOUNT ENDPOINTS ====================

    @GetMapping("/savings")
    @Operation(summary = "Pobierz konta oszczędnościowe", description = "Zwraca tylko konta typu SavingsAccount")
    public ApiResponse<List<SavingsAccountDto>> getSavingsAccounts() {
        try {
            List<SavingsAccountDto> accounts = accountService.findAllSavingsAccounts();
            return ApiResponse.success(accounts, "Found " + accounts.size() + " savings accounts");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch savings accounts: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/savings/{id}")
    @Operation(summary = "Pobierz konto oszczędnościowe po ID")
    public ApiResponse<SavingsAccountDto> getSavingsAccountById(@PathVariable Long id) {
        try {
            return accountService.findSavingsById(id)
                    .map(dto -> ApiResponse.success(dto, "Savings account found"))
                    .orElse(ApiResponse.notFound("Savings account", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch savings account: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/savings")
    @Operation(summary = "Utwórz nowe konto oszczędnościowe")
    public ApiResponse<SavingsAccountDto> createSavingsAccount(@RequestBody SavingsAccountDto dto) {
        try {
            SavingsAccountDto created = accountService.createSavings(dto);
            return ApiResponse.created(created);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create savings account: " + e.getMessage(), 400);
        }
    }

    @PutMapping("/savings/{id}")
    @Operation(summary = "Aktualizuj konto oszczędnościowe")
    public ApiResponse<SavingsAccountDto> updateSavingsAccount(@PathVariable Long id, @RequestBody SavingsAccountDto dto) {
        try {
            return accountService.updateSavings(id, dto)
                    .map(updated -> ApiResponse.success(updated, "Savings account updated successfully"))
                    .orElse(ApiResponse.notFound("Savings account", id));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResponse.error("Failed to update savings account: " + e.getMessage(), 400);
        }
    }

    @DeleteMapping("/savings/{id}")
    @Operation(summary = "Usuń konto oszczędnościowe")
    public ApiResponse<Void> deleteSavingsAccount(@PathVariable Long id) {
        try {
            if (accountService.deleteSavings(id)) {
                return ApiResponse.success(null, "Savings account deleted successfully");
            }
            return ApiResponse.notFound("Savings account", id);
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete savings account: " + e.getMessage(), 500);
        }
    }

    // ==================== INVESTMENT ACCOUNT ENDPOINTS ====================

    @GetMapping("/investment")
    @Operation(summary = "Pobierz konta inwestycyjne", description = "Zwraca tylko konta typu InvestmentAccount")
    public ApiResponse<List<InvestmentAccountDto>> getInvestmentAccounts() {
        try {
            List<InvestmentAccountDto> accounts = accountService.findAllInvestmentAccounts();
            return ApiResponse.success(accounts, "Found " + accounts.size() + " investment accounts");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch investment accounts: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/investment/{id}")
    @Operation(summary = "Pobierz konto inwestycyjne po ID")
    public ApiResponse<InvestmentAccountDto> getInvestmentAccountById(@PathVariable Long id) {
        try {
            return accountService.findInvestmentById(id)
                    .map(dto -> ApiResponse.success(dto, "Investment account found"))
                    .orElse(ApiResponse.notFound("Investment account", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch investment account: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/investment")
    @Operation(summary = "Utwórz nowe konto inwestycyjne")
    public ApiResponse<InvestmentAccountDto> createInvestmentAccount(@RequestBody InvestmentAccountDto dto) {
        try {
            InvestmentAccountDto created = accountService.createInvestment(dto);
            return ApiResponse.created(created);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create investment account: " + e.getMessage(), 400);
        }
    }

    @PutMapping("/investment/{id}")
    @Operation(summary = "Aktualizuj konto inwestycyjne")
    public ApiResponse<InvestmentAccountDto> updateInvestmentAccount(@PathVariable Long id, @RequestBody InvestmentAccountDto dto) {
        try {
            return accountService.updateInvestment(id, dto)
                    .map(updated -> ApiResponse.success(updated, "Investment account updated successfully"))
                    .orElse(ApiResponse.notFound("Investment account", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to update investment account: " + e.getMessage(), 400);
        }
    }

    @DeleteMapping("/investment/{id}")
    @Operation(summary = "Usuń konto inwestycyjne")
    public ApiResponse<Void> deleteInvestmentAccount(@PathVariable Long id) {
        try {
            if (accountService.deleteInvestment(id)) {
                return ApiResponse.success(null, "Investment account deleted successfully");
            }
            return ApiResponse.notFound("Investment account", id);
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete investment account: " + e.getMessage(), 500);
        }
    }

    // ==================== POLIMORFICZNY ENDPOINT ====================

    @PostMapping("/")
    @Operation(summary = "Utwórz nowe konto (polimorficznie)", description = "Typ konta określany przez pole accountType: BANK, SAVINGS, INVESTMENT")
    public ApiResponse<AccountDto> createAccount(@RequestBody AccountDto dto) {
        try {
            AccountDto created = accountService.create(dto);
            return ApiResponse.created(created);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create account: " + e.getMessage(), 400);
        }
    }
}

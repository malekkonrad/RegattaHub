package pl.edu.agh.dp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.dp.dto.AccountDto;
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

    @GetMapping("/bank")
    @Operation(summary = "Pobierz konta bankowe", description = "Zwraca tylko konta typu BankAccount")
    public ApiResponse<List<AccountDto>> getBankAccounts() {
        try {
            List<AccountDto> accounts = accountService.findAllBankAccounts();
            return ApiResponse.success(accounts, "Found " + accounts.size() + " bank accounts");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch bank accounts: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/savings")
    @Operation(summary = "Pobierz konta oszczędnościowe", description = "Zwraca tylko konta typu SavingsAccount")
    public ApiResponse<List<AccountDto>> getSavingsAccounts() {
        try {
            List<AccountDto> accounts = accountService.findAllSavingsAccounts();
            return ApiResponse.success(accounts, "Found " + accounts.size() + " savings accounts");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch savings accounts: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/investment")
    @Operation(summary = "Pobierz konta inwestycyjne", description = "Zwraca tylko konta typu InvestmentAccount")
    public ApiResponse<List<AccountDto>> getInvestmentAccounts() {
        try {
            List<AccountDto> accounts = accountService.findAllInvestmentAccounts();
            return ApiResponse.success(accounts, "Found " + accounts.size() + " investment accounts");
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch investment accounts: " + e.getMessage(), 500);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz konto po ID")
    public ApiResponse<AccountDto> getAccountById(@PathVariable Long id) {
        try {
            return accountService.findById(id)
                    .map(dto -> ApiResponse.success(dto, "Account found"))
                    .orElse(ApiResponse.notFound("Account", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch account: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/")
    @Operation(summary = "Utwórz nowe konto", description = "Typ konta określany przez pole accountType: BANK, SAVINGS, INVESTMENT")
    public ApiResponse<AccountDto> createAccount(@RequestBody AccountDto dto) {
        try {
            AccountDto created = accountService.create(dto);
            return ApiResponse.created(created);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create account: " + e.getMessage(), 400);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualizuj konto")
    public ApiResponse<AccountDto> updateAccount(@PathVariable Long id, @RequestBody AccountDto dto) {
        try {
            return accountService.update(id, dto)
                    .map(updated -> ApiResponse.success(updated, "Account updated successfully"))
                    .orElse(ApiResponse.notFound("Account", id));
        } catch (Exception e) {
            return ApiResponse.error("Failed to update account: " + e.getMessage(), 400);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń konto")
    public ApiResponse<Void> deleteAccount(@PathVariable Long id) {
        try {
            if (accountService.delete(id)) {
                return ApiResponse.success(null, "Account deleted successfully");
            }
            return ApiResponse.notFound("Account", id);
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete account: " + e.getMessage(), 500);
        }
    }
}

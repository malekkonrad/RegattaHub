package pl.edu.agh.dp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.agh.dp.entity.Account;
import pl.edu.agh.dp.entity.BankAccount;
import pl.edu.agh.dp.entity.SavingsAccount;
import pl.edu.agh.dp.entity.InvestmentAccount;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO dla hierarchii Account (JOINED inheritance).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private Long id;
    private String accountNumber;
    private String accountName;
    private BigDecimal balance;
    private LocalDate openDate;
    private String currency;
    private Boolean isActive;
    
    // Typ konta - określa podklasę
    private String accountType; // BANK, SAVINGS, INVESTMENT
    
    // Pola BankAccount
    private String bankName;
    private String iban;
    private String swift;
    private String branchCode;
    private Boolean hasDebitCard;
    private Boolean hasOnlineBanking;
    
    // Pola SavingsAccount
    private BigDecimal interestRate;
    private BigDecimal minimumBalance;
    private Integer withdrawalLimit;
    private Boolean hasAutomaticTransfer;
    private String savingsGoal;
    
    // Pola InvestmentAccount
    private String riskLevel;
    private String portfolioType;
    private BigDecimal managementFee;
    private Boolean hasBrokerAccess;
    private String investmentStrategy;
    private BigDecimal minimumInvestment;

    public static AccountDto fromEntity(Account account) {
        if (account == null) return null;

        AccountDto.AccountDtoBuilder builder = AccountDto.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountName(account.getAccountName())
                .balance(account.getBalance())
                .openDate(account.getOpenDate())
                .currency(account.getCurrency())
                .isActive(account.getIsActive());

        if (account instanceof BankAccount ba) {
            builder.accountType("BANK")
                    .bankName(ba.getBankName())
                    .iban(ba.getIban())
                    .swift(ba.getSwift())
                    .branchCode(ba.getBranchCode())
                    .hasDebitCard(ba.getHasDebitCard())
                    .hasOnlineBanking(ba.getHasOnlineBanking());
        } else if (account instanceof SavingsAccount sa) {
            builder.accountType("SAVINGS")
                    .interestRate(sa.getInterestRate())
                    .minimumBalance(sa.getMinimumBalance())
                    .withdrawalLimit(sa.getWithdrawalLimit())
                    .hasAutomaticTransfer(sa.getHasAutomaticTransfer())
                    .savingsGoal(sa.getSavingsGoal());
        } else if (account instanceof InvestmentAccount ia) {
            builder.accountType("INVESTMENT")
                    .riskLevel(ia.getRiskLevel())
                    .portfolioType(ia.getPortfolioType())
                    .managementFee(ia.getManagementFee())
                    .hasBrokerAccess(ia.getHasBrokerAccess())
                    .investmentStrategy(ia.getInvestmentStrategy())
                    .minimumInvestment(ia.getMinimumInvestment());
        } else {
            builder.accountType("BASE");
        }

        return builder.build();
    }

    public Account toEntity() {
        Account account;
        
        if ("BANK".equals(accountType)) {
            BankAccount ba = new BankAccount();
            ba.setBankName(this.bankName);
            ba.setIban(this.iban);
            ba.setSwift(this.swift);
            ba.setBranchCode(this.branchCode);
            ba.setHasDebitCard(this.hasDebitCard);
            ba.setHasOnlineBanking(this.hasOnlineBanking);
            account = ba;
        } else if ("SAVINGS".equals(accountType)) {
            SavingsAccount sa = new SavingsAccount();
            sa.setInterestRate(this.interestRate);
            sa.setMinimumBalance(this.minimumBalance);
            sa.setWithdrawalLimit(this.withdrawalLimit);
            sa.setHasAutomaticTransfer(this.hasAutomaticTransfer);
            sa.setSavingsGoal(this.savingsGoal);
            account = sa;
        } else if ("INVESTMENT".equals(accountType)) {
            InvestmentAccount ia = new InvestmentAccount();
            ia.setRiskLevel(this.riskLevel);
            ia.setPortfolioType(this.portfolioType);
            ia.setManagementFee(this.managementFee);
            ia.setHasBrokerAccess(this.hasBrokerAccess);
            ia.setInvestmentStrategy(this.investmentStrategy);
            ia.setMinimumInvestment(this.minimumInvestment);
            account = ia;
        } else {
            account = new Account();
        }

        account.setId(this.id);
        account.setAccountNumber(this.accountNumber);
        account.setAccountName(this.accountName);
        account.setBalance(this.balance);
        account.setOpenDate(this.openDate);
        account.setCurrency(this.currency);
        account.setIsActive(this.isActive);

        return account;
    }
}

package pl.edu.agh.dp.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.edu.agh.dp.entity.Account;
import pl.edu.agh.dp.entity.BankAccount;
import pl.edu.agh.dp.entity.SavingsAccount;
import pl.edu.agh.dp.entity.InvestmentAccount;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Bazowe DTO dla hierarchii Account (JOINED inheritance).
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "accountType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BankAccountDto.class, name = "BANK"),
        @JsonSubTypes.Type(value = SavingsAccountDto.class, name = "SAVINGS"),
        @JsonSubTypes.Type(value = InvestmentAccountDto.class, name = "INVESTMENT")
})
public class AccountDto {

    private Long id;
    private String accountNumber;
    private String accountName;
    private BigDecimal balance;
    private LocalDate openDate;
    private String currency;
    private Boolean isActive;

    /**
     * Konwertuje encję Account na odpowiednie DTO w hierarchii.
     */
    public static AccountDto fromEntity(Account account) {
        if (account == null) return null;

        if (account instanceof BankAccount ba) {
            return BankAccountDto.builder()
                    .id(ba.getId())
                    .accountNumber(ba.getAccountNumber())
                    .accountName(ba.getAccountName())
                    .balance(ba.getBalance())
                    .openDate(ba.getOpenDate())
                    .currency(ba.getCurrency())
                    .isActive(ba.getIsActive())
                    .bankName(ba.getBankName())
                    .iban(ba.getIban())
                    .swift(ba.getSwift())
                    .branchCode(ba.getBranchCode())
                    .hasDebitCard(ba.getHasDebitCard())
                    .hasOnlineBanking(ba.getHasOnlineBanking())
                    .build();
        } else if (account instanceof SavingsAccount sa) {
            return SavingsAccountDto.builder()
                    .id(sa.getId())
                    .accountNumber(sa.getAccountNumber())
                    .accountName(sa.getAccountName())
                    .balance(sa.getBalance())
                    .openDate(sa.getOpenDate())
                    .currency(sa.getCurrency())
                    .isActive(sa.getIsActive())
                    .interestRate(sa.getInterestRate())
                    .minimumBalance(sa.getMinimumBalance())
                    .withdrawalLimit(sa.getWithdrawalLimit())
                    .hasAutomaticTransfer(sa.getHasAutomaticTransfer())
                    .savingsGoal(sa.getSavingsGoal())
                    .build();
        } else if (account instanceof InvestmentAccount ia) {
            return InvestmentAccountDto.builder()
                    .id(ia.getId())
                    .accountNumber(ia.getAccountNumber())
                    .accountName(ia.getAccountName())
                    .balance(ia.getBalance())
                    .openDate(ia.getOpenDate())
                    .currency(ia.getCurrency())
                    .isActive(ia.getIsActive())
                    .riskLevel(ia.getRiskLevel())
                    .portfolioType(ia.getPortfolioType())
                    .managementFee(ia.getManagementFee())
                    .hasBrokerAccess(ia.getHasBrokerAccess())
                    .investmentStrategy(ia.getInvestmentStrategy())
                    .minimumInvestment(ia.getMinimumInvestment())
                    .build();
        }

        // Bazowy typ
        return AccountDto.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountName(account.getAccountName())
                .balance(account.getBalance())
                .openDate(account.getOpenDate())
                .currency(account.getCurrency())
                .isActive(account.getIsActive())
                .build();
    }

    /**
     * Konwertuje DTO na encję. Podklasy nadpisują tę metodę.
     */
    public Account toEntity() {
        Account account = new Account();
//        account.setId(this.id);
        account.setAccountNumber(this.accountNumber);
        account.setAccountName(this.accountName);
        account.setBalance(this.balance);
        account.setOpenDate(this.openDate);
        account.setCurrency(this.currency);
        account.setIsActive(this.isActive);
        return account;
    }

    /**
     * Wypełnia wspólne pola encji z DTO.
     */
    protected void fillCommonFields(Account account) {
//        account.setId(this.id);
        account.setAccountNumber(this.accountNumber);
        account.setAccountName(this.accountName);
        account.setBalance(this.balance);
        account.setOpenDate(this.openDate);
        account.setCurrency(this.currency);
        account.setIsActive(this.isActive);
    }

    /**
     * Zwraca typ konta (do serializacji JSON).
     */
    public String getAccountType() {
        if (this instanceof BankAccountDto) return "BANK";
        if (this instanceof SavingsAccountDto) return "SAVINGS";
        if (this instanceof InvestmentAccountDto) return "INVESTMENT";
        return "BASE";
    }
}

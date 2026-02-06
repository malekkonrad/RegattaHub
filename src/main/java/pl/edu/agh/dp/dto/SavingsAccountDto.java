package pl.edu.agh.dp.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.edu.agh.dp.entity.Account;
import pl.edu.agh.dp.entity.SavingsAccount;

import java.math.BigDecimal;

/**
 * DTO dla SavingsAccount.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class SavingsAccountDto extends AccountDto {

    private BigDecimal interestRate;
    private BigDecimal minimumBalance;
    private Integer withdrawalLimit;
    private Boolean hasAutomaticTransfer;
    private String savingsGoal;

    @Override
    public Account toEntity() {
        SavingsAccount entity = new SavingsAccount();
        fillCommonFields(entity);
        entity.setInterestRate(this.interestRate);
        entity.setMinimumBalance(this.minimumBalance);
        entity.setWithdrawalLimit(this.withdrawalLimit);
        entity.setHasAutomaticTransfer(this.hasAutomaticTransfer);
        entity.setSavingsGoal(this.savingsGoal);
        return entity;
    }

    /**
     * Tworzy DTO z encji SavingsAccount.
     */
    public static SavingsAccountDto fromEntity(SavingsAccount entity) {
        if (entity == null) return null;
        return SavingsAccountDto.builder()
                .id(entity.getId())
                .accountNumber(entity.getAccountNumber())
                .accountName(entity.getAccountName())
                .balance(entity.getBalance())
                .openDate(entity.getOpenDate())
                .currency(entity.getCurrency())
                .isActive(entity.getIsActive())
                .interestRate(entity.getInterestRate())
                .minimumBalance(entity.getMinimumBalance())
                .withdrawalLimit(entity.getWithdrawalLimit())
                .hasAutomaticTransfer(entity.getHasAutomaticTransfer())
                .savingsGoal(entity.getSavingsGoal())
                .build();
    }
}

package pl.edu.agh.dp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.edu.agh.dp.entity.Account;
import pl.edu.agh.dp.entity.BankAccount;

/**
 * DTO dla BankAccount.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BankAccountDto extends AccountDto {

    private String bankName;
    private String iban;
    private String swift;
    private String branchCode;
    private Boolean hasDebitCard;
    private Boolean hasOnlineBanking;

    @Override
    public Account toEntity() {
        BankAccount entity = new BankAccount();
        fillCommonFields(entity);
        entity.setBankName(this.bankName);
        entity.setIban(this.iban);
        entity.setSwift(this.swift);
        entity.setBranchCode(this.branchCode);
        entity.setHasDebitCard(this.hasDebitCard);
        entity.setHasOnlineBanking(this.hasOnlineBanking);
        return entity;
    }

    /**
     * Tworzy DTO z encji BankAccount.
     */
    public static BankAccountDto fromEntity(BankAccount entity) {
        if (entity == null) return null;
        return BankAccountDto.builder()
                .id(entity.getId())
                .accountNumber(entity.getAccountNumber())
                .accountName(entity.getAccountName())
                .balance(entity.getBalance())
                .openDate(entity.getOpenDate())
                .currency(entity.getCurrency())
                .isActive(entity.getIsActive())
                .bankName(entity.getBankName())
                .iban(entity.getIban())
                .swift(entity.getSwift())
                .branchCode(entity.getBranchCode())
                .hasDebitCard(entity.getHasDebitCard())
                .hasOnlineBanking(entity.getHasOnlineBanking())
                .build();
    }
}

package pl.edu.agh.dp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.edu.agh.dp.entity.Account;
import pl.edu.agh.dp.entity.InvestmentAccount;

import java.math.BigDecimal;

/**
 * DTO dla InvestmentAccount.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InvestmentAccountDto extends AccountDto {

    private String riskLevel;
    private String portfolioType;
    private BigDecimal managementFee;
    private Boolean hasBrokerAccess;
    private String investmentStrategy;
    private BigDecimal minimumInvestment;

    @Override
    public Account toEntity() {
        InvestmentAccount entity = new InvestmentAccount();
        fillCommonFields(entity);
        entity.setRiskLevel(this.riskLevel);
        entity.setPortfolioType(this.portfolioType);
        entity.setManagementFee(this.managementFee);
        entity.setHasBrokerAccess(this.hasBrokerAccess);
        entity.setInvestmentStrategy(this.investmentStrategy);
        entity.setMinimumInvestment(this.minimumInvestment);
        return entity;
    }

    /**
     * Tworzy DTO z encji InvestmentAccount.
     */
    public static InvestmentAccountDto fromEntity(InvestmentAccount entity) {
        if (entity == null) return null;
        return InvestmentAccountDto.builder()
                .id(entity.getId())
                .accountNumber(entity.getAccountNumber())
                .accountName(entity.getAccountName())
                .balance(entity.getBalance())
                .openDate(entity.getOpenDate())
                .currency(entity.getCurrency())
                .isActive(entity.getIsActive())
                .riskLevel(entity.getRiskLevel())
                .portfolioType(entity.getPortfolioType())
                .managementFee(entity.getManagementFee())
                .hasBrokerAccess(entity.getHasBrokerAccess())
                .investmentStrategy(entity.getInvestmentStrategy())
                .minimumInvestment(entity.getMinimumInvestment())
                .build();
    }
}

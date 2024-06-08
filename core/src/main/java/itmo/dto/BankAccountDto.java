package itmo.dto;

import itmo.entities.Money;
import itmo.enums.BankAccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BankAccountDto {
    private BankAccountType bankAccountType;
    private int clientId;
    private Money money;
}

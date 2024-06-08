package itmo.services.interfaces;

import itmo.dto.BankAccountDto;
import itmo.entities.Client;

public interface BankAccountServiceFabric {
    int registerBankAccount(Client client, BankAccountDto bankAccountDto);
}

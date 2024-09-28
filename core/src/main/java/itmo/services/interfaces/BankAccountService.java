package itmo.services.interfaces;

import itmo.entities.Money;

public interface BankAccountService {
    Money withdrawMoney(Money money);
    void topUpBankAccount(Money money);
}

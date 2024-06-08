package itmo.helpers;

import itmo.dto.BankAccountDto;
import itmo.entities.Client;
import itmo.entities.Money;

public abstract class BankAccountServiceHelper {
    protected BankAccountServiceHelper next;

    public void addNext(BankAccountServiceHelper next) {
        if (this.next == null) {
            this.next = next;
            return;
        }

        this.next.addNext(next);
    }

    public abstract int createBankAccount(Client client, BankAccountDto bankAccountDto);

    public abstract Money withdrawMoney(int bankAccountId, Money money);

    public void chargePercent() {}

    public abstract void topUpBankAccount(int bankAccountId, Money money);
}

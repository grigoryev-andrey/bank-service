package itmo.services.interfaces;

import itmo.dto.BankDto;
import itmo.entities.Bank;
import itmo.entities.Money;

import java.util.Date;

public interface CentralBankService {
    int registerBank(BankDto bankDto);
    void removeBank(int id);
    Bank getBank(int id);
    void transferMoney(int recipientBankAccount, int senderBankAccount, Money amount);
    void skipTime(int dayAmount);
    void rollbackTransaction(Date date);
}

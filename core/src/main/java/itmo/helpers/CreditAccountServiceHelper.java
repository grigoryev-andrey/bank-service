package itmo.helpers;

import itmo.dao.CreditAccountDao;
import itmo.dao.TransactionDao;
import itmo.dto.BankAccountDto;
import itmo.entities.Client;
import itmo.entities.CreditAccount;
import itmo.entities.Money;
import itmo.entities.Transaction;
import itmo.enums.OperationType;
import itmo.exceptions.NegativeValueException;
import itmo.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.Setter;

import java.util.Date;

public class CreditAccountServiceHelper extends BankAccountServiceHelper {
    private final CreditAccountDao creditAccountDao;
    private final TransactionDao transactionDao;
    @Setter
    private Money creditLimitAccount;

    public CreditAccountServiceHelper(CreditAccountDao creditAccountDao, TransactionDao transactionDao, Money creditLimitAccount) {
        this.creditAccountDao = creditAccountDao;
        this.transactionDao = transactionDao;
        this.creditLimitAccount = creditLimitAccount;
    }

    @Override
    public int createBankAccount(Client client, BankAccountDto bankAccountDto) {
        var creditAccount = new CreditAccount(bankAccountDto.getMoney(),
                client,
                client.getBank(),
                new Date(),
                creditLimitAccount);

        return creditAccountDao.add(creditAccount);
    }

    @Override
    @Transactional
    public Money withdrawMoney(int bankAccountId, Money money) {
        var bankAccount = creditAccountDao.get(bankAccountId);

        if (bankAccount == null) {
            throw new NotFoundException("Bank account with id %d not found");
        }

        if (money.inKopecks() < 0) {
            throw new NegativeValueException("You can't withdraw negative value");
        }

        var bankAccountMoney = bankAccount.getMoney();
        var kopecks = bankAccountMoney.inKopecks();

        var totalKopecks = 0;
        if (kopecks < 0) {
            totalKopecks = money.inKopecks() * bankAccount.getCommission() / 100;
        } else {
            totalKopecks = money.inKopecks();
        }

        bankAccountMoney.minus(new Money(0, totalKopecks));

        creditAccountDao.update(bankAccount);
        transactionDao.add(new Transaction(new Date(), money, bankAccount, OperationType.WITHDRAW));

        return money;
    }

    @Override
    public void topUpBankAccount(int bankAccountId, Money money) {
        var bankAccount = creditAccountDao.get(bankAccountId);

        if (bankAccount == null) {
            throw new NotFoundException("Bank account with id %d not found");
        }

        if (money.inKopecks() < 0) {
            throw new NegativeValueException("You can't top up negative value");
        }
        var bankAccountMoney = bankAccount.getMoney();
        var kopecks = bankAccountMoney.inKopecks();

        var totalKopecks = 0;
        if (kopecks < 0) {
            totalKopecks = money.inKopecks() * bankAccount.getCommission() / 100;
        } else {
            totalKopecks = money.inKopecks();
        }

        bankAccountMoney.plus(new Money(0, totalKopecks));

        creditAccountDao.update(bankAccount);
        transactionDao.add(new Transaction(new Date(), money, bankAccount, OperationType.TOP_UP));
    }
}

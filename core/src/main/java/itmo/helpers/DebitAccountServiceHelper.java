package itmo.helpers;

import itmo.dao.DebitAccountDao;
import itmo.dao.TransactionDao;
import itmo.dto.BankAccountDto;
import itmo.entities.*;
import itmo.enums.OperationType;
import itmo.exceptions.NegativeValueException;
import itmo.exceptions.NotFoundException;
import jakarta.transaction.Transactional;

import java.util.Date;

public class DebitAccountServiceHelper extends BankAccountServiceHelper {
    private final DebitAccountDao debitAccountDao;
    private final TransactionDao transactionDao;

    public DebitAccountServiceHelper(DebitAccountDao debitAccountDao, TransactionDao transactionDao) {
        this.debitAccountDao = debitAccountDao;
        this.transactionDao = transactionDao;
    }

    @Override
    public int createBankAccount(Client client, BankAccountDto bankAccountDto) {
        var debitBankAccount = new DebitAccount(bankAccountDto.getMoney(),
                client,
                client.getBank(),
                new Date());

        return debitAccountDao.add(debitBankAccount);
    }

    @Override
    @Transactional
    public Money withdrawMoney(int bankAccountId, Money money) {
        var bankAccount = debitAccountDao.get(bankAccountId);

        if (bankAccount == null) {
            throw new NotFoundException("Bank account with id %d not found");
        }

        if (money.inKopecks() < 0) {
            throw new NegativeValueException("You can't withdraw negative value");
        }

        if (bankAccount.getMoney().inKopecks() < money.inKopecks()) {
            throw new IllegalArgumentException("You can't withdraw more than you have");
        }

        bankAccount.getMoney().minus(money);
        debitAccountDao.update(bankAccount);
        transactionDao.add(new Transaction(new Date(), money, bankAccount, OperationType.WITHDRAW));

        return money;
    }

    @Override
    public void topUpBankAccount(int bankAccountId, Money money) {
        var bankAccount = debitAccountDao.get(bankAccountId);

        if (bankAccount == null) {
            throw new NotFoundException("Bank account with id %d not found");
        }

        if (money.inKopecks() < 0) {
            throw new NegativeValueException("You can't top up negative value");
        }

        bankAccount.getMoney().plus(money);
        debitAccountDao.update(bankAccount);
        transactionDao.add(new Transaction(new Date(), money, bankAccount, OperationType.TOP_UP));
    }
}

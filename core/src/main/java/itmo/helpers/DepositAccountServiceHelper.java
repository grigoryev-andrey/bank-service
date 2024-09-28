package itmo.helpers;

import itmo.dao.BankPercentDao;
import itmo.dao.DepositAccountDao;
import itmo.dao.TransactionDao;
import itmo.dto.BankAccountDto;
import itmo.entities.*;
import itmo.enums.OperationType;
import itmo.exceptions.NegativeValueException;
import itmo.exceptions.NotFoundException;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DepositAccountServiceHelper extends BankAccountServiceHelper {
    private final DepositAccountDao depositAccountDao;
    private final TransactionDao transactionDao;
    private final BankPercentDao bankPercentDao;
    @Setter
    private int depositDeltaTime;

    public DepositAccountServiceHelper(DepositAccountDao depositAccountDao,
                                       TransactionDao transactionDao,
                                       BankPercentDao bankPercentDao,
                                       int depositDeltaTime) {
        this.depositAccountDao = depositAccountDao;
        this.transactionDao = transactionDao;
        this.bankPercentDao = bankPercentDao;
        this.depositDeltaTime = depositDeltaTime;
    }

    @Override
    public int createBankAccount(Client client, BankAccountDto bankAccountDto) {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        calendar.add(Calendar.DAY_OF_MONTH, depositDeltaTime);

        var depositAccount = new DepositAccount(bankAccountDto.getMoney(),
                client,
                client.getBank(),
                currentDate,
                calendar.getTime());

        return depositAccountDao.add(depositAccount);
    }

    @Override
    public Money withdrawMoney(int bankAccountId, Money money) {
        var bankAccount = depositAccountDao.get(bankAccountId);

        if (bankAccount == null) {
            throw new NotFoundException("Bank account with id %d not found");
        }

        if (money.inKopecks() < 0) {
            throw new NegativeValueException("You can't withdraw negative value");
        }

        if (bankAccount.getMoney().inKopecks() < money.inKopecks()) {
            throw new IllegalArgumentException("You can't withdraw more than you have");
        }

        if (bankAccount.getDateOfClosing().before(new Date())) {
            bankAccount.getMoney().minus(money);
            depositAccountDao.update(bankAccount);
            transactionDao.add(new Transaction(new Date(), money, bankAccount, OperationType.WITHDRAW));
        }

        return money;
    }

    @Override
    public void topUpBankAccount(int bankAccountId, Money money) {
        var bankAccount = depositAccountDao.get(bankAccountId);

        if (bankAccount == null) {
            throw new NotFoundException("Bank account with id %d not found");
        }

        if (money.inKopecks() < 0) {
            throw new NegativeValueException("You can't withdraw negative value");
        }

        bankAccount.getMoney().plus(money);
        depositAccountDao.update(bankAccount);
        transactionDao.add(new Transaction(new Date(), money, bankAccount, OperationType.TOP_UP));
    }

    @Override
    public void chargePercent() {
        var depositAccounts = depositAccountDao.getAll();

        for (var depositAccount : depositAccounts) {
            List<BankPercent> bankPercents = bankPercentDao.getBankPercents(depositAccount.getBank().getId());

            bankPercents.sort((o1, o2) -> Integer.compare(o2.getMoney().inKopecks(), o1.getMoney().inKopecks()));

            int i = 0;
            while (depositAccount.getStartMoney().inKopecks() < bankPercents.get(i).getMoney().inKopecks()) {
                i++;
            }

            var additionalValueInKopecks = depositAccount.getMoney().inKopecks() * (bankPercents.get(i).getPercent() / 100);
            var depositMoneyInKopecks = depositAccount.getMoney().inKopecks() + additionalValueInKopecks;

            depositAccount.setMoney(new Money(0, depositMoneyInKopecks));

            depositAccountDao.update(depositAccount);
            transactionDao.add(new Transaction(new Date(), new Money(0, additionalValueInKopecks), depositAccount, OperationType.CHARGE_PERCENT));
        }
    }
}

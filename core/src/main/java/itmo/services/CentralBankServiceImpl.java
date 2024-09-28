package itmo.services;

import itmo.dao.BankDao;
import itmo.dao.TransactionDao;
import itmo.dto.BankDto;
import itmo.entities.Bank;
import itmo.entities.Money;
import itmo.enums.OperationType;
import itmo.helpers.BankAccountServiceHelper;
import itmo.services.interfaces.CentralBankService;
import itmo.validators.BankValidator;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class CentralBankServiceImpl implements CentralBankService {
    private final BankDao bankDao;
    private final BankValidator bankValidator;
    private final BankAccountServiceHelper bankAccountServiceHelper;
    private final TransactionDao transactionDao;

    public CentralBankServiceImpl(BankDao bankDao,
                                  BankValidator bankValidator,
                                  BankAccountServiceHelper bankAccountServiceHelper,
                                  TransactionDao transactionDao) {
        this.bankDao = bankDao;
        this.bankValidator = bankValidator;
        this.bankAccountServiceHelper = bankAccountServiceHelper;
        this.transactionDao = transactionDao;
    }

    @Override
    public int registerBank(BankDto bankDto) {
        var bank = new Bank(bankDto);
        bankValidator.validate(bank);
        return bankDao.add(bank);
    }

    @Override
    public void removeBank(int id) {
        bankDao.delete(id);
    }

    @Override
    public Bank getBank(int id) {
        return bankDao.get(id);
    }

    @Override
    @Transactional
    public void transferMoney(int recipientBankAccount, int senderBankAccount, Money amount) {
        bankAccountServiceHelper.withdrawMoney(recipientBankAccount, amount);
        bankAccountServiceHelper.topUpBankAccount(senderBankAccount, amount);
    }

    @Override
    public void skipTime(int dayAmount) {
        var currentDate = LocalDate.now();
        var newDate = currentDate.plusDays(dayAmount);
        long monthsBetween = ChronoUnit.MONTHS.between(currentDate, newDate);
        while (monthsBetween > 0){
            bankAccountServiceHelper.chargePercent();
            monthsBetween--;
        }
    }

    @Override
    public void rollbackTransaction(Date date) {
        var transactions = transactionDao.getTransactionBefore(date);

        for (var transaction : transactions) {
            var bankAccount = transaction.getBankAccount();

            if (transaction.getOperationType() == OperationType.WITHDRAW) {
                bankAccount.setMoney(bankAccount.getMoney().plus(transaction.getAmount()));
            } else {
                bankAccount.setMoney(bankAccount.getMoney().minus(transaction.getAmount()));
            }
        }
    }
}

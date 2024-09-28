package itmo;

import itmo.controllers.BankController;
import itmo.controllers.CentralBankController;
import itmo.dao.*;
import itmo.entities.Money;
import itmo.helpers.CreditAccountServiceHelper;
import itmo.helpers.DebitAccountServiceHelper;
import itmo.helpers.DepositAccountServiceHelper;
import itmo.services.BankServiceImpl;
import itmo.services.CentralBankServiceImpl;
import itmo.validators.BankValidator;
import itmo.validators.ClientValidator;
import picocli.CommandLine;

@CommandLine.Command(name = "app", description = "Bank Service", mixinStandardHelpOptions = true)
public class Main {
    public static void main(String[] args) {
        CommandLine commandLine = new CommandLine(new Main());

        var creditAccountDao = new CreditAccountDaoImpl();
        var debitAccountDao = new DebitAccountDaoImpl();
        var depositAccountDao = new DepositAccountDaoImpl();

        var transactionDao = new TransactionDaoImpl();
        var bankPercentDao = new BankPercentDaoImpl();

        var bankAccountServiceHelper = new CreditAccountServiceHelper(creditAccountDao, transactionDao, new Money());
        bankAccountServiceHelper.addNext(new DebitAccountServiceHelper(debitAccountDao, transactionDao));
        bankAccountServiceHelper.addNext(new DepositAccountServiceHelper(depositAccountDao, transactionDao, bankPercentDao, 30));

        var clientValidator = new ClientValidator();
        var clientDao = new ClientDaoImpl();
        var bankDao = new BankDaoImpl();
        var messageDao = new MessageDaoImpl();
        var bankService = new BankServiceImpl(bankDao,
                clientDao,
                bankAccountServiceHelper,
                clientValidator,
                messageDao,
                bankPercentDao);
        var bankController = new BankController(bankService);
        commandLine.addSubcommand(bankController);

        var bankValidator = new BankValidator();
        var centralBankService = new CentralBankServiceImpl(bankDao, bankValidator, bankAccountServiceHelper, transactionDao);
        var centralBankController = new CentralBankController(centralBankService);
        commandLine.addSubcommand(centralBankController);

        commandLine.execute(args);
    }
}


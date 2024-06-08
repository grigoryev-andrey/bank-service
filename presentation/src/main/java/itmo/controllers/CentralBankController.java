package itmo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.converters.MoneyConverter;
import itmo.dto.BankDto;
import itmo.entities.Money;
import itmo.services.interfaces.CentralBankService;
import picocli.CommandLine;

import java.util.Date;

@CommandLine.Command(name = "central-bank", mixinStandardHelpOptions = true)
public class CentralBankController {
    private final CentralBankService centralBankService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CentralBankController(CentralBankService centralBankService) {
        this.centralBankService = centralBankService;
    }

    @CommandLine.Command(name = "register-bank", description = "Add a new bank")
    public void registerBank(@CommandLine.Parameters(index = "0", description = "bank name")String name,
                             @CommandLine.Parameters(index = "1", description = "bank commission") int bank_commission) {
        try {
            var bankDto = new BankDto(name, bank_commission);
            var bankId = centralBankService.registerBank(bankDto);
            System.out.printf("Bank with id %d was added", bankId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @CommandLine.Command(name = "transfer", description = "Transfer money")
    public void transferMoney(@CommandLine.Option(names = "-s", description = "sender bank account id") int senderId,
                              @CommandLine.Option(names = "-r", description = "recipient bank account id") int recipientId,
                              @CommandLine.Option(names = "-a", description = "amount in the format amount,kopecks", required = true, converter = MoneyConverter.class) Money amount) {
        try {
            centralBankService.transferMoney(senderId, recipientId, amount);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @CommandLine.Command(name = "get-bank", description = "get a bank")
    public void getBank(@CommandLine.Parameters(index = "0", description = "id") int id) {
        try {
            var bank = centralBankService.getBank(id);
            System.out.println(objectMapper.writeValueAsString(bank));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @CommandLine.Command(name = "remove-bank", description = "remove a bank")
    public void removeBank(@CommandLine.Parameters(index = "0", description = "id") int id) {
        try {
            centralBankService.removeBank(id);
            System.out.printf("bank with id %d was deleted", id);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @CommandLine.Command(name = "skip", description = "skip n days")
    public void skip(@CommandLine.Parameters(index = "0", description = "n") int n) {
        try {
            centralBankService.skipTime(n);
            System.out.println("skip %n days");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @CommandLine.Command(name = "rollback", description = "skip n days")
    public void rollback(@CommandLine.Parameters(index = "0", description = "n") Date date) {
        try {
            centralBankService.rollbackTransaction(date);
            System.out.println("rollback until " + date.toString());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

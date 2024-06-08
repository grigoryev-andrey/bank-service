package itmo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.dto.BankAccountDto;
import itmo.dto.ClientDto;
import itmo.dto.UpdatedClientDto;
import itmo.entities.Money;
import itmo.entities.Passport;
import itmo.enums.BankAccountType;
import itmo.services.interfaces.BankService;
import picocli.CommandLine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

@CommandLine.Command(name = "bank", mixinStandardHelpOptions = true)
public class BankController {
    private final BankService bankService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private final Scanner scanner;

    public BankController(BankService bankService) {
        this.bankService = bankService;
        this.scanner = new Scanner(System.in);
    }

    @CommandLine.Command(name = "register-client", description = "register a new client")
    public void registerClient(@CommandLine.Parameters(index = "0", description = "bank name") String bankName) {
        var clientDtoBuilder = ClientDto.clientBuilder();

        try {
            clientDtoBuilder.bank(bankName);

            System.out.println("Enter name:");
            var name = scanner.nextLine();
            clientDtoBuilder.name(name);

            System.out.println("Enter birthday:");
            var birthday = formatter.parse(scanner.nextLine());
            clientDtoBuilder.birthday(birthday);

            System.out.println("Enter address (Optional):");
            var address = scanner.nextLine();
            clientDtoBuilder.address(address);

            System.out.println("Enter series of passport (Optional):");
            var series = scanner.nextLine();
            System.out.println("Enter number of passport (Optional):");
            var number = scanner.nextLine();

            var passport = new Passport(series, number);
            clientDtoBuilder.passport(passport);

            var clientDto = clientDtoBuilder.build();

            var clientId = bankService.registerClient(clientDto);
            System.out.printf("Client with id %d was added", clientId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @CommandLine.Command(name = "get-client", description = "Get a bank")
    public void getClient(@CommandLine.Parameters(index = "0", description = "id") int id) {
        try {
            var bank = bankService.getClient(id);
            System.out.println(objectMapper.writeValueAsString(bank));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @CommandLine.Command(name = "subscribe-to-changes", description = "Subscribe to changes")
    public void subscribeToChanges(@CommandLine.Parameters(index = "0", description = "bank name") String bankName,
                                   @CommandLine.Parameters(index = "1", description = "client id") int clientId) {
        try {
            bankService.subscribeToChanges(clientId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @CommandLine.Command(name = "remove-client", description = "Remove a client")
    public void removeClient(@CommandLine.Parameters(index = "0", description = "client id") int clientId) {
        try {
            bankService.removeClient(clientId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @CommandLine.Command(name = "update-client", description = "Update a client")
    public void updateClient(@CommandLine.Parameters(index = "0", description = "client id") int clientId,
                             @CommandLine.Option(names = "-n", description = "name") String name,
                             @CommandLine.Option(names = "-b", description = "birthday") Date birthday,
                             @CommandLine.Option(names = "-a", description = "address") String address,
                             @CommandLine.Option(names = "-p", description = "passport") Passport passport) {
        try {
            var clientDto = new UpdatedClientDto(name, birthday, address, passport);
            bankService.updateClient(clientId, clientDto);

            System.out.printf("Client with id %d was updated", clientId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @CommandLine.Command(name = "register-bank-account", description = "Register a bank account")
    public void registerBankAccount(@CommandLine.Option(names = "-t", defaultValue = "DEBIT",
            description = "bank account type") String bankAccountTypeString,
                                    @CommandLine.Option(names = "-c", description = "client id") int clientId,
                                    @CommandLine.Option(names = "-m", description = "money") Money money) {
        try {
            var bankAccountType = BankAccountType.valueOf(bankAccountTypeString);

            var bankAccountDto = new BankAccountDto(bankAccountType, clientId, money);
            var bankAccountId = bankService.registerBankAccount(bankAccountDto);

            System.out.printf("Bank account with id %d and type" + bankAccountType, bankAccountId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @CommandLine.Command(name = "subscribe-to-change", description = "subscribe to change")
    public void subscribeToChange(@CommandLine.Parameters(index = "0", description = "index") int clientId) {
        try {
            bankService.subscribeToChanges(clientId);
            System.out.printf("Client with id %d became subscriber", clientId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @CommandLine.Command(name = "change-percent", description = "change-limit")
    public void changeBankPercent(@CommandLine.Option(names = "-b", description = "bank id") int bankId,
                                  @CommandLine.Option(names = "-m", description = "money") Money money,
                                  @CommandLine.Option(names = "-p", description = "percent") int percent) {
        bankService.changeBankPercent(bankId, percent, money);
    }

    @CommandLine.Command(name = "change-commission", description = "change-limit")
    public void changeCommission(@CommandLine.Option(names = "-b", description = "bank id") int bankId,
                                 @CommandLine.Option(names = "-c", description = "percent") int commission) {
        bankService.changeBankCommission(bankId, commission);
    }
}

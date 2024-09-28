package itmo.services;

import itmo.dao.*;
import itmo.dto.*;
import itmo.entities.*;
import itmo.exceptions.NotFoundException;
import itmo.helpers.BankAccountServiceHelper;
import itmo.services.interfaces.BankService;
import itmo.validators.ClientValidator;
import jakarta.transaction.Transactional;

public class BankServiceImpl implements BankService {
    private final BankDao bankDao;
    private final ClientDao clientDao;
    private final BankAccountServiceHelper bankAccountServiceHelper;
    private final ClientValidator clientValidator;
    private final MessageDao messageDao;
    private final BankPercentDao bankPercentDao;

    public BankServiceImpl(BankDao bankDao,
                           ClientDao clientDao,
                           BankAccountServiceHelper bankAccountServiceHelper,
                           ClientValidator clientValidator,
                           MessageDao messageDao,
                           BankPercentDao bankPercentDao) {
        this.bankDao = bankDao;
        this.clientDao = clientDao;
        this.bankAccountServiceHelper = bankAccountServiceHelper;
        this.clientValidator = clientValidator;
        this.messageDao = messageDao;
        this.bankPercentDao = bankPercentDao;
    }


    @Override
    public int registerBankAccount(BankAccountDto bankAccountDto) {
        var client = clientDao.get(bankAccountDto.getClientId());

        return bankAccountServiceHelper.createBankAccount(client, bankAccountDto);
    }

    @Override
    @Transactional
    public int registerClient(ClientDto clientDto) {
        var bank = getBank(clientDto.getBank());

        var client = new Client(clientDto.getName(),
                clientDto.getBirthday(),
                clientDto.getAddress(),
                clientDto.getPassport(),
                bank);

        clientValidator.validate(client);

        return clientDao.add(client);
    }

    @Override
    public Client getClient(int id) {
        return clientDao.get(id);
    }

    @Override
    public void removeClient(int id) {
        clientDao.delete(id);
    }

    @Override
    @Transactional
    public void updateClient(int id, UpdatedClientDto clientDto) {
        var client = clientDao.get(id);

        if (clientDto.getName() != null) {
            client.setName(clientDto.getName());
        }

        if (clientDto.getBirthday() != null) {
            client.setBirthday(clientDto.getBirthday());
        }

        if (clientDto.getPassport() != null && !clientDto.getPassport().isVerified()) {
            client.setPassport(clientDto.getPassport());
        }

        if (clientDto.getAddress() != null) {
            client.setAddress(clientDto.getAddress());
        }

        clientDao.update(client);
    }

    @Override
    public void subscribeToChanges(int clientId) {
        var client = clientDao.get(clientId);

        if (client == null) {
            throw new NotFoundException("Client with id %d not found");
        }

        client.setSubscribedToNotification(true);
        clientDao.update(client);
    }

    @Override
    public void changeBankCommission(int bankId, int commission) {
        var bank = getBank(bankId);
        bank.setBankCommission(commission);

        bankDao.update(bank);
    }

    @Override
    @Transactional
    public void changeBankPercent(int bankId, int percent, Money money) {
        var bank = getBank(bankId);
        var bankPercent = bank.getBankPercent().stream()
                .filter(bc -> bc.getPercent() == percent)
                .findFirst();

        if (bankPercent.isEmpty()) {
            var newBankPercent = new BankPercent(bank, money, percent);
            bank.getBankPercent().add(newBankPercent);
            bankDao.update(bank);

            bankPercentDao.add(newBankPercent);

            return;
        }

        bankPercent.get().setMoney(money);
        bankPercentDao.update(bankPercent.get());
        sendNotification();
    }

    private void sendNotification() {
        var subscribers = clientDao.getAllSubscribers();

        for (var subscriber : subscribers) {
            messageDao.add(new Message(subscriber, "bank commission was changed"));
        }
    }

    private Bank getBank(String name) {
        var bank = bankDao.getByName(name);

        if (bank == null) {
            throw new NotFoundException("Bank with id % not found");
        }

        return bank;
    }

    private Bank getBank(int bankId) {
        var bank = bankDao.get(bankId);

        if (bank == null) {
            throw new NotFoundException("Bank with id % not found");
        }

        return bank;
    }
}

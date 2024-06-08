package itmo.services.interfaces;

import itmo.dto.*;
import itmo.entities.*;

public interface BankService {
    int registerBankAccount(BankAccountDto bankAccountDto);
    int registerClient(ClientDto clientDto);
    Client getClient(int id);
    void removeClient(int id);
    void updateClient(int id, UpdatedClientDto clientDto);
    void subscribeToChanges(int clientId);
    void changeBankCommission(int bankId, int commission);
    void changeBankPercent(int bankId, int percent, Money money);
}

package itmo.dao;

import itmo.entities.Transaction;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface TransactionDao extends CrudDao<UUID, Transaction> {
    List<Transaction> getTransactionBefore(Date date);
}

package itmo.dao;

import itmo.entities.Bank;

public interface BankDao extends CrudDao<Integer, Bank> {
    Bank getByName(String name);
}

package itmo.dao;

import itmo.entities.BankPercent;

import java.util.List;

public interface BankPercentDao {
    List<BankPercent> getBankPercents(int bankId);
    void add(BankPercent bankPercent);
    void update(BankPercent bankPercent);
}

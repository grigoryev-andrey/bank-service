package itmo.dao;

import itmo.entities.Bank;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class BankDaoImpl implements BankDao {
    private final SessionFactory sessionFactory;

    public BankDaoImpl() {
        var configuration = new Configuration().configure();
        configuration.addAnnotatedClass(Bank.class);
        sessionFactory = configuration.buildSessionFactory();
    }

    @Override
    public Integer add(Bank bank) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            session.persist(bank);
            transaction.commit();
        }

        return bank.getId();
    }

    @Override
    public void update(Bank bank) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            session.merge(bank);
            transaction.commit();
        }
    }

    @Override
    public void delete(Integer id) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            var bank = session.get(Bank.class, id);
            session.remove(bank);
            transaction.commit();
        }
    }

    @Override
    public Bank get(Integer id) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            var bank = session.get(Bank.class, id);
            transaction.commit();
            return bank;
        }
    }

    @Override
    public Bank getByName(String name) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            String hql = "FROM Bank WHERE name = :name";
            Query<Bank> query = session.createQuery(hql, Bank.class);
            query.setParameter("name", name);
            Bank bank = query.uniqueResult();

            transaction.commit();
            return bank;
        }
    }

    @Override
    public List<Bank> getAll() {
        try(var session = sessionFactory.openSession()) {
            var query = session.createQuery("from Bank ", Bank.class);
            return query.getResultList();
        }
    }
}

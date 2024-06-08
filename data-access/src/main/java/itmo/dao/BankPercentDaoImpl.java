package itmo.dao;

import itmo.entities.BankPercent;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class BankPercentDaoImpl implements BankPercentDao {
    private final SessionFactory sessionFactory;

    public BankPercentDaoImpl() {
        var configuration = new Configuration().configure();
        configuration.addAnnotatedClass(BankPercent.class);
        sessionFactory = configuration.buildSessionFactory();
    }
    @Override
    public List<BankPercent> getBankPercents(int bankId) {
        try (var session = sessionFactory.openSession()) {
            var query = session.createQuery("from BankPercent where bank.id = :bankId", BankPercent.class);
            query.setParameter("bankId", bankId);
            return query.getResultList();
        }
    }

    @Override
    public void add(BankPercent bankPercent) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            session.persist(bankPercent);
            transaction.commit();
        }
    }

    @Override
    public void update(BankPercent bankPercent) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            session.merge(bankPercent);
            transaction.commit();
        }
    }
}

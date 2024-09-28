package itmo.dao;

import itmo.entities.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TransactionDaoImpl implements TransactionDao {
    private final SessionFactory sessionFactory;
    public TransactionDaoImpl() {
        var configuration = new Configuration().configure();
        configuration.addAnnotatedClass(Transaction.class);
        sessionFactory = configuration.buildSessionFactory();
    }

    @Override
    public UUID add(Transaction transactionBase) {
        try(var session = sessionFactory.openSession()) {
            var sqlTransaction = session.beginTransaction();
            session.persist(transactionBase);
            sqlTransaction.commit();
        }

        return transactionBase.getId();
    }

    @Override
    public void update(Transaction transactionBase) {
        try(var session = sessionFactory.openSession()) {
            var sqlTransaction = session.beginTransaction();

            session.merge(transactionBase);
            sqlTransaction.commit();
        }
    }

    @Override
    public void delete(UUID id) {
        try(var session = sessionFactory.openSession()) {
            var sqlTransaction = session.beginTransaction();

            var creditAccount = session.get(Transaction.class, id);
            session.remove(creditAccount);

            sqlTransaction.commit();
        }
    }

    @Override
    public Transaction get(UUID id) {
        try(var session = sessionFactory.openSession()) {
            var sqlTransaction = session.beginTransaction();
            var transaction = session.get(Transaction.class, id);
            sqlTransaction.commit();
            return transaction;
        }
    }

    @Override
    public List<Transaction> getAll() {
        try(var session = sessionFactory.openSession()) {
            var query = session.createQuery("from Transaction ", Transaction.class);
            return query.getResultList();
        }
    }

    @Override
    public List<Transaction> getTransactionBefore(Date date) {
        try (var session = sessionFactory.openSession()) {
            var query = session.createQuery("from Transaction t where t.datetime < :date", Transaction.class);
            query.setParameter("date", date);
            return query.getResultList();
        }
    }
}

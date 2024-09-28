package itmo.dao;

import itmo.entities.DepositAccount;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class DepositAccountDaoImpl implements DepositAccountDao {
    private final SessionFactory sessionFactory;
    public DepositAccountDaoImpl() {
        var configuration = new Configuration().configure();
        configuration.addAnnotatedClass(DepositAccount.class);
        sessionFactory = configuration.buildSessionFactory();
    }

    @Override
    public Integer add(DepositAccount depositAccount) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            session.persist(depositAccount);
            transaction.commit();
        }

        return depositAccount.getId();
    }

    @Override
    public void update(DepositAccount depositAccount) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            session.merge(depositAccount);
            transaction.commit();
        }
    }

    @Override
    public void delete(Integer id) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            var creditAccount = session.get(DepositAccount.class, id);
            session.remove(creditAccount);

            transaction.commit();
        }
    }

    @Override
    public DepositAccount get(Integer id) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            var depositAccount = session.get(DepositAccount.class, id);
            transaction.commit();
            return depositAccount;
        }
    }

    @Override
    public List<DepositAccount> getAll() {
        try(var session = sessionFactory.openSession()) {
            var query = session.createQuery("from DepositAccount ", DepositAccount.class);
            return query.getResultList();
        }
    }
}

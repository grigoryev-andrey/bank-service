package itmo.dao;

import itmo.entities.CreditAccount;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class CreditAccountDaoImpl implements CreditAccountDao {
    private final SessionFactory sessionFactory;
    public CreditAccountDaoImpl() {
        var configuration = new Configuration().configure();
        configuration.addAnnotatedClass(CreditAccount.class);
        sessionFactory = configuration.buildSessionFactory();
    }

    @Override
    public Integer add(CreditAccount creditAccount) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            session.persist(creditAccount);
            transaction.commit();
        }

        return creditAccount.getId();
    }

    @Override
    public void update(CreditAccount creditAccount) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            // TODO: update

            session.merge(creditAccount);
            transaction.commit();
        }
    }

    @Override
    public void delete(Integer id) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            var creditAccount = session.get(CreditAccount.class, id);
            session.remove(creditAccount);

            transaction.commit();
        }
    }

    @Override
    public CreditAccount get(Integer id) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            var creditAccount = session.get(CreditAccount.class, id);
            transaction.commit();
            return creditAccount;
        }
    }

    @Override
    public List<CreditAccount> getAll() {
        try(var session = sessionFactory.openSession()) {
            var query = session.createQuery("from CreditAccount ", CreditAccount.class);
            return query.getResultList();
        }
    }
}

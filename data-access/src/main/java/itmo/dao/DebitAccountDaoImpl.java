package itmo.dao;

import itmo.entities.DebitAccount;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class DebitAccountDaoImpl implements DebitAccountDao {
    private final SessionFactory sessionFactory;
    public DebitAccountDaoImpl() {
        var configuration = new Configuration().configure();
        configuration.addAnnotatedClass(DebitAccountDaoImpl.class);
        sessionFactory = configuration.buildSessionFactory();
    }

    @Override
    public Integer add(DebitAccount debitAccount) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            session.persist(debitAccount);
            transaction.commit();
        }

        return debitAccount.getId();
    }

    @Override
    public void update(DebitAccount debitAccount) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            // TODO: update

            session.merge(debitAccount);
            transaction.commit();
        }
    }

    @Override
    public void delete(Integer id) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            var creditAccount = session.get(DebitAccount.class, id);
            session.remove(creditAccount);

            transaction.commit();
        }
    }

    @Override
    public DebitAccount get(Integer id) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            var debitAccount = session.get(DebitAccount.class, id);
            transaction.commit();
            return debitAccount;
        }
    }

    @Override
    public List<DebitAccount> getAll() {
        try(var session = sessionFactory.openSession()) {
            var query = session.createQuery("from DebitAccount ", DebitAccount.class);
            return query.getResultList();
        }
    }
}

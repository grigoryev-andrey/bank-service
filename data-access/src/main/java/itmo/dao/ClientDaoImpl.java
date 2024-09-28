package itmo.dao;

import itmo.entities.Bank;
import itmo.entities.Client;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class ClientDaoImpl implements ClientDao {
    private final SessionFactory sessionFactory;
    public ClientDaoImpl() {
        var configuration = new Configuration().configure();
        configuration.addAnnotatedClass(Bank.class);
        sessionFactory = configuration.buildSessionFactory();
    }

    @Override
    public Integer add(Client client) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            session.persist(client);
            transaction.commit();
        }

        return client.getId();
    }

    @Override
    public void update(Client client) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            session.merge(client);
            transaction.commit();
        }
    }

    @Override
    public void delete(Integer id) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();

            var client = session.get(Client.class, id);
            session.remove(client);

            transaction.commit();
        }
    }

    @Override
    public Client get(Integer id) {
        try(var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            var client = session.get(Client.class, id);
            transaction.commit();
            return client;
        }
    }

    @Override
    public List<Client> getAll() {
        try(var session = sessionFactory.openSession()) {
            var query = session.createQuery("from Client ", Client.class);
            return query.getResultList();
        }
    }

    @Override
    public List<Client> getAllSubscribers() {
        try (var session = sessionFactory.openSession()) {
            var query = session.createQuery("from Client where isSubscribedToNotification = true", Client.class);
            return query.getResultList();
        }
    }
}

package itmo.dao;

import itmo.entities.Client;

import java.util.List;

public interface ClientDao extends CrudDao<Integer, Client> {
    List<Client> getAllSubscribers();
}

package itmo.dao;

import java.util.List;

public interface CrudDao<TId, TValue> {
    TId add(TValue tValue);
    void update(TValue tValue);
    void delete(TId tId);
    TValue get(TId tId);

    List<TValue> getAll();
}

package dao;

import exception.EntityAlreadyExistsException;
import exception.EntityNotFoundException;
import model.Identifiable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface Repository<K, V extends Identifiable<K>> {
    List<V> findAll();
    Optional<V> findById(K id);
    V create(V entity) throws EntityAlreadyExistsException;
    int createFromMemory (Collection<V> entity) throws EntityAlreadyExistsException;
    V update(V entity) throws EntityNotFoundException;
    V deleteById(K id) throws EntityNotFoundException;
}

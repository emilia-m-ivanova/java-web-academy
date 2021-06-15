package dao.impl;

import dao.PlayerRepository;
import exception.EntityAlreadyExistsException;
import exception.EntityNotFoundException;
import model.Player;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class PlayerRepositoryJpaImpl implements PlayerRepository {

    EntityManagerFactory emf;
    EntityManager em;

    public PlayerRepositoryJpaImpl() {
    }

    public void init() {
        emf = Persistence.createEntityManagerFactory("hiperquiz");
        em = emf.createEntityManager();
    }

    @Override
    public Player findByUsernameAndPassword(String username, String password) {
        return this.findAll().stream()
                .filter(e -> e.getUsername().equals(username) && e.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Player> findAll() {
        return em.createQuery("SELECT p FROM Player AS p").getResultList();
    }

    @Override
    public Optional<Player> findById(Long id) {
        return Optional.of(em.find(Player.class, id));
    }

    @Override
    public Player create(Player player) throws EntityAlreadyExistsException {
        EntityTransaction entityTransaction = em.getTransaction();
        entityTransaction.begin();
        em.persist(player);
        entityTransaction.commit();
        return player;
    }

    @Override
    public int createFromMemory(Collection<Player> players) throws EntityAlreadyExistsException {
        List<Player> results = new ArrayList<>();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            for (Player p : players) {
                em.persist(p);
                results.add(p);
            }
            transaction.commit();
        } catch (PersistenceException e) {
            transaction.rollback();
            throw new EntityAlreadyExistsException("Error creating players", e);
        }
        return results.size();
    }

    @Override
    public Player update(Player player) throws EntityNotFoundException {
        try {
            EntityTransaction entityTransaction = em.getTransaction();
            entityTransaction.begin();
            Player updatedPlayer = em.merge(player);
            entityTransaction.commit();
            return updatedPlayer;
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Error updating player" + player);
        }
    }

    @Override
    public Player deleteById(Long id) throws EntityNotFoundException {
        try {
            Player player = findById(id).orElseThrow();
            EntityTransaction entityTransaction = em.getTransaction();
            entityTransaction.begin();
            em.remove(player);
            entityTransaction.commit();
            return player;
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Error deleting player with id " + id);
        }
    }

    @Override
    public void updateKeyGenerator(Long playersCount) {

    }
}

package dao.impl;

import dao.KeyGenerator;
import dao.PlayerRepository;
import model.Player;

public class PlayerRepositoryImpl extends RepositoryMemoryImpl<Long, Player> implements PlayerRepository {
    public PlayerRepositoryImpl(KeyGenerator<Long> keyGenerator) {
        super(keyGenerator);
    }
    public Player findByUsernameAndPassword(String username, String password){
        return this.findAll().stream()
                .filter(e->e.getUsername().equals(username) && e.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
}

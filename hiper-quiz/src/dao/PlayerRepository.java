package dao;

import model.Player;

public interface PlayerRepository extends Repository<Long, Player>{
    Player findByUsernameAndPassword(String username, String password);
}

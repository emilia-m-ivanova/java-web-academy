package dao.impl;

import dao.KeyGenerator;
import exception.EntityAlreadyExistsException;
import exception.EntityNotFoundException;
import model.Player;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static model.Gender.*;

public class PlayerRepositoryImplTest {
    private static List<Player> PLAYERS;
    private static final Player NEW_USER = new Player("New player","new.player@email.com", "new-player-password",MALE);
    private PlayerRepositoryImpl playerRepository;
    private KeyGenerator<Long> keyGenerator;

    @Before
    public void setUp() {
        PLAYERS = List.of(
                new Player("Emilia","emilia@gmail.com","pass",FEMALE),
                new Player("Valentin","valentin@gmail.com","valio",MALE),
                new Player("Elena","elena@gmail.com","elena123",FEMALE),
                new Player("Velichka","velichka@gmail.com","vili45",FEMALE)
        );
        keyGenerator = new LongKeyGenerator();
        playerRepository = new PlayerRepositoryImpl(keyGenerator);
    }

    @Test
    public void findAllWithEmptyRepo() {
        List<Player> playerList = playerRepository.findAll();
        assertEquals("Find function should return an empty collection for an empty repo",0,playerList.size());
    }

    @Test
    public void findAllWithFilledInRepo() {
        this.addPlayers();
        List<Player> playerList = playerRepository.findAll();
        assertEquals("Find function should return a collection with the size of the number of added players",PLAYERS.size(),playerList.size());
        assertEquals(playerList.get(0).getUsername(),PLAYERS.get(0).getUsername());
        assertEquals(playerList.get(0).getEmail(),PLAYERS.get(0).getEmail());
        assertEquals(playerList.get(0).getPassword(),PLAYERS.get(0).getPassword());
        assertEquals(playerList.get(0).getGender(),PLAYERS.get(0).getGender());
    }

    @Test
    public void findById() {
        this.addPlayers();
        Optional<Player> player = playerRepository.findById(2L);
        assertTrue("Find by ID should return an already added player",player.isPresent());
        assertEquals(player.get().getUsername(),PLAYERS.get(1).getUsername());
        assertEquals(player.get().getEmail(),PLAYERS.get(1).getEmail());
        assertEquals(player.get().getPassword(),PLAYERS.get(1).getPassword());
        assertEquals(player.get().getGender(),PLAYERS.get(1).getGender());

    }

    @Test
    public void create() throws EntityAlreadyExistsException {
        Player player = playerRepository.create(NEW_USER);
        assertEquals(player.getUsername(),NEW_USER.getUsername());
        assertEquals(player.getEmail(),NEW_USER.getEmail());
        assertEquals(player.getPassword(),NEW_USER.getPassword());
        assertEquals(player.getGender(),NEW_USER.getGender());
        assertTrue("Create should assign an auto-increment ID",1L == player.getId());

    }

    @Test
    public void createFromMemory() throws EntityAlreadyExistsException {
        this.setIds();
        playerRepository.createFromMemory(PLAYERS);
        List<Player> playerList = playerRepository.findAll();
        assertEquals("Find function should return a collection with the size of the number of added players",PLAYERS.size(),playerList.size());
        for (int i = 0; i < playerList.size(); i++) {
            assertEquals(playerList.get(i).getUsername(),PLAYERS.get(i).getUsername());
            assertEquals(playerList.get(i).getEmail(),PLAYERS.get(i).getEmail());
            assertEquals(playerList.get(i).getPassword(),PLAYERS.get(i).getPassword());
            assertEquals(playerList.get(i).getGender(),PLAYERS.get(i).getGender());
        }

    }

    @Test(expected = EntityNotFoundException.class)
    public void updateNonExistingPlayer() throws EntityNotFoundException {
        this.setIds();
        playerRepository.update(PLAYERS.get(0));
    }

    @Test()
    public void updateAnExistingPlayer() throws EntityNotFoundException {
        this.addPlayers();
        this.setIds();
        String newPassword = "New Password";
        PLAYERS.get(0).setPassword(newPassword);
        Player updatedPlayer = playerRepository.update(PLAYERS.get(0));
        assertEquals(newPassword,updatedPlayer.getPassword());
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteNonExistingPlayerById() throws EntityNotFoundException {
        playerRepository.deleteById(1L);
    }

    @Test
    public void deleteAnExistingPlayerById() throws EntityNotFoundException {
        this.addPlayers();
        this.setIds();
        Player deletedPlayer = playerRepository.deleteById(1L);
        assertEquals(PLAYERS.get(0).getUsername(),deletedPlayer.getUsername());
    }

    @Test
    public void updateKeyGenerator() throws EntityAlreadyExistsException {
        Random random = new Random(PLAYERS.size());
        long randomNumber = random.nextLong();
        playerRepository.updateKeyGenerator(randomNumber);
        Player player = playerRepository.create(NEW_USER);
        assertTrue(randomNumber + 1 == player.getId());
    }

    @Test
    public void findByUsernameAndPassword() {
        this.addPlayers();
        Player player = playerRepository.findByUsernameAndPassword(PLAYERS.get(1).getUsername(), PLAYERS.get(1).getPassword());
        assertEquals(PLAYERS.get(1),player);
    }

    @Test
    public void findByUsernameAndPasswordNonMatching() {
        this.addPlayers();
        Player player = playerRepository.findByUsernameAndPassword(PLAYERS.get(0).getUsername(), PLAYERS.get(1).getPassword());
        assertNull(player);
    }

    private void setIds(){
        for (int i = 0; i < PLAYERS.size(); i++) {
            PLAYERS.get(i).setId(i+1L);
        }
    }

    private void addPlayers() {
        for (Player player : this.PLAYERS) {
            try {
                playerRepository.create(player);
            } catch (EntityAlreadyExistsException e) {
                e.printStackTrace();
            }
        }
    }
}
package commands;

import dao.PlayerRepository;
import dao.impl.LongKeyGenerator;
import dao.impl.PlayerRepositoryImpl;
import exception.EntityAlreadyExistsException;
import model.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Scanner;

import static model.Gender.FEMALE;
import static org.junit.Assert.*;

public class LoginCommandTest {
    private InputStream inputStream;
    private PrintStream printStream;
    private final String TEST_USERNAME = "Username";
    private final String TEST_PASSWORD = "Password";
    private String credentials = String.join("\n", TEST_USERNAME, TEST_PASSWORD);
    private ByteArrayOutputStream output = new ByteArrayOutputStream();
    private LoginCommand loginCommand;
    private PlayerRepository playerRepository;

    @Before
    public void setUp() {
        printStream = new PrintStream(output, true);
        inputStream = new ByteArrayInputStream(credentials.getBytes());
        playerRepository = new PlayerRepositoryImpl(new LongKeyGenerator());
    }

    @After
    public void tearDown() throws IOException {
        inputStream.close();
    }

    @Test
    public void loginWithWrongCredentials() {
        loginCommand = new LoginCommand(playerRepository, new Scanner(inputStream), printStream);
        loginCommand.action();
        assertTrue(output.toString().endsWith("Wrong username or password\r\n"));
    }

    @Test
    public void loginWithCorrectCredentials() throws EntityAlreadyExistsException {
        playerRepository.create(new Player(TEST_USERNAME, "", TEST_PASSWORD, FEMALE));
        loginCommand = new LoginCommand(playerRepository, new Scanner(inputStream), printStream);
        loginCommand.action();
        assertTrue(output.toString().endsWith("Welcome " + TEST_USERNAME + "!\r\n"));
    }
}
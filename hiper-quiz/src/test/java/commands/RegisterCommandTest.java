package commands;

import dao.PlayerRepository;
import dao.impl.LongKeyGenerator;
import dao.impl.PlayerRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.Assert.*;

public class RegisterCommandTest {
    private InputStream inputStream;
    private PrintStream printStream;
    private ByteArrayOutputStream output = new ByteArrayOutputStream();
    private RegisterCommand registerCommand;
    private PlayerRepository playerRepository;
    private final String INVALID_USERNAME = "1nv@lidname";
    private final String VALID_USERNAME = "validname";
    private final String INVALID_EMAIL = "invalid_email";
    private final String VALID_EMAIL = "valid@email.com";
    private final String INVALID_PASSWORD = "invalid_password";
    private final String VALID_PASSWORD = "v@lidPa$$w0rd";
    private final String INVALID_GENDER = "unknown";
    private final String VALID_GENDER = "F";
    String credentials = String.join("\n",INVALID_USERNAME,VALID_USERNAME,INVALID_EMAIL,VALID_EMAIL,
            INVALID_PASSWORD,VALID_PASSWORD,INVALID_GENDER,VALID_GENDER,VALID_USERNAME,VALID_PASSWORD);
    String expectedOutput = "Register Page\r\n" +
            "Press enter if you want to go back to the main menu.\r\n" +
            "Username: \r\n" +
            "Username should be 2 to 15 characters long, word characters only. Please enter a valid username\r\n" +
            "Email: \r\n" +
            "Please enter a valid email\r\n" +
            "Password: \r\n" +
            "Password doesn't meet the requirements: 8 to 15 characters long, at least one digit, one capital letter, and one special sign\r\n" +
            "Gender: M/F\r\n" +
            "Gender is not valid. Please enter M or F\r\n" +
            "You have registered successfully\r\n" +
            "Login Page\r\n" +
            "Press enter if you want to go back to the main menu.\r\n" +
            "Username: \r\n" +
            "Password: \r\n" +
            "Welcome validname!\r\n";

    @Before
    public void setUp() throws Exception {
        printStream = new PrintStream(output, true);
        playerRepository = new PlayerRepositoryImpl(new LongKeyGenerator());
    }

    @AfterEach
    public void tearDown() throws Exception {
        inputStream.close();
    }

    @Test
    public void action() {
        inputStream = new ByteArrayInputStream(credentials.getBytes());
        registerCommand = new RegisterCommand(playerRepository,new Scanner(inputStream),printStream);
        registerCommand.action();
        String programOutput = output.toString();
        assertEquals(expectedOutput,programOutput);
    }
}
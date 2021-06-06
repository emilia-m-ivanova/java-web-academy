package commands;

import dao.PlayerRepository;
import exception.EntityAlreadyExistsException;
import model.Gender;
import model.Player;
import util.InputUtils;

import java.io.PrintStream;
import java.util.Scanner;

import static model.Gender.*;

public class RegisterCommand implements Command {
    private PlayerRepository playerRepository;
    private Scanner in;
    private PrintStream out;

    public RegisterCommand(PlayerRepository playerRepository, Scanner in, PrintStream out) {
        this.playerRepository = playerRepository;
        this.in = in;
        this.out = out;
    }

    @Override
    public void action() {
        out.printf("Register Page%nPress enter if you want to go back to the main menu.%nUsername: %n");
        String username = in.nextLine().trim();
        if (username.isEmpty()) {
            return;
        }
        while (!InputUtils.usernameValidator(username)) {
            out.println("Username should be 2 to 15 characters long, word characters only. Please enter a valid username");
            username = in.nextLine().trim();
        }
        out.println("Email: ");
        String email = in.nextLine().trim();
        while (!InputUtils.emailValidator(email)) {
            out.println("Please enter a valid email");
            email = in.nextLine().trim();
        }
        out.println("Password: ");
        String password = in.nextLine().trim();
        while (!InputUtils.passwordValidator(password)) {
            out.println("Password doesn\'t meet the requirements: 8 to 15 characters long, at least one digit, one capital letter, and one special sign");
            password = in.nextLine().trim();
        }
        out.println("Gender: M/F");
        String genderStr = in.nextLine().trim();
        while (!InputUtils.genderValidator(genderStr.toUpperCase())) {
            out.println("Gender is not valid. Please enter M or F");
            genderStr = in.nextLine().trim();
        }
        Gender gender = genderStr == "M"? MALE : FEMALE;
        try {
            playerRepository.create(new Player(username, email, password,gender));
            out.println("You have registered successfully");
            new LoginCommand(playerRepository, in, out).action();
        } catch (EntityAlreadyExistsException e) {
            out.println(e.getMessage());
        }
    }

}

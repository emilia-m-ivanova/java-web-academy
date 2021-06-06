package commands;

import dao.PlayerRepository;
import exception.EntityAlreadyExistsException;
import model.Gender;
import model.Player;
import util.InputUtils;
import java.io.PrintStream;
import java.util.Scanner;

public class RegisterCommand implements Command{
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
        if(username.isEmpty()){
            return;
        }
        while (!InputUtils.usernameValidator(username)){
            out.println("Username should be 2 to 15 characters long, word characters only. Please enter a valid username");
            username = in.nextLine().trim();
        }
        out.println("Email: ");
        String email = in.nextLine().trim();
        while (!InputUtils.emailValidator(email)){
            out.println("Please enter a valid email");
            email = in.nextLine().trim();
        }
        out.println("Password: ");
        String password = in.nextLine().trim();
        //TODO: add password validation
        out.println("Gender: ");
        String gender = in.nextLine().trim();
        while (!InputUtils.genderValidator(gender.toUpperCase())){
            //TODO: fix gender validation
//                    System.out.println("Gender is not valid");
//                    email = in.nextLine().trim();
        }
        try {
            playerRepository.create(new Player(username, email, password, Gender.valueOf(gender.toUpperCase())));
            new LoginCommand(playerRepository,in,out).action();
        } catch (EntityAlreadyExistsException e) {
            out.println(e.getMessage());
        }
    }

}
